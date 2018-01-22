package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcOutRegionLimitSpeedLogDBEntity;

import java.util.List;


public interface LcOutRegionLimitSpeedLogDao  extends BaseDao<LcOutRegionLimitSpeedLogDBEntity>{


	/**
	 * 根据参数查询出区域限速日志
	 * @param terminalId 终端标识
	 * @param areaId 区域编码
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return List< LcMessageBroadcastLogDBEntity>
	 */
	public List<LcOutRegionLimitSpeedLogDBEntity> queryLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) ;
	/**
	 * 查询总数
	 * @param terminalId
	 * @param areaId
	 * @param start
	 * @param end
	 * @return
	 */
	public int getCount(long terminalId,int areaId,long start,long end);

}
