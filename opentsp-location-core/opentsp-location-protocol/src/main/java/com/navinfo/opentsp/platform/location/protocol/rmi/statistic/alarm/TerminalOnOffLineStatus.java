package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;


/**
 * 终端在线状态统计信息
 * @author hws
 *
 */
public class TerminalOnOffLineStatus implements Serializable{
	private static final long serialVersionUID = 1L;
	private long terminalId;//终端标识号（索引）
	private int	onlineStatus;//	0：离线；1：在线
	private long	beginDate;//	报警开始时间
	private long	endDate;//	报警结束时间
	private int	continuousTime;//	报警持续时间长度
    public TerminalOnOffLineStatus(){
		
	}
	public long getTerminalId() {
		return terminalId;
	}
    public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
    }
	public int getOnlineStatus() {
		return onlineStatus;
	}
	public void setOnlineStatus(int onlineStatus) {
		this.onlineStatus = onlineStatus;
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
	@Override
	public String toString() {
		return "TerminalOnOffLineStatus [terminalId=" + terminalId
				+ ", onlineStatus=" + onlineStatus + ", beginDate=" + beginDate
				+ ", endDate=" + endDate + ", continuousTime=" + continuousTime
				+ "]";
	}
	
	

}
