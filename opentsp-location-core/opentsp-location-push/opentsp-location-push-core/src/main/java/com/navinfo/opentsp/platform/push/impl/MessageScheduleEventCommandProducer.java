package com.navinfo.opentsp.platform.push.impl;

import com.google.common.base.Strings;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.push.MessageScheduleEventCommand;
import com.navinfo.opentsp.platform.push.api.LocalMessageEvent;
import com.navinfo.opentsp.platform.push.api.LocalMessageListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;


/**
 * Component which act as listener for {@link com.navinfo.opentsp.platform.push.MessageScheduleEvent} and post it
 * to rabbit.
 */
public class MessageScheduleEventCommandProducer implements LocalMessageListener {

    private final Logger log = LoggerFactory.getLogger(getClass());
    private final ObjectFactory<MessageChannel> messageChannel;

    public MessageScheduleEventCommandProducer(ObjectFactory<MessageChannel> messageChannel) {
        this.messageChannel = messageChannel;
    }

    @Override
    public void accept(LocalMessageEvent localEvent) {
        MessageScheduleEventCommand cmd = new MessageScheduleEventCommand().event(localEvent.getEvent());
        String queue = localEvent.getTask().getListenerQueue();
        if(Strings.isNullOrEmpty(queue)) {
            log.warn("The listenerQueue name is null or empty on event: " + localEvent);
            return;
        }
        try {
            messageChannel.getObject().sendAndReceive(cmd, null, queue);
        } catch (Exception e) {
            log.error("On event: " + localEvent, e);
        }
    }
}
