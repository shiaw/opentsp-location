package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResRouteINOUTAlarmRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<RouteINOUTAlarm> dataList = new ArrayList<RouteINOUTAlarm>();

	
	public ResRouteINOUTAlarmRecoredsQuery(){}
	public ResRouteINOUTAlarmRecoredsQuery(int tRecord, int staCode, List<RouteINOUTAlarm> resultList){
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

	public List<RouteINOUTAlarm> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<RouteINOUTAlarm> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addRouteINOUTItem(RouteINOUTAlarm value){
		this.dataList.add(value);
	}
}
