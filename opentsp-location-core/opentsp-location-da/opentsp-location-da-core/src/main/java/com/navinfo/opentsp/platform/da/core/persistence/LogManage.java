package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.*;

import java.util.List;



public interface LogManage {
	/**
	 * 服务日志
	 * @param lcServiceLog {@link:LcServiceLog 服务日志}
	 */
	public void serverLogSave(LcServiceLogDBEntity lcServiceLog);

	/**
	 * 节点日志
	 * @param lcNodeLog {@link:LcNodeLog 节点日志}
	 */
	public void nodeLogSave(LcNodeLogDBEntity lcNodeLog);

	/**
	 * 终端操作日志存储
	 * @param terminalOperationLog {@link:LcTerminalOperationLog 终端操作日志实体类}
	 */
	public void terminalOperateLogSave(LcTerminalOperationLogDBEntity terminalOperationLog);

	/**
	 * 终端操作状态更新
	 * @param terminalId {@link:Long 终端标识}
	 * @param messageCod {@link:Integer 终端指令编码}
	 * @param results {@link:Boolean 操作状态}
	 */
	public void terminalOperateLogUpdate(long terminalId,int messageCod,boolean results);

	/**
	 * 根据参数查询规则日志
	 *
	 * @param terminalId
	 *            终端标识
	 * @param ruleCode
	 *            规则编码
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return List< LcTerminalRuleDBEntity>
	 */
	public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(long terminalId,int ruleCode, long start,long end);



	/**
	 * 根据终端id和规则id查找规则数据日志具体信息
	 * @param terminalId
	 * @param ruleId
	 * @return
	 */
	public LcTerminalRuleLogDBEntity queryTerminalRuleLogById(int ruleId,long terminalId);

	/**
	 * 根据参数查询区域日志
	 *
	 * @param terminalId
	 *            终端标识
	 * @param originalAreaId
	 *            区域标识
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return List< LcTerminalAreaDBEntity>
	 */
	public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(long terminalId,int originalAreaId,long start,long end);

	/**
	 * 根据区域ID查询区域数据
	 *
	 * @param taId
	 *            区域ID
	 * @return
	 */
	public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(long terminalId,int taId);

	/**
	 * 根据参数查询服务日志
	 *
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @param logDistrict
	 *            服务区域编码
	 * @param logIp
	 *            IP地址
	 * @param logContent
	 *            服务状态
	 * @return
	 */
	public List<LcServiceLogDBEntity> queryServiceLogList(long start,long end,int logDistrict,String logIp,int typeCode,int currentPage,int pageSize);

	public int queryServiceLogCount(long start,long end,int logDistrict,String logIp,int typeCode);

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
	public List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLogList(long terminalId,int switchType,long start,long end);

	/**
	 * 根据参数节点日志
	 *
	 * @param nodeCode
	 *            节点标识
	 * @param logDistrict
	 *            服务区域编码
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return List< LcNodeLogDBEntity>
	 */
	public List<LcNodeLogDBEntity> queryNodeLogList(int nodeCode,long start,long end,int logDistrict,int currentPage,int pageSize);

	public int queryNodeLogCount(int nodeCode,long start,long end,int logDistrict);

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
	public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(long terminalId,int operatinoType,long start,long end,int currentPage,int pageSize );
	/**
	 * 根据主键ID查询终端操作信息
	 * @param tol_id
	 * @return
	 */
	public LcTerminalOperationLogDBEntity queryTerminalOperationLogById(int tol_id,long terminalId);

	/**
	 * 保存信息播报日志
	 * @param entity
	 */
	public boolean saveMessageBroadcastLog(LcMessageBroadcastLogDBEntity entity);

	/**
	 * 查询信息播报日志
	 * @param terminalId
	 * @param seviceStationId
	 * @param start
	 * @param end
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LcMessageBroadcastLogDBEntity> queryMessageBroadcastLogList(
			long terminalId,
			int areaId,
			long start,
			long end,
			int currentPage,
			int pageSize);
	/**
	 * 保存信息播报日志
	 * @param entity
	 */
	public boolean saveOutRegionLimitSpeedLog(LcOutRegionLimitSpeedLogDBEntity entity);

	/**
	 * 查询信息播报日志
	 * @param terminalId
	 * @param seviceStationId
	 * @param start
	 * @param end
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public List<LcOutRegionLimitSpeedLogDBEntity> queryOutRegionLimitSpeedLogList(
			long terminalId,
			int areaId,
			long start,
			long end,
			int currentPage,
			int pageSize);
	/**
	 * 查询总数
	 * @param terminalId
	 * @param areaId
	 * @param start
	 * @param end
	 * @return
	 */
	public int getCount(long terminalId,int areaId,long start,long end);
}
