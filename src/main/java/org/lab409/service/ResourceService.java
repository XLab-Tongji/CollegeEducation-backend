package org.lab409.service;

import org.lab409.entity.ResourceCategoryEntity;
import org.lab409.entity.ResourceEntity;
import org.lab409.entity.ResourceMajorEntity;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;
import java.util.List;

public interface ResourceService {
    Pair<Boolean, String> uploadResource(MultipartFile resource);
    Pair<Boolean, GridFsResource> downloadResource(String resourceID);
    boolean uploadResourceMetaData(ResourceEntity resourceEntity);
    int deleteResource(String resourceID);
    List<ResourceCategoryEntity> getResourceCategories();
    List<ResourceMajorEntity> getResourceMajors();
}
