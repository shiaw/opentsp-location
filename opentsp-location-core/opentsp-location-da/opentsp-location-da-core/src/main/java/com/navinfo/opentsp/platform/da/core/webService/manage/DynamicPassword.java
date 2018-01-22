package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.io.Serializable;

public class DynamicPassword implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4225156628845740662L;

	/**
	 * 动态口令,用uuid生成
	 */
	private String dynamicPassword;

	/**
	 * 会话id
	 */
	private String sessionId;

	/**
	 * 活动时间，会根据活动时间来判断是否失效
	 */
	private long actionTime;

	public long getActionTime() {
		return actionTime;
	}

	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}

	public String getDynamicPassword() {
		return dynamicPassword;
	}

	public void setDynamicPassword(String dynamicPassword) {
		this.dynamicPassword = dynamicPassword;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

}