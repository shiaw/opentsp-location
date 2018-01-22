package com.navinfo.opentsp.platform.dp.core.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.dp.command.StorageTerminalInfoCommand;
import com.navinfo.opentsp.platform.dp.core.acceptor.da.DACommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by yinsihua on 2016/9/22.
 */
@Component
public class StorageTerminalInfoHandler extends AbstractCommandHandler<StorageTerminalInfoCommand, CommandResult> {

    @Autowired
    private DaProtocolDispatcher daProtocolDispatcher;

    public StorageTerminalInfoHandler() {
        super(StorageTerminalInfoCommand.class, CommandResult.class);
    }

    public StorageTerminalInfoHandler(Class<StorageTerminalInfoCommand> commandType, Class<CommandResult> resultType) {
        super(commandType, resultType);
    }

    @Override
    public CommandResult handle(StorageTerminalInfoCommand storageTerminalInfoCommand) {
        Packet packet = new Packet();
        DACommand daCommand = daProtocolDispatcher.getHandler("0902");

        Map<String, Object> terminalInfo = storageTerminalInfoCommand.getTerminalInfo();
        if (terminalInfo != null) {
            LCTerminalInfo.TerminalInfo.Builder builder = LCTerminalInfo.TerminalInfo.newBuilder();
            Long terminalId = terminalInfo.get("terminal_id") != null ? Long.valueOf(String.valueOf(terminalInfo.get("terminal_id"))): null;

            if (terminalId != null) {
                builder.setTerminalId(terminalId);
                builder.setProtocolType(terminalInfo.get("proto_code") != null ? Integer.valueOf(String.valueOf(terminalInfo.get("proto_code"))) : null);
//                builder.setNodeCode(terminalInfo.get("terminal_id") != null ? Integer.valueOf(String.valueOf(terminalInfo.get("terminal_id"))) : null);
//                builder.setAuthCode(terminalInfo.get("terminal_id") != null ? String.valueOf(terminalInfo.get("terminal_id")) : null);
                Integer regularInTerminal = terminalInfo.get("regular_in_terminal") != null ? Integer.valueOf(String.valueOf(terminalInfo.get("regular_in_terminal"))): null;
                builder.setRegularInTerminal(regularInTerminal == 0 ? false : true);

                packet.setContent(builder.build().toByteArray());
                daCommand.processor(packet);
            }
        }

        return new CommandResult().fillResult(ResultCode.OK);
    }
}
