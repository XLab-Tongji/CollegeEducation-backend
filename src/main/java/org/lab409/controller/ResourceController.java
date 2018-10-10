package org.lab409.controller;

import javafx.util.Pair;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public ResponseMessage uploadResourceController(@RequestParam(name = "file") MultipartFile uploadResource,
                                                    @RequestParam(name = "resourceName") String resourceName,
                                                    @RequestParam(name = "description") String description ) {
        Pair<Boolean, String> success = resourceService.uploadResource(uploadResource, resourceName, description);
        if (success.getKey()) {
            return new ResponseMessage<>(success.getValue()).success();
        }
        else {
            return new ResponseMessage<>(success.getValue()).error(202, "upload fail");
        }
    }
}
