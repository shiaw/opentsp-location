package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao;


import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDrivingRecorderEntity;

public interface DrivingRecorderMongodbService {
	/**
	 * 存储行驶记录仪数据
	 * @param gpsDrivingRecorderEntity
	 */
	public void saveDrivingRecorder(GpsDrivingRecorderEntity gpsDrivingRecorderEntity);
}