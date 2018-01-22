package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.OptResult;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption.MileageConsumption;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface AlarmStatisticsQueryService   {

	/**
	 * rmi服务可用性检测
	 *
	 * @return
	 * @throws RemoteException
	 */
	public boolean isConnected()  ;

	/***
	 * 查询超速报警统计概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getOverspeedAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询超速报警统计详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getOverspeedAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询进出区域报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getAreaINOUTAlarmSummary(List<Long> terminalIds, long startDate, long endDate, int type,
													  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询进出区域报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getAreaINOUTAlarmRecords(List<Long> terminalIds, long startDate, long endDate, int type,
											  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询区域内超速报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getAreaOverspeedAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
														  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询区域内超速报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getAreaOverspeedAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询进出区域报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getRouteINOUTAlarmSummary(List<Long> terminalIds, long startDate, long endDate, int type,
													   CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询进出区域报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getRouteINOUTAlarmRecords(List<Long> terminalIds, long startDate, long endDate, int type,
											   CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询线路超速报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getRLineOverspeedAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
														   CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询线路超速报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getRLineOverspeedAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												   CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询路段超时报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getRLineOvertimeAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
														  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询路段超时报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getRLineOvertimeAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询超时停车报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getOvertimeParkingAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
															CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询超时停车报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getOvertimeParkingAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
													CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询疲劳驾驶报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getFatigueAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询疲劳驾驶报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getFatigueAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询紧急报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getEmergencyAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询紧急报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getEmergencyAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询电源欠压概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getPowerStayInLowerAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
															 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询电源欠压详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getPowerStayInLowerAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
													 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询主电源断电报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getElectricPowerOffAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
															 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询主电源断电报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getElectricPowerOffAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
													 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询ACC状态报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public AlarmSummaryQuery getACCStatusAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询ACC状态报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getACCStatusAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
											  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询里程统计概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ResMileagesSummaryQuery getMileagesSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询里程统计详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getMileagesRecords(List<Long> terminalIds, long startDate, long endDate,
										CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询驾驶员登录详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getDriverLoginRecords(List<Long> terminalIds, String driverIDCode, long startDate, long endDate,
										   int type, CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询终端在线状态详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getTerminalOnOffLineStatusRecords(List<Long> terminalIds, long startDate, long endDate, int type,
													   CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询流量统计概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public ResWFlowSummaryQuery getWFlowSummary(List<Long> terminalIds, long startDate, long endDate,
												CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询流量统计详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public OptResult getWFlowRecords(List<Long> terminalIds, long startDate, long endDate,
									 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/***
	 * 查询汇总报警概要
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResAllAlarmSummaryQuery getAllAlarmSummary(List<Long> terminalIds, long startDate, long endDate,
													  CommonParameter comParameter, String queryKey) throws RemoteException;

	/**
	 * 查询终端油耗。起止时间为某一天的起止时间00：00：00---23：59：59
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public Map<Long, Integer> getOilConsumptionData(List<Long> terminalIds, long startDate, long endDate)
			throws RemoteException;

	/**
	 * 获取滞留超时报警详情
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @param queryKey
	 * @return
	 * @throws RemoteException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public OptResult getOvertimeParkAlarmRecords(List<Long> terminalIds, long startDate, long endDate,
												 CommonParameter comParameter, String queryKey) throws  InstantiationException,
			IllegalAccessException;

	/**
	 * 网格车次检索
	 *
	 * @param districtCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ResVehiclePassTimesRecordsQuery queryVehiclePassTimesRecords(int districtCode, long startDate, long endDate)
			throws RemoteException;

	public ResVehiclePassTimesRecordsQuery getVehiclePassTimesBytileId(List<Long> tileIds, long startDate, long endDate)
			throws RemoteException;

	/**
	 * 区域车次检索
	 *
	 * @param districtCodes
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws RemoteException
	 */
	public ResVehiclePassInAreaRecordsQuery queryVehiclePassInAreaRecords(List<Integer> districtCodes, int type,
																		  long startDate, long endDate) throws RemoteException;

	public ResFaultCodeAlarmRecoreds getFaultCodeRecords(List<Long> terminalIds, int spn, int fmi, long startDate,
														 long endDate, CommonParameter commonParameter) throws RemoteException;

	/*
	 * 获取对应终端最新的故障码记录：根据faultCodeAlarm的endtime属性取值
	 * 
	 * @param start 开始节点
	 * 
	 * @param end 结束节点
	 * 
	 * @return <tid#spn#fmi,FaultCodeEntity>
	 * 
	 * add by zhangyj
	 */
	public Map<String, FaultCodeEntity> getLastFaultCode(List<Long> terminalIds, long startDate, long endDate)
			throws RemoteException;

	/*
	 * 获取对应终端最新的滞留超时记录
	 * 
	 * @param allTerminals 终端列表
	 * 
	 * @return <tid#spn#fmi,OvertimeParkAlarm>
	 * 
	 * add by zhangyj
	 */
	public Map<Long, OvertimeParkAlarm> getOvertimeParkAlarmNoEndMerge(List<Long> terminalIds, int day)
			throws RemoteException;

	/*
	 * 获取对应终端最新的区域停留时间记录
	 *
	 * @param allTerminals 终端列表
	 *
	 * @return <tid#spn#fmi,StaytimeParkAlarm>
	 *
	 * add by zhangyue
	 */
	public Map<Long, StaytimeParkAlarm> getStaytimeParkAlarmNoEndMerge(List<Long> terminalIds, int day)
			throws RemoteException;

	/*
	 * 从redis拉取最新的终端里程油耗数据，缓存到dsa
	 * 
	 * add by zhangyj
	 */
	public Map<Long, Mileages> getAllMileageAndOilDataFromRedis() throws RemoteException;

	/*
	 * 从redis拉取最新的区域停留缓存，缓存到dsa
	 *
	 * add by zhangyj
	 */
	public Map<Long, StaytimeParkAlarm> getAllStaytimeParkCacheFromRedis() throws RemoteException;

	public Map<Long, LCMileageAndOilDataRes.MileageAndOilData> mileagesRecoredsQuery(List<Long> terminalId) throws RemoteException;

	/**
	 * 获取对应终端最新的停滞超时记录
	 * @param terminalIds
	 * @param day
	 * @return
	 * @throws RemoteException
	 */
	public Map<Long,StagnationTimeoutEntity> getStagnationTimeoutAlarmNoEndMerge(List<Long> terminalIds, long day) throws RemoteException;
}
