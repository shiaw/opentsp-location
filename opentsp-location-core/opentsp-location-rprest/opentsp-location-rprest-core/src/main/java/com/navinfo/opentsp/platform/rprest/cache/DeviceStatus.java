package com.navinfo.opentsp.platform.rprest.cache;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/2
 * @modify
 * @copyright opentsp
 */
public enum DeviceStatus {

    ONLINE(1), OFFLINE(0);

    private final int value;

    DeviceStatus(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
