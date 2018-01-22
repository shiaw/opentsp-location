package com.navinfo.opentsp.platform.push.impl;

import com.google.common.collect.ImmutableMap;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.MessageRouter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * Registry which gather all beans implemented MessageRouter and choose appropriate for each message.
 */
@Component
public class MessageRouterRegistry implements MessageRouter {

    private final Map<String, MessageRouter> routerMap;

    @Autowired
    public MessageRouterRegistry(Map<String, MessageRouter> routers) {
        this.routerMap = ImmutableMap.copyOf(routers);
    }

    @Override
    public Collection<?> resolveRoute(ScheduledTask task) {
        final String routerName = task.getRouter();
        final MessageRouter router = routerMap.get(routerName);
        if(router == null) {
            throw new RuntimeException("Can not find router for name '" + routerName + "'");
        }
        return router.resolveRoute(task);
    }
}
