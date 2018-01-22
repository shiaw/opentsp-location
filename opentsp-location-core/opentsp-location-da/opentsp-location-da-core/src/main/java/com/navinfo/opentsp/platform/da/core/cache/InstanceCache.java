package com.navinfo.opentsp.platform.da.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例缓存<br>
 * 目前只提供给服务重连使用 <br>
 * Key:节点编号;Value:实例
 * 
 * @author lgw
 * 
 */
public class InstanceCache {
	private static Map<Integer, Object> cache = new ConcurrentHashMap<Integer, Object>();

	public static void addInstance(int nodeCode, Object instance) {
		cache.put(nodeCode, instance);
	}

	public static Object getInstance(int nodeCode) {
		return cache.get(nodeCode);
	}
	public static void removeInstance(int nodeCode){
		cache.remove(nodeCode);
	}
	public static int size() {
		return cache.size();
	}
}
