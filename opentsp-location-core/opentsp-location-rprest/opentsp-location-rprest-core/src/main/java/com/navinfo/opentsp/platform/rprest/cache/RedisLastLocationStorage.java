package com.navinfo.opentsp.platform.rprest.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ByteArraySerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 */
@Component
public class RedisLastLocationStorage {

    private final RedisTemplate<byte[], Object> redisTemplate;

    private final String KEY = "LASTEST_LOCATION_DATA_LATLNG";
    private final String RANGE_KEY = "LASTEST_VEHICLE_PASS_AREA_TIMES";
    @Autowired
    public RedisLastLocationStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(factory);
        this.redisTemplate.afterPropertiesSet();
    }

    public Object get(final String key) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        return hashOperations.get(KEY, key);

    }

    public Map getAll() {
        final Map[] result = {new HashMap()};
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                result[0] = connection.hGetAll(redisTemplate.getStringSerializer().serialize(KEY));
                return null;
            }
        });
        return result[0];
    }

    public Map getAllPassTimes() {
        final Map[] result = {new HashMap()};
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection)
                    throws DataAccessException {
                result[0] = connection.hGetAll(redisTemplate.getStringSerializer().serialize(RANGE_KEY));
                return null;
            }
        });
        return result[0];
    }
    public void put(String key, Object value) {
        HashOperations hashOperations = redisTemplate.opsForHash();
        hashOperations.put(KEY, key, value);
    }

}
