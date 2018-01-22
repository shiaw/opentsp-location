package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResAreaOverspeedAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<AreaOverspeedAlarm> dataList = new ArrayList<AreaOverspeedAlarm>();

	
	public ResAreaOverspeedAlarmRecoredsQuery(){}
	public ResAreaOverspeedAlarmRecoredsQuery(int tRecord, int staCode, List<AreaOverspeedAlarm> resultList){
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

	public List<AreaOverspeedAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<AreaOverspeedAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addAreaOverspeedItem(AreaOverspeedAlarm value){
		this.dataList.add(value);
	}
}
