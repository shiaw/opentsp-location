package com.navinfo.opentsp.platform.rprest.entity;

import java.io.Serializable;

/**
 * Created by zhangyue on 2017/6/20.
 */
public class TerminalMileageOilEntity implements Serializable {
    private static final long serialVersionUID = 7158379400006513888L;
    private long terminalId;
    private int mileageType;
    private int oilType;

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
