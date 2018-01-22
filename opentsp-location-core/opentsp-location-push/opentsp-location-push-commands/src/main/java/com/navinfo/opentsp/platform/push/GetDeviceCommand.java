package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * Created by wanliang on 2017/3/20.
 * 查询终端信息
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class GetDeviceCommand  extends AbstractCommand<Command.Result> {
    private String deviceId;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }


    public static class Result extends CommandResult {


        public Result() { }

        public Result(Integer resultCode, String message) {
            super(resultCode, message);
        }

    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }

}
