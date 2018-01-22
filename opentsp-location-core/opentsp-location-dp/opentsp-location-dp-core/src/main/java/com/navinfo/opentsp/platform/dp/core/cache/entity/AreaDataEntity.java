package com.navinfo.opentsp.platform.dp.core.cache.entity;

public class AreaDataEntity extends Entity implements Cloneable {
	private static final long serialVersionUID = 1L;
	private int dataSn;
	private int longitude;
	private int latitude;
	private int radiusLength;
	private long dataStatus;


	@Override
	public AreaDataEntity clone(){
		try {
			AreaDataEntity point = (AreaDataEntity) super.clone();
			point.setLatitude(latitude);
			point.setLongitude(longitude);
			point.setDataSn(dataSn);
			return point;
		} catch (CloneNotSupportedException e) {
		}
		return this;
	}

	public AreaDataEntity(int longitude, int latitude) {
		super();
		this.longitude = longitude;
		this.latitude = latitude;
	}

	@Override
	public String toString() {
		return "AreaDataEntity [dataSn=" + dataSn + ", longitude=" + longitude
				+ ", latitude=" + latitude + ", radiusLength=" + radiusLength
				+ ", dataStatus=" + dataStatus + "]";
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

	public AreaDataEntity() {
		super();
	}

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

}
