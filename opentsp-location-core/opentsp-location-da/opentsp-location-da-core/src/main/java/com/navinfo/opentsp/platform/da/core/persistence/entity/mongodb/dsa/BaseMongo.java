package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa;

import com.mongodb.DBObject;

public abstract interface BaseMongo  {
	
   
    abstract 	public DBObject toDBObject();
    abstract    public long getDay(); 
	abstract    public void setDay(long day);
	
}
