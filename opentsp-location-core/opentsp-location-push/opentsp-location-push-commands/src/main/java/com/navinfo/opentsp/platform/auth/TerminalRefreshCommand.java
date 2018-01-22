package com.navinfo.opentsp.platform.auth;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import com.navinfo.opentsp.platform.push.PushModuleConstants;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/7/20
 * @modify
 * @copyright opentsp
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class TerminalRefreshCommand extends AbstractCommand<Command.Result> {

    private String terminalId;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public static class Result extends CommandResult {

        private Map results;

        public Result() { }

        public Result(Integer resultCode, String message,Map results) {
            super(resultCode, message);
            this.results = results;
        }

        public Map getResults() {
            return results;
        }

        public void setResults(Map results) {
            this.results = results;
        }
    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

}
