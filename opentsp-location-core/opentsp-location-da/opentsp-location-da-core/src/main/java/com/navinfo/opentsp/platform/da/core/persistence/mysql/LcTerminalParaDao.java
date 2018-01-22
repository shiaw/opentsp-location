package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalParaDBEntity;

import java.util.List;


public interface LcTerminalParaDao extends BaseDao<LcTerminalParaDBEntity> {
	/**
	 * 根据终端标识查找
	 * @param terminalId
	 * @return
	 */
	public List<LcTerminalParaDBEntity> getByTerminalId(long terminalId);

	/**
	 * 根据指令号查找
	 * @param para_code
	 * @return
	 */
	public List<LcTerminalParaDBEntity> getByCommandCode(int... para_code);

	/**
	 * 根据终端标识和终端指令删除
	 * @param terminalId
	 * @param para_code
	 * @return
	 */
	public int deleteByTerminalInfo(long terminalId,int para_code);
	/**
	 * 查询终端参数
	 *
	 * @param terminalId
	 * @param paramterCode
	 *            {@link Integer}终端参数编码
	 * @return {@link List}<{@link LcTerminalParaDBEntity}>
	 */
	public List<LcTerminalParaDBEntity> getByTerminalParameter(long terminalId,int... paramterCode);

	/**
	 * 根据终端标识和终端指令名称查找终端参数信息
	 * @param terminalId 终端标识
	 * @param command 指令
	 * @return
	 */
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(long terminalId,
															 int commandName,int currentPage,int pageSize);

	List<LcTerminalParaDBEntity> queryTerminalParaResForDsa(long[] terminalIds);

}
