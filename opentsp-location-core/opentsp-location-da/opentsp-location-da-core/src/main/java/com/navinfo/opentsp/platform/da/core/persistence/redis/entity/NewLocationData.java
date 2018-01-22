package com.navinfo.opentsp.platform.da.core.persistence.redis.entity;

/**
 * @author Lenovo
 * @date 2016-12-13
 * @modify
 * @copyright
 */
public class NewLocationData {
    private int longitude;//经度
    private int latitude; //纬度
    private long canMileage;//整车里程
    private long runTime;//发动机运行时长，单位秒
    private long oilValue;//车辆当前油量
    private long rotation;//车辆当前转速
    private float standardMileage;//标准里程
    private float standardFuelCon;//标准油耗


    public int getLongitude() {
        return longitude;
    }
    public void setLongitude(int longitude) {
        this.longitude = longitude;
    }
    public int getLatitude() {
        return latitude;
    }
    public void setLatitude(int latitude) {
        this.latitude = latitude;
    }
    public long getCanMileage() {
        return canMileage;
    }
    public void setCanMileage(long canMileage) {
        this.canMileage = canMileage;
    }
    public long getRunTime() {
        return runTime;
    }
    public void setRunTime(long runTime) {
        this.runTime = runTime;
    }
    public long getOilValue() {
        return oilValue;
    }
    public void setOilValue(long oilValue) {
        this.oilValue = oilValue;
    }
    public long getRotation() {
        return rotation;
    }
    public void setRotation(long rotation) {
        this.rotation = rotation;
    }

    public float getStandardMileage() {
        return standardMileage;
    }

    public void setStandardMileage(float standardMileage) {
        this.standardMileage = standardMileage;
    }

    public float getStandardFuelCon() {
        return standardFuelCon;
    }

    public void setStandardFuelCon(float standardFuelCon) {
        this.standardFuelCon = standardFuelCon;
    }
}
