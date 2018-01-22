package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResWFlowSummaryQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<WFSummary> dataList = new ArrayList<WFSummary>();

	
	public ResWFlowSummaryQuery(){}
	public ResWFlowSummaryQuery(int tRecord, int staCode, List<WFSummary> resultList){
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

	public List<WFSummary> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<WFSummary> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addData(WFSummary value){
		this.dataList.add(value);
	}
}
