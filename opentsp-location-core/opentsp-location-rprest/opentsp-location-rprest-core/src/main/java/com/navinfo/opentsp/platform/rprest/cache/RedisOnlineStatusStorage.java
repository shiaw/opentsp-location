package com.navinfo.opentsp.platform.rprest.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 */
@Component
public class RedisOnlineStatusStorage{

    private static final String PREFIX ="com.navinfo.opentsp.platform.push.online.RedisOnlineStatusStorage";
    private final RedisTemplate<String, DeviceRegistration> template;

    @Autowired
    public RedisOnlineStatusStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.template = new RedisTemplate<>();
        this.template.setConnectionFactory(factory);
        this.template.setKeySerializer(new StringRedisSerializer());
        this.template.setValueSerializer(new Jackson2JsonRedisSerializer<>(DeviceRegistration.class));
        this.template.afterPropertiesSet();
    }

    private String asKey(String device) {
        return PREFIX + ":" + device;
    }


    private void updateExpire(BoundValueOperations<String, DeviceRegistration> ops, DeviceRegistration dr) {
        // note that in cluster time in current node and redis storage node may differ, therefore you don't set small ttl
        ops.expire(dr.getTtl(), TimeUnit.MILLISECONDS);
    }

    public List<DeviceRegistration> getAll() {
        Set<String> keys = template.opsForList().getOperations().keys(PREFIX+"*");
        return template.opsForValue().multiGet(keys);
    }

}
