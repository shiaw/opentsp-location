package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;

import javax.annotation.Resource;
import java.io.Serializable;

/**
 * 区域限速规则状态
 * @author lgw
 *
 */
public class Rule_170010_Status implements Serializable,RuleStatus {
	private static final long serialVersionUID = 2295833714299277850L;
	@Resource
	private IRedisService redisService;

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
	public void addRule170010StatusEntry(long areaId, String uniqueId, AreaType areaType, long alarmTime) {
		redisService.hset(RuleEum.R170010.getMapKey(),uniqueId,
				new Rule_170010_StatusEntry(areaId, alarmTime, alarmTime, uniqueId));
	}
	
	/**
	 * 判断是否超速是否超过指定持续时间
	 * @param areaId
	 * @param areaType
	 * @param continuousTime
	 * @param gpsTime
	 * @return
	 */
	public boolean isContinuousTime(long areaId , String uniqueId, AreaType areaType , long continuousTime , long gpsTime){
		Rule_170010_StatusEntry statusEntry = redisService.getHashValue(RuleEum.R170010.getMapKey(),
				uniqueId, Rule_170010_StatusEntry.class);
		if (statusEntry != null){
			long interval = statusEntry.getSpeedingcontinuousTime(gpsTime) - continuousTime;
			statusEntry.setLastAlarmTime(gpsTime);
			redisService.hset(RuleEum.R170010.getMapKey(), uniqueId, statusEntry);
			return interval > 0;
		}else{
			this.addRule170010StatusEntry(areaId,uniqueId,areaType,gpsTime);
			return false;
		}
	}
	/**
	 * 重置指定区域的状态信息
	 * @param areaId
	 * @param areaType
	 */
	public void reset(long areaId, String uniqueId, AreaType areaType){
		redisService.delete(RuleEum.R170010.getMapKey(),uniqueId);
	}

	public static class Rule_170010_StatusEntry implements Serializable {
		private static final long serialVersionUID = 1L;
		private long areaId;
		private long firstAlarmTime;
		private long lastAlarmTime;
		private String uniqueId;
		
		
		public void reset() {
			this.firstAlarmTime = 0;
			this.lastAlarmTime = 0;
		}
		/**
		 * 传入Gps时间,获取当前区域的持续超速时间
		 * @param current_gps_time
		 * @return
		 */
		public long getSpeedingcontinuousTime(long current_gps_time) {
			return current_gps_time - this.firstAlarmTime;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("区域ID："+this.areaId+",");
			builder.append("区域唯一标识："+this.uniqueId+",");
			// TODO: 16/8/24 时间格式要更改 
			//builder.append("首次报警时间："+(DateUtils.format(this.firstAlarmTime, DateFormat.YY_YY_MM_DD_HH_MM_SS))+",");
			//builder.append("最后报警时间："+(DateUtils.format(this.lastAlarmTime, DateFormat.YY_YY_MM_DD_HH_MM_SS))+",");
			builder.append("累计持续报警时长："+(this.formatLongToTimeString(this.lastAlarmTime - this.firstAlarmTime))+"");
			return builder.toString();
		}

		/**
		 * 将秒转换为时分秒格式
		 * 
		 * @param time
		 * @return
		 */
		public String formatLongToTimeString(long time) {
			long second = Long.parseLong(String.valueOf(time));
			if (second <= 0) {
				return "";
			}
			long hour = 0;
			long minute = 0;

			if (second > 60) {
				minute = second / 60;
				second = second % 60;
			}
			if (minute > 60) {
				hour = minute / 60;
				minute = minute % 60;
			}
			return (hour + "小时" + minute + "分钟" + second + "秒");
		}
		
		public Rule_170010_StatusEntry() {
			super();
		}


		public String getUniqueId() {
			return uniqueId;
		}

		public void setUniqueId(String uniqueId) {
			this.uniqueId = uniqueId;
		}

		public Rule_170010_StatusEntry(long areaId, long firstAlarmTime,
				long lastAlarmTime , String uniqueId) {
			this.areaId = areaId;
			this.firstAlarmTime = firstAlarmTime;
			this.lastAlarmTime = lastAlarmTime;
			this.uniqueId = uniqueId;
		}

		public long getAreaId() {
			return areaId;
		}

		public void setAreaId(long areaId) {
			this.areaId = areaId;
		}

		public long getFirstAlarmTime() {
			return firstAlarmTime;
		}

		public void setFirstAlarmTime(long firstAlarmTime) {
			this.firstAlarmTime = firstAlarmTime;
		}

		public long getLastAlarmTime() {
			return lastAlarmTime;
		}

		public void setLastAlarmTime(long lastAlarmTime) {
			this.lastAlarmTime = lastAlarmTime;
		}

	}

	@Override
	public void alarmHandle() {//报警处理对超速没有影响
		
	}
}
