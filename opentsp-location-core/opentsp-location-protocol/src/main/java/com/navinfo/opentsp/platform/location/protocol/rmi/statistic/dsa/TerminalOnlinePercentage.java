package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa;

public class TerminalOnlinePercentage  implements java.io.Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long	terminalId;//终端标识
	private int	statisticDay;//	统计天数（结束时间，开始时间跨度的天数）
	private int	onlineDay	;//在线天数（终端当天只要曾经上过线，当天就算在线）
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
	public int getStatisticDay() {
		return statisticDay;
	}
	public void setStatisticDay(int statisticDay) {
		this.statisticDay = statisticDay;
	}
	public int getOnlineDay() {
		return onlineDay;
	}
	public void setOnlineDay(int onlineDay) {
		this.onlineDay = onlineDay;
	}
	public float getOnlinePercentage() {
		return onlinePercentage;
	}
	public void setOnlinePercentage(float onlinePercentage) {
		this.onlinePercentage = onlinePercentage;
	}
	private float	onlinePercentage;//	在线百分比

}
