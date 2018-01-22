package com.navinfo.opentsp.platform.push.handler;

import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.push.TerminalAddCommand;
import com.navinfo.opentsp.platform.push.TerminalDelCommand;
import com.navinfo.opentsp.platform.push.online.RedisOnlineStatusStorage;
import com.navinfo.opentsp.platform.push.online.RedisTerminalInfoStorage;
import com.navinfo.opentsp.platform.push.online.TerminalInfo;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class TerminalDelHandler extends AbstractCommandHandler<TerminalDelCommand, TerminalDelCommand.Result> {
    private static final Logger LOG = LoggerFactory.getLogger(TerminalDelHandler.class);


    @Autowired
    private RedisTerminalInfoStorage redisTerminalInfoStorage;
    @Autowired
    private RedisOnlineStatusStorage redisOnlineStatusStorage;

    public TerminalDelHandler() {
        super(TerminalDelCommand.class, TerminalDelCommand.Result.class);
    }


    @Override
    public TerminalDelCommand.Result handle(TerminalDelCommand command) {
        TerminalDelCommand.Result commandResult = new TerminalDelCommand.Result();
        commandResult.setResultCode(ResultCode.OK.code());

        for(String terminalId:command.getTerminalIds()) {
//            TerminalInfo terminalInfo = redisTerminalInfoStorage.get(terminalId);
//            if (terminalInfo == null) {
//                commandResult.setResultCode(ResultCode.CLIENT_ERROR.code());
//                commandResult.setMessage("终端信息不存在！");
//                return commandResult;
//            }
            redisTerminalInfoStorage.unregister(terminalId.startsWith("0")?terminalId:"0"+terminalId);
            LOG.info("终端信息删除成功,终端ID={}",terminalId.startsWith("0")?terminalId:"0"+terminalId);
        }
        return commandResult;
    }

}
