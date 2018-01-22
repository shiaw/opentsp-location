package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataReportEntity;

import java.util.List;


public interface CanDataMongodbService {
	abstract boolean saveCanData(CanDataEntity dataEntity);

	abstract boolean saveReportCanData(CanDataReportEntity dataEntity);

	/**
	 * pageNum和pageSize都为0的时候，查询全部数据
	 * @param terminalIds
	 * @param beginTime
	 * @param endTime
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<CanDataEntity> findCanData(List<Long> terminalIds,
										   long beginTime, long endTime,int pageNum,int pageSize);

	/**
	 * pageNum和pageSize都为0的时候，查询全部数据
	 * @param terminalIds
	 * @param beginTime
	 * @param endTime
	 * @param month
	 * @param pageNum
	 * @param pageSize
	 * @return
	 */
	public List<CanDataReportEntity> findReportCanData(List<Long> terminalIds,
													   long beginTime, long endTime,String month,int pageNum,int pageSize);

}
