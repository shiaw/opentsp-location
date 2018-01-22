package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm;
import com.navinfo.opentsp.platform.da.core.common.schedule.ITask;
import com.navinfo.opentsp.platform.da.core.common.schedule.TaskStatus;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.TerminalStatisticStatusService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalStatisticStatusServiceImpl;

/**
 * 清除发生滞留超时
 */
public class ClearStatisticStatusRedisTask extends TimerTask implements ITask {

    private Logger log = LoggerFactory.getLogger(ClearStatisticStatusRedisTask.class);
    private TerminalStatisticStatusService service = new TerminalStatisticStatusServiceImpl();

    @Override
    public void setExecuteCycle(long cycle) {
    }

    @Override
    public long getLastExecuteTime() {
        return 0;
    }

    @Override
    public TaskStatus getStatus() {
        return null;
    }

    @Override
    public void run() {
        try {
            boolean b = service.removeLastDayStatus(LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE);
            log.info("删除redis中的滞留超时终端缓存，结果：" + b);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}
