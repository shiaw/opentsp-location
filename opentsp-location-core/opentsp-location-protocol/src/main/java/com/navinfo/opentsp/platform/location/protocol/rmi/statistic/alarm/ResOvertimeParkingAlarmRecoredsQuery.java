package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResOvertimeParkingAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<StagnationTimeoutEntity> dataList = new ArrayList<StagnationTimeoutEntity>();

	
	public ResOvertimeParkingAlarmRecoredsQuery(){}
	public ResOvertimeParkingAlarmRecoredsQuery(int tRecord, int staCode, List<StagnationTimeoutEntity> resultList){
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

	public List<StagnationTimeoutEntity> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<StagnationTimeoutEntity> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addOvertimeParkingItem(StagnationTimeoutEntity value){
		this.dataList.add(value);
	}
}
