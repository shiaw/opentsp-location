package com.navinfo.opentsp.platform.da.core.webService.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcNodeConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceConfigDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;

@WebService
public interface ConfigWebService {
    /**
     * 查询服务配置信息记录数
     * @param userName
     * @param ip
     * @param district
     * @param start
     * @param end
     * @param flag
     * @return
     */
    public int queryServiceConfigResCount(
            @WebParam(name = "userName", mode = Mode.IN) String userName,
            @WebParam(name = "ip", mode = Mode.IN) String ip,
            @WebParam(name = "district", mode = Mode.IN) int district,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "flag", mode = Mode.IN) int flag);
    /**
     * 服务配置信息查询
     *
     * @param userName
     *            用户名
     * @param ip
     *            ip地址
     * @param district
     *            服务区域
     * @param start
     *            开始时间
     * @param end
     *            结束时间
     * @return
     */
    public List<LcServiceConfigDBEntity> queryServiceConfigRes(
            @WebParam(name = "userName", mode = Mode.IN) String userName,
            @WebParam(name = "ip", mode = Mode.IN) String ip,
            @WebParam(name = "district", mode = Mode.IN) int district,
            @WebParam(name = "start", mode = Mode.IN) long start,
            @WebParam(name = "end", mode = Mode.IN) long end,
            @WebParam(name = "flag", mode = Mode.IN) int flag,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);

    /**
     * 新增和修改服务配置信息
     *
     * @param serviceConfig
     *            服务配置
     * @param district
     *            服务所属分区
     * @return
     */
    public PlatformResponseResult saveOrUpdateServiceConfig(
            @WebParam(name = "serviceConfig", mode = Mode.IN) LcServiceConfigDBEntity serviceConfig,
            @WebParam(name = "district", mode = Mode.IN) int[] district,
            @WebParam(name = "authName", mode = Mode.IN) String authName);

    /**
     * 删除服务配置信息
     *
     * @param primaryKeys
     *            主键
     * @return
     */
    public PlatformResponseResult deleteServiceConfig(int[] primaryKeys);

    /**
     * 根据用户名删除服务配置
     *
     * @param authName
     *            用户名
     * @return
     */
    public PlatformResponseResult deleteServiceConfigByAuthName(String authName);

    /**
     * 节点配置信息查询
     *
     * @param district
     *            服务区域编码
     * @param nodeCode
     *            节点编号
     * @return
     */
    public List<LcNodeConfigDBEntity> queryNodeConfigRes(
            @WebParam(name = "district", mode = Mode.IN) int district,
            @WebParam(name = "nodeCode", mode = Mode.IN) int nodeCode,
            @WebParam(name = "nodeType", mode = Mode.IN) int nodeType,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize,
            @WebParam(name = "nodetype", mode = Mode.IN) int nodetype);
    /**
     * 查询节点配置信息记录数
     *
     * @param district
     *            服务区域编码
     * @param nodeCode
     *            节点编号
     * @return
     */
    public int queryNodeConfigResCount(
            @WebParam(name = "district", mode = Mode.IN) int district,
            @WebParam(name = "nodeCode", mode = Mode.IN) int nodeCode,
            @WebParam(name = "nodetype", mode = Mode.IN) int nodetype);

    /**
     * 新增和修改节点配置信息
     *
     * @param nodeConfig
     *            节点配置
     * @return
     */
    public PlatformResponseResult saveOrUpdateNodeConfig(
            @WebParam(name = "nodeConfig", mode = Mode.IN) LcNodeConfigDBEntity nodeConfig);

    /**
     * 删除节点配置信息
     *
     * @param primaryKeys
     *            主键
     * @return
     */
    public PlatformResponseResult deleteNodeConfig(int[] primaryKeys);

    /**
     * 根据用节点标识删除节点配置
     *
     * @param nodeCode
     *            节点标识
     * @return
     */
    public PlatformResponseResult deleteNodeConfigByNodeCode(int nodeCode);

    /**
     * 根据主键查询服务配置信息
     * @param sc_id 主键
     * @return
     */
    public LcServiceConfigDBEntity queryServiceConfigById(int sc_id);

    /**
     * 查询服务所在分区
     * @param sc_id 服务配置主键
     * @return
     */
    public List<LcServiceDistrictConfigDBEntity> queryServiceInDistrict(int sc_id);
    /**
     * 根据主键查询节点信息
     * @param nc_id
     * @return
     */
    public LcNodeConfigDBEntity queryNodeConfigById(	@WebParam(name = "nc_id", mode = Mode.IN)int nc_id);

}
