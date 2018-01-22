package com.navinfo.opentsp.platform.push.online;

import com.google.common.collect.ImmutableSet;
import com.navinfo.opentspcore.common.userdevice.UserDevice;
import com.navinfo.opentspcore.common.userdevice.UserDeviceUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.function.Consumer;

/**
 * Tool which do actual device choosing
 */
@Component
class DeviceChooserFactory {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final UserDeviceService userDeviceService;

    @Autowired
    public DeviceChooserFactory(UserDeviceService userDeviceService) {
        this.userDeviceService = userDeviceService;
    }

    public DeviceChooser newChooser(String username) {
        List<UserDevice> userDevices = userDeviceService.getUserDevices(username);
        Map<String, UserDevice> map = new HashMap<>();
        for(UserDevice userDevice: userDevices) {
            map.put(userDevice.getId(), userDevice);
        }
        return new DeviceChooser(username, map);
    }

    public class DeviceChooser implements Consumer<OnlineStatusStorage.DeviceChooserContext> {
        private final Map<String, UserDevice> map;
        private final String username;
        private DeviceRegistration deviceRegistration;

        public DeviceChooser(String username, Map<String, UserDevice> map) {
            this.username = username;
            this.map = map;
        }

        @Override
        public void accept(OnlineStatusStorage.DeviceChooserContext context) {
            String device = context.getDeviceId();
            UserDevice userDevice = this.map.get(device);
            if(userDevice == null) {
                log.warn("Online device {} is not registered for user {}", device, username);
                return;
            }
            Set<String> roles = userDevice.getRoles();
            if(Collections.disjoint(roles, UserDeviceUtils.ROLES_RECEIVE_MESSAGES)) {
                return;
            }
            //store appropriate device
            deviceRegistration = context.getDevice();
            context.end();
        }

        public DeviceRegistration getDeviceRegistration() {
            return deviceRegistration;
        }
    }
}
