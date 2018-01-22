package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;

public class VehiclePassTimesDetail implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private long tid;
	private long mileage; 		//整车里程
	private long runTime;		//发动机运行时长
	private boolean faultCode;	//是否有故障码
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
	public boolean getFaultCode() {
		return faultCode;
	}
	public void setFaultCode(boolean faultCode) {
		this.faultCode = faultCode;
	}
}
