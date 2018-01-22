package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcFunc entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_FUNC")
public class LcFuncDBEntity implements java.io.Serializable {

	// Fields
	@LCTransient
	@LCPrimaryKey
	private Integer func_id;
	private String func_code;
	private String func_name;
	private Integer parent_func;

	// Constructors

	/** default constructor */
	public LcFuncDBEntity() {
	}

	public Integer getFunc_id() {
		return func_id;
	}

	public void setFunc_id(Integer func_id) {
		this.func_id = func_id;
	}

	public String getFunc_code() {
		return func_code;
	}

	public void setFunc_code(String func_code) {
		this.func_code = func_code;
	}

	public String getFunc_name() {
		return func_name;
	}

	public void setFunc_name(String func_name) {
		this.func_name = func_name;
	}

	public Integer getParent_func() {
		return parent_func;
	}

	public void setParent_func(Integer parent_func) {
		this.parent_func = parent_func;
	}


	
}