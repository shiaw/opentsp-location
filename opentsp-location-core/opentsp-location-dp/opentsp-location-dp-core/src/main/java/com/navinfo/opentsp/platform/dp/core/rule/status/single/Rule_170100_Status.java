package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Rule_170100_Status implements Serializable {
    private static final long serialVersionUID = 1L;

    @Resource
    private IRedisService redisService;

    private Map<String, Boolean> cache = new ConcurrentHashMap<>();
    private static Rule_170100_Status instance = new Rule_170100_Status();

    public final static Rule_170100_Status getInstance() {
        return instance;
    }

    public void addOutRegionToLSpeed(String id, Boolean b) {
//		RedisUtil.getRedis().setHashValue(RuleEum.R170100.getMapKey() ,id,b.toString());
        cache.put(id, b);
        redisService.hset(RuleEum.R170100.getMapKey(), id, b.toString());
    }

    public void delOutRegionToLSpeed(String id) {
//		RedisUtil.getRedis().delHashValue(RuleEum.R170100.getMapKey() ,id);
        cache.remove(id);
        redisService.delete(RuleEum.R170100.getMapKey(), id);
    }

    public Map<String, Boolean> getOutRegionToLSpeed() {
        Map<String, Boolean> temp = redisService.getAllKeys(RuleEum.R170100.getMapKey(), Boolean.class);
//		return RedisUtil.getRedis().getAllKeys(RuleEum.R170100.getMapKey(),RuleEum.R170100.getClassType());
        return temp;
    }

    public Boolean getOutRegionToLSpeed(String id) {
//		return Boolean.valueOf(RedisUtil.getRedis().getHashValue(RuleEum.R170100.getMapKey() ,id));
        Boolean flag = cache.get(id);
        if (flag != null) {
            return flag;
        } else {
            flag = redisService.getHashValue(RuleEum.R170100.getMapKey(), id, Boolean.class);
            if (flag != null) {
                cache.put(id, flag);
            }
            return flag;
        }
    }
}
