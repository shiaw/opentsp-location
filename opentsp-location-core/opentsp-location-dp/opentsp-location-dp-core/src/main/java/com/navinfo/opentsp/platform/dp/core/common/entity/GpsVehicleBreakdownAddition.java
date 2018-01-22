package com.navinfo.opentsp.platform.dp.core.common.entity;

import java.util.List;

public class GpsVehicleBreakdownAddition {

	private List<GpsVehicleBreakdown> breakdowns;
	
	public GpsVehicleBreakdownAddition(){
		
	}
	public GpsVehicleBreakdownAddition(List<GpsVehicleBreakdown> breakdowns){
		super();
		this.breakdowns = breakdowns;
	}
	
	public List<GpsVehicleBreakdown> getBreakdowns() {
		return breakdowns;
	}
	public void setBreakdowns(List<GpsVehicleBreakdown> breakdowns) {
		this.breakdowns = breakdowns;
	}
	
}
