package com.navinfo.opentsp.platform.monitor;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * Created by machi1 on 2017/6/27.
 */
@MessageGroup(MonitorModuleConstants.QUEUE)
public class LastestMonitorCommand extends AbstractCommand<LastestMonitorCommand.Result> {

    private String monitorTerminalId;

    public String getMonitorTerminalId() {
        return monitorTerminalId;
    }

    public void setMonitorTerminalId(String monitorTerminalId) {
        this.monitorTerminalId = monitorTerminalId;
    }

    @Override
    public Class<? extends LastestMonitorCommand.Result> getResultType() {
        return LastestMonitorCommand.Result.class;
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
