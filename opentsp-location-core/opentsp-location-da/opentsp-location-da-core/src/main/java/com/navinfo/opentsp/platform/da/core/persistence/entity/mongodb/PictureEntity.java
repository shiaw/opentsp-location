package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb;

import java.io.Serializable;

import com.mongodb.DBObject;

public class PictureEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long _id;
	private String fileName;
	private long tId;
	private long saveDate;
	private byte[] data;
	/*添加位置数据*/
	public PictureEntity() {
		super();
	}
	public DBObject toDBObject(){
		return null;
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

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
