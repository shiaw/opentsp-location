package com.navinfo.opentsp.platform.da.core.webService.manage;

import java.io.Serializable;

public class WSConfigCache implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1137235491626455405L;
	
	private String ip;
	private String port;
	private String district;
	private int  nodeCode;
	boolean isAvailable =true;
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
	public int getNodeCode() {
		return nodeCode;
	}
	public void setNodeCode(int nodeCode) {
		this.nodeCode = nodeCode;
	}
	public boolean isAvailable() {
		return isAvailable;
	}
	public void setAvailable(boolean isAvailable) {
		this.isAvailable = isAvailable;
	}

}
