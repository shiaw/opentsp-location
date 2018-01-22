package com.navinfo.opentsp.platform.dp.core.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.dp.command.RefreshGroupDpCacheCommand;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 刷新DP队列下全部服务缓存缓存
 * User: zhanhk
 * Date: 16/9/12
 * Time: 下午2:48
 */
@Component
public class RefreshGroupDpCacheHandler extends AbstractCommandHandler<RefreshGroupDpCacheCommand, CommandResult> {

    @Autowired
    private RpProtocolDispatcher protocolDispatcher;

    protected static final Logger logger = LoggerFactory.getLogger(RefreshGroupDpCacheHandler.class);

    public RefreshGroupDpCacheHandler() {
        super(RefreshGroupDpCacheCommand.class, CommandResult.class);
    }

    public RefreshGroupDpCacheHandler(Class<RefreshGroupDpCacheCommand> commandType, Class<CommandResult> resultType) {
        super(commandType, resultType);
    }

    @Override
    public CommandResult handle(RefreshGroupDpCacheCommand packetCommand) {
        RPCommand rpCommand = protocolDispatcher.getHandler(packetCommand.getPacket().getCommandForHex());
        if(rpCommand != null) {
            logger.info("RP-->DP 接收MQ组播指令成功!{}",packetCommand.getPacket().toString()+"["+packetCommand.getPacket().getContentForHex()+"]");
            rpCommand.processor(packetCommand.getPacket());
        }else {
            logger.error("rp group command id is null !{}",packetCommand.getPacket().getCommand());
        }
        return new CommandResult().fillResult(ResultCode.OK);
    }
}
