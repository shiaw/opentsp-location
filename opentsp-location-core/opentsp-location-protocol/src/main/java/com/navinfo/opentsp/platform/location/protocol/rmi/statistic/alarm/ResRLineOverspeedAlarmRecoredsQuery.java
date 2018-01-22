package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResRLineOverspeedAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<RLineOverspeedAlarm> dataList = new ArrayList<RLineOverspeedAlarm>();

	
	public ResRLineOverspeedAlarmRecoredsQuery(){}
	public ResRLineOverspeedAlarmRecoredsQuery(int tRecord, int staCode, List<RLineOverspeedAlarm> resultList){
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

	public List<RLineOverspeedAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<RLineOverspeedAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addRLineOverspeedItem(RLineOverspeedAlarm value){
		this.dataList.add(value);
	}
}
