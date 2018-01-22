package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;


public class MileageSummaryEntity  {
	private long   terminalId;
	private long	beginMileage;//开始里程
	private long	endMileage;  //结束里程
	private int	mileage;     //里程数
	private long	beginDate;   //开始时间
	private long	endDate;     //结束时间
	private int	    beginLat;    //开始纬度
	private int	    beginLng;    //开始经度
	private int	    endLat;      //结束纬度
	private int	    endLNG;      //结束经度
	
	private long	    totalTime;      //统计时间
	private int oil;
	
	
	public int getOil() {
		return oil;
	}
	public void setOil(int oil) {
		this.oil = oil;
	}
	public long getTotalTime() {
		return totalTime;
	}
	public void setTotalTime(long totalTime) {
		this.totalTime = totalTime;
	}
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public long getBeginMileage() {
		return beginMileage;
	}
	public void setBeginMileage(long beginMileage) {
		this.beginMileage = beginMileage;
	}
	public long getEndMileage() {
		return endMileage;
	}
	public void setEndMileage(long endMileage) {
		this.endMileage = endMileage;
	}
	public int getMileage() {
		return mileage;
	}
	public void setMileage(int mileage) {
		this.mileage = mileage;
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
	public int getEndLNG() {
		return endLNG;
	}
	public void setEndLNG(int endLNG) {
		this.endLNG = endLNG;
	}
}
