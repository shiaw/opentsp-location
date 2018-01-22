package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

public class TerminalSnInfo {
    private Integer id;

    private Long terminalId;

    private String vin;

    private String ecuid;

    private String van;

    private String sn;

    private String vehicle;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(Long terminalId) {
        this.terminalId = terminalId;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    public String getEcuid() {
        return ecuid;
    }

    public void setEcuid(String ecuid) {
        this.ecuid = ecuid == null ? null : ecuid.trim();
    }

    public String getVan() {
        return van;
    }

    public void setVan(String van) {
        this.van = van == null ? null : van.trim();
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn == null ? null : sn.trim();
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle == null ? null : vehicle.trim();
    }
}