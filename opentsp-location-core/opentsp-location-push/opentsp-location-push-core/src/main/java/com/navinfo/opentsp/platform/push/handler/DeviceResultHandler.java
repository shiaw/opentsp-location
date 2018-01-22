package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.push.DeviceResultCommand;
import com.navinfo.opentsp.platform.push.DownCommand;
import com.navinfo.opentsp.platform.push.DownCommandState;
import com.navinfo.opentsp.platform.push.online.RedisCommandStatusStorage;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/4
 * @modify
 * @copyright opentsp
 */
@Component
public class DeviceResultHandler extends AbstractCommandHandler<DeviceResultCommand, DeviceResultCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceResultHandler.class);


    @Autowired
    private RedisCommandStatusStorage storage;

    public DeviceResultHandler() {
        super(DeviceResultCommand.class, DeviceResultCommand.Result.class);
    }


    @Override
    public DeviceResultCommand.Result handle(DeviceResultCommand command) {
        LOG.info("DeviceResultCommand=[id:{}]",command.getId());
        DeviceResultCommand.Result commandResult = new DeviceResultCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        try {
            DownCommand downCommand= storage.get(command.getId());
            if(downCommand==null){
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("未找到改下发指令");
                return commandResult;
            }
            commandResult.setResultCode(downCommand.getState());
            commandResult.setMessage(DownCommandState.adapt(downCommand.getState()).getMessage());
            commandResult.setReceiptCode(downCommand.getBusinessCode());
            commandResult.setData(downCommand.getData());

        } catch (Exception e) {
            LOG.error("{}查询指令状态失败{},原因：{}", command.getId(), e.getMessage());
        }
        return commandResult;
    }
}