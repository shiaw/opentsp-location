package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;

/**
 * 路段超时报警信息
 * @author hws
 *
 */
public class RLineOvertimeAlarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	private long	areaId;	//路线标识
	private long	segmentId;	//路段标识
	private long	beginDate;	//报警开始时间
	private long	endDate;	//报警结束时间
	private int	continuousTime;	//报警持续时间长度
	private int	maxLimitTime; 	//最大时间阀值
	private int	minLimitTime;	//最小时间阀值
	private int	type;	//0：不足；1过长
	private int	beginLat;	//开始纬度
	private int	beginLng;	//开始经度
	private int	endLat;	//结束纬度
	private int	endLng;	//结束经度
	public RLineOvertimeAlarm(){
		
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
	public long getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(long segmentId) {
		this.segmentId = segmentId;
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
	public int getMaxLimitTime() {
		return maxLimitTime;
	}
	public void setMaxLimitTime(int maxLimitTime) {
		this.maxLimitTime = maxLimitTime;
	}
	public int getMinLimitTime() {
		return minLimitTime;
	}
	public void setMinLimitTime(int minLimitTime) {
		this.minLimitTime = minLimitTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
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
		return "RLineOvertimeAlarm [terminalId=" + terminalId + ", areaId="
				+ areaId + ", segmentId=" + segmentId + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", continuousTime="
				+ continuousTime + ", maxLimitTime=" + maxLimitTime
				+ ", minLimitTime=" + minLimitTime + ", type=" + type
				+ ", beginLat=" + beginLat + ", beginLng=" + beginLng
				+ ", endLat=" + endLat + ", endLng=" + endLng + "]";
	}
}
