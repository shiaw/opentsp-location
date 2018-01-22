package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.DBObject;

public abstract class BaseAlarmEntity {
     public abstract void dbObjectToBean(DBObject dbObjectDetail);
}
