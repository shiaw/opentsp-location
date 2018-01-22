package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassInDistrict;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesRecord;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesTreeNode;
import java.util.List;
import java.util.Map;


public interface AlarmStatisticsStoreService  {
	/**
	 * 保存终端产生超速报警信息
	 *
	 * @param terminalId
	 * @param day
	 * @param overspeedAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveOverspeedAlarmInfo(long terminalId, long day,
																  List<OverspeedAlarm> overspeedAlarmList) ;

	/**
	 * 存储进出区域报警统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param areaINOUTAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveAreaINOUTAlarm(long terminalId, long day,
															  List<AreaINOUTAlarm> areaINOUTAlarmList) ;

	/**
	 * 保存终端产生超速报警统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param areaOverspeedAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveAreaOverspeedAlarmInfo(long terminalId, long day,
																	  List<AreaOverspeedAlarm> areaOverspeedAlarmList) ;

	/**
	 * 保存进出线路报警统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param routeINOUTAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveRouteINOUTAlarmInfo(long terminalId, long day,
																   List<RouteINOUTAlarm> routeINOUTAlarmList) ;

	/**
	 * 保存终端产生路段超速报警统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param rLineOverspeedAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveRLineOverspeedAlarmInfo(long terminalId, long day,
																	   List<RLineOverspeedAlarm> rLineOverspeedAlarmList) ;

	/**
	 * 保存终端产生路段超时报警信息
	 *
	 * @param terminalId
	 * @param day
	 * @param rLineOvertimeAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveRLineOvertimeAlarm(long terminalId, long day,
																  List<RLineOvertimeAlarm> rLineOvertimeAlarmList) ;

	/**
	 * 停滞超时数据存储
	 *
	 * @param overtimeParkingAlarmList     停滞超时统计数据
	 * @param day	当天统计时间，单位(秒)
	 * @return
	 */
	abstract public PlatformResponseResult saveStagnationTimeoutInfo(
			List<StagnationTimeoutEntity> overtimeParkingAlarmList, long day) ;

	/**
	 * 保存终端产生疲劳驾驶统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param fatigueAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveFatigueAlarmInfo(long terminalId, long day,
																List<FatigueAlarm> fatigueAlarmList) ;

	/**
	 * 保存终端产生紧急报警统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param emergencyAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveEmergencyAlarmInfo(long terminalId, long day,
																  List<EmergencyAlarm> emergencyAlarmList) ;

	/**
	 * 保存终端产生主电源欠压统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param powerStayInLowerAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult savePowerStayInLowerAlarmInfo(long terminalId, long day,
																		 List<PowerStayInLowerAlarm> powerStayInLowerAlarmList) ;

	/**
	 * 保存终端产生主电压断电统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param electricPowerOffAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveElectricPowerOffAlarmInfo(long terminalId, long day,
																		 List<ElectricPowerOffAlarm> electricPowerOffAlarmList) ;

	/**
	 * 保存终端产生ACC状态统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param aCCStatusAlarmList
	 * @return
	 */
	abstract public PlatformResponseResult saveACCStatusAlarmInfo(long terminalId, long day,
																  List<TerACCStatus> aCCStatusAlarmList) ;

	/**
	 * 保存终端产生里程统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param oil
	 * @param milagesList
	 * @return
	 * @
	 */
	abstract public PlatformResponseResult saveMilagesInfo(long terminalId, long day, int oil,
														   List<Mileages> milagesList) ;

	abstract public PlatformResponseResult saveBatchMilagesInfo(List<Mileages> allTerminalMileages, long day)
			;

	/**
	 * 保存终端产生驾驶员登录统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param driverLoginList
	 * @return
	 */
	abstract public PlatformResponseResult saveDriverLoginInfo(long terminalId, long day,
															   List<DriverLogin> driverLoginList) ;

