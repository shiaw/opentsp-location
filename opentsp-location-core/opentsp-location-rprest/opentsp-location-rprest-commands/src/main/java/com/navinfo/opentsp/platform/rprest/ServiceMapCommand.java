package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 *  2.6服务站周边车辆信息接口
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class ServiceMapCommand extends AbstractCommand<ServiceMapCommand.Result> {

    private String beginDate;

    private  String endDate;

    public Integer level;

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }


    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }


    @Override
    public Class<? extends Result> getResultType() {
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
