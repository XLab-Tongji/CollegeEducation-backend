package org.lab409.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.tika.Tika;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.lab409.entity.*;
import org.lab409.mapper.ResourceMapper;
import org.lab409.mapper.UserMapper;
import org.lab409.service.ResourceElasticSearchRepo;
import org.lab409.service.ResourceService;
import org.lab409.util.FormatDateUtil;
import org.lab409.util.MimeTypes;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ResourceElasticSearchRepo resourceElasticSearchRepo;

    private static ConcurrentHashMap<DOCUMENT_SERVICE,ExecutorService> cachedDocThread;
    private static ConcurrentHashMap<RESOURCE_SERVICE, ExecutorService> cachedResourceThread;
    //public static final Tika tika = new Tika();

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
    public AbstractMap.SimpleEntry<String, String> uploadResource(MultipartFile resource) {

        UserEntity currentUser = userUtil.getCurrentUser();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            bufferedInputStream.mark(0);
            String type = tika.detect(bufferedInputStream);
            bufferedInputStream.reset();
            if (!MimeTypes.isResourceValid(type)) {
                return new AbstractMap.SimpleEntry<>("resource type invalid", "");
            }
            BasicDBObject metaData = new BasicDBObject();
            metaData.put("uploader", currentUser.getUserID());
            String info = gridFsTemplate.store(bufferedInputStream,
                    resource.getOriginalFilename(),
                    resource.getContentType(),
                    metaData).toString();
            ResourceEntity resourceEntity = new ResourceEntity(info, currentUser.getUserID());
            resourceEntity.setUploadTime(FormatDateUtil.formatDate(new Date()));
            if(resourceMapper.uploadResource(resourceEntity) == 1 ) {
                return new AbstractMap.SimpleEntry<>(OK, info);
            }
            return new AbstractMap.SimpleEntry<>("upload fail", "");
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
        return new AbstractMap.SimpleEntry<>("upload fail", "");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String uploadResourceMetaData(ResourceEntity resourceEntity) {
        UserEntity currentUser = userUtil.getCurrentUser();
        ResourceEntity dbEntity = resourceMapper.getResourceFromID(resourceEntity.getResourceID());
        if(dbEntity.getUploaderID().equals(currentUser.getUserID())) {
            resourceEntity.setId(dbEntity.getId());
            resourceEntity.setUploaderID(dbEntity.getUploaderID());
            resourceEntity.setUploadTime(dbEntity.getUploadTime());
        }
        else {
            return "you are unauthorized to modify the file";
        }
        if (resourceEntity.getPoints() < MIN_SCORE || resourceEntity.getPoints() > MAX_SCORE) {
            return "wrong resource points, should between 0 and 5";
        }
        String[] tags = resourceEntity.getTags().split(" ");
        if (tags.length > 5) {
            return "too much tags";
        }
        for(String tag: tags) {
            if (tag.length() > 10 ) {
                return "tag " + tag +"to0 long";
            }
        }
        int success = resourceMapper.updateResourceMetaData(resourceEntity);
        if (success == 1) {
            cachedDocThread.get(DOCUMENT_SERVICE.UPDATE_DOC).execute(()->saveResourceDoc(resourceEntity));
            return OK;
        }
        return "Up load fail";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public AbstractMap.SimpleEntry<String, GridFsResource> downloadResource(String resourceID) {
        ResourceEntity resourceEntity = resourceMapper.getResourceFromID(resourceID);
        UserEntity currentUser = userUtil.getCurrentUser();
        Integer delta = resourceEntity.getPoints();
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceID)));
        if (gridFSFile == null) {
            return new AbstractMap.SimpleEntry<>("resource not found", null);
        }
        Integer uploaderID = resourceEntity.getUploaderID();
        String gridFileName = gridFSFile.getFilename();
        GridFsResource[] gridFsResources = gridFsTemplate.getResources(gridFileName);

        for(GridFsResource gridFsResource : gridFsResources ) {
            if (gridFsResource.getId().toString().equals(gridFSFile.getId().toString())){
                if (resourceMapper.isUserDownloadResource(resourceID, currentUser.getUserID()) == 0) {
                    if(delta > currentUser.getPoints()) {
                        return new AbstractMap.SimpleEntry<>("you don't have enough points", null);
                    }
                    int success1 = resourceMapper.updateLeftPoints(-delta, currentUser.getUserID());
                    int success2 = resourceMapper.updateLeftPoints(delta, uploaderID);
                    if(success1 != 1 || success2 != 1) {
                        return new AbstractMap.SimpleEntry<>("user points error", null);
                    }
                }
                cachedResourceThread.get(RESOURCE_SERVICE.UPDATE_RESOURCE).execute(()->{
                    DownloadResourceEntity downloadResourceEntity = new DownloadResourceEntity();
                    downloadResourceEntity.setResourceID(resourceID);
                    downloadResourceEntity.setUserID(currentUser.getUserID());
                    downloadResourceEntity.setId(currentUser.getUserID() + resourceID);
                    downloadResourceEntity.setDownloadTime(FormatDateUtil.formatDate(new Date()));
                    resourceMapper.insertIntoDownloadResource(downloadResourceEntity);
                });
                return new AbstractMap.SimpleEntry<>(OK, gridFsResource);
            }
        }
        return new AbstractMap.SimpleEntry<>("resource not found", null);
    }

    @Override
    @Transactional
    public String deleteResource(String resourceID) {
        UserEntity userEntity = userUtil.getCurrentUser();
        Query query = new Query(Criteria.where("_id").is(resourceID));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (gridFSFile == null) {
            return "resource not found";
        }
        if (!gridFSFile.getMetadata().get("uploader").equals(userEntity.getUserID())) {
            return "you are unauthorized to delete the resource";
        }
        gridFsTemplate.delete(query);
        int a = resourceMapper.deleteResource(resourceID);
        if(a != 1) {
            return "delete fail";
        }
        cachedDocThread.get(DOCUMENT_SERVICE.DELETE_DOC).execute(()->deleteResourceDoc(resourceID));
        return OK;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String likeResource(String resourceID) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        FavouriteResourceEntity favouriteResourceEntity = new FavouriteResourceEntity();
        favouriteResourceEntity.setResourceID(resourceID);
        favouriteResourceEntity.setUserID(currentUserID);
        favouriteResourceEntity.setId(currentUserID + resourceID);
        favouriteResourceEntity.setFavouriteTime(FormatDateUtil.formatDate(new Date()));
        int success = resourceMapper.insertIntoFavouriteResource(favouriteResourceEntity);
        return success == 1? OK:"add to favourite fail";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String dislikeResource(String resourceID) {
        int success = resourceMapper.deleteFavouriteResource(resourceID, userUtil.getCurrentUser().getUserID());
        return success == 1? OK:"remove from favourite fail";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String suggestResource(String resourceID, int suggested) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        SuggestedResource suggestedResource = new SuggestedResource();
        suggestedResource.setId(currentUserID + resourceID);
        suggestedResource.setResourceID(resourceID);
        suggestedResource.setUserID(currentUserID);
        suggestedResource.setSuggestTime(FormatDateUtil.formatDate(new Date()));
        suggestedResource.setSuggested(suggested);
        int success = resourceMapper.insertIntoSuggestedResourceAndSuggest(suggestedResource);
        return success > 0? OK:"suggest resource fail";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String undoSuggestResource(String resourceID) {
        int success = resourceMapper.deleteSuggestedResource(resourceID, userUtil.getCurrentUser().getUserID());
        return success == 1? OK:"undo suggest resource fail";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String commentResource(ResourceComment resourceComment) {
        if (resourceComment.getScore() < MIN_SCORE || resourceComment.getScore() > MAX_SCORE) {
            return "score wrong, should between 0 and 5";
        }
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        resourceComment.setUserID(currentUserID);
        resourceComment.setCommentTime(FormatDateUtil.formatDate(new Date()));
        int success = resourceMapper.insertIntoResourceComment(resourceComment);
        return success == 1? OK:"comment fail";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String deleteResourceComment(Integer commentID) {
        Integer currentUserID = userUtil.getCurrentUser().getUserID();
        int success = resourceMapper.deleteResourceComment(commentID, currentUserID);
        return success == 1? OK: "delete comment fail";
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
    public ResourceDetail getResourceDetail(String resourceID) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<Object>> callableList = new ArrayList<>();

        final AtomicReference<ResourceDetail.UploaderInfo> uploaderInfo = new AtomicReference<>();
        final AtomicReference<ResourceDetail.CommentInfo> commentInfo = new AtomicReference<>();
        final AtomicReference<ResourceDetail.SuggestInfo> suggestInfo = new AtomicReference<>();

        final AtomicReference<Integer> downloadTimes = new AtomicReference<>();
        final AtomicReference<Integer> favouriteNum = new AtomicReference<>();
        final AtomicReference<Double> uploaderAvgScore = new AtomicReference<>();
        final AtomicReference<Double> uploaderSuggestedRate = new AtomicReference<>();

        callableList.add(Executors.callable(()->{
            ResourceDetail.UploaderInfo uploaderInfo1 = resourceMapper.getResourceUploaderInfo(resourceID);
            uploaderInfo1.setResourceID(resourceID);
            uploaderInfo.set(uploaderInfo1);
            uploaderAvgScore.set(resourceMapper.getMyResourceAvgScore(uploaderInfo1.getUploaderID()));
            uploaderSuggestedRate.set(resourceMapper.getMyResourceSuggestedRate(uploaderInfo1.getUploaderID()));
        }));
        callableList.add(Executors.callable(()->{
            ResourceDetail.CommentInfo commentInfo1 = resourceMapper.getResourceCommentInfo(resourceID);
            commentInfo.set(commentInfo1);
        }));
        callableList.add(Executors.callable(()->{
            ResourceDetail.SuggestInfo suggestInfo1 = resourceMapper.getResourceSuggestedInfo(resourceID);
            suggestInfo.set(suggestInfo1);
        }));
        callableList.add(Executors.callable(()->{
            downloadTimes.set(resourceMapper.getDownloadTimes(resourceID));
        }));
        callableList.add(Executors.callable(()->{
            favouriteNum.set(resourceMapper.getFavouriteNum(resourceID));
        }));

        try {
            executorService.invokeAll(callableList);
            return new ResourceDetail(uploaderInfo.get(), suggestInfo.get(),
                    commentInfo.get(),downloadTimes.get(), favouriteNum.get(),
                    uploaderAvgScore.get(),uploaderSuggestedRate.get());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserDetail getUserDetail(Integer userID) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<Object>> callableList = new ArrayList<>();
        final AtomicReference<String> username = new AtomicReference<>();
        final AtomicReference<Integer> downloadNum = new AtomicReference<>();
        final AtomicReference<Integer> favouriteNum = new AtomicReference<>();
        final AtomicReference<Integer> suggestedNum = new AtomicReference<>();
        final AtomicReference<Integer> uploadNum = new AtomicReference<>();
        final AtomicReference<Double> myResourceAvgScore = new AtomicReference<>();
        final AtomicReference<Double> myResourceSuggestedRate = new AtomicReference<>();
        callableList.add(Executors.callable(()->{
            username.set(userMapper.getUserNameByID(userID).getUsername());
        }));
        callableList.add(Executors.callable(()->{
            favouriteNum.set(resourceMapper.getMyFavouriteNum(userID));
        }));
        callableList.add(Executors.callable(()->{
            downloadNum.set(resourceMapper.getMyDownloadNum(userID));
        }));
        callableList.add(Executors.callable(()->{
            suggestedNum.set(resourceMapper.getMySuggestedNum(userID));
        }));
        callableList.add(Executors.callable(()->{
            uploadNum.set(resourceMapper.getMyUploadNum(userID));
        }));
        callableList.add(Executors.callable(()->{
            myResourceAvgScore.set(resourceMapper.getMyResourceAvgScore(userID));
        }));
        callableList.add(Executors.callable(()->{
            myResourceSuggestedRate.set(resourceMapper.getMyResourceSuggestedRate(userID));
        }));
        try {
            executorService.invokeAll(callableList);
            return new UserDetail(userID, username.get(),uploadNum.get(), downloadNum.get(), favouriteNum.get(),
                    suggestedNum.get(), myResourceAvgScore.get(), myResourceSuggestedRate.get());
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserDetail getMyDetail() {
        Integer userID = userUtil.getCurrentUser().getUserID();
        return getUserDetail(userID);
    }

    public Map<String, Integer> getUserHistoryOnResource(String resourceID) {
        Integer userID = userUtil.getCurrentUser().getUserID();
        Map<String, Integer> map = new HashMap<>();
        ExecutorService executorService = Executors.newCachedThreadPool();
        List<Callable<Object>> callableList = new ArrayList<>();

        final AtomicReference<Integer> favourite = new AtomicReference<>();
        final AtomicReference<Integer> suggested = new AtomicReference<>();

        callableList.add(Executors.callable(()->{
            favourite.set(resourceMapper.isUserFavouriteResource(resourceID, userID));
        }));
        callableList.add(Executors.callable(()->{
            SuggestedResource suggestedResource = resourceMapper.isUserSuggestedResource(resourceID, userID);
            suggested.set(suggestedResource == null? 0:suggestedResource.getSuggested());
        }));
        try {
            executorService.invokeAll(callableList);
            map.put("favourite", favourite.get());
            map.put("suggested", suggested.get());
            return map;
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
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
                .should(QueryBuilders.matchQuery("tags", keyword).fuzziness(Fuzziness.AUTO))
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
        return elasticsearchTemplate.queryForPage(nativeSearchQuery, ResourceEntity.class);
    }

    @Override
    public PageInfo<ResourceEntity> relativeRecommend(Integer pageID, Integer categoryID, Integer resourceMajorID) {
        PageHelper.startPage(pageID, PAGE_SIZE);
        List<ResourceEntity> resourceEntities = resourceMapper.getRecommendResourceList(categoryID, resourceMajorID);
        return new PageInfo<>(resourceEntities);
    }
}
