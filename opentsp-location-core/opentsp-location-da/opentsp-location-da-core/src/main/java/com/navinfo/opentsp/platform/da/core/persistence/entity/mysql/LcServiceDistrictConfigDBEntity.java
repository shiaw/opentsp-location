package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;

import java.io.Serializable;


@LCTable(name="LC_SERVICE_DISTRICT_CONFIG")
public class LcServiceDistrictConfigDBEntity implements Serializable {

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer sdc_id;
	private Integer sc_id;
	private Integer service_district;

	public LcServiceDistrictConfigDBEntity() {
	}

	public Integer getSdc_id() {
		return sdc_id;
	}

	public void setSdc_id(Integer sdc_id) {
		this.sdc_id = sdc_id;
	}

	public Integer getSc_id() {
		return sc_id;
	}

	public void setSc_id(Integer sc_id) {
		this.sc_id = sc_id;
	}

	public Integer getService_district() {
		return service_district;
	}

	public void setService_district(Integer service_district) {
		this.service_district = service_district;
	}

}
