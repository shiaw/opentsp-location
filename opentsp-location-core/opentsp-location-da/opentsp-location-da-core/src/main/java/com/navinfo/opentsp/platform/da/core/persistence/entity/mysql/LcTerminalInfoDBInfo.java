package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;



/**
 * LcTerminalInfo entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_INFO")
public class LcTerminalInfoDBInfo implements java.io.Serializable {

	// Fields

	/**
	 *
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer ti_id;
	private Long terminal_id;
	private Integer proto_code;
	private Integer district;
	private Integer node_code;
	private Integer regular_in_terminal;
	private Integer create_time;
	private Integer data_status;
	private String auth_code;

	/**
	 * 江淮鉴权新增begin**
	 */
	private String device_id;//设备ID（江淮，鉴权）
	private long change_tid;//变更的终端通信标识
	private int business_type;//0默认，1江淮

	public String getDevice_id() {
		return device_id;
	}

	public long getChange_tid() {
		return change_tid;
	}

	public void setChange_tid(long change_tid) {
		this.change_tid = change_tid;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}


	public int getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(int business_type) {
		this.business_type = business_type;
	}

	/***江淮鉴权新增end***/
	// Constructors


	/**
	 * default constructor
	 */
	public LcTerminalInfoDBInfo() {
	}

	// Property accessors

	public Integer getTi_id() {
		return ti_id;
	}

	public void setTi_id(Integer ti_id) {
		this.ti_id = ti_id;
	}

	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getProto_code() {
		return proto_code;
	}

	public void setProto_code(Integer proto_code) {
		this.proto_code = proto_code;
	}

	public Integer getDistrict() {
		return district;
	}

	public void setDistrict(Integer district) {
		this.district = district;
	}


	public Integer getNode_code() {
		return node_code;
	}

	public void setNode_code(Integer node_code) {
		this.node_code = node_code;
	}

	public Integer getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}

	public Integer getData_status() {
		return data_status;
	}

	public void setData_status(Integer data_status) {
		this.data_status = data_status;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public Integer getRegular_in_terminal() {
		return regular_in_terminal;
	}

	public void setRegular_in_terminal(Integer regular_in_terminal) {
		this.regular_in_terminal = regular_in_terminal;
	}


}