package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave;

import java.io.Serializable;


public class TempGpsDataEntry implements Serializable {
	private static final long serialVersionUID = 1L;
	private long terminalId;
	private long gpsTime;
	private String day;
	private LCLocationData.LocationData locationData;

	public TempGpsDataEntry() {
		super();
	}

	public TempGpsDataEntry( LCLocationDataSave.LocationDataSave locationDataSave) {
		this.terminalId = locationDataSave.getTerminalId();
		this.locationData = locationDataSave.getLocationData();
		this.gpsTime = locationData.getGpsDate();
		this.day = DateUtils.format(locationData.getGpsDate(), DateUtils.DateFormat.YYYYMMDD);
	}

	public long getTerminalId() {
		return terminalId;
	}

	public void setTerminalId(long terminalId) {
		this.terminalId = terminalId;
	}

	public long getGpsTime() {
		return gpsTime;
	}

	public void setGpsTime(long gpsTime) {
		this.gpsTime = gpsTime;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public String getDay() {
		return day;
	}

	public LCLocationData.LocationData getLocationData() {
		return locationData;
	}

	public void setLocationData(LCLocationData.LocationData locationData) {
		this.locationData = locationData;
	}

}
