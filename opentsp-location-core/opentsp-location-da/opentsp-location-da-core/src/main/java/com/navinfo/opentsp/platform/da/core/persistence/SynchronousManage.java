package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcSynchronizationLogDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import java.util.List;


public interface SynchronousManage {

	/**
	 * 保存同步数据
	 * @param log
	 * @return
	 */
	public PlatformResponseResult saveSynchronousData(LcSynchronizationLogDBEntity log);
	/**
	 * 查询同步数据
	 * @return
	 */
	public List<LcSynchronizationLogDBEntity> getSynchronizationData();
	/**
	 * 修改同步数据
	 * @param log
	 * @return
	 */
	public PlatformResponseResult updateSynchronousData(LcSynchronizationLogDBEntity log);

	/**
	 * 获取当前数据库中id的最大值
	 * @return
	 */
	public int getMaxRecordValue();
}
