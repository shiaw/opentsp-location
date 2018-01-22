package com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm;

import com.mongodb.DBObject;


public class DACommonSummaryEntity extends BaseAlarmEntity{
    //
	private long terminalID;
	//
	private long startTime;
	//
	private long endTime;
	//
	private int recordsTotal;
	//
	private int calculatedAT;
	
	
	public DACommonSummaryEntity(){}
	public DACommonSummaryEntity(long tid, long st, long et, int rtotal, int calAT){
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
	@Override
	public void dbObjectToBean(DBObject dbObjectDetail) {
		// TODO Auto-generated method stub
		
	}
}
