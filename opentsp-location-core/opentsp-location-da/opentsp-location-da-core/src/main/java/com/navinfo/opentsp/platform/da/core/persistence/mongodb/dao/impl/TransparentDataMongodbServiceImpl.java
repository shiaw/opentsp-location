package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsTransferEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.TransparentDataMongodbService;
import com.mongodb.DBObject;

public class TransparentDataMongodbServiceImpl extends MongoDaoImp implements
		TransparentDataMongodbService {

	@Override
	public void savePassThrough(List<GpsTransferEntity> transferEntity) {
		if(CollectionUtils.isNotEmpty(transferEntity)){
			List<DBObject> dbObjects = new ArrayList<DBObject>();
			for(GpsTransferEntity entity:transferEntity){
				dbObjects.add(entity.toDBObject());
			}
			super.bacthSave(dbObjects);
		}
	
	}

}
