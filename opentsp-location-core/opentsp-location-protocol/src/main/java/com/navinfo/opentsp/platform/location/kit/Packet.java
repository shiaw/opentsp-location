package com.navinfo.opentsp.platform.location.kit;

import com.navinfo.opentsp.platform.location.kit.id.IDFactory;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用数据传输对象
 *
 * @author lgw
 */
public class Packet implements Serializable {
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
    private Map<String, Object> pushArguments;
    private int resultCode = 0;//用于异步返回状态码

    private boolean dpProcess = true;//判断dp组播时候,是否要执行
    private String queueName;//队列名称
    private String upperUniqueMark;//链路唯一标识

    public Packet() {
        super();
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public boolean isDpProcess() {
        return dpProcess;
    }

    public void setDpProcess(boolean dpProcess) {
        this.dpProcess = dpProcess;
    }

    /**
     * 指定容量
     *
     * @param capacity {@link Integer}
     */
    public Packet(int capacity) {
        this(false, capacity);
    }

    /**
     * 自动生成流水号
     *
     * @param isGenerateId {@link Boolean}
     */
    public Packet(boolean isGenerateId) {
        this(isGenerateId, 0);
    }

    /**
     * 指定容量以及自动生成流水号
     *
     * @param isGenerateId {@link Boolean}
     * @param capacity     {@link Integer}
     */
    public Packet(boolean isGenerateId, int capacity) {
        if (isGenerateId) {
            if (capacity > 0) {
                this.content = new byte[capacity];
                this._content_capacity = capacity;
            }
            this.serialNumber = (int) IDFactory.builderID(IDFactory.IDType.SerialNumber);
        } else {
            if (capacity > 0) {
                this.content = new byte[capacity];
                this._content_capacity = capacity;
            }
        }
    }

    /**
     * 添加一个自定义参数
     *
     * @param pKey   {@link String} Key
     * @param pValue {@link Object} Value
     * @return
     */
    public Packet addParameter(Object pKey, Object pValue) {
        if (this.parameters == null)
            this.parameters = new ConcurrentHashMap<Object, Object>();
        this.parameters.put(pKey, pValue);
        return this;
    }

    public Object getParamter(Object pKey) {
        if (this.parameters != null)
            return this.parameters.get(pKey);
        return null;
    }

    public String getParamterForString(Object pKey) {
        if (this.parameters != null) {
            Object value = this.parameters.get(pKey);
            return String.valueOf(value);
        }
        return null;
    }

    public Packet appendContent(byte[] bytes) {
        if (bytes.length + this._content_position > this._content_capacity) {
            throw new RuntimeException(
                    "Packet Content Capacity is not enough .");
        }
        ArraysUtils.arrayappend(this.content, this._content_position, bytes);
        this._content_position += bytes.length;
        return this;
    }

    public Packet appendContent(byte bt) {
        return this.appendContent(new byte[]{bt});
    }

    /**
     * 获取指令号的16进制形式
     */
    public String getCommandForHex() {
        return Convert.decimalToHexadecimal(this.command, 4);
    }

    /**
     * 获取数据内容体的16进制形式
     */
    public String getContentForHex() {
        return Convert.bytesToHexString(this.content);
    }

    /**
     * 获取原始数据包的16进制形式
     */
    public String getOriginalPacketForHex() {
        if (this.originalPacket != null && this.originalPacket.length > 0) {
            Convert.bytesToHexString(this.originalPacket);
        }
        return null;
    }

    public byte[] getContent() {
        return content;
    }

    public byte[] getOriginalPacket() {
        return originalPacket;
    }

    public void setOriginalPacket(byte[] originalPacket) {
        this.originalPacket = originalPacket;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getBlockId() {
        return blockId;
    }

    public void setBlockId(int blockId) {
        this.blockId = blockId;
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

    public Map<String, Object> getPushArguments() {
        return pushArguments;
    }

    public void setPushArguments(Map<String, Object> pushArguments) {
        this.pushArguments = pushArguments;
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String toString() {
        return "Packet{" +
                "command=" + command +
                ", commandForHex=" + getCommandForHex() +
                ", uniqueMark='" + uniqueMark + '\'' +
                ", serialNumber=" + serialNumber +
                ", createTime=" + createTime +
                '}';
    }

    /**
     * 跟key获取对应的值
     *
     * @param key
     * @return
     */
    public Object getPushArgumentByKey(String key) {
        if (pushArguments != null) {
            return pushArguments.get(key);
        }
        return null;
    }

    public String getUpperUniqueMark() {
        return upperUniqueMark;
    }

    public void setUpperUniqueMark(String upperUniqueMark) {
        this.upperUniqueMark = upperUniqueMark;
    }
}
