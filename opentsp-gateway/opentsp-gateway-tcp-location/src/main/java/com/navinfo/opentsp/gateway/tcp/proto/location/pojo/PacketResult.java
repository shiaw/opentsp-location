package com.navinfo.opentsp.gateway.tcp.proto.location.pojo;

import com.navinfo.opentsp.common.messaging.AbstractCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * Created by wanliang on 2016/12/8.
 */
public class PacketResult {

    private Packet terminalPacket;
    private Packet kafkaPacket;
    private AbstractCommand mqCommand;
    private Packet answerPacket;

    public Packet getTerminalPacket() {
        return terminalPacket;
    }

    public void setTerminalPacket(Packet terminalPacket) {
        this.terminalPacket = terminalPacket;
    }

    public Packet getKafkaPacket() {
        return kafkaPacket;
    }

    public void setKafkaPacket(Packet kafkaPacket) {
        this.kafkaPacket = kafkaPacket;
    }

    public AbstractCommand getMqCommand() {
        return mqCommand;
    }

    public void setMqCommand(AbstractCommand mqCommand) {
        this.mqCommand = mqCommand;
    }

    public Packet getAnswerPacket() {
        return answerPacket;
    }

    public void setAnswerPacket(Packet answerPacket) {
        this.answerPacket = answerPacket;
    }
}
