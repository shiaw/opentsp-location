package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;

import java.util.List;



public interface LcTerminalProtoMappingDao extends BaseDao<LcTerminalProtoMappingDBEntity> {
	/**
	 * 根据终端协议编码查找协议指令
	 * @param logic_code
	 * @return
	 */
	public List<LcTerminalProtoMappingDBEntity> getByLogicCode(int logic_code);
	/**
	 * 根据协议删除终端指令协议映射信息
	 * @param logicCode
	 * @return
	 */
	public void deleteProtoMapping(int logicCode);
	/**
	 * 根据终端协议编码查找协议指令
	 * @param logic_code
	 * @return
	 */
	public List<LcTerminalProtoMappingDBEntity> getByGroupLogicCode( int logic_code);
	/**
	 *
	 * @param logic_code
	 * @param dictCode
	 * @param messageCode
	 */
	public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(int logic_code, int dictCode,int messageCode,int currentPage,int pageSize);
	public int selectPMListCount(int logic_code, int dictCode,int messageCode);

	public void deleteProtoMapping(int[] tplId, int messageCode);

}