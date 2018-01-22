package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * 油门开度统计分析
 * Created by Sunyu on 2017/11/29.
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class StatisticsThrottleAngleCommand extends AbstractCommand<StatisticsThrottleAngleCommand.Result> {

    /**
     * 通信号
     */
    private Long id;

    /**
     * 开始时间yyyy-MM-dd
     */
    private String start;

    /**
     * 结束时间yyyy-MM-dd
     */
    private String end;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    @Override
    public Class<? extends StatisticsThrottleAngleCommand.Result> getResultType() {
        return StatisticsThrottleAngleCommand.Result.class;
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