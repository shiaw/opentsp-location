package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentsp.platform.push.DownCommand;
import com.navinfo.opentsp.platform.push.DownCommandState;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import com.navinfo.opentsp.platform.push.event.OperationLogEvent;
import com.navinfo.opentsp.platform.push.impl.DeliveryProcessor;
import com.navinfo.opentsp.platform.push.online.OnlineUsers;
import com.navinfo.opentsp.platform.push.online.RedisCommandStatusStorage;
import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.persisted.Convert;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/4
 * @modify
 * @copyright opentsp
 */
@Component
public class DownStatusHandler extends AbstractCommandHandler<DownStatusCommand, DownStatusCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(DownStatusHandler.class);


    @Autowired
    private RedisCommandStatusStorage storage;

    @Autowired
    private  ObjectFactory<DeliveryProcessor> processor;

    @Autowired
    private MessageChannel messageChannel;

    @Autowired
    private ApplicationContext context;


    @Value("${opentsp.push.openOperationLog:true}")
    private boolean openOperationLog;

    public DownStatusHandler() {
        super(DownStatusCommand.class, DownStatusCommand.Result.class);
    }


    @Override
    public DownStatusCommand.Result handle(DownStatusCommand command) {
        DownStatusCommand.Result commandResult = new DownStatusCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        DownCommand downCommand= storage.get(command.getId());
         try {
            storage.modiflyState(command.getId(), command.getState(), command.getBusinessCode(), command.getData());
        } catch (Exception e) {
            LOG.error("{}更新指令状态失败{},原因：{}", command.getId(), command.getState(), e.getMessage());
        }
        if(openOperationLog) {
            DeviceCommand deviceCommand = new DeviceCommand();
            deviceCommand.setId(command.getId());
            deviceCommand.setDevice(downCommand.getDevice());
            OperationLogEvent operationLogEvent = new OperationLogEvent(this, deviceCommand, command.getState().getValue());
            context.publishEvent(operationLogEvent);
        }
        try {
            ResultNoticeCommand resultNoticeCommand = new ResultNoticeCommand();
            resultNoticeCommand.setId(command.getId());
            resultNoticeCommand.setDevice(downCommand.getDevice());
            resultNoticeCommand.setCommandId(command.getCommandId());
            resultNoticeCommand.setCommand(command.getCommand());
            resultNoticeCommand.setProtocol(command.getProtocol());
            resultNoticeCommand.setSerialNumber(command.getSerialNumber());
            resultNoticeCommand.setMessage(Convert.hexStringToBytes((String) command.getData()));
            LOG.info("下发指令终端回复通知：[sendId={},执行状态={},deviceId={}]",command.getId(),command.getState().getValue(),downCommand.getDevice());
            messageChannel.send(resultNoticeCommand, downCommand.getQueueName());
            LOG.info("下发指令终端回复通知，执行完成！");
        }catch(Exception e){
            LOG.error("未找到对应的handler,DownStatusHandler--ResultNoticeCommand");
        }
        return commandResult;
    }


}