package org.lab409.entity;

import lombok.Data;

@Data
public class ResourceEntity {
    private Integer id;
    private String resourceID;
    private Integer uploaderID;
    private Integer categoryID;
    private Integer resourceMajorID;

    public ResourceEntity(String resourceID, Integer uploaderID, Integer categoryID, Integer resourceMajorID) {
        this.resourceID = resourceID;
        this.uploaderID = uploaderID;
        this.categoryID = categoryID;
        this.resourceMajorID = resourceMajorID;
    }

    public ResourceEntity() {

    }

    public ResourceEntity(Integer id, String resourceID, Integer uploaderID, Integer categoryID, Integer resourceMajorID) {
        this.id = id;
        this.resourceID = resourceID;
        this.uploaderID = uploaderID;
        this.categoryID = categoryID;
        this.resourceMajorID = resourceMajorID;
    }
}
