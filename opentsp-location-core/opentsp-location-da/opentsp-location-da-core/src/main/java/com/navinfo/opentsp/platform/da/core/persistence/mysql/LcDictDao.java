package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.DictData;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;

import java.util.List;


public interface LcDictDao extends BaseDao<LcDictDBEntity> {

	/**
	 * 根据字典类型查找字典信息
	 * @param dictType {@link Integer} 字典类型
	 * @return  {@link List<{@link DictData}> 字典数据
	 */
	public List<DictData> getDictDataRes(int dictType);
	/**
	 * 根据参数查询字典信息，无参数为查询所有字典信息
	 * @param dtName  字典类型名称
	 * @return List<LcDictDBEntity> 字典数据
	 */
	public List<LcDictDBEntity> queryDictList(String dtName,int dictCode);
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName,int dictCode);
	/**
	 * 根据code修改字典信息
	 * @param lcDictDBEntity
	 * @return
	 */
	public int updateByDictCode(LcDictDBEntity lcDictDBEntity);

	/**
	 * 根据字典类型查找字典信息
	 * @param dictType 字典类型
	 * @return
	 */
	public List<LcDictDBEntity> getDictByType(int dictType,int currentPage,int pageSize);

	public int getDictDataResCount(int dictType);
	/**
	 * 根据字典编码获得字典值
	 * @param dictCode
	 * @return
	 */
	public LcDictDBEntity getDictByCode(int dictCode);
	/**
	 * 根据code删除字典信息
	 * @param dictCode
	 * @return
	 */
	public int deleteByCode(int[] dictCode);

}