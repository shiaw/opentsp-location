package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GpsDrivingRecorderEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long _id;
	private long tId;
	private long saveDate;
	private int type;
	private byte[] content;

	public GpsDrivingRecorderEntity() {
		super();
	}

	public GpsDrivingRecorderEntity(DBObject dbObject) {
		this._id = Long.parseLong(String.valueOf(dbObject.get("_id")));
		this.tId = Long.parseLong(String.valueOf(dbObject.get("tId")));
		this.saveDate = Long
				.parseLong(String.valueOf(dbObject.get("saveDate")));
		this.type = Integer.parseInt(String.valueOf("type"));
		this.content = (byte[]) dbObject.get("content");
	}

	public DBObject toDBObject() {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("tId", this.gettId());
		dbObject.put("saveData", this.getSaveDate());
		dbObject.put("type", this.getType());
		dbObject.put("content", this.getContent());
		return dbObject;
	}

	public long get_id() {
		return _id;
	}

	public void set_id(long _id) {
		this._id = _id;
	}

	public long gettId() {
		return tId;
	}

	public void settId(long tId) {
		this.tId = tId;
	}

	public long getSaveDate() {
		return saveDate;
	}

	public void setSaveDate(long saveDate) {
		this.saveDate = saveDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
