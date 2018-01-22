package com.navinfo.opentsp.platform.dp.core.redis;

import java.util.Map;
import java.util.Set;

/**
 * redis 接口
 * User: zhanhk
 * Date: 16/8/9
 * Time: 下午3:55
 */
public interface RedisDao {

    void setValue(String key, String value);

    void setExpireValue(byte[] key, byte[] value, int second);

    void setHashValue(String mapName, String key, String value);

    void setHashValue(String mapName, String key, Object obj);

    void setHashValue(String mapName, long key, String value);

    void setHashValue(String mapName, String key, long value);

    void setHashValue(byte[] mapName, byte[] key, byte[] value);

    void zadd(String key, Map<String, Double> scoreMembers);

    Set<String> zrangeByscore(String key, String value);

    byte[] getValue(byte[] key);

    String getValue(String key);

    String getHashValue(String mapName, String key);

    String getHashValue(String mapName, long key);

    <T> T getHashValue(String mapName, long key, Class<T> valueType);

    <T> T getHashValue(String mapName, String key, Class<T> valueType);

    byte[] getHashValue(byte[] mapName, byte[] key);

    Map<String, String> getAllKeys(String mapName);

    <T> Map<String, T> getAllKeys(String mapName, Class<T> valueType);

    long incr(String key);

    long setHashHincr(String key, String field, long value);

    long del(String key);

    long zrem(String key, String... members);

    Long delHashValue(String mapName, String key);

    Long delHashValue(String mapName, long key);
}
