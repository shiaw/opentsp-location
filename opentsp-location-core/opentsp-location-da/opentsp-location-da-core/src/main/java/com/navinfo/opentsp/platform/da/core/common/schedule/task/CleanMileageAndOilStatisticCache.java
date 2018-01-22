package com.navinfo.opentsp.platform.da.core.common.schedule.task;

import java.util.TimerTask;

import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 *每日0点清除统计当日里程油耗缓存,redis
 */
public class CleanMileageAndOilStatisticCache extends  TimerTask  {
	private static final Logger log = LoggerFactory.getLogger(CleanMileageAndOilStatisticCache.class);
	@Override
	public void run() {
		log.error("清除昨天统计里程油耗缓存");
		TempGpsData.delMileageAndOilDataStatisticCache();
	}

	
}
