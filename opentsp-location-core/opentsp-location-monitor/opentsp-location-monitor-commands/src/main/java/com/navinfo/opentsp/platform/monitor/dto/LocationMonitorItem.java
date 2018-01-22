package com.navinfo.opentsp.platform.monitor.dto;

/**
 * Created by zhangyue on 2017/6/15.
 */
public class LocationMonitorItem {
    private String name;
    private int value;
    private boolean direct;//true:正向，false:反向

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isDirect() {
        return direct;
    }

    public void setDirect(boolean direct) {
        this.direct = direct;
    }
}
