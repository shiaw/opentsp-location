package com.navinfo.opentsp.platform.dp.core.common.schedule.task;

import com.navinfo.opentsp.platform.dp.core.common.schedule.ITask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LatestGpsDataCacheTask extends ITask {

	private static final Logger log = LoggerFactory.getLogger(LatestGpsDataCacheTask.class);
	@Override
	public void run() {
		// TODO Auto-generated method stub
//		log.error("清理前：当前最新位置数据缓存数目： "+ LatestGpsDataCache.getInstance().size());
//		Map<Long, GpsLocationDataEntity> map = LatestGpsDataCache.getInstance().getDataEntityMap();
//		Set set = map.entrySet();
//		GpsLocationDataEntity gps = null;
//        for (Iterator<Map.Entry<Long, GpsLocationDataEntity>> it = set.iterator(); it.hasNext();) {
//            Map.Entry<Long, GpsLocationDataEntity> entry = (Map.Entry<Long, GpsLocationDataEntity>) it.next();
////            System.out.println(entry.getKey() + "--->" + entry.getValue());
//            gps = entry.getValue();
//            if((System.currentTimeMillis()/1000-gps.getCreateTime())>180){
//            	//缓存时间超过当前时间180秒，清理次缓存
//            	LatestGpsDataCache.getInstance().delete(entry.getKey());
//            }
//        }
//        log.error("清理后：当前最新位置数据缓存数目： "+ LatestGpsDataCache.getInstance().size());
	}

}
