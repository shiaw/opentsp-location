package com.navinfo.opentsp.platform.auth;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import com.navinfo.opentsp.platform.push.DownCommandState;
import com.navinfo.opentsp.platform.push.PushModuleConstants;

import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/4
 * @modify
 * @copyright opentsp
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class DownStatusCommand extends AbstractCommand<Command.Result> {

    @NotNull
    private String id;

    private String commandId;

    private DownCommandState state;

    private String businessCode;

    private int command;
    private int Protocol;
    private int serialNumber;

    private Object data;

    public String getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DownCommandState getState() {
        return state;
    }

    public void setState(DownCommandState state) {
        this.state = state;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getCommandId() {
        return commandId;
    }

    public void setCommandId(String commandId) {
        this.commandId = commandId;
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public int getProtocol() {
        return Protocol;
    }

    public void setProtocol(int protocol) {
        Protocol = protocol;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public static class Result extends CommandResult {

        private Map results;

        public Result() {
        }

        public Result(Integer resultCode, String message, Map results) {
            super(resultCode, message);
            this.results = results;
        }

        public Map getResults() {
            return results;
        }

        public void setResults(Map results) {
            this.results = results;
        }
    }


    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }
}
