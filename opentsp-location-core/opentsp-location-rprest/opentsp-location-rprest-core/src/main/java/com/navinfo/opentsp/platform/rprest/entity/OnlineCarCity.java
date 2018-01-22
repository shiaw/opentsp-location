package com.navinfo.opentsp.platform.rprest.entity;

/**
 * 城市车辆在线数
 */
public class OnlineCarCity {
    /**
     * 城市行政区域编码
     */
    private int districtCode;
    /**
     * 城市在线车辆总数
     */
    private int number;

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
}
