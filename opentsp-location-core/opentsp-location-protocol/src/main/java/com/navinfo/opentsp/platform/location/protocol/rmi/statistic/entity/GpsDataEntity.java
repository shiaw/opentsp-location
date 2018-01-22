package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class GpsDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;
	private long _id;
	private long tId;
	private String day;
	private int total;
	private List<GpsDetailedEntity> dataList = new ArrayList<GpsDetailedEntity>();

	public GpsDataEntity() {
		super();
	}
	
	public void addAllGpsDetailed(List<GpsDetailedEntity> detailedEntities){
		this.dataList.addAll(detailedEntities);
	}
	
	public void addGpsDetailed(GpsDetailedEntity detailedEntity) {
		this.dataList.add(detailedEntity);
	}

	public GpsDetailedEntity newGpsDetailedEntity() {
		return new GpsDetailedEntity();
	}
	public long get_id() {
		return _id;
	}

	public long gettId() {
		return tId;
	}

	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public void settId(long tId) {
		this.tId = tId;
	}

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	public List<GpsDetailedEntity> getDataList() {
		return dataList;
	}

	public void setDataList(List<GpsDetailedEntity> dataList) {
		this.dataList = dataList;
	}

}
