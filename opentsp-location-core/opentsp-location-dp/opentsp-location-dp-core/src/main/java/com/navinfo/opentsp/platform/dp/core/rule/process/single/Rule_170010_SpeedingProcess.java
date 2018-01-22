package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170010_Status;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaSpeeding.AreaSpeeding;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 规则编码：speeding	170010	区域超速报警   
 *
 */
@SingleRuleAnno(ruleId = RegularCode.speeding_VALUE)
public class Rule_170010_SpeedingProcess extends SingleRegularProcess {

	@Resource
	private RuleStatusCache ruleStatusCache;

	@Resource
	private AreaCache areaCache;

	@Override
	public GpsLocationDataEntity process(RuleEntity rule,
			GpsLocationDataEntity dataEntity) {
		List<AreaSpeeding> rules = rule.getAreaSpeedingRule();
		if (rule != null && rules.size() > 0) {
			long terminalId = dataEntity.getTerminalId();
			// 获取状态缓存
			Rule_170010_Status ruleStatus = ruleStatusCache.getRuleStatus(RegularCode.speeding_VALUE, terminalId);
			// 获取此终端区域集合
			Map<Long, AreaEntity> areaMap = areaCache.getAreaEntity(terminalId);
			if(areaMap == null)
				return dataEntity;
			for (AreaSpeeding areaSpeeding : rules) {
				AreaEntity areaEntity = areaMap.get(areaSpeeding.getAreaId());
				if(areaEntity!=null){
					List<AreaDataEntity> areaDataEntities = areaEntity.getDatas();
					String unqiueId = this.uniqueId(areaSpeeding.getAreaId(), areaSpeeding.getRouteId());
					if (areaSpeeding.getBasedTime()) {
						boolean dateIsLegal = Help.dateIsLegal(
								dataEntity.getGpsDate(),
								areaSpeeding.getStartDate(),
								areaSpeeding.getEndDate(),
								areaSpeeding.getIsEveryDay());
						if (!dateIsLegal) {
							// 不在规则时间内
							if (ruleStatus != null) {
								ruleStatus.reset(areaSpeeding.getAreaId(),unqiueId,
										areaSpeeding.getTypes());
							}
							continue;
						}
					}
					boolean is_in_area = false;
					if (dataEntity.getSpeed() > areaSpeeding.getMaxSpeed()) {
						// 据区域类型进行不同几何运算
						switch (areaSpeeding.getTypes().getNumber()) {
						case AreaType.circle_VALUE:
							is_in_area = GeometricCalculation.insideCircle(
									dataEntity.getLongitude(), dataEntity
									.getLatitude(), areaEntity.getDatas()
											.get(0), areaEntity.getDatas().get(0)
											.getRadiusLength());

							break;
						case AreaType.rectangle_VALUE:
							is_in_area = GeometricCalculation.insideRectangle(
									dataEntity.getLongitude(),
									dataEntity.getLatitude(),
									areaDataEntities.get(0),
									areaDataEntities.get(1));

							break;
						case AreaType.polygon_VALUE:
							is_in_area = GeometricCalculation.insidePolygon(
									dataEntity.getLongitude(),
									dataEntity.getLatitude(), areaDataEntities);

							break;
						case AreaType.segment_VALUE:
							// 收集路段点信息
							List<AreaDataEntity> routes = new ArrayList<AreaDataEntity>();
							for (int i = 0, size = areaDataEntities.size(); i < size; i++) {
								if (areaDataEntities.get(i).getDataStatus() == areaSpeeding
										.getRouteId()) {
									routes.add(areaDataEntities.get(i));
									routes.add(areaDataEntities.get(i + 1));
									break;
								}
							}
							if (routes.size() == 0) {
								break;
							}
							is_in_area = GeometricCalculation.insideRoute(
									dataEntity.getLongitude(), dataEntity
											.getLatitude(), routes);
							break;
						default:
							log.error("\n不支持此类型限速报警：\n"+this.formartRule(areaSpeeding));
							break;
						}
						// 更新状态以及Gps数据信息
						if (is_in_area) {
							// 从缓存中获取当前区域的状态信息,判断持续时间
							if (ruleStatus != null
									&& ruleStatus.isContinuousTime(
											areaSpeeding.getAreaId(),unqiueId,
											areaSpeeding.getTypes(),
											areaSpeeding.getContinuousTime(),
											dataEntity.getGpsDate())) {
								long routeId = areaSpeeding.getTypes().getNumber() == AreaType.segment_VALUE ? areaSpeeding.getRouteId() : 0;						
								dataEntity.addSpeedAddition(areaSpeeding.getTypes()
										.getNumber(), areaSpeeding.getAreaId() , routeId);
							} else {
								// 创建此区域的状态信息
								if (ruleStatus == null) {
									ruleStatus = new Rule_170010_Status();
									ruleStatus.addRule170010StatusEntry(
											areaSpeeding.getAreaId(),unqiueId,
											areaSpeeding.getTypes(),
											dataEntity.getGpsDate());
									ruleStatusCache.addRuleStatus(
											RegularCode.speeding_VALUE, terminalId,
											ruleStatus);
								}
							}
						} else {
							// 出区域,重置状态
							if (ruleStatus != null)
								ruleStatus.reset(areaEntity.getAreaId(),unqiueId,
										areaSpeeding.getTypes());
						}
					} else {
						// 无超速
						if (ruleStatus != null) {
							ruleStatus.reset(areaSpeeding.getAreaId(),unqiueId,
									areaSpeeding.getTypes());
						}
					}
				}
			}
		}
		return dataEntity;
	}

	private String formartRule(AreaSpeeding areaSpeeding) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("规则内容：\n");
		buffer.append("区域标识=" + areaSpeeding.getAreaId() + "\n");
		buffer.append("区域类型=" + areaSpeeding.getTypes().name() + "\n");
		buffer.append("路线标识=" + areaSpeeding.getRouteId() + "\n");
		buffer.append("最高速度=" + areaSpeeding.getMaxSpeed() + "\n");
		buffer.append("超速持续时间=" + areaSpeeding.getContinuousTime() + "\n");
		buffer.append("是否依据时间=" + areaSpeeding.getBasedTime() + "\n");
		buffer.append("依据时间类型=" + (areaSpeeding.getIsEveryDay() ? "每天" : "按日期")
				+ "\n");
		buffer.append("开始时间="
				+ DateUtils.format(areaSpeeding.getStartDate(),
						DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS) + "\n");
		buffer.append("结束时间="
				+ DateUtils.format(areaSpeeding.getEndDate(),
						DateUtils.DateFormat.YY_YY_MM_DD_HH_MM_SS) + "\n");
		return buffer.toString();
	}
	
	private String uniqueId(long areaId , long routeId){
		return areaId+"_"+routeId;
	}
}
