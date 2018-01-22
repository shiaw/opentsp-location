package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;

/**
 * 紧急报警统计信息
 * @author hws
 *
 */
public class EmergencyAlarm implements Serializable{
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	private long	beginDate;//	报警开始时间
	private long	endDate	;//报警结束时间
	private int	continuousTime;//	报警持续时间长度
	private int	beginLat	;//开始纬度
	private int	beginLng	;//开始经度
	private int	endLat	;//结束纬度
	private int	endLng	;//结束经度
	
	public EmergencyAlarm(){
			
	}
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
			this.terminalId = terminalId;
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
	public void setContinuousTime(int l) {
		this.continuousTime = l;
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
		return "EmergencyAlarm [terminalId=" + terminalId + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", continuousTime="
				+ continuousTime + ", beginLat=" + beginLat + ", beginLng="
				+ beginLng + ", endLat=" + endLat + ", endLng=" + endLng + "]";
	}

}
