package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import java.io.Serializable;

public class TerminalOnOffStatus  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long terminalId;
    
	private int status;
	
	private long startTime;
	
	private long endTime;

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	
	
}
