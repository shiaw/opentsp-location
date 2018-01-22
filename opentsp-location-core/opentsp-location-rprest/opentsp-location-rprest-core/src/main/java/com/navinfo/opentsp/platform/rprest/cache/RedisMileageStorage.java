package com.navinfo.opentsp.platform.rprest.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 修伟 on 2017/10/9 0009.
 */
@Component
public class RedisMileageStorage {
    private final RedisTemplate<String, Object> redisTemplate;

    private final  String KEY="DAY_MILEAGE_OIL_VALUE";
    @Autowired
    public RedisMileageStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(factory);
        this.redisTemplate.afterPropertiesSet();
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
}
