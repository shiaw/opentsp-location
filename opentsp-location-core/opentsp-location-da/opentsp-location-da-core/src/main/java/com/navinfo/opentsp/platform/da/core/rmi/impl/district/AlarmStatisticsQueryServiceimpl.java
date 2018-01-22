
package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.cache.TerminalMilOilTypeCache;
import com.navinfo.opentsp.platform.da.core.cache.alarm.*;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.FlowCacheEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.WFlowEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.*;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerMilOilTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.ReportQueryService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.VehicleDrivingNumber;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.ReportQueryServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.VehicleDrivingNumberImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.WFlowServiceImp;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.AllAlarmData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.OptResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption.MileageConsumption;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 报警统计查询服务接口服务
 *
 *
 * @author claus
 *
 */
@Service
public class AlarmStatisticsQueryServiceimpl  implements AlarmStatisticsQueryService {

	private Logger log = LoggerFactory.getLogger(AlarmStatisticsQueryServiceimpl.class);


	/**
	 * 版本号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 管理当前业务系统涉及的分页缓存对象
	 */
	private static AlarmQueryManager alarmQuqeryMgmt = AlarmQueryManager.getInstance();
	private VehicleDrivingNumber vehicleDrivingNumber = new VehicleDrivingNumberImpl();

