package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 主电源欠压统计信息
 * @author hws
 *
 */
public class PowerStayInLowerAlarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	long	beginDate;//	报警开始时间
	long	endDate;//	报警结束时间
	int	continuousTime;//	报警持续时间长度
	int	beginLat;//	开始纬度
	int	beginLng;//	开始经度
	int	endLat;//	结束纬度
	int	endLng;//	结束经度
	public PowerStayInLowerAlarm(){
			
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
		return "PowerStayInLowerAlarm [terminalId=" + terminalId
				+ ", beginDate=" + beginDate + ", endDate=" + endDate
				+ ", continuousTime=" + continuousTime + ", beginLat="
				+ beginLat + ", beginLng=" + beginLng + ", endLat=" + endLat
				+ ", endLng=" + endLng + "]";
	}

}
