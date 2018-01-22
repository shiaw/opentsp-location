package com.navinfo.opentsp.gateway.tcp.proto.locationrp.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DpProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.gateway.userdevice.DeviceHandler;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Lenovo
 * @date 2016-09-28
 * @modify
 * @copyright
 */
@Component
public class ReceivePushHandler extends AbstractCommandHandler<ResultNoticeCommand, ResultNoticeCommand.Result> {

    private static final Logger logger = LoggerFactory.getLogger(DeviceHandler.class);

    @Autowired
    private DpProtocolDispatcher dpProtocolDispatcher;

    public ReceivePushHandler() {
        super(ResultNoticeCommand.class, ResultNoticeCommand.Result.class);
    }

    public ReceivePushHandler(Class<ResultNoticeCommand> commandType, Class<ResultNoticeCommand.Result> resultType) {
        super(commandType, resultType);
    }

    @Override
    public ResultNoticeCommand.Result handle(ResultNoticeCommand command) {
        ResultNoticeCommand.Result result = new ResultNoticeCommand.Result();
        //String id = command.getId();
        String commandId = command.getCommandId();
        if (null != commandId) {
            PushCommand handler = dpProtocolDispatcher.getHandler(commandId);
            handler.handle(command);
        }
        result.setResultCode(ResultCode.OK.code());
        return result;
    }

}
