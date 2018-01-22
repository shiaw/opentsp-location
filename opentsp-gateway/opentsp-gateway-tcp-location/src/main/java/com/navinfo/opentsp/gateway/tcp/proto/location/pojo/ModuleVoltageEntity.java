package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import java.io.Serializable;

public class ModuleVoltageEntity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int moduleNum;
	private int number;
	private float voltage;

	public int getModuleNum() {
		return moduleNum;
	}
	public void setModuleNum(int moduleNum) {
		this.moduleNum = moduleNum;
	}
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public float getVoltage() {
		return voltage;
	}
	public void setVoltage(float voltage) {
		this.voltage = voltage;
	}
}