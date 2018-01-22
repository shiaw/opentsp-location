package com.navinfo.opentsp.platform.dp.core.cache.entity;

import org.springframework.beans.factory.annotation.Value;

public class SettingParameterEntry extends Entity {
	private static final long serialVersionUID = 1L;
	private long terminalId;
	private int commandCode;

	@Value("${dp_terminalSettingThreshold}")
	private int timeOut;
	private long createTime;
	private byte[] paraContent;

	public SettingParameterEntry() {
		this.createTime = System.currentTimeMillis()/1000;
	}
	public boolean isTimeOut(){
		long currentTime = System.currentTimeMillis()/1000;
		return currentTime - this.createTime > this.getTimeOut() ? true : false;
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public int getCommandCode() {
		return commandCode;
	}

	public void setCommandCode(int commandCode) {
		this.commandCode = commandCode;
	}

	public byte[] getParaContent() {
		return paraContent;
	}

	public void setParaContent(byte[] paraContent) {
		this.paraContent = paraContent;
	}

	public int getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(int timeOut) {
		this.timeOut = timeOut;
	}

}
