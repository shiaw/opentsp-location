package com.navinfo.opentsp.platform.monitor;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * Created by zhangyue on 2017/6/14.
 */
@MessageGroup(MonitorModuleConstants.QUEUE)
public class LocationMonitorCommand extends AbstractCommand<LocationMonitorCommand.Result> {
    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
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
