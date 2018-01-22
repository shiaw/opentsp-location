package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * Created by machi on 2017/6/20.
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class TerminalMileageOilUpdateCommand extends AbstractCommand<TerminalMileageOilUpdateCommand.Result> {

    @Override
    public Class<? extends TerminalMileageOilUpdateCommand.Result> getResultType() {
        return TerminalMileageOilUpdateCommand.Result.class;
    }
    private Long terminalId;

    private String mileageType;

    private String oilType;

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public String getMileageType() {
        return mileageType;
    }

    public void setMileageType(String mileageType) {
        this.mileageType = mileageType;
    }

    public String getOilType() {
        return oilType;
    }

    public void setOilType(String oilType) {
        this.oilType = oilType;
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
