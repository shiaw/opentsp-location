package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.MessageRouter;
import com.navinfo.opentsp.platform.push.api.UserDeviceRecipient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Route message to concrete device of user.
 */
@Component(UserDeviceRouter.NAME)
public class UserDeviceRouter implements MessageRouter {

    /**
     * name of bean
     */
    public static final String NAME = "userDeviceRouter";

    @Override
    public Collection<?> resolveRoute(ScheduledTask task) {
        Object routerArgument = task.getRouterArgument();
        Assert.notNull(routerArgument, "routerArgument is null");
        //Set<String> usernamesDevices = StringUtils.commaDelimitedListToSet(routerArgument.toString());
        List<UserDeviceRecipient> list = new ArrayList<>();
//        for(String usernameDevice: usernamesDevices) {
//            String[] strings = StringUtils.split(usernameDevice, DELIMTER);
//            Assert.isTrue(strings.length == 2, usernameDevice + " must have two parts delimited with space");
//        }
        list.add(new UserDeviceRecipient(routerArgument.toString()));

        return list;
    }

    public static String toRouterArgument(String device) {
       // Assert.notNull(username, "username is null");
        Assert.notNull(device, "device is null");
        //username + DELIMTER +
        return  device;
    }
}
