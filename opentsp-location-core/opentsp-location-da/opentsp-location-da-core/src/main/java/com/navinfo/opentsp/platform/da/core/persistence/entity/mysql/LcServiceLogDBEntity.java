package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcServiceLog entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_SERVICE_LOG")
public class LcServiceLogDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer sl_id;
	private Integer log_time;
	private String log_content;
	private Integer log_district;
	private String log_ip;
	private Integer log_typecode;

	// Constructors

	public Integer getLog_typecode() {
		return log_typecode;
	}

	public void setLog_typecode(Integer log_typecode) {
		this.log_typecode = log_typecode;
	}

	/** default constructor */
	public LcServiceLogDBEntity() {
	}

	// Property accessors

	public Integer getSl_id() {
		return sl_id;
	}


	public void setSl_id(Integer sl_id) {
		this.sl_id = sl_id;
	}


	public Integer getLog_district() {
		return log_district;
	}

	public void setLog_district(Integer log_district) {
		this.log_district = log_district;
	}

	public String getLog_ip() {
		return log_ip;
	}

	public void setLog_ip(String log_ip) {
		this.log_ip = log_ip;
	}

	public Integer getLog_time() {
		return log_time;
	}

	public void setLog_time(Integer log_time) {
		this.log_time = log_time;
	}

	public String getLog_content() {
		return log_content;
	}

	public void setLog_content(String log_content) {
		this.log_content = log_content;
	}
	
}