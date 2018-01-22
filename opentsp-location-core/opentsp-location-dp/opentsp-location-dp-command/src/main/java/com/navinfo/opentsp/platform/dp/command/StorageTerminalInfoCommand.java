package com.navinfo.opentsp.platform.dp.command;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;

import java.util.Map;

/**
 * Created by yinsihua on 2016/9/22.
 */
@DpQueue
public class StorageTerminalInfoCommand extends AbstractCommand<CommandResult> {

    private Map<String, Object> terminalInfo;

    public Map<String, Object> getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(Map<String, Object> terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    @Override
    public Class<? extends CommandResult> getResultType() {
        return CommandResult.class;
    }
}
