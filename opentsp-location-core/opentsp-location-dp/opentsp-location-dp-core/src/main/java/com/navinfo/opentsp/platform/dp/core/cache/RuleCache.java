package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 规则缓存<br>
 * key=终端ID,value=规则数据
 *
 * @author lgw
 */
@Component("ruleCache")
public class RuleCache {

    private static Map<Long, RuleEntity> cache = new ConcurrentHashMap<>();

    @Autowired
    private IRedisService redisService;// redis 通用dao

    private RuleCache() {
    }

    public void addRuleEntity(RuleEntity ruleEntity) {
//        redisService.hsetRuleEntity(Constant.RULE_CACHE_KEY, String.valueOf(ruleEntity.getTerminal()), ruleEntity);
        cache.put(ruleEntity.getTerminal(), ruleEntity);
    }

    public RuleEntity getRuleEntity(long terminal) {
        return cache.get(terminal);
    }

    public boolean removeRuleEntity(long terminal) {
        return cache.remove(terminal) != null;
    }

//    public RuleEntity getRuleEntity(long terminal) {
////        return redisService.getHashValue(Constant.RULE_CACHE_KEY, String.valueOf(terminal), RuleEntity.class);
//        return redisService.hgetRuleEntity(Constant.RULE_CACHE_KEY, String.valueOf(terminal));
//    }
//
//    public void removeRuleEntity(long terminal) {
//        redisService.delete(Constant.RULE_CACHE_KEY, String.valueOf(terminal));
//    }

    /**
     * 缓存中是否存在
     *
     * @param ruleEntity
     * @return
     */
    public boolean isExists(RuleEntity ruleEntity) {
        long regularCode = ruleEntity.getRegularCode().getNumber();
        if (redisService.exists(Constant.RULE_COMMON_CACHE_KEY + "_" + String.valueOf(regularCode))) {
            return true;
        }
        return false;
    }

    /**
     * 批量set
     *
     * @param key
     * @param mapCache
     */
    public void batchSet(String key, Map mapCache) {
        redisService.batchSetRule(key, mapCache);
    }
}
