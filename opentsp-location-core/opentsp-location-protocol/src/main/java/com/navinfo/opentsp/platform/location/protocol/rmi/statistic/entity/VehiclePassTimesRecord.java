package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author HK
 *
 */
public class VehiclePassTimesRecord implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	 * 区域编码（服务站/省/市）
	 */
	private int district;
	/**
	 * 类型  1省2市5服务站
	 */
	private int type;
	/**
	 * 车次
	 */
	private int times;
	/**
	 * 最小时间粒度为天，格式YYYYMMDD
	 */
	private int day;

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getLng() {
		return lng;
	}

	public void setLng(long lng) {
		this.lng = lng;
	}

	/**
	 * 纬度
	 */
	private long lat;
	/**
	 * 经度
	 */
	private long lng;

	public int getDistrictCode() {
		return districtCode;
	}

	public void setDistrictCode(int districtCode) {
		this.districtCode = districtCode;
	}

	/**
	 * 服务站所属市编码
	 */
	private  int districtCode;
	private List<Long> terminals = new ArrayList<>();
	
	public List<Long> getTerminals(){
		return terminals;
	}
	
	private Map<Long, VehiclePassTimesDetail> maps = new HashMap<Long, VehiclePassTimesDetail>();
	
	public VehiclePassTimesDetail getVehiclePassTimesDetail(long tids){
		return maps.get(tids);
	}
	
	public void addVehiclePassTimesDetail(long tid, VehiclePassTimesDetail detail){
		maps.put(tid, detail);
	}
	
	public int getDistrict() {
		return district;
	}
	public void setDistrict(int district) {
		this.district = district;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTimes() {
		return times;
	}
	public void setTimes(int times) {
		this.times = times;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	
}
