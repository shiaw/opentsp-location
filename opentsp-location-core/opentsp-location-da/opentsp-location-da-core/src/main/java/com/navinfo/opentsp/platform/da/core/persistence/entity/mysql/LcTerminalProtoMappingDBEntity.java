package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcTerminalProtoLogic entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_PROTO_MAPPING")
public class LcTerminalProtoMappingDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer tpl_d;
	private Integer logic_code;
	private Integer dict_code;
	private Integer message_code;

	// Constructors

	/** default constructor */
	public LcTerminalProtoMappingDBEntity() {
	}

	public Integer getTpl_d() {
		return tpl_d;
	}

	public void setTpl_d(Integer tpl_d) {
		this.tpl_d = tpl_d;
	}

	public Integer getLogic_code() {
		return logic_code;
	}

	public void setLogic_code(Integer logic_code) {
		this.logic_code = logic_code;
	}

	public Integer getDict_code() {
		return dict_code;
	}

	public void setDict_code(Integer dict_code) {
		this.dict_code = dict_code;
	}

	public Integer getMessage_code() {
		return message_code;
	}

	public void setMessage_code(Integer message_code) {
		this.message_code = message_code;
	}
	
}