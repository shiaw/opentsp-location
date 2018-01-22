package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;
import java.util.List;

@MessageGroup(RpRestModuleConstants.QUEUE)
public class StaytimeParkRecordsCommand extends AbstractCommand<StaytimeParkRecordsCommand.Result>
{
    private List<Long> terminalID;
    private List<Long> areaIds;
    private long startDate;
    private long endDate;

    public List<Long> getTerminalID()
    {
        return this.terminalID;
    }

    public void setTerminalID(List<Long> terminalID) {
        this.terminalID = terminalID;
    }

    public List<Long> getAreaIds() {
        return this.areaIds;
    }

    public void setAreaIds(List<Long> areaIds) {
        this.areaIds = areaIds;
    }

    public long getStartDate() {
        return this.startDate;
    }

    public void setStartDate(long startDate) {
        this.startDate = startDate;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public Class<? extends Result> getResultType()
    {
        return Result.class;
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