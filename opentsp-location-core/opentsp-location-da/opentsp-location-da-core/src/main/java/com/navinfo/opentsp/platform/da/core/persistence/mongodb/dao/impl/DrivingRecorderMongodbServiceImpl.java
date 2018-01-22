package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDrivingRecorderEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.DrivingRecorderMongodbService;

public class DrivingRecorderMongodbServiceImpl extends MongoDaoImp implements
		DrivingRecorderMongodbService {

	@Override
	public void saveDrivingRecorder(GpsDrivingRecorderEntity gpsDrivingRecorderEntity) {
		if(gpsDrivingRecorderEntity!=null){
			super.save(gpsDrivingRecorderEntity.toDBObject());
		}
		
	}

}
