package com.navinfo.opentsp.platform.da.core.cache.alarm;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAAreaINOUTAlarm;

import java.util.List;



/***************************
 * 多终端报进出区域警详情分页缓存
 * 
 * @author claus
 *
 */
public class AreaINOUTAlarmCache extends AlarmDefaultCache<DAAreaINOUTAlarm>{

	public AreaINOUTAlarmCache(List<Long> terminalId, String queryKey) {
		super(terminalId,queryKey);
	}

}
