package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRegisterDBEntity;

import java.sql.SQLException;


public interface LcTerminalRegisterDao extends BaseDao<LcTerminalRegisterDBEntity>{
	/**
	 * 根据终端id查找
	 * @param terminalId
	 * @return
	 */
	public LcTerminalRegisterDBEntity getByTerminalId(long terminalId);

	/**
	 * 根据鉴权码查找
	 * @param auth_code
	 * @return
	 */
	public LcTerminalRegisterDBEntity getByAuthCode(String auth_code);

	/**
	 * 根据终端id修改注册信息
	 * @param terminalRegister
	 * @return
	 */
	public int updateByTerminalId(LcTerminalRegisterDBEntity terminalRegister)throws SQLException;


}
