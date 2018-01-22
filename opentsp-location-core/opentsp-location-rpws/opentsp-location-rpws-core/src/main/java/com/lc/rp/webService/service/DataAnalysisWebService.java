package com.lc.rp.webService.service;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;
import java.util.List;

/**
 *
 * 数据统计查询的接口
 *
 */
@WebService
public interface DataAnalysisWebService {

	/**
	 * 超速报警统计-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	@WebMethod(action = "OverspeedAlarmRecords")
//	public byte[] getOverspeedAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 超速报警统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getOverspeedAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 进去区域报警统计-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getAreaINOUTAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "type", mode = Mode.IN) int type,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 进去区域报警统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getAreaINOUTAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "type", mode = Mode.IN) int type,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 区域超速报警统计-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getAreaOverspeedAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 区域超速报警统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getAreaOverspeedAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 进出线路报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getRouteINOUTAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "type", mode = Mode.IN) int type,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 进出线路报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getRouteINOUTAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "type", mode = Mode.IN) int type,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 路段超速报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getRLineOverspeedAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 路段超速报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getRLineOverspeedAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.4.3 路段超时报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getRLineOvertimeAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.4.3 路段超时报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getRLineOvertimeAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 超时停车报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getOvertimeParkingAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 超时停车报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getOvertimeParkingAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.6 疲劳驾驶报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getFatigueAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.6 疲劳驾驶报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getFatigueAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.7 紧急报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getEmergencyAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.7 紧急报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getEmergencyAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.7 主电源欠压报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getPowerStayInLowerAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.7 主电源欠压报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getPowerStayInLowerAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.8 主电压断电报警-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getElectricPowerOffAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.8 主电压断电报警-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getElectricPowerOffAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.9 ACC状态-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getACCStatusAlarmRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.9 ACC状态-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getACCStatusAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.10 里程统计-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getMileagesRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.10 里程统计-统计数据详情
	 *
	 * add by zhangyj
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
	public byte[] getMileageAndOilData(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId);

	/**
	 * 新接口4.1.2 里程统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
	public byte[] getMileageConsumptionRecords(@WebParam(name = "terminalID", mode = Mode.IN) List<Long> terminalId,
											   @WebParam(name = "startDate", mode = Mode.IN) long startDate,
											   @WebParam(name = "endDate", mode = Mode.IN) long endDate,
											   @WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.10 里程统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getMileagesSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.11 驾驶员登录统计-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getDriverLoginRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "driverIDCode", mode = Mode.IN) String driverIDCode,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.11 驾驶员登录统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
	// public byte[] getDriverLoginSummary(
	// @WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
	// @WebParam(name = "driverIDCode", mode = Mode.IN) String driverIDCode,
	// @WebParam(name = "startDate", mode = Mode.IN) long startDate,
	// @WebParam(name = "endDate", mode = Mode.IN) long endDate,
	// @WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter
	// commonParameter
	// );
	/**
	 * 2.12 终端在线状态-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getTerminalOnOffLineStatusRecords(
//			@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "type", mode = Mode.IN) int type,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.12 终端在线状态-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
	// public byte[] getTerminalOnOffLineStatusSummary(
	// @WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
	// @WebParam(name = "startDate", mode = Mode.IN) long startDate,
	// @WebParam(name = "endDate", mode = Mode.IN) long endDate,
	// @WebParam(name = "type", mode = Mode.IN) int type,
	// @WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter
	// commonParameter
	// );
	/**
	 * 2.13 流量统计-统计数据详情
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getWFlowRecords(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.13 流量统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getWFlowSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 2.1 报警汇总统计-统计数据概要
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getAllAlarmSummary(@WebParam(name = "terminalId", mode = Mode.IN) List<Long> terminalId,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 3.12.3 终端上线率
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param commonParameter
	 *            通用参数对象
	 * @return
	 */
//	public byte[] getTerminalOnlinePercentage(@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 管理看板查询
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
//	public byte[] getManageBoardRecords(@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
//			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
//			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
//			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 滞留超时查询
	 *
	 * @param terminalID
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public byte[] getOvertimeParkRecords(
			@WebParam(name = "terminalID", mode = Mode.IN) List<Long> terminalID,
			@WebParam(name = "areaIds", mode = Mode.IN) List<Long> areaId,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 区域停留时长查询
	 *
	 * @param terminalID
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public byte[] getStaytimeParkRecords(
			@WebParam(name = "terminalID", mode = Mode.IN) List<Long> terminalID,
			@WebParam(name = "areaIds", mode = Mode.IN) List<Long> areaId,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 删除滞留超时报表
	 *
	 * @param accessTocken
	 * @param _id
	 * @return
	 */
	public byte[] delOvertimeParkRecords(
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken,
			@WebParam(name = "_id", mode = Mode.IN) String _id,
			@WebParam(name = "recordDate", mode = Mode.IN) long recordDate);


	/**
	 * 4.16.1 网格车次检索
	 *
	 * @param districtCode
	 * @param startDate
	 * @param endDate
	 * @param accessTocken
	 * @return
	 */
	public byte[] getVehiclePassTimesRecords(
			@WebParam(name = "districtCode", mode = Mode.IN) int districtCode,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);

	/**
	 * 4.16.2 区域车次检索
	 *
	 * @param districtCodes
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @param accessTocken
	 * @return
	 */
	public byte[] getVehiclePassInArea(
			@WebParam(name = "districtCodes", mode = Mode.IN) List<Integer> districtCodes,
			@WebParam(name = "type", mode = Mode.IN) int type,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);

	/**
	 * 4.3.4	区域最新车辆检索    （新接口）
	 * @param districtCodes
	 * @param accessTocken
	 * @return
	 */
	public byte[] getLastestVehiclePassInArea(
			@WebParam(name = "districtCodes", mode = Mode.IN) List<Integer> districtCodes,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);
	/**
	 * 区域车次检索
	 * @param terminalIds
	 * @param tileId
	 * @param startDate
	 * @param endDate
	 * @param accessTocken
	 * @return
	 */
	public byte[] getGridCrossCounts(
			@WebParam(name = "terminalIds", mode = Mode.IN) List<Long> terminalIds,
			@WebParam(name = "tileId", mode = Mode.IN) List<Long> tileId,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);

	/**
	 * 4.3.1   根据瓦片ID查询车次
	 * @param tileIds
	 * @param startDate
	 * @param endDate
	 * @param accessTocken
	 * @return
	 */
	public byte[] getVehiclePassTimesBytileId(
			@WebParam(name = "tileIds", mode = Mode.IN) List<Long> tileIds,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);

	/**
	 * 故障码查询
	 * @param terminalID
	 * @param spn
	 * @param fmi
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public byte[] getFaultCodeRecords(
			@WebParam(name = "terminalID", mode = Mode.IN) List<Long> terminalID,
			@WebParam(name = "spn", mode = Mode.IN) int spn,
			@WebParam(name = "fmi", mode = Mode.IN) int fmi,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 4.2.3  停滞超时记录检索
	 * @param terminalID
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public byte[] getStagnationTimeoutRecords(
			@WebParam(name = "terminalID", mode = Mode.IN) List<Long> terminalID,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 4.2.4    停滞超时撤销和恢复
	 * @param _id
	 * @param isCancel
	 * @param recordDate
	 * @param accessTocken
	 * @return
	 */
	public byte[] stagnationTimeoutCancelOrNot(
			@WebParam(name = "_id", mode = Mode.IN) String _id,
			@WebParam(name = "isCancel", mode = Mode.IN) boolean isCancel,
			@WebParam(name = "recordDate", mode = Mode.IN) long recordDate,
			@WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);

	/**
	 * 4.4    历史停车
	 * @param terminalIDs
	 * @param startDate
	 * @param endDate
	 * @param parkLimit
	 * @param commonParameter
	 * @return
	 */
	public byte[] GetHistoryParking(
			@WebParam(name = "terminalIDs", mode = Mode.IN) List<Long> terminalIDs,
			@WebParam(name = "startDate", mode = Mode.IN) long startDate,
			@WebParam(name = "endDate", mode = Mode.IN) long endDate,
			@WebParam(name = "parkLimit", mode = Mode.IN) int parkLimit,
			@WebParam(name = "commonParameter", mode = Mode.IN) CommonParameter commonParameter);

	/**
	 * 新接口4.1.3 里程能耗实时计算
	 *
	 * @param terminalId
	 *            终端标识号
	 * @param startDate
	 *            开始时间，UTC时间，单位为秒
	 * @param endDate
	 *            结束时间，UTC时间，单位为秒
	 * @param accessTocken
	 *            客户端鉴权动态口令
	 * @return
	 */
	public byte[] calculateMileageConsumption(@WebParam(name = "terminalID", mode = Mode.IN) long terminalId,
											  @WebParam(name = "startDate", mode = Mode.IN) long startDate,
											  @WebParam(name = "endDate", mode = Mode.IN) long endDate,
											  @WebParam(name = "accessTocken", mode = Mode.IN) long accessTocken);
}