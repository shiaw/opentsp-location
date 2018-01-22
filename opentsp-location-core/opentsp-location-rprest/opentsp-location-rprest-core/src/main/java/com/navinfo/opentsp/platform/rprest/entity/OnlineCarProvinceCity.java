package com.navinfo.opentsp.platform.rprest.entity;

import java.util.List;

/**
 * 车辆在线数省市表
 */
public class OnlineCarProvinceCity {

    /**
     * 日期:yyyyMMdd
     */
    private String date;
    /**
     * 省级行政区域编码
     */
    private int districtCode;
    /**
     * 省份车辆在线总数
     */
    private int number;
    /**
     * 城市车辆在线列表
     */
    private List<OnlineCarCity> cityList;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(int districtCode) {
        this.districtCode = districtCode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public List<OnlineCarCity> getCityList() {
        return cityList;
    }

    public void setCityList(List<OnlineCarCity> cityList) {
        this.cityList = cityList;
    }

}
