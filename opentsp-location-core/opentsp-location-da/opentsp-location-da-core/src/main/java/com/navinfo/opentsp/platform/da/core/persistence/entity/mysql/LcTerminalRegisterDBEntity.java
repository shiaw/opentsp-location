package com.navinfo.opentsp.platform.da.core.persistence.entity.mysql;


import com.navinfo.opentsp.platform.da.core.persistence.common.LCPrimaryKey;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTransient;

/**
 * LcTerminalRegister entity. @author MyEclipse Persistence Tools
 */
@LCTable(name="LC_TERMINAL_REGISTER")
public class LcTerminalRegisterDBEntity implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	@LCTransient
	@LCPrimaryKey
	private Integer register_id;
	private Long terminal_id;
	private String auth_code;
	private Integer province;
	private Integer city;
	private String product;
	private String terminal_type;
	private String terminal_sn;
	private Integer license_color;
	private String license;

	// Constructors

	/** default constructor */
	public LcTerminalRegisterDBEntity() {
	}

	// Property accessors
	public Integer getRegister_id() {
		return register_id;
	}

	public void setRegister_id(Integer register_id) {
		this.register_id = register_id;
	}


	public Long getTerminal_id() {
		return terminal_id;
	}

	public void setTerminal_id(Long terminal_id) {
		this.terminal_id = terminal_id;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public Integer getProvince() {
		return province;
	}

	public void setProvince(Integer province) {
		this.province = province;
	}

	public Integer getCity() {
		return city;
	}

	public void setCity(Integer city) {
		this.city = city;
	}

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getTerminal_type() {
		return terminal_type;
	}

	public void setTerminal_type(String terminal_type) {
		this.terminal_type = terminal_type;
	}

	public String getTerminal_sn() {
		return terminal_sn;
	}

	public void setTerminal_sn(String terminal_sn) {
		this.terminal_sn = terminal_sn;
	}

	public Integer getLicense_color() {
		return license_color;
	}

	public void setLicense_color(Integer license_color) {
		this.license_color = license_color;
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license;
	}

	@Override
	public String toString() {
		return "LcTerminalRegisterDBEntity [register_id=" + register_id
				+ ", terminal_id=" + terminal_id + ", auth_code=" + auth_code
				+ ", province=" + province + ", city=" + city + ", product="
				+ product + ", terminal_type=" + terminal_type
				+ ", terminal_sn=" + terminal_sn + ", license_color="
				+ license_color + ", license=" + license + "]";
	}

}