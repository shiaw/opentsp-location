package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResACCStatusAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<TerACCStatus> dataList = new ArrayList<TerACCStatus>();

	
	public ResACCStatusAlarmRecoredsQuery(){}
	public ResACCStatusAlarmRecoredsQuery(int tRecord, int staCode, List<TerACCStatus> resultList){
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

	public List<TerACCStatus> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<TerACCStatus> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addACCStatusItem(TerACCStatus value){
		this.dataList.add(value);
	}
}
