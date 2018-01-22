package com.navinfo.opentsp.platform.rprest.dto;

/**
 * Created by HOUQL on 2017/5/22.
 * 服务站周边车辆信息
 */
public class ServiceStatisticsDto {

    private Integer district;

    private  Integer number;

//    private double Lng;
//    private double Lat;
//
//    public double getLng() {
//        return Lng;
//    }
//
//    public void setLng(double lng) {
//        Lng = lng;
//    }
//
//    public double getLat() {
//        return Lat;
//    }
//
//    public void setLat(double lat) {
//        Lat = lat;
//    }

    public Integer getDistrict() {
        return district;
    }

    public void setDistrict(Integer district) {
        this.district = district;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
