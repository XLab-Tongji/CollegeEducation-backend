package org.lab409.service;

import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;

public interface ResourceService {
    Pair<Boolean, String> uploadResource(MultipartFile resource, String resourceName, String description);
    //boolean downloadResource();
    //boolean deleteResource();
}
