package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMessageBroadcastLogDBEntity;

import java.util.List;


public interface LcMessageBroadcastLogDao  extends BaseDao<LcMessageBroadcastLogDBEntity>{


	/**
	 * 根据参数查询信息播报日志
	 * @param terminalId 终端标识
	 * @param areaId 区域编码
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return List< LcMessageBroadcastLogDBEntity>
	 */
	public List<LcMessageBroadcastLogDBEntity> queryLogList(
			long terminalId, int areaId, long start, long end,int currentPage,int pageSize) ;

}
