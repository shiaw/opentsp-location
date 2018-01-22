package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 疲劳驾驶统计信息
 * @author hws
 *
 */
public class FatigueAlarm implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	private long	beginDate;//	报警开始时间
	private long	endDate	;//报警结束时间
	private int	continuousTime;//	报警持续时间长度
	private int	limitDriving;//  	单次疲劳驾驶时间阀值
	private int	limitDayDriving;//	当天疲劳驾驶时间阀值
	private int	limitRest;//	疲劳驾驶休息时间阀值
	private int	beginLat;//	开始纬度
	private int	beginLng;//	开始经度
	private int	endLat;//结束纬度
	private int	endLng;//结束经度
	public FatigueAlarm(){
			
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
	public int getLimitDriving() {
		return limitDriving;
	}
	public void setLimitDriving(int limitDriving) {
		this.limitDriving = limitDriving;
	}
	public int getLimitDayDriving() {
		return limitDayDriving;
	}
	public void setLimitDayDriving(int limitDayDriving) {
		this.limitDayDriving = limitDayDriving;
	}
	public int getLimitRest() {
		return limitRest;
	}
	public void setLimitRest(int limitRest) {
		this.limitRest = limitRest;
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
		return "FatigueAlarm [terminalId=" + terminalId + ", beginDate="
				+ beginDate + ", endDate=" + endDate + ", continuousTime="
				+ continuousTime + ", limitDriving=" + limitDriving
				+ ", limitDayDriving=" + limitDayDriving + ", limitRest="
				+ limitRest + ", beginLat=" + beginLat + ", beginLng="
				+ beginLng + ", endLat=" + endLat + ", endLng=" + endLng + "]";
	}
}
