package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OutRegionLimitSpeedQuery implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1146813537170977436L;
	private int totalRords;
	private int statusCode;
	private List<LcOutRegionLimitSpeedLogEntity> dataList = new ArrayList<LcOutRegionLimitSpeedLogEntity>();
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
	public List<LcOutRegionLimitSpeedLogEntity> getDataList() {
		return dataList;
	}
	public void setDataList(List<LcOutRegionLimitSpeedLogEntity> dataList) {
		this.dataList = dataList;
	}
}
