package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractSecuredCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * This command push specified message to matched recipients (users).
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class MessageScheduleCommand extends AbstractSecuredCommand<MessageScheduleCommand.Result> {

    private ScheduledTask task;

    /**
     * Schedule task.
     * @return
     */
    public ScheduledTask getTask() {
        return task;
    }

    /**
     * Schedule task.
     * @param task
     */
    public void setTask(ScheduledTask task) {
        this.task = task;
    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

    public static class Result extends CommandResult {
        private MessageScheduleEvent event;

        /**
         * Event with new state of scheduled message.
         * @return
         */
        public MessageScheduleEvent getEvent() {
            return event;
        }

        /**
         * Event with new state of scheduled message.
         * @param event
         */
        public void setEvent(MessageScheduleEvent event) {
            this.event = event;
        }
    }
}
