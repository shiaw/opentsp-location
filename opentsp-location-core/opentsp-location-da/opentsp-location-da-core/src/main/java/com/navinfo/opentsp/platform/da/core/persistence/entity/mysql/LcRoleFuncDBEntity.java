package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcRoleFunc entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_ROLE_FUNC")
public class LcRoleFuncDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer rf_Id;
	private Integer role_id;
	private Integer func_id;

	// Constructors

	/** default constructor */
	public LcRoleFuncDBEntity() {
	}


	public Integer getRf_Id() {
		return rf_Id;
	}

	public void setRf_Id(Integer rf_Id) {
		this.rf_Id = rf_Id;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public Integer getFunc_id() {
		return func_id;
	}

	public void setFunc_id(Integer func_id) {
		this.func_id = func_id;
	}

	// Property accessors

	

}