package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalManage;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.SubscribeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty.NettyChannelMap;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.Command;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.da.receiver.RmiConstant;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DpProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.rmi.DaRmiService;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.dp.command.PacketCommand;
import com.navinfo.opentsp.platform.dp.command.RefreshGroupDpCacheCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCServerCommonRes.ServerCommonRes;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import com.navinfo.opentspcore.common.gateway.ClientConnection;
import com.navinfo.opentspcore.common.push.PushConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public abstract class RPCommand extends Command {

    public static Logger logger = LoggerFactory.getLogger(RPCommand.class);

    public final static String P_SESSION_ID = "P_SESSION_ID";

    @Autowired
    private DaRmiService daRmiService;

    @Autowired
    protected NettyClientConnections connections;

    @Qualifier(OpentspQueues.PERSONAL)
    @Autowired
    private Queue queue;

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Autowired
    private DpProtocolDispatcher dpProtocolDispatcher;

    @Autowired
    private MessageChannel messageChannel;

    /**
     * 发送通用应答
     *
     * @param uniqueMark
     * @param responsesSerialNumber
     * @param responsesId
     * @param result
     * @return
     */
    public Packet commonResponses(String uniqueMark, int responsesSerialNumber, int responsesId, LCPlatformResponseResult.PlatformResponseResult result, long to) {
        Packet outPacket = new Packet(true);
        ServerCommonRes.Builder builder = ServerCommonRes.newBuilder();
        builder.setSerialNumber(responsesSerialNumber);
        builder.setResponseId(responsesId);
        builder.setResults(result);

        outPacket.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
        outPacket.setProtocol(LCConstant.LCMessageType.PLATFORM);
        outPacket.setContent(builder.build().toByteArray());
        outPacket.setUniqueMark(uniqueMark);
        outPacket.setTo(to);
        return outPacket;
    }


    /**
     * 发送kafka数据到DP
     *
     * @param packet
     * @param topicName
     */
   /* public void writeKafKaToDP(Packet packet, String topicName) {
        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
            //kafkaCommand.setMessage(JSON.toJSONBytes(packet));
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(topicName);
            kafkaCommand.setKey(packet.getUniqueMark());
            kafkaMessageChannel.send(kafkaCommand);
        } catch (Exception e) {
            logger.error("序列化出错!{}", kafkaCommand, e);
        }
    }*/

    /**
     * 通过RP向终端发送数据，改造后，不经过DP业务处理的直接发给终端，否则发给DP，由DP发给终端。
     * 缺少RP需找发给DP的链路寻址，临时处理，广播给所有DP
     *
     * @param packet
     * @return
     */
    public void writeToTerminal(Packet packet) {
        DeviceCommand command = new DeviceCommand();
        //terminal + "_" + command + "_" + serialNumber;
        String id = packet.getUniqueMark() + "_" + packet.getCommand() + "_" + packet.getSerialNumber();
        command.setId(id);
        command.setCommand(packet.getCommandForHex());
        command.setDevice(packet.getUniqueMark());
        command.setQueueName(queue.getName());
        //command.setPushConfig(PushConfig.builder().ttl(10 * 60 * 3600).builder());
        Map<String, Object> map = new HashMap<>();
        map.put("packetContent", Convert.bytesToHexString(packet.getContent()));
        map.put("serialnumber", packet.getSerialNumber());
        command.setArguments(map);
        messageChannel.send(command);
        /*try {
            com.navinfo.opentsp.common.messaging.Command.Result result = messageChannel.sendAndReceive(command);
            if (null == result) {
                return;
            } else if (result instanceof DeviceCommand.Result) {
                DPCommand handler = dpProtocolDispatcher.getHandler("");
                handler.handle(null);
            } else {
                logger.error("Command Result Error :{}");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        /*messageChannel.sendAndReceive(command, new ResultCallback<DeviceCommand.Result>() {
            @Override
            public void onResult(DeviceCommand.Result result) {
                if (null == result) {
                    return;
                } else if (result instanceof DeviceCommand.Result) {
                    DPCommand handler = dpProtocolDispatcher.getHandler("");
                    handler.handle(null);
                } else {
                    logger.error("Command Result Error :{}");
                }
            }

            @Override
            public void onError(RuntimeException e) {
                logger.error("Exception :{}", e);
            }
        });*/
        logger.info("send deviceCommand to terminal " + id);
    }


    /**
     * 通过mq向dp发送数据
     *
     * @param packet
     */
    public void writeMQToDP(Packet packet) {
        PacketCommand command = new PacketCommand();
        packet.setQueueName(queue.getName());
        command.setPacket(packet);
        //单播command
        messageChannel.send(command);
        logger.info("send message to dp commandid " + packet.getCommand() + " " + packet.getCommandForHex());
    }

    /**
     * 向DA节点发送数据
     * @param packet
     * @return
     */
    public int writeToDataAccess(Packet packet){
        daRmiService.callRmi(RmiConstant.RMI_SERVICE_ID,
                RmiConstant.RMI_INTERFACE_NAME,
                RmiConstant.RmiInterFaceEum.DaRmiInterface.getClassType(),
                packet);
        return 0;
    }

    /**
     * 向所有的DP节点广播，第一个链路发送可以转发DA的packet，通用规则，
     * <p>
     * 1、记录所有有效的RP->DP 链路
     * 2、存在有效链路时， 第一个链路发送可以转发DA的packet
     *
     * @param _packet
     * @return
     */
    public void broadcastCommon(Packet _packet_saveDA, Packet _packet) {
        PacketCommand command = new PacketCommand();
        command.setPacket(_packet_saveDA);
        _packet_saveDA.setQueueName(queue.getName());
        //单播command
        messageChannel.send(command);
        //writeToDataAccess(_packet_saveDA);
        logger.info("send message to dp commandid " + _packet_saveDA.getCommandForHex());
        //组播command
        if (_packet != null) {
            RefreshGroupDpCacheCommand refreshDpCacheCommand = new RefreshGroupDpCacheCommand();
            refreshDpCacheCommand.setPacket(_packet);
            messageChannel.send(refreshDpCacheCommand);
        }
    }


    /**
     * 向所有的DP节点广播，第一个链路发送可以转发DA的packet，通用规则，
     * <p>
     * 1、记录所有有效的RP->DP 链路
     * 2、存在有效链路时， 第一个链路发送可以转发DA的packet
     *
     * @param _packet
     * @return
     */
    public void broadcastCommon2(Packet _packet_saveDA, Packet _packet) {
        PacketCommand command = new PacketCommand();
        command.setPacket(_packet_saveDA);
        _packet_saveDA.setQueueName(queue.getName());
        //单播command
        messageChannel.send(command);
        messageChannel.send(_packet);
    }

    /**
     * 向指定业务系统发送终端数据
     *
     * @param packet
     * @return
     */
    public void writeToUpper(Packet packet) {
        //ClientConnection connection = connections.getConnectionByDevice(packet.getUpperUniqueMark());
        Map<String, String> userMap = NettyChannelMap.userMap;
        SubscribeEntry subscribe = null;
        for (Map.Entry<String, String> entry : userMap.entrySet()) {
            subscribe = TerminalManage.getInstance().getSubscribe(Long.parseLong(packet.getUniqueMark()) + entry.getValue());
            if (subscribe != null) {
                Vector<String> uniqueMarks = subscribe.getUniqueMarks();
                if (uniqueMarks != null) {
                    Iterator<String> iterator = uniqueMarks.iterator();
                    while (iterator.hasNext()) {
                        String uniqueMark = iterator.next();
                        ClientConnection connection = connections.getConnection(uniqueMark);
                        if (connection != null) {
                            connection.send(packet);
                        }
                    }
                }
                break;
            }
        }
    }
}
