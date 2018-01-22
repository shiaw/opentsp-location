package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 滞留超时
 */
public class OvertimeParkAlarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String _id;//mongo主键
	private long terminalId;//终端标识号（索引）
	private long areaId;//区域ID
	private long beginDate;//入区域时间
	private long endDate;//出区域时间
	private int	continuousTime;//滞留超时时间长度
	private int	limitParking;//滞留阀值
	private int	beginLat;//	开始纬度
	private int	beginLng;//	开始经度
	private int	endLat;//结束纬度
	private int	endLng;//结束经度
	private int headMerge; 	// 0需要合并 1统计完结
	private int tailMerge;	// 0需要合并 1统计完结
	public int getHeadMerge() {
		return headMerge;
	}
	public void setHeadMerge(int headMerge) {
		this.headMerge = headMerge;
	}
	public int getTailMerge() {
		return tailMerge;
	}
	public void setTailMerge(int tailMerge) {
		this.tailMerge = tailMerge;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
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
	public int getLimitParking() {
		return limitParking;
	}
	public void setLimitParking(int limitParking) {
		this.limitParking = limitParking;
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
		return "OvertimeParkAlarm [_id=" + _id + ", terminalId=" + terminalId
				+ ", areaId=" + areaId + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", continuousTime=" + continuousTime
				+ ", limitParking=" + limitParking + ", beginLat=" + beginLat
				+ ", beginLng=" + beginLng + ", endLat=" + endLat + ", endLng="
				+ endLng + "]";
	}
}
