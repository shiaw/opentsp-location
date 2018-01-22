package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.MessageScheduleCommand;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.LocalMessageListener;
import com.navinfo.opentsp.platform.push.impl.DeliveryProcessor;
import com.navinfo.opentsp.platform.push.api.LocalMessageEvent;
import com.navinfo.opentsp.platform.push.impl.LocalMessageEventHub;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Handler for message schedule commands
 */
@Component
public class MessageScheduleHandler extends AbstractCommandHandler<MessageScheduleCommand, MessageScheduleCommand.Result> {

    private final DeliveryProcessor processor;
    private final LocalMessageEventHub eventHub;

    private static class EventHolder implements LocalMessageListener {
        private LocalMessageEvent event;


        @Override
        public void accept(LocalMessageEvent event) {
            this.event = event;
        }

        public LocalMessageEvent getEvent() {
            return event;
        }
    }

    @Autowired
    public MessageScheduleHandler(DeliveryProcessor processor, LocalMessageEventHub eventHub) {
        super(MessageScheduleCommand.class, MessageScheduleCommand.Result.class);
        this.processor = processor;
        this.eventHub = eventHub;
    }

    @Override
    public MessageScheduleCommand.Result handle(MessageScheduleCommand command) {
        EventHolder holder =  new EventHolder();
        this.eventHub.addConsumer(holder);
        ScheduledTask task = command.getTask();
        MessageScheduleCommand.Result result = new MessageScheduleCommand.Result().fillResult(ResultCode.OK);
        try {
            this.processor.schedule(task);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("On " + task, e);
            result.setMessage(e.getMessage());
            result.setResultCode(ResultCode.SERVER_ERROR.code());
        } finally {
            this.eventHub.removeConsumer(holder);
        }
        LocalMessageEvent value = holder.getEvent();
        if(value != null) {
            result.setEvent(value.getEvent());
        }
        return result;
    }
}
