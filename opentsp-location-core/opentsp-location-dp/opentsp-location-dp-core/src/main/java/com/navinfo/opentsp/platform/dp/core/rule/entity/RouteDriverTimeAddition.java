package com.navinfo.opentsp.platform.dp.core.rule.entity;


public class RouteDriverTimeAddition {
	long areaId;
	int routeTime;
	boolean routeResult;
	long lineId;

	public RouteDriverTimeAddition(long areaId, int routeTime, boolean routeResult ,long lineId) {
		this.areaId = areaId;
		this.routeTime = routeTime;
		this.routeResult = routeResult;
		this.lineId = lineId;
	}

	public long getAreaId() {
		return areaId;
	}

	public long getLineId() {
		return lineId;
	}

	public int getRouteTime() {
		return routeTime;
	}

	public boolean isRouteResult() {
		return routeResult;
	}

}