package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResFaultCodeAlarmRecoreds implements Serializable{
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<FaultCodeEntity> dataList = new ArrayList<FaultCodeEntity>();

	
	public ResFaultCodeAlarmRecoreds(){}
	public ResFaultCodeAlarmRecoreds(int tRecord, int staCode, List<FaultCodeEntity> resultList){
		totalRords = tRecord;
		statusCode = staCode;
		dataList = resultList;
	}
	
	public int getTotalRords() {
		return totalRords;
	}

	public void setTotalRords(int totalRords) {
		this.totalRords = totalRords;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public List<FaultCodeEntity> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<FaultCodeEntity> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addFaultCodeAlarmItem(FaultCodeEntity value){
		this.dataList.add(value);
	}
}
