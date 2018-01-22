package com.navinfo.opentsp.platform.da.core.persistence.redis.service;

import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.ServiceUniqueMark;

import java.util.List;


public interface IUpperService {
	/**
	 * 添加服务标识
	 *
	 * @param uniqueMark
	 */
	abstract boolean saveServiceUniqueMark(ServiceUniqueMark uniqueMark);

	/**
	 * 更新服务标识
	 *
	 * @param uniqueMark
	 */
	abstract void updateServiceUniqueMark(ServiceUniqueMark uniqueMark);

	/**
	 * 查找服务标识
	 *
	 * @param districtCode
	 * @param uniqueMark
	 * @return
	 */
	abstract ServiceUniqueMark findServiceUniqueMark(long uniqueMark);

	/**
	 * 删除服务标识
	 *
	 * @param districtCode
	 * @param uniqueMark
	 */
	abstract void delServiceUniqueMark(long uniqueMark);

	/**
	 * 查找所有服务标识
	 *
	 * @return
	 */
	abstract List<ServiceUniqueMark> findAllServiceUniqueMark();
}
