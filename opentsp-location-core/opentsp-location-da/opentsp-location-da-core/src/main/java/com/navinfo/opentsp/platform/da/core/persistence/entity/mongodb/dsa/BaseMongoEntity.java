package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import java.util.List;

import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.BaseAlarmEntity;

public abstract class BaseMongoEntity extends BaseAlarmEntity implements BaseMongo {
	
	abstract public List<DBObject> toBatchDBObject();
	
    public   void dbObjectToBean(DBObject dbObjectDetail){
    	
    }
}
