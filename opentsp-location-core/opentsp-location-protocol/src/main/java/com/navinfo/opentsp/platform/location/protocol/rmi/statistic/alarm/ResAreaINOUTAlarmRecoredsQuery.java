package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResAreaINOUTAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<AreaINOUTAlarm> dataList = new ArrayList<AreaINOUTAlarm>();

	
	public ResAreaINOUTAlarmRecoredsQuery(){}
	public ResAreaINOUTAlarmRecoredsQuery(int tRecord, int staCode, List<AreaINOUTAlarm> resultList){
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

	public List<AreaINOUTAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<AreaINOUTAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addAreaINOUTItem(AreaINOUTAlarm value){
		this.dataList.add(value);
	}
}
