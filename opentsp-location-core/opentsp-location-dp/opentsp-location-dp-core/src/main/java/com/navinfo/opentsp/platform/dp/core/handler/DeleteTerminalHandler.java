package com.navinfo.opentsp.platform.dp.core.handler;

import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.platform.dp.command.DeleteTerminalCommand;
import com.navinfo.opentsp.platform.dp.core.cache.TerminalCache;
import com.navinfo.opentspcore.common.handler.AbstractCommandHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by yinsihua on 2016/9/22.
 */
@Component
public class DeleteTerminalHandler extends AbstractCommandHandler<DeleteTerminalCommand, CommandResult> {

    @Resource
    private TerminalCache terminalCache;

    public DeleteTerminalHandler() {
        super(DeleteTerminalCommand.class, CommandResult.class);
    }

    public DeleteTerminalHandler(Class<DeleteTerminalCommand> commandType, Class<CommandResult> resultType) {
        super(commandType, resultType);
    }

    @Override
    public CommandResult handle(DeleteTerminalCommand deleteTerminalCommand) {

        Long[] terminalInfos = deleteTerminalCommand.getTerminalInfos();

        if (terminalInfos != null) {
            for (Long terminalId : terminalInfos) {
                terminalCache.removeTerminalEntity(terminalId);
            }
        }


        return new CommandResult().fillResult(ResultCode.OK);
    }
}
