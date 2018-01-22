package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResMileagesRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<Mileages> dataList = new ArrayList<Mileages>();

	
	public ResMileagesRecoredsQuery(){}
	public ResMileagesRecoredsQuery(int tRecord, int staCode, List<Mileages> resultList){
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

	public List<Mileages> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<Mileages> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addMilagesItem(Mileages value){
		this.dataList.add(value);
	}
}
