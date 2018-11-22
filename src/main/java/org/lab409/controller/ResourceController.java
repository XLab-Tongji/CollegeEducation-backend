package org.lab409.controller;

import com.github.pagehelper.PageInfo;
import org.lab409.entity.*;
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
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;

@RestController
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @RequestMapping(path = "/uploadResource", method = RequestMethod.POST)
    public ResponseMessage uploadResourceController(@RequestParam(name = "resource") MultipartFile uploadResource) {

        AbstractMap.SimpleEntry<String, String> success = resourceService.uploadResource(uploadResource);
        if (success.getKey().equals(resourceService.OK)) {
            return new ResponseMessage<>(success.getValue()).success();
        }
        else {
            return new ResponseMessage<>(success.getValue()).error(202, success.getValue());
        }
    }

    @RequestMapping(path = "/uploadResourceMetaData", method = RequestMethod.PUT)
    public ResponseMessage uploadResourceController(@RequestBody ResourceEntity resourceEntity){

        String success = resourceService.uploadResourceMetaData(resourceEntity);
        if (success.equals(resourceService.OK)) {
            return new ResponseMessage<String>(null).success();
        }
        else {
            return new ResponseMessage<String>(null).error(202, success);
        }
    }

    @RequestMapping(path = "/downloadResource/{resourceID}", method = RequestMethod.GET)
    public ResponseEntity downloadResourceController(@PathVariable("resourceID") String resourceID) {
        AbstractMap.SimpleEntry<String, GridFsResource> success = resourceService.downloadResource(resourceID);
        if (success.getKey().equals(resourceService.OK)) {
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
        String success = resourceService.deleteResource(resourceID);
        if (success.equals(resourceService.OK)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null ).error(202, success);
    }

    @RequestMapping(path = "/resourceCategories", method = RequestMethod.GET)
    public ResponseMessage resourceCategoriesController() {
        List<ResourceCategoryEntity> resourceCategoryEntities = resourceService.getResourceCategories();
        return resourceCategoryEntities != null? new ResponseMessage<>(resourceCategoryEntities).success():
                new ResponseMessage<>(null).error(202, "can't get category");
    }

    @RequestMapping(path = "/resourceMajors", method = RequestMethod.GET)
    public ResponseMessage resourceMajorsController() {
        List<ResourceMajorEntity> resourceMajorEntities = resourceService.getResourceMajors();
        return resourceMajorEntities != null? new ResponseMessage<>(resourceMajorEntities).success():
                new ResponseMessage<>(null).error(202,"can't get majors");
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
            return new ResponseMessage<>(null).error(202,"file not found");
        }
    }

    @RequestMapping(path = "/resource/recommend/{resourceMajorID}/{categoryID}/{pageID}", method = RequestMethod.GET)
    public ResponseMessage recommendResourceController(@PathVariable("categoryID") Integer categoryID,
                                                       @PathVariable("resourceMajorID") Integer resourceMajorID,
                                                       @PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.relativeRecommend(pageID, categoryID, resourceMajorID);
        return resourceEntities != null? new ResponseMessage<>(resourceEntities).success():
                new ResponseMessage<>(null).error(202, "recommend fail");
    }

    @RequestMapping(path = "/resource/favourite/like/{resourceID}", method = RequestMethod.POST)
    public ResponseMessage likeFavouriteResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.likeResource(resourceID);
       if(success.equals(ResourceService.OK)) {
           return new ResponseMessage<>(null).success();
       }
       return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/favourite/dislike/{resourceID}", method = RequestMethod.DELETE)
    public ResponseMessage unlikeFavouriteResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.dislikeResource(resourceID);
        if(success.equals(ResourceService.OK)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/suggest/make/{resourceID}", method = RequestMethod.POST)
    public ResponseMessage suggestResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.suggestResource(resourceID, 1);
        if(success.equals(ResourceService.OK)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/suggest/dislike/{resourceID}", method = RequestMethod.POST)
    public ResponseMessage disSuggestResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.suggestResource(resourceID, 0);
        if(success.equals(ResourceService.OK)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/suggest/undo/{resourceID}", method = RequestMethod.DELETE)
    public ResponseMessage undoSuggestsResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.undoSuggestResource(resourceID);
        if(success.equals(ResourceService.OK)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/comment/make", method = RequestMethod.POST)
    public ResponseMessage commentResourceController(@RequestBody ResourceComment resourceComment) {
        String success = resourceService.commentResource(resourceComment);
        if(success.equals(ResourceService.OK)) {
            return new ResponseMessage<>(resourceComment).success();
        }
        return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/comment/delete/{commentID}", method = RequestMethod.DELETE)
    public ResponseMessage deleteResourceCommentController(@PathVariable("commentID") Integer commentID) {
        String success = resourceService.deleteResourceComment(commentID);
        if(success.equals(resourceService.OK)) {
            return new ResponseMessage<>(null).success();
        }
        return new ResponseMessage<>(null).error(202, success);
    }

    @RequestMapping(path = "/resource/myFavourite/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyFavouriteResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getFavouriteResources(pageID);
        return resourceEntities != null? new ResponseMessage<>(resourceEntities).success():
                new ResponseMessage<>(null).error(202, "can't get my favourite resource");
    }

    @RequestMapping(path = "/resource/myDownload/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyDownloadResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getDownloadResources(pageID);
        return resourceEntities != null ? new ResponseMessage<>(resourceEntities).success():
                new ResponseMessage<>(null).error(202, "can't get my download resource");
    }

    @RequestMapping(path = "/resource/myUpload/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyUploadResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getUploadResources(pageID);
        return resourceEntities != null ? new ResponseMessage<>(resourceEntities).success():
                new ResponseMessage<>(null).error(202, "can't get my upload resource");
    }

    @RequestMapping(path = "/resource/comments/{resourceID}/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getResourceCommentsController(@PathVariable("resourceID") String resourceID, @PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceComment> resourceComments = resourceService.getResourceComments(pageID, resourceID);
        return resourceComments != null ? new ResponseMessage<>(resourceComments).success():
                new ResponseMessage<>(null).error(202, "can't get resource comment");
    }

    @RequestMapping(path = "/resource/myComment/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMyCommentedResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceComment> resourceComments = resourceService.getCommentResources(pageID);
        return resourceComments != null ?new ResponseMessage<>(resourceComments).success():
                new ResponseMessage<>(null).error(202, "can't get my comment");
    }

    @RequestMapping(path = "/resource/mySuggest/{pageID}", method = RequestMethod.GET)
    public ResponseMessage getMySuggestedResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceComments = resourceService.getSuggestedResources(pageID);
        return resourceComments != null ? new ResponseMessage<>(resourceComments).success():
                new ResponseMessage<>(null).error(202, "can't get my suggested resource");
    }

    @RequestMapping(path = "/resource/user/history/{resourceID}", method = RequestMethod.GET)
    public ResponseMessage getUserHistoryOnResourceController(@PathVariable("resourceID") String resourceID) {
        Map<String, Integer> map = resourceService.getUserHistoryOnResource(resourceID);
        return map != null ? new ResponseMessage<>(map).success():
                new ResponseMessage<>(null).error(202, "can't get my history on this resource");
    }

    @RequestMapping(path ="/resource/detail/{resourceID}", method = RequestMethod.GET)
    public ResponseMessage getResourceDetailController(@PathVariable("resourceID") String resourceID) {
        ResourceDetail resourceDetail = resourceService.getResourceDetail(resourceID);
        return resourceDetail != null ? new ResponseMessage<>(resourceDetail).success():
                new ResponseMessage<>(null).error(202, "can't resource detail info");
    }

    @RequestMapping(path ="/user/detail", method = RequestMethod.GET)
    public ResponseMessage myDetailController() {
        UserDetail userDetail = resourceService.getMyDetail();
        return userDetail != null ? new ResponseMessage<>(userDetail).success():
                new ResponseMessage<>(null).error(202, "can't get my detail info");
    }
}