	/***
	 * 查询超速报警统计概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getOverspeedAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = null;
		reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOverSpeed);
		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());
		return result;
	}

	/***
	 * 查询超速报警统计详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getOverspeedAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOverSpeed, startDate, endDate, DAOverspeedAlarm.class);
		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				OverspeedAlarm item = new OverspeedAlarm();
				DAOverspeedAlarm value = (DAOverspeedAlarm) alarmData.get(i);
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setLimitSpeed(value.getLimitSpeed());
				item.setMaxSpeed(value.getMaxSpeed());
				item.setMinSpeed(value.getMinSpeed());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<OverspeedAlarm>(16);
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
		}

		optResult.setStatus(PlatformResponseResult.success_VALUE);

		return optResult;
	}

	/***
	 * 查询进出区域报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getAreaINOUTAlarmSummary(List<Long> terminalIds, long startDate, long endDate, int type,
													  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = null;
		reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAreaINOUT, type);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询进出区域报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getAreaINOUTAlarmRecords(List<Long> terminalIds, long startDate, long endDate, int type,
											  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAreaINOUT, startDate, endDate,
				DAAreaINOUTAlarm.class, type);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAAreaINOUTAlarm value = (DAAreaINOUTAlarm) alarmData.get(i);
				AreaINOUTAlarm item = new AreaINOUTAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setAreaId(value.getAreaId());
				item.setType(value.getType());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<AreaINOUTAlarm>(16);
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
		}

		optResult.setStatus(PlatformResponseResult.success_VALUE);

		return optResult;

	}

	/***
	 * 查询区域内超速报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getAreaOverspeedAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
														  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAreaOverspeed);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询区域内超速报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getAreaOverspeedAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAreaOverspeed, startDate, endDate,
				DAAreaOverspeedAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAAreaOverspeedAlarm value = (DAAreaOverspeedAlarm) alarmData.get(i);
				AreaOverspeedAlarm item = new AreaOverspeedAlarm();
				item.setAreaId(value.getAreaId());
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setLimitSpeed(value.getLimitSpeed());
				item.setMaxSpeed(value.getMaxSpeed());
				item.setMinSpeed(value.getMinSpeed());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<AreaOverspeedAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询进出区域报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getRouteINOUTAlarmSummary(List<Long> terminalIds, long startDate, long endDate, int type,
													   CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRouteINOUT, type);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询进出区域报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getRouteINOUTAlarmRecords(List<Long> terminalIds, long startDate, long endDate, int type,
											   CommonParameter comParameter, String queryKey) {
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRouteINOUT, startDate, endDate,
				DARouteINOUTAlarm.class, type);
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DARouteINOUTAlarm value = (DARouteINOUTAlarm) alarmData.get(i);
				RouteINOUTAlarm item = new RouteINOUTAlarm();
				item.setAreaId(value.getAreaId());
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTriggerContinuousTime(value.getTriggerContinuousTime());
				item.setTriggerDate(value.getTriggerDate());
				item.setTriggerLat(value.getTriggerLat());
				item.setTriggerLng(value.getTriggerLng());
				item.setType(value.getType());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<RouteINOUTAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}

		return optResult;

	}

	/***
	 * 查询线路超速报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getRLineOverspeedAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
														   CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRLineOverspeed);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询线路超速报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getRLineOverspeedAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												   CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRLineOverspeed, startDate, endDate,
				DARLineOverspeedAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DARLineOverspeedAlarm value = (DARLineOverspeedAlarm) alarmData.get(i);
				RLineOverspeedAlarm item = new RLineOverspeedAlarm();
				item.setAreaId(value.getAreaId());
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setLimitSpeed(value.getLimitSpeed());
				item.setMaxSpeed(value.getMaxSpeed());
				item.setSegmentId(value.getSegmentId());
				item.setMinSpeed(value.getMinSpeed());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<RLineOverspeedAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询路段超时报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getRLineOvertimeAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
														  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRLineOvertime);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询路段超时报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getRLineOvertimeAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRLineOvertime, startDate, endDate,
				DARLineOvertimeAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DARLineOvertimeAlarm value = (DARLineOvertimeAlarm) alarmData.get(i);
				RLineOvertimeAlarm item = new RLineOvertimeAlarm();
				item.setAreaId(value.getAreaId());
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setMaxLimitTime(value.getMaxLimitTime());
				item.setMinLimitTime(value.getMinLimitTime());
				item.setSegmentId(value.getSegmentId());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<RLineOvertimeAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询超时停车报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getOvertimeParkingAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
															CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimeParking);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询超时停车报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getOvertimeParkingAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
													CommonParameter comParameter, String queryKey) {
//		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
//		OptResult optResult = new OptResult();
//		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
//				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimeParking, startDate, endDate,
//				DAOvertimeParkingAlarm.class);
//		if (alarmData != null && alarmData.size() > 0) {
//			Map<String, Object> retMap = new HashMap<String, Object>();
//			for (int i = 0; i < alarmData.size(); i++) {
//				DAOvertimeParkingAlarm value = (DAOvertimeParkingAlarm) alarmData.get(i);
//				OvertimeParkingAlarm item = new OvertimeParkingAlarm();
//				item.setBeginDate(value.getBeginDate());
//				item.setBeginLat(value.getBeginLat());
//				item.setBeginLng(value.getBeginLng());
//				item.setContinuousTime(value.getContinuousTime());
//				item.setEndDate(value.getEndDate());
//				item.setEndLat(value.getEndLat());
//				item.setEndLng(value.getEndLng());
//				item.setTerminalId(value.getTerminalId());
//				item.setLimitParking(value.getLimitParking());
//				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
//				if (reList == null) {
//					reList = new ArrayList<OvertimeParkingAlarm>();
//					retMap.put(String.valueOf(value.getTerminalId()), reList);
//				}
//				reList.add(item);
//			}
//			optResult.setMap(retMap);
//			optResult.setStatus(PlatformResponseResult.success_VALUE);
//		}
//
//		return optResult;
		return null;

	}

	/***
	 * 查询疲劳驾驶报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getFatigueAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmFatigue);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询疲劳驾驶报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getFatigueAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmFatigue, startDate, endDate, DAFatigueAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAFatigueAlarm value = (DAFatigueAlarm) alarmData.get(i);
				FatigueAlarm item = new FatigueAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				item.setLimitDayDriving(value.getLimitDayDriving());
				item.setLimitDriving(value.getLimitDriving());
				item.setLimitRest(value.getLimitRest());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<FatigueAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}

		return optResult;
	}

	/***
	 * 查询紧急报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getEmergencyAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmEmergency);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询紧急报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getEmergencyAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmEmergency, startDate, endDate, DAEmergencyAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAEmergencyAlarm value = (DAEmergencyAlarm) alarmData.get(i);
				EmergencyAlarm item = new EmergencyAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<EmergencyAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}

		return optResult;

	}

	/***
	 * 查询电源欠压概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getPowerStayInLowerAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
															 CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmPowerStayInLower);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询电源欠压详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getPowerStayInLowerAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
													 CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmPowerStayInLower, startDate, endDate,
				DAPowerStayInLowerAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAPowerStayInLowerAlarm value = (DAPowerStayInLowerAlarm) alarmData.get(i);
				PowerStayInLowerAlarm item = new PowerStayInLowerAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<PowerStayInLowerAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;
	}

	/***
	 * 查询主电源断电报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getElectricPowerOffAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
															 CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmElectricPowerOff);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询主电源断电报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getElectricPowerOffAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
													 CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmElectricPowerOff, startDate, endDate,
				DAElectricPowerOffAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAElectricPowerOffAlarm value = (DAElectricPowerOffAlarm) alarmData.get(i);
				ElectricPowerOffAlarm item = new ElectricPowerOffAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<ElectricPowerOffAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询ACC状态报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public AlarmSummaryQuery getACCStatusAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		AlarmSummaryQuery result = new AlarmSummaryQuery();
		List<CommonSummaryEntity> reList = getSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmACCStatus);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询ACC状态报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getACCStatusAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmACCStatus, startDate, endDate, DAACCStatusAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAACCStatusAlarm value = (DAACCStatusAlarm) alarmData.get(i);
				TerACCStatus item = new TerACCStatus();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				item.setAccStatus(value.getAccStatus());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<TerACCStatus>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);

			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询里程统计概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public ResMileagesSummaryQuery getMileagesSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) {
		/*
		 * log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate); //
		 * 构造返回结果对象 ResMileagesSummaryQuery result = new
		 * ResMileagesSummaryQuery(); List<MileageSummaryEntity> reList = null;
		 * reList = getMileagesSummaryData(terminalIds, startDate, endDate,
		 * comParameter, queryKey,
		 * Constant.PropertiesKey.AlarmTypeTableMapping.AlarmMilages);
		 * 
		 * if (reList != null) { List<Mileages> list = new
		 * ArrayList<Mileages>(); for (MileageSummaryEntity item : reList) {
		 * Mileages milages = new Mileages();
		 * milages.setTerminalId(item.getTerminalId()); if (item.getBeginLat()
		 * == 0) { milages.setBeginDate(startDate); milages.setEndDate(endDate);
		 * } else { milages.setBeginDate(item.getBeginDate());
		 * milages.setBeginLat(item.getBeginLat());
		 * milages.setBeginLng(item.getBeginLng());
		 * milages.setBeginMileage(item.getBeginMileage());
		 * milages.setEndDate(item.getEndDate());
		 * milages.setEndLat(item.getEndLat());
		 * milages.setEndLNG(item.getEndLNG());
		 * milages.setEndMileage(item.getEndMileage());
		 * milages.setMileage(item.getMileage());
		 * milages.setTotalTime(item.getTotalTime());
		 * milages.setOil(item.getOil()); }
		 * 
		 * list.add(milages);
		 * 
		 * } result.setCommResultList(list); }
		 * 
		 * result.setStatusCode(1); result.setTotalRords(terminalIds.size());
		 * return result;
		 */
		return null;
	}

	/**
	 * 查询终端里程概要信息数据
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @param queryKey
	 * @param alarmmilages
	 * @return
	 */
	private List<MileageSummaryEntity> getMileagesSummaryData(List<Long> terminalIds, long startDate, long endDate,
															  CommonParameter comParameter, String queryKey, String alarmmilages) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		List<MileageSummaryEntity> temps = null;
		if (!comParameter.isMultipage()) {
			// 不分页
			DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
			try {
				// 查询全部数据
				temps = service.queryMileageSummaryData(terminalIds, alarmmilages, startDate, endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			// 根据鉴权码获取概要缓存对象
			MilagesSummaryCache instance = (MilagesSummaryCache) alarmQuqeryMgmt.getMilagesCacheByKey(queryKey);
			// 首次查询
			if (instance == null) {
				// 初始化查询缓存
				instance = new MilagesSummaryCache(terminalIds, queryKey);
				// 查询指定数据的缓存数据
				boolean initCache = instance.initCache(startDate, endDate, comParameter.getPageIndex(),
						comParameter.getPageSize(), alarmmilages);
				if (initCache) {
					// 缓存对象实例
					alarmQuqeryMgmt.put(queryKey, instance);
					temps = instance.getAlarmCacheData(queryKey, terminalIds, alarmmilages, startDate, endDate,
							comParameter.getPageIndex(), comParameter.getPageSize());
				}
			} else {
				temps = instance.getAlarmCacheData(queryKey, terminalIds, alarmmilages, startDate, endDate,
						comParameter.getPageIndex(), comParameter.getPageSize());
			}

		}
		if (temps != null && temps.size() > 0) {
			List<MileageSummaryEntity> ret = new ArrayList<MileageSummaryEntity>();
			for (MileageSummaryEntity tem : temps) {
				ret.add(tem);
			}
			return ret;
		}
		return null;

	}

	/***
	 * 查询里程统计详情，不分页
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getMileagesRecords(List<Long> terminalIds, long startDate, long endDate,
										CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		/*
		 * // 构造返回对象 OptResult optResult = new OptResult(); // 查询全部里程数据信息 List
		 * alarmData = getAlarmDataWithNoMultipage(terminalIds,
		 * Constant.PropertiesKey.AlarmTypeTableMapping.AlarmMilages, startDate,
		 * endDate, MilagesEntity.class); // 里程不为空 if (alarmData != null &&
		 * alarmData.size() > 0) { // Map<String,Object> key:终端标识
		 * value：里程信息按天统计的list Map<String, Object> retMap = new HashMap<String,
		 * Object>(); for (int i = 0; i < alarmData.size(); i++) { //
		 * 每个终端一天的里程信息,里程是每5分钟为统计时间段 MilagesEntity milagesEntity =
		 * (MilagesEntity) alarmData.get(i); // 汇总本次查询的一天的里程信息 DAMilages value =
		 * null; // 里程是每5分钟为统计时间段详情信息 List<DAMilages> dataList =
		 * milagesEntity.getDataList(); // 过滤有效统计查询数据 if (dataList != null) {
		 * for (int j = 0; j < dataList.size(); j++) { DAMilages milages =
		 * dataList.get(j); // 里程开始时间 long beginDate = milages.getBeginDate();
		 * // 开始时间>查询开始时间 if (beginDate < startDate) { continue; } else { //
		 * 初始化汇总本次查询的一天的里程信息 if (value == null) { value = new DAMilages();
		 * value.setTerminalId(milagesEntity.getTerminal_id());
		 * value.setBeginDate(milages.getBeginDate());
		 * value.setBeginLat(milages.getBeginLat());
		 * value.setBeginLng(milages.getBeginLng());
		 * value.setBeginMileage(milages.getBeginMileage());
		 * value.setEndDate(milages.getEndDate());
		 * value.setEndLat(milages.getEndLat());
		 * value.setEndLNG(milages.getEndLNG());
		 * value.setEndMileage(milages.getEndMileage());
		 * 
		 * }
		 * 
		 * } if (value != null) { // 开始时间>查询的结束时间 if (beginDate > endDate) {
		 * break; } else// 更新汇总本次查询的一天的里程信息的结束点信息 {
		 * value.setEndDate(milages.getEndDate());
		 * value.setEndLat(milages.getEndLat());
		 * value.setEndLNG(milages.getEndLNG());
		 * value.setEndMileage(milages.getEndMileage());
		 * 
		 * } // 里程累加 value.setMileage(value.getMileage() +
		 * milages.getMileage()); } } } // 构建返回的信息 if (value != null) { Mileages
		 * item = new Mileages(); item.setBeginDate(value.getBeginDate());
		 * item.setBeginLat(value.getBeginLat());
		 * item.setBeginLng(value.getBeginLng());
		 * item.setBeginMileage(value.getBeginMileage());
		 * item.setEndDate(value.getEndDate());
		 * item.setEndLat(value.getEndLat()); item.setEndLNG(value.getEndLNG());
		 * item.setEndMileage(value.getEndMileage());
		 * item.setMileage(value.getMileage());
		 * item.setTerminalId(value.getTerminalId());
		 * item.setOil(milagesEntity.getOil()); List reList = (List)
		 * retMap.get(String.valueOf(value.getTerminalId())); if (reList ==
		 * null) { reList = new ArrayList<Mileages>();
		 * retMap.put(String.valueOf(value.getTerminalId()), reList); }
		 * reList.add(item); }
		 * 
		 * } optResult.setMap(retMap);
		 * optResult.setStatus(PlatformResponseResult.success_VALUE); } return
		 * optResult;
		 */
		return null;
	}

	/**
	 * 查询终端所有统计数据概要信息
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @param queryKey
	 * @param AlarmType
	 * @return
	 */
	private List<AllAlarmData> getAllSummaryData(List<Long> terminalIds, long startDate, long endDate,
												 CommonParameter comParameter, String queryKey, String AlarmType) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		List<AllAlarmData> temps = null;
		if (!comParameter.isMultipage()) {
			DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
			try {
				temps = service.queryAllAlarmSummaryData(terminalIds, AlarmType, startDate, endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			/*
			 * //根据鉴权码获取概要缓存对象 修改jbp AllAlarmSummaryCache instance =
			 * (AllAlarmSummaryCache)
			 * alarmQuqeryMgmt.getAllAlarmCacheByKey(queryKey);
			 */
			AllAlarmSummaryCache instance = alarmQuqeryMgmt.getAllAlarmCacheByKey(queryKey);
			// 构造返回结果对象

			if (instance == null) {
				instance = new AllAlarmSummaryCache(terminalIds, queryKey);
				boolean initCache = instance.initCache(startDate, endDate, comParameter.getPageIndex(),
						comParameter.getPageSize(), AlarmType);
				if (initCache) {
					alarmQuqeryMgmt.putAllAlarm(queryKey, instance);
					temps = instance.getAlarmCacheData(queryKey, terminalIds, AlarmType, startDate, endDate,
							comParameter.getPageIndex(), comParameter.getPageSize());
				}
			} else {
				temps = instance.getAlarmCacheData(queryKey, terminalIds, AlarmType, startDate, endDate,
						comParameter.getPageIndex(), comParameter.getPageSize());
			}

		}
		if (temps != null && temps.size() > 0) {
			List<AllAlarmData> ret = new ArrayList<AllAlarmData>();
			for (AllAlarmData tem : temps) {
				ret.add(tem);
			}
			return ret;
		}

		return null;
	}

	/**
	 * 查询流量信息概要统计数据
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @param queryKey
	 * @param alarmType
	 * @return
	 */
	private List<WFSummary> getWFlowSummaryData(List<Long> terminalIds, long startDate, long endDate,
												CommonParameter comParameter, String queryKey, String alarmType) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		List<WFSummary> temps = null;
		if (!comParameter.isMultipage()) {
			DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
			temps = service.queryWFSummaryData(terminalIds, alarmType, startDate, endDate);

		} else {
			// 根据鉴权码获取概要缓存对象
			WFSummaryCache instance = (WFSummaryCache) alarmQuqeryMgmt.getWFCacheByKey(queryKey);

			// 构造返回结果对象

			if (instance == null) {
				instance = new WFSummaryCache(terminalIds, queryKey);
				boolean initCache = instance.initCache(startDate, endDate, comParameter.getPageIndex(),
						comParameter.getPageSize(), alarmType);
				if (initCache) {
					alarmQuqeryMgmt.putWFCache(queryKey, instance);
					temps = instance.getAlarmCacheData(queryKey, terminalIds, alarmType, startDate, endDate,
							comParameter.getPageIndex(), comParameter.getPageSize());
				}
			} else {
				temps = instance.getAlarmCacheData(queryKey, terminalIds, alarmType, startDate, endDate,
						comParameter.getPageIndex(), comParameter.getPageSize());
			}

		}
		if (temps != null && temps.size() > 0) {
			List<WFSummary> ret = new ArrayList<WFSummary>();
			for (WFSummary tem : temps) {
				if (tem.getBeginDate() == 0) {
					tem.setBeginDate(startDate);
				}
				if (tem.getEndDate() == 0) {
					tem.setEndDate(endDate);
				}
				ret.add(tem);
			}
			return ret;
		}

		return null;

	}

	/**
	 * 公用的查询统计数据概要数据方法
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @param queryKey
	 * @param AlarmType
	 * @return
	 */
	private List<CommonSummaryEntity> getSummaryData(List<Long> terminalIds, long startDate, long endDate,
													 CommonParameter comParameter, String queryKey, String AlarmType) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 返回数据引用
		List<DACommonSummaryEntity> temps = null;
		// 不分页查询，则查询全部的终端数据
		if (!comParameter.isMultipage()) {
			DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
			try {
				// 调用服务查询数据
				temps = service.queryAlarmSummaryData(terminalIds, AlarmType, startDate, endDate);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		} else {
			// 根据鉴权码获取概要缓存对象
			AlarmSummaryCacheEntity instance = (AlarmSummaryCacheEntity) alarmQuqeryMgmt.getAlarmCacheByKey(queryKey);
			// 缓存对象为空，构建缓存
			if (instance == null) {

				instance = new AlarmSummaryCacheEntity(terminalIds, queryKey);
				// 初始化缓存数据,进行查询
				boolean initCache = instance.initCache(startDate, endDate, comParameter.getPageIndex(),
						comParameter.getPageSize(), AlarmType);
				// 初始化成功,将实例放入缓存
				if (initCache) {
					alarmQuqeryMgmt.put(queryKey, instance);
					temps = instance.getAlarmCacheData(queryKey, terminalIds, AlarmType, startDate, endDate,
							comParameter.getPageIndex(), comParameter.getPageSize());
				}
			} else {
				temps = instance.getAlarmCacheData(queryKey, terminalIds, AlarmType, startDate, endDate,
						comParameter.getPageIndex(), comParameter.getPageSize());
			}

		}
		// 构建返回数据
		if (temps != null && temps.size() > 0) {
			ArrayList<CommonSummaryEntity> reList = new ArrayList<>();
			for (DACommonSummaryEntity value : temps) {
				CommonSummaryEntity item = new CommonSummaryEntity();
				// 报警持续时间总和
				item.setCalculatedAT(value.getCalculatedAT());
				// 结束报警时间 UTC(s)
				item.setEndTime(value.getEndTime());
				// 记录总条数
				item.setRecordsTotal(value.getRecordsTotal());
				// 开始时间 UTC(s)
				item.setStartTime(value.getStartTime());
				// 终端ID
				item.setTerminalID(value.getTerminalID());
				reList.add(item);
			}
			return reList;
		}
		return null;
	}

	/**
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @param queryKey
	 * @param AlarmType
	 * @param type
	 * @return
	 */
	private List<CommonSummaryEntity> getSummaryData(List<Long> terminalIds, long startDate, long endDate,
													 CommonParameter comParameter, String queryKey, String AlarmType, int type) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 返回数据引用
		List<DACommonSummaryEntity> temps = null;
		// 不分页查询，则查询全部的终端数据
		if (!comParameter.isMultipage()) {
			DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
			try {
				// 调用服务查询数据
				temps = service.queryAlarmSummaryData(terminalIds, AlarmType, startDate, endDate, type);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		} else {
			// 根据鉴权码获取概要缓存对象
			AlarmSummaryCacheEntity instance = (AlarmSummaryCacheEntity) alarmQuqeryMgmt.getAlarmCacheByKey(queryKey);

			// 缓冲为空，初始化缓存
			if (instance == null) {
				instance = new AlarmSummaryCacheEntity(terminalIds, queryKey);
				boolean initCache = instance.initCache(startDate, endDate, comParameter.getPageIndex(),
						comParameter.getPageSize(), AlarmType, type);
				if (initCache) {
					alarmQuqeryMgmt.put(queryKey, instance);
					temps = instance.getAlarmCacheData(queryKey, terminalIds, AlarmType, startDate, endDate,
							comParameter.getPageIndex(), comParameter.getPageSize(), type);
				}
			} else {
				temps = instance.getAlarmCacheData(queryKey, terminalIds, AlarmType, startDate, endDate,
						comParameter.getPageIndex(), comParameter.getPageSize());
			}

		}
		if (temps != null && temps.size() > 0) {
			ArrayList<CommonSummaryEntity> reList = new ArrayList<>();
			for (DACommonSummaryEntity value : temps) {
				CommonSummaryEntity item = new CommonSummaryEntity();
				// 报警持续时间总和
				item.setCalculatedAT(value.getCalculatedAT());
				// 结束报警时间 UTC(s)
				item.setEndTime(value.getEndTime());
				// 记录总条数
				item.setRecordsTotal(value.getRecordsTotal());
				// 开始时间 UTC(s)
				item.setStartTime(value.getStartTime());
				// 终端ID
				item.setTerminalID(value.getTerminalID());
				reList.add(item);
			}
			return reList;
		}

		return null;

	}

	/***
	 * 查询驾驶员登录详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getDriverLoginRecords(List<Long> terminalIds, String driverIDCode, long startDate, long endDate,
										   int type, CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmDriverLogin, startDate, endDate, DADriverLogin.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DADriverLogin value = (DADriverLogin) alarmData.get(i);
				DriverLogin item = new DriverLogin();
				item.setAgencyName(value.getAgencyName());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setCertificateValid(value.getCertificateValid());
				item.setCreditDate(value.getCreditDate());
				item.setDriverCertificate(value.getDriverCertificate());
				item.setDriverIdCode(value.getDriverIdCode());
				item.setDriverName(value.getDriverName());
				item.setDriverTime(value.getDriverTime());
				item.setFailedReason(value.getFailedReason());
				item.setEndLat(value.getEndLat());
				item.setEndLNG(value.getEndLNG());
				item.setStubbsCard(value.getStubbsCard());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<DriverLogin>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询终端在线状态详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getTerminalOnOffLineStatusRecords(List<Long> terminalIds, long startDate, long endDate, int type,
													   CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmTerminalOnOffLineStatus, startDate, endDate,
				DATerminalOnOffLineStatus.class, type);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DATerminalOnOffLineStatus value = (DATerminalOnOffLineStatus) alarmData.get(i);
				TerminalOnOffLineStatus item = new TerminalOnOffLineStatus();
				item.setBeginDate(value.getBeginDate());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setOnlineStatus(value.getOnlineStatus());
				item.setTerminalId(value.getTerminalId());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<TerminalOnOffLineStatus>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;

	}

	/***
	 * 查询流量统计概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public ResWFlowSummaryQuery getWFlowSummary(List<Long> terminalIds, long startDate, long endDate,
												CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		ResWFlowSummaryQuery result = new ResWFlowSummaryQuery();
		List<WFSummary> reList = null;
		reList = getWFlowSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmWFlow);

		result.setCommResultList(reList);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/***
	 * 查询流量统计详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	@Override
	public OptResult getWFlowRecords(List<Long> terminalIds, long startDate, long endDate,
									 CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 返回信息
		OptResult optResult = new OptResult();
		// 查询全部终端的流量信息
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmWFlow, startDate, endDate, WFlowEntity.class);
		// Map<String,Object> key:终端标识 value：流量信息按天统计的list
		Map<String, Object> retMap = new HashMap<String, Object>();
		// 数据不为空
		if (alarmData != null && alarmData.size() > 0) {
			for (int i = 0; i < alarmData.size(); i++) {

				DAWFlow value = null;
				// 每个终端一天的流量统计数据,以一个小时为统计时间段
				WFlowEntity wFlowEntity = (WFlowEntity) alarmData.get(i);
				// 统计日期
				long day = wFlowEntity.getDay();
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(day * 1000);
				// 以一个小时为统计时间段的详细的数据
				List<DAWFlow> dataList = wFlowEntity.getDataList();
				// 一天的统计查询数据
				WFlow item = new WFlow();
				if (dataList != null) {
					// 过滤查询的流量信息
					for (int j = 0; j < dataList.size(); j++) {
						// 一小时的流量数据
						DAWFlow wFlow = dataList.get(j);
						int flowDate = wFlow.getFlowDate();
						calendar.set(Calendar.HOUR_OF_DAY, flowDate);
						long currentFlowTime = calendar.getTimeInMillis() / 1000;
						// 流量统计时间小于查询的开始时间或者大于结束时间，数据无效
						if (currentFlowTime < startDate || currentFlowTime > endDate) {
							continue;
						} else {
							if (null == value) {
								value = new DAWFlow();
								// 终端标识
								value.setTerminalId(wFlowEntity.getTerminal_id());
								// 开始日期
								item.setBeginDate(currentFlowTime);
								// 结束日期
								item.setEndDate(currentFlowTime);
							}
							// 流量累加
							value.setDownFlow(value.getDownFlow() + wFlow.getDownFlow());
							value.setUpFlow(value.getUpFlow() + wFlow.getUpFlow());
							item.setEndDate(currentFlowTime);
						}
					}
				}

				item.setDownFlow(value.getDownFlow());
				item.setTerminalId(wFlowEntity.getTerminal_id());
				if (item.getBeginDate() == 0) {
					item.setBeginDate(startDate);
				}
				if (item.getEndDate() == 0) {
					item.setEndDate(endDate);
				}

				item.setUpFlow(value.getUpFlow());
				List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<WFlow>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				if (item.getDownFlow() > 0 || item.getDownFlow() > 0) {
					reList.add(item);
				}

			}
		}
		// 判断结束日期是否为当天
		int endTimeDay = DateUtils.differDay(DateUtils.current(), DateUtils.calendar(endDate));
		// 为当前日期,当前日期的数据需要从缓存中查询
		if (endTimeDay == 0) {
			WFlowServiceImp wFlowServiceImp = new WFlowServiceImp();
			for (long terminalId : terminalIds) {
				// 查询缓存流量数据
				Object obj = wFlowServiceImp.findTerminalData(terminalId);
				// 不为空，统计当天的流量数据
				if (null != obj) {
					// 每个终端一天的流量统计数据,以一个小时为统计时间段
					DAWFlow value = null;
					WFlow item = new WFlow();
					FlowCacheEntity flowData = (FlowCacheEntity) obj;
					String formatTime = DateUtils.format(endDate, DateFormat.YYYYMMDD);
					if (formatTime.equals(flowData.getDate())) {
						List<DAWFlow> dataList = flowData.getDataList();
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(endDate * 1000);
						for (int j = 0; j < dataList.size(); j++) {

							DAWFlow wFlow = dataList.get(j);
							int flowDate = wFlow.getFlowDate();

							calendar.set(Calendar.HOUR_OF_DAY, flowDate);
							long currentFlowTime = calendar.getTimeInMillis() / 1000;
							// 流量统计时间小于查询的开始时间或者大于结束时间，数据无效
							if (currentFlowTime < startDate || currentFlowTime > endDate) {
								continue;
							} else {
								if (null == value) {
									value = new DAWFlow();
									item.setBeginDate(currentFlowTime);
									item.setEndDate(currentFlowTime);
									value.setTerminalId(terminalId);
								}
								value.setDownFlow(value.getDownFlow() + wFlow.getDownFlow());
								value.setUpFlow(value.getUpFlow() + wFlow.getUpFlow());
								item.setEndDate(currentFlowTime);
							}

						}

						item.setDownFlow(value.getDownFlow());
						item.setTerminalId(terminalId);
						if (item.getBeginDate() == 0) {
							item.setBeginDate(startDate);
						}
						if (item.getEndDate() == 0) {
							item.setEndDate(endDate);
						}
						item.setUpFlow(value.getUpFlow());
						List reList = (List) retMap.get(String.valueOf(value.getTerminalId()));
						if (reList == null) {
							reList = new ArrayList<WFlow>();
							retMap.put(String.valueOf(value.getTerminalId()), reList);
						}
						if (item.getDownFlow() > 0 || item.getDownFlow() > 0) {
							reList.add(item);
						}

					}

				}

			}

		}
		optResult.setMap(retMap);
		optResult.setStatus(PlatformResponseResult.success_VALUE);
		return optResult;
	}

	/**
	 * 不分页查询统计数据
	 *
	 * @param tids
	 * @param type
	 * @param st
	 * @param et
	 * @param clazz
	 * @return
	 */
	public List<? extends BaseAlarmEntity> getAlarmDataWithNoMultipage(List<Long> tids, String type, long st, long et,
																	   Class<? extends BaseAlarmEntity> clazz) {
		log.info("from DSA->DA 报警统计  st:" + st + " et:" + et);
		DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
		List<DBObject> queryAlarmData = null;
		try {
			queryAlarmData = service.queryAlarmData(tids, type, st, et);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (queryAlarmData != null) {
			List returnList = new ArrayList();
			for (DBObject opda : queryAlarmData) {
				// 反射加载
				BaseAlarmEntity item = null;
				try {
					item = (BaseAlarmEntity) clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				item.dbObjectToBean(opda);
				returnList.add(item);
			}
			return returnList;
		}

		return null;

	}

	/**
	 * 不分页查询统计数据
	 *
	 * @param tids
	 * @param type
	 * @param st
	 * @param et
	 * @param clazz
	 * @param queryType
	 * @return
	 */
	public List<? extends BaseAlarmEntity> getAlarmDataWithNoMultipage(List<Long> tids, String type, long st, long et,
																	   Class<? extends BaseAlarmEntity> clazz, int queryType) {
		log.info("DSA->DA 报警统计  st:" + st + " et:" + et);
		DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
		List<DBObject> queryAlarmData = null;
		try {
			queryAlarmData = service.queryAlarmData(tids, type, st, et, queryType);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if (queryAlarmData != null) {
			List returnList = new ArrayList();
			for (DBObject opda : queryAlarmData) {
				// 反射加载
				BaseAlarmEntity item = null;
				try {
					item = (BaseAlarmEntity) clazz.newInstance();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				item.dbObjectToBean(opda);
				returnList.add(item);
			}
			return returnList;
		}

		return null;
	}

	/**
	 * 服务可用性检测
	 */
	@Override
	public boolean isConnected() {
		return true;
	}

	/**
	 * 查询汇总报警概要
	 */
	@Override
	public ResAllAlarmSummaryQuery getAllAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) {
		log.info("DSA->DA 报警统计  st:" + startDate + " et:" + endDate);
		// 构造返回结果对象
		ResAllAlarmSummaryQuery result = new ResAllAlarmSummaryQuery();
		List<AllAlarmData> temps = null;
		temps = getAllSummaryData(terminalIds, startDate, endDate, comParameter, queryKey,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAll);

		result.setCommResultList(temps);
		result.setStatusCode(1);
		result.setTotalRords(terminalIds.size());

		return result;
	}

	/**
	 * 查询终端油耗。起止时间为某一天的起止时间00：00：00---23：59：59
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Override
	public Map<Long, Integer> getOilConsumptionData(List<Long> terminalIds, long startDate, long endDate) {
		DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
		Map<Long, Integer> oilConsumptionData = service.getOilConsumptionData(terminalIds, startDate, endDate);
		return oilConsumptionData;
	}

	@Override
	public OptResult getOvertimeParkAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException {
		log.info("DSA->DA 滞留超时报警统计  st:" + startDate + " et:" + endDate + ",tid+" + terminalIds);
		OptResult optResult = new OptResult();
		List alarmData = getAlarmDataWithNoMultipage(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark, startDate, endDate,
				DAOvertimeParkAlarm.class);

		if (alarmData != null && alarmData.size() > 0) {
			Map<String, Object> retMap = new HashMap<String, Object>();
			for (int i = 0; i < alarmData.size(); i++) {
				DAOvertimeParkAlarm value = (DAOvertimeParkAlarm) alarmData.get(i);
				OvertimeParkAlarm item = new OvertimeParkAlarm();
				item.set_id(value.get_id());
				item.setTerminalId(value.getTerminalId());
				item.setAreaId(value.getAreaId());
				item.setContinuousTime(value.getContinuousTime());
				item.setLimitParking(value.getLimitParking());
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				List<OvertimeParkAlarm> reList = (List<OvertimeParkAlarm>) retMap.get(String.valueOf(value
						.getTerminalId()));
				if (reList == null) {
					reList = new ArrayList<OvertimeParkAlarm>();
					retMap.put(String.valueOf(value.getTerminalId()), reList);
				}
				reList.add(item);
			}
			log.info("滞留超时查询结果size：" + retMap.size());
			optResult.setMap(retMap);
			optResult.setStatus(PlatformResponseResult.success_VALUE);
		}
		return optResult;
	}

	@Override
	public ResVehiclePassTimesRecordsQuery queryVehiclePassTimesRecords(int districtCode, long startDate, long endDate)
			{
		ResVehiclePassTimesRecordsQuery records = vehicleDrivingNumber.queryVehiclePassTimesRecords(districtCode,
				startDate, endDate);
		return records;
	}

	@Override
	public ResVehiclePassInAreaRecordsQuery queryVehiclePassInAreaRecords(List<Integer> districtCodes, int type,
																		  long startDate, long endDate) {
		ResVehiclePassInAreaRecordsQuery records = vehicleDrivingNumber.queryVehiclePassInAreaRecords(districtCodes,
				type, startDate, endDate);
		return records;
	}

	@Override
	public ResVehiclePassTimesRecordsQuery getVehiclePassTimesBytileId(List<Long> tileIds, long startDate, long endDate)
			{
		ResVehiclePassTimesRecordsQuery records = vehicleDrivingNumber.getVehiclePassTimesBytileId(tileIds, startDate,
				endDate);
		return records;
	}

	@Override
	public ResFaultCodeAlarmRecoreds getFaultCodeRecords(List<Long> terminalIds, int spn, int fmi, long startDate,
														 long endDate, CommonParameter commonParameter) {
		ResFaultCodeAlarmRecoreds recoreds = new ResFaultCodeAlarmRecoreds();
		ReportQueryService service = new ReportQueryServiceImpl();
		List<DAFaultCodeAlarm> alarms = service.queryFaultCodeAlarm(terminalIds, spn, fmi, startDate, endDate,
				commonParameter);
		// da to rmi
		if (null != alarms && alarms.size() > 0) {
			for (DAFaultCodeAlarm alarm : alarms) {
				FaultCodeEntity fault = new FaultCodeEntity();
				fault.setTerminalId(alarm.getTerminalId());
				fault.setSpn(alarm.getSpn());
				fault.setFmi(alarm.getFmi());
				fault.setBeginDate(alarm.getBeginDate());
				fault.setContinueTime(alarm.getContinuousTime());
				fault.setBeginLat(alarm.getBeginLat());
				fault.setBeginLng(alarm.getBeginLng());
				fault.setEndLat(alarm.getEndLat());
				fault.setEndLng(alarm.getEndLng());
				recoreds.addFaultCodeAlarmItem(fault);
			}
		}
		recoreds.setStatusCode(1);
		return recoreds;
	}

	public static final String SEPERATOR = "#";

	@Override
	public Map<String, FaultCodeEntity> getLastFaultCode(List<Long> terminalIds, long startDate, long endDate)
			{
		Map<String, FaultCodeEntity> result = new HashMap<String, FaultCodeEntity>();
		ReportQueryService service = new ReportQueryServiceImpl();
		List<DAFaultCodeAlarm> alarms = service.queryFaultCodeAlarm(terminalIds, 0, 0, startDate, endDate, null);
		// da to rmi
		if (null != alarms && alarms.size() > 0) {
			for (DAFaultCodeAlarm alarm : alarms) {
				if(result.containsKey(alarm.getTerminalId())){
					FaultCodeEntity entity = result.get(alarm.getTerminalId());
					if(entity.getEndDate() > alarm.getEndDate()){
						continue;
					}
				}
				FaultCodeEntity fault = new FaultCodeEntity();
				fault.setTerminalId(alarm.getTerminalId());
				fault.setSpn(alarm.getSpn());
				fault.setFmi(alarm.getFmi());
				fault.setBeginDate(alarm.getBeginDate());
				fault.setEndDate(alarm.getEndDate());
				fault.setContinueTime(alarm.getContinuousTime());
				fault.setBeginLat(alarm.getBeginLat());
				fault.setBeginLng(alarm.getBeginLng());
				fault.setEndLat(alarm.getEndLat());
				fault.setEndLng(alarm.getEndLng());
				fault.set_id(alarm.get_id());
				result.put(alarm.getTerminalId() + SEPERATOR + alarm.getFmi() + SEPERATOR + alarm.getSpn(), fault);
			}
			log.error(">>>dsa初始化加载故障码 条数："+alarms.size());
		}

		return result;
	}

	@Override
	public Map<Long, OvertimeParkAlarm> getOvertimeParkAlarmNoEndMerge(List<Long> terminalIds, int day)
	{
		Date date = new Date((long)day*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		dateString = String.valueOf(dateString).substring(2, 6);
		Map<Long, OvertimeParkAlarm> result = new HashMap<Long, OvertimeParkAlarm>();
		DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
		List<DBObject> alarmData = service.findOverTimeParkNoMergeData(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark + "_" + dateString);
		List<DAOvertimeParkAlarm> data = new ArrayList<DAOvertimeParkAlarm>();
		if (alarmData != null && alarmData.size() > 0) {
			for(DBObject object : alarmData){
				DAOvertimeParkAlarm alarm = new DAOvertimeParkAlarm();
				alarm.dbObjectToBean(object);
				data.add(alarm);
			}
		}
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				DAOvertimeParkAlarm value = (DAOvertimeParkAlarm) data.get(i);
				OvertimeParkAlarm item = new OvertimeParkAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				item.setLimitParking(value.getLimitParking());
				item.set_id(value.get_id());
				result.put(item.getTerminalId(), item);
			}
			log.error(">>>dsa初始化加载[滞留数据缓存]："+alarmData.size());
		}
		return result;
	}

	@Override
	public Map<Long, StaytimeParkAlarm> getStaytimeParkAlarmNoEndMerge(List<Long> terminalIds, int day)
	{
		Date date = new Date((long)day*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		dateString = String.valueOf(dateString).substring(2, 6);
		Map<Long, StaytimeParkAlarm> result = new HashMap<Long, StaytimeParkAlarm>();
		DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
		List<DBObject> alarmData = service.findOverTimeParkNoMergeData(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark + "_" + dateString);
		List<DAStaytimeParkAlarm> data = new ArrayList<DAStaytimeParkAlarm>();
		if (alarmData != null && alarmData.size() > 0) {
			for(DBObject object : alarmData){
				DAStaytimeParkAlarm alarm = new DAStaytimeParkAlarm();
				alarm.dbObjectToBean(object);
				data.add(alarm);
			}
		}
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				DAStaytimeParkAlarm value = (DAStaytimeParkAlarm) data.get(i);
				StaytimeParkAlarm item = new StaytimeParkAlarm();
				item.setBeginDate(value.getBeginDate());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setContinuousTime(value.getContinuousTime());
				item.setEndDate(value.getEndDate());
				item.setEndLat(value.getEndLat());
				item.setEndLng(value.getEndLng());
				item.setTerminalId(value.getTerminalId());
				item.set_id(value.get_id());
				result.put(item.getTerminalId(), item);
			}
			log.error(">>>dsa初始化加载[区域停留数据缓存]："+alarmData.size());
		}
		return result;
	}

	public Map<Long, Mileages> getAllMileageAndOilDataFromRedis() {
		Map<Long, Mileages> result = new HashMap<Long, Mileages>();
		Map<Long, MileageConsumption> map = TempGpsData.getAllMileageAndOilDataFromRedis();
		for (Long key : map.keySet()) {
			MileageConsumption mileage = map.get(key);
			Mileages mileages = new Mileages();
			mileages.setTerminalID(mileage.getTerminalID());
			mileages.setBeginLat(mileage.getBeginLat());
			mileages.setBeginLng(mileage.getBeginLng());
			mileages.setBeginMileage(mileage.getBeginMileage());
			mileages.setElectricConsumption(mileage.getElectricConsumption());
			mileages.setEndDate(mileage.getEndDate());
			mileages.setEndLat(mileage.getEndLat());
			mileages.setEndLng(mileage.getEndLng());
			mileages.setEndMileage(mileage.getEndMileage());
			mileages.setCanMileage(mileage.getMileage());
			mileages.setOilConsumption(mileage.getOilConsumption());
			mileages.setStartDate(mileage.getStartDate());
			mileages.setStaticDate(mileage.getStaticDate());
			mileages.setGpsMileage(mileage.getTerminalMileage());
			mileages.setMeterMileage(mileage.getMeterMileage());
			mileages.setFuelOil(mileage.getFuelOil());
			mileages.setOilValue(mileage.getOilValue());
			mileages.setBeginGpsMileage(mileage.getBeginGpsMileage());
			mileages.setEndGpsMileage(mileage.getEndGpsMileage());
			mileages.setBeginMeMileage(mileage.getBeginMeMileage());
			mileages.setEndMeMileage(mileage.getEndMeMileage());
			result.put(key, mileages);
		}
		if(map.size()>0){
			log.error(">>>dsa初始化加载[里程缓存]条数："+map.size());
		}
		return result;
	}
	public Map<Long, StaytimeParkAlarm> getAllStaytimeParkCacheFromRedis() {
		Map<Long, StaytimeParkAlarm> result = TempGpsData.getAllStaytimeParkCacheFromRedis();
		if(result.size()>0){
			log.error(">>>dsa初始化加载[区域停留缓存]条数："+result.size());
		}
		return result;
	}

	@Override
	public Map<Long, LCMileageAndOilDataRes.MileageAndOilData> mileagesRecoredsQuery(List<Long> terminalId) {
		Map<Long, LCMileageAndOilDataRes.MileageAndOilData> mapRes = new HashMap<>();
		Map<Long, MileageConsumption> map = TempGpsData.findAllMileageAndOilDataFromRedis(terminalId);
		if (map.size()>0){
			for (Map.Entry<Long, MileageConsumption> entry : map.entrySet()){
				long tid = entry.getKey();
				MileageConsumption con = entry.getValue();
				LCMileageAndOilDataRes.MileageAndOilData.Builder res = LCMileageAndOilDataRes.MileageAndOilData.newBuilder();
				if (entry.getValue()!=null){
					LcTerMilOilTypeDBEntity entity = TerminalMilOilTypeCache.getInstance().getValue(tid);
					if (entity!=null){
						if (entity.getMileage_type() == 1){
							res.setMileage(con.getMeterMileage());
						}else if (entity.getMileage_type() == 2){
							res.setMileage(con.getMileage());
						}else if (entity.getMileage_type() == 3 || entity.getMileage_type() == 4){
							res.setMileage(con.getTerminalMileage());
						}
						if (entity.getOil_type() == 1){
							res.setOil(con.getOilConsumption());
						}else if (entity.getOil_type() == 2){
							res.setOil(con.getFuelOil());
						}
					}
					res.setTerminalId(con.getTerminalID());
					res.setOilValue(con.getOilValue());
					res.setCurrentDate(con.getEndDate());
				}
				mapRes.put(tid,res.build());
			}
		}
		return mapRes;
	}

	@Override
	public Map<Long, StagnationTimeoutEntity> getStagnationTimeoutAlarmNoEndMerge(
			List<Long> terminalIds, long day) {
		Date date = new Date(day*1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(date);
		dateString = String.valueOf(dateString).substring(2, 6);
		Map<Long, StagnationTimeoutEntity> result = new HashMap<Long, StagnationTimeoutEntity>();
		DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
		List<DBObject> alarmData = service.findStagnationTimeoutNoMergeData(terminalIds,
				Constant.PropertiesKey.AlarmTypeTableMapping.StagnationTimeout + "_" + dateString);
		List<DAStagnationTimeoutAlarm> data = new ArrayList<DAStagnationTimeoutAlarm>();
		if (alarmData != null && alarmData.size() > 0) {
			for(DBObject object : alarmData){
				DAStagnationTimeoutAlarm alarm = new DAStagnationTimeoutAlarm();
				alarm.dbObjectToBean(object);
				data.add(alarm);
			}
		}
		if (data != null && data.size() > 0) {
			for (int i = 0; i < data.size(); i++) {
				DAStagnationTimeoutAlarm value = (DAStagnationTimeoutAlarm) data.get(i);
				StagnationTimeoutEntity item = new StagnationTimeoutEntity();
				item.set_id(value.get_id());
				item.setTerminalId(value.getTerminalId());
				item.setBeginDate(value.getBeginDate());
				item.setEndDate(value.getEndDate());
				item.setContinuousTime(value.getContinuousTime());
				item.setLimitParking(value.getLimitParking());
				item.setBeginLat(value.getBeginLat());
				item.setBeginLng(value.getBeginLng());
				item.setStatus(value.isStatus() == true ? 1 : 0);
				item.setTailMerge(value.getTailMerge());
				result.put(item.getTerminalId(), item);
			}
			log.error(">>>dsa初始化加载[停滞超时数据缓存]："+alarmData.size());
		}
		return result;
	}
}
