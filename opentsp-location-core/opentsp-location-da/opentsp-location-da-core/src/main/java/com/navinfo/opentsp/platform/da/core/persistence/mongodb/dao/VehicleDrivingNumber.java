/**
 * 
 */
package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;

import java.util.List;
import  com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.ResVehiclePassInAreaRecordsQuery;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.ResVehiclePassTimesRecordsQuery;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesRecord;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesTreeNode;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCCrossGridCounts.CrossGridCounts;
import  com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassTimesRecords.VehiclePassTimesRecords;

/**
 * 车次统计
 * @author zyl
 * @date 2015年10月28日
 */
public interface VehicleDrivingNumber {

	/**
	 * 网格车次查询
	 * @param districtCode
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	@Deprecated
	public ResVehiclePassTimesRecordsQuery queryVehiclePassTimesRecords(
			int districtCode, long startDate, long endDate);
	@Deprecated
	public ResVehiclePassTimesRecordsQuery getVehiclePassTimesBytileId(
			List<Long> tileIds, long startDate, long endDate);

	/**
	 * 网格车次查询（2016.07.29新增）——王景康
	 *
	 * @param tileIds
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public VehiclePassTimesRecords getVehiclePassTimesByTileId(List<Long> tileIds, long startDate, long endDate);

	public VehiclePassTimesRecords getVehiclePassTimesRecords(int districtCode, long startDate, long endDate);

	/**
	 * 区域车次查询
	 * @param districtCodes
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public ResVehiclePassInAreaRecordsQuery queryVehiclePassInAreaRecords(
			List<Integer> districtCodes, int type, long startDate, long endDate);

	/**
	 * 保存网格车次统计结果
	 * @param grids
	 * @return
	 */
	public PlatformResponseResult saveVehiclePassGridTimes(List<VehiclePassTimesTreeNode> grids);
	/**
	 * 保存区域车次统计结果
	 * @param areas
	 * @return
	 */
	public PlatformResponseResult saveVehiclePassAreaTimes(List<VehiclePassTimesRecord> areas);
	public CrossGridCounts getGridCrossCounts(List<Long> terminalIds,
											  List<Long> tileId,long startDate, long endDate);
}
