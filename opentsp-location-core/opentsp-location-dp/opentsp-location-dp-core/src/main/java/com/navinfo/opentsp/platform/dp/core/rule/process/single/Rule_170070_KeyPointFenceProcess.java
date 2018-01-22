package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCKeyPointFence.KeyPointFence;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170070_Status;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170070_Status.Rule_170070StatusEntry;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help.InOutMark;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 规则编码：keyPointFence	170070	关键点围栏报警
 * @author Administrator
 *
 */
@SingleRuleAnno(ruleId = RegularCode.keyPointFence_VALUE)
public class Rule_170070_KeyPointFenceProcess extends SingleRegularProcess {

	@Resource
	private AreaCache areaCache;

	@Resource
	private RuleStatusCache ruleStatusCache;

	@Override
	public GpsLocationDataEntity process(RuleEntity ruleEntity, GpsLocationDataEntity dataEntity) {
		if(ruleEntity != null){
			Map<Long, AreaEntity> areaMap = areaCache.getAreaEntity(dataEntity.getTerminalId()); //获取区域
			if(areaMap == null){
				return dataEntity;
			}
			List<KeyPointFence> rules = ruleEntity.getKeyPointFenceRule(); //获取该终端的规则
			for (KeyPointFence rule : rules) {
				AreaEntity areaEntity = areaMap.get(rule.getAreaId());
				if(areaEntity == null){
					continue;
				}
				if(rule.getNotArriveFenceAlarmToDriver() || rule.getNotArriveFenceAlarmToPlatform() || rule.getNotLeaveFenceAlarmToDriver() || rule.getNotLeaveFenceAlarmToPlatform()){
					Rule_170070_Status ruleStatus = ruleStatusCache.getRuleStatus(RegularCode.keyPointFence_VALUE, dataEntity.getTerminalId());
					if(ruleStatus == null){
						ruleStatus = new Rule_170070_Status();
						ruleStatusCache.addRuleStatus(RegularCode.keyPointFence_VALUE, dataEntity.getTerminalId(), ruleStatus);
					}
					//判断是否在时间范围内
					boolean dateIsLegal = Help.dateIsLegal(dataEntity.getGpsDate(),
							rule.getStartDate(), rule.getEndDate(),
							rule.getIsEveryDay());
					if(dateIsLegal){//时间范围内，处理状态
						InOutMark inOutMark = checkStatusInOrOut(areaEntity, dataEntity);
						//未按时到达，在规定时间段：只要有一个位置点状态为围栏内，不报警；所有点全部在围栏外，报警
						if((rule.getNotArriveFenceAlarmToDriver() || rule.getNotArriveFenceAlarmToPlatform()) && inOutMark == InOutMark.In){
							ruleStatus.addMeetStatus(areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark, dataEntity.getGpsDate());
						}
						//未按时离开，在规定时间段：只要有一个位置点状态为围栏外，不报警；所有点全部在围栏内，报警
						if((rule.getNotLeaveFenceAlarmToDriver() || rule.getNotLeaveFenceAlarmToPlatform()) && inOutMark == InOutMark.Out){
							ruleStatus.addMeetStatus(areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark, dataEntity.getGpsDate());
						}
					}else{
						//报警处理时间是否为当天
						boolean handleIsToday = Help.dateOnSameDay(ruleStatus.getTimeStamp(), System.currentTimeMillis()/1000);
						if(handleIsToday){
							//当前规则是否被处理过了（规则设置的结束时间如果在处理时间之前，表示规则被处理了）
							boolean ruleIsAfterHandle = Help.dateIsAfer(ruleStatus.getTimeStamp(), rule.getEndDate(), rule.getIsEveryDay());
							if(!ruleIsAfterHandle){	//报警未被处理过，判断条件产生报警
								boolean dateIsAfer = Help.dateIsAfer(
										dataEntity.getGpsDate(), rule.getEndDate(),
										rule.getIsEveryDay());
								if(dateIsAfer){//时间范围后，处理报警
									Rule_170070StatusEntry rule_170070StatusEntry = ruleStatus.getMeetStatus(areaEntity.getAreaId(), areaEntity.getAreaType());
									if(rule_170070StatusEntry == null){//没有符合要求的数据，报警添加到报警缓存
										if(rule.getNotArriveFenceAlarmToDriver()){
											//TODO : 未按时抵达报警给驾驶员
										}
										if(rule.getNotArriveFenceAlarmToPlatform()){
											ruleStatus.addFenceAlarm(areaEntity.getAreaId(), areaEntity.getAreaType(), true);
										}
										if(rule.getNotLeaveFenceAlarmToDriver()){
											//TODO : 未按时离开报警给驾驶员
										}
										if(rule.getNotLeaveFenceAlarmToPlatform()){
											ruleStatus.addFenceAlarm(areaEntity.getAreaId(), areaEntity.getAreaType(), false);
										}
									}
								}
							}else{//清理数据该规则的状态缓存
								ruleStatus.removeMeetStatus(
										areaEntity.getAreaId(),
										areaEntity.getAreaType(),
										rule.getStartDate(), rule.getEndDate(),
										rule.getIsEveryDay());
							}
						}
					}
					
				}
			}
			//更加报警缓存，统一添加附加报警数据
			Rule_170070_Status ruleStatus = ruleStatusCache.getRuleStatus(RegularCode.keyPointFence_VALUE, dataEntity.getTerminalId());
			handlerGpsData(dataEntity, ruleStatus);
		}
		return dataEntity;
	}
	
	/***************************
	 * 根据报警缓存状态，添加报警附加信息
	 * 
	 * @param dataEntity
	 * @param entity
	 */
	private void handlerGpsData(GpsLocationDataEntity dataEntity, Rule_170070_Status entity){
		//进出报警缓存
		Map<String, Boolean> rulesData = entity.getAlarmCache();
		if(rulesData != null){
			for(String key:rulesData.keySet()){
				boolean notArriveOrLeave = rulesData.get(key);
				long areaId = Long.parseLong(key.split("_")[0]);
			    int type = Integer.parseInt(key.split("_")[1]);
				dataEntity.addFenceAddition(areaId, type, notArriveOrLeave);
			}
		}
	}
	
	/**
	 * 判断点是否在区域（圆形，矩形，多边形）内
	 * @param areaEntity
	 * @param dataEntity
	 * @return 点在区域内的状态枚举
	 */
	private InOutMark checkStatusInOrOut(AreaEntity areaEntity, GpsLocationDataEntity dataEntity){
		boolean isInArea = false;
		List<AreaDataEntity> areaDataEntities = areaEntity.getDatas();
		switch (areaEntity.getAreaType()) {
		    case AreaType.circle_VALUE:
		    	isInArea = GeometricCalculation.insideCircle(dataEntity.getLongitude(),dataEntity.getLatitude(),areaEntity.getDatas().get(0),areaEntity.getDatas().get(0).getRadiusLength());
			break;
		    case AreaType.rectangle_VALUE:
		    	isInArea = GeometricCalculation.insideRectangle(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities.get(0), areaDataEntities.get(1));
			break;
		    case AreaType.polygon_VALUE:
		    	isInArea = GeometricCalculation.insidePolygon(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities);
			break;
		}
		InOutMark inoutMark = isInArea ? InOutMark.In : InOutMark.Out;
		return inoutMark;
	}
}
