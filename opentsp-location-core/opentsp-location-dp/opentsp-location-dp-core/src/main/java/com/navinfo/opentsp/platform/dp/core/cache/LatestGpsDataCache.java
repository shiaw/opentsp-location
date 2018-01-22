package com.navinfo.opentsp.platform.dp.core.cache;


import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 最新位置数据缓存<br>
 * key=终端ID,value=Gps数据
 * 
 * @author lgw
 * 
 */
public class LatestGpsDataCache {
	private static Map<Long, GpsLocationDataEntity> cache = new ConcurrentHashMap<Long, GpsLocationDataEntity>();
	public static int currentTimes = 0;
	public static long beginDate = 0;
	public static long endDate = 0;
	
	private LatestGpsDataCache() {
	}

	private final static LatestGpsDataCache instance = new LatestGpsDataCache();

	public static final LatestGpsDataCache getInstance() {
		return instance;
	}

	public void add(GpsLocationDataEntity dataEntity) {
		cache.put(dataEntity.getTerminalId(), dataEntity);
	}
	
	public Map<Long, GpsLocationDataEntity> getDataEntityMap(){
		return cache;
	}
	
	public GpsLocationDataEntity delete(long terminalId) {
		return cache.remove(terminalId);
	}

	public int size() {
		return cache.size();
	}
}
