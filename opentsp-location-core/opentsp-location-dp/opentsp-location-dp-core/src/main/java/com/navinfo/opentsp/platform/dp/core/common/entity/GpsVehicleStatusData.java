package com.navinfo.opentsp.platform.dp.core.common.entity;

import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;

public class GpsVehicleStatusData {
	
	StatusType	types;	//状态类型
	long	statusValue;	//状态数值，所有数据都乘于100取整。
	
	public GpsVehicleStatusData(){}
	
	public GpsVehicleStatusData(StatusType	types,int	statusValue){
		super();
		this.types = types;
		this.statusValue = statusValue;
	}

	public StatusType getTypes() {
		return types;
	}

	public void setTypes(StatusType types) {
		this.types = types;
	}

	public long getStatusValue() {
		return statusValue;
	}

	public void setStatusValue(long statusValue) {
		this.statusValue = statusValue;
	}

}
