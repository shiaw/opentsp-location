package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;

/**
 * LcServiceConfig entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_SERVICE_CONFIG")
public class LcServiceConfigDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer sc_id;
	private String auth_name;
	private String password;
	private Integer stats;
	private Integer create_time;
	private String auth_key;
	private String ip_address;
	private Integer up_flow_limit;
	private Integer down_flow_limit;
	private Integer access_terminal_num;
	private Integer last_update_time;
	private String username;
	private Integer data_status;

	// Constructors

	/** default constructor */
	public LcServiceConfigDBEntity() {
	}

	public Integer getSc_id() {
		return sc_id;
	}

	public void setSc_id(Integer sc_id) {
		this.sc_id = sc_id;
	}

	public String getAuth_name() {
		return auth_name;
	}

	public void setAuth_name(String auth_name) {
		this.auth_name = auth_name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getStats() {
		return stats;
	}

	public void setStats(Integer stats) {
		this.stats = stats;
	}

	public Integer getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}

	public String getAuth_key() {
		return auth_key;
	}

	public void setAuth_key(String auth_key) {
		this.auth_key = auth_key;
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		this.ip_address = ip_address;
	}


	public Integer getUp_flow_limit() {
		return up_flow_limit;
	}

	public void setUp_flow_limit(Integer up_flow_limit) {
		this.up_flow_limit = up_flow_limit;
	}

	public Integer getDown_flow_limit() {
		return down_flow_limit;
	}

	public void setDown_flow_limit(Integer down_flow_limit) {
		this.down_flow_limit = down_flow_limit;
	}

	public Integer getAccess_terminal_num() {
		return access_terminal_num;
	}

	public void setAccess_terminal_num(Integer access_terminal_num) {
		this.access_terminal_num = access_terminal_num;
	}

	public Integer getLast_update_time() {
		return last_update_time;
	}

	public void setLast_update_time(Integer last_update_time) {
		this.last_update_time = last_update_time;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getData_status() {
		return data_status;
	}

	public void setData_status(Integer data_status) {
		this.data_status = data_status;
	}

	
}