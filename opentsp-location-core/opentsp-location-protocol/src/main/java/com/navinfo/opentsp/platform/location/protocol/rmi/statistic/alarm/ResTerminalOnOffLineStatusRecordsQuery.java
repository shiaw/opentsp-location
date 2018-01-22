package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResTerminalOnOffLineStatusRecordsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<TerminalOnOffLineStatus> dataList = new ArrayList<TerminalOnOffLineStatus>();

	
	public ResTerminalOnOffLineStatusRecordsQuery(){}
	public ResTerminalOnOffLineStatusRecordsQuery(int tRecord, int staCode, List<TerminalOnOffLineStatus> resultList){
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

	public List<TerminalOnOffLineStatus> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<TerminalOnOffLineStatus> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addTerminalOnOffLineStatusItem(TerminalOnOffLineStatus value){
		this.dataList.add(value);
	}
}
