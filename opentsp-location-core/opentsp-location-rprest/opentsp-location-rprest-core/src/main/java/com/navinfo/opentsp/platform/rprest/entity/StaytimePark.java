package com.navinfo.opentsp.platform.rprest.entity;

public class StaytimePark
{
    private long terminalId;
    private long areaId;
    private long beginDate;
    private long endDate;
    private int continuousTime;
    private int beginLat;
    private int beginLng;
    private int endLat;
    private int endLng;

    public long getTerminalId()
    {
        return this.terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public long getAreaId() {
        return this.areaId;
    }

    public void setAreaId(long areaId) {
        this.areaId = areaId;
    }

    public long getBeginDate() {
        return this.beginDate;
    }

    public void setBeginDate(long beginDate) {
        this.beginDate = beginDate;
    }

    public long getEndDate() {
        return this.endDate;
    }

    public void setEndDate(long endDate) {
        this.endDate = endDate;
    }

    public int getContinuousTime() {
        return this.continuousTime;
    }

    public void setContinuousTime(int continuousTime) {
        this.continuousTime = continuousTime;
    }

    public int getBeginLat() {
        return this.beginLat;
    }

    public void setBeginLat(int beginLat) {
        this.beginLat = beginLat;
    }

    public int getBeginLng() {
        return this.beginLng;
    }

    public void setBeginLng(int beginLng) {
        this.beginLng = beginLng;
    }

    public int getEndLat() {
        return this.endLat;
    }

    public void setEndLat(int endLat) {
        this.endLat = endLat;
    }

    public int getEndLng() {
        return this.endLng;
    }

    public void setEndLng(int endLng) {
        this.endLng = endLng;
    }
}