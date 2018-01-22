package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcTerminalRule entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_RULE")
public class DSATerminalRuleDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer tr_id;
	private Long terminal_id;
	private Long original_area_id;
	public Long getOriginal_area_id() {
		return original_area_id;
	}
	private Long original_line_id;
	private Integer area_type;
	private Integer rule_type;
	private byte[] rule_content;
	
	private Integer last_update_time;
	

	// Constructors

	public Long getOriginal_line_id() {
		return original_line_id;
	}
	public void setOriginal_line_id(Long original_line_id) {
		this.original_line_id = original_line_id;
	}
	public void setOriginal_area_id(Long original_area_id) {
		this.original_area_id = original_area_id;
	}
	/** default constructor */
	public DSATerminalRuleDBEntity() {
	}
	// Property accessors
	public Integer getTr_id() {
		return tr_id;
	}

	public void setTr_id(Integer tr_id) {
		this.tr_id = tr_id;
	}

	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getRule_type() {
		return rule_type;
	}

	public void setRule_type(Integer rule_type) {
		this.rule_type = rule_type;
	}

	public byte[] getRule_content() {
		return rule_content;
	}

	public void setRule_content(byte[] rule_content) {
		this.rule_content = rule_content;
	}

	public Integer getLast_update_time() {
		return last_update_time;
	}

	public void setLast_update_time(Integer last_update_time) {
		this.last_update_time = last_update_time;
	}
	
	
	public Integer getArea_type() {
		return area_type;
	}
	public void setArea_type(Integer area_type) {
		this.area_type = area_type;
	}

}