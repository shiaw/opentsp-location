package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa;

public class LcTerminalAreaInfoData implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer data_sn;
	private Integer data_status;
	private Integer radius_len;
	private Integer latitude;
	private Integer longitude;
	public Integer getData_sn() {
		return data_sn;
	}
	public void setData_sn(Integer data_sn) {
		this.data_sn = data_sn;
	}
	public Integer getData_status() {
		return data_status;
	}
	public void setData_status(Integer data_status) {
		this.data_status = data_status;
	}
	public Integer getRadius_len() {
		return radius_len;
	}
	public void setRadius_len(Integer radius_len) {
		this.radius_len = radius_len;
	}
	public Integer getLatitude() {
		return latitude;
	}
	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}
	public Integer getLongitude() {
		return longitude;
	}
	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}
}
