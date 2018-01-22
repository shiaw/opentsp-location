package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatus.TerminalStatus;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCDoorOpenOutArea.DoorOpenOutArea;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170050_Status;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 规则编码：doorOpenOutArea	170050	区域外开门报警
 * @author Administrator
 *
 */
@SingleRuleAnno(ruleId = RegularCode.doorOpenOutArea_VALUE)
public class Rule_170050_DoorOpenOutAreaProcess extends SingleRegularProcess {

	@Resource
	private RuleStatusCache ruleStatusCache;

	@Resource
	private AreaCache areaCache;

	@Override
	public GpsLocationDataEntity process(RuleEntity rule, GpsLocationDataEntity dataEntity) {
		if(rule == null)
			return dataEntity;
		//判断是否开门
		if((dataEntity.getStatus()&TerminalStatus.frontDoorSwitch_VALUE) == 0
				&&(dataEntity.getStatus()&TerminalStatus.middleDoorSwitch_VALUE) == 0
				&&(dataEntity.getStatus()&TerminalStatus.backDoorSwitch_VALUE) == 0
				&&(dataEntity.getStatus()&TerminalStatus.driverDoorSwitch_VALUE) == 0){
			return dataEntity;
		}	
			
		List<DoorOpenOutArea> rules = rule.getDoorOpenOutAreaRule();
		if(rules.size() > 0) {
			long terminalId = Convert.uniqueMarkToLong(dataEntity.getUniqueMark());
			Rule_170050_Status status = ruleStatusCache
					.getRuleStatus(RegularCode.doorOpenOutArea_VALUE, terminalId);
			if(status == null){
				status = new Rule_170050_Status();
				ruleStatusCache.addRuleStatus(170050, terminalId, status);
			}
			for(DoorOpenOutArea dooa : rules) {
				Map<Long, AreaEntity> map = areaCache
						.getAreaEntity(terminalId);
				AreaEntity area = map.get(dooa.getAreaId());
				//判读是否有该区域的状态缓存，如果没有，则添加。
				if(!status.hasCache(area.getOriginalAreaId(), 
						AreaType.valueOf(area.getAreaType()))){
					status.addRule170050StatusEntry(area.getOriginalAreaId(), AreaType.valueOf(area.getAreaType()), 0);
				}
				//如果不报警则进返回
				if(!status.isAlarm(area.getOriginalAreaId(), 
						AreaType.valueOf(area.getAreaType()))){
					return dataEntity;
				}
				//是否有时间限制
				if(dooa.getBasedTime()) {
					boolean isDateLegal = Help.dateIsLegal(
							dataEntity.getGpsDate(), 
							dooa.getStartDate(), 
							dooa.getEndDate(),
							dooa.getIsEveryDay());
					if(!isDateLegal) {
						status.reset();
						return dataEntity;
					}	
				}
				
				boolean isInArea = false;
				//根据区域类型判断终端是否在区域内
				switch(area.getAreaType()){
				case AreaType.circle_VALUE:
					isInArea = GeometricCalculation.insideCircle(dataEntity.getLongitude(), 
							dataEntity.getLatitude(),
							area.getDatas().get(0),
							area.getDatas().get(0).getRadiusLength());
					break;
				case AreaType.rectangle_VALUE:
					isInArea = GeometricCalculation.insideRectangle(dataEntity.getLongitude(), 
							dataEntity.getLatitude(), 
							area.getDatas().get(0),
							area.getDatas().get(1));
					break;
				case AreaType.polygon_VALUE:
					isInArea = GeometricCalculation.insidePolygon(dataEntity.getLongitude(),
							dataEntity.getLatitude(), area.getDatas());
					break;
				case AreaType.route_VALUE:
					isInArea = GeometricCalculation.insideRoute(dataEntity.getLongitude(),
							dataEntity.getLatitude(),
							area.getDatas());
					break;
				}
				if(isInArea) {
					//TODO 发出区域外开门报警
					status.updateLastAlarmTime(area.getOriginalAreaId(), 
							AreaType.valueOf(area.getAreaType()), dataEntity.getGpsDate());
					break;
				}
			}
		}
		
		return dataEntity;
	}

}
