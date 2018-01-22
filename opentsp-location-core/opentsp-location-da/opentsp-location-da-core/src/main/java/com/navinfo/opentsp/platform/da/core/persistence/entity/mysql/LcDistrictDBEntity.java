package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcDistrict entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_DISTRICT")
public class LcDistrictDBEntity implements java.io.Serializable {

	// Fields
	@LCTransient
	@LCPrimaryKey
	private Integer district_id;
	private Integer dict_code;
	private Integer parent_code;

	// Constructors

	/** default constructor */
	public LcDistrictDBEntity() {
	}
	// Property accessors
	public Integer getDistrict_id() {
		return district_id;
	}

	public void setDistrict_id(Integer district_id) {
		this.district_id = district_id;
	}

	public Integer getDict_code() {
		return dict_code;
	}

	public void setDict_code(Integer dict_code) {
		this.dict_code = dict_code;
	}

	public Integer getParent_code() {
		return parent_code;
	}

	public void setParent_code(Integer parent_code) {
		this.parent_code = parent_code;
	}

	
}