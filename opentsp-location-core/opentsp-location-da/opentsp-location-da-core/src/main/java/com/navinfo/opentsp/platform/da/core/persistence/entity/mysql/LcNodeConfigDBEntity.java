package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;

/**
 * LcNodeConfig entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_NODE_CONFIG")
public class LcNodeConfigDBEntity  implements java.io.Serializable {


    // Fields    
	@LCTransient
	@LCPrimaryKey
	private Integer nc_id;
     private Integer node_code;
     private Integer max_limit;
     private Integer min_limit;
     private String local_ip_address;
     private String internet_ip_address;
     private Integer node_type;
     private Integer district;
     private byte[] ext_content;
     private Integer last_update_time;
     private String username;
     private Integer data_status;
     private Integer is_redundance;


    // Constructors

    /** default constructor */
    public LcNodeConfigDBEntity() {
    }


    // Property accessors
    
	public Integer getNc_id() {
		return nc_id;
	}


	public Integer getIs_redundance() {
		return is_redundance;
	}


	public void setIs_redundance(Integer is_redundance) {
		this.is_redundance = is_redundance;
	}


	public void setNc_id(Integer nc_id) {
		this.nc_id = nc_id;
	}


	public Integer getNode_code() {
		return node_code;
	}


	public void setNode_code(Integer node_code) {
		this.node_code = node_code;
	}


	public Integer getMax_limit() {
		return max_limit;
	}


	public void setMax_limit(Integer max_limit) {
		this.max_limit = max_limit;
	}


	public Integer getMin_limit() {
		return min_limit;
	}


	public void setMin_limit(Integer min_limit) {
		this.min_limit = min_limit;
	}


	public String getLocal_ip_address() {
		return local_ip_address;
	}


	public void setLocal_ip_address(String local_ip_address) {
		this.local_ip_address = local_ip_address;
	}


	public String getInternet_ip_address() {
		return internet_ip_address;
	}


	public void setInternet_ip_address(String internet_ip_address) {
		this.internet_ip_address = internet_ip_address;
	}


	public Integer getNode_type() {
		return node_type;
	}


	public void setNode_type(Integer node_type) {
		this.node_type = node_type;
	}


	public Integer getDistrict() {
		return district;
	}


	public void setDistrict(Integer district) {
		this.district = district;
	}


	public Integer getData_status() {
		return data_status;
	}


	public void setData_status(Integer data_status) {
		this.data_status = data_status;
	}


	public byte[] getExt_content() {
		return ext_content;
	}


	public void setExt_content(byte[] ext_content) {
		this.ext_content = ext_content;
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
	

}