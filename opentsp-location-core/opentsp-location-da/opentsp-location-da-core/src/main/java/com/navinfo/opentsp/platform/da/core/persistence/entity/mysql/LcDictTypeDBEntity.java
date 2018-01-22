package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcDictType entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_DICT_TYPE")
public class LcDictTypeDBEntity implements java.io.Serializable {
	
	// Fields
	@LCTransient
	@LCPrimaryKey
	private Integer dt_code;
	private String dict_name;
	private String dict_desc;

	// Constructors

	/** default constructor */
	public LcDictTypeDBEntity() {
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

	public String getDict_desc() {
		return dict_desc;
	}

	public void setDict_desc(String dict_desc) {
		this.dict_desc = dict_desc;
	}

	

}