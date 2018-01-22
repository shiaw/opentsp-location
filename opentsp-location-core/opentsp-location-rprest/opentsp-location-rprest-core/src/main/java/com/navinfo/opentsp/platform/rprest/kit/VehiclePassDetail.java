package com.navinfo.opentsp.platform.rprest.kit;

import java.io.Serializable;

public class VehiclePassDetail implements Serializable{
	private static final long serialVersionUID = 7158379400006513868L;
	private int time;
	private long tid;
	private long mileage;
	private long runTime;
	private boolean hasFaultCode;
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public long getTid() {
		return tid;
	}
	public void setTid(long tid) {
		this.tid = tid;
	}
	public long getMileage() {
		return mileage;
	}
	public void setMileage(long mileage) {
		this.mileage = mileage;
	}
	public long getRunTime() {
		return runTime;
	}
	public void setRunTime(long runTime) {
		this.runTime = runTime;
	}
	public boolean isHasFaultCode() {
		return hasFaultCode;
	}
	public void setHasFaultCode(boolean hasFaultCode) {
		this.hasFaultCode = hasFaultCode;
	}
}
