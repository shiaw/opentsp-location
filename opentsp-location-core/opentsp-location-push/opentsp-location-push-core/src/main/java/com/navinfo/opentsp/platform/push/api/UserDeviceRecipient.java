package com.navinfo.opentsp.platform.push.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.Assert;

/**
 * User device as recipient for message parcels
 */
public class UserDeviceRecipient {
    private final String deviceId;

    @JsonCreator
    public UserDeviceRecipient(@JsonProperty("deviceId") String deviceId) {
        Assert.hasLength(deviceId, "deviceId is null or empty");
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserDeviceRecipient)) {
            return false;
        }

        UserDeviceRecipient that = (UserDeviceRecipient) o;

        if (deviceId != null ? !deviceId.equals(that.deviceId) : that.deviceId != null) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result =deviceId != null ? deviceId.hashCode() : 0;
        return result;
    }

    @Override
    public String toString() {
        return "UserDeviceRecipient{" +
          ", deviceId='" + deviceId + '\'' +
          '}';
    }
}
