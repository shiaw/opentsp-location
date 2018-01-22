package com.navinfo.opentsp.platform.rprest.entity;

/**
 * @author: wzw
 * <p> 里程油耗统计
 * Time: 2017/11/29 9:35
 */
public class MileageFuel {
    /**
     * 通信号
     */
    private Long id;
    /**
     * 统计日期yyyy-MM-dd
     */
    private String date;
    /**
     * 当日里程,单位KM,两位小数
     */
    private Double mileage;
    /**
     * 当日油耗,单位L,两位小数
     */
    private Double fuel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Double getMileage() {
        return mileage;
    }

    public void setMileage(Double mileage) {
        this.mileage = mileage;
    }

    public Double getFuel() {
        return fuel;
    }

    public void setFuel(Double fuel) {
        this.fuel = fuel;
    }
}
