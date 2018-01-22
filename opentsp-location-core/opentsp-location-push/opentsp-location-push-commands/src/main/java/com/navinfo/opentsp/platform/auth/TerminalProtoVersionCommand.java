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
public class TerminalProtoVersionCommand extends AbstractCommand<Command.Result> {

    private String deviceId;

    private String sendId;

    private String returnAddress;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

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
