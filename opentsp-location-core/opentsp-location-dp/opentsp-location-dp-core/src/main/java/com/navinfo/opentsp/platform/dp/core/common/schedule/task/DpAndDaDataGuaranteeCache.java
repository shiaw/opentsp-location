package com.navinfo.opentsp.platform.dp.core.common.schedule.task;

import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.dp.core.cache.DataAccessCache;
import com.navinfo.opentsp.platform.dp.core.cache.entity.DataAccessCacheEntry;
import com.navinfo.opentsp.platform.dp.core.common.schedule.ITask;

import java.util.List;

/**
 * 负责与DA间数据存储的保证<BR>
 * 
 * @author lgw
 * 
 */
public class DpAndDaDataGuaranteeCache extends ITask {

	@Override
	public void run() {
		List<DataAccessCacheEntry> lists = DataAccessCache.getInstance()
				.getExpiredObject();
		if (lists != null) {
			for (DataAccessCacheEntry dataAccessCacheEntry : lists) {
				Packet packet = (Packet) dataAccessCacheEntry.getObject();
				//// TODO: 16/8/24  
				//DAFacade.writeForCache(packet);
			}
		}
	}


}
