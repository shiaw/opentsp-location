package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCCalculateMileageConsumptionRes.CalculateMileageConsumptionRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetLastestVehiclePassInAreaRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetStagnationTimeoutRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCDelOvertimeParkResult.DelOvertimeParkResult;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalTrackRes.TerminalTrackRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCFaultCodeRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCOvertimeParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCStaytimeParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassInAreaRecords.VehiclePassInAreaRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassTimesRecords.VehiclePassTimesRecords;

import java.rmi.RemoteException;
import java.util.List;

import static com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCMileageConsumptionRecords.MileageConsumptionRecords;


/**
 * 统计查询接口（2016年5月19日添加）
 *
 * @author 王景康
 *
 */
public interface StatisticsQueryService  {

	/**
	 * RMI服务可用性检测
	 *
	 * @return
	 * @
	 */
	public boolean isConnected() ;

	/**
	 * 查询指定终端、指定时间范围内的轨迹数据（2016.06.02新增——王景康）
	 *
	 * @param terminalId
	 * @param beginDate
	 * @param endDate
	 * @param isFilterBreakdown
	 * @param breakdownCode
	 * @param commonParameter
	 * @return
	 */
	public TerminalTrackRes getTerminalTrack(long terminalId, long beginDate, long endDate, boolean isFilterBreakdown,
											 long breakdownCode, boolean isThin, int level,int isAll,CommonParameter commonParameter) ;

	/**
	 * 最新能耗、里程数据检索，支持分页，查询结果按入参的终端ID排序
	 *
	 * @param terminalIds
	 * @param commonParameter
	 * @return
	 * @
	 */
	public MileageAndOilDataRes getMileageAndOilData(List<Long> terminalIds, CommonParameter commonParameter)
	;

	/**
	 * 里程能耗数据检索，支持分页，查询结果按入参的终端ID排序，数据粒度为天，每个车一条记录（自动合并）
	 *
	 * @param terminalIds
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 * @
	 */
	public MileageConsumptionRecords getMileageConsumptionRecords(List<Long> terminalIds, long startDate, long endDate,
																  CommonParameter commonParameter) ;

	/**
	 * 滞留超时数据检索（支持分页，查询结果按开始时间倒序排序）
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 * @
	 */
	public LCOvertimeParkRecoreds.OvertimeParkRecoreds getOvertimeParkRecords(List<Long> terminalIds, List<Long> areaIds, long startDate,
																			  long endDate, CommonParameter commonParameter) ;

	/**
	 * 区域停留时长数据检索（支持分页，查询结果按开始时间倒序排序）
	 *
	 * @param terminalIds
	 * @param areaIds
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 * @
	 */
	public LCStaytimeParkRecoreds.StaytimeParkRecoreds getStaytimeParkRecords(List<Long> terminalIds, List<Long> areaIds, long startDate,
																			  long endDate, CommonParameter commonParameter) ;

	/**
	 * 滞留超时数据删除
	 *
	 * @param id
	 * @param recordTime
	 * @return
	 * @
	 */
	public DelOvertimeParkResult delOvertimeParkRecords(String id, long recordTime) ;

	/**
	 * 故障码数据检索，支持分页，查询结果按发生的时间倒序排序
	 *
	 * @param terminalIds
	 * @param spn
	 * @param fmi
	 * @param startDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 * @
	 */
	public LCFaultCodeRecords.FaultCodeRecords getFaultCodeRecords(List<Long> terminalIds, int spn, int fmi, long startDate, long endDate,
																   CommonParameter commonParameter) ;

	/**
	 * 网格车次数据检索
	 *
	 * @param districtCode
	 * @param startDate
	 * @param endDate
	 * @return
	 * @
	 */
	public VehiclePassTimesRecords getVehiclePassTimesRecords(int districtCode, long startDate, long endDate)
	;

	/**
	 * 根据瓦片ID查询车次
	 *
	 * @param tileIds
	 * @param startDate
	 * @param endDate
	 * @return
	 * @
	 */
	public VehiclePassTimesRecords getVehiclePassTimesBytileId(List<Long> tileIds, long startDate, long endDate)
	;

	/**
	 * 区域车次检索
	 *
	 * @param districtCodes
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 * @
	 */
	public VehiclePassInAreaRecords getVehiclePassInArea(List<Integer> districtCodes, int type, long startDate,
														 long endDate) ;

	/**
	 * 区域最新车辆检索
	 *
	 * @param districtCodes
	 * @
	 */
	public LCGetLastestVehiclePassInAreaRes.GetLastestVehiclePassInAreaRes getLastestVehiclePassInArea(List<Integer> districtCodes)
	;

	/**
	 * 停滞超时
	 */
	public LCGetStagnationTimeoutRes.GetStagnationTimeoutRes getStagnationTimeoutRecords(List<Long> terminalID,
																						 long startDate, long endDate, CommonParameter commonParameter) ;

	/**
	 * 里程能耗实时计算
	 */
	public CalculateMileageConsumptionRes calculateMileageConsumption(long terminalId,
																	  long startDate, long endDate, long accessTocken);
}
