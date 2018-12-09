package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jiao on 2018/12/9
 */
public class Sector {
    private Integer SectorId;
    private String SectorName;
    private Integer topic_id;

    @JsonProperty(value = "SectorId")
    public Integer getSectorId() {
        return SectorId;
    }

    public void setSectorId(Integer sectorId) {
        SectorId = sectorId;
    }

    @JsonProperty(value = "SectorName")
    public String getSectorName() {
        return SectorName;
    }

    public void setSectorName(String sectorName) {
        SectorName = sectorName;
    }

    @JsonProperty(value = "topic_id")
    public Integer getTopic_id() {
        return topic_id;
    }

    public void setTopic_id(Integer topic_id) {
        this.topic_id = topic_id;
    }
}
