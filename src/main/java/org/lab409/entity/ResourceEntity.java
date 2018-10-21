package org.lab409.entity;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;

import java.io.Serializable;

@Data
@Document(indexName = "resource", type = "resource")
@Mapping(mappingPath = "resource_search.json")
public class ResourceEntity implements Serializable {

    private String resourceID;
    private Integer id;
    private Integer uploaderID;
    private Integer categoryID;
    private Integer resourceMajorID;
    private String resourceName;
    private String description;
    private Integer downloadTimes = 0;
    private String uploadTime;

    public ResourceEntity(String resourceID,
                          Integer uploaderID,
                          Integer categoryID,
                          Integer resourceMajorID,
                          String resourceName,
                          String description ) {
        this(categoryID, resourceMajorID, resourceName, description);
        this.resourceID = resourceID;
        this.uploaderID = uploaderID;
    }

    public ResourceEntity(Integer categoryID, Integer resourceMajorID, String resourceName, String description) {
        this.categoryID = categoryID;
        this.resourceMajorID = resourceMajorID;
        this.resourceName = resourceName;
        this.description = description;
    }


    public ResourceEntity() {

    }

    public ResourceEntity(String resourceID, Integer uploaderID) {
        this.resourceID = resourceID;
        this.uploaderID = uploaderID;
    }

    public ResourceEntity(Integer id,
                          String resourceID,
                          Integer uploaderID,
                          Integer categoryID,
                          Integer resourceMajorID,
                          String resourceName,
                          String description) {
        this(resourceID, uploaderID, categoryID, resourceMajorID, resourceName, description);
        this.id = id;
    }
}
