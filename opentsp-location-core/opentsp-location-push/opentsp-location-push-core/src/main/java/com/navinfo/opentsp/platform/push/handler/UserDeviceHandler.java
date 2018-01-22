package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.*;
import com.navinfo.opentsp.platform.push.event.OperationLogEvent;
import com.navinfo.opentsp.platform.push.impl.DeliveryProcessor;
import com.navinfo.opentsp.platform.push.impl.UserDeviceRouter;
import com.navinfo.opentsp.platform.push.online.DeviceRegistration;
import com.navinfo.opentsp.platform.push.online.OnlineDeliverer;
import com.navinfo.opentsp.platform.push.online.OnlineStatusStorage;
import com.navinfo.opentsp.platform.push.online.RedisCommandStatusStorage;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import com.navinfo.opentspcore.common.push.PushConfig;
import com.navinfo.opentspcore.common.userdevice.UserDeviceCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * Basic class for proxing {@link UserDeviceCommand}
 */
public class UserDeviceHandler<C extends UserDeviceCommand<?>> extends AbstractCommandHandler<C, CommandResult> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDeviceHandler.class);
    private DeliveryProcessor processor;

    @Autowired
    private OnlineStatusStorage storage;

    @Autowired
    private RedisCommandStatusStorage commandStatusStorage;

    @Autowired
    private MessageChannel messageChannel;
    @Autowired
    private ApplicationContext context;

    @Value("${opentsp.push.openOperationLog:true}")
    private boolean openOperationLog;

    public UserDeviceHandler(Class<C> commandType) {
        super(commandType, CommandResult.class);
    }

    public DeliveryProcessor getProcessor() {
        return processor;
    }

    @Autowired
    public void setProcessor(DeliveryProcessor processor) {
        this.processor = processor;
    }

    @Override
    public CommandResult handle(C command) {
        LOG.info("DeviceCommand=[id:{},device:]",command.getId(),command.getDevice());
        //第一步，判断设备是否在线，不在线则直接返回
        DeviceRegistration deviceRegistration = storage.get(command.getDevice());
        CommandResult result = new CommandResult().fillResult(ResultCode.OK);
        if (deviceRegistration == null || deviceRegistration.getStatus() == DeviceStatus.OFFLINE) {
            LOG.error("{}设备不在线", command.getDevice());
            result.setMessage("终端不在线");
            result.setResultCode(ResultCode.OK.code());
            return result;
        }
//
//        ResultNoticeCommand resultNoticeCommand = new ResultNoticeCommand();
//        resultNoticeCommand.setId(command.getId());
//        resultNoticeCommand.setMessage(null);
//        messageChannel.send(resultNoticeCommand, command.getQueueName());

        PushConfig pushConfig = command.getPushConfig();
        ScheduledTask task = ScheduledTask.builder()
                .id(command.getId())
                .router(UserDeviceRouter.NAME)
                .routerArgument(UserDeviceRouter.toRouterArgument(command.getDevice()))
                .deliveryMethods(OnlineDeliverer.ID)
                .ttl(pushConfig == null ? 3600l : pushConfig.getTtl())
                .persistent(pushConfig == null ? false : pushConfig.isPersisted())
                .listenerQueue("")//TODO import it from command
                .message(command)
                .build();
        DownCommand downCommand = new DownCommand();
        downCommand.setCommandId(command.getId());
        downCommand.setState(DownCommandState.INIT.getValue());
        downCommand.setDate(new SimpleDateFormat("yyyy-MM-dd mm:HH:ss").format(new Date()));
        downCommand.setQueueName(command.getQueueName());
        downCommand.setDevice(command.getDevice());
        commandStatusStorage.add(downCommand);
        if (openOperationLog) {
            OperationLogEvent operationLogEvent = new OperationLogEvent(this, (DeviceCommand) command, DownCommandState.INIT.getValue());
            context.publishEvent(operationLogEvent);
        }
        try {
            this.processor.schedule(task);
        } catch (Exception e) {
            LoggerFactory.getLogger(getClass()).error("On " + command, e);
            result.setMessage(e.getMessage());
            result.setResultCode(ResultCode.SERVER_ERROR.code());
        }

        return result;
    }

}
