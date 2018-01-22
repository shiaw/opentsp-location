package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class VehiclePassInDistrict implements Serializable{
	/**
	 * 区域编码（服务站/省/市）
	 */
	private int district;
	
	private List<Long> terminals = new ArrayList<>();

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}

	public List<Long> getTerminals() {
		return terminals;
	}

	public void setTerminals(List<Long> terminals) {
		this.terminals = terminals;
	}
}
