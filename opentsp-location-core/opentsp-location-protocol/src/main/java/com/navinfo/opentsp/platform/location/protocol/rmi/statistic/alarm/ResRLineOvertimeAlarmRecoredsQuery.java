package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResRLineOvertimeAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<RLineOvertimeAlarm> dataList = new ArrayList<RLineOvertimeAlarm>();

	
	public ResRLineOvertimeAlarmRecoredsQuery(){}
	public ResRLineOvertimeAlarmRecoredsQuery(int tRecord, int staCode, List<RLineOvertimeAlarm> resultList){
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

	public List<RLineOvertimeAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<RLineOvertimeAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addRLineOvertimeItem(RLineOvertimeAlarm value){
		this.dataList.add(value);
	}
}
