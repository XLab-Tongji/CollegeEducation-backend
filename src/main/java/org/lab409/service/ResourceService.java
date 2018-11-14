package org.lab409.service;

import org.springframework.data.domain.Page;
import com.github.pagehelper.PageInfo;
import org.lab409.entity.ResourceCategoryEntity;
import org.lab409.entity.ResourceComment;
import org.lab409.entity.ResourceEntity;
import org.lab409.entity.ResourceMajorEntity;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;

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

    Pair<Boolean, String> uploadResource(MultipartFile resource);
    Pair<Boolean, GridFsResource> downloadResource(String resourceID);
    boolean uploadResourceMetaData(ResourceEntity resourceEntity);
    int deleteResource(String resourceID);
    List<ResourceCategoryEntity> getResourceCategories();
    List<ResourceMajorEntity> getResourceMajors();
    void saveResourceDoc(ResourceEntity resourceEntity);
    void deleteResourceDoc(String resourceID);
    PageInfo<ResourceEntity> getFavouriteResources(Integer pageID);
    PageInfo<ResourceEntity> getDownloadResources(Integer pageID);
    PageInfo<ResourceEntity> getUploadResources(Integer pageID);
    PageInfo<ResourceEntity> getSuggestedResources(Integer pageID);
    PageInfo<ResourceComment> getResourceComments(Integer pageID, String resourceID);
    PageInfo<ResourceComment> getCommentResources(Integer pageID);
    boolean likeResource(String resourceID);
    boolean dislikeResource(String resourceID);
    boolean suggestResource(String resourceID);
    boolean undoSuggestResource(String resourceID);
    boolean commentResource(ResourceComment resourceComment);
    boolean deleteResourceComment(Integer commentID);
    Page<ResourceEntity> keywordSearchPage(String keyword, Integer categoryID, Integer resourceMajorID, Integer pageID);
}
