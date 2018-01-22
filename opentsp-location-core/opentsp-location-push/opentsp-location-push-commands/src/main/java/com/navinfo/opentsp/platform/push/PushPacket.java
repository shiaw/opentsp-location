package com.navinfo.opentsp.platform.push;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用数据传输对象
 *
 * @author lgw
 */
public class PushPacket implements Serializable {
    private static final long serialVersionUID = 1L;
    private int command;// 消息ID-
    private long from;// 来个哪个唯一标识
    private long to;// 发向哪个唯一标识
    private int protocol;// 协议类型-
    private long channels;// 通道ID
    private String uniqueMark = "";// 唯一标识-
    private int serialNumber = 0;// 流水与-
    private long createTime = System.currentTimeMillis() / 1000;
    private int packetTotal = 0;// 包序号
    private int packetSerial = 0;// 总包数
    private int blockId = 0;//分包块ID
    private byte[] content = new byte[0];// 消息体-
    private byte[] originalPacket;// 原始包数据
    private int _content_position;
    private int _content_capacity;
    private Map<Object, Object> parameters;
    private int resultCode = 0;//用于异步返回状态码

    private boolean dpProcess = true;//判断dp组播时候,是否要执行
    private String queueName;//队列名称
    private String upperUniqueMark;//链路唯一标识


    @Override
    public String toString() {
        return "Packet{" +
                "command=" + command +
                ", uniqueMark='" + uniqueMark + '\'' +
                ", serialNumber=" + serialNumber +
                ", createTime=" + createTime +
                ", packetTotal=" + packetTotal +
                ", packetSerial=" + packetSerial +
                '}';
    }

    public int getCommand() {
        return command;
    }

    public void setCommand(int command) {
        this.command = command;
    }

    public long getFrom() {
        return from;
    }

    public void setFrom(long from) {
        this.from = from;
    }

    public long getTo() {
        return to;
    }

    public void setTo(long to) {
        this.to = to;
    }

    public int getProtocol() {
        return protocol;
    }

    public void setProtocol(int protocol) {
        this.protocol = protocol;
    }

    public long getChannels() {
        return channels;
    }

    public void setChannels(long channels) {
        this.channels = channels;
    }

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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public int getPacketTotal() {
        return packetTotal;
    }

    public void setPacketTotal(int packetTotal) {
        this.packetTotal = packetTotal;
    }

    public int getPacketSerial() {
        return packetSerial;
    }

    public void setPacketSerial(int packetSerial) {
        this.packetSerial = packetSerial;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public byte[] getOriginalPacket() {
        return originalPacket;
    }

    public void setOriginalPacket(byte[] originalPacket) {
        this.originalPacket = originalPacket;
    }

    public int get_content_position() {
        return _content_position;
    }

    public void set_content_position(int _content_position) {
        this._content_position = _content_position;
    }

    public int get_content_capacity() {
        return _content_capacity;
    }

    public void set_content_capacity(int _content_capacity) {
        this._content_capacity = _content_capacity;
    }

    public Map<Object, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<Object, Object> parameters) {
        this.parameters = parameters;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public boolean isDpProcess() {
        return dpProcess;
    }

    public void setDpProcess(boolean dpProcess) {
        this.dpProcess = dpProcess;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getUpperUniqueMark() {
        return upperUniqueMark;
    }

    public void setUpperUniqueMark(String upperUniqueMark) {
        this.upperUniqueMark = upperUniqueMark;
    }
}
