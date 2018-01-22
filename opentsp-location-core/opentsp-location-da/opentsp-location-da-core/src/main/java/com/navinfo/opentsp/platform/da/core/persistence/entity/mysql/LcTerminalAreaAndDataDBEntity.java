package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


/**
 * LcTerminalAreaData entity. @author MyEclipse Persistence Tools
 */

public class LcTerminalAreaAndDataDBEntity {


		
		private Integer ta_id;
		private Integer original_area_id;
		private Integer area_type;
		private Integer create_time;
		private Long terminal_id;
		private Integer data_sn;
		private Integer data_status;
		private Integer radius_len;
		private Integer latitude;
		private Integer longitude;
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
		public Integer getData_sn() {
			return data_sn;
		}
		public void setData_sn(Integer data_sn) {
			this.data_sn = data_sn;
		}
		public Integer getData_status() {
			return data_status;
		}
		public void setData_status(Integer data_status) {
			this.data_status = data_status;
		}
		public Integer getRadius_len() {
			return radius_len;
		}
		public void setRadius_len(Integer radius_len) {
			this.radius_len = radius_len;
		}
		public Integer getLatitude() {
			return latitude;
		}
		public void setLatitude(Integer latitude) {
			this.latitude = latitude;
		}
		public Integer getLongitude() {
			return longitude;
		}
		public void setLongitude(Integer longitude) {
			this.longitude = longitude;
		}

}
