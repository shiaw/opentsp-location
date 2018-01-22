package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm;

import java.io.Serializable;

public class WFSummary  implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long   terminalId;
	private long    beginDate;
	private long    endDate;   
	private float	upFlow;    //上行流量
	private float	downFlow;  //下行流量
	
	
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
	public float getUpFlow() {
		return upFlow;
	}
	public void setUpFlow(float upFlow) {
		this.upFlow = upFlow;
	}
	public float getDownFlow() {
		return downFlow;
	}
	public void setDownFlow(float downFlow) {
		this.downFlow = downFlow;
	}
}
