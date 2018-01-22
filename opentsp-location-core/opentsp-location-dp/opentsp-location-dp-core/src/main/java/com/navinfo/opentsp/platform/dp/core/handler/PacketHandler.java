package com.navinfo.opentsp.platform.dp.core.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.dp.command.PacketCommand;
import com.navinfo.opentsp.platform.dp.core.acceptor.rp.RPCommand;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * User: zhanhk
 * Date: 16/9/5
 * Time: 下午6:14
 */
@Component
public class PacketHandler extends AbstractCommandHandler<PacketCommand, CommandResult> {

    @Autowired
    private RpProtocolDispatcher protocolDispatcher;

    protected static final Logger logger = LoggerFactory.getLogger(PacketHandler.class);

    public PacketHandler() {
        super(PacketCommand.class, CommandResult.class);
    }

    public PacketHandler(Class<PacketCommand> commandType, Class<CommandResult> resultType) {
        super(commandType, resultType);
    }

    @Override
    public CommandResult handle(PacketCommand packetCommand) {
        RPCommand rpCommand = protocolDispatcher.getHandler(packetCommand.getPacket().getCommandForHex());
        if(rpCommand != null) {
            logger.info("RP-->DP 接收MQ指令成功!{}",packetCommand.getPacket().toString()+"["+packetCommand.getPacket().getContentForHex()+"]");
            rpCommand.processor(packetCommand.getPacket());
        }else {
            logger.error("rp command id is null !{}",packetCommand.getPacket().getCommandForHex());
        }
        return new CommandResult().fillResult(ResultCode.OK);
    }
}
