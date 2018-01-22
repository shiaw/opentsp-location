package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * 信息播报日志实体
 */
@LCTable(name="LC_MESSAGE_BROADCAST_LOG")
public class LcMessageBroadcastLogDBEntity implements java.io.Serializable {

	@LCTransient
	@LCPrimaryKey
	private int id;
	/**
	 * 终端标识
	 */
	private long terminal_id;
	/**
	 * 区域标识（上层业务系统提供唯一标识）
	 */
	private long original_area_id;
	/**
	 * 播报时间
	 */
	private long broadcast_time;
	/**
	 * 播报内容
	 */
	private String content;
	/**
	 * 播报方式 1.TTS 2.终端显示器显示 3.广告屏显示
	 */
	private int broadcast_mode;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(long terminal_id) {
		this.terminal_id = terminal_id;
	}
	public long getOriginal_area_id() {
		return original_area_id;
	}
	public void setOriginal_area_id(long original_area_id) {
		this.original_area_id = original_area_id;
	}
	public long getBroadcast_time() {
		return broadcast_time;
	}
	public void setBroadcast_time(long broadcast_time) {
		this.broadcast_time = broadcast_time;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getBroadcast_mode() {
		return broadcast_mode;
	}
	public void setBroadcast_mode(int broadcast_mode) {
		this.broadcast_mode = broadcast_mode;
	}
	@Override
	public String toString() {
		return "LcMessageBroadcastLogDBEntity [id=" + id + ", terminal_id="
				+ terminal_id + ", original_area_id=" + original_area_id
				+ ", broadcast_time=" + broadcast_time + ", content=" + content
				+ ", broadcast_mode=" + broadcast_mode + "]";
	}


}