package com.navinfo.opentsp.platform.push.online;

import com.google.common.collect.ImmutableList;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.users.commands.DeviceListResult;
import com.navinfo.opentsp.users.commands.GetUserDevicesCommand;
import com.navinfo.opentspcore.common.userdevice.UserDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Service which is provide user devices
 */
@Component
public class UserDeviceService {
    private final Logger LOG = LoggerFactory.getLogger(getClass());
    private final MessageChannel messageChannel;

    @Autowired
    public UserDeviceService(MessageChannel messageChannel) {
        this.messageChannel = messageChannel;
    }

    public List<UserDevice> getUserDevices(String username) {
        GetUserDevicesCommand cmd = new GetUserDevicesCommand();
        cmd.setUsername(username);
        try {
            DeviceListResult result = messageChannel.sendAndReceive(cmd);
            return ImmutableList.copyOf(result.getDevices());
        } catch (Exception  e) {
            LOG.error("On getUserDevices({})", username, e);
        }
        return Collections.emptyList();
    }
}
