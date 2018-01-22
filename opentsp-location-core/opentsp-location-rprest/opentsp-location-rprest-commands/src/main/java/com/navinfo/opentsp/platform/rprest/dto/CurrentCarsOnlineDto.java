package com.navinfo.opentsp.platform.rprest.dto;

/**
 * Created by wanliang on 2017/5/22.
 */
public class CurrentCarsOnlineDto {

    private Integer  today;
    private Integer  avg;

    public Integer getToday() {
        return today;
    }

    public void setToday(Integer today) {
        this.today = today;
    }

    public Integer getAvg() {
        return avg;
    }

    public void setAvg(Integer avg) {
        this.avg = avg;
    }
}
