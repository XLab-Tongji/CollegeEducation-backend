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
    @CrossOrigin(allowCredentials="true")
    public ResponseEntity uploadResourceController(@RequestParam(name = "resource") MultipartFile uploadResource) {

        AbstractMap.SimpleEntry<String, String> success = resourceService.uploadResource(uploadResource);
        if (success.getKey().equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(success.getValue()).success());
        }
        else {
            return ResponseEntity.status(202).body(new ResponseMessage<>(success.getValue()).error(202, success.getKey()));
        }
    }

    @RequestMapping(path = "/uploadResourceMetaData", method = RequestMethod.PUT)
    public ResponseEntity uploadResourceController(@RequestBody ResourceEntity resourceEntity){

        String success = resourceService.uploadResourceMetaData(resourceEntity);
        if (success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<String>(null).success());
        }
        else {
            return ResponseEntity.status(202).body(new ResponseMessage<String>(null).error(202, success));
        }
    }

    @RequestMapping(path = "/downloadResource/{resourceID}", method = RequestMethod.GET)
    public ResponseEntity downloadResourceController(@PathVariable("resourceID") String resourceID) {
        AbstractMap.SimpleEntry<String, GridFsResource> success = resourceService.downloadResource(resourceID);
        if (success.getKey().equals(ResourceService.OK)) {
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
            return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success.getKey()));
        }
    }

    @RequestMapping(path = "/deleteResource/{resourceID}", method = RequestMethod.DELETE)
    public ResponseEntity deleteResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.deleteResource(resourceID);
        if (success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null ).error(202, success));
    }

    @RequestMapping(path = "/resourceCategories", method = RequestMethod.GET)
    public ResponseEntity resourceCategoriesController() {
        List<ResourceCategoryEntity> resourceCategoryEntities = resourceService.getResourceCategories();
        return resourceCategoryEntities != null?
                ResponseEntity.ok(new ResponseMessage<>(resourceCategoryEntities).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get category"));
    }

    @RequestMapping(path = "/resourceMajors", method = RequestMethod.GET)
    public ResponseEntity resourceMajorsController() {
        List<ResourceMajorEntity> resourceMajorEntities = resourceService.getResourceMajors();
        return resourceMajorEntities != null?
                ResponseEntity.ok(new ResponseMessage<>(resourceMajorEntities).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202,"can't get majors"));
    }

    @RequestMapping(path = "/searchResource/{resourceMajorID}/{categoryID}/{pageID}", method = RequestMethod.GET)
    public ResponseEntity keywordSearchAllController(@PathVariable("resourceMajorID") Integer resourceMajorID,
                                                        @PathVariable("categoryID") Integer categoryID,
                                                        @RequestParam("keyword") String keyword,
                                                        @PathVariable("pageID") Integer pageID) {
        Page<ResourceEntity> resourceEntityList = resourceService.keywordSearchAll(keyword, categoryID, resourceMajorID, pageID);
        if(resourceEntityList != null) {
            return ResponseEntity.ok(new ResponseMessage<>(resourceEntityList).success());
        }
        else {
            return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202,"resource not found"));
        }
    }

    @RequestMapping(path = "/searchResource/time/{resourceMajorID}/{categoryID}/{pageID}", method = RequestMethod.GET)
    public ResponseEntity keywordSearchOnTimeController(@PathVariable("resourceMajorID") Integer resourceMajorID,
                                                     @PathVariable("categoryID") Integer categoryID,
                                                     @RequestParam("keyword") String keyword,
                                                     @PathVariable("pageID") Integer pageID) {
        Page<ResourceEntity> resourceEntityList = resourceService.keywordSearchOnTime(keyword, categoryID, resourceMajorID, pageID);
        if(resourceEntityList != null) {
            return ResponseEntity.ok(new ResponseMessage<>(resourceEntityList).success());
        }
        else {
            return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202,"resource not found"));
        }
    }

    @RequestMapping(path = "/searchResource/score/{resourceMajorID}/{categoryID}/{pageID}", method = RequestMethod.GET)
    public ResponseEntity keywordSearchOnScoreController(@PathVariable("resourceMajorID") Integer resourceMajorID,
                                                    @PathVariable("categoryID") Integer categoryID,
                                                    @RequestParam("keyword") String keyword,
                                                    @PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntityList = resourceService.keywordSearchOnScore(keyword, categoryID, resourceMajorID, pageID);
        if(resourceEntityList != null) {
            return ResponseEntity.ok(new ResponseMessage<>(resourceEntityList).success());
        }
        else {
            return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202,"resource not found"));
        }
    }

    @RequestMapping(path = "/resource/recommend/{resourceMajorID}/{categoryID}/{pageID}", method = RequestMethod.GET)
    public ResponseEntity recommendResourceController(@PathVariable("categoryID") Integer categoryID,
                                                       @PathVariable("resourceMajorID") Integer resourceMajorID,
                                                       @PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.relativeRecommend(pageID, categoryID, resourceMajorID);
        return resourceEntities != null?
                ResponseEntity.ok(new ResponseMessage<>(resourceEntities).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "recommend fail"));
    }

    @RequestMapping(path = "/resource/favourite/like/{resourceID}", method = RequestMethod.POST)
    public ResponseEntity likeFavouriteResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.likeResource(resourceID);
       if(success.equals(ResourceService.OK)) {
           return ResponseEntity.ok(new ResponseMessage<>(null).success());
       }
       return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/favourite/dislike/{resourceID}", method = RequestMethod.DELETE)
    public ResponseEntity unlikeFavouriteResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.dislikeResource(resourceID);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/suggest/make/{resourceID}", method = RequestMethod.POST)
    public ResponseEntity suggestResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.suggestResource(resourceID, 1);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/suggest/dislike/{resourceID}", method = RequestMethod.POST)
    public ResponseEntity disSuggestResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.suggestResource(resourceID, 0);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/suggest/undo/{resourceID}", method = RequestMethod.DELETE)
    public ResponseEntity undoSuggestsResourceController(@PathVariable("resourceID") String resourceID) {
        String success = resourceService.undoSuggestResource(resourceID);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/comment/make", method = RequestMethod.POST)
    public ResponseEntity commentResourceController(@RequestBody ResourceComment resourceComment) {
        String success = resourceService.commentResource(resourceComment);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(resourceComment).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/comment/delete/{commentID}", method = RequestMethod.DELETE)
    public ResponseEntity deleteResourceCommentController(@PathVariable("commentID") Integer commentID) {
        String success = resourceService.deleteResourceComment(commentID);
        if(success.equals(ResourceService.OK)) {
            return ResponseEntity.ok(new ResponseMessage<>(null).success());
        }
        return ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, success));
    }

    @RequestMapping(path = "/resource/myFavourite/{pageID}", method = RequestMethod.GET)
    public ResponseEntity getMyFavouriteResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getFavouriteResources(pageID);
        return resourceEntities != null?
                ResponseEntity.ok(new ResponseMessage<>(resourceEntities).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my favourite resource"));
    }

    @RequestMapping(path = "/resource/myDownload/{pageID}", method = RequestMethod.GET)
    public ResponseEntity getMyDownloadResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getDownloadResources(pageID);
        return resourceEntities != null ?
                ResponseEntity.ok(new ResponseMessage<>(resourceEntities).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my download resource"));
    }

    @RequestMapping(path = "/resource/myUpload/{pageID}", method = RequestMethod.GET)
    public ResponseEntity getMyUploadResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceEntities = resourceService.getUploadResources(pageID);
        return resourceEntities != null ?
                ResponseEntity.ok(new ResponseMessage<>(resourceEntities).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my upload resource"));
    }

    @RequestMapping(path = "/resource/comments/{resourceID}/{pageID}", method = RequestMethod.GET)
    public ResponseEntity getResourceCommentsController(@PathVariable("resourceID") String resourceID, @PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceComment> resourceComments = resourceService.getResourceComments(pageID, resourceID);
        return resourceComments != null ?
                ResponseEntity.ok(new ResponseMessage<>(resourceComments).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get resource comment"));
    }

    @RequestMapping(path = "/resource/myComment/{pageID}", method = RequestMethod.GET)
    public ResponseEntity getMyCommentedResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceComment> resourceComments = resourceService.getCommentResources(pageID);
        return resourceComments != null ?
                ResponseEntity.ok(new ResponseMessage<>(resourceComments).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my comment"));
    }

    @RequestMapping(path = "/resource/mySuggest/{pageID}", method = RequestMethod.GET)
    public ResponseEntity getMySuggestedResourceController(@PathVariable("pageID") Integer pageID) {
        PageInfo<ResourceEntity> resourceComments = resourceService.getSuggestedResources(pageID);
        return resourceComments != null ?
                ResponseEntity.ok(new ResponseMessage<>(resourceComments).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my suggested resource"));
    }

    @RequestMapping(path = "/resource/user/history/{resourceID}", method = RequestMethod.GET)
    public ResponseEntity getUserHistoryOnResourceController(@PathVariable("resourceID") String resourceID) {
        Map<String, Integer> map = resourceService.getUserHistoryOnResource(resourceID);
        return map != null ?
                ResponseEntity.ok(new ResponseMessage<>(map).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my history on this resource"));
    }

    @RequestMapping(path ="/resource/detail/{resourceID}", method = RequestMethod.GET)
    public ResponseEntity getResourceDetailController(@PathVariable("resourceID") String resourceID) {
        ResourceDetail resourceDetail = resourceService.getResourceDetail(resourceID);
        return resourceDetail != null ?
                ResponseEntity.ok(new ResponseMessage<>(resourceDetail).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't resource detail info"));
    }

    @RequestMapping(path ="/user/detail", method = RequestMethod.GET)
    public ResponseEntity myDetailController() {
        UserDetail userDetail = resourceService.getMyDetail();
        return userDetail != null ?
                ResponseEntity.ok(new ResponseMessage<>(userDetail).success()):
                ResponseEntity.status(202).body(new ResponseMessage<>(null).error(202, "can't get my detail info"));
    }
}
