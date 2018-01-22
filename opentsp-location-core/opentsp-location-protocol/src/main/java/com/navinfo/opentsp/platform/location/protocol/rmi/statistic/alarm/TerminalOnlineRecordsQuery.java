package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.TerminalOnlinePercentage;

import java.util.ArrayList;
import java.util.List;


public class TerminalOnlineRecordsQuery implements java.io.Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//
	private int totalRords;
	//
	private int statusCode;
	//
	private List<TerminalOnlinePercentage> dataList = new ArrayList<TerminalOnlinePercentage>();
	public TerminalOnlineRecordsQuery(){}
	
	public TerminalOnlineRecordsQuery(int totalRords,int statusCode,List<TerminalOnlinePercentage> dataList){
		this.totalRords=totalRords;
		this.statusCode=statusCode;
		this.dataList=dataList;
		
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

	public List<TerminalOnlinePercentage> getDataList() {
		return dataList;
	}

	public void setDataList(List<TerminalOnlinePercentage> dataList) {
		this.dataList = dataList;
	}



	
}
