package com.navinfo.opentsp.platform.location;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalInfo;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * User: zhanhk
 * Date: 16/8/8
 * Time: 上午11:47
 */
@Configuration
@EnableAutoConfiguration
public class RedisConf {

    @Bean(name="lcRedisTemplate")
    RedisTemplate<String, TerminalInfo> redisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate<String, TerminalInfo> template = new RedisTemplate<String, TerminalInfo>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new JacksonJsonRedisSerializer<>(TerminalInfo.class));
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TerminalInfo.class));
        template.afterPropertiesSet();
        return template;
    }

}
