package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 区域停留时间报警
 */
public class StaytimeParkAlarm implements Serializable{

	private static final long serialVersionUID = 1L;
	private String _id;//mongo主键
	private long terminalId;//终端标识号（索引）
	private long areaId;//区域ID
	private long beginDate;//入区域时间
	private long endDate;//出区域时间
	private int	continuousTime;//滞留超时时间长度
	private int	beginLat;//	开始纬度
	private int	beginLng;//	开始经度
	private int	endLat;//结束纬度
	private int	endLng;//结束经度
	private boolean isCount;//是否到达阈值（是否需要统计）
	private int headMerge; 	// 0需要合并 1统计完结
	private int tailMerge;	// 0需要合并 1统计完结

	private int updateFlg; //0 insert,1 update

	public StaytimeParkAlarm() {
	}

	public StaytimeParkAlarm(long beginDate, int beginLat, int beginLng, long endDate,int endLat, int endLng, boolean isCount) {
		this.beginDate = beginDate;
		this.beginLat = beginLat;
		this.beginLng = beginLng;
		this.endDate = endDate;
		this.endLat = endLat;
		this.endLng = endLng;
		this.isCount = isCount;
	}

	public StaytimeParkAlarm(long terminalId, long areaId, long beginDate, long endDate, int continuousTime, int beginLat, int beginLng, int endLat, int endLng, int tailMerge, int updateFlg) {
		this.terminalId = terminalId;
		this.areaId = areaId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.continuousTime = continuousTime;
		this.beginLat = beginLat;
		this.beginLng = beginLng;
		this.endLat = endLat;
		this.endLng = endLng;
		this.tailMerge = tailMerge;
		this.updateFlg = updateFlg;
	}

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

	public int getUpdateFlg() {
		return updateFlg;
	}

	public void setUpdateFlg(int updateFlg) {
		this.updateFlg = updateFlg;
	}

	public boolean isCount() {
		return isCount;
	}

	public void setCount(boolean count) {
		isCount = count;
	}

	@Override
	public String toString() {
		return "StaytimeParkAlarm [_id=" + _id + ", terminalId=" + terminalId
				+ ", areaId=" + areaId + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", continuousTime=" + continuousTime
				+ ", beginLat=" + beginLat + ", beginLng=" + beginLng
				+ ", endLat=" + endLat + ", endLng=" + endLng + "]";
	}
}