package com.navinfo.opentsp.platform.push.online;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.navinfo.opentsp.platform.push.DownCommand;
import com.navinfo.opentsp.platform.push.DownCommandState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 */
@Component
public class RedisCommandStatusStorage {

    private static final String PREFIX = RedisCommandStatusStorage.class.getName();
    private final RedisTemplate<String, DownCommand> template;

    @Autowired
    public RedisCommandStatusStorage(ObjectMapper objectMapper, RedisConnectionFactory factory) {
        this.template = new RedisTemplate<>();
        this.template.setConnectionFactory(factory);
        this.template.setKeySerializer(new StringRedisSerializer());
        this.template.setValueSerializer(new Jackson2JsonRedisSerializer<>(DownCommand.class));
        this.template.afterPropertiesSet();
    }

    private String asKey(String id) {
        return PREFIX + ":" + id;
    }

    public void add(DownCommand downCommand) {
        String commandId = downCommand.getCommandId();
        BoundValueOperations<String, DownCommand> ops = template.boundValueOps(asKey(commandId));
        ops.expire(1, TimeUnit.DAYS);
        ops.set(downCommand);
    }

    public DownCommand get(String id) {
        BoundValueOperations<String, DownCommand> ops = template.boundValueOps(asKey(id));
        DownCommand downCommand = ops.get();
        return downCommand;
    }

    public void delete(String id) {
        template.delete(asKey(id));
    }

    public void modiflyState(String id, DownCommandState state,String businessCode,Object data) {
        DownCommand downCommand = this.get(id);
        downCommand.setState(state.getValue());
        downCommand.setBusinessCode(businessCode);
        downCommand.setData(data);
        this.add(downCommand);
    }
}