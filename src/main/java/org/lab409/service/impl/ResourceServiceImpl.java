package org.lab409.service.impl;

import com.mongodb.BasicDBObject;
import com.oracle.tools.packager.Log;
import org.lab409.entity.UserEntity;
import org.lab409.service.ResourceService;
import org.lab409.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javafx.util.Pair;
import java.io.IOException;
import java.io.InputStream;

@Service
public class ResourceServiceImpl implements ResourceService {
    @Autowired
    private GridFsTemplate gridFsTemplate;

    @Autowired
    private UserUtil userUtil;

    @Override
    public Pair<Boolean, String> uploadResource(MultipartFile resource, String resourceName, String description) {

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

    /*@Override
    public boolean downloadResource() {

    }*/
}
