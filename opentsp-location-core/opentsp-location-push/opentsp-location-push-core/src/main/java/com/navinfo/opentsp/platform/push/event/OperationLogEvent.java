package com.navinfo.opentsp.platform.push.event;

import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.DownCommandState;
import org.springframework.context.ApplicationEvent;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/31
 * @modify
 * @copyright opentsp
 */
public class OperationLogEvent extends ApplicationEvent {

    private DeviceCommand deviceCommand;

    private int downCommandState;

    public OperationLogEvent(Object source) {
        super(source);
    }

    public OperationLogEvent(Object source, DeviceCommand deviceCommand, int downCommandState) {
        super(source);
        this.deviceCommand = deviceCommand;
        this.downCommandState = downCommandState;
    }

    public DeviceCommand getDeviceCommand() {
        return deviceCommand;
    }
}
