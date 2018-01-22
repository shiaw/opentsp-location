package com.navinfo.opentsp.platform.da.core.persistence.redis.dao;

public interface IRedisStringDao {
	abstract void set(String key, Object value);

	abstract void set(String key, Object value, int seconds);

	abstract Object get(String key);

	abstract String getString(String key);

}
