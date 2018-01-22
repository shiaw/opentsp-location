package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 滞留超时延时缓存 Map<终端id,延时终止时间戳> 延时终止时间戳=延时时间+当前时间
 * 
 * @author Administrator
 */
@Component("delayTimeCache")
public class DelayTimeCache
{
    // static Map<String, Long> cache = new ConcurrentHashMap<String, Long>();
    @Resource
    private IRedisService redisService;// redis 通用dao
    
    private DelayTimeCache()
    {
    }
    

    public void addDelayTimeCache(String terminalId, long delayTime)
    {
        redisService.hset(RuleEum.DelayTimeCache.getMapKey(), terminalId, delayTime);
    }
    
    public void delDelayTimeCache(String terminalId)
    {
        redisService.delete(RuleEum.DelayTimeCache.getMapKey(), terminalId);
    }
    
    public Map<String, Long> getDelayTimeCache()
    {
        return redisService.getAllKeys(RuleEum.DelayTimeCache.getMapKey(), RuleEum.DelayTimeCache.getClassType());
    }
    
    public Long getDelayTime(String terminalId)
    {
        String result = redisService.getHashValue(RuleEum.DelayTimeCache.getMapKey(), terminalId, String.class);
        if (result != null)
        {
            return Long.valueOf(result);
        }
        return (long)0;
    }
    
    public Boolean isDelayTime(String terminalId)
    {
        //青汽平台暂时不用此指令
        /*String result = redisService.getHashValue(RuleEum.DelayTimeCache.getMapKey(), terminalId, String.class);
        if (result != null)
        {
            return true;
        }*/
        return false;
    }
}
