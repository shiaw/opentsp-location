package com.navinfo.opentsp.platform.dp.core.redis;

import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * User: zhanhk
 * Date: 16/11/11
 * Time: 下午4:37
 */
@Service
public class TerminalRedisServiceImple implements IRedisService {

    @Resource
    @Qualifier("terminalCacheTemplate")
    private StringRedisTemplate stringRedisTemplate;

    private ValueOperations<String, String> opsForValue() {
        return stringRedisTemplate.opsForValue();
    }

    private HashOperations<String, Object, Object> opsForHash() {
        return stringRedisTemplate.opsForHash();
    }

    @Override
    public void hset(String key, Object hashKey, Object value) {
        this.opsForHash().put(this.keys(key), hashKey, JacksonUtil.toJSon(value));
    }

    @Override
    public void hsetRuleEntity(String key, Object hashKey, RuleEntity ruleEntity) {

    }

    @Override
    public void rPushRuleEntity(String key, RuleEntity ruleEntity) {

    }

    @Override
    public <T> Map<String, T> getAllKeys(String key, Class<T> valueType) {
        Map<String, T> map = new HashMap<>();
        Map<Object, Object> result = stringRedisTemplate.opsForHash().entries(keys(key));
        for (Map.Entry<Object, Object> temp : result.entrySet()) {
            map.put(String.valueOf(temp.getKey()), JacksonUtil.readValue(String.valueOf(temp.getValue()), valueType));
        }
        return map;
    }

    @Override
    public <T> Map<Long, T> getAllKeysByLong(String key, Class<T> valueType) {
        return null;
    }

    @Override
    public void batchSet(String key, Map map) {

    }

    @Override
    public void batchSetRule(String key, Map<String, RuleEntity> map) {

    }

    @Override
    public void hMsetRuleEntity(Map<Long, List<RuleEntity>> ruleEntityMap) {

    }


    @Override
    public void set(String key, String value) {

    }

    @Override
    public void set(String key, String value, long timeout, TimeUnit unit) {

    }

    @Override
    public void expire(String key, long expire, TimeUnit unit) {

    }

    @Override
    public boolean exists(String key) {
        return false;
    }

    @Override
    public boolean exists(String key, Object hashKey) {
        return false;
    }

    @Override
    public void delete(String key, Object hashkey) {
        stringRedisTemplate.opsForHash().delete(keys(key), hashkey);
    }

    @Override
    public void delete(String key) {

    }

    @Override
    public <T> T get(String key, Class<T> valueType) {
        String result = stringRedisTemplate.opsForValue().get(key);
        return JacksonUtil.readValue(result, valueType);
    }

    @Override
    public <T> List<RuleEntity> hgetRuleEntityList(String key, Object hashKey) {
        return null;
    }

    @Override
    public <T> RuleEntity hgetRuleEntity(String key, Object hashKey) {
        return null;
    }

    @Override
    public String hget(String key, Object hashKey) {
        return null;
    }

    @Override
    public <T> T getHashValue(String key, Object hashKey, Class<T> valueType) {
        return null;
    }

    protected String keys(String key) {
        return key;
    }
}
