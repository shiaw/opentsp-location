package com.navinfo.opentsp.platform.da.core.cache.alarm.factory;

import com.navinfo.opentsp.platform.da.core.cache.alarm.AlarmDefaultCache;
import com.navinfo.opentsp.platform.da.core.cache.alarm.OverspeedAlarmCache;

import java.util.List;


public class OverspeedAlarmCacheFactory implements AlarmCacheProvider{

	@Override
	public AlarmDefaultCache produce(List<Long> terminalId, String queryKey) {
		
		return new OverspeedAlarmCache(terminalId,queryKey);
	}

}
