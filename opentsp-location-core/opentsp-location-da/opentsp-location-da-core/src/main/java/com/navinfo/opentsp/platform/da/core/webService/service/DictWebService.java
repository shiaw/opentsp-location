package com.navinfo.opentsp.platform.da.core.webService.service;

import java.util.List;

import javax.jws.WebParam;
import javax.jws.WebParam.Mode;
import javax.jws.WebService;

import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;

@WebService
public interface DictWebService {
    /**
     * 根据参数查询字典信息，无参数为查询所有字典信息
     *
     * @param dtName
     *            字典类型名称
     * @param dictId
     *            协议Id
     * @return List<LcDictDBEntity> 字典数据
     */
    public List<LcDictDBEntity> queryDictList(
            @WebParam(name = "userName", mode = Mode.IN) String dtName,
            @WebParam(name = "dictId", mode = Mode.IN) int dictId);
    public List<LcDictTypeDBEntity> queryDictTypeInfo(
            @WebParam(name = "userName", mode = Mode.IN) String dtName,
            @WebParam(name = "dictTypeId", mode = Mode.IN) int dictTypeId);
    /**
     * 新增（修改字典）
     *
     * @param lcDictDBEntity
     *            字典实体
     * @return 枚举
     */
    public PlatformResponseResult saveOrUpdateDict(
            @WebParam(name = "lcDictDBEntity", mode = Mode.IN) LcDictDBEntity lcDictDBEntity);

    /**
     * 删除字典（支持批量删除）
     *
     * @param dictId
     *            字典Id
     * @return 枚举
     */
    public PlatformResponseResult deleteDict(
            @WebParam(name = "dictCode", mode = Mode.IN) int[] dictCode);

    /**
     * 新增（修改字典类型）
     *
     * @param lcDictDBEntity
     *            字典实体
     * @return 枚举
     */
    public PlatformResponseResult saveOrUpdateDictType(
            @WebParam(name = "lcDictTypeDBEntity", mode = Mode.IN) LcDictTypeDBEntity lcDictTypeDBEntity);

    /**
     * 删除字典类型（支持批量删除）
     *
     * @param dictId
     *            字典Id
     * @return 枚举
     */
    public PlatformResponseResult deleteDictType(
            @WebParam(name = "dtId", mode = Mode.IN) int[] dtId);

    /**
     * 根据参数查询终端指令协议映射信息
     *
     * @param dictCodeName
     *            协议类型code
     * @return 实体对象
     */
    public List<LcTerminalProtoMappingDBEntity> getByLogicCode(
            @WebParam(name = "logic_code", mode = Mode.IN) int logic_code);
    /**
     * 根据参数查询终端指令协议映射信息
     *
     * @param dictCodeName
     *            协议类型code
     * @return 实体对象
     */
    public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(
            @WebParam(name = "logicCode", mode = Mode.IN) int logicCode,
            @WebParam(name = "dictCode", mode = Mode.IN) int dictCode,
            @WebParam(name = "messageCode", mode = Mode.IN) int messageCode,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
    /**
     * 根据参数查询终端指令协议映射信息 总记录数
     *
     * @param dictCodeName
     *            协议类型code
     * @return 实体对象
     */
    public int selectPMListCount(
            @WebParam(name = "logicCode", mode = Mode.IN) int logicCode,
            @WebParam(name = "dictCode", mode = Mode.IN) int dictCode,
            @WebParam(name = "messageCode", mode = Mode.IN) int messageCode);
    /**
     *
     * @param lcDictDBEntity
     * @return
     */
    public PlatformResponseResult saveOrUpdateTerminalProtoMapping(
            @WebParam(name = "lcTerminalProtoMapping", mode = Mode.IN) LcTerminalProtoMappingDBEntity lcTerminalProtoMapping);

    /**
     * 根据参数查询终端指令协议映射信息(分组查询)
     *
     * @param dictCodeName
     *            协议类型code
     * @return 实体对象
     */
    public List<LcTerminalProtoMappingDBEntity> getByGroupLogicCode(
            @WebParam(name = "logic_code", mode = Mode.IN) int logic_code);

    /**
     * 保存终端指令协议映射前删除此协议类型下以前原有映射关系
     *
     * @param logicCode
     *            终端协议
     * @param dictCode
     *            终端指令
     * @return
     */
    public PlatformResponseResult bindProtoMapping(
            @WebParam(name = "logicCode", mode = Mode.IN) int logicCode,
            @WebParam(name = "dictCode", mode = Mode.IN) int[] dictCode);
    /**
     * 根据主键ID删除终端指令映射
     *
     * @param tplId
     * 		 终端指令
     * @return
     */
    public PlatformResponseResult deleteTerminalProtoMapping(
            @WebParam(name = "tplId", mode = Mode.IN) int[] tplId,
            @WebParam(name = "messageCode", mode = Mode.IN) int messageCode);
    /**
     * 根据主键ID删除终端指令映射
     *
     * @param tplId
     * 		 终端指令
     * @return
     */
    public PlatformResponseResult deleteProtoMapping(
            @WebParam(name = "tplId", mode = Mode.IN) int[] tplId);
    /**
     * 根据协议类型删除终端指令映射
     *
     * @param tplId
     * 		 终端指令
     * @return
     */
    public PlatformResponseResult deleteProtoMappingByDictcode(
            @WebParam(name = "logic_code", mode = Mode.IN) int[] logic_code);

    /**
     * 根据参数查询行政区划信息
     *
     * @param dtName
     *            字典类型名称
     * @param dictId
     *            协议Id
     * @return List<LcDictDBEntity> 字典数据
     */
    public List<LcDistrictDBEntity> queryDistrictList(
            @WebParam(name = "dictCodeName", mode = Mode.IN) String dictCodeName);

    /**
     * 新增（修改行政区划）
     *
     * @param lcDictDBEntity
     *            字典实体
     * @return 枚举
     */
    public PlatformResponseResult bindDistrict(
            @WebParam(name = "parentCode", mode = Mode.IN) int parentCode,
            @WebParam(name = "dictCode", mode = Mode.IN) int[] dictCode);

    /**
     * 删除行政区划（支持批量删除）
     *
     * @param dictId
     *            字典Id
     * @return 枚举
     */
    public PlatformResponseResult deleteDistrict(
            @WebParam(name = "districtId", mode = Mode.IN) int[] districtId);
    /**
     * 根据父服务区域编码查询服务区域编码
     * @param parent_code
     * @return
     */
    public List<LcDistrictDBEntity> queryDistrictByParent_code(
            @WebParam(name = "parent_code", mode = Mode.IN) int parent_code,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
    /**
     * 根据父服务区域编码查询服务区域编码
     * @param parent_code
     * @return
     */
    public int queryDistrictCount(
            @WebParam(name = "parent_code", mode = Mode.IN) int parent_code);

    /**
     * 根据字典类型查找字典信息
     * @param dictType 字典类型
     * @return
     */
    public List<LcDictDBEntity> getDictByType(
            @WebParam(name = "dictType", mode = Mode.IN) int dictType,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
    /**
     * 根据字典类型查找字典信息记录数
     * @param dictType 字典类型
     * @return
     */
    public int getDictDataResCount(
            @WebParam(name = "dictType", mode = Mode.IN) int dictType);
    /**
     * 协议绑定更新
     * @param lcTerminalProtoMappingDBEntity
     * @return
     */
    public PlatformResponseResult updateProtocoMapping(List<LcTerminalProtoMappingDBEntity> lcTerminalProtoMappingDBEntity);

    /**
     * 根据参数查询字典类型信息，无参数为查询所有字典类型信息 --记录数
     *
     * @param dtName
     *            字典类型名称
     * @return List<LcDictTypeDBEntity> 字典类型数据
     */
    public int queryDictTypeListCount(
            @WebParam(name = "userName", mode = Mode.IN) String dtName);
    /**
     * 根据参数查询字典类型信息，无参数为查询所有字典类型信息
     *
     * @param dtName
     *            字典类型名称
     * @return List<LcDictTypeDBEntity> 字典类型数据
     */
    public List<LcDictTypeDBEntity> queryDictTypeList(
            @WebParam(name = "userName", mode = Mode.IN) String dtName,
            @WebParam(name = "currentPage", mode = Mode.IN) int currentPage,
            @WebParam(name = "pageSize", mode = Mode.IN) int pageSize);
}
