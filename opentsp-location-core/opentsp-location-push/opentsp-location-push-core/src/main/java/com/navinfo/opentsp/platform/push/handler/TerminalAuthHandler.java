package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.common.utils.Callback;
import com.navinfo.opentsp.platform.auth.TerminalAuthCommand;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.DeviceResult;
import com.navinfo.opentsp.platform.push.impl.TerminalServiceImpl;
import com.navinfo.opentsp.platform.push.online.OnlineUsers;
import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.online.TerminalInfo;
import com.navinfo.opentsp.platform.push.persisted.TerminalMileageOilTypeEntry;
import com.navinfo.opentspcore.common.gateway.ConnectionEventCommand;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import com.navinfo.opentspcore.common.userdevice.UserDeviceCommand;
import com.navinfo.opentspcore.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.Map;


@Component
public class TerminalAuthHandler extends AbstractCommandHandler<TerminalAuthCommand, TerminalAuthCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalAuthHandler.class);


    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    private final OnlineUsers onlineUsers;

    @Autowired
    private TerminalServiceImpl terminalService;

    @Autowired
    private MessageChannel messageChannel;

    @Autowired
    public TerminalAuthHandler(OnlineUsers onlineUsers) {
        super(TerminalAuthCommand.class, TerminalAuthCommand.Result.class);
        this.onlineUsers = onlineUsers;
    }


    @Override
    public TerminalAuthCommand.Result handle(TerminalAuthCommand command) {
        TerminalAuthCommand.Result commandResult = new TerminalAuthCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        //不健全，直接执行在线列表
        if(!command.isAuth()){
          //  LOG.info("{}不执行鉴权处理",command.getDeviceId());
            TerminalInfo terminalInfo=new TerminalInfo();
            terminalInfo.setTerminalId(command.getDeviceId());
            terminalInfo.setAuth(true);
            redisTerminalInfoStorage.register(terminalInfo);
            onlineUsers.online(command.getDeviceId(), command.getReturnAddress());
            LOG.info("终端[{}]上线",command.getDeviceId());
            return commandResult;
        }

        TerminalInfo terminalInfo = redisTerminalInfoStorage.get(command.getDeviceId());
        if (terminalInfo == null) {
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
            return commandResult;
        }
        LOG.info("{},{}",command.getAuthCode(),terminalInfo.getAuthCode());
        //鉴权成功
        if (!StringUtils.isEmpty(terminalInfo.getAuthCode())&& terminalInfo.getAuthCode().equals(command.getAuthCode())) {
            LOG.info("redis 获取{}", JsonUtils.pojo2Json(terminalInfo));
            terminalInfo.setProtocolType(terminalInfo.getProtocolType());
            terminalInfo.setAuth(true);
            terminalInfo.setAuthCode(terminalInfo.getAuthCode());
            LOG.info("鉴权成功[{}],终端[{}]上线,协议类型{}",command.getAuthCode(),command.getDeviceId(),terminalInfo.getProtocolType());
            redisTerminalInfoStorage.register(terminalInfo);
            onlineUsers.online(command.getDeviceId(), command.getReturnAddress());
        } else {
            commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
        }
        Map arguments = new HashMap();
        arguments.put("authCode", command.getAuthCode());
        long startTime=System.currentTimeMillis();
        TerminalMileageOilTypeEntry terminalMileageOilTypeEntry=terminalService.getTerminalMileageOilType(Long.parseLong(command.getDeviceId()));
        LOG.info("查询{}里程油耗类型耗时{}ms,",command.getDeviceId(),System.currentTimeMillis()-startTime);
        if(terminalMileageOilTypeEntry!=null) {
            LOG.info("查询{}里程油耗类型,mileageType:{},oilType{}",command.getDeviceId(),terminalMileageOilTypeEntry.getMileageType(),terminalMileageOilTypeEntry.getOilType());
            arguments.put("mileageType",terminalMileageOilTypeEntry.getMileageType());
            arguments.put("oilType",terminalMileageOilTypeEntry.getOilType());
        }
        DeviceCommand deviceCommand = new DeviceCommand();
        deviceCommand.setCommand(command.getCmd());
        deviceCommand.setDevice(command.getDeviceId());
        deviceCommand.setId(command.getSendId());
        deviceCommand.setArguments(arguments);
        deviceCommand.setResultCode(commandResult.getResultCode());
        try {
            this.messageChannel.send(deviceCommand, command.getReturnAddress());
        } catch (Exception e) {
            LOG.error("鉴权异步回写失败，{}", e);
        }

        return commandResult;
    }

}
