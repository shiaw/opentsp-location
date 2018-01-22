package com.navinfo.opentsp.platform.push.mail;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.mail.MailQueueStatusCommand;
import com.navinfo.opentsp.platform.mail.dto.MailSendResult;
import com.navinfo.opentsp.platform.mail.dto.MailStatus;
import com.navinfo.opentsp.platform.push.api.*;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * callback for mail module
 */
@Component
public class MailStatusHandler extends AbstractCommandHandler<MailQueueStatusCommand,CommandResult> {

    private ScheduledTasksStorage storage;

    @Autowired
    public MailStatusHandler(ScheduledTasksStorage storage) {
        super(MailQueueStatusCommand.class, CommandResult.class);
        this.storage = storage;
    }

    @Override
    public CommandResult handle(MailQueueStatusCommand command) {
        List<MailSendResult> results = command.getResults();
        for(MailSendResult result: results) {
            MailStatus status = result.getStatus();
            if(status == MailStatus.QUEUED) {
                continue;
            }
            DeliveringId ids = DeliveringId.parse(result.getHead().getCustomId());
            StoredScheduledTask schedule = storage.getSchedule(ids.getTask());
            DeliveringStatus newStatus = null;
            if(status == MailStatus.OK) {
                newStatus = DeliveringStatus.OK;
            } else if(status == MailStatus.UNKNOWN_FAIL) {
                newStatus = DeliveringStatus.ERROR;
            }
            if(newStatus != null) {
                DeliveringState state = schedule.getDeliveringStates().get(ids.getDelivering());
                DeliveringState newState = state.clone();
                newState.setStatus(newStatus);
                storage.updateDelivering(ids.getTask(), state, newState);
            }
        }
        return new CommandResult().fillResult(ResultCode.OK);
    }
}
