package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * 滞留超时
 * @author zyl
 *
 */
public class ResOvertimeParkRecoredsQuery implements Serializable{
	private static final long serialVersionUID = 7230827359597945252L;
	//总记录
	private int totalRords;
	//状态码
	private int statusCode;
	//详情
	private List<OvertimeParkAlarm> dataList = new ArrayList<OvertimeParkAlarm>();
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
	public List<OvertimeParkAlarm> getDataList() {
		return dataList;
	}
	public void setDataList(List<OvertimeParkAlarm> dataList) {
		this.dataList = dataList;
	}
	public void addOvertimeParkAlarmItem(OvertimeParkAlarm value){
		this.dataList.add(value);
	}
}
