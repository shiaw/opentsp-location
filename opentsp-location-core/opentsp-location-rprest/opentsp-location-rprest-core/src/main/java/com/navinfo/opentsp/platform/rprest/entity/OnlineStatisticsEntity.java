package com.navinfo.opentsp.platform.rprest.entity;

import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;

/**
 * Created by wanliang on 2017/5/19.
 */
public class OnlineStatisticsEntity {

    @Id
    private String day;
    private List<OnlineHour> data;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public List<OnlineHour> getData() {
        return data;
    }

    public void setData(List<OnlineHour> data) {
        this.data = data;
    }
}
