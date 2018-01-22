package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDictTypeDBEntity;

import java.util.List;


public interface LcDictTypeDao extends BaseDao<LcDictTypeDBEntity> {
	/**
	 * 根据类型名称查找
	 * @param role_name
	 * @return
	 */
	public LcDictTypeDBEntity getDictType(String role_name);
	/**
	 * 根据参数查询字典类型信息，无参数为查询所有字典类型信息
	 * @param dtName  字典类型名称
	 * @return List<LcDictTypeDBEntity> 字典类型数据
	 */
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName);

	/**
	 * 根据参数查询字典类型信息，无参数为查询所有字典类型信息
	 * @param dtName  字典类型名称
	 * @return List<LcDictTypeDBEntity> 字典类型数据
	 */
	public List<LcDictTypeDBEntity> queryDictTypeList(String dtName,int currentPage,int pageSize);
	/**
	 * 查询字典类型记录数
	 * @param dtName
	 * @return
	 */
	public int queryDictTypeListCount(String dtName);


}
