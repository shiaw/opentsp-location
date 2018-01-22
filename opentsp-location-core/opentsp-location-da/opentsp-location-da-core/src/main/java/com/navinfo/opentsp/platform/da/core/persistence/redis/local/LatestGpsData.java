package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class LatestGpsData {
	private static Map<Long, LCLocationData.LocationData> cache = new ConcurrentHashMap<Long, LCLocationData.LocationData>();

	public static void put(long terminalId, LCLocationData.LocationData data) {
		cache.put(terminalId, data);
	}

	public static LCLocationData.LocationData get(long terminalId) {
		return cache.get(terminalId);
	}
	
}
