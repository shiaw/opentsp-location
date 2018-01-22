package com.navinfo.opentsp.platform.dp.core.common.schedule.task;


import com.navinfo.opentsp.platform.dp.core.cache.LatestGpsDataCache;
import com.navinfo.opentsp.platform.dp.core.common.schedule.ITask;

import java.util.Calendar;

public class SumGpsDataNumberTask extends ITask {

	@Override
	public void run() {
		log.error("SumGpsDataNumberTask 零点重置当日位置数据存储次数");
		Calendar c1 = Calendar.getInstance();
	    c1.set(Calendar.HOUR_OF_DAY, 0);
	    c1.set(Calendar.MINUTE, 0);
	    c1.set(Calendar.SECOND, 0);
	    log.error("开始时间："+c1.getTime());
	    Calendar c2 = Calendar.getInstance();
	    c2.set(Calendar.HOUR_OF_DAY, 23);
	    c2.set(Calendar.MINUTE, 59);
	    c2.set(Calendar.SECOND, 59);
	    log.error("结束时间"+c2.getTime());
	    LatestGpsDataCache.beginDate= c1.getTime().getTime()/1000;
		LatestGpsDataCache.endDate=c2.getTime().getTime()/1000;
		LatestGpsDataCache.currentTimes=0;
		log.error("begin:"+LatestGpsDataCache.beginDate);
	}
}
