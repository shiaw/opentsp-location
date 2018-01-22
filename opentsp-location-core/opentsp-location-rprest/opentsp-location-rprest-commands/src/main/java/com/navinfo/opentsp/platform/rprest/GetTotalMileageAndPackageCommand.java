package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * Created by wanliang on 2017/5/22.
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class GetTotalMileageAndPackageCommand extends AbstractCommand<GetTotalMileageAndPackageCommand.Result> {



    @Override
    public Class<? extends GetTotalMileageAndPackageCommand.Result> getResultType() {
        return GetTotalMileageAndPackageCommand.Result.class;
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
