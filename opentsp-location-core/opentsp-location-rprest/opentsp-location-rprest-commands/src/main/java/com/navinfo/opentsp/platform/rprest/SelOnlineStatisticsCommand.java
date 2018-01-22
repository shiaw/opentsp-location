package com.navinfo.opentsp.platform.rprest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

import javax.validation.constraints.NotNull;

/**
 * Command which invoke process of email delivery to all specified clients.
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class SelOnlineStatisticsCommand extends AbstractCommand<SelOnlineStatisticsCommand.Result> {


    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

    public static class Result extends CommandResult {

    }
}
