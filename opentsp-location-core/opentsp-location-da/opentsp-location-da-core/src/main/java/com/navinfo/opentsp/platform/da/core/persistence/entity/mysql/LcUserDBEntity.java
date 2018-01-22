package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;

import  com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import  com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;


/**
 * LcUser entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_USER")
public class LcUserDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer user_id;
	private Integer role_id;
	private String username;
	private String password;
	private Integer create_time;
	private int data_status;

	// Constructors

	/** default constructor */
	public LcUserDBEntity() {
	}

	/** minimal constructor */
	public LcUserDBEntity(String username, String password) {
		this.username = username;
		this.password = password;
	}

	/** full constructor */
	public LcUserDBEntity(Integer role_id, String username, String password,
			Integer createTime,short data_status) {
		this.role_id = role_id;
		this.username = username;
		this.password = password;
		this.create_time = createTime;
		this.data_status=data_status;
	}

	// Property accessors

	
	public String getUsername() {
		return this.username;
	}

	public int getData_status() {
		return data_status;
	}

	public void setData_status(int data_status) {
		this.data_status = data_status;
	}

	public Integer getUser_id() {
		return user_id;
	}

	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}

	public Integer getRole_id() {
		return role_id;
	}

	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Integer create_time) {
		this.create_time = create_time;
	}

}