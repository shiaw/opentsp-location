package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcDict entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_DICT")
public class LcDictDBEntity implements java.io.Serializable {

	@LCTransient
	@LCPrimaryKey
	private Integer dict_id;
	private Integer dict_code;
	private Integer gb_code;
	private Integer dt_code;
	private String dict_name;
	private String dict_data;

	// Constructors

	/** default constructor */
	public LcDictDBEntity() {
	}

	public Integer getDict_id() {
		return dict_id;
	}

	public void setDict_id(Integer dict_id) {
		this.dict_id = dict_id;
	}

	public Integer getDict_code() {
		return dict_code;
	}

	public void setDict_code(Integer dict_code) {
		this.dict_code = dict_code;
	}

	public Integer getGb_code() {
		return gb_code;
	}

	public void setGb_code(Integer gb_code) {
		this.gb_code = gb_code;
	}

	

	public Integer getDt_code() {
		return dt_code;
	}

	public void setDt_code(Integer dt_code) {
		this.dt_code = dt_code;
	}

	public String getDict_name() {
		return dict_name;
	}

	public void setDict_name(String dict_name) {
		this.dict_name = dict_name;
	}

	public String getDict_data() {
		return dict_data;
	}

	public void setDict_data(String dict_data) {
		this.dict_data = dict_data;
	}

	@Override
	public String toString() {
		return "LcDictDBEntity [dict_id=" + dict_id + ", dict_code="
				+ dict_code + ", gb_code=" + gb_code + ", dt_code=" + dt_code
				+ ", dict_name=" + dict_name + ", dict_data=" + dict_data + "]";
	}
}