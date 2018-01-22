package com.navinfo.opentsp.gateway.tcp.proto.locationrp.common;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceMarkFactory {

	private static Map<String, Long> serviceMarkCache = new ConcurrentHashMap<>();


	/* 计数器，从1开始，当达到最大值后，重新置1 */
	public static int counter = 1;

	/**
	 * 获取服务标识(当前时间毫秒数低位截取12位与计数器求和)
	 * 
	 * @return String
	 */
	public static long getServiceMark(String key) {
		long id = System.currentTimeMillis() / 10 + counter;
		if (counter == Integer.MAX_VALUE) {
			counter = 1;
		} else {
			counter++;
		}
//		if(serviceMarkCache.containsKey(key)) {
//			return serviceMarkCache.get(key);
//		}
		serviceMarkCache.put(key,id);
		return id;
	}

	/**
	 * 判断login是否有效
	 * @param loginKey
	 * @return
     */
	public static boolean isLoginKey(String loginKey) {
		return serviceMarkCache.containsValue(Long.valueOf(loginKey));
	}
}
