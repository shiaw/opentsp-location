package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport;

import java.io.Serializable;


public class TempCanData implements Serializable {
	private static final long serialVersionUID = 1L;
	private long terminalId;
	private long gpsTime;
	private String day;
	private LCCANBUSDataReport.CANBUSDataReport data;

	public TempCanData() {
		super();
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public long getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(long gpsTime) {
		this.gpsTime = gpsTime;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}
	public LCCANBUSDataReport.CANBUSDataReport getData() {
		return data;
	}

	public void setData(LCCANBUSDataReport.CANBUSDataReport data) {
		this.data = data;
	}



}
