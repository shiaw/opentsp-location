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
@Table(name = "lc_terminal_info")
public class TerminalEntry {
    @Id
    @GeneratedValue
    private long tiId;

    @Column(nullable = false, unique = true, name = "terminal_id")
    private String terminalId;
    @Column
    private int protoCode;

    @Column(nullable = true)
    private Integer nodeCode;
    @Column
    private String district;
    @Column
    private long createTime;
    @Column
    private int dataStatus;
    @Column(name = "regular_in_terminal")
    private int regularInTerminal;
    @Column
    private String deviceId;

    @Column(name = "change_tid")
    private String changeTid;
    @Column
    private int businessType;


    private String authCode;

    public TerminalEntry() {
    }

    public TerminalEntry(String terminalId, int protoCode, Integer nodeCode, String district, long createTime, int dataStatus, int regularInTerminal, String deviceId, String changeTid, int businessType, String authCode) {
        this.terminalId = terminalId;
        this.protoCode = protoCode;
        this.nodeCode = nodeCode;
        this.district = district;
        this.createTime = createTime;
        this.dataStatus = dataStatus;
        this.regularInTerminal = regularInTerminal;
        this.deviceId = deviceId;
        this.changeTid = changeTid;
        this.businessType = businessType;
        this.authCode = authCode;
    }

    public long getTiId() {
        return tiId;
    }

    public void setTiId(long tiId) {
        this.tiId = tiId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public int getProtoCode() {
        return protoCode;
    }

    public void setProtoCode(int protoCode) {
        this.protoCode = protoCode;
    }

    public Integer getNodeCode() {
        return nodeCode;
    }

    public void setNodeCode(Integer nodeCode) {
        this.nodeCode = nodeCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(int dataStatus) {
        this.dataStatus = dataStatus;
    }

    public int getRegularInTerminal() {
        return regularInTerminal;
    }

    public void setRegularInTerminal(int regularInTerminal) {
        this.regularInTerminal = regularInTerminal;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getChangeTid() {
        return changeTid;
    }

    public void setChangeTid(String changeTid) {
        this.changeTid = changeTid;
    }

    public int getBusinessType() {
        return businessType;
    }

    public void setBusinessType(int businessType) {
        this.businessType = businessType;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
