package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAACCStatusAlarm;

import java.util.List;


/***************************
 * 多终端ACC状态报警详情分页缓存
 * 
 * @author claus
 *
 */
public class ACCStatusAlarmCache extends AlarmDefaultCache<DAACCStatusAlarm>{

	public ACCStatusAlarmCache(List<Long> terminalId, String queryKey) {
		super(terminalId,queryKey);
	}

}
