package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.TerminalMileageOilQueryCommand;
import com.navinfo.opentsp.platform.rprest.TerminalMileageOilUpdateCommand;
import com.navinfo.opentsp.platform.rprest.entity.TerminalMileageOilEntity;
import com.navinfo.opentsp.platform.rprest.service.TerminalMileageOilService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by machi on 2017/6/21.
 */

@Component
public class TerminalMileageOilUpdateHandler extends AbstractCommandHandler<TerminalMileageOilUpdateCommand, TerminalMileageOilUpdateCommand.Result> {

    private static final Logger LOG = LoggerFactory.getLogger(TerminalMileageOilUpdateHandler.class);
    @Autowired
    private TerminalMileageOilService terminalMileageOilService;

    public TerminalMileageOilUpdateHandler() {
        super(TerminalMileageOilUpdateCommand.class, TerminalMileageOilUpdateCommand.Result.class);
    }

    @Override
    public TerminalMileageOilUpdateCommand.Result handle(TerminalMileageOilUpdateCommand command) {
        LOG.debug("进入修改里程油耗标准Handler层");
        TerminalMileageOilUpdateCommand.Result commandResult = new TerminalMileageOilUpdateCommand.Result();
        int i= 0;
        if(null!=command.getTerminalId()){
            try {
                LOG.debug("终端编号"+command.getTerminalId());
                if("".equals(command.getMileageType())&&"".equals(command.getOilType())){
                    commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                    commandResult.setMessage("mileageType,oilType为空");
                    return commandResult;
                }else{
                    i = terminalMileageOilService.update(command.getTerminalId(),Integer.parseInt(command.getMileageType()),Integer.parseInt(command.getOilType()));
                }
                if(i==1){
                    commandResult.setResultCode(ResultCode.OK.code());
                    commandResult.setMessage("更新成功");
                }else{
                    commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
                    commandResult.setMessage("更新失败");
                }
            } catch (Exception e) {
                LOG.error("修改里程油耗标准出错！{}", command, e);
                commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
                commandResult.setMessage("服务器错误");
            }
        }
        return commandResult;
    }

}
