package org.lab409.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.lab409.entity.*;
import org.lab409.mapper.ResourceMapper;
import org.lab409.service.ResourceElasticSearchRepo;
import org.lab409.service.ResourceService;
import org.lab409.util.FormatDateUtil;
import org.lab409.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ResourceElasticSearchRepo resourceElasticSearchRepo;

    private static ConcurrentHashMap<DOCUMENT_SERVICE,ExecutorService> cachedDocThread;
    private static ConcurrentHashMap<RESOURCE_SERVICE, ExecutorService> cachedResourceThread;

    static {
        cachedDocThread = new ConcurrentHashMap<>();
        cachedResourceThread = new ConcurrentHashMap<>();
        cachedDocThread.put(DOCUMENT_SERVICE.UPDATE_DOC, Executors.newCachedThreadPool());
        cachedDocThread.put(DOCUMENT_SERVICE.DELETE_DOC, Executors.newCachedThreadPool());
        cachedResourceThread.put(RESOURCE_SERVICE.UPDATE_RESOURCE, Executors.newCachedThreadPool());
        cachedResourceThread.put(RESOURCE_SERVICE.SEARCH_RESOURCE, Executors.newCachedThreadPool());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<Boolean, String> uploadResource(MultipartFile resource) {

        UserEntity currentUser = userUtil.getCurrentUser();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            BasicDBObject metaData = new BasicDBObject();
            metaData.put("uploader", currentUser.getUserID());
            String info = gridFsTemplate.store(inputStream,
                    resource.getOriginalFilename(),
                    resource.getContentType(),
                    metaData).toString();
            ResourceEntity resourceEntity = new ResourceEntity(info, currentUser.getUserID());
            resourceEntity.setUploadTime(FormatDateUtil.formatDate(new Date()));
            if(resourceMapper.uploadResource(resourceEntity) == 1 ) {
                return new Pair<>(true, info);
            }
            return new Pair<>(false, "");
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return new Pair<>(false, "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean uploadResourceMetaData(ResourceEntity resourceEntity) {
        UserEntity currentUser = userUtil.getCurrentUser();
        ResourceEntity dbEntity = resourceMapper.getResourceFromID(resourceEntity.getResourceID());
        if(dbEntity.getUploaderID().equals(currentUser.getUserID())) {
            resourceEntity.setId(dbEntity.getId());
            resourceEntity.setUploaderID(dbEntity.getUploaderID());
            resourceEntity.setUploadTime(dbEntity.getUploadTime());
        }
        else {
            return false;
        }
        int success = resourceMapper.updateResourceMetaData(resourceEntity);
        if (success == 1) {
            cachedDocThread.get(DOCUMENT_SERVICE.UPDATE_DOC).execute(()->saveResourceDoc(resourceEntity));
            return true;
        }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Pair<Boolean, GridFsResource> downloadResource(String resourceID) {
        ResourceEntity resourceEntity = resourceMapper.getResourceFromID(resourceID);
        UserEntity currentUser = userUtil.getCurrentUser();
        Integer delta = resourceEntity.getPoints();
        if(delta > currentUser.getPoints()) {
            return new Pair<>(false, null);
        }
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceID)));
        if (gridFSFile == null) {
            return new Pair<>(false, null);
        }
        Integer uploaderID = resourceEntity.getUploaderID();
        String gridFileName = gridFSFile.getFilename();
        GridFsResource[] gridFsResources = gridFsTemplate.getResources(gridFileName);

        for(GridFsResource gridFsResource : gridFsResources ) {
            if (gridFsResource.getId().toString().equals(gridFSFile.getId().toString())){
                int success1 = resourceMapper.updateLeftPoints(-delta, currentUser.getUserID());
                int success2 = resourceMapper.updateLeftPoints(delta, uploaderID);
                if(success1 != 1 || success2 != 1) {
                    return new Pair<>(false, null);
                }
                cachedResourceThread.get(RESOURCE_SERVICE.UPDATE_RESOURCE).execute(()->{
                    DownloadResourceEntity downloadResourceEntity = new DownloadResourceEntity();
                    downloadResourceEntity.setResourceID(resourceID);
                    downloadResourceEntity.setUserID(currentUser.getUserID());
                    downloadResourceEntity.setId(currentUser.getUserID() + resourceID);
                    resourceMapper.insertIntoDownloadResource(downloadResourceEntity);
                });
                return new Pair<>(true, gridFsResource);
            }
        }
        return new Pair<>(false, null);
    }

    /*
    -1: can't find file
    -2: no authority
    -3: db error
     0: ok
    delete on cascade
    */
    @Override
    @Transactional
    public int deleteResource(String resourceID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        Query query = new Query(Criteria.where("_id").is(resourceID));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (gridFSFile == null) {
            return -1;
        }
        if (!gridFSFile.getMetadata().get("uploader").equals(userEntity.getUserID())) {
            return -2;
        }
        gridFsTemplate.delete(query);
        int a = resourceMapper.deleteResource(resourceID);
        if(a != 1) {
            return -3;
        }
        cachedDocThread.get(DOCUMENT_SERVICE.DELETE_DOC).execute(()->deleteResourceDoc(resourceID));
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean likeResource(String resourceID) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        FavouriteResourceEntity favouriteResourceEntity = new FavouriteResourceEntity();
        favouriteResourceEntity.setResourceID(resourceID);
        favouriteResourceEntity.setUserID(currentUserID);
        favouriteResourceEntity.setId(currentUserID + resourceID);
        int success = resourceMapper.insertIntoFavouriteResource(favouriteResourceEntity);
        return success == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean dislikeResource(String resourceID) {
        int success = resourceMapper.deleteFavouriteResource(resourceID, userUtil.getCurrentUser().getUserID());
        return success == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean suggestResource(String resourceID) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        SuggestedResource suggestedResource = new SuggestedResource();
        suggestedResource.setId(currentUserID + resourceID);
        suggestedResource.setResourceID(resourceID);
        suggestedResource.setUserID(currentUserID);
        int success = resourceMapper.insertIntoSuggestedResource(suggestedResource);
        return success == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean undoSuggestResource(String resourceID) {
        int success = resourceMapper.deleteSuggestedResource(resourceID, userUtil.getCurrentUser().getUserID());
        return success == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean commentResource(ResourceComment resourceComment) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        resourceComment.setUserID(currentUserID);
        resourceComment.setCommentTime(FormatDateUtil.formatDate(new Date()));
        int success = resourceMapper.insertIntoResourceComment(resourceComment);
        return success == 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteResourceComment(Integer commentID) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        int success = resourceMapper.deleteResourceComment(commentID, currentUserID);
        return success == 1;
    }

    @Override
    public List<ResourceCategoryEntity> getResourceCategories() {
        return resourceMapper.getResourceCategories();
    }

    @Override
    public List<ResourceMajorEntity> getResourceMajors() {
        return resourceMapper.getResourceMajors();
    }

    @Override
    public PageInfo<ResourceEntity> getDownloadResources(Integer pageID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceEntity> resourceEntities = resourceMapper.getDownloadResourceList(userEntity.getUserID());
        return new PageInfo<>(resourceEntities);
    }

    @Override
    public PageInfo<ResourceEntity> getFavouriteResources(Integer pageID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceEntity> resourceEntities = resourceMapper.getFavouriteResourceList(userEntity.getUserID());
       return new PageInfo<>(resourceEntities);
    }

    @Override
    public PageInfo<ResourceEntity> getUploadResources(Integer pageID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceEntity> resourceEntities = resourceMapper.getUploadResourceList(userEntity.getUserID());
        return new PageInfo<>(resourceEntities);
    }

    @Override
    public PageInfo<ResourceEntity> getSuggestedResources(Integer pageID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceEntity> resourceEntities = resourceMapper.getSuggestedResourceList(userEntity.getUserID());
        return new PageInfo<>(resourceEntities);
    }

    @Override
    public PageInfo<ResourceComment> getResourceComments(Integer pageID, String resourceID) {
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceComment> resourceComments = resourceMapper.getResourceCommentList(resourceID);
        return new PageInfo<>(resourceComments);
    }

    @Override
    public PageInfo<ResourceComment> getCommentResources(Integer pageID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceComment> resourceComments = resourceMapper.getMyResourceCommentList(userEntity.getUserID());
        return new PageInfo<>(resourceComments);
    }

    @Override
    public void saveResourceDoc(ResourceEntity resourceEntity) {
        resourceElasticSearchRepo.save(resourceEntity);
    }

    @Override
    public void deleteResourceDoc(String resourceID) {
        BoolQueryBuilder builder = QueryBuilders.boolQuery();
        builder.must(QueryBuilders.termQuery("resourceID", resourceID));
        resourceElasticSearchRepo.deleteAll(resourceElasticSearchRepo.search(builder));
    }

    @Override
    public Page<ResourceEntity> keywordSearchPage(String keyword, Integer categoryID, Integer resourceMajorID, Integer pageID) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("resourceName", keyword).fuzziness(Fuzziness.AUTO))
                .should(QueryBuilders.matchQuery("description", keyword).fuzziness(Fuzziness.AUTO))
                .must(QueryBuilders.termQuery("categoryID", categoryID))
                .must(QueryBuilders.termQuery("resourceMajorID", resourceMajorID));
        if(keyword.equals("")) {
            boolQueryBuilder.minimumShouldMatch(0);
        }
        else {
            boolQueryBuilder.minimumShouldMatch(1);
        }
        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder).setMinScore(2);
        PageRequest pageRequest = PageRequest.of(pageID, PAGE_SIZE);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(functionScoreQueryBuilder)
                .withPageable(pageRequest);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        Page<ResourceEntity> resourceEntities = elasticsearchTemplate.queryForPage(nativeSearchQuery, ResourceEntity.class);
        return resourceEntities;
    }
}
