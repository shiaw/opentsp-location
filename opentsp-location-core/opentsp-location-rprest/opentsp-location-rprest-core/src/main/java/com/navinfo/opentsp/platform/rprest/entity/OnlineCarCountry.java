package com.navinfo.opentsp.platform.rprest.entity;

/**
 * 国家车辆在线数表
 */
public class OnlineCarCountry {

    /**
     * 日期:yyyyMMdd
     */
    private String date;
    /**
     * 总数
     */
    private int number;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

}
