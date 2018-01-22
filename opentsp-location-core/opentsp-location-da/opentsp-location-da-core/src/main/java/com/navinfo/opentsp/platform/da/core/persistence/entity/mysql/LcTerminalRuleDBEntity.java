package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

import java.util.Arrays;


/**
 * LcTerminalRule entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_RULE")
public class LcTerminalRuleDBEntity implements java.io.Serializable {

	@LCTransient
	@LCPrimaryKey
	private Integer tr_id;

	private Long terminal_id;
	/**
	 * 标识（区域/路线、禁驾，由上级业务系统提供）
	 */
	private Long original_area_id;
	/**
	 * 路段标识
	 */
	private Long original_line_id;
	/**
	 * 内部编码（规则编码）
	 */
	private Integer business_type;
	/**
	 * 规则内容（protobuf）
	 */
	private byte[] rule_content;
	/**
	 * 最后修改时间
	 */
	private Integer last_update_time;
	/**
	 * 规则类型0：通用规则；1个性规则
	 */
	private Integer rule_type;
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
	public Long getOriginal_area_id() {
		return original_area_id;
	}
	public void setOriginal_area_id(Long original_area_id) {
		this.original_area_id = original_area_id;
	}
	public Long getOriginal_line_id() {
		return original_line_id;
	}
	public void setOriginal_line_id(Long original_line_id) {
		this.original_line_id = original_line_id;
	}
	public Integer getBusiness_type() {
		return business_type;
	}
	public void setBusiness_type(Integer business_type) {
		this.business_type = business_type;
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
	public Integer getRule_type() {
		return rule_type;
	}
	public void setRule_type(Integer rule_type) {
		this.rule_type = rule_type;
	}
	@Override
	public String toString() {
		return "LcTerminalRuleDBEntity [tr_id=" + tr_id + ", terminal_id="
				+ terminal_id + ", original_area_id=" + original_area_id
				+ ", original_line_id=" + original_line_id + ", business_type="
				+ business_type + ", rule_content="
				+ Arrays.toString(rule_content) + ", last_update_time="
				+ last_update_time + ", rule_type=" + rule_type + "]";
	}


}