package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAAreaOverspeedAlarm;

import java.util.List;




/***************************
 * 多终端报警详情分页缓存
 * 
 * @author jin_s
 *
 */
public class AreaOverspeedAlarmCache extends AlarmDefaultCache<DAAreaOverspeedAlarm>{

	public AreaOverspeedAlarmCache(List<Long> terminalId, String queryKey) {
		super(terminalId, queryKey);
	}
   

}
