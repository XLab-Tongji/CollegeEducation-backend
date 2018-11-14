package org.lab409.controller;

import com.github.pagehelper.PageInfo;
import javafx.util.Pair;
import org.lab409.entity.ResourceComment;
import org.lab409.entity.ResourceEntity;
import org.lab409.entity.ResponseMessage;
import org.lab409.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
    public ResponseMessage uploadResourceController(@RequestParam(name = "resource") MultipartFile uploadResource) {

        Pair<Boolean, String> success = resourceService.uploadResource(uploadResource);
        if (success.getKey()) {
            return new ResponseMessage<>(success.getValue()).success();
        }
        else {
            return new ResponseMessage<>(success.getValue()).error(202, "upload fail");
        }
    }

    @RequestMapping(path = "/uploadResourceMetaData", method = RequestMethod.PUT)
    public ResponseMessage uploadResourceController(@RequestBody ResourceEntity resourceEntity){

        boolean success = resourceService.uploadResourceMetaData(resourceEntity);
        if (success) {
            return new ResponseMessage<String>(null).success();
        }
        else {
            return new ResponseMessage<String>(null).error(202, "upload fail");
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

    @RequestMapping(path = "/searchResource/{resourceMajorID}/{categoryID}/{pageID}", method = RequestMethod.GET)
    public ResponseMessage keywordResourceController(@PathVariable("resourceMajorID") Integer resourceMajorID,
                                                     @PathVariable("categoryID") Integer categoryID,
                                                     @RequestParam("keyword") String keyword,
                                                     @PathVariable("pageID") Integer pageID) {
        Page<ResourceEntity> resourceEntityList = resourceService.keywordSearchPage(keyword, categoryID, resourceMajorID, pageID);
        if(resourceEntityList != null) {
            return new ResponseMessage<>(resourceEntityList).success();
        }
        else {
            return  new ResponseMessage<>(null).error(202,"file not found");
        }
    }

    @RequestMapping(path = "/resource/favourite/like/{resourceID}", method = RequestMethod.POST)
    public ResponseMessage likeFavouriteResourceController(@PathVariable("resourceID") String resourceID) {
       if(resourceService.likeResource(resourceID)) {
           return new ResponseMessage<>(null).success();
       }
       return new ResponseMessage<>(null).error(202, "unknown error");
    }

    @RequestMapping(path = "/resource/favourite/dislike/{resourceID}", method = RequestMethod.DELETE)
    public ResponseMessage unlikeFavouriteResourceController(@PathVariable("resourceID") String resourceID) {
        if(resourceService.dislikeResource(resourceID)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, "unknown error");
    }

    @RequestMapping(path = "/resource/suggest/make/{resourceID}", method = RequestMethod.POST)
    public ResponseMessage suggestResourceController(@PathVariable("resourceID") String resourceID) {
        if(resourceService.suggestResource(resourceID)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, "unknown error");
    }

    @RequestMapping(path = "/resource/suggest/undo/{resourceID}", method = RequestMethod.DELETE)
    public ResponseMessage undoSuggestsResourceController(@PathVariable("resourceID") String resourceID) {
        if(resourceService.undoSuggestResource(resourceID)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, "unknown error");
    }

    @RequestMapping(path = "/resource/comment/make", method = RequestMethod.POST)
    public ResponseMessage commentResourceController(@RequestBody ResourceComment resourceComment) {
        if(resourceService.commentResource(resourceComment)) {
            return new ResponseMessage<>(resourceComment).success();
        }
        return new ResponseMessage<>(null).error(202, "unknown error");
    }

    @RequestMapping(path = "/resource/comment/delete/{commentID}", method = RequestMethod.DELETE)
    public ResponseMessage deleteResourceCommentController(@PathVariable("commentID") Integer commentID) {
        if(resourceService.deleteResourceComment(commentID)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, "unknown error");
    }

    @RequestMapping(path = "/resource/myFavourite/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyFavouriteResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getFavouriteResources(pageID);
        return new ResponseMessage<>(resourceEntities).success();
    }

    @RequestMapping(path = "/resource/myDownload/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyDownloadResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getDownloadResources(pageID);
        return new ResponseMessage<>(resourceEntities).success();
    }

    @RequestMapping(path = "/resource/myUpload/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyUploadResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getUploadResources(pageID);
        return new ResponseMessage<>(resourceEntities).success();
    }

    @RequestMapping(path = "/resource/comments/{resourceID}/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getResourceCommentsController(@PathVariable("resourceID") String resourceID, @PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceComment> resourceComments = resourceService.getResourceComments(pageID, resourceID);
        return new ResponseMessage<>(resourceComments).success();
    }

    @RequestMapping(path = "/resource/myComment/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyCommentedResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceComment> resourceComments = resourceService.getCommentResources(pageID);
        return new ResponseMessage<>(resourceComments).success();
    }

    @RequestMapping(path = "/resource/mySuggest/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMySuggestedResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity>  resourceComments = resourceService.getSuggestedResources(pageID);
        return new ResponseMessage<>(resourceComments).success();
    }
}
