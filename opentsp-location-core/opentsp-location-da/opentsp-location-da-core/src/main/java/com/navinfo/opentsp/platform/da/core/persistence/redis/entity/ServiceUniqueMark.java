package com.navinfo.opentsp.platform.da.core.persistence.redis.entity;

import java.io.Serializable;
import java.util.Map;

public class ServiceUniqueMark implements Serializable{

	private static final long serialVersionUID = -838589348965574993L;

	/**
	 * 服务标识
	 */
	private long uniqueMark;

	/**
	 * 云分区编码
	 */
	private int districtCode;

	/**
	 * ip
	 */
	private String ip;
	/**
	 * 状态
	 */
	private int status;
	/**
	 * 活动时间
	 */
	private long actionTime;
	/**
	 * 鉴权时间
	 */
	private long authTime;
	/**
	 * 登陆时间
	 */
	private long loginTime;
	/**
	 * 重连时间
	 */
	private long reloginTime;
	/**
	 * 断开时间
	 */
	private long disconnectTime;

	/**
	 * 子标识
	 */
	private Map<Long,String> children;

	/**
	 * 类型，用来区分主从标识，1主标识，0从标识
	 */
	private int type;

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Map<Long, String> getChildren() {
		return children;
	}
	public void setChildren(Map<Long, String> children) {
		this.children = children;
	}
	public long getActionTime() {
		return actionTime;
	}
	public void setActionTime(long actionTime) {
		this.actionTime = actionTime;
	}
	public long getReloginTime() {
		return reloginTime;
	}
	public void setReloginTime(long reloginTime) {
		this.reloginTime = reloginTime;
	}
	public ServiceUniqueMark() {
		super();
	}
	public long getUniqueMark() {
		return uniqueMark;
	}
	public int getDistrictCode() {
		return districtCode;
	}

	public long getLoginTime() {
		return loginTime;
	}
	public void setLoginTime(long loginTime) {
		this.loginTime = loginTime;
	}
	public void setDistrictCode(int districtCode) {
		this.districtCode = districtCode;
	}
	public void setUniqueMark(long uniqueMark) {
		this.uniqueMark = uniqueMark;
	}
	public String getIp() {
		return ip;
	}

	public long getAuthTime() {
		return authTime;
	}
	/**鉴权时间*/
	public void setAuthTime(long authTime) {
		this.authTime = authTime;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public long getDisconnectTime() {
		return disconnectTime;
	}
	public void setDisconnectTime(long disconnectTime) {
		this.disconnectTime = disconnectTime;
	}
	@Override
	public String toString() {
		return "ServiceUniqueMark [uniqueMark=" + uniqueMark
				+ ", districtCode=" + districtCode + ", ip=" + ip + ", status="
				+ status + ", actionTime=" + actionTime + ", authTime="
				+ authTime + ", loginTime=" + loginTime + ", reloginTime="
				+ reloginTime + ", disconnectTime=" + disconnectTime
				+ ", children=" + children + ", type=" + type + "]";
	}


}
