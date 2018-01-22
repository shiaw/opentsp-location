package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalOnlineModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalParaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRegisterDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import java.util.List;
import java.util.Map;



public interface TerminalInfoManage {
	/**
	 * 查询终端列表
	 *
	 * @param districtCode
	 * @return {@link List}<{@link Map}> Key<br>
	 *         terminalInfo=终端信息-->{@link LcTerminalInfoDBEntity}<br>
	 *         terminalRegisterInfo=终端注册信息-->{@link LcTerminalRegisterDBEntity}
	 */
	abstract List<Map<String, Object>> getTerminalInfos(int districtCode);

	/**
	 * 更新终端所在TA节点信息
	 *
	 * @param terminalId
	 * @param districtCode
	 * @param node_code
	 * @return
	 */
	abstract OptResult updateTerminalInfo(long terminalId, int districtCode,
										  long node_code,long changeTid);

	/**
	 * 终端注册信息存储
	 *
	 * @param terminalRegister
	 *            {@link LcTerminalRegisterDBEntity}
	 * @return
	 */
	abstract OptResult saveTerminalRegister(
			LcTerminalRegisterDBEntity terminalRegister);

	/**
	 * 查询终端注册信息
	 *
	 * @param terminalId
	 * @return {@link LcTerminalRegisterDBEntity}
	 */
	abstract LcTerminalRegisterDBEntity queryTerminalRegister(long terminalId);

	/**
	 * 参数存储
	 *
	 * @param terminalId
	 * @param parameterCode
	 * @param content
	 * @return
	 */
	abstract OptResult saveOrUpdateTerminalParameter(
			LcTerminalParaDBEntity terminalPara);



	/**
	 * 新增和修改终端信息
	 *
	 * @param terminalInfo
	 */
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			LcTerminalInfoDBEntity terminalInfo);

	/**
	 * 新增和修改终端信息
	 *
	 * @param terminalInfo
	 * @param isAdd true新增；false修改
	 */
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			LcTerminalInfoDBEntity terminalInfo,boolean isAdd);

	/**
	 * 删除终端参数
	 *
	 * @param terminalId
	 * @param parameterCode
	 * @return
	 */
	abstract OptResult deleteTerminalParameter(long terminalId,
											   int parameterCode);

	/**
	 * 查询终端参数
	 *
	 * @param terminalId
	 * @param paramterCode
	 *            {@link Integer}终端参数编码
	 * @return {@link List}<{@link LcTerminalParaDBEntity}>
	 */
	abstract List<LcTerminalParaDBEntity> getTerminalParameter(long terminalId,
															   int... paramterCode);

	/**
	 * 根据主键删除终端信息，支持批量删除
	 *
	 * @param primaryKeys
	 *            主键
	 * @return
	 */
	public PlatformResponseResult deleteTerminalInfo(long[] primaryKeys);

	/**
	 * 根据终端id获得终端信息
	 *
	 * @param terminalId
	 * @return
	 */
	public LcTerminalInfoDBEntity queryTerminalInfo(long terminalId);

	/**
	 * 终端参数查询
	 *
	 * @param terminalId
	 *            终端标识
	 * @param command
	 *            指令
	 * @return
	 */
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(long terminalId,
															 int command,int currentPage,int pageSize);

	/**
	 * 查询终端信息
	 *
	 * @param terminalId
	 *            终端标识
	 * @param protoTypeName
	 *            协议类型名称
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public List<LcTerminalInfoDBEntity> queryTerminalInfoRes(long terminalId,
															 int protoType, String deviceId,long changeId,long start, long end,int flag,int currentPage,int pageSize);
	//查看终端信息 记录数
	public int queryTerminalInfoResCount(long terminalId,
										 int protoType,String deviceId,long changeId, long start, long end);

	/**
	 * 根据终端标识查询终端在线状态
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	public TerminalOnlineModel queryTerminalOnLineRes(long terminalId);
	/**
	 * 多媒体参数查询
	 *
	 * @param terminalId
	 *            终端标识
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public List<LcMultimediaParaDBEntity> queryMediaParaRes(long terminalId,long start, long end,int currentPage,int pageSize);
	/**
	 * 批量添加终端
	 * @param list
	 * @return
	 */
	public PlatformResponseResult batchAddTerminalInfo( List<LcTerminalInfoDBEntity> list);

	public	PlatformResponseResult saveOrUpdateTerminalInfoForSyn(
			LcTerminalInfoDBEntity terminalInfo);
	public	PlatformResponseResult saveOrUpdateTerminalInfoForSyn(
			LcTerminalInfoDBEntity terminalInfo,boolean isAdd);

	public PlatformResponseResult deleteTerminalInfoForSyn(long[] primaryKeys);
	public LcTerminalInfoDBEntity queryTerminalInfoById(long terminalId);
}
