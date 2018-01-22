package com.navinfo.opentsp.platform.rprest.entity;

import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatisticData;

import java.util.List;

/**
 * Created by 修伟 on 2017/11/9 0009.
 */
public class TripStatisticData {
    private long terminalId;
    //private List<LCTerminalStatisticData.StatisticData> list;
    private List<String> list;

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    /*public List<LCTerminalStatisticData.StatisticData> getList() {
        return list;
    }

    public void setList(List<LCTerminalStatisticData.StatisticData> list) {
        this.list = list;
    }*/

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
