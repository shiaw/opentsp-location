package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.navinfo.opentsp.common.messaging.*;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.StringUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.auth.TerminalRegisterCommand;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRecieveDateSave;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Map;

@LocationCommand(id = "0100")
public class JT_0100_TerminalRegister extends TerminalCommand {

    @Autowired
    private MessageChannel messageChannel;

    private final String queueName;

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    @Autowired
    public JT_0100_TerminalRegister( @Qualifier(OpentspQueues.PERSONAL) Queue queue) {
        this.queueName = queue.getName();
    }

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        PacketResult packetResult=new PacketResult();
        if (packet.getResultCode() == 0) {
            byte[] content = packet.getContent();
            if (content == null || content.length < 37) {
                packetResult.setTerminalPacket(super.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), 2));
                return packetResult;
            }
            int province = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
            int city = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 2), 2);
            String produce = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 4, 5));
            String terminalType = StringUtils.bytesToGbkString(filterFillCharacter(ArraysUtils.subarrays(content, 9, 20)));
            String deviceId = StringUtils.bytesToGbkString(filterFillCharacter(ArraysUtils.subarrays(content, 29, 7)));//设备id,//设备id,这里接设备id不能用this.a()
            int licenseColor = Convert.byte2Int(ArraysUtils.subarrays(content, 36, 1), 1);
            String license = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 37, content.length - 37));
            log.info("收到终端注册数据：省域：" + province + ",市县域：" + city + ",制造商：" + produce + ",终端型号：" + terminalType + ",终端ID：" + deviceId + ",车牌颜色：" + licenseColor + ",车牌：" + license);
            TerminalRegisterCommand terminalRegisterCommand = new TerminalRegisterCommand();
            Map params = new HashMap<>();
            params.put("province", province);
            params.put("city", city);
            params.put("produce", produce);
            params.put("terminalModel", terminalType);
            params.put("terminalIdentify", deviceId);
            params.put("licenseColor", licenseColor);
            params.put("license", license);
            params.put("terminalId", packet.getUniqueMark());
            params.put("serialNumber",packet.getSerialNumber());
            final String id = connection.getId();
            connection.setDevice(packet.getUniqueMark());
            String sendId = uuidGenerator.generate().toString();
            connection.setDevice(packet.getUniqueMark());
            terminalRegisterCommand.setParams(params);
            terminalRegisterCommand.setSendId(sendId);
            terminalRegisterCommand.setReturnAddress(queueName);
            terminalRegisterCommand.setCmd("8100");
            terminalRegisterCommand.setDeviceId(packet.getUniqueMark());
            try {
                messageChannel.send(terminalRegisterCommand);

                //添加首次连接时间
                Packet pk = new Packet(true);
                pk.setCommand(2440);
                pk.setProtocol(LCConstant.LCMessageType.PLATFORM);
                LCTerminalRecieveDateSave.TerminalRecieveDateSave.Builder builder = LCTerminalRecieveDateSave.TerminalRecieveDateSave.newBuilder();
                builder.setTerminalId(Long.valueOf(packet.getUniqueMark()));
                builder.setRecieveDate(System.currentTimeMillis()/1000);
                pk.setContent(builder.build().toByteArray());

                KafkaCommand kafkaCommand = new KafkaCommand();
                kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(pk));
                kafkaCommand.setCommandId(pk.getCommandForHex());
                kafkaCommand.setTopic("daposdone");
                kafkaCommand.setKey(packet.getUniqueMark());
                kafkaMessageChannel.send(kafkaCommand);
            } catch (Exception e) {
                log.error("{}注册指令发送失败{}", packet.getUniqueMark(), e.getMessage());
            }
        }
        return null;

    }

    private byte[] filterFillCharacter(byte[] bytes) {
        int index = bytes.length;
        for (int i = bytes.length - 1; i >= 0; i--) {
            if (bytes[i] == 0x00) {
                index = i;
            } else {
                break;
            }
        }
        return ArraysUtils.subarrays(bytes, 0, index);
    }
}
