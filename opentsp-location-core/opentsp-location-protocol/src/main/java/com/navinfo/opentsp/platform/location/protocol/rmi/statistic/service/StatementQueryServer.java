package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDataEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.ResManageBoardRecoredsEntity;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;



public interface StatementQueryServer extends Remote{

	/************************
	 * 多终端超速概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery overspeedSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/*****************************
	 * 超速报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResOverspeedAlarmRecoredsQuery overspeedRecordsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/************************
	 * 区域超速概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery areaOverspeedSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * 区域超速报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResAreaOverspeedAlarmRecoredsQuery areaOverspeedRecordsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/************************
	 * 紧急报警概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery emergencyAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * 紧急报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResEmergencyAlarmRecoredsQuery emergencyAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/************************
	 * 进出区域概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery areaINOUTAlarmSummaryQuery(List<Long> terminalIds, int type, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/*****************************
	 * 进出区域报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResAreaINOUTAlarmRecoredsQuery areaINOUTAlarmRecoredsQuery(List<Long> terminalIds, int type, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/************************
	 * 路线超速概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery routeLineOverspeedAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ; 
	
	/*****************************
	 * 路线超速报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResRLineOverspeedAlarmRecoredsQuery routeLineOverspeedAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/************************
	 * 路线超时概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery routeLineOvertimeAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/*****************************
	 * 路线超时报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResRLineOvertimeAlarmRecoredsQuery routeLineOvertimeAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/************************
	 * 进出路线概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery routeINOUTAlarmSummaryQuery(List<Long> terminalIds, int type, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/*****************************
	 * 进出路线报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResRouteINOUTAlarmRecoredsQuery routeLineINOUTAlarmRecoredsQuery(List<Long> terminalIds, int type, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/************************
	 * ACC状态概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery accStatusAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * ACC状态报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResACCStatusAlarmRecoredsQuery accStatusAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/************************
	 * 超时停车概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery overtimeParkingAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * 超时停车报警详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResOvertimeParkingAlarmRecoredsQuery overtimeParkingAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * 终端是否在线状态详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResTerminalOnOffLineStatusRecordsQuery terminalOnOffLineStatusRecordsQuery(List<Long> terminalIds, int type, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	/************************
	 * 流量统计概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResWFlowSummaryQuery wflowSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * 流量统计详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResWFlowRecoredsQuery wflowRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/*****************************
	 * 驾驶员登录统计详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResDriverLoginRecordsQuery driverLoginRecordsQuery(List<Long> terminalIds, String driverId, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException ;
	
	
	/************************
	 * 主电源掉电概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery electricPowerOffAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException;
	
	/*****************************
	 * 主电源掉电统计详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResElectricPowerOffAlarmRecoredsQuery electricPowerOffAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException;
	
	
	/************************
	 * 里程统计概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResMileagesSummaryQuery mileagesSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException;
	
	
	/*****************************
	 * 里程统计详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResMileagesRecoredsQuery mileagesRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException;
	
	/************************
	 * 疲劳驾驶统计概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery fatigueAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException;
	
	/*****************************
	 * 疲劳驾驶统计详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResFatigueAlarmRecoredsQuery fatigueAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey) throws RemoteException;
	
	
	/************************
	 * 主电源欠压概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public AlarmSummaryQuery powerStayInLowerAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey)throws RemoteException;
	
	/*****************************
	 * 主电源欠压统计详情分页查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResPowerStayInLowerAlarmRecoredsQuery powerStayInLowerAlarmRecoredsQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey)throws RemoteException;
	
	
	/************************
	 * 报警汇总概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public ResAllAlarmSummaryQuery allAlarmSummaryQuery(List<Long> terminalIds, long startDate, long endDate, CommonParameter comParameter, String mdkey)throws RemoteException;
	
	
	/************************
	 * 报警汇总概要查询
	 * 
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public GpsDataEntity terminalLocations(List<Long> terminalIds, long startDate, long endDate,
										   boolean isFilterBreakdown, long breakdownCode, CommonParameter commonParameter, String mdkey)throws RemoteException;
	
	/**
	 * 查询终端上线率
	 * @param terminalIds
	 * @param beginTime
	 * @param endTime
	 * @param commonParameter
	 * @return
	 * @throws RemoteException
	 */
	public TerminalOnlineRecordsQuery  getTerminalOnlinePercentage(List<Long> terminalIds,
																   long beginTime, long endTime, CommonParameter commonParameter, String mdkey)
				throws RemoteException;
		
	/**
	 * 查询管理看板详情
	 * @param terminalIds
	 * @param beginTime
	 * @param endTime
	 * @param commonParameter
	 * @param mdkey
	 * @return
	 * @throws RemoteException
	 */
	public ResManageBoardRecoredsEntity getManageBoardRecords(List<Long> terminalIds,
															  long beginTime, long endTime, CommonParameter commonParameter, String mdkey)
			throws RemoteException;
	/**
	 * 查找滞留超时报警报表
	 * @param terminalID
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public ResOvertimeParkRecoredsQuery getOvertimeParkRecords(List<Long> terminalID, List<Long> areaIds,
															   long startDate, long endDate, CommonParameter commonParameter) throws RemoteException;
	
	/**
	 * 网格车次检索
	 * @param districtCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ResVehiclePassTimesRecordsQuery getVehiclePassTimesRecords(int districtCode, long startDate, long endDate) throws RemoteException;
	/**
	 * 区域车次检索
	 * @param districtCodes
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ResVehiclePassInAreaRecordsQuery getVehiclePassInArea(List<Integer> districtCodes, int type,
																 long startDate, long endDate) throws RemoteException;
	public ResFaultCodeAlarmRecoreds getFaultCodeRecords(List<Long> terminalIds, int spn, int fmi,
														 long startDate, long endDate, CommonParameter commonParameter) throws RemoteException;
}
