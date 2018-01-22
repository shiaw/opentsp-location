package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

import javax.validation.constraints.NotNull;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/23
 * @modify
 * @copyright opentsp
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class TerminalDelCommand extends AbstractCommand<Command.Result> {

    @NotNull
    private String[] terminalIds;


    public String[] getTerminalIds() {
        return terminalIds;
    }

    public void setTerminalIds(String[] terminalIds) {
        this.terminalIds = terminalIds;
    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

    public static class Result extends CommandResult {


        public Result() {
        }

        public Result(Integer resultCode, String message) {
            super(resultCode, message);
        }

    }
}
