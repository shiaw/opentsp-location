package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


public class CommonSummaryEntity implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//终端ID
	private long terminalID;
	//开始时间 UTC(s)
	private long startTime;
	//结束报警时间 UTC(s)
	private long endTime;
	//记录总条数
	private int recordsTotal;
	//报警持续时间总和 CalculatedAlarmTime
	private int calculatedAT;
	
	
	public CommonSummaryEntity(){}
	public CommonSummaryEntity(long tid, long st, long et, int rtotal, int calAT){
		terminalID = tid;
		startTime = st;
		endTime = et;
		recordsTotal = rtotal;
		calculatedAT = calAT;
	}
	public long getTerminalID() {
		return terminalID;
	}
	public void setTerminalID(long terminalID) {
		this.terminalID = terminalID;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getRecordsTotal() {
		return recordsTotal;
	}
	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	public int getCalculatedAT() {
		return calculatedAT;
	}
	public void setCalculatedAT(int calculatedAT) {
		this.calculatedAT = calculatedAT;
	}
}
