package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * com.navinfo.opentsp.platform.rprest
 *
 * @author zhangdong
 * @date 2017/11/29
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class CarsCurrentOnlineRankCommand extends AbstractCommand<CarsInDistrictOnlineNumCommand.Result> {

    @Override
    public Class<? extends CarsInDistrictOnlineNumCommand.Result> getResultType() {
        return CarsInDistrictOnlineNumCommand.Result.class;
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
