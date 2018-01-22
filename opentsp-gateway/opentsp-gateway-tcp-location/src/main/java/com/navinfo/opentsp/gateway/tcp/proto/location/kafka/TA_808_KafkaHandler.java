package com.navinfo.opentsp.gateway.tcp.proto.location.kafka;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TAMonitorCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalProtoVersionCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.netty.DispatchTcpHandler;
import com.navinfo.opentsp.gateway.tcp.proto.location.netty.PacketProcessing;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.Command;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.ProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.MergePacketProcess;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.auth.TerminalProtoVersionCommand;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Created by wanliang on 2016/11/16.
 */
@KafkaConsumerHandler(topic = "ta_808", commandId = "808")
public class TA_808_KafkaHandler extends AbstractKafkaCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(TA_808_KafkaHandler.class);

    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Autowired
    private MessageChannel messageChannel;


    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;


    @Autowired
    private TerminalProtoVersionCache terminalProtoVersionCache;

    @Qualifier(OpentspQueues.PERSONAL)
    @Autowired
    private Queue queue;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    protected TA_808_KafkaHandler() {
        super(TA_808_KafkaHandler.class);
    }

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        try {
            byte[] packet = Convert.hexStringToBytes(new String(kafkaCommand.getMessage()));
            //构建内部数据包对象
            Packet outpacket = new Packet();
            //去包头包尾
            byte[] tempBytes = new byte[packet.length - 2];
            System.arraycopy(packet, 1, tempBytes, 0, tempBytes.length);
            //转义还原
            byte[] bytes = PacketProcessing.unEscape(tempBytes, LCConstant.TERMINAL_TO_ESCAPE_2011, LCConstant.TERMINAL_ESCAPE_2011);
            //取出源数据检验码
            int checkCode = bytes[bytes.length - 1];
            //计算检验码
            int tempCode = PacketProcessing.checkPackage(bytes, 0, bytes.length - 2);
            if (checkCode != tempCode) {
                log.error("Xor error , result[" + tempCode + "],source[" + checkCode
                        + "].source data :\t" + Convert.bytesToHexString(packet));
                return;
            }
            int cmdId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 2), 2);
            String uniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 4, 6));
            int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 10, 2), 2);
            int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 2, 2), 2);
            Command command = this.protocolDispatcher.getHandler(kafkaCommand.getCommandId());
            outpacket.setCommand(cmdId);
            outpacket.setSerialNumber(serialNumber);
            outpacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
            outpacket.setOriginalPacket(packet);
            outpacket.setUniqueMark(uniqueMark);
            outpacket.setFrom(Convert.uniqueMarkToLong(uniqueMark));
            PacketResult result = null;
            if (command != null) {
                TAMonitorCache.addTACount();
                result = command.processor(null, outpacket);
                //异步发送kafka
                if (result.getKafkaPacket() != null) {
                    writeKafKaToDP(result.getKafkaPacket(), TopicConstants.POSRAW);
                }

            } else {
                log.error("指令[ " + outpacket.getCommandForHex()
                        + " ]未找到匹配的协议解析类 . ");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public String getProtoVersion(String uniqueMark) {
        String protoCode = terminalProtoVersionCache.get(uniqueMark);
        log.info("{}从缓存获取的版本号是{}", uniqueMark, protoCode);
        if (org.springframework.util.StringUtils.isEmpty(protoCode)) {
            TerminalProtoVersionCommand terminalProtoVersionCommand = new TerminalProtoVersionCommand();
            terminalProtoVersionCommand.setDeviceId(uniqueMark);
            terminalProtoVersionCommand.setReturnAddress(queue.getName());
            terminalProtoVersionCommand.setSendId(uuidGenerator.generate().toString());
            messageChannel.send(terminalProtoVersionCommand);
        }

        return protoCode;
    }


    /**
     * 发送kafka数据到DP
     *
     * @param packet
     * @param topicName
     */
    public void writeKafKaToDP(Packet packet, String topicName) {

        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
            long startTime = System.currentTimeMillis();
            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(topicName);
            kafkaCommand.setKey(packet.getUniqueMark());
            kafkaMessageChannel.send(kafkaCommand);

            TAMonitorCache.addKafkaTime(System.currentTimeMillis() - startTime);
            TAMonitorCache.addKafkaCount();
        } catch (Exception e) {
            log.error("序列化出错!{}", kafkaCommand, e);
        }
    }

}

