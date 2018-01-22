package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

@Component("rule_170080_Status")
public class Rule_170080_Status
{

    @Resource
    private IRedisService redisService;// redis 通用dao

    private Rule_170080_Status()
    {

    }

    /**
     * 添加一个链路对象
     *
     * @param sessionId
     * @param messageBroad
     * @return
     */
    public void addmessageBroadCache(String sessionId, Boolean messageBroad)
    {

        redisService.hset(RuleEum.R170080_B.getMapKey(), sessionId, messageBroad.toString());
    }

    /**
     * 根据链路ID移除一个链路对象
     *
     * @param id
     * @return
     */
    public void removemessageBroadCache(String id)
    {
        redisService.delete(RuleEum.R170080_B.getMapKey(), id);
    }

    public Map<String, Boolean> getMessageBroadCache()
    {
        return redisService.getAllKeys(RuleEum.R170080_B.getMapKey(), Boolean.class);
    }

    public Boolean IscontainsKey(String terminalId)
    {
        String result = redisService.getHashValue(RuleEum.R170080_B.getMapKey(), terminalId, String.class);
        if (result != null)
        {
            return true;
        }
        return false;
    }

    /**
     * 根据链路ID获取一个链路对象
     *
     * @param key
     * @return
     */
    public Boolean getmessageBroadCacheById(String key)
    {
        String result = redisService.getHashValue(RuleEum.R170080_B.getMapKey(), key, String.class);
        if (result != null)
        {
            return Boolean.valueOf(result);
        }
        return false;
    }
    
    public void addArrivingDateCache(String sessionId, Long arrivingDate)
    {
        redisService.hset(RuleEum.R170080_L.getMapKey(), sessionId, arrivingDate);
    }
    
    public void removeArrivingDateCache(String id)
    {
        redisService.delete(RuleEum.R170080_L.getMapKey(), id);
    }
    
    public Map<String, Long> getArrivingDateCache()
    {
        return redisService.getAllKeys(RuleEum.R170080_L.getMapKey(), Long.class);
    }
    
    public Long getArrivingDateCacheById(String key)
    {
        String result = redisService.getHashValue(RuleEum.R170080_L.getMapKey(), key, String.class);
        if (result != null)
        {
            return Long.valueOf(result);
        }
        return null;
    }
}
