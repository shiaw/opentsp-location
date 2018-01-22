package com.navinfo.opentsp.platform.rprest;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.common.messaging.CommandResult;
import com.navinfo.opentsp.common.messaging.routing.annotation.MessageGroup;

/**
 * 油耗里程统计分析command
 * @Time 2017年11月28日 15:37:17
 * @Author wzw
 */
@MessageGroup(RpRestModuleConstants.QUEUE)
public class GetFuelCommand extends AbstractCommand<GetFuelCommand.Result> {
    /**
     * 通信号
     */
    private Long id;
    /**
     *开始时间yyyy-MM-dd
     */
    private String start;
    /**
     *结束时间yyyy-MM-dd
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
    public Class<? extends GetFuelCommand.Result> getResultType() {
        return GetFuelCommand.Result.class;
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

    @Override
    public String toString() {
        return "GetFuelCommand{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
