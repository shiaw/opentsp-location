package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;

public class GpsDetailedEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long _id;
	private long gpsTime;
	private byte[] data;

	public GpsDetailedEntity() {
		super();
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
