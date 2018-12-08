package org.lab409.service;

import org.apache.tika.Tika;
import org.lab409.entity.*;
import org.springframework.data.domain.Page;
import com.github.pagehelper.PageInfo;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;
import java.util.AbstractMap;
import java.util.List;

public interface ResourceService {
    enum  DOCUMENT_SERVICE {
        DELETE_DOC,
        UPDATE_DOC,
        SEARCH_DOC
    }
    enum RESOURCE_SERVICE {
        DELETE_RESOURCE,
        UPDATE_RESOURCE,
        CREATE_RESOURCE,
        SEARCH_RESOURCE
    }
    //for test paging
    int PAGE_SIZE = 3;
    int MIN_SCORE = 0;
    int MAX_SCORE = 5;

    String OK = "OK";

    Tika tika = new Tika();

    AbstractMap.SimpleEntry<String, String> uploadResource(MultipartFile resource);
    AbstractMap.SimpleEntry<String, GridFsResource> downloadResource(String resourceID);
    String uploadResourceMetaData(ResourceEntity resourceEntity);
    String deleteResource(String resourceID);
    List<ResourceCategoryEntity> getResourceCategories();
    List<ResourceMajorEntity> getResourceMajors();
    void saveResourceDoc(ResourceEntity resourceEntity);
    void saveAllDoc(List<ResourceEntity> resourceEntities);
    void deleteResourceDoc(String resourceID);
    PageInfo<ResourceEntity> getFavouriteResources(Integer pageID);
    PageInfo<ResourceEntity> getDownloadResources(Integer pageID);
    PageInfo<ResourceEntity> getUploadResources(Integer pageID);
    PageInfo<ResourceEntity> getSuggestedResources(Integer pageID);
    PageInfo<ResourceComment> getResourceComments(Integer pageID, String resourceID);
    PageInfo<ResourceComment> getCommentResources(Integer pageID);
    ResourceDetail getResourceDetail(String resourceID);
    UserDetail getUserDetail(Integer userID);
    UserDetail getMyDetail();
    Map<String,Integer> getUserHistoryOnResource(String resourceID);
    String likeResource(String resourceID);
    String dislikeResource(String resourceID);
    String suggestResource(String resourceID, int suggested);
    String undoSuggestResource(String resourceID);
    String commentResource(ResourceComment resourceComment);
    String deleteResourceComment(Integer commentID);
    Page<ResourceEntity> keywordSearchOnTime(String keyword, Integer categoryID, Integer resourceMajorID, Integer pageID);
    PageInfo<ResourceEntity> keywordSearchOnScore(String keyword, Integer categoryID, Integer resourceMajorID, Integer pageID);
    PageInfo<ResourceEntity> relativeRecommend(Integer pageID, Integer categoryID, Integer resourceMajorID);
}
