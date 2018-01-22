package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResDriverLoginRecordsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<DriverLogin> dataList = new ArrayList<DriverLogin>();

	
	public ResDriverLoginRecordsQuery(){}
	public ResDriverLoginRecordsQuery(int tRecord, int staCode, List<DriverLogin> resultList){
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

	public List<DriverLogin> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<DriverLogin> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addDriverLoginItem(DriverLogin value){
		this.dataList.add(value);
	}
}
