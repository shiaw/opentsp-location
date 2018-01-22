package com.navinfo.opentsp.platform.push.api;

/**
 * Iface for message deliverer
 */
public interface MessageDeliverer {
    void deliver(MessageParcel parcel) throws Exception;
}
