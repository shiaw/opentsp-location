package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.platform.push.DeviceCommand;
import org.springframework.stereotype.Component;

/**
 * Configuration of {@link com.navinfo.opentsp.platform.push.handler.UserDeviceHandler } for concrete command type.
 */
@Component
public class DeviceHandler extends UserDeviceHandler<DeviceCommand> {

    public DeviceHandler() {
        super(DeviceCommand.class);
    }
}
