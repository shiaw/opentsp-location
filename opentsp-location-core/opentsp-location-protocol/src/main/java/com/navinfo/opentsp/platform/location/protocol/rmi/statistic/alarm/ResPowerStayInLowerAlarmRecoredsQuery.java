package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResPowerStayInLowerAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<PowerStayInLowerAlarm> dataList = new ArrayList<PowerStayInLowerAlarm>();

	
	public ResPowerStayInLowerAlarmRecoredsQuery(){}
	public ResPowerStayInLowerAlarmRecoredsQuery(int tRecord, int staCode, List<PowerStayInLowerAlarm> resultList){
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

	public List<PowerStayInLowerAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<PowerStayInLowerAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addElectricPowerOffAlarmItem(PowerStayInLowerAlarm value){
		this.dataList.add(value);
	}
}
