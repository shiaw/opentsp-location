package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.redis .JacksonUtil;
import com.navinfo.opentsp.platform.dp.core.redis .RedisUtil;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.dp.core.rule.entity.RouteDriverTimeAddition;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * 路线行驶时间规则状态
 * 
 * @author jin_s
 * 
 */
public class Rule_170030_Status implements Serializable, RuleStatus {
	private static final long serialVersionUID = 2295833714299277850L;

	@Resource
	private IRedisService redisService;

	/**
	 * 添加状态缓存
	 * @param gpsTime
	 * @param lineId
	 * @param routeId
     */
	public void addRule170030StatusEntry(long gpsTime, long lineId, long routeId) {
		Rule_170030_StatusEntry entry = new Rule_170030_StatusEntry();
		entry.setFirstInTime(gpsTime);
		entry.setLastTime(gpsTime);
		entry.setLineId(lineId);
		entry.setRouteId(routeId);
//		RedisUtil.getRedis().setHashValue(RuleEum.R170030.getMapKey(), routeId + "_" + lineId, entry);
		redisService.hset(RuleEum.R170030.getMapKey(), routeId + "_" + lineId, entry);
	}

	/**
	 * 重置指定区域的状态信息
	 * 
	 * @param routeId
	 * @param lineId
	 */
	public void reset(long routeId, long lineId) {
//		RedisUtil.getRedis().delHashValue(RuleEum.R170030.getMapKey(), routeId + "_" + lineId);
		redisService.delete(RuleEum.R170030.getMapKey(), routeId + "_" + lineId);
	}

	/**
	 * 重置所有状态
	 */
	public void reset() {
//		Map<String ,String> redisMap = RedisUtil.getRedis().getAllKeys(RuleEum.R170030.getMapKey());
		Map<String ,String> redisMap = redisService.getAllKeys(RuleEum.R170030.getMapKey(), String.class);
		for(Map.Entry<String,String> temp : redisMap.entrySet()) {
			Rule_170030_StatusEntry ruleObject = (Rule_170030_StatusEntry)JacksonUtil.
					readValue(temp.getValue(),RuleEum.R170030.getClassType());
			ruleObject.reset();
//			RedisUtil.getRedis().setHashValue(RuleEum.R170030.getMapKey(), temp.getKey(), ruleObject);
			redisService.hset(RuleEum.R170030.getMapKey(), temp.getKey(), ruleObject);
		}
	}

	public static class Rule_170030_StatusEntry implements Serializable {
		private static final long serialVersionUID = 1L;
		private long routeId;
		private long lineId;
		private long firstInTime;
		private long lastTime;

		public void reset() {
			this.firstInTime = 0;
			this.lastTime = 0;
		}

		public Rule_170030_StatusEntry() {
			super();
		}

		public long getRouteId() {
			return routeId;
		}

		public void setRouteId(long routeId) {
			this.routeId = routeId;
		}

		public long getLineId() {
			return lineId;
		}

		public void setLineId(long lineId) {
			this.lineId = lineId;
		}

		public long getFirstInTime() {
			return firstInTime;
		}

		public void setFirstInTime(long firstInTime) {
			this.firstInTime = firstInTime;
		}

		public long getLastTime() {
			return lastTime;
		}

		public void setLastTime(long lastTime) {
			this.lastTime = lastTime;
		}

	}

	public Rule_170030_StatusEntry getLineIdCache(String lineId) {
//		return (Rule_170030_StatusEntry)RedisUtil.getRedis().
//				getHashValue(RuleEum.R170030.getMapKey(),lineId,RuleEum.R170030.getClassType());
		return redisService.getHashValue(RuleEum.R170030.getMapKey(), lineId, Rule_170030_StatusEntry.class);
	}
	//清除已报警缓存的累计时间，清除所有已产生的报警缓存
	@Override
	public void alarmHandle() {
		for (Map.Entry<String, RouteDriverTimeAddition> entry : this.getRule170030Alarm().entrySet()) {
			String[] key = entry.getKey().split("_");
			String cacheKey = key[1]+"_"+key[2];
			RedisUtil.getRedis().delHashValue(RuleEum.R170030.getMapKey(),cacheKey);
		}
		RedisUtil.getRedis().del(RuleEum.R170030_AlarmCache.getMapKey());
		redisService.delete(RuleEum.R170030_AlarmCache.getMapKey());
	}

	public Map<String, RouteDriverTimeAddition> getRule170030Alarm() {
		return redisService.getAllKeys(RuleEum.R170030_AlarmCache.getMapKey(), RuleEum.R170030_AlarmCache.getClassType());

	}

	public void addRule170030Alarm(long terminalId, long routeId, long lineId,
								   String type, RouteDriverTimeAddition routeAddition) {
		redisService.hset(RuleEum.R170030_AlarmCache.getMapKey(),
				terminalId + "_" + routeId + "_" + lineId + "_" + type, routeAddition);
	}

}
