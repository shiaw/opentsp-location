package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 4.16.1	网格车次检索
 * @author zyl
 *
 */
public class ResVehiclePassTimesRecordsQuery implements Serializable{
	private static final long serialVersionUID = -5494173583858984448L;
	//总记录
	private int totalRecords;
	//状态码
	private int statusCode;
	//详情
	private List<VehiclePassTimesEntity> dataList = new ArrayList<VehiclePassTimesEntity>();
	public int getTotalRecords() {
		return totalRecords;
	}
	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}
	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}
	public List<VehiclePassTimesEntity> getDataList() {
		return dataList;
	}
	public void setDataList(List<VehiclePassTimesEntity> dataList) {
		this.dataList = dataList;
	}
	public void addVehiclePassTimes(List<VehiclePassTimesEntity> entities){
		this.dataList.addAll(entities);
	}
}
