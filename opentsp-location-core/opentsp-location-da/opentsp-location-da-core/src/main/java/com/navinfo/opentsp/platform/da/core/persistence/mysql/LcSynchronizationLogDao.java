package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;

import java.util.List;


public interface LcSynchronizationLogDao extends BaseDao<LcSynchronizationLogDBEntity> {

	/**
	 * 获得要同步的数据
	 * @return
	 */
	public List<LcSynchronizationLogDBEntity> getSynchronizationData();

	/**
	 * 获取当前数据库中id的最大值
	 * @return
	 */
	public int getMaxRecordValue() throws Exception;

	/**
	 * 根据唯一标识修改
	 * @param log
	 * @return
	 */
	public int updateByUniqueCode(LcSynchronizationLogDBEntity log);
}

