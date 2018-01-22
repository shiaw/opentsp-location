package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResOverspeedAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<OverspeedAlarm> dataList = new ArrayList<OverspeedAlarm>();

	
	public ResOverspeedAlarmRecoredsQuery(){}
	public ResOverspeedAlarmRecoredsQuery(int tRecord, int staCode, List<OverspeedAlarm> resultList){
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

	public List<OverspeedAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<OverspeedAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addOverspeedItem(OverspeedAlarm value){
		this.dataList.add(value);
	}
}
