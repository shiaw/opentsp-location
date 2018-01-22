package com.navinfo.opentsp.platform.da.core.common.schedule;

import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.Constant.ConfigKey;
import com.navinfo.opentsp.platform.da.core.common.schedule.task.*;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 调试任务管理器
 *
 * @author aerozh
 */
public class ScheduleController {

    private final static long MILLIS = 1000;
    private final static long SECOND = 1 * MILLIS;
    private final static long MINUTE = 60 * SECOND;
    private final static long HOUR = 60 * MINUTE;
    private final static long DAY = 24 * HOUR;
    private final static Logger log = LoggerFactory.getLogger(ScheduleController.class);
    private final static ScheduleController instance = new ScheduleController();
    private final static int GPS_LOCAL_TO_REDIS_COMMIT_TIME = Configuration.getInt(ConfigKey.GPS_LOCAL_TO_REDIS_COMMIT_TIME);
    private static ScheduledExecutorService scheduExec = Executors
            .newScheduledThreadPool(4);
    private SaveGpsToRedisTask saveGpsToRedisTask;
    private ClearStatisticStatusRedisTask clearStatisticStatusRedisTask;
    private TerminalBJTask bjTask;

    private ScheduleController() {
    }

    public final static ScheduleController instance() {
        return instance;
    }

    public void start() {

        //数据同步
        log.info("当前节点为总部DA节点,加载数据同步任务...");
        SynchronousAdmin.manage();

        log.info("加载定时保存位置数据到redis任务");
        saveGpsToRedisTask = new SaveGpsToRedisTask();
        scheduExec.scheduleWithFixedDelay(saveGpsToRedisTask, 1 * MINUTE, GPS_LOCAL_TO_REDIS_COMMIT_TIME,
                TimeUnit.MILLISECONDS);
        bjTask = new TerminalBJTask();
        scheduExec.scheduleWithFixedDelay(bjTask, 1 * MINUTE, 5*MINUTE,
                TimeUnit.MILLISECONDS);
    }

    /**
     * RMI DSA调用运行存储线程，异步返回
     */
    public void saveGpsDataTask() {
        log.error("加载定时redis转存mongo任务");
        SaveGpsDataTask saveGpsDataTask = new SaveGpsDataTask();
        scheduExec.execute(saveGpsDataTask);

        clearStatisticStatusRedisTask = new ClearStatisticStatusRedisTask();
        scheduExec.execute(clearStatisticStatusRedisTask);
        log.error("加载定时redis转存mongo任务结束");
    }

    public long getNextZeroTime() {
        Calendar calendar = DateUtils.current();
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 1);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis() / 1000 - System.currentTimeMillis() / 1000;
    }
}
