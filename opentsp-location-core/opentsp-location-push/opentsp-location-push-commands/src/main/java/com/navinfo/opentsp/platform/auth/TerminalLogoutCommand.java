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
public class TerminalLogoutCommand extends AbstractCommand<Command.Result> {

    @NotNull
    private String deviceId;

    @NotNull
    private String qname;

    public String getQname() {
        return qname;
    }

    public void setQname(String qname) {
        this.qname = qname;
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
