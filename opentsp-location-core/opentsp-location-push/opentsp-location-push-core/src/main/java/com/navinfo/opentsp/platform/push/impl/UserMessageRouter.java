package com.navinfo.opentsp.platform.push.impl;

import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.MessageRouter;
import com.navinfo.opentsp.platform.push.api.UserRecipient;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * Route to user list specified by routerArgument.
 */
@Component(UserMessageRouter.NAME)
public class UserMessageRouter implements MessageRouter {

    public static final String NAME = "userRouter";

    @Override
    public Collection<?> resolveRoute(ScheduledTask task) {
        Object routerArgument = task.getRouterArgument();
        Assert.notNull(routerArgument, "routerArgument is null");
        Set<String> usernames = StringUtils.commaDelimitedListToSet(routerArgument.toString());
        List<UserRecipient> list = new ArrayList<>();
        for(String username: usernames) {
            list.add(new UserRecipient(username.trim()));
        }
        return list;
    }
}
