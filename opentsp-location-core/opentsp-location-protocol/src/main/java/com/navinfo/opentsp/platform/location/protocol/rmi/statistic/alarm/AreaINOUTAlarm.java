package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;

/**
 * 进出区域报警信息
 * @author 
 *
 */
public class AreaINOUTAlarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	private long	areaId ;	//区域标识
	private int	type;	//0进区域；1出区域
	private long	beginDate	;//报警开始时间
	private long	endDate  ;	//报警结束时间
	private int	continuousTime;	//报警持续时间长度
	private int	beginLat;	//开始纬度
	private int	beginLng;	//开始经度
	private int	endLat	;//结束纬度
	private 	int	endLng;	//结束经度
	public AreaINOUTAlarm(){
		super();
	}
	
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
	public int getContinuousTime() {
		return continuousTime;
	}
	public void setContinuousTime(int continuousTime) {
		this.continuousTime = continuousTime;
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

	@Override
	public String toString() {
		return "AreaINOUTAlarm [terminalId=" + terminalId + ", areaId="
				+ areaId + ", type=" + type + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", continuousTime=" + continuousTime
				+ ", beginLat=" + beginLat + ", beginLng=" + beginLng
				+ ", endLat=" + endLat + ", endLng=" + endLng + "]";
	}
}
