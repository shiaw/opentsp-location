package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcTerminalArea entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_AREA")
public class LcTerminalAreaLogDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer ta_id;
	private Integer original_area_id;
	private Integer area_type;
	private Integer create_time;
	private Long terminal_id;

	// Constructors

	/** default constructor */
	public LcTerminalAreaLogDBEntity() {
	}
	// Property accessors
	public Integer getTa_id() {
		return ta_id;
	}

	public void setTa_id(Integer ta_id) {
		this.ta_id = ta_id;
	}

	public Integer getOriginal_area_id() {
		return original_area_id;
	}

	public void setOriginal_area_id(Integer original_area_id) {
		this.original_area_id = original_area_id;
	}

	public Integer getArea_type() {
		return area_type;
	}

	public void setArea_type(Integer area_type) {
		this.area_type = area_type;
	}

	public Integer getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}
	public Long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	
	
}