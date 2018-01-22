package com.navinfo.opentsp.platform.da.core.persistence;

import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.DictData;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalProtoMappingDBEntity;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import java.util.List;



public interface DictManage {
	/**
	 * 获取字典信息
	 *
	 * @param dictType
	 *            {@link Integer} 字典类型
	 * @return List<{@link LcDictDBEntity}> 字典信息集合
	 */
	public List<DictData> getDictDataRes(int dictType);

	abstract PlatformResponseResult saveOrUpdateDict(
			LcDictDBEntity lcDictDBEntity);

	/**
	 * 根据参数查询字典信息，无参数为查询所有字典信息
	 *
	 * @param dtName
	 *            字典类型名称
	 * @param dictId
	 *            协议Id
	 * @return List<LcDictDBEntity> 字典数据
	 */
	public List<LcDictDBEntity> queryDictList(String dtName, int dictId);
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName, int dictId);
	/**
	 * 根据参数查询字典类型信息，无参数为查询所有字典类型信息
	 *
	 * @param dtName
	 * @return
	 */
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName);

	/**
	 * 根据参数查询终端指令协议映射信息
	 *
	 * @param dictCodeName
	 *            协议类型code
	 * @return 实体对象
	 */
	public List<LcTerminalProtoMappingDBEntity> getByLogicCode(int logic_code);
	/**
	 * 根据参数查询终端指令协议映射信息
	 *
	 * @param dictCodeName
	 *            协议类型code
	 * @return 实体对象
	 */
	public List<LcTerminalProtoMappingDBEntity> getTerminalProtoMapping(int logic_code,int dictCode,int messageCode,int currentPage,int pageSize);
	/**
	 * 查询终端指令映射信息总记录
	 * @param logic_code
	 * @param dictCode
	 * @param messageCode
	 * @return
	 */
	public int selectPMListCount(int logic_code,int dictCode,int messageCode);
	/**
	 *
	 * @param lcTerminalProtoMappingDBEntity
	 * @return
	 */
	public abstract PlatformResponseResult saveOrUpdateTerminalProtoMapping(
			LcTerminalProtoMappingDBEntity lcTerminalProtoMappingDBEntity);
	/**
	 * 删除字典（支持批量删除）暂时不做
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public PlatformResponseResult deleteTerminalProtoMapping(int[] tplId,int messageCode);

	/**
	 * 根据参数查询行政区划信息
	 *
	 * @param dtName
	 *            字典类型名称
	 * @param dictId
	 *            协议Id
	 * @return List<LcDictDBEntity> 字典数据
	 */
	public List<LcDistrictDBEntity> queryDistrictList(String dictCodeName);

	/**
	 * 根据字典类型查找字典信息
	 *
	 * @param dictType
	 *            字典类型
	 * @return
	 */
	public List<LcDictDBEntity> getDictByType(int dictType,int currentPage,int pageSize);

	public int getDictDataResCount(int dictType);
	/**
	 * 根据父服务区域编码查询服务区域编码
	 *
	 * @param parent_code
	 * @return
	 */
	public List<LcDistrictDBEntity> queryDistrictByParent_code(int parent_code,int currentPage,int pageSize);

	public int queryDistrictCount(int parent_code);

	/**
	 * 根据参数查询终端指令协议映射信息(分组查询)
	 *
	 * @param dictCodeName
	 *            协议类型code
	 * @return 实体对象
	 */
	public List<LcTerminalProtoMappingDBEntity> getByGroupLogicCode(
			int logic_code);

	/**
	 * 删除字典（支持批量删除）暂时不做
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public PlatformResponseResult deleteDict(int[] dictCode);

	/**
	 * 新增（修改字典类型）
	 *
	 * @param lcDictDBEntity
	 *            字典实体
	 * @return 枚举
	 */
	public PlatformResponseResult saveOrUpdateDictType(
			LcDictTypeDBEntity lcDictTypeDBEntity);

	/**
	 * 删除字典类型（支持批量删除）
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public PlatformResponseResult deleteDictType(int[] dtId);

	/**
	 * 保存终端指令协议映射前删除此协议类型下以前原有映射关系
	 *
	 * @param logicCode
	 *            终端协议
	 * @param dictCode
	 *            终端指令
	 * @return
	 */
	public PlatformResponseResult bindProtoMapping(int logicCode, int[] dictCode);

	/**
	 * 新增（修改行政区划）
	 *
	 * @param lcDictDBEntity
	 *            字典实体
	 * @return 枚举
	 */
	public PlatformResponseResult bindDistrict(int parentCode, int[] dictCode);

	/**
	 * 删除行政区划（支持批量删除）
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public PlatformResponseResult deleteDistrict(int[] districtId);

	/**
	 * 根据主键ID删除终端指令映射
	 *
	 * @param tplId
	 * 		 终端指令
	 * @return
	 */
	public PlatformResponseResult deleteProtoMapping(int[] tplId);

	/**
	 * 根据协议类型删除终端指令映射
	 *
	 * @param tplId
	 * 		 终端指令
	 * @return
	 */
	public PlatformResponseResult deleteProtoMappingByDictcode(int[] logic_code);
	/**
	 * 协议绑定更新
	 *
	 * @param dictId
	 *            字典Id
	 * @return 枚举
	 */
	public PlatformResponseResult updateProtocoMapping(List<LcTerminalProtoMappingDBEntity> lcTerminalProtoMappingDBEntity);

	/**
	 * 通过字典编码查询字典值
	 * @param dictCode
	 * @return
	 */
	public LcDictDBEntity getDictByCode(int dictCode);
	/**
	 * 根据参数查询字典类型信息，无参数为查询所有字典类型信息
	 *
	 * @param dtName
	 * @return
	 */
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName,int currentPage,int pageSize);
	/**
	 * 查询字典类型记录数
	 * @param dtName
	 * @return
	 */
	public int queryDictTypeListCount(String dtName);
}
