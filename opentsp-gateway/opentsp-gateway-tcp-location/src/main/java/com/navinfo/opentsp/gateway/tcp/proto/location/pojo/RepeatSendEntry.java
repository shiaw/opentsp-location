package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * Packet重发实体
 * User: zhanhk
 * Date: 16/6/22
 * Time: 上午10:07
 */
public class RepeatSendEntry extends AnswerEntry{

    private Packet packet;

    private boolean sendFlag = false;

    public Packet getPacket() {
        return packet;
    }

    public void setPacket(Packet packet) {
        this.packet = packet;
    }

    public boolean getSendFlag() {
        return sendFlag;
    }

    public void setSendFlag(boolean sendFlag) {
        this.sendFlag = sendFlag;
    }

    @Override
    public String toString() {
        return "RepeatSendEntry{" +
                "packet=" + packet +
                ", sendFlag=" + sendFlag +
                '}';
    }
}
