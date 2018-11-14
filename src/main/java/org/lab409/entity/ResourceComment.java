package org.lab409.entity;

import lombok.Data;

@Data
public class ResourceComment {
    Integer id;
    Integer userID;
    String resourceID;
    String comment;
    String commentTime;
}
