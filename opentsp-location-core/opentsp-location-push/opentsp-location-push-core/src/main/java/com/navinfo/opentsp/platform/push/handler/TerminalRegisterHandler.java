package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.TerminalRegisterCommand;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.impl.TerminalServiceImpl;
import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.online.TerminalInfo;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import com.navinfo.opentspcore.common.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class TerminalRegisterHandler extends AbstractCommandHandler<TerminalRegisterCommand, TerminalRegisterCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalRegisterHandler.class);

    @Autowired
    private TerminalServiceImpl terminalService;

    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;

    @Autowired
    private MessageChannel messageChannel;

    public TerminalRegisterHandler() {
        super(TerminalRegisterCommand.class, TerminalRegisterCommand.Result.class);
    }

    @Override
    public TerminalRegisterCommand.Result handle(TerminalRegisterCommand command) {
        TerminalRegisterCommand.Result commandResult = new TerminalRegisterCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        Map params = command.getParams();
        int province = (Integer) params.get("province");
        int city = (Integer) params.get("city");
        String produce = (String) params.get("produce");
        String terminalModel = (String) params.get("terminalModel");
        String deviceId = (String) params.get("terminalIdentify");
        String license = (String) params.get("license");
        String terminalId = (String) params.get("terminalId");
        int licenseColor = (Integer) params.get("licenseColor");

        LOG.info("收到终端注册数据：省域：" + province + ",市县域：" + city + ",制造商：" + produce + ",终端型号：" + terminalModel + ",终端ID：" + deviceId + ",车牌颜色：" + licenseColor + ",车牌：" + license);
        String authCode = terminalService.register(province, city, produce, terminalModel, deviceId, license, terminalId, licenseColor);
        Map arguments = new HashMap();
        arguments.put("hasAuthCoding", true);
        arguments.put("authCoding",authCode);
        DeviceCommand deviceCommand = new DeviceCommand();
        if(params.get("serialNumber")!=null){
            int serialNumber=(Integer)params.get("serialNumber");
            arguments.put("serialnumber",serialNumber);
            LOG.info("注册回流水号{}",serialNumber);
        }
        deviceCommand.setCommand(command.getCmd());
        deviceCommand.setDevice(command.getDeviceId());
        deviceCommand.setId(command.getSendId());
        deviceCommand.setArguments(arguments);
        deviceCommand.setResultCode(commandResult.getResultCode());
        TerminalInfo terminalInfo= redisTerminalInfoStorage.get(command.getDeviceId());
        if (terminalInfo!=null){
            LOG.info("redis 获取{}",JsonUtils.pojo2Json(terminalInfo));
            terminalInfo.setProtocolType(terminalInfo.getProtocolType());
            terminalInfo.setAuthCode(authCode);
            terminalInfo.setAuth(false);
            redisTerminalInfoStorage.register(terminalInfo);
            LOG.info("注册成功，终端[{}]缓存更新鉴权码[{}]，协议类型：{}",command.getDeviceId(),authCode,terminalInfo.getProtocolType());
        }
        try {
            this.messageChannel.send(deviceCommand, command.getReturnAddress());
        } catch (Exception e) {
            LOG.error("鉴权异步回写失败，{}", e);
        }

        return commandResult;
    }


}
