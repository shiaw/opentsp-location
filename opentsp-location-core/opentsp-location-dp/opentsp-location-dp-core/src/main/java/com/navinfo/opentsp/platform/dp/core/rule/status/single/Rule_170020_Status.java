package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help.InOutMark;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;

/**
 * 
 * alarmCache 缓存产生的报警，在业务系统下发报警处理前，始终上报缓存中的报警
 * 
 * lastStatus 最后状态，缓存位置数据点的最后状态，用于规则处理
 *
 */
public class Rule_170020_Status implements Serializable,RuleStatus {

	private static final long serialVersionUID = 1L;
	@Resource
	private IRedisService redisService;

	/**
	 * 设置区域的最新状态
	 * @param gpsDate
	 * @param areaId
	 * @param areaType
	 * @param inOutStatus
     */
	public void updateLatestStatus(long gpsDate, long areaId, int areaType, InOutMark inOutStatus){
		Rule_170020StatusEntry statusEntry = redisService.
				getHashValue(RuleEum.R170020_LastStatus.getMapKey(),
						areaId+"_"+areaType,
						Rule_170020StatusEntry.class);
		if(statusEntry == null){
			//首次创建缓存标记
			statusEntry = new Rule_170020StatusEntry();
			statusEntry.setInOutMark(inOutStatus);
			statusEntry.setAreaType(areaType);
			statusEntry.setTimestemp(gpsDate);
		} else {
			//更新最新的缓存状态
			statusEntry.setInOutMark(inOutStatus);
			statusEntry.setAreaType(areaType);
			statusEntry.setTimestemp(gpsDate);
		}
		redisService.hset(RuleEum.R170020_LastStatus.getMapKey(),
				areaId + "_" + areaType, statusEntry);
	}
	/**
	 * 根据区域ID，区域类型，进出标识判断是否有报警
	 * @param areaId
	 * @param areaType
	 * @param inOutMark
	 * @return true 有报警  false 无报警
	 */
	public boolean getAlarm(long areaId, int areaType, InOutMark inOutMark){
		Rule_170020StatusEntry statusEntry = redisService.
				getHashValue(RuleEum.R170020_AlarmCache.getMapKey(),
						areaId+"_"+areaType+"_"+inOutMark.ordinal(),
						Rule_170020StatusEntry.class);
		if(statusEntry == null){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 更新历史的报警
	 * @param gpsDate
	 * @param areaId
	 * @param areaType
	 * @param inOutStatus
     */
	public void updateAlarm(long gpsDate, long areaId, int areaType, InOutMark inOutStatus){
		Rule_170020StatusEntry statusEntry = redisService.
				getHashValue(RuleEum.R170020_AlarmCache.getMapKey(),
						areaId+"_"+areaType+"_"+inOutStatus.ordinal(),
						Rule_170020StatusEntry.class);
		//首次创建缓存标记
		if(statusEntry == null){
			statusEntry = new Rule_170020StatusEntry();
			statusEntry.setInOutMark(inOutStatus);
			statusEntry.setAreaType(areaType);
			statusEntry.setTimestemp(gpsDate);
		} else {
			//更新最新的缓存状态
			statusEntry.setInOutMark(inOutStatus);
			statusEntry.setAreaType(areaType);
			statusEntry.setTimestemp(gpsDate);
		}
		redisService.hset(RuleEum.R170020_AlarmCache.getMapKey(),
				areaId + "_" + areaType + "_" + inOutStatus.ordinal(), statusEntry);
	}

	/**
	 * 记录产生的报警
	 * @param gpsDate
	 * @param areaId
	 * @param areaType
	 * @param inOutMark
     */
	public void addAlarm(long gpsDate, long areaId , int areaType, InOutMark inOutMark){
		Rule_170020StatusEntry statusEntry = new Rule_170020StatusEntry();
		statusEntry.setInOutMark(inOutMark);
		statusEntry.setAreaType(areaType);
		statusEntry.setTimestemp(gpsDate);
		redisService.hset(RuleEum.R170020_AlarmCache.getMapKey(),
				areaId+"_"+areaType+"_"+inOutMark.ordinal(), statusEntry);
	}
	
	/*******************
	 * 获取最近一次位置数据状态
	 * 
	 * @param areaId
	 * @param areaType
	 * @return
	 */
	public Rule_170020StatusEntry getLastStatus(long areaId, int areaType){
		return redisService.
				getHashValue(RuleEum.R170020_LastStatus.getMapKey(),
						areaId+"_"+areaType,
						Rule_170020StatusEntry.class);
	}

	public Map<String, Rule_170020StatusEntry> getAlarmCache() {
		return redisService.getAllKeys(RuleEum.R170020_AlarmCache.getMapKey(), Rule_170020StatusEntry.class);
	}

	public static class Rule_170020StatusEntry {
		//对应某个区域的状态
		private InOutMark inOutMark;
		//区域类型
		private int areaType;
		//时间戳
		private long timestemp;
		
		public Rule_170020StatusEntry() {
			super();
		}
		
		public int getAreaType() {
			return areaType;
		}
		public void setAreaType(int areaType) {
			this.areaType = areaType;
		}
		public InOutMark getInOutMark() {
			return inOutMark;
		}
		public void setInOutMark(InOutMark inOutMark) {
			this.inOutMark = inOutMark;
		}
		public long getTimestemp() {
			return timestemp;
		}
		public void setTimestemp(long timestemp) {
			this.timestemp = timestemp;
		}
	}

	@Override
	public void alarmHandle() {
		//清除报警缓存
		redisService.delete(RuleEum.R170020_AlarmCache.getMapKey());
	}
}
