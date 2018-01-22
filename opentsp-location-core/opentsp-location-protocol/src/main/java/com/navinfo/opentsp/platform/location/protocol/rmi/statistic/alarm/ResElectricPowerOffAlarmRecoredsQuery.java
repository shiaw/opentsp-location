package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResElectricPowerOffAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<ElectricPowerOffAlarm> dataList = new ArrayList<ElectricPowerOffAlarm>();

	
	public ResElectricPowerOffAlarmRecoredsQuery(){}
	public ResElectricPowerOffAlarmRecoredsQuery(int tRecord, int staCode, List<ElectricPowerOffAlarm> resultList){
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

	public List<ElectricPowerOffAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<ElectricPowerOffAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addElectricPowerOffAlarmItem(ElectricPowerOffAlarm value){
		this.dataList.add(value);
	}
}
