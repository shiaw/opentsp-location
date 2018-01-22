package com.navinfo.opentsp.gateway.tcp.proto.location.redis;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by yinsihua on 2016/9/13.
 */
@Service("redisService")
public class RedisServiceImpl implements IRedisService {

    private static Logger log = Logger.getLogger(RedisServiceImpl.class);

//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    private ValueOperations<String, String> opsForValue() {
        return redisTemplate.opsForValue();
    }

    private HashOperations<String, Object, Object> opsForHash() {
        return redisTemplate.opsForHash();
    }

    protected String keys(String key) {
        return key;
    }

    @Override
    public void set(String key, String value) {
        opsForValue().set(keys(key), value);
    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(keys(key), value, timeout, unit);
    }

    @Override
    public void expire(String key, long expire, TimeUnit unit) {
        redisTemplate.expire(keys(key), expire, unit);
    }

    @Override
    public boolean exists(String key) {
        boolean existFlag = false;
        try {
            existFlag = redisTemplate.hasKey(keys(key));
            if (existFlag) {
                // 过期时间设置为30分钟
                // expire(key, CommonConst.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("exists method error: haskey(key) error key=" + keys(key) + e);
        }
        return existFlag;
    }

    @Override
    public boolean exists(String key, Object hashKey) {
        boolean existFlag = false;
        try {
            existFlag = redisTemplate.opsForHash().hasKey(keys(key), hashKey);
            if (existFlag) {
                // 过期时间设置为30分钟
                // expire(key, CommonConst.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            log.error("exists method error: haskey(key) error key=" + keys(key) + e);
        }
        return existFlag;
    }

    @Override
    public void delete(String key, Object hashkey) {
        redisTemplate.opsForHash().delete(keys(key), hashkey);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(keys(key));
    }

    @Override
    public void hset(String key, Object hashKey, Object value) {
        try {
            this.opsForHash().put(keys(key), hashKey, JacksonUtil.toJSon(value));
        }catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public void batchSet(String key, Map map) {
        this.opsForHash().putAll(key,map);
    }


    @Override
    public <T> T get(String key, Class<T> valueType) {
        String result = (String) redisTemplate.opsForValue().get(key);
        return JacksonUtil.readValue(result, valueType);
    }


    @Override
    public String hget(String key, Object hashKey) {
        Object ret = null;
        try {
            ret = this.opsForHash().get(keys(key), hashKey);
            // 过期时间设置为30分钟
            // expire(key, CommonConst.REDIS_EXPIRE_TIME, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("hget key=" + key + "  hashKey=" + hashKey + "error" + e);
        }
        return String.valueOf(ret);
    }

    /**
     * 根据hash key,Class 对象,获取指定key 对象
     *
     * @param key
     * @param hashKey
     * @param valueType
     * @param <T>
     * @return
     */
    @Override
    public <T> T getHashValue(String key, Object hashKey, Class<T> valueType) {
        String result = this.hget(key, hashKey);
        if (result != null) {
            return JacksonUtil.readValue(result, valueType);
        }
        return null;
    }

    /**
     * 根据hash key,Class 对象,获取全部redis hash Map
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    @Override
    public <T> Map<String, T> getAllKeys(String key, Class<T> valueType) {
        Map<String, T> map = new HashMap<>();
        Map<Object, Object> result = redisTemplate.opsForHash().entries(keys(key));
        for (Map.Entry<Object, Object> temp : result.entrySet()) {
            map.put(String.valueOf(temp.getKey()), JacksonUtil.readValue(String.valueOf(temp.getValue()), valueType));
        }
        return map;
    }

    /**
     * 根据hash key,Class 对象,获取全部redis hash Map
     *
     * @param key
     * @param valueType
     * @param <T>
     * @return
     */
    @Override
    public <T> Map<Long, T> getAllKeysByLong(String key, Class<T> valueType) {
        Map<Long, T> map = new HashMap<>();
        Map<Object, Object> result = redisTemplate.opsForHash().entries(keys(key));
        for (Map.Entry<Object, Object> temp : result.entrySet()) {
            map.put(Long.valueOf(String.valueOf(temp.getKey())),
                    JacksonUtil.readValue(String.valueOf(temp.getValue()), valueType));
        }
        return map;
    }
}
