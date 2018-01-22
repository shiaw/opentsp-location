package com.navinfo.opentsp.platform.dp.core.rule.status.common;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component("rule_170190_Status")
public class Rule_170190_Status implements Serializable {
	
//	private Map<String, Boolean> statusCache  = new ConcurrentHashMap<String, Boolean>();
//	private static Rule_170190_Status instance = new Rule_170190_Status();
//
//	private Rule_170190_Status(){
//
//	}
//	public final static Rule_170190_Status getInstance(){
//		return instance;
//	}
//	public Boolean statusInorOut(String key){
//		return statusCache.get(key);
//	}
//	public Boolean IscontainsKey(String key){
//		return statusCache.containsKey(key);
//	}
//	public void addStatus(String key, boolean status){
//		statusCache.put(key, status);
//	}

	private static final long serialVersionUID = 1L;

	@Resource
	private IRedisService redisService;// redis 通用dao


	public void addStaytimeParkCache(String key, long timestamp)
	{
		redisService.hset(RuleEum.R170190_NotifyCache.getMapKey(), key, timestamp);
	}

	public void addStayTimeCache(String key,Object value)
	{
		redisService.hset("LATEST_PARK_DATA", key, value);
	}


	public void delStaytimeParkCache(String key)
	{
		redisService.delete(RuleEum.R170190_NotifyCache.getMapKey(), key);
	}


//	public void delOvertimeParkNodifyCache(String key)
//	{
//		redisService.delete(RuleEum.R170090_ParkNotify.getMapKey(), key);
//	}


	public Long getStaytimeParkTime(String id)
	{
		String result = redisService.getHashValue(RuleEum.R170190_NotifyCache.getMapKey(), id, String.class);
		if (result != null)
		{
			return Long.valueOf(result);
		}
		return null;
	}

}
