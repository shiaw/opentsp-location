package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认为服务站不会重叠，即在同一时间，车辆只在一个服务器内 overtimeParkCache : Map<String, Boolean> ：<终端id+区域id,首次进入区域的时间>
 * 
 * @author Administrator
 *
 */
@Component("rule_170090_Status")
public class Rule_170090_Status implements Serializable
{
    private static final long serialVersionUID = 1L;

    private Map<String, Long> overtimeParkCache = new ConcurrentHashMap<String, Long>();//终端id+区域id首次进入区域的时间

    @Resource
    private IRedisService redisService;// redis 通用dao
    
    // key : terminalId_areaId
    public void addOvertimeParkCache(String key, long timestamp)
    {
        overtimeParkCache.put(key,timestamp);
        redisService.hset(RuleEum.R170090_ParkCache.getMapKey(), key, timestamp);
    }

    public void addStaytimeParkCache(String key, long timestamp)
    {
        redisService.hset(RuleEum.R170090_ParkCache1.getMapKey(), key, timestamp);
    }

    public void addStayTimeCache(String key,Object value)
    {
        redisService.hset("LATEST_PARK_DATA", key, value);
    }
    
    public void delOvertimeParkCache(String key)
    {
        if (overtimeParkCache.containsKey(key)){
            overtimeParkCache.remove(key);
            redisService.delete(RuleEum.R170090_ParkCache.getMapKey(), key);
        }
    }

    public void delStaytimeParkCache(String key)
    {
        redisService.delete(RuleEum.R170090_ParkCache1.getMapKey(), key);
    }
    
    public void addOvertimeParkNodifyCache(String key, long timestamp)
    {
        redisService.hset(RuleEum.R170090_ParkNotify.getMapKey(), key, timestamp);
    }
    
    public void delOvertimeParkNodifyCache(String key)
    {
        redisService.delete(RuleEum.R170090_ParkNotify.getMapKey(), key);
    }
    
    public Map<String, Long> getOvertimeParkCache()
    {
        return redisService.getAllKeys(RuleEum.R170090_ParkCache.getMapKey(), Long.class);
    }
    
    public Long getOvertimeParkTime(String id)
    {
        if (overtimeParkCache.get(id)!=null){
            return overtimeParkCache.get(id);
        }
        String result = redisService.getHashValue(RuleEum.R170090_ParkCache.getMapKey(), id, String.class);
        if (result != null)
        {
            overtimeParkCache.put(id,Long.valueOf(result));
            return Long.valueOf(result);
        }
        return null;
    }

    public Long getStaytimeParkTime(String id)
    {
        String result = redisService.getHashValue(RuleEum.R170090_ParkCache1.getMapKey(), id, String.class);
        if (result != null)
        {
            return Long.valueOf(result);
        }
        return null;
    }
    
    public Long getOvertimeParkNodifyTime(String id)
    {
        String result = redisService.getHashValue(RuleEum.R170090_ParkNotify.getMapKey(), id, String.class);
        if (result != null)
        {
            return Long.valueOf(result);
        }
        return null;
    }
}
