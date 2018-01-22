package com.navinfo.opentsp.platform.da.core.persistence.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcServiceDistrictConfigDBEntity;

import java.util.List;


public interface LcServiceDistrictConfigDao extends BaseDao<LcServiceDistrictConfigDBEntity>{
	/**
	 * 根据服务id查找
	 * @param sc_id
	 * @return
	 */
	public List<LcServiceDistrictConfigDBEntity> getByScId(int sc_id);
	/**
	 * 根据服务区域编码查找
	 * @param service_district
	 * @return
	 */
	public LcServiceDistrictConfigDBEntity getByServiceDistrict(int service_district);
	/**
	 * 根据服务删除服务所属分区
	 * @param sc_id
	 */
	public void deleteByService(int sc_id);
}
