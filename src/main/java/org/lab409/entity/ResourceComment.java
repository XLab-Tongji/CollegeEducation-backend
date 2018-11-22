package org.lab409.entity;

import lombok.Data;

@Data
public class ResourceComment {
    Integer id;
    Integer userID;
    String resourceID;
    String commentTitle;
    String comment;
    String commentTime;
    Integer score;
}
