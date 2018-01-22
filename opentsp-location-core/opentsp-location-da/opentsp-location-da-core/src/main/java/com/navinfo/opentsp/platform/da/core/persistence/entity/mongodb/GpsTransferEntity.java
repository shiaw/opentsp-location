package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb;

import java.io.Serializable;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class GpsTransferEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long _id;
	private long tId;
	private long saveDate;
	private int isUp;
	private byte[] content;

	public GpsTransferEntity() {
		super();
	}

	public DBObject toDBObject() {
		DBObject dbObject = new BasicDBObject();
		dbObject.put("tId", this.gettId());
		dbObject.put("saveDate", this.getSaveDate());
		dbObject.put("isUp", this.getIsUp());
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

	public int getIsUp() {
		return isUp;
	}

	public void setIsUp(int isUp) {
		this.isUp = isUp;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

}
