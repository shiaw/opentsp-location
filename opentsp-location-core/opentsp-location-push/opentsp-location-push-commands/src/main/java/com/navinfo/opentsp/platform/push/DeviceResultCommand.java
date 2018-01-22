package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

import javax.validation.constraints.NotNull;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/5
 * @modify
 * @copyright opentsp
 */
@MessageGroup(PushModuleConstants.QUEUE)
public class DeviceResultCommand extends AbstractCommand<Command.Result> {

    @NotNull
    private String id;

    private byte[] message;

    private String commandId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class Result extends CommandResult {

        private String receiptCode;//终端回执码

        private Object data;

        public Result() {
        }

        public Result(Integer resultCode, String message) {
            super(resultCode, message);
        }

        public String getReceiptCode() {
            return receiptCode;
        }

        public void setReceiptCode(String receiptCode) {
            this.receiptCode = receiptCode;
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }
    }

    @Override
    public Class<? extends Result> getResultType() {
        return Result.class;
    }
}
