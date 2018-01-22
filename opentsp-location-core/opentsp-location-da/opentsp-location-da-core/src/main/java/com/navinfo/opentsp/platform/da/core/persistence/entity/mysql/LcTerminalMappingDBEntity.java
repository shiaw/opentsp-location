package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

@LCTable(name="LC_TERMINAL_MAPPING")
public class LcTerminalMappingDBEntity implements java.io.Serializable {

	@LCTransient
	@LCPrimaryKey
	private Integer tm_id;
	private long main_tid;
	private long secondary_tid;
	public Integer getTm_id() {
		return tm_id;
	}
	public void setTm_id(Integer tm_id) {
		this.tm_id = tm_id;
	}
	public long getMain_tid() {
		return main_tid;
	}
	public void setMain_tid(long main_tid) {
		this.main_tid = main_tid;
	}
	public long getSecondary_tid() {
		return secondary_tid;
	}
	public void setSecondary_tid(long secondary_tid) {
		this.secondary_tid = secondary_tid;
	}
	@Override
	public String toString() {
		return "LcTerminalMappingDBEntity [tm_id=" + tm_id + ", main_tid="
				+ main_tid + ", secondary_tid=" + secondary_tid + "]";
	}
	
	
}