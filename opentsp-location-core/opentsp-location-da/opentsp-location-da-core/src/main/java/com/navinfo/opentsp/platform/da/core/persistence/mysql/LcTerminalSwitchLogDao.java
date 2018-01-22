package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalInfoModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalOperationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalSwitchLogDBEntity;

import java.util.List;


public interface LcTerminalSwitchLogDao extends
		BaseDao<LcTerminalSwitchLogDBEntity> {
	/**
	 * 终端操作日志存储
	 *
	 * @param terminalOperationLog
	 *            {@link LcTerminalOperationLogDBEntity}
	 */
	abstract void terminalOperateLogSave(
			LcTerminalOperationLogDBEntity terminalOperationLog);

	/**
	 * 终端操作日志更新
	 *
	 * @param terminalId
	 *            {@link Long} 终端ID
	 * @param messageCod
	 *            {@link Integer} 日志编码
	 * @param results
	 *            {@link Bool} 操作状态
	 */
	abstract void terminalOperateLogUpdate(long terminalId, int messageCod,
										   boolean results);

	/**
	 * 终端在线状态查询
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	abstract TerminalInfoModel queryTerminalInfoRes(long terminalId);

	/**
	 * 终端在线日志查询
	 *
	 * @param terminalId
	 *            终端标识
	 * @param switchType
	 *            终端状态
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	abstract List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLog(
			long terminalId, int switchType, long start, long end);

}
