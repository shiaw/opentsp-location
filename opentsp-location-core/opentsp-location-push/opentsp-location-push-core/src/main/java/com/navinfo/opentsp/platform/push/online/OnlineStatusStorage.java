package com.navinfo.opentsp.platform.push.online;

import java.util.function.Consumer;

/**
 * Abstract iface for device storage
 */
public interface OnlineStatusStorage {
    void register(DeviceRegistration deviceRegistration);

    /**
     * Update registration, for prevent exceeding.
     * @param userDeviceId
     * @see DeviceRegistration#getTtl()
     */
    void heartbeat(String userDeviceId);
    DeviceRegistration get(String userDeviceId);
    void unregister(String userDeviceId);

    /***
     * Enumerates all online devices os specified user.
     * @param deviceId
     * @param callback callback which is accept device registrations, note than it is called from redis context,
     *                 therefore we do not use heavy or blocking operations.
     */
    void enumerate(String deviceId, final Consumer<DeviceChooserContext> callback);

    interface DeviceChooserContext {
        DeviceRegistration getDevice();
        String getDeviceId();

        /**
         * Notify enumerator that enumeration must be ended.
         */
        void end();
    }
}
