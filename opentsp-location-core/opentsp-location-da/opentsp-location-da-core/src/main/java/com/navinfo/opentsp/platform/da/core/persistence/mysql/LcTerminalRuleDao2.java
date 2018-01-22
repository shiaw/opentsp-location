package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.DSATerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;

import java.util.List;


public interface LcTerminalRuleDao2 extends BaseDao<LcTerminalRuleDBEntity>{

	public int saveRegularData(RegularData regularData) throws Exception;
	/**
	 * 根据终端标识查找规则
	 * @param terminalId
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int currentPage,int pageSize)throws Exception;
	/**
	 * 根据终端标识和规则编码查找
	 * @param terminalId
	 * @param ruleCode
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularData1(long terminalId,int ruleCode)throws Exception;


	/**
	 * 根据规则 终端标识、区域标识、规则编码查找
	 * @param terminalId
	 * @param areaId
	 * @param areaId
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int areaId)throws Exception;
	public List<LcTerminalRuleDBEntity> getRegularData2(long terminalId,int regularCode,int areaId)throws Exception;
	/**
	 * 根据终端标识和区域标识查找
	 * @param terminalId
	 * @param areaId
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaId(long terminalId,int areaId)throws Exception;
	/**
	 * 根据终端id和区域标识删除规则
	 * @param terminalId
	 * @param areaIdentify
	 * @return
	 */
	public boolean deleteByTermianlIdAndAreaId(long terminalId,int... areaIdentify) throws Exception;

	/**
	 * 根据终端标识和规则编码删除规则
	 * @param terminalId
	 * @param regularCode
	 * @param areaId
	 * @return
	 */
	public boolean deleteRegularData(long terminalId,int regularCode, long areaId)throws Exception ;
	/**
	 * 根据节点标识和服务区域查找s
	 * @param districtCode
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> queryRegularData(Integer districtCode,
			int... node_code)throws Exception;

	List<DSATerminalRuleDBEntity> getRegularDataForDsa(long[] terminalId)throws Exception;

	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			long terminalId, int[] areaIds)throws Exception;
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			List<Long> areaIds)throws Exception;
	public List<LcTerminalRuleDBEntity> getRegularDataByTerminalIds(
			List<Long> terminalId)throws Exception;

	public int ruleCount(long terminalId,long areaId)throws Exception;

	public List<LcTerminalRuleDBEntity> getRuleByType(int type);
	public List<LcTerminalRuleDBEntity> getRuleByCode(int type);

}
