package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;

/**
 * @author zhangyue
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class PoiDenseLocationCommand extends AbstractCommand<PoiDenseLocationCommand.Result> {
    private long terminalId;
    private long beginDate;
    private long endDate;
    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }
    @Override
    public Class<? extends PoiDenseLocationCommand.Result> getResultType() {
        return PoiDenseLocationCommand.Result.class;
    }

    public static class Result extends CommandResult {
        private Object data;

        public Result() {
        }

        public Result(Integer resultCode, String message) {
            super(resultCode, message);
        }

        public Object getData() {
            return data;
        }

        public void setData(Object data) {
            this.data = data;
        }


    }

}
