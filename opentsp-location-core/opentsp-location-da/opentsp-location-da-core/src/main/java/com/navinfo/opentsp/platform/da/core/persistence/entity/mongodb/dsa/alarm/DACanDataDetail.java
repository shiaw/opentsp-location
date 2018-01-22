package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * Can数据详情
 * @author zyl
 */
public class DACanDataDetail {
	private long time;
	private byte[] data;
	public DACanDataDetail(){}
	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public DACanDataDetail(DBObject dbObjectDetail) {
		this.time = (long) dbObjectDetail.get("time");
		this.data = (byte[]) dbObjectDetail.get("data");
	}

	public DBObject toDBObject() {
		DBObject alarm = new BasicDBObject();
		alarm.put("time", this.time);// �ն˱�ʶ��
		alarm.put("data", this.data);
		return alarm;
	}
	
	/*@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		this.time = (long) dbObjectDetail.get("time");
		this.data = (byte[]) dbObjectDetail.get("data");
	}*/
}
