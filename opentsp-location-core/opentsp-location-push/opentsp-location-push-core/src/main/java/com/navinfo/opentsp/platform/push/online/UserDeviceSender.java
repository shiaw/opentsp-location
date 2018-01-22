package com.navinfo.opentsp.platform.push.online;

/**
 * Iface for sending to device through some gateway or some other way.
 */
public interface UserDeviceSender {
    void send(UserDeviceSenderContext context) throws Exception;
}
