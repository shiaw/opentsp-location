package com.navinfo.opentsp.platform.push.online;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 */
@Component
public class RedisTerminalInfoStorage {

    private static final String PREFIX = RedisTerminalInfoStorage.class.getName();
    private final RedisTemplate<String, TerminalInfo> template;

    @Autowired
    public RedisTerminalInfoStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.template = new RedisTemplate<>();
        this.template.setConnectionFactory(factory);
        this.template.setKeySerializer(new StringRedisSerializer());
        this.template.setValueSerializer(new Jackson2JsonRedisSerializer<>(TerminalInfo.class));
        this.template.afterPropertiesSet();
    }

    private String asKey(String termid) {
        return PREFIX + ":" + termid;
    }

    public void register(TerminalInfo terminalInfo) {
        String device = terminalInfo.getTerminalId();
        BoundValueOperations<String, TerminalInfo> ops = template.boundValueOps(asKey(device));
        ops.set(terminalInfo);
      //  updateExpire(ops, terminalInfo);
    }

    public TerminalInfo get(String terminalId) {
        BoundValueOperations<String, TerminalInfo> ops = template.boundValueOps(asKey(terminalId));
        TerminalInfo terminalInfo = ops.get();
        return terminalInfo;
    }

    public List<TerminalInfo> getAll() {
        Set<String> keys = template.opsForList().getOperations().keys(PREFIX+"*");

        return template.opsForValue().multiGet(keys);
    }

    public void  clear(){
        template.opsForList().getOperations().keys(PREFIX+"*").clear();
    }

    public void unregister(String terminalId) {
        template.delete(asKey(terminalId));
    }

}
