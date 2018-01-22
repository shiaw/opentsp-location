package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa;


/**
 * LcTerminalRule entity. @author MyEclipse Persistence Tools
 */

public class LcTerminalRuleEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer tr_id;
	private long terminal_id;
	private long original_area_id;
	private Integer business_type;
	private Integer rule_type;
	private byte[] rule_content;
	private Integer last_update_time;
	

	// Constructors

	/** default constructor */
	public LcTerminalRuleEntity() {
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

	public Integer getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(Integer business_type) {
		this.business_type = business_type;
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
	
	public long getOriginal_area_id() {
		return original_area_id;
	}
	public void setOriginal_area_id(long original_area_id) {
		this.original_area_id = original_area_id;
	}
	

}