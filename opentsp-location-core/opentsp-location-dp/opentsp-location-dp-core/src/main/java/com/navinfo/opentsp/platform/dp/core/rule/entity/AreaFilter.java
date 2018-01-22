package com.navinfo.opentsp.platform.dp.core.rule.entity;

public class AreaFilter {

	private long areaId;
	private long filterTime;//屏蔽时间
	public long getAreaId() {
		return areaId;
	}
	public void setAreaId(long areaId) {
		this.areaId = areaId;
	}
	public long getFilterTime() {
		return filterTime;
	}
	public void setFilterTime(long filterTime) {
		this.filterTime = filterTime;
	}
	
}
