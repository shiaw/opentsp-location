package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.auth.TerminalRefreshCommand;
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
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class TerminalRefreshHandler extends AbstractCommandHandler<TerminalRefreshCommand, TerminalRefreshCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalRefreshHandler.class);

    @Autowired
    private TerminalServiceImpl terminalService;


    public TerminalRefreshHandler() {
        super(TerminalRefreshCommand.class, TerminalRefreshCommand.Result.class);
    }

    @Override
    public TerminalRefreshCommand.Result handle(TerminalRefreshCommand command) {
        TerminalRefreshCommand.Result commandResult = new TerminalRefreshCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());
        String terminalId=command.getTerminalId();
        if(StringUtils.isEmpty(terminalId)) {
            terminalService.loadTerminal();
        }else{
            terminalService.refreshTerminalById(terminalId);
        }
        return commandResult;
    }


}
