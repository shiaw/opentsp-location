package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * 2.2	获取在线车辆数排行接口
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class CarsInDistrictOnlineNumCommand extends AbstractCommand<CarsInDistrictOnlineNumCommand.Result> {

    private String queryDay;

    private Integer districtType;

    private Integer districtCode;

    public String getQueryDay() {
        return queryDay;
    }

    public Integer getDistrictType() {
        return districtType;
    }

    public Integer getDistrictCode() {
        return districtCode;
    }

    public void setQueryDay(String queryDay) {
        this.queryDay = queryDay;
    }

    public void setDistrictType(Integer districtType) {
        this.districtType = districtType;
    }

    public void setDistrictCode(Integer districtCode) {
        this.districtCode = districtCode;
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
