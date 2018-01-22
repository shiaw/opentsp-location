package com.navinfo.tasktracker.rprest.entity;

public class AreaDataEntity {
	private int dataSn;//序号
	private int longitude;//区域经度
	private int latitude;//区域纬度
	private int radiusLength;//点/圆形半径/路段宽度，路段时代表该拐点和下一拐点的宽度，单位米
	private long dataStatus;//数据标识（路段标识，两个拐点组成路段）
	public int getDataSn() {
		return dataSn;
	}
	public void setDataSn(int dataSn) {
		this.dataSn = dataSn;
	}
	public int getLongitude() {
		return longitude;
	}
	public void setLongitude(int longitude) {
		this.longitude = longitude;
	}
	public int getLatitude() {
		return latitude;
	}
	public void setLatitude(int latitude) {
		this.latitude = latitude;
	}
	public int getRadiusLength() {
		return radiusLength;
	}
	public void setRadiusLength(int radiusLength) {
		this.radiusLength = radiusLength;
	}
	public long getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(long dataStatus) {
		this.dataStatus = dataStatus;
	}

}
