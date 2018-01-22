package com.navinfo.opentsp.platform.da.core.persistence.redis.entity;

import java.io.Serializable;

/**
 * 终端状态
 *
 * @author lgw
 *
 */
public class RDTerminalStatus implements Serializable {
	private static final long serialVersionUID = 1L;
	private long terminalId;
	private long accessIp;
	private long accessPort;
	private int status;
	private long statusTime;
	private long fristConnectTime;

	public RDTerminalStatus() {
		super();
	}

	public long getTerminalId() {
		return terminalId;
	}

	/** 终端标识 */
	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public long getAccessIp() {
		return accessIp;
	}

	/** 接入ip */
	public void setAccessIp(long accessIp) {
		this.accessIp = accessIp;
	}

	public long getAccessPort() {
		return accessPort;
	}

	/** 接入port */
	public void setAccessPort(long accessPort) {
		this.accessPort = accessPort;
	}

	public int getStatus() {
		return status;
	}

	/** 当前状态 */
	public void setStatus(int status) {
		this.status = status;
	}

	public long getStatusTime() {
		return statusTime;
	}

	/** 当前状态发生时间 */
	public void setStatusTime(long statusTime) {
		this.statusTime = statusTime;
	}

	public long getFristConnectTime() {
		return fristConnectTime;
	}

	/** 首次连接服务时间 */
	public void setFristConnectTime(long fristConnectTime) {
		this.fristConnectTime = fristConnectTime;
	}

}
