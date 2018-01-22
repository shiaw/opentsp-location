package com.navinfo.opentsp.platform.dp.core.common.entity;

import java.util.List;


public class GpsVehicleStatusAddition {
	
	private List<GpsVehicleStatusData> status;
	public GpsVehicleStatusAddition(){
		
	}
	public GpsVehicleStatusAddition(List<GpsVehicleStatusData> status){
		super();
		this.status = status;
	}
	public List<GpsVehicleStatusData> getStatus() {
		return status;
	}
	public void setStatus(List<GpsVehicleStatusData> status) {
		this.status = status;
	}

}
