package com.navinfo.opentsp.platform.dp.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 终端点名应答缓存
 * key=终端ID_加密请求流水号,value=点名应答流水号
 * @author lgw
 *
 */
public class CallNameResCache {
	private static Map<String, Integer> cache = new ConcurrentHashMap<String, Integer>();
	public static void addCallNameRes(long terminalId , int serialNumber , int callNameSerialNumber){
		cache.put(terminalId+"_"+serialNumber, callNameSerialNumber);
	}
	public static Integer getCallNameRes(long terminalId , int serialNumber , boolean isDel){
		if(isDel)
			return cache.remove(terminalId+"_"+serialNumber);
		else
			return cache.get(terminalId+"_"+serialNumber);
	}
	public static int size(){
		return cache.size();
	}
}
