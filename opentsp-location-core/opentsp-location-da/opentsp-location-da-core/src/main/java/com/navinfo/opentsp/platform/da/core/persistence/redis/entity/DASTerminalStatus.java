package com.navinfo.opentsp.platform.da.core.persistence.redis.entity;

import java.io.Serializable;

public class DASTerminalStatus implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private  DASTerminalStatusEntry terminalStatusEntry;
	private long terminalId;
	
	private int computerDate;
	private int status;
	public DASTerminalStatusEntry getTerminalStatusEntry() {
		return terminalStatusEntry;
	}
	public void setTerminalStatusEntry(DASTerminalStatusEntry terminalStatusEntry) {
		this.terminalStatusEntry = terminalStatusEntry;
	}
	public int getComputerDate() {
		return computerDate;
	}
	public void setComputerDate(int computerDate) {
		this.computerDate = computerDate;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public long getTerminalId() {
		return terminalId;
	}
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}
}
