package com.navinfo.opentsp.platform.da.core.persistence.entity.extend;

import java.io.Serializable;

public class TerminalInfoModel implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4172071689447108147L;
	//终端标识
	private Long terminal_id;
	//服务区域
	private Integer district;
	//节点服务IP
	private String local_ip_address;
	//状态
	private Integer switch_type;
	//终端协议编码
	private Integer proto_code;
	//终端编号
	private Integer terminal_sn;

	public Integer getTerminal_sn() {
		return terminal_sn;
	}

	public void setTerminal_sn(Integer terminal_sn) {
		this.terminal_sn = terminal_sn;
	}

	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public Integer getDistrict() {
		return district;
	}
	public void setDistrict(Integer district) {
		this.district = district;
	}

	public String getLocal_ip_address() {
		return local_ip_address;
	}
	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}
	public Integer getSwitch_type() {
		return switch_type;
	}
	public void setSwitch_type(Integer switch_type) {
		this.switch_type = switch_type;
	}
	public Integer getProto_code() {
		return proto_code;
	}
	public void setProto_code(Integer proto_code) {
		this.proto_code = proto_code;
	}


}
