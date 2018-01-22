package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class ResWFlowRecoredsQuery implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<WFlow> dataList = new ArrayList<WFlow>();

	
	public ResWFlowRecoredsQuery(){}
	public ResWFlowRecoredsQuery(int tRecord, int staCode, List<WFlow> resultList){
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

	public List<WFlow> getCommResultList() {
		return dataList;
	}

	public void setCommResultList(List<WFlow> commResultList) {
		this.dataList = commResultList;
	}
	
	public void addWFlowStatusItem(WFlow value){
		this.dataList.add(value);
	}
}
