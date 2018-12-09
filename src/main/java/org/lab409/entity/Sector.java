package org.lab409.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by jiao on 2018/12/9
 */
public class Sector {
    private Integer SectorId;
    private String SectorName;

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
}
