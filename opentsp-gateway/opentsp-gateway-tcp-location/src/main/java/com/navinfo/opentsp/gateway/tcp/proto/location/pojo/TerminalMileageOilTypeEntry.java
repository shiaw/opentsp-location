package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import java.io.Serializable;

/**
 * Created by wanliang on 2017/6/16.
 * 终端里程油耗类型表
 */
public class TerminalMileageOilTypeEntry implements Serializable{

    private long terminalId;// '终端唯一标识',
    private int  mileageType;//'里程类型 1 仪表里程；2 ECU里程；3 GPS里程',
    private int oilType;   //油耗类型 1 燃油总消耗；2 积分总油耗'
    public TerminalMileageOilTypeEntry(){}
    public TerminalMileageOilTypeEntry(long terminalId, int mileageType, int oilType) {
        this.terminalId = terminalId;
        this.mileageType = mileageType;
        this.oilType = oilType;
    }

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public int getMileageType() {
        return mileageType;
    }

    public void setMileageType(int mileageType) {
        this.mileageType = mileageType;
    }

    public int getOilType() {
        return oilType;
    }

    public void setOilType(int oilType) {
        this.oilType = oilType;
    }
}
