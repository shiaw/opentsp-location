package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class AlarmSummaryQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<CommonSummaryEntity> dataList = new ArrayList<CommonSummaryEntity>();

	
	public AlarmSummaryQuery(){}
	public AlarmSummaryQuery(int tRecord, int staCode, List<CommonSummaryEntity> resultList){
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

	public List<CommonSummaryEntity> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<CommonSummaryEntity> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addData(CommonSummaryEntity value){
		this.dataList.add(value);
	}
}
