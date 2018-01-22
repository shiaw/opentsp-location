package com.navinfo.opentsp.platform.push.online;

import com.navinfo.opentsp.platform.push.api.MessageParcel;
import com.navinfo.opentspcore.common.userdevice.UserDeviceCommand;

/**
 * Context fore device sender.
 */
public class UserDeviceSenderContext {
    private UserDeviceCommand<?> command;
    private String route;
    private MessageParcel parcel;

    public UserDeviceSenderContext(MessageParcel parcel, UserDeviceCommand<?> command, String route) {
        this.command = command;
        this.route = route;
        this.parcel = parcel;
    }

    public UserDeviceCommand<?> getCommand() {
        return command;
    }

    public String getRoute() {
        return route;
    }

    public MessageParcel getParcel() {
        return parcel;
    }
}
