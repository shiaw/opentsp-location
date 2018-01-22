package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 *  2.3	获取行政区域最近7天的在线车辆数
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class CarsDistrictLastestDaysNumsCommand extends AbstractCommand<CarsDistrictLastestDaysNumsCommand.Result> {

    private Integer districtType;

    private Integer districtCode;

    public Integer getDistrictType() {
        return districtType;
    }

    public void setDistrictType(Integer districtType) {
        this.districtType = districtType;
    }

    public Integer getDistrictCode() {
        return districtCode;
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
