package com.navinfo.opentsp.platform.da.core.webService.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMessageBroadcastLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcOutRegionLimitSpeedLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaDataLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalAreaLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalOperationLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalRuleLogDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalSwitchLogDBEntity;

@WebService
public interface LogWebService {

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
    public List<LcTerminalRuleLogDBEntity> queryTerminalRuleLogList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "ruleCode", mode = Mode.IN) int ruleCode,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end);



    /**
     * 根据终端id和规则id查找规则数据日志具体信息
     * @param terminalId
     * @param ruleId
     * @return
     */
    public LcTerminalRuleLogDBEntity queryTerminalRuleLogById(
            @WebParam(name = "terminalId", mode = Mode.IN) int ruleId,
            @WebParam(name = "ruleId", mode = Mode.IN)long terminalId);

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
    public List<LcTerminalAreaLogDBEntity> queryTerminalAreaLogList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "originalAreaId", mode = Mode.IN) int originalAreaId,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end);

    /**
     * 根据区域ID查询区域数据
     *
     * @param taId
     *            区域ID
     * @return
     */
    public List<LcTerminalAreaDataLogDBEntity> queryTerminalAreaDataList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "taId", mode = Mode.IN) int taId);

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
    public List<LcServiceLogDBEntity> queryServiceLogList(
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "logDistrict", mode = Mode.IN) int logDistrict,
            @WebParam(name = "logIp", mode = Mode.IN) String logIp,
            @WebParam(name = "typeCode", mode = Mode.IN) int typeCode,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
    /**
     * 根据查询条件查询记录总数
     * @param start
     * @param end
     * @param logDistrict
     * @param logIp
     * @param typeCode
     * @return
     */
    public int queryServiceLogCount(
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "logDistrict", mode = Mode.IN) int logDistrict,
            @WebParam(name = "logIp", mode = Mode.IN) String logIp,
            @WebParam(name = "typeCode", mode = Mode.IN) int typeCode);

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
    public List<LcTerminalSwitchLogDBEntity> queryTerminalSwitchLogList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "switchType", mode = Mode.IN) int switchType,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end);

    /**
     * 节点日志记录数量
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
    public int queryNodeLogCount(
            @WebParam(name = "nodeCode", mode = Mode.IN) int nodeCode,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "logDistrict", mode = Mode.IN) int logDistrict);
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
    public List<LcNodeLogDBEntity> queryNodeLogList(
            @WebParam(name = "nodeCode", mode = Mode.IN) int nodeCode,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "logDistrict", mode = Mode.IN) int logDistrict,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

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
    public List<LcTerminalOperationLogDBEntity> queryTerminalOperationLogList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "operatinoType", mode = Mode.IN) int operatinoType,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "currentPage",mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize",mode = Mode.IN) int pageSize);
    /**
     * 根据主键ID查询终端操作信息
     * @param tol_id
     * @return
     */
    public LcTerminalOperationLogDBEntity queryTerminalOperationLogById(@WebParam(name = "tol_id", mode = Mode.IN) int tol_id,@WebParam(name = "terminalId", mode = Mode.IN)  long terminalId);

    /**
     * 查询信息播报日志
     * @param terminalId
     * @param areaId
     * @param start 开始时间
     * @param end 结束时间
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<LcMessageBroadcastLogDBEntity> queryMessageBroadcastLogList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "areaId", mode = Mode.IN) int areaId,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "end", mode = Mode.IN) int currentPage,
            @WebParam(name = "end", mode = Mode.IN) int pageSize);
    /**
     * 查询出区域限速日志
     * @param terminalId
     * @param areaId
     * @param start 开始时间
     * @param end 结束时间
     * @param currentPage
     * @param pageSize
     * @return
     */
    public List<LcOutRegionLimitSpeedLogDBEntity> queryOutRegionLimitSpeedLogList(
            @WebParam(name = "terminalId", mode = Mode.IN) long terminalId,
            @WebParam(name = "areaId", mode = Mode.IN) int areaId,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
}
