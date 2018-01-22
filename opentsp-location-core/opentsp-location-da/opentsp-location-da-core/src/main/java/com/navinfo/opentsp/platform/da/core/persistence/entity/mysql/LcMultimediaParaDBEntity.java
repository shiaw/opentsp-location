package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

@LCTable(name="LC_MULTIMEDIA_PARA")
public class LcMultimediaParaDBEntity implements java.io.Serializable{

	/**
	 * 
	 */

	@LCTransient
	@LCPrimaryKey
	private Integer lmp_id;
	private String file_id;
	private Long terminal_id;
	private Integer media_id;
	private Integer media_date;
	private Integer type;
	private Integer encode;
	private Integer events;
	private Integer channels;
	
	
	public Integer getLmp_id() {
		return lmp_id;
	}
	public void setLmp_id(Integer lmp_id) {
		this.lmp_id = lmp_id;
	}
	public Long getTerminal_id() {
		return terminal_id;
	}
	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}
	public Integer getMedia_id() {
		return media_id;
	}
	public void setMedia_id(Integer media_id) {
		this.media_id = media_id;
	}
	public Integer getMedia_date() {
		return media_date;
	}
	public void setMedia_date(Integer media_date) {
		this.media_date = media_date;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getEncode() {
		return encode;
	}
	public void setEncode(Integer encode) {
		this.encode = encode;
	}
	public Integer getEvents() {
		return events;
	}
	public void setEvents(Integer events) {
		this.events = events;
	}
	public Integer getChannels() {
		return channels;
	}
	public void setChannels(Integer channels) {
		this.channels = channels;
	}
	public String getFile_id() {
		return file_id;
	}
	public void setFile_id(String file_id) {
		this.file_id = file_id;
	}
	
	


}
