package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service;

import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalInfo.TerminalInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.LcTerminalParaEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.LcTerminalRuleEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.OptResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.OutRegionLimitSpeedQuery;
import java.util.List;
import java.util.Map;




/************
 * 
 * 
 * @author jinshan
 *
 */
public interface TermianlRuleAndParaService {
	/**
	 * 获取所有终端信息接口   重构用
	 * @return
	 * @
	 */
	public List<TerminalInfo> getAllTerminalInfo() ;
		
	
	
    /**
     * 查询多个终端的参数及规则数据
     * 
     * @param terminalId
     * @return
     * @
     */
	public OptResult queryRuleAndPara(List<Long> terminalId) ;

	/**
	 * 查询一个终端定义的所有规则数据
	 * 
	 * @param terminalId
	 * @return
	 * @
	 */
	public List<LcTerminalRuleEntity> queryTerminalRule(long terminalId) ;
	
	/**
	 * 查询一个终端及参数编码对应的参数相信信息
	 * 
	 * @param terminalId
	 * @param paramterCode
	 * @return
	 * @
	 */
	public LcTerminalParaEntity queryTerminalPara(long terminalId, int paramterCode) ;
	
	/**
	 * 根据参数编码查询终端的设置
	 * @param paramterCode
	 * @return
	 * @
	 */
	public Map<String, Object> queryTerminalByParaCode(int... paramterCode) ;
	
	/**
	 * 查询一个终端，在某一个对应的区域内的规则数据
	 * 
	 * @param terminalId
	 * @param areaId
	 * @return
	 * @
	 */
	public List<LcTerminalRuleEntity> queryTerminalRule(List<Long> terminalId, List<Long> areaId, boolean type) ;
	/**
	 * 查询一个终端，在某一个对应的区域列表内的规则数据
	 * 
	 * @param terminalId
	 * @param areaIds
	 * @return
	 * @
	 */
	public List<LcTerminalRuleEntity>  queryTerminalRuleByAreaIds(long terminalId, int[] areaIds) ;
	
	/**
	 * 查询出区域限速日志
	 * @param terminalIds
	 * @param beginDate
	 * @param endDate
	 * @param commonParameter
	 * @return
	 */
	public OutRegionLimitSpeedQuery queryOutRegionLimitSpeedLogList(List<Long> terminalIds,
																	long beginDate, long endDate, CommonParameter commonParameter) throws InstantiationException, IllegalAccessException;

	/**
	 * rmi服务可用性检测
	 * @return
	 * @
	 */
	public boolean isConnected() ;
	/**
	 * 通过字典编码(内部编码)查询字典值
	 * @param dictCode
	 * @return
	 */
	public int getDictByCode(int dictCode) ;
	/**
	 * 获取行政区域和瓦片映射缓存 Map<行政区域ID，瓦片ID>
	 * @return
	 * @
	 */
	public Map<Long,Integer[]> getDistrictAndTileMap() ;
	public Map<Long,Integer[]> getDistrictAndTileMapPage(int pageNum, int pagesize) ;
	/**
	 * 查询服务站相关规则区域,用来做服务站内的车次统计。
	 * @return
	 * @
	 */
	public List<AreaInfo> getAreaInfoForStatistic() ;
	
	/**
	 * 查询服务站滞留阀值
	 * @param code
	 * @return
	 * @
	 */
	public Map<Long, Integer> getRegularDatasByRegularCode(RegularCode code) ;
	
	/**
	 * 查询超时停车规则
	 * @return
	 * @
	 */
	public Map<Long, Long> getAlarmRule() ;
	
	public boolean AlarmCancleOrNot(long tid, long ruleid, boolean cancleorNot) ;
}
