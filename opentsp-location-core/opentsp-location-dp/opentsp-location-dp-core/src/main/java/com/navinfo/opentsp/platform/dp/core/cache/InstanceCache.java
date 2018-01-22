package com.navinfo.opentsp.platform.dp.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	private static Map<Long, Object> cache = new ConcurrentHashMap<Long, Object>();
	private static final Logger log = LoggerFactory.getLogger(InstanceCache.class);
	public static void addInstance(long nodeCode, Object instance) {
		cache.put(nodeCode, instance);
	}

	public static Object getInstance(long nodeCode) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\n获取节点["+nodeCode+"]连接实例\n");
		buffer.append("当前缓存实例集合\n");
		buffer.append(cache.toString());
		log.error(buffer.toString());
		return cache.get(nodeCode);
	}
	public static void removeInstance(long nodeCode){
		cache.remove(nodeCode);
	}
	public static int size() {
		return cache.size();
	}
}
