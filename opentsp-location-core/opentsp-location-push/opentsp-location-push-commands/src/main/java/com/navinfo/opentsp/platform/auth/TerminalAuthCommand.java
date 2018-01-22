package com.navinfo.opentsp.platform.auth;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import com.navinfo.opentsp.platform.push.PushModuleConstants;
import org.springframework.security.access.annotation.Secured;

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
public class TerminalAuthCommand extends AbstractCommand<Command.Result> {

    @NotNull
    private String deviceId;

    private String authCode;

    @NotNull
    private String returnAddress;

    private Map params;

    private String cmd;

    private String sendId;

    private  boolean isAuth;

    public String getSendId() {
        return sendId;
    }

    public void setSendId(String sendId) {
        this.sendId = sendId;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getReturnAddress() {
        return returnAddress;
    }

    public void setReturnAddress(String returnAddress) {
        this.returnAddress = returnAddress;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Map getParams() {
        return params;
    }

    public void setParams(Map params) {
        this.params = params;
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

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }
}
