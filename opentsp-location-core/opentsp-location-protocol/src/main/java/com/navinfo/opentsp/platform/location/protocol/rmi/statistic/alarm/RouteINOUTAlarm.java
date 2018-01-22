package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 进出线路报警信息
 * @author hws
 *
 */
public class RouteINOUTAlarm implements Serializable{
	private static final long serialVersionUID = 1L;
	private long terminalId;  //终端标识号（索引）
	private long	areaId;	  //路线标识
	private int	type;               //0进区域；1出区域; 2偏离路线
	private long	beginDate;	    //报警开始时间
	private long	endDate;	    //用户下发处理报警结束时间
	private long	triggerDate;	//偏离路线报警，维持到报警条件解除结束时间
	private int	continuousTime;	    //报警持续时间长度
	private int	triggerContinuousTime;	//触发报警处置时间长度
	private int	beginLat;	        //开始纬度
	private int	beginLng;	        //开始经度
	private int	endLat;	     //结束纬度
	private int	endLng;	     //结束经度
	private int	triggerLat;	 //触发结束报警纬度
	private int	triggerLng;	 //触发结束报警经度
	
	
	public long getTerminalId() {
		return terminalId;
	}
    public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
    }
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public long getBeginDate() {
		return beginDate;
	}
	public void setBeginDate(long beginDate) {
		this.beginDate = beginDate;
	}
	public long getEndDate() {
		return endDate;
	}
	public void setEndDate(long endDate) {
		this.endDate = endDate;
	}
	public long getTriggerDate() {
		return triggerDate;
	}
	public void setTriggerDate(long triggerDate) {
		this.triggerDate = triggerDate;
	}
	public int getContinuousTime() {
		return continuousTime;
	}
	public void setContinuousTime(int continuousTime) {
		this.continuousTime = continuousTime;
	}
	public int getTriggerContinuousTime() {
		return triggerContinuousTime;
	}
	public void setTriggerContinuousTime(int triggerContinuousTime) {
		this.triggerContinuousTime = triggerContinuousTime;
	}
	public int getBeginLat() {
		return beginLat;
	}
	public void setBeginLat(int beginLat) {
		this.beginLat = beginLat;
	}
	public int getBeginLng() {
		return beginLng;
	}
	public void setBeginLng(int beginLng) {
		this.beginLng = beginLng;
	}
	public int getEndLat() {
		return endLat;
	}
	public void setEndLat(int endLat) {
		this.endLat = endLat;
	}
	public int getEndLng() {
		return endLng;
	}
	public void setEndLng(int endLng) {
		this.endLng = endLng;
	}
	public int getTriggerLat() {
		return triggerLat;
	}
	public void setTriggerLat(int triggerLat) {
		this.triggerLat = triggerLat;
	}
	public int getTriggerLng() {
		return triggerLng;
	}
	public void setTriggerLng(int triggerLng) {
		this.triggerLng = triggerLng;
	}
	@Override
	public String toString() {
		return "RouteINOUTAlarm [terminalId=" + terminalId + ", areaId="
				+ areaId + ", type=" + type + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", triggerDate=" + triggerDate
				+ ", continuousTime=" + continuousTime
				+ ", triggerContinuousTime=" + triggerContinuousTime
				+ ", beginLat=" + beginLat + ", beginLng=" + beginLng
				+ ", endLat=" + endLat + ", endLng=" + endLng + ", triggerLat="
				+ triggerLat + ", triggerLng=" + triggerLng + "]";
	}

}
