package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcTerminalInfo entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_INFO")
public class LcTerminalInfoDBEntity implements java.io.Serializable {

	
	@LCTransient
	@LCPrimaryKey
	private Integer ti_id;
	private Long terminal_id;
	private Integer proto_code;
	private Integer district;
	private Integer node_code;
	private Integer regular_in_terminal;
	private String device_id;
	private Long change_tid;
	private Integer business_type;


	private Long create_time;
	private Integer data_status;

	
	public Long getChange_tid() {
		return change_tid;
	}

	public void setChange_tid(Long change_tid) {
		this.change_tid = change_tid;
	}

	public String getDevice_id() {
		return device_id;
	}

	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}


	public Integer getBusiness_type() {
		return business_type;
	}

	public void setBusiness_type(Integer business_type) {
		this.business_type = business_type;
	}


	// Constructors

	/** default constructor */
	public LcTerminalInfoDBEntity() {
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

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public Integer getData_status() {
		return data_status;
	}

	public void setData_status(Integer data_status) {
		this.data_status = data_status;
	}

	public Integer getRegular_in_terminal() {
		return regular_in_terminal;
	}

	public void setRegular_in_terminal(Integer regular_in_terminal) {
		this.regular_in_terminal = regular_in_terminal;
	}

	@Override
	public String toString() {
		return "LcTerminalInfoDBEntity [ti_id=" + ti_id + ", terminal_id="
				+ terminal_id + ", proto_code=" + proto_code + ", district="
				+ district + ", node_code=" + node_code
				+ ", regular_in_terminal=" + regular_in_terminal
				+ ", device_id=" + device_id + ", business_type="
				+ business_type + ", create_time=" + create_time
				+ ", data_status=" + data_status + "]";
	}
	
}