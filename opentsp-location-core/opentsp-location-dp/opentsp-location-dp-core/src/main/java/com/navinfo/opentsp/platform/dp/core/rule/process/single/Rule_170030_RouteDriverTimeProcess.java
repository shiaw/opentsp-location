package com.navinfo.opentsp.platform.dp.core.rule.process.single;

import com.navinfo.opentsp.platform.dp.core.cache.AreaCache;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRuleAnno;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRouteDriverTime.RouteDriverTime;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaDataEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.cache.entity.RuleEntity;
import com.navinfo.opentsp.platform.dp.core.rule.entity.RouteDriverTimeAddition;
import com.navinfo.opentsp.platform.dp.core.rule.handler.single.SingleRegularProcess;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.RuleStatusCache;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170030_Status;
import com.navinfo.opentsp.platform.dp.core.rule.status.single.Rule_170030_Status.Rule_170030_StatusEntry;
import com.navinfo.opentsp.platform.dp.core.rule.tools.GeometricCalculation;
import com.navinfo.opentsp.platform.dp.core.rule.tools.Help;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 规则编码：routeDriverTime	170030	路线行驶时间不足或者过长
 * @author jin_s
 * 
 */
@SingleRuleAnno(ruleId = RegularCode.routeDriverTime_VALUE)
public class Rule_170030_RouteDriverTimeProcess extends SingleRegularProcess {

	@Resource
	private RuleStatusCache ruleStatusCache;

	@Resource
	private AreaCache areaCache;

	@Override
	public GpsLocationDataEntity process(RuleEntity rule,
										 GpsLocationDataEntity dataEntity) {
		List<RouteDriverTime> rules = rule.getRouteDriverTimeRule();
		long terminalId = dataEntity.getTerminalId();
		Rule_170030_Status ruleStatus = ruleStatusCache.getRuleStatus(RegularCode.routeDriverTime_VALUE, terminalId);

		if (rule != null && rules.size() > 0) {
			// 获取此终端区域集合
			Map<Long, AreaEntity> areaMap = areaCache.getAreaEntity(terminalId);
			if (areaMap == null)
				return dataEntity;
			for (RouteDriverTime routeDriverTime : rules) {
				// 是否有时间限制
				if (routeDriverTime.getBasedTime()) {
					boolean dateIsLegal = Help.dateIsLegal(
							dataEntity.getGpsDate(),
							routeDriverTime.getStartDate(),
							routeDriverTime.getEndDate(),
							routeDriverTime.getIsEveryDay());
					if (!dateIsLegal) {
						// 不在规则时间内
						if (ruleStatus != null) {
							ruleStatus.reset(routeDriverTime.getRouteId(),
									routeDriverTime.getLineId());
						}
						continue;
					}

					// 创建此区域的状态信息
					if (ruleStatus == null) {
						ruleStatus = new Rule_170030_Status();
						// ruleStatus.addRule170030StatusEntry(dataEntity.getGpsDate(),routeDriverTime.getLineId(),
						// routeDriverTime.getRouteId());
						ruleStatusCache.addRuleStatus(
								RegularCode.routeDriverTime_VALUE, terminalId,
								ruleStatus);
					}

					AreaEntity areaEntity = areaMap.get(routeDriverTime
							.getRouteId());
					List<AreaDataEntity> areaDataEntities = areaEntity
							.getDatas();

					// 收集路段点信息
					List<AreaDataEntity> routes = new ArrayList<AreaDataEntity>();
					for (int i = 0, size = areaDataEntities.size(); i < size; i++) {
						if (areaDataEntities.get(i).getDataStatus() == routeDriverTime
								.getLineId()) {
							routes.add(areaDataEntities.get(i));
							routes.add(areaDataEntities.get(i + 1));
							break;
						}
					}
					if (routes.size() == 0) {
						log.error("未找到路线[" + routeDriverTime.getRouteId()
								+ "] > 路段[" + routeDriverTime.getLineId()
								+ "]的规则数据.");
						break;
					}

					// boolean isInRoute =
					// GeometricCalculation.insideRoute(dataEntity.getLongitude(),dataEntity.getLatitude(),routes,
					// routes.get(0).getRadiusLength());
					boolean isInRoute = GeometricCalculation.insideRoute(
							dataEntity.getLongitude(),
							dataEntity.getLatitude(), routes);
					if (isInRoute) {
						// 判断是否首次进入路段
						Rule_170030_StatusEntry lineInfo = ruleStatus
								.getLineIdCache(routeDriverTime.getRouteId()
										+ "_" + routeDriverTime.getLineId());
						// 首次进入路段
						if (null == lineInfo) {
							ruleStatus.addRule170030StatusEntry(
									dataEntity.getGpsDate(),
									routeDriverTime.getRouteId(),
									routeDriverTime.getLineId());
							// ruleStatus.addRule170030StatusEntry(dataEntity.getGpsDate(),routeDriverTime.getLineId(),
							// routeDriverTime.getRouteId());
						} else {

							lineInfo.setLastTime(dataEntity.getGpsDate());
							int inRouteTime = (int) (dataEntity.getGpsDate() - lineInfo
									.getFirstInTime());
							if (inRouteTime > routeDriverTime.getMaxTime()) {

								ruleStatus.addRule170030Alarm(terminalId,
										routeDriverTime.getRouteId(),
										routeDriverTime.getLineId(), "L",
										new RouteDriverTimeAddition(
												routeDriverTime.getRouteId(),
												inRouteTime, false,
												routeDriverTime.getLineId()));
								// Rule_170030_Status.addRule170030Alarm(terminalId,
								// routeDriverTime.getRouteId(),
								// routeDriverTime.getLineId(),new
								// GpsLocationDataEntity().new
								// RouteAddition(routeDriverTime.getRouteId(),
								// inRouteTime,false,routeDriverTime.getLineId()));//new
								// RouteAddition(routeDriverTime.getLineId(),
								// inRouteTime,false));
								// 行驶时间超长报警
								// dataEntity.addRouteAddition(routeDriverTime.getRouteId(),
								// inRouteTime,
								// false,routeDriverTime.getLineId());
							}
						}

					} else {
						Rule_170030_StatusEntry lineInfo = ruleStatus
								.getLineIdCache(routeDriverTime.getRouteId()
										+ "_" + routeDriverTime.getLineId());
						if (lineInfo != null) {
							int inRouteTime = (int) (lineInfo.getLastTime() - lineInfo
									.getFirstInTime());
							if (inRouteTime < routeDriverTime.getMinTime()) {
								// if(rule170030Alarm==null||!rule170030Alarm.containsKey(routeDriverTime.getRouteId()+"_"+
								// routeDriverTime.getLineId())){
								ruleStatus.addRule170030Alarm(terminalId,
										routeDriverTime.getRouteId(),
										routeDriverTime.getLineId(), "S",
										new RouteDriverTimeAddition(
												routeDriverTime.getRouteId(),
												inRouteTime, true,
												routeDriverTime.getLineId()));
								// 行驶时间超长报警
								// dataEntity.addRouteAddition(routeDriverTime.getRouteId(),
								// inRouteTime,
								// true,routeDriverTime.getLineId());

							}
							// 无路线路段行驶时间不足
							ruleStatus.reset(routeDriverTime.getRouteId(),
									routeDriverTime.getLineId());

						}

					}

				}

			}
		}
		if (ruleStatus != null) {
			Map<String, RouteDriverTimeAddition> rule170030Alarm = ruleStatus
					.getRule170030Alarm();
			if (rule170030Alarm != null && !rule170030Alarm.isEmpty()) {

				Collection<RouteDriverTimeAddition> values = rule170030Alarm
						.values();
				for (RouteDriverTimeAddition alarm : values) {
					dataEntity.addRouteAddition(alarm.getAreaId(),
							alarm.getRouteTime(), alarm.isRouteResult(),
							alarm.getLineId());
				}
			}
		}
		return dataEntity;
	}

}
