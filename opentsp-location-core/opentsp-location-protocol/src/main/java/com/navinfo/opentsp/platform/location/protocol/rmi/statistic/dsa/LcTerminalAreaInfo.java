package com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa;

import java.util.ArrayList;
import java.util.List;

/**
 * LcTerminalAreaData entity. @author MyEclipse Persistence Tools
 */

public class LcTerminalAreaInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private Integer ta_id;
	private Integer original_area_id;
	private Integer area_type;
	private Integer create_time;
	private Long terminal_id;
	private List<LcTerminalAreaInfoData> areaData = new ArrayList<LcTerminalAreaInfoData>();

	public void addreaData(LcTerminalAreaInfoData data) {
		areaData.add(data);
	}

	public Integer getTa_id() {
		return ta_id;
	}

	public void setTa_id(Integer ta_id) {
		this.ta_id = ta_id;
	}

	public Integer getOriginal_area_id() {
		return original_area_id;
	}

	public void setOriginal_area_id(Integer original_area_id) {
		this.original_area_id = original_area_id;
	}

	public Integer getArea_type() {
		return area_type;
	}

	public void setArea_type(Integer area_type) {
		this.area_type = area_type;
	}

	public Integer getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}

	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public List<LcTerminalAreaInfoData> getAreaData() {
		return areaData;
	}

	public void setAreaData(List<LcTerminalAreaInfoData> areaData) {
		this.areaData = areaData;
	}
}
