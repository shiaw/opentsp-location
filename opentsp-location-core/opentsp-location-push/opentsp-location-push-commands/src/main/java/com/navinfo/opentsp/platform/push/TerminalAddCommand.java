package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/23
 * @modify
 * @copyright opentsp
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class TerminalAddCommand extends AbstractCommand<Command.Result> {

    private String terminalId;
    private int protocolType;
    private String authCode;
    private boolean isAuth;
    private String deviceId;//设备id 厂商的硬件ID
    private String deviceType;//设备类型 1.电动车 默认是其他车厂
    private String deviceProtolType;//协议类型

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public int getProtocolType() {
        return protocolType;
    }

    public void setProtocolType(int protocolType) {
        this.protocolType = protocolType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public boolean isAuth() {
        return isAuth;
    }

    public void setAuth(boolean isAuth) {
        this.isAuth = isAuth;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceProtolType() {
        return deviceProtolType;
    }

    public void setDeviceProtolType(String deviceProtolType) {
        this.deviceProtolType = deviceProtolType;
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
