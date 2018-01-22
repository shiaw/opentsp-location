package com.navinfo.opentsp.platform.da.core.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内部公共缓存对象
 *
 */
public class InternalCache {
	private static Map<String, InternalCacheEntity> cache = new ConcurrentHashMap<String, InternalCacheEntity>();
	private final int DEFAULT_TIME_OUT = 600;
	private final static InternalCache instance = new InternalCache();

	private InternalCache() {
	}

	public static InternalCache getInstance() {
		return instance;
	}

	/**
	 * 添加一个缓存数据
	 *
	 * @param key
	 * @param expired
	 * @param value
	 * @return
	 */
	public boolean add(String key, int expired, Object value) {
		InternalCacheEntity entry = new InternalCacheEntity();
		entry.setObject(value);
		entry.setTimeout(expired);
		entry.setLastUpdateTime(System.currentTimeMillis() / 1000);
		return cache.put(key, entry) != null;
	}

	public boolean delete(String key) {
		return cache.remove(key) != null;
	}

	public boolean update(String key, Object value) {
		InternalCacheEntity InternalCacheEntity = cache.get(key);
		if (InternalCacheEntity != null) {
			InternalCacheEntity.setLastUpdateTime(System.currentTimeMillis() / 1000);
			InternalCacheEntity.setObject(value);
			return cache.put(key, InternalCacheEntity) != null;
		} else {
			return false;
		}

	}

	public boolean add(String key, Object value) {
		return this.add(key, DEFAULT_TIME_OUT, value);
	}

	/**
	 * 获取缓存超时对象
	 * 
	 * @return
	 */
	public List<Object> getExpiredObject() {
		Iterator<String> iterable = cache.keySet().iterator();
		List<Object> result = new ArrayList<Object>();
		long time = System.currentTimeMillis() / 1000;
		while (iterable.hasNext()) {
			InternalCacheEntity InternalCacheEntity = cache.get(iterable.next());
			if (InternalCacheEntity != null) {
				if (time - InternalCacheEntity.getLastUpdateTime() > InternalCacheEntity.getTimeout()) {
					result.add(InternalCacheEntity.getObject());
				}
			}
		}
		return result;
	}

	public Object get(String key) {
		InternalCacheEntity entry = cache.get(key);
		if (entry != null)
			return entry.getObject();
		return null;
	}
}
