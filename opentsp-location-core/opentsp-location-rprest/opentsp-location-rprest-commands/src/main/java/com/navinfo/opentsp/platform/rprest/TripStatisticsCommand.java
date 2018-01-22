package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.Command;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

import java.util.List;

/**
 * Created by 修伟 on 2017/11/8 0008.
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class TripStatisticsCommand extends AbstractCommand<TripStatisticsCommand.Result> {
    private List<Long> terminalId;
    private long beginDate;
    private long endDate;

    public List<Long> getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(List<Long> terminalId) {
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
    public Class<? extends Result> getResultType() {
        return TripStatisticsCommand.Result.class;
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
