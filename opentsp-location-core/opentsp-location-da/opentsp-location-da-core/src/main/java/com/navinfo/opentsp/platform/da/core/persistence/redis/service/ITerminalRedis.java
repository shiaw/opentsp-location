package com.navinfo.opentsp.platform.da.core.persistence.redis.service;

import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.RDTerminalStatus;

import java.util.List;


public interface ITerminalRedis {
	/**
	 * 添加终端状态
	 *
	 * @param status
	 */
	abstract void addTerminalStatus(RDTerminalStatus status);

	/**
	 * 添加终端状态
	 *
	 * @param status
	 */
	abstract void addTerminalStatus(RDTerminalStatus... status);
	/**
	 * 更新终端状态
	 * @param status
	 */
	abstract void updateTerminalStatus(RDTerminalStatus status);

	/**
	 * 查询终端状态
	 *
	 * @param terminalId
	 * @return
	 */
	abstract RDTerminalStatus findTerminalStatus(long terminalId);

	/**
	 * 查询终端状态
	 *
	 * @param terminalId
	 * @return
	 */
	abstract List<RDTerminalStatus> findTerminalStatus(long... terminalIds);

	/**
	 * 删除终端状态
	 *
	 * @param terminalId
	 * @return
	 */
	abstract void delTerminalStatus(long terminalId);
}
