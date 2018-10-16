package org.lab409.service.impl;

import com.mongodb.BasicDBObject;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.oracle.tools.packager.Log;
import org.lab409.entity.ResourceCategoryEntity;
import org.lab409.entity.ResourceEntity;
import org.lab409.entity.ResourceMajorEntity;
import org.lab409.entity.UserEntity;
import org.lab409.mapper.ResourceMapper;
import org.lab409.service.ResourceService;
import org.lab409.util.FormatDateUtil;
import org.lab409.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Date;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private FormatDateUtil formatDateUtil;

    class increaseDownloadTimes implements Runnable {
        private String resourceID;

        public increaseDownloadTimes(String resourceID) {
            this.resourceID = resourceID;
        }

        @Override
        public void run() {
            resourceMapper.increaseDownloadTimes(this.resourceID);
        }
    }

    @Override
    public Pair<Boolean, String> uploadResource(MultipartFile resource) {

        UserEntity currentUser = userUtil.getCurrentUser();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            BasicDBObject metaData = new BasicDBObject();
            metaData.put("uploader", currentUser.getUserID());
            String info = gridFsTemplate.store(inputStream,
                    resource.getOriginalFilename(),
                    resource.getContentType(),
                    metaData).toString();
            ResourceEntity resourceEntity = new ResourceEntity(info, currentUser.getUserID());
            resourceEntity.setUploadTime(formatDateUtil.formatDate(new Date()));
            resourceMapper.uploadResource(resourceEntity);
            return new Pair<>(true, info);
        }
        catch (IOException e) {
            Log.debug("inputStream get error" + e.getMessage());
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                }
                catch (IOException e) {
                    Log.debug("inputStream close error" + e.getMessage());
                }
            }
        }
        return new Pair<>(false, "");
    }

    @Override
    public boolean uploadResourceMetaData(ResourceEntity resourceEntity) {
        int success = resourceMapper.updateResourceMetaData(resourceEntity);
        if (success == 1) {
            return true;
        }
        return false;
    }

    @Override
    public Pair<Boolean, GridFsResource> downloadResource(String resourceID) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceID)));
        if (gridFSFile == null) {
            return new Pair<>(false, null);
        }
        String gridFileName = gridFSFile.getFilename();
        GridFsResource[] gridFsResources = gridFsTemplate.getResources(gridFileName);
        for(GridFsResource gridFsResource : gridFsResources ) {
            if (gridFsResource.getId().toString().equals(gridFSFile.getId().toString())){
                Thread increaseDownloadTimes = new Thread(new increaseDownloadTimes(resourceID));
                increaseDownloadTimes.start();
                return new Pair<>(true, gridFsResource);
            }
        }
        return new Pair<>(false, null);
    }

    @Override
    public int deleteResource(String resourceID) {
        /* -1: can't find file
           -2: no authority
           0: ok
        */
        UserEntity userEntity = userUtil.getCurrentUser();
        Query query = new Query(Criteria.where("_id").is(resourceID));
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);
        if (gridFSFile == null) {
            return -1;
        }
        if (gridFSFile.getMetadata().get("uploader") != userEntity.getUserID() ) {
            return -2;
        }
        gridFsTemplate.delete(query);
        resourceMapper.deleteResource(resourceID);
        return 0;
    }

    @Override
    public List<ResourceCategoryEntity> getResourceCategories() {
        return resourceMapper.getResourceCategories();
    }

    @Override
    public List<ResourceMajorEntity> getResourceMajors() {
        return resourceMapper.getResourceMajors();
    }
}
