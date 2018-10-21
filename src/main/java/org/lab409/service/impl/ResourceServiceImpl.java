package org.lab409.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.oracle.tools.packager.Log;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.lab409.entity.ResourceCategoryEntity;
import org.lab409.entity.ResourceEntity;
import org.lab409.entity.ResourceMajorEntity;
import org.lab409.entity.UserEntity;
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
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
    private FormatDateUtil formatDateUtil;

    public static final int PAGE_SIZE = 10;

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
            resourceEntity.setUploadTime(formatDateUtil.formatDate(new Date()));
            if(resourceMapper.uploadResource(resourceEntity) == 1 ) {
                return new Pair<>(true, info);
            }
            return new Pair<>(false, "");
        }
        catch (IOException e) {
            Log.debug("inputStream get error" + e.getMessage());
            e.printStackTrace();
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    Log.debug("inputStream close error:" + e.getMessage());
                }
            }
        }
        return new Pair<>(false, "");
    }

    @Override
    public boolean uploadResourceMetaData(ResourceEntity resourceEntity) {
        UserEntity currentUser = userUtil.getCurrentUser();
        ResourceEntity dbEntity = resourceMapper.getResourceFromID(resourceEntity.getResourceID());
        if(dbEntity.getUploaderID().equals(currentUser.getUserID())) {
            resourceEntity.setId(dbEntity.getId());
            resourceEntity.setUploaderID(dbEntity.getUploaderID());
            resourceEntity.setUploadTime(dbEntity.getUploadTime());
            resourceEntity.setDownloadTimes(dbEntity.getDownloadTimes());
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
    public Pair<Boolean, GridFsResource> downloadResource(String resourceID) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceID)));
        if (gridFSFile == null) {
            return new Pair<>(false, null);
        }
        String gridFileName = gridFSFile.getFilename();
        GridFsResource[] gridFsResources = gridFsTemplate.getResources(gridFileName);
        for(GridFsResource gridFsResource : gridFsResources ) {
            if (gridFsResource.getId().toString().equals(gridFSFile.getId().toString())){
                cachedResourceThread.get(RESOURCE_SERVICE.UPDATE_RESOURCE).execute(()->resourceMapper.increaseDownloadTimes(resourceID));
                cachedDocThread.get(DOCUMENT_SERVICE.UPDATE_DOC).execute(()->{
                    ResourceEntity resourceEntity = resourceMapper.getResourceFromID(resourceID);
                    resourceEntity.setDownloadTimes(resourceEntity.getDownloadTimes() + 1);
                    saveResourceDoc(resourceEntity);
                });
                return new Pair<>(true, gridFsResource);
            }
        }
        return new Pair<>(false, null);
    }

    /*
    -1: can't find file
    -2: no authority
     0: ok
    */
    @Override

    public int deleteResource(String resourceID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        Query query = new Query(Criteria.where("_id").is(resourceID));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (gridFSFile == null) {
            return -1;
        }
        if (gridFSFile.getMetadata().get("uploader") != userEntity.getUserID() ) {
            return -2;
        }
        gridFsTemplate.delete(query);
        resourceMapper.deleteResource(resourceID);
        cachedDocThread.get(DOCUMENT_SERVICE.DELETE_DOC).execute(()->deleteResourceDoc(resourceID));
        return 0;
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
    public List<ResourceEntity> keywordSearchPage(String keyword, Integer categoryID, Integer resourceMajorID, Integer pageID) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.should(QueryBuilders.matchQuery("resourceName", keyword).fuzziness(Fuzziness.AUTO))
                .should(QueryBuilders.matchQuery("description", keyword).fuzziness(Fuzziness.AUTO))
                .minimumShouldMatch(1)
                .must(QueryBuilders.termQuery("categoryID", categoryID))
                .must(QueryBuilders.termQuery("resourceMajorID", resourceMajorID));

        FunctionScoreQueryBuilder functionScoreQueryBuilder = QueryBuilders.functionScoreQuery(boolQueryBuilder).setMinScore(2);
        PageRequest pageRequest = PageRequest.of(pageID, PAGE_SIZE);
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder()
                .withQuery(functionScoreQueryBuilder)
                .withPageable(pageRequest);
        NativeSearchQuery nativeSearchQuery = nativeSearchQueryBuilder.build();
        Page<ResourceEntity> resourceEntityPage = elasticsearchTemplate.queryForPage(nativeSearchQuery, ResourceEntity.class);
        List<ResourceEntity> resourceEntities = new ArrayList<>();
        for(ResourceEntity resourceEntity: resourceEntityPage) {
            resourceEntities.add(resourceEntity);
        }
        return resourceEntities;
    }
}
