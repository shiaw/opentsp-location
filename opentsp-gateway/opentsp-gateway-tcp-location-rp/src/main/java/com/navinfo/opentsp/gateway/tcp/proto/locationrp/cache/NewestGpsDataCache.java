package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 终端最新位置数据缓存
 * 
 * @author lgw
 * 
 */
public final class NewestGpsDataCache {
	private static Map<Long, LocationData> cache = new ConcurrentHashMap<Long, LocationData>();

	public static void addNewsetGps(long terminalId, LocationData data) {
		cache.put(terminalId, data);
	}

	public static LocationData getNewsetGps(long terminalId) {
		return cache.get(terminalId);
	}

	public static Map<Long, LocationData> get() {
		Map<Long, LocationData> temp = cache;
		return temp;

	}

	public static int size() {
		return cache.size();
	}
}
