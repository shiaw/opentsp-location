package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResAllAlarmSummaryQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<AllAlarmData> dataList = new ArrayList<AllAlarmData>();

	
	public ResAllAlarmSummaryQuery(){}
	public ResAllAlarmSummaryQuery(int tRecord, int staCode, List<AllAlarmData> resultList){
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

	public List<AllAlarmData> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<AllAlarmData> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addData(AllAlarmData value){
		this.dataList.add(value);
	}
}
