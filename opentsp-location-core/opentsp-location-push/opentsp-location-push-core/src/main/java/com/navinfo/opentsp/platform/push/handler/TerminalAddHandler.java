package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.TerminalAuthCommand;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.TerminalAddCommand;
import com.navinfo.opentsp.platform.push.online.OnlineUsers;
import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.online.TerminalInfo;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
public class TerminalAddHandler extends AbstractCommandHandler<TerminalAddCommand, TerminalAddCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalAddHandler.class);


    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    public TerminalAddHandler() {
        super(TerminalAddCommand.class, TerminalAddCommand.Result.class);
    }


    @Override
    public TerminalAddCommand.Result handle(TerminalAddCommand command) {
        TerminalAddCommand.Result commandResult = new TerminalAddCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());

        TerminalInfo terminalInfo = redisTerminalInfoStorage.get(command.getTerminalId());
        if (terminalInfo != null) {
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            commandResult.setMessage("终端信息已经存在！");
            return commandResult;
        }
        terminalInfo=new TerminalInfo();
        terminalInfo.setTerminalId(command.getTerminalId().startsWith("0")?command.getTerminalId():"0"+command.getTerminalId());
        terminalInfo.setDeviceId(command.getDeviceId());
        terminalInfo.setProtocolType(command.getProtocolType());
        terminalInfo.setAuthCode("");
        terminalInfo.setAuth(false);
        redisTerminalInfoStorage.register(terminalInfo);
        LOG.info("终端添加成功，terminalId={},版本号={}",command.getTerminalId(),command.getProtocolType());
        return commandResult;
    }

}
