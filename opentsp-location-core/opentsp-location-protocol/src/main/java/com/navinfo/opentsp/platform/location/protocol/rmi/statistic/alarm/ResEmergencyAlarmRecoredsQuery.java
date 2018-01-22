package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResEmergencyAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<EmergencyAlarm> dataList = new ArrayList<EmergencyAlarm>();

	
	public ResEmergencyAlarmRecoredsQuery(){}
	public ResEmergencyAlarmRecoredsQuery(int tRecord, int staCode, List<EmergencyAlarm> resultList){
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

	public List<EmergencyAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<EmergencyAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addEmergencyAlarmItem(EmergencyAlarm value){
		this.dataList.add(value);
	}
}
