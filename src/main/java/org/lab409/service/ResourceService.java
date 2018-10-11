package org.lab409.service;

import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;

public interface ResourceService {
    Pair<Boolean, String> uploadResource(MultipartFile resource, String resourceName, String description);
    Pair<Boolean, GridFsResource> downloadResource(String resourceID);
    int deleteResource(String resourceID);
}
