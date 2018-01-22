package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;

import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsTransferEntity;

import java.util.List;


public interface TransparentDataMongodbService {
	/**
	 * 存储终端透传数据
	 * @param transferEntity
	 */
	public void savePassThrough(List<GpsTransferEntity> transferEntity);
}
