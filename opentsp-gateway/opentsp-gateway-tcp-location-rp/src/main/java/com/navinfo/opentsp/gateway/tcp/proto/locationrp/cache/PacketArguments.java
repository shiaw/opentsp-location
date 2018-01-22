package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/2
 * @modify
 * @copyright opentsp
 */
public class PacketArguments {
    private String uniqueMark = "";// 唯一标识-
    private int serialNumber = 0;// 流水与-


    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }
}
