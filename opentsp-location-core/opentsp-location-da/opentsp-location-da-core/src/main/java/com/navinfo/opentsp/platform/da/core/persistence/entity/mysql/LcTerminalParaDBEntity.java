package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcTerminalPara entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_PARA")
public class LcTerminalParaDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer para_id;
	private Long terminal_id;
	private Integer para_code;
	private byte[] para_content;
	private Long last_update_time;

	// Constructors

	/** default constructor */

	public LcTerminalParaDBEntity() {
	}

	// Property accessors
	public Integer getPara_id() {
		return para_id;
	}

	public void setPara_id(Integer para_id) {
		this.para_id = para_id;
	}

	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getPara_code() {
		return para_code;
	}

	public void setPara_code(Integer para_code) {
		this.para_code = para_code;
	}


	public byte[] getPara_content() {
		return para_content;
	}

	public void setPara_content(byte[] para_content) {
		this.para_content = para_content;
	}

	public Long getLast_update_time() {
		return last_update_time;
	}

	public void setLast_update_time(Long last_update_time) {
		this.last_update_time = last_update_time;
	}

	
}