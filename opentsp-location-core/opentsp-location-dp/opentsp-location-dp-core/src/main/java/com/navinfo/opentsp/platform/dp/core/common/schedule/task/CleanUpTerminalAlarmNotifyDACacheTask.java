package com.navinfo.opentsp.platform.dp.core.common.schedule.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * 每天凌晨0点清除 报警终端缓存 FilterCache.alarmTerminals
 */
@Component
@Configurable
@EnableScheduling
public class CleanUpTerminalAlarmNotifyDACacheTask {

	public static Logger logger = LoggerFactory.getLogger(CleanUpTerminalAlarmNotifyDACacheTask.class);

//	@Scheduled(cron = "0 1 0 * * ?")
//	public void run() {
//		logger.error("清除报警终端缓存 FilterCache.alarmTerminals,清除前报警终端缓存数="+ FilterCache.getIntence().getAlarmTerminas());
//		FilterCache.getIntence().cleanAlarmTerminalCache();
//		logger.error("清除报警终端缓存 FilterCache.alarmTerminals,清除后报警终端缓存数="+FilterCache.getIntence().getAlarmTerminas());
//	}
}
