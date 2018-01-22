package com.navinfo.opentsp.platform.dp.core.common.entity;

import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;

public class GpsOvertimeParkingAddition {
	
	AreaType	areaType;	//
	long	areaId;	// 
	
	public GpsOvertimeParkingAddition(){}
	
	public GpsOvertimeParkingAddition(AreaType	areaType,long	areaId){
		super();
		this.areaType = areaType;
		this.areaId = areaId;
	}

	public AreaType getAreaType() {
		return areaType;
	}

	public void setAreaType(AreaType areaType) {
		this.areaType = areaType;
	}

	public long getAreaId() {
		return areaId;
	}

	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}

	
}
