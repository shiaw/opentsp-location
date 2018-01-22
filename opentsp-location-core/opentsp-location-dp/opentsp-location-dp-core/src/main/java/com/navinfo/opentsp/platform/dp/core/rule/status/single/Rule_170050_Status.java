package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class Rule_170050_Status implements Serializable,RuleStatus{
	
	private static final long serialVersionUID = 1L;

	private static final int INTERVAL_TIME = 5*60;

	private Map<Long, Rule_170050_StatusEntry> map = new ConcurrentHashMap<Long, Rule_170050_StatusEntry>();

	@Resource
	private IRedisService redisService;
	
	public boolean hasCache(long areaId, AreaType areaType) {
		return map.containsKey(areaId);
	}
	
	/**
	 * 添加状态缓存
	 * 
	 * @param areaId
	 *            {@link Long} 区域ID
	 * @param areaType
	 *            {@link AreaType} 区域类型
	 * @param alarmTime
	 *            {@link Long} 报警时间
	 */
	public void addRule170050StatusEntry(long areaId, AreaType areaType,
			long alarmTime) {
//		map.put(areaId, new Rule_170050_StatusEntry(areaId, alarmTime));

		redisService.hset(RuleEum.R170050.getMapKey(),areaId,
				new Rule_170050_StatusEntry(areaId, alarmTime));
	}

	/**
	 * 更新状态缓存
	 * 
	 * @param areaId
	 *            {@link Long} 区域ID
	 * @param areaType
	 *            {@link AreaType} 区域类型
	 * @param gpsTime
	 *            {@link Long} Gps时间
	 */
	public void updateLastAlarmTime(long areaId, AreaType areaType,
			long gpsTime) {
//		Rule_170050_StatusEntry statusEntry = map.get(areaId);
//		statusEntry.setLastAlarmTime(gpsTime);

		Rule_170050_StatusEntry statusEntry = redisService.getHashValue(RuleEum.R170050.getMapKey(),areaId,
				Rule_170050_StatusEntry.class);
		statusEntry.setLastAlarmTime(gpsTime);

		redisService.hset(RuleEum.R170050.getMapKey(), areaId, statusEntry);
	}
	
	public boolean isAlarm(long areaId, AreaType areaType) {
//		Rule_170050_StatusEntry statusEntry = map.get(areaId);
		Rule_170050_StatusEntry statusEntry = redisService.getHashValue(RuleEum.R170050.getMapKey(), areaId,
				Rule_170050_StatusEntry.class);
		return statusEntry.isAlarm();
	}
	
	public void enableAlarm(long areaId, AreaType areaType,boolean isAlarm) {
//		Rule_170050_StatusEntry statusEntry = map.get(areaId);
		Rule_170050_StatusEntry statusEntry = redisService.getHashValue(RuleEum.R170050.getMapKey(), areaId,
				Rule_170050_StatusEntry.class);
		statusEntry.enableAlarm(isAlarm);

		redisService.hset(RuleEum.R170050.getMapKey(), areaId, statusEntry);
	}
	
	/**
	 * 重置指定区域的状态信息
	 * @param areaId
	 * @param areaType
	 */
	public void reset(long areaId , AreaType areaType){
//		Rule_170050_StatusEntry statusEntry = map.get(areaId);
		Rule_170050_StatusEntry statusEntry = redisService.getHashValue(RuleEum.R170050.getMapKey(), areaId,
				Rule_170050_StatusEntry.class);
		statusEntry.reset();

		redisService.hset(RuleEum.R170050.getMapKey(), areaId, statusEntry);
	}

	/**
	 * 重置所有状态
	 */
	public void reset(){
		Map<Long,Rule_170050_StatusEntry> tempMap = redisService.getAllKeysByLong(RuleEum.R170050.getMapKey(), Rule_170050_StatusEntry.class);
		Iterator<Entry<Long, Rule_170050_StatusEntry>> iterator = tempMap.entrySet().iterator();
		while(iterator.hasNext()){
			Entry<Long, Rule_170050_StatusEntry> entry = iterator.next();
			entry.getValue().reset();
			redisService.hset(RuleEum.R170050.getMapKey(), entry.getKey(), entry.getValue());
		}
	}
	
	public class Rule_170050_StatusEntry implements Serializable {
		private static final long serialVersionUID = 2213652024435175894L;
		/**区域编号*/
		//private long areaId;
		/**最后一次报警时间*/
		private long lastAlarmTime;
		/**是否报警*/
		private boolean isAlarm = true;
		
		public Rule_170050_StatusEntry(long areaId,long lastAlarmTime) {
			//this.areaId = areaId;
			this.lastAlarmTime = lastAlarmTime;
		}
		
		public boolean isAlarm() {
			if(!isAlarm && (System.currentTimeMillis()/1000) - lastAlarmTime > INTERVAL_TIME)
				isAlarm = true;
			return isAlarm;
		}
		
		public void reset() {
			this.lastAlarmTime = 0;
		}

		public void setLastAlarmTime(long lastAlarmTime) {
			this.lastAlarmTime = lastAlarmTime;
		}
		
		public void enableAlarm(boolean isAlarm) {
			this.isAlarm = isAlarm;
		}
	}

	@Override
	public void alarmHandle() {
		// TODO Auto-generated method stub
		
	}
}
