package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.DSATerminalRuleDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCRegularData.RegularData;

import java.util.List;


public interface LcTerminalRuleDao extends BaseDao<LcTerminalRuleDBEntity>{

	public boolean batchSave(List<RegularData> regularDatas);
	/**
	 * 根据终端标识查找规则
	 * @param terminalId
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int currentPage,int pageSize);
	/**
	 * 根据终端标识和规则编码查找
	 * @param terminalId
	 * @param ruleCode
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularData1(long terminalId,int ruleCode);

	/**
	 * 根据规则 终端标识、区域标识、规则编码查找
	 * @param terminalId
	 * @param areaId
	 * @param ruleCode
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularData(long terminalId,int areaId);
	/**
	 * 根据终端标识和区域标识查找
	 * @param terminalId
	 * @param ruleCode
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaId(long terminalId,int areaId);
	/**
	 * 根据终端id和区域标识删除规则
	 * @param terminalId
	 * @param areaIdentify
	 * @return
	 */
	public boolean deleteByTermianlIdAndAreaId(long terminalId,int... areaIdentify);

	/**
	 * 根据终端标识和规则编码删除规则
	 * @param terminalId
	 * @param regularCode
	 * @param regularIdentifyList
	 * @return
	 */
	public boolean deleteRegularData(long terminalId,int regularCode, long areaId);
	/**
	 * 根据节点标识和服务区域查找s
	 * @param node_code
	 * @param district
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> queryRegularData(Integer districtCode,
														 int... node_code);

	List<DSATerminalRuleDBEntity> getRegularDataForDsa(long[] terminalId);

	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			long terminalId, int[] areaIds);
	public List<LcTerminalRuleDBEntity> getRegularDataByAreaIds(
			List<Long> areaIds);
	public List<LcTerminalRuleDBEntity> getRegularDataByTerminalIds(
			List<Long> terminalId);

	public int ruleCount(long terminalId,long areaId);
}
