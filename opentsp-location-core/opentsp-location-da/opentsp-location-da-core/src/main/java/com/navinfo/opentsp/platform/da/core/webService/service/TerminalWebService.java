package com.navinfo.opentsp.platform.da.core.webService.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

import  com.navinfo.opentsp.platform.da.core.persistence.entity.extend.MediaFileModel;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.extend.TerminalOnlineModel;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalParaDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRegisterDBEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

@WebService
public interface TerminalWebService {

	/**
	 * 终端参数查询
	 *
	 * @param terminalId
	 *            终端标识
	 * @param command
	 *            指令
	 * @return
	 */
	public List<LcTerminalParaDBEntity> queryTerminalParaRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "command", mode = Mode.IN) int command,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

	/**
	 * 新增和修改终端信息
	 *
	 * @param terminalInfo
	 */
	public PlatformResponseResult saveOrUpdateTerminalInfo(
			@WebParam(name = "terminalInfo", mode = Mode.IN) LcTerminalInfoDBEntity terminalInfo);

	public PlatformResponseResult saveOrUpdateTerminalInfo2(
			@WebParam(name = "terminalInfo", mode = Mode.IN) LcTerminalInfoDBEntity terminalInfo,
			@WebParam(name = "isAdd", mode = Mode.IN) boolean isAdd);

	/**
	 * 查询终端信息
	 *
	 * @param terminalId
	 *            终端标识
	 * @param protoType
	 *            协议类型名称
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public List<LcTerminalInfoDBEntity> queryTerminalInfoRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "protoType", mode = Mode.IN) int protoType,
			@WebParam(name = "deviceId", mode = Mode.IN) String deviceId,
			@WebParam(name = "changeId", mode = Mode.IN) long changeId,
			@WebParam(name = "start", mode = Mode.IN) long start,
			@WebParam(name = "end", mode = Mode.IN) long end,
			@WebParam(name = "flag", mode = Mode.IN) int flag,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
	/**
	 * 查询终端信息 总记录数
	 *
	 * @param terminalId
	 *            终端标识
	 * @param protoType
	 *            协议类型名称
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return
	 */
	public int queryTerminalInfoResCount(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "protoType", mode = Mode.IN) int protoType,
			@WebParam(name = "deviceId", mode = Mode.IN) String deviceId,
			@WebParam(name = "changeId", mode = Mode.IN) long changeId,
			@WebParam(name = "start", mode = Mode.IN) long start,
			@WebParam(name = "end", mode = Mode.IN) long end);
	/**
	 * 根据主键查找终端信息
	 *
	 * @param primaryKey
	 *            主键
	 * @return
	 */
	public LcTerminalInfoDBEntity queryTerminalInfoResById(
			@WebParam(name = "primaryKey", mode = Mode.IN) long primaryKey);

	/**
	 * 根据主键删除终端信息，支持批量删除
	 *
	 * @param primaryKeys
	 *            主键
	 * @return
	 */
	public PlatformResponseResult deleteTerminalInfo(
			@WebParam(name = "primaryKeys", mode = Mode.IN) long[] primaryKeys);

	/**
	 * 根据终端标识删除终端信息，此方法给同步数据用
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	public PlatformResponseResult deleteTerminalInfoByTerminalId(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId);

	/**
	 * 根据终端标识查询注册信息
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	public LcTerminalRegisterDBEntity queryTerminalRegisterRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId);

	/**
	 * 根据终端标识查询规则
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	public List<LcTerminalRuleDBEntity> queryTerminalRuleRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

	/**
	 * 根据终端标识查询终端在线状态
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	public TerminalOnlineModel queryTerminalOnLineRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId);

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
	public List<LcMultimediaParaDBEntity> queryMediaParaRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "start", mode = Mode.IN) long start,
			@WebParam(name = "end", mode = Mode.IN) long end,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

	/**
	 * 多媒体文件查询
	 *
	 * @param fileCode
	 * @param terminalId
	 * @return
	 */
	public MediaFileModel queryMediaFileRes(
			@WebParam(name = "fileCode", mode = Mode.IN) String fileCode,
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId);

	/**
	 * 根据参数查询区域
	 *
	 * @param terminalId
	 *            终端标识
	 * @param areaId
	 *            区域标识
	 * @param start
	 *            开始时间
	 * @param end
	 *            结束时间
	 * @return List< LcTerminalAreaDBEntity>
	 */
	public List<LcTerminalAreaDBEntity> queryTerminalAreaRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "areaId", mode = Mode.IN) int areaId,
			@WebParam(name = "start", mode = Mode.IN) long start,
			@WebParam(name = "end", mode = Mode.IN) long end,
			@WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
			@WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

	/**
	 * 根据区域ID查询区域数据
	 *
	 * @param terminalId
	 *            终端标识
	 * @param taId
	 *            区域ID
	 *
	 * @return
	 */
	public List<LcTerminalAreaDataDBEntity> queryAreaDataRes(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "taId", mode = Mode.IN) int taId);

	/**
	 * 根据终端标识和规则id查询规则
	 *
	 * @param terminalId
	 *            终端标识
	 * @return
	 */
	public LcTerminalRuleDBEntity queryTerminalRuleById(
			@WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
			@WebParam(name = "trId", mode = Mode.IN) int trId);

	public PlatformResponseResult batchAddTerminalInfo(
			@WebParam(name = "list", mode = Mode.IN) List<LcTerminalInfoDBEntity> list);

	/**
	 * 保存终端注册鉴权
	 * @param terminalRegister
	 * @return 返回保存失败的ID集合
	 */
	public List<Integer> saveTerminalRegister(List<LcTerminalRegisterDBEntity> terminalRegisters);

	public PlatformResponseResult terminalMappingBindOrNot(long mainTerminalId,
														   long secondaryTerminalId, boolean isBinding);
}
