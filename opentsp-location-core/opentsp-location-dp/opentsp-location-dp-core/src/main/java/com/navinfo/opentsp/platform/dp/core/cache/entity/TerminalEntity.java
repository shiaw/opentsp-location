package com.navinfo.opentsp.platform.dp.core.cache.entity;

public class TerminalEntity extends Entity {
	private static final long serialVersionUID = 1L;
	private long terminalId;
	private int protocolType;
	private String authCode;
	private boolean auth;
	private String deviceId;
	private long changeTid;
	private long lastTime;
	public TerminalEntity() {
		super();
	}

	public long getLastTime() {
		return lastTime;
	}

	public void setLastTime(long lastTime) {
		this.lastTime = lastTime;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public long getChangeTid() {
		return changeTid;
	}

	public void setChangeTid(long changeTid) {
		this.changeTid = changeTid;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public int getProtocolType() {
		return protocolType;
	}

	public void setProtocolType(int protocolType) {
		this.protocolType = protocolType;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String authCode) {
		this.authCode = authCode;
	}

	public boolean isRegularInTerminal() {
		return auth;
	}

	public void setAuth(boolean auth) {
		this.auth = auth;
	}
}
