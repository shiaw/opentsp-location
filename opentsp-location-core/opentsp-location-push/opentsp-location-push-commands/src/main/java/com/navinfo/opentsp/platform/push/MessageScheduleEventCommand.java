package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;

/**
 */
public class MessageScheduleEventCommand extends AbstractCommand<CommandResult> {

    private MessageScheduleEvent event;

    public MessageScheduleEvent getEvent() {
        return event;
    }

    public MessageScheduleEventCommand event(MessageScheduleEvent event) {
        setEvent(event);
        return this;
    }

    public void setEvent(MessageScheduleEvent event) {
        this.event = event;
    }

    @Override
    public Class<? extends CommandResult> getResultType() {
        return CommandResult.class;
    }
}
