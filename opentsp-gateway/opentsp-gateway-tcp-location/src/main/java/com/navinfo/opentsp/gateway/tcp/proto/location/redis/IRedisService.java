package com.navinfo.opentsp.gateway.tcp.proto.location.redis;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yinsihua on 2016/9/13.
 */
public interface IRedisService {

    public void set(String key, String value);

    public void set(String key, String value, long timeout, TimeUnit unit);

    public void expire(String key, long expire, TimeUnit unit);

    public boolean exists(String key);

    public boolean exists(String key, Object hashKey);

    public void delete(String key, Object hashkey);

    public void delete(String key);

    public void hset(String key, Object hashKey, Object value);

    <T> T get(String key, Class<T> valueType);

    public String hget(String key, Object hashKey);

    public <T> T getHashValue(String key, Object hashKey, Class<T> valueType);

    public <T> Map<String, T> getAllKeys(String key, Class<T> valueType);

    public <T> Map<Long, T> getAllKeysByLong(String key, Class<T> valueType);

    public void batchSet(String key, Map map);
}
