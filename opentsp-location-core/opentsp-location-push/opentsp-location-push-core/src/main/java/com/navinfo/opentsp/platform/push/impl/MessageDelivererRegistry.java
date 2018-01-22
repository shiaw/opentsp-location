package com.navinfo.opentsp.platform.push.impl;

import com.google.common.collect.ImmutableMap;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.MessageDeliverer;
import com.navinfo.opentsp.platform.push.api.MessageParcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;

/**
 * Composite deliverer which gather all deliverers and invoke its for each delivering task.
 */
@Component
public class MessageDelivererRegistry implements MessageDeliverer {

    private Map<String, MessageDeliverer> deliverers;

    @Autowired
    public MessageDelivererRegistry(Map<String, MessageDeliverer> deliverers) {
        this.deliverers = ImmutableMap.copyOf(deliverers);
    }

    @Override
    public void deliver(MessageParcel parcel) throws Exception {
        final ScheduledTask task = parcel.getTask();
        String method = parcel.getMethod();
        if(method == null) {
            throw new RuntimeException(MessageFormat.format("Method for task \"{0}\" is null.", task));
        }
        MessageDeliverer deliverer = deliverers.get(method);
        if(deliverer == null) {
            throw new RuntimeException(MessageFormat.format("We can not find deliverer for \"{0}\" method.", method));
        }
        deliverer.deliver(parcel);
    }
}
