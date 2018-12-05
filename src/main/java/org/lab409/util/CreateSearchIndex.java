package org.lab409.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.lab409.Application;
import org.lab409.entity.ResourceEntity;
import org.lab409.mapper.ResourceMapper;
import org.lab409.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= {Application.class})
public class CreateSearchIndex {

    @Autowired
    ResourceService resourceService;

    @Autowired
    ResourceMapper resourceMapper;

    @Test
    public void createIndex() {
        List<ResourceEntity> resourceEntities = resourceMapper.getAllResource();
        resourceService.saveAllDoc(resourceEntities);
    }
}
