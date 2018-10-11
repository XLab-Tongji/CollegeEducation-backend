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

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserUtil userUtil;

    @Autowired
    ResourceMapper resourceMapper;

    @Override
    public Pair<Boolean, String> uploadResource(MultipartFile resource,
                                                String resourceName,
                                                String description,
                                                Integer categoryID,
                                                Integer resourceMajorID) {

        UserEntity currentUser = userUtil.getCurrentUser();
        InputStream inputStream = null;
        try {
            inputStream = resource.getInputStream();
            BasicDBObject metaData = new BasicDBObject();
            metaData.put("description", description);
            metaData.put("resourceName", resourceName);
            metaData.put("uploader", currentUser.getUserID());
            String info = gridFsTemplate.store(inputStream,
                    resource.getOriginalFilename(),
                    resource.getContentType(),
                    metaData).toString();
            ResourceEntity resourceEntity = new ResourceEntity(info, currentUser.getUserID(),
                                                                categoryID,resourceMajorID);

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
    public Pair<Boolean, GridFsResource> downloadResource(String resourceID) {
        GridFSFile gridFSFile = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(resourceID)));
        if (gridFSFile == null) {
            return new Pair<>(false, null);
        }
        String gridFileName = gridFSFile.getFilename();
        GridFsResource[] gridFsResources = gridFsTemplate.getResources(gridFileName);
        for(GridFsResource gridFsResource : gridFsResources ) {
            if (gridFsResource.getId().toString().equals(gridFSFile.getId().toString())){
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
