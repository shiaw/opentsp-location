package com.navinfo.opentsp.platform.da.core.cache.alarm.factory;

import com.navinfo.opentsp.platform.da.core.cache.alarm.AlarmDefaultCache;

import java.util.List;


public interface AlarmCacheProvider {
      public AlarmDefaultCache produce(List<Long> terminalId, String queryKey);
}
