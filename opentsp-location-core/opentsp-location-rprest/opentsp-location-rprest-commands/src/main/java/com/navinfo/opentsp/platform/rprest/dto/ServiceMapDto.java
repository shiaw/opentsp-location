package com.navinfo.opentsp.platform.rprest.dto;

/**
 * Created by HOUQL on 2017/5/22.
 * 服务站周边车辆信息
 */
public class ServiceMapDto {

    private Integer district;

    private Double lat;

    private Double lng;

    private  Integer number=0;

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
