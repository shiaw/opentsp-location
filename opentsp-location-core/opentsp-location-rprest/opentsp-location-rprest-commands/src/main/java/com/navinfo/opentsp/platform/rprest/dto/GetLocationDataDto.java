package com.navinfo.opentsp.platform.rprest.dto;

/**
 * Created by wanliang on 2017/5/19.
 */
public class GetLocationDataDto {

    private long tid;
    private  double lat;
    private double lng;
    private long gpsDate;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public long getGpsDate() {
        return gpsDate;
    }

    public void setGpsDate(long gpsDate) {
        this.gpsDate = gpsDate;
    }
}
