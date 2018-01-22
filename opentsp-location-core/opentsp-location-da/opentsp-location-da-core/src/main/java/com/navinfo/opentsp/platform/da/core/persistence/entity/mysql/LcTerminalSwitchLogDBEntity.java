package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcTerminalSwitchLog entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_SWITCH_LOG")
public class LcTerminalSwitchLogDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer tsl_id;
	private long terminal_id;
	private Integer switch_type;
	private Integer switch_time;

	// Constructors

	/** default constructor */
	public LcTerminalSwitchLogDBEntity() {
	}

	// Property accessors
	public Integer getTsl_id() {
		return tsl_id;
	}

	public void setTsl_id(Integer tsl_id) {
		this.tsl_id = tsl_id;
	}

	public long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getSwitch_type() {
		return switch_type;
	}

	public void setSwitch_type(Integer switch_type) {
		this.switch_type = switch_type;
	}

	public Integer getSwitch_time() {
		return switch_time;
	}

	public void setSwitch_time(Integer switch_time) {
		this.switch_time = switch_time;
	}


}