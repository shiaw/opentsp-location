package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import java.io.Serializable;

/**
 * 默认超时时间：300秒
 * 
 * @author lgw
 * 
 */
public class AnswerEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	private String uniqueMark;
	private int internalCommand;
	private long channelId;
	private int serialNumber;
	private int terminalCommand;
	private long createTime;
	private int timeout;
    private String downCmd;

	public AnswerEntry() {
		this.createTime = System.currentTimeMillis() / 1000;
		this.timeout = 300;
	}

	public String getUniqueMark() {
		return uniqueMark;
	}

	public void setUniqueMark(String uniqueMark) {
		this.uniqueMark = uniqueMark;
	}

	public long getCreateTime() {
		return createTime;
	}

	public int getInternalCommand() {
		return internalCommand;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setInternalCommand(int internalCommand) {
		this.internalCommand = internalCommand;
	}

	public long getChannelId() {
		return channelId;
	}

	public void setChannelId(long channelId) {
		this.channelId = channelId;
	}

	public int getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(int serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getTerminalCommand() {
		return terminalCommand;
	}

	public void setTerminalCommand(int terminalCommand) {
		this.terminalCommand = terminalCommand;
	}

    public String getDownCmd() {
        return downCmd;
    }

    public void setDownCmd(String downCmd) {
        this.downCmd = downCmd;
    }
}
