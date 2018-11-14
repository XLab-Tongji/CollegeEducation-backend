package org.lab409.entity;

import lombok.Data;

@Data
public class ResourceDetail {
    ResourceEntity resourceEntity;
    String uploaderName;
    Integer downloadTimes;
    Integer favouriteNum;
    Integer suggestedNum;
}
