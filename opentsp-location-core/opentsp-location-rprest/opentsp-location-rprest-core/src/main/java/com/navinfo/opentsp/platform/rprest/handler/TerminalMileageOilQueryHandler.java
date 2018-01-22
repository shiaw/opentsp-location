package com.navinfo.opentsp.platform.rprest.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.rprest.TerminalMileageOilQueryCommand;
import com.navinfo.opentsp.platform.rprest.entity.TerminalMileageOilEntity;
import com.navinfo.opentsp.platform.rprest.service.TerminalMileageOilService;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyue on 2017/6/20.
 */

@Component
public class TerminalMileageOilQueryHandler extends AbstractCommandHandler<TerminalMileageOilQueryCommand, TerminalMileageOilQueryCommand.Result> {

    private static final Logger LOG = LoggerFactory.getLogger(TerminalMileageOilQueryHandler.class);
    @Autowired
    private TerminalMileageOilService terminalMileageOilService;

    public TerminalMileageOilQueryHandler() {
        super(TerminalMileageOilQueryCommand.class, TerminalMileageOilQueryCommand.Result.class);
    }

    @Override
    public TerminalMileageOilQueryCommand.Result handle(TerminalMileageOilQueryCommand command) {
        LOG.debug("进入查询里程油耗标准Handler层");
        TerminalMileageOilQueryCommand.Result commandResult = new TerminalMileageOilQueryCommand.Result();
        Long terminalId = command.getTerminalId();
        try {
            if (null != terminalId) {
                LOG.debug("查询终端"+terminalId);
                TerminalMileageOilEntity data = terminalMileageOilService.query(terminalId);
                commandResult.setResultCode(ResultCode.OK.code());
                commandResult.setData(data);
            } else {
                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
                commandResult.setMessage("参数有误");
            }
        } catch (Exception e) {
            LOG.error("获取里程油耗标准出错！{}", command, e);
            commandResult.setResultCode(ResultCode.SERVER_ERROR.code());
            commandResult.setMessage("服务器错误");
        }
        return commandResult;
    }
}
