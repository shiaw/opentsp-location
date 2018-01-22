package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;

public class CanBusDataDetailedEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long tId;
	private long gpsTime;
	private byte[] data;

	public long gettId() {
		return tId;
	}

	public void settId(long tId) {
		this.tId = tId;
	}

	public long getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(long gpsTime) {
		this.gpsTime = gpsTime;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

}
