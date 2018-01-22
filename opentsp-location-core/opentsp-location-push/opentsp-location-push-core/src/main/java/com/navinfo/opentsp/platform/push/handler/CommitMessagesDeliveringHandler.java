package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.CommitMessagesDeliveringCommand;
import com.navinfo.opentsp.platform.push.ScheduledTask;
import com.navinfo.opentsp.platform.push.api.*;
import com.navinfo.opentsp.platform.push.impl.PushUtils;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 */
@Component
public class CommitMessagesDeliveringHandler extends AbstractCommandHandler<CommitMessagesDeliveringCommand, CommandResult> {

    private final ScheduledTasksStorage storage;

    @Autowired
    public CommitMessagesDeliveringHandler(ScheduledTasksStorage storage) {
        super(CommitMessagesDeliveringCommand.class, CommandResult.class);
        this.storage = storage;
    }

    @Override
    public CommandResult handle(CommitMessagesDeliveringCommand command) {
        for(String id: command.getMessagesIds()) {
            DeliveringId deliveringId = DeliveringId.parse(id);
            StoredScheduledTask stored = storage.getSchedule(deliveringId.getTask());
            if(stored == null) {
                continue;
            }
            ScheduledTask task = stored.getTask();
            DeliveringState delivering = stored.getDeliveringStates().get(deliveringId.getDelivering());
            PushUtils.commitDelivering(storage, task, delivering);
        }
        return new CommandResult().fillResult(ResultCode.OK);
    }
}
