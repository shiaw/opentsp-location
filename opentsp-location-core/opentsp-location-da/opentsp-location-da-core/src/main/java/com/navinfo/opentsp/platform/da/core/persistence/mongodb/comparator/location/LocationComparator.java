package com.navinfo.opentsp.platform.da.core.persistence.mongodb.comparator.location;

import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;

import java.util.Comparator;



public class LocationComparator implements Comparator<LCLocationData.LocationData>{

	@Override
	public int compare(LCLocationData.LocationData o1, LCLocationData.LocationData o2) {
		int flag=(String.valueOf(o1.getGpsDate())).compareTo(String.valueOf(o2.getGpsDate()));
		return flag;
	}
}
