package com.navinfo.tasktracker.rprest.domain;

import java.io.Serializable;

public class LcDistrictAndTileMapping implements Serializable {
    private Long tileId;

    private Integer districtId;

    private Integer parentDistrictId;

    public Long getTileId() {
        return tileId;
    }

    public void setTileId(Long tileId) {
        this.tileId = tileId;
    }

    public Integer getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Integer districtId) {
        this.districtId = districtId;
    }

    public Integer getParentDistrictId() {
        return parentDistrictId;
    }

    public void setParentDistrictId(Integer parentDistrictId) {
        this.parentDistrictId = parentDistrictId;
    }
}