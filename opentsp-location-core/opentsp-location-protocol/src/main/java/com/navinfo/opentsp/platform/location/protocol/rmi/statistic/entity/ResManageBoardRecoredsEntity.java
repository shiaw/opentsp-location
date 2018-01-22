package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ResManageBoardRecoredsEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -198905637495802090L;
	private int statusCode;
	private int totalRecords;
	private List<ManageBoardEntity> dataList = new ArrayList<ManageBoardEntity>();
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public List<ManageBoardEntity> getDataList() {
		return dataList;
	}
	public void setDataList(List<ManageBoardEntity> dataList) {
		this.dataList = dataList;
	}
	
}