	/**
	 * 保存终端在线状态统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param terminalOnOffLineStatusList
	 * @return
	 */
	abstract public PlatformResponseResult saveTerminalOnOffLineStatusInfo(long terminalId, long day,
																		   List<TerminalOnOffLineStatus> terminalOnOffLineStatusList) ;

	/**
	 * 保存流量统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param wFlowList
	 * @return
	 */
	abstract public PlatformResponseResult saveWFlowInfo(long terminalId, long day, List<WFlow> wFlowList)
	;

	/**
	 * 报警汇总统计信息
	 *
	 * @param terminalId
	 * @param day
	 * @param totalAlarmInfo
	 * @return
	 */
	abstract public PlatformResponseResult saveTotalAlarmInfo(long terminalId, long day,
															  List<TotalAlarmInfo> totalAlarmInfo) ;

	/**
	 * rmi服务可用性检测
	 *
	 * @return
	 * @
	 */
	public boolean isConnected() ;

	/**
	 * 保存滞留超时统计结果
	 *
	 * @param terminalId
	 * @param day
	 * @param alarmInfo
	 * @return
	 * @
	 */
	abstract public PlatformResponseResult saveOvertimeParkAlarmInfo(long terminalId, long day,
																	 List<OvertimeParkAlarm> alarmInfo) ;

	abstract public PlatformResponseResult saveBatchOvertimeParkAlarmInfo(
			List<OvertimeParkAlarm> allTerminalOvertimeParkAlarm, long day) ;


	abstract public PlatformResponseResult saveBatchStaytimeParkAlarmInfo(
			List<StaytimeParkAlarm> allTerminalStaytimeParkAlarm, long day) ;


	/**
	 * 根据mongo主键删除滞留超时统计结果
	 *
	 * @param id
	 * @return
	 * @
	 */
	abstract public PlatformResponseResult delOvertimeParkStatisticInfo(String id, long recordDate)
	;

	/**
	 * 保存网格车次统计结果
	 *
	 * @param grids
	 * @return
	 * @
	 */
	abstract public PlatformResponseResult saveVehiclePassGridTimes(List<VehiclePassTimesTreeNode> grids)
	;

	/**
	 * 保存区域车次统计结果
	 *
	 * @param areas
	 * @return
	 * @
	 */
	abstract public PlatformResponseResult saveVehiclePassAreaTimes(List<VehiclePassTimesRecord> areas)
	;

	abstract public PlatformResponseResult saveFaultCodeStatistic(List<FaultCodeEntity> alarms, long day)
			;

	// 以下均为新增接口 start
	// add by zhangyj
	abstract public PlatformResponseResult updateFaultCodeStatistic(List<FaultCodeEntity> alarms, long day)
	;

	abstract public PlatformResponseResult updateBatchOvertimeParkAlarmInfo(
			List<OvertimeParkAlarm> allTerminalOvertimeParkAlarm, long day) ;

	abstract public PlatformResponseResult updateBatchStaytimeParkAlarmInfo(
			List<StaytimeParkAlarm> allTerminalStaytimeParkAlarm, long day) ;

	abstract PlatformResponseResult saveMileageAndOilDataToStaticRedis(List<Mileages> mileages) ;

	abstract PlatformResponseResult saveLastestVehicleInfoToStaticRedis(
			List<VehiclePassTimesRecord> vehiclePassTimesList) ;
	abstract PlatformResponseResult saveDistrictTimesToStaticRedis(List<VehiclePassInDistrict> list) ;

	/**
	 * 更新对应终端最新的停滞超时记录
	 * @param StagnationTimeoutAlarmList
	 * @param day
	 * @return
	 * @
	 */
	abstract PlatformResponseResult updateStagnationTimeoutInfo(List<StagnationTimeoutEntity> StagnationTimeoutAlarmList,
																long day) ;

	abstract PlatformResponseResult stagnationTimeoutCancelOrNot(String _id, boolean isCancel,
																 long recordDate) ;

	abstract PlatformResponseResult saveBatchStaytimeParkCache(Map<Long, StaytimeParkAlarm> cache);
	// end
}
