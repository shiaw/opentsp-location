package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.TerminalAuthCommand;
import com.navinfo.opentsp.platform.auth.TerminalLogoutCommand;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.online.*;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 设备退出
 */
@Component
public class TerminalLogoutHandler extends AbstractCommandHandler<TerminalLogoutCommand, TerminalLogoutCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalLogoutHandler.class);


    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    @Autowired
    private RedisOnlineStatusStorage redisOnlineStatusStorage;

    public TerminalLogoutHandler() {
        super(TerminalLogoutCommand.class, TerminalLogoutCommand.Result.class);
    }


    @Override
    public TerminalLogoutCommand.Result handle(TerminalLogoutCommand command) {
        TerminalLogoutCommand.Result commandResult = new TerminalLogoutCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        if(StringUtils.isEmpty(command.getDeviceId())){
            LOG.info("device is null");
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            return commandResult;
        }
        DeviceRegistration deviceRegistration= redisOnlineStatusStorage.get(command.getDeviceId());
        if (deviceRegistration==null){
            LOG.info("device online is null");
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            return commandResult;
        }
        if(deviceRegistration.getReturnAddress().equals(command.getQname())) {
            redisOnlineStatusStorage.unregister(command.getDeviceId());
            redisTerminalInfoStorage.unregister(command.getDeviceId());
            LOG.info("终端[{}]下线", command.getDeviceId());
        }
        return commandResult;
    }

}
