package com.navinfo.opentsp.platform.location.entity;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.io.Serializable;

public class GpsDetailedEntityDB implements Serializable {
	private static final long serialVersionUID = 1L;
	private long _id;
	private long gpsTime;
	private byte[] data;

	public GpsDetailedEntityDB() {
		super();
	}

	public GpsDetailedEntityDB(DBObject dbObject) {
		//this._id = Long.parseLong(String.valueOf(dbObject.get("_id")));
		this.gpsTime = Long.parseLong(String.valueOf(dbObject.get("gpsTime")));
		this.data = (byte[]) dbObject.get("data");
	}
	public GpsDetailedEntityDB(DBObject dbObject,long terminalId) {
		this._id =terminalId;
		this.gpsTime = Long.parseLong(String.valueOf(dbObject.get("gpsTime")));
		this.data = (byte[]) dbObject.get("data");
	}
	public DBObject toDBObject() {
		DBObject detailed = new BasicDBObject();
		detailed.put("gpsTime", this.getGpsTime());
		detailed.put("data", this.getData());
		return detailed;
	}

	public long get_id() {
		return _id;
	}

	public long getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(long gpsTime) {
		this.gpsTime = gpsTime;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
