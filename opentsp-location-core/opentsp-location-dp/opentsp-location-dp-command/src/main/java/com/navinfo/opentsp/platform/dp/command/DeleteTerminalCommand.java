package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;

/**
 * Created by yinsihua on 2016/9/22.
 */
@DpQueue
public class DeleteTerminalCommand extends AbstractCommand<CommandResult> {

    private Long[] terminalInfos;


    public Long[] getTerminalInfos() {
        return terminalInfos;
    }

    public void setTerminalInfos(Long[] terminalInfos) {
        this.terminalInfos = terminalInfos;
    }

    @Override
    public Class<? extends CommandResult> getResultType() {
        return CommandResult.class;
    }
}
