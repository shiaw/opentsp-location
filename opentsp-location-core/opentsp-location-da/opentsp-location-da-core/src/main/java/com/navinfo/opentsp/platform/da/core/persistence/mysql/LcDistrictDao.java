package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcDistrictDBEntity;

import java.util.List;


public interface LcDistrictDao extends BaseDao<LcDistrictDBEntity>{
	/**
	 * 根据服务区域编码查找
	 * @param dictCode
	 * @return
	 */
	public LcDistrictDBEntity getDistrict(int dictCode);
	/**
	 * 根据参数查询行政区划信息
	 * @param dtName  字典类型名称
	 *  @param dictId  协议Id
	 * @return List<LcDictDBEntity> 字典数据
	 */
	public List<LcDistrictDBEntity> queryDistrictList(String dictCodeName);
	/**
	 * 根据父服务区域删除行政区划
	 * @param parent_code
	 */
	public void deleteByParentCode(int parent_code);
	/**
	 * 根据父服务区域查询服务区域编码
	 * @param parent_code
	 * @return
	 */
	public List<LcDistrictDBEntity> queryDistrictByParent_code(int parent_code,int currentPage,int pageSize);

	public int queryDistrictCount(int parent_code);
}
