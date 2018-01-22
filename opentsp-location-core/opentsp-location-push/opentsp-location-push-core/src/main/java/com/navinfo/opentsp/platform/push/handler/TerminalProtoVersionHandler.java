package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.TerminalProtoVersionCommand;
import com.navinfo.opentsp.platform.auth.TerminalRegisterCommand;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.impl.TerminalServiceImpl;
import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.online.TerminalInfo;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class TerminalProtoVersionHandler extends AbstractCommandHandler<TerminalProtoVersionCommand, TerminalProtoVersionCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalProtoVersionHandler.class);

    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    @Autowired
    private MessageChannel messageChannel;

    private static final String GET_PROTO_VERSION_COMMAND = "getProtoVersion";

    public TerminalProtoVersionHandler() {
        super(TerminalProtoVersionCommand.class, TerminalProtoVersionCommand.Result.class);
    }

    @Override
    public TerminalProtoVersionCommand.Result handle(TerminalProtoVersionCommand command) {
        TerminalProtoVersionCommand.Result commandResult = new TerminalProtoVersionCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        TerminalInfo terminalInfo = redisTerminalInfoStorage.get(command.getDeviceId());
        if (terminalInfo == null) {
            LOG.info("未查询到终端信息[{}]", command.getDeviceId());
            return commandResult;
        }
        LOG.info("{}的版本信息号{}",command.getDeviceId(),terminalInfo.getProtocolType());
        Map arguments = new HashMap();
        arguments.put("protoVersion", String.valueOf(terminalInfo.getProtocolType()));
        DeviceCommand deviceCommand = new DeviceCommand();
        deviceCommand.setCommand(GET_PROTO_VERSION_COMMAND);
        deviceCommand.setDevice(command.getDeviceId());
        deviceCommand.setId(command.getSendId());
        deviceCommand.setArguments(arguments);
        deviceCommand.setId(command.getSendId());
        deviceCommand.setResultCode(commandResult.getResultCode());
        try {
            this.messageChannel.send(deviceCommand, command.getReturnAddress());
        } catch (Exception e) {
            LOG.error("异步回写失败，{}", e);
        }

        return commandResult;
    }


}
