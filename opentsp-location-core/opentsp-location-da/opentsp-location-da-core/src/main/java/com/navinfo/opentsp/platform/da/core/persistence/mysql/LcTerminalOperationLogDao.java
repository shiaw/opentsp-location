package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalOperationLogDBEntity;

import java.util.List;


public interface LcTerminalOperationLogDao extends
		BaseDao<LcTerminalOperationLogDBEntity> {
	/**
	 * 终端操作状态更新
	 * */
	public boolean terminalOperateLogUpdate(long terminalId, int messageCod,
											boolean results);

	/**
	 * 终端操作日志查询
	 *
	 * @param terminalId
	 *            终端标识
	 * @param operatinoType
	 *            终端指令编码名称
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(
			long terminalId, int operatinoType, long start, long end,int currentPage,int pageSize);
}
