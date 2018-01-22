package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.Mileages;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalTrackRes.TerminalTrackRes;

import java.util.List;


/**
 * @author zyl
 * @date 2016年2月22日
 *
 * @author 王景康，2016/05/20修改
 *
 */
public interface ReportQueryService {

	@Deprecated
	public List<DAFaultCodeAlarm> queryFaultCodeAlarm(List<Long> terminalIds, int spn, int fmi, long startDate,
													  long endDate, CommonParameter commonParameter);

	/**
	 * 查询指定终端、指定时间范围内的轨迹数据
	 *
	 * @param terminalId
	 * @param beginDate
	 * @param endDate
	 * @param isFilterBreakdown
	 * @param breakdownCode
	 * @param commonParameter
	 * @return
	 */
	public TerminalTrackRes getTerminalTrack(long terminalId, long beginDate, long endDate, boolean isFilterBreakdown, long breakdownCode, boolean isThin, int level,int isAll, CommonParameter commonParameter);

	/**
	 * 查询指定时间范围内指定终端的里程、油耗等信息（每个终端、每天一条记录）
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<DAMilages> getMilageRecords(List<Long> terminalIds, long startDate, long endDate);

	/**
	 * 查询故障码（可指定是否分页查询），查询结果按发生的时间倒序排序
	 *
	 * @param terminalIds
	 * @param spn
	 * @param fmi
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public List<DAFaultCodeAlarm> getFaultCodeAlarmRecords(List<Long> terminalIds, int spn, int fmi, long startDate,
														   long endDate, CommonParameter commonParameter);

	/**
	 * 查询符合条件的故障码总数
	 *
	 * @param terminalIds
	 * @param spn
	 * @param fmi
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long getFaultCodeAlarmRecordsCount(List<Long> terminalIds, int spn, int fmi, long startDate, long endDate);

	/**
	 * 查询滞留超时报警记录（可指定是否分页查询），查询结果按开始时间倒序排序
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public List<DAOvertimeParkAlarm> getOvertimeParkAlarmRecords(List<Long> terminalIds, List<Long> areaIds,
																 long startDate, long endDate, CommonParameter comParameter);

	/**
	 * 查询区域停留时长报警记录（可指定是否分页查询），查询结果按开始时间倒序排序
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @param comParameter
	 * @return
	 */
	public List<DAStaytimeParkAlarm> getStaytimeParkAlarmRecords(List<Long> terminalIds, List<Long> areaIds,
																 long startDate, long endDate, CommonParameter comParameter);

	/**
	 * 查询符合条件的滞留超时报警记录总数
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long getOvertimeParkAlarmRecordsCount(List<Long> terminalIds, List<Long> areaIds, long startDate,
												 long endDate);

	/**
	 * 查询符合条件的区域停留时长报警记录总数
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public long getStaytimeParkAlarmRecordsCount(List<Long> terminalIds, List<Long> areaIds, long startDate,
												 long endDate);

	public List<LocationData> getTerminalLocationData(long terminalId,long queryDate);

	/**
	 * 停滞超时
	 */
	public List<DAStagnationTimeoutAlarm> getStagnationTimeoutRecords(List<Long> terminalID, long startDate, long endDate,
																	  CommonParameter commonParameter);

	public long getStagnationTimeoutRecordsCount(List<Long> terminalID, long startDate, long endDate);

	public Mileages calculateMileageConsumption(long terminalId, long startDate, long endDate, long accessTocken);
}
