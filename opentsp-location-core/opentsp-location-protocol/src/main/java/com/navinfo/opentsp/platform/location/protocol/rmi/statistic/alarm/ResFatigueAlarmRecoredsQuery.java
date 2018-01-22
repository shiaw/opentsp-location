package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResFatigueAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<FatigueAlarm> dataList = new ArrayList<FatigueAlarm>();

	
	public ResFatigueAlarmRecoredsQuery(){}
	public ResFatigueAlarmRecoredsQuery(int tRecord, int staCode, List<FatigueAlarm> resultList){
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

	public List<FatigueAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<FatigueAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addFatigueAlarmItem(FatigueAlarm value){
		this.dataList.add(value);
	}
}
