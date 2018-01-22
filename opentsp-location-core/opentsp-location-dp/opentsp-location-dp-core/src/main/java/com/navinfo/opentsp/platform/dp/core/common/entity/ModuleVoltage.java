package com.navinfo.opentsp.platform.dp.core.common.entity;

public class ModuleVoltage {
	private int	moduleNum = 0;
	private int	number = 0;
	private float voltage = 0;
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
