package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcTerminalAreaData entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_AREA_DATA")
public class LcTerminalAreaDataDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer tad_id;
	private Integer ta_id;
	private Integer data_sn;
	private Integer data_status;
	private Integer radius_len;
	private Integer latitude;
	private Integer longitude;

	// Constructors

	/** default constructor */
	public LcTerminalAreaDataDBEntity() {
	}
	// Property accessors
	public Integer getTad_id() {
		return tad_id;
	}

	public void setTad_id(Integer tad_id) {
		this.tad_id = tad_id;
	}

	public Integer getTa_id() {
		return ta_id;
	}

	public void setTa_id(Integer ta_id) {
		this.ta_id = ta_id;
	}

	public Integer getData_sn() {
		return data_sn;
	}

	public void setData_sn(Integer data_sn) {
		this.data_sn = data_sn;
	}

	public Integer getData_status() {
		return data_status;
	}

	public void setData_status(Integer data_status) {
		this.data_status = data_status;
	}

	public Integer getLatitude() {
		return latitude;
	}

	public void setLatitude(Integer latitude) {
		this.latitude = latitude;
	}

	public Integer getLongitude() {
		return longitude;
	}

	public void setLongitude(Integer longitude) {
		this.longitude = longitude;
	}
	public Integer getRadius_len() {
		return radius_len;
	}
	public void setRadius_len(Integer radius_len) {
		this.radius_len = radius_len;
	}


}