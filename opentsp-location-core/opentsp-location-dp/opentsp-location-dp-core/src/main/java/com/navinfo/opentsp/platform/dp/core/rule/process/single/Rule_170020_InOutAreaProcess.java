package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170020_Status;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCInOutArea.InOutArea;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**********************
 * 终端进出规则报警判断
 * 规则编码：inOutArea	170020	进出区域/路线报警
 * @author claus
 *
 */
@SingleRuleAnno(ruleId = RegularCode.inOutArea_VALUE)
public class Rule_170020_InOutAreaProcess extends SingleRegularProcess {
	private static int MAX_IDLE_TIME = 24 * 60 * 60; //区域报警的首点需要特殊处理，该变量为首点的过期时间阀值

	@Resource
	private RuleStatusCache ruleStatusCache;

	@Resource
	private AreaCache areaCache;
	/*********************
	 * 处置过程
	 * 
	 * @param   
	 * dataEntity 终端上报的GPS数据实体，不包含报警状态数据
	 * RuleEntity 上层业务系统为该终端设定的规则实体<来自于数据Rule, 由DP节点启动时请求DA获得并保存于缓存中>
	 */
	@Override
	public GpsLocationDataEntity process(RuleEntity ruleEntity, GpsLocationDataEntity dataEntity) {
		if(ruleEntity != null){
			Map<Long, AreaEntity> areaMap = areaCache.getAreaEntity(dataEntity.getTerminalId()); //获取区域
			if(areaMap == null)
				return dataEntity;
			List<InOutArea> rules = ruleEntity.getInOutAreaRule(); //获取该终端的规则
			for (InOutArea rule : rules) {
				AreaEntity areaEntity = areaMap.get(rule.getAreaId());
				if(areaEntity == null){
					return dataEntity;
				}
				//是否需要依据时间
				if (rule.getBasedTime()) {
					boolean dateIsLegal = Help.dateIsLegal(dataEntity.getGpsDate(),
							rule.getStartDate(),
							rule.getEndDate(),
							rule.getIsEveryDay());
					if (!dateIsLegal) { //不在规则时间内, 返回不处理
						continue;
					}    
				} 
				//1.按照时间运算且在规则时间范围内；2.不按照时间运算规则
				Help.InOutMark inOutMark = checkStatusInOrOut(areaEntity, dataEntity);
				handleAreaRegular(inOutMark, areaEntity, rule, dataEntity);
			}
			//更加报警缓存，统一添加附加报警数据
			Rule_170020_Status ruleStatus = ruleStatusCache.getRuleStatus(RegularCode.inOutArea_VALUE, dataEntity.getTerminalId());
			if(ruleStatus != null){
				handlerGpsData(dataEntity, ruleStatus);
			}
		}
		return dataEntity;
	}
	/**
	 * 区域规则处理
	 * @param inOutMark
	 * @param areaEntity
	 * @param rule
	 * @param dataEntity
	 */
	private void handleAreaRegular(Help.InOutMark inOutMark, AreaEntity areaEntity, InOutArea rule, GpsLocationDataEntity dataEntity){
		Rule_170020_Status ruleStatus = ruleStatusCache.getRuleStatus(RegularCode.inOutArea_VALUE, dataEntity.getTerminalId());
		if(ruleStatus == null){
			ruleStatus = new Rule_170020_Status();
			ruleStatusCache.addRuleStatus(RegularCode.inOutArea_VALUE, dataEntity.getTerminalId(), ruleStatus);
		}
		//首点
		if(ruleStatus.getLastStatus(areaEntity.getAreaId(), areaEntity.getAreaType()) == null){
			//根据规则判断是否需要产生报警给平台
			if(isAlarmToPlatform(rule, inOutMark)){
				ruleStatus.addAlarm(dataEntity.getGpsDate(), areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark);
				//设置了出区域规则，是路线，出状态,处理偏离路线
				if(rule.getOutAreaAlarmToPlatform() && areaEntity.getAreaType() == AreaType.route_VALUE && inOutMark == Help.InOutMark.Out){
					dataEntity.addDeviateAddition(areaEntity.getAreaType(), areaEntity.getAreaId(), false);
				}
			}
			//根据规则判断是否需要产生报警给驾驶员
			if(isAlarmToDriver(rule, inOutMark)){
				//TODO 报警给驾驶员
			}
		} else {
			Rule_170020_Status.Rule_170020StatusEntry entity = ruleStatus.getLastStatus(areaEntity.getAreaId(), areaEntity.getAreaType());
			long intervalTime = dataEntity.getGpsDate() - entity.getTimestemp();
			if(intervalTime >= MAX_IDLE_TIME){//超过时间阀值，按照首点处理
				if(isAlarmToPlatform(rule, inOutMark)){
					ruleStatus.addAlarm(dataEntity.getGpsDate(), areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark);
					//设置了出区域规则，是路线，出状态，处理偏离路线
					if(rule.getOutAreaAlarmToPlatform() && areaEntity.getAreaType() == AreaType.route_VALUE && inOutMark == Help.InOutMark.Out){
						dataEntity.addDeviateAddition(areaEntity.getAreaType(), areaEntity.getAreaId(), false);
					}
				}
				if(isAlarmToDriver(rule, inOutMark)){
					//TODO 报警给驾驶员
				}
			} else {
				if(entity.getInOutMark() != inOutMark){
					if(isAlarmToPlatform(rule, inOutMark)){
						ruleStatus.addAlarm(dataEntity.getGpsDate(), areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark);
					}
					if(isAlarmToDriver(rule, inOutMark)){
						//TODO 报警给驾驶员
					}
				}
				//处理偏离
				if(rule.getOutAreaAlarmToPlatform() && areaEntity.getAreaType() == AreaType.route_VALUE && inOutMark == Help.InOutMark.Out){
					if(ruleStatus.getAlarm(areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark)){
						dataEntity.addDeviateAddition(areaEntity.getAreaType(), areaEntity.getAreaId(), false);
					}else{
						dataEntity.addDeviateAddition(areaEntity.getAreaType(), areaEntity.getAreaId(), true);
					}
				}
			}
		}
		//记录最新状态
		ruleStatus.updateLatestStatus(dataEntity.getGpsDate(), areaEntity.getAreaId(), areaEntity.getAreaType(), inOutMark);
	}
	/**
	 * 判断点是否在区域（圆形，矩形，多边形，路线）内
	 * @param areaEntity
	 * @param dataEntity
	 * @return 点在区域内的状态枚举
	 */
	private Help.InOutMark checkStatusInOrOut(AreaEntity areaEntity, GpsLocationDataEntity dataEntity){
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
		    case AreaType.route_VALUE:
		    	isInArea = GeometricCalculation.insideRoute(dataEntity.getLongitude(), dataEntity.getLatitude(), areaDataEntities);
			break;
		}
		Help.InOutMark inoutMark = isInArea ? Help.InOutMark.In : Help.InOutMark.Out;
		return inoutMark;
	}
	
	/***************************
	 * 根据报警缓存状态，添加报警附加信息
	 * 
	 * @param dataEntity
	 * @param entity
	 */
	private void handlerGpsData(GpsLocationDataEntity dataEntity, Rule_170020_Status entity){
		//进出报警缓存
		Map<String, Rule_170020_Status.Rule_170020StatusEntry> rulesData = entity.getAlarmCache();
		if(rulesData != null){
			for(String key:rulesData.keySet()){
				Rule_170020_Status.Rule_170020StatusEntry item = rulesData.get(key);
				long areaId = Long.parseLong(key.split("_")[0]);
			    int type = Integer.parseInt(key.split("_")[1]);
				dataEntity.addAreaAddition(type, areaId, (item.getInOutMark() == Help.InOutMark.In) ? true : false);
			}
		}
	}
	
	/******
	 * 判断是否要报警给平台
	 * 
	 * @param rule
	 * @param inOutMark
	 * @return
	 */
	private boolean isAlarmToPlatform(InOutArea rule, Help.InOutMark inOutMark){
		if(rule.getInAreaAlarmToPlatform() && inOutMark == Help.InOutMark.In){
			return true;
		}else if(rule.getOutAreaAlarmToPlatform() && inOutMark == Help.InOutMark.Out){
			return true;
		}else{
			return false;
		}
	}
	
	/******
	 * 判断是否要报警给驾驶员
	 * 
	 * @param rule
	 * @param inOutMark
	 * @return
	 */
	private boolean isAlarmToDriver(InOutArea rule, Help.InOutMark inOutMark){
		if(rule.getInAreaAlarmToDriver() && inOutMark == Help.InOutMark.In){
			return true;
		}else if(rule.getOutAreaAlarmToDriver()&& inOutMark == Help.InOutMark.Out){
			return true;
		}else{
			return false;
		}
	}
}
