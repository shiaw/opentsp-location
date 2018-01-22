package com.navinfo.tasktracker.rprest.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by zhangdong on 2017/5/19.
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Autowired
    private RedisProperties redisProperties;
    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMinIdle(redisProperties.getPool().getMinIdle());
        jedisPoolConfig.setMaxIdle(redisProperties.getPool().getMaxIdle());
        jedisPoolConfig.setMaxTotal(redisProperties.getPool().getMaxActive());
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getPool().getMaxWait());

        RedisProperties.Sentinel sentinel = redisProperties.getSentinel();
        if (sentinel != null) {
            RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration().master(sentinel.getMaster());
            String[] nodes = sentinel.getNodes().split(",");
            for (int i = 0;i < nodes.length ;i++){
                String[] node = nodes[i].split(":");
                sentinelConfig.sentinel(node[0], Integer.valueOf(node[1]));
            }
            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(sentinelConfig,jedisPoolConfig);
            jedisConnectionFactory.setPassword(redisProperties.getPassword());
            jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
            return jedisConnectionFactory;
        } else {

            JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory();
            jedisConnectionFactory.setHostName(redisProperties.getHost());
            jedisConnectionFactory.setPort(redisProperties.getPort());
            jedisConnectionFactory.setPassword(redisProperties.getPassword());
            jedisConnectionFactory.setDatabase(redisProperties.getDatabase());
            jedisConnectionFactory.setPoolConfig(jedisPoolConfig);
            return jedisConnectionFactory;
        }

    }

    @Bean
    public RedisTemplate redisTemplate(RedisConnectionFactory jedisConnectionFactory) {
        RedisTemplate template = new RedisTemplate();
        template.setConnectionFactory(jedisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        //template.setValueSerializer(new StringRedisSerializer());
        return template;
    }


}
