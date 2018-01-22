package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcTerminalOperationLog entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_OPERATION_LOG")
public class LcTerminalOperationLogDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer tol_id;
	private Long terminal_id;
	private Integer operation_type;
	private Integer operation_time;
	private byte[] operation_content;
	private Integer operator_result;

	// Constructors

	/** default constructor */
	public LcTerminalOperationLogDBEntity() {
	}
	// Property accessors
	public Integer getTol_id() {
		return tol_id;
	}

	public void setTol_id(Integer tol_id) {
		this.tol_id = tol_id;
	}

	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getOperation_type() {
		return operation_type;
	}

	public void setOperation_type(Integer operation_type) {
		this.operation_type = operation_type;
	}

	public Integer getOperation_time() {
		return operation_time;
	}

	public void setOperation_time(Integer operation_time) {
		this.operation_time = operation_time;
	}

	public byte[] getOperation_content() {
		return operation_content;
	}

	public void setOperation_content(byte[] operation_content) {
		this.operation_content = operation_content;
	}
	public Integer getOperator_result() {
		return operator_result;
	}
	public void setOperator_result(Integer operator_result) {
		this.operator_result = operator_result;
	}
	
}