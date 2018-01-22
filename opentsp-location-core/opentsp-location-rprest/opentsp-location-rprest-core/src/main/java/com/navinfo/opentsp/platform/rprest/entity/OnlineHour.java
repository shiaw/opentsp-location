package com.navinfo.opentsp.platform.rprest.entity;

/**
 * Created by wanliang on 2017/5/19.
 */
public class OnlineHour {

    public Integer total;

    private Integer online;

    public  OnlineHour(Integer total,Integer online){
        this.online=online;
        this.total=total;
    }
    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getOnline() {
        return online;
    }

    public void setOnline(Integer online) {
        this.online = online;
    }
}
