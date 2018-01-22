package com.navinfo.opentsp.platform.dp.core.rule.status.single;

import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.dp.core.rule.RuleStatus;
import com.navinfo.opentsp.platform.dp.core.rule.status.RuleEum;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help.InOutMark;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Rule_170070_Status implements Serializable, RuleStatus {
	private static final long serialVersionUID = 1L;

	@Resource
	private IRedisService redisService;

	//持续记录区域的历史报警状态<Key 区域编号+区域类型+未按时离开或到达的状态, Value: true：未按时抵达关键点；false：未按时离开关键点； >
	private Map<String, Boolean> alarmCache = new ConcurrentHashMap<String, Boolean>();

	//符合条件的状态点<Key 区域编号+区域类型, 进出状态>
	private Map<String, Rule_170070StatusEntry> meetStatus = new ConcurrentHashMap<String, Rule_170070StatusEntry>();
	//报警处理时间戳,单位秒
	private long timeStamp;
	/**
	 * 清除属于该规则的状态信息
	 * @param areaId
	 * @param areaType
	 * @param begin
	 * @param end
	 * @param isEveryDay
	 */
	public void removeMeetStatus(long areaId, int areaType, long begin, long end, boolean isEveryDay){
		Rule_170070StatusEntry rule_170070StatusEntry = getMeetStatus(areaId, areaType);
		if(rule_170070StatusEntry != null){
			boolean dateIsLegal = Help.dateIsLegal(rule_170070StatusEntry.getGpaDate(), begin, end, isEveryDay);
			if(dateIsLegal){
//				meetStatus.remove(areaId + "_" + areaType);
				redisService.delete(RuleEum.R170060.getMapKey(), areaId + "_" + areaType);
			}
		}
	}
	/**
	 * 根据区域标识和区域类型为Key，添加状态数据
	 * @param areaId
	 * @param areaType
	 * @param inOutMark
	 */
	public void addMeetStatus(long areaId, int areaType, InOutMark inOutMark, long gpsDate){
		Rule_170070StatusEntry rule_170070StatusEntry = meetStatus.get(areaId+"_"+areaType);
		if(rule_170070StatusEntry==null){
			rule_170070StatusEntry = new Rule_170070StatusEntry();
		}
		rule_170070StatusEntry.setInOutMark(inOutMark);
		rule_170070StatusEntry.setGpaDate(gpsDate);
//		meetStatus.put(areaId + "_" + areaType, rule_170070StatusEntry);
		redisService.hset(RuleEum.R170070.getMapKey(), areaId + "_" + areaType, rule_170070StatusEntry);
	}
	
	public Rule_170070StatusEntry getMeetStatus(long areaId, int areaType){
//		return meetStatus.get(areaId + "_" + areaType);

		return redisService.getHashValue(RuleEum.R170070.getMapKey(), areaId + "_" + areaType, Rule_170070StatusEntry.class);
	}
	
	public void addFenceAlarm(long areaId, int areaType, boolean notArriveOrLeave ){
//		alarmCache.put(areaId+"_"+areaType+"_"+notArriveOrLeave, notArriveOrLeave);
		redisService.hset(RuleEum.R170070_AlarmCache.getMapKey(), areaId+"_"+areaType+"_"+notArriveOrLeave, notArriveOrLeave);
	}
	
	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}
	
	public Map<String, Boolean> getAlarmCache() {
		Map<String, Boolean> temp = redisService.getAllKeys(RuleEum.R170070_AlarmCache.getMapKey(), Boolean.class);
//		return alarmCache;
		return temp;
	}

	@Override
	public void alarmHandle() {
		timeStamp = System.currentTimeMillis()/1000;
//		alarmCache.clear();
		redisService.delete(RuleEum.R170070_AlarmCache.getMapKey());
	}
	
	public class Rule_170070StatusEntry {
		//对应某个区域的状态
		private InOutMark inOutMark;
		//有效信息点时间
		private long gpaDate;
		
		public Rule_170070StatusEntry() {
			super();
		}
		
		public InOutMark getInOutMark() {
			return inOutMark;
		}
		public void setInOutMark(InOutMark inOutMark) {
			this.inOutMark = inOutMark;
		}
		public long getGpaDate() {
			return gpaDate;
		}
		public void setGpaDate(long gpaDate) {
			this.gpaDate = gpaDate;
		}
	}

}
