package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry;

import java.io.Serializable;

/**
 * 默认超时时间：30秒
 *
 * @author lgw
 */
public class AnswerEntry implements Serializable {
    private static final long serialVersionUID = 1L;
    private String uniqueMark;
    private int internalCommand;
    private int serialNumber;


    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public int getInternalCommand() {
        return internalCommand;
    }

    public void setInternalCommand(int internalCommand) {
        this.internalCommand = internalCommand;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

}
