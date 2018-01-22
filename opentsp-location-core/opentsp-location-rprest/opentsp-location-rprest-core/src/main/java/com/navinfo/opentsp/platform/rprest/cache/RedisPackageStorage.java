package com.navinfo.opentsp.platform.rprest.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 */
@Component
public class RedisPackageStorage {

    private final RedisTemplate<String, Object> redisTemplate;

    private final  String KEY="totalMileageAndPackage";
    @Autowired
    public RedisPackageStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.redisTemplate = new RedisTemplate<>();
        this.redisTemplate.setConnectionFactory(factory);
        this.redisTemplate.afterPropertiesSet();
    }


 public Object get(String key){
     HashOperations hashOperations = redisTemplate.opsForHash();

     return  hashOperations.get(KEY,key);

 }

    public Object getAll(){
        HashOperations hashOperations = redisTemplate.opsForHash();
        return  hashOperations.entries(KEY);

    }
 public void put(String key,Object value){
     HashOperations hashOperations = redisTemplate.opsForHash();
     hashOperations.put(KEY,key,value);
 }

}
