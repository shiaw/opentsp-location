package com.navinfo.opentsp.platform.dp.core.redis;

/**
 * User: zhanhk
 * Date: 16/11/11
 * Time: 下午4:25
 */
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class TerminalCacheConfiguration {

    @Bean(name = "terminalCacheTemplate")
    public StringRedisTemplate terminalCacheTemplate(
            @Value("${terminal.redis.hostName:''}") String hostName,
            @Value("${terminal.redis.port:0}") int port,
            @Value("${terminal.redis.password}") String password,
            @Value("${terminal.redis.maxIdle}") int maxIdle,
            @Value("${terminal.redis.maxTotal}") int maxTotal,
            @Value("${terminal.redis.index}") int index,
            @Value("${terminal.redis.maxWaitMillis}") long maxWaitMillis,
            @Value("${terminal.redis.testOnBorrow}") boolean testOnBorrow,
            @Value("${terminal.redis.sentinel.master:}") String master,
            @Value("${terminal.redis.sentinel.nodes:}") String nodes) {
        StringRedisTemplate temple = new StringRedisTemplate();
        temple.setConnectionFactory(connectionFactory(hostName, port, password,
                maxIdle, maxTotal, index, maxWaitMillis, testOnBorrow,master,nodes));
        return temple;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(
            RedisConnectionFactory factory) {
        StringRedisTemplate template = new StringRedisTemplate(factory);
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        template.setValueSerializer(jackson2JsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    public RedisConnectionFactory connectionFactory(String hostName, int port,
                                                    String password, int maxIdle, int maxTotal, int index,
                                                    long maxWaitMillis, boolean testOnBorrow,String master ,String nodes) {
        JedisConnectionFactory factory;
        if(master != null && !("").equals(master)) {
            factory = new JedisConnectionFactory(poolSentinelCofig(maxIdle, maxTotal, maxWaitMillis,
                    testOnBorrow,master,nodes),poolCofig(maxIdle, maxTotal, maxWaitMillis,
                    testOnBorrow));
        } else {
            factory = new JedisConnectionFactory();
            factory.setHostName(hostName);
            factory.setPort(port);
            factory.setPoolConfig(poolCofig(maxIdle, maxTotal, maxWaitMillis,
                    testOnBorrow));
            // 初始化连接pool
        }
        if (!StringUtils.isEmpty(password)) {
            factory.setPassword(password);
        }
        if (index != 0) {
            factory.setDatabase(index);
        }
        factory.afterPropertiesSet();
        return factory;
    }

    public JedisPoolConfig poolCofig(int maxIdle, int maxTotal,
                                     long maxWaitMillis, boolean testOnBorrow) {
        JedisPoolConfig poolCofig = new JedisPoolConfig();
        poolCofig.setMaxIdle(maxIdle);
        poolCofig.setMaxTotal(maxTotal);
        poolCofig.setMaxWaitMillis(maxWaitMillis);
        poolCofig.setTestOnBorrow(testOnBorrow);
        return poolCofig;
    }

    public RedisSentinelConfiguration poolSentinelCofig(int maxIdle, int maxTotal,
                                     long maxWaitMillis, boolean testOnBorrow,String master,String nodes) {
        RedisSentinelConfiguration poolCofig = new RedisSentinelConfiguration();
        String[] arr = nodes.split(":");
        poolCofig.setMaster(master);
        poolCofig.addSentinel(new RedisNode(arr[0],Integer.valueOf(arr[1])));
        return poolCofig;
    }
}
