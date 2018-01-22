package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaLogDBEntity;

import java.util.List;


public interface LcTerminalAreaLogDao extends BaseDao<LcTerminalAreaLogDBEntity>{
	/**
	 * 根据参数查询区域日志
	 * @param terminalId 终端标识
	 * @param originalAreaId 区域标识
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return List< LcTerminalAreaDBEntity>
	 */
	public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(
			long terminalId, int originalAreaId, long start, long end);
}
