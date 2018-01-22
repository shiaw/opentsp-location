package com.navinfo.opentsp.platform.push.persisted;

import javax.persistence.*;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/10
 * @modify
 * @copyright opentsp
 */
@Entity
@Table(name = "lc_terminal_register")
public class TerminalRegisterEntry {
    @Id
    @GeneratedValue
    private long registerId;

    @Column
    private String terminalId;
    @Column
    private String authCode;
    @Column
    private int province;
    @Column
    private int city;
    @Column
    private String product;
    @Column
    private String terminalType;
    @Column
    private String terminalSn;
    @Column
    private int licenseColor;
    @Column
    private String license;

    public long getRegisterId() {
        return registerId;
    }

    public void setRegisterId(long registerId) {
        this.registerId = registerId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public int getProvince() {
        return province;
    }

    public void setProvince(int province) {
        this.province = province;
    }

    public int getCity() {
        return city;
    }

    public void setCity(int city) {
        this.city = city;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getTerminalType() {
        return terminalType;
    }

    public void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }

    public String getTerminalSn() {
        return terminalSn;
    }

    public void setTerminalSn(String terminalSn) {
        this.terminalSn = terminalSn;
    }

    public int getLicenseColor() {
        return licenseColor;
    }

    public void setLicenseColor(int licenseColor) {
        this.licenseColor = licenseColor;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }
}
