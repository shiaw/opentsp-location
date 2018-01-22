package com.navinfo.opentsp.platform.dp.core.cache;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

@Deprecated
public class KeyValusCache implements ICache {
	private static Map<Object, List<Object>> cache = new ConcurrentHashMap<Object, List<Object>>();
	private KeyValusCache (){}
	private final static KeyValusCache instance = new KeyValusCache();
	public final static KeyValusCache getInstance(){
		return instance;
	}

	@Deprecated
	@Override
	public void writeOne(String key, Object object, int timeout) {

	}

	@Override
	public void writeArrays(String key, Object value, int timeout) {
		List<Object> list = cache.get(key);
		if (list != null) {
			list = new ArrayList<Object>();
		}
		list.add(value);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> readArrays(String key, boolean isDelete) {
		if (isDelete) {
			List<Object> list = cache.get(key);
			cache.remove(key);
			return (List<T>) list;
		} else {
			List<Object> list = cache.get(key);
			return (List<T>) list;
		}
	}

	@Deprecated
	@Override
	public <T> T readOne(String key, boolean isDelete) {
		return null;
	}

	@Override
	public void remove(String key) {
		cache.remove(key);
	}

	@Override
	public int size() {
		return cache.size();
	}

}
