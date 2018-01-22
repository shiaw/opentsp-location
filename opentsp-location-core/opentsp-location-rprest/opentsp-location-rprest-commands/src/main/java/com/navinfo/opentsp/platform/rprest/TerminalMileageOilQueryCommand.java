package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * Created by zhangyue on 2017/6/20.
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class TerminalMileageOilQueryCommand extends AbstractCommand<TerminalMileageOilQueryCommand.Result> {

    @Override
    public Class<? extends TerminalMileageOilQueryCommand.Result> getResultType() {
        return TerminalMileageOilQueryCommand.Result.class;
    }
    private Long terminalId;

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public static class Result extends CommandResult {

        private Object data;

        public Result() {
        }

        public Result(Integer resultCode, String message) {
            super(resultCode, message);
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }
}
