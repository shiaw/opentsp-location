package com.navinfo.opentsp.platform.rprest.entity;

import com.navinfo.opentsp.platform.location.protocol.common.LCFaultInfo;

import java.util.List;

/**
 * Created by 修伟 on 2017/11/10 0010.
 */
public class FaultData {
    private long terminalId;
    private List<String> list;

    public long getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(long terminalId) {
        this.terminalId = terminalId;
    }

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }
}
