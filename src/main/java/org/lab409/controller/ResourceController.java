package org.lab409.controller;

import javafx.util.Pair;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(path = "/uploadResource", method = RequestMethod.POST)
    public ResponseMessage uploadResourceController(@RequestParam(name = "resource") MultipartFile uploadResource,
                                                    @RequestParam(name = "resourceName") String resourceName,
                                                    @RequestParam(name = "description") String description,
                                                    @RequestParam(name = "categoryID") Integer categoryID,
                                                    @RequestParam(name = "resourceMajorID") Integer resourceMajorID) {
        Pair<Boolean, String> success = resourceService.uploadResource(uploadResource,
                                                                        resourceName,
                                                                        description,
                                                                        categoryID,
                                                                        resourceMajorID);
        if (success.getKey()) {
            return new ResponseMessage<>(success.getValue()).success();
        }
        else {
            return new ResponseMessage<>(success.getValue()).error(202, "upload fail");
        }
    }

    @RequestMapping(path = "/downloadResource/{resourceID}", method = RequestMethod.GET)
    public ResponseEntity downloadResourceController(@PathVariable("resourceID") String resourceID) {
        Pair<Boolean, GridFsResource> success = resourceService.downloadResource(resourceID);
        if (success.getKey()) {
            GridFsResource gridFsResource = success.getValue();
            try {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentDispositionFormData("attachment", gridFsResource.getFilename());
                return ResponseEntity.ok()
                        .contentType(MediaType.parseMediaType(gridFsResource.getContentType()))
                        .contentLength(gridFsResource.contentLength())
                        .headers(headers)
                        .body(gridFsResource);

            }
            catch (IOException e) {
                return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        else {
            return new ResponseEntity((HttpStatus.NOT_FOUND));
        }
    }

    @RequestMapping(path = "/deleteResource/{resourceID}", method = RequestMethod.DELETE)
    public ResponseMessage deleteResourceController(@PathVariable("resourceID") String resourceID) {
        int success = resourceService.deleteResource(resourceID);
        if (success == -1) {
            return new ResponseMessage<>(null).error(202, "file not found");
        }
        else if (success == -2) {
            return new ResponseMessage<>(null).error(401, "unauthorized deletion");
        }
        else {
            return new ResponseMessage<>(null).success();
        }
    }

    @RequestMapping(path = "/resourceCategories", method = RequestMethod.GET)
    public ResponseMessage resourceCategoriesController() {
        return new ResponseMessage<>(resourceService.getResourceCategories()).success();
    }

    @RequestMapping(path = "/resourceMajors", method = RequestMethod.GET)
    public ResponseMessage resourceMajorsController() {
        return new ResponseMessage<>(resourceService.getResourceMajors()).success();
    }
}
