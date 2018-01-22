package com.navinfo.opentsp.gateway.tcp.proto.location.netty;

import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.handler.SendOfflineMessageHandler;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.Forward808Data;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.TcpClient;
import com.navinfo.opentsp.platform.auth.DownStatusCommand;
import com.navinfo.opentsp.platform.auth.TerminalAuthCommand;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.Command;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.ProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.*;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.auth.TerminalProtoVersionCommand;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.push.DownCommandState;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.*;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 服务分发
 *
 * @author zhanhk
 * @version 1.0
 * @date 2015-09-01
 * @modify
 * @copyright Navi Tsp
 */
@Component
@ChannelHandler.Sharable
public class DispatchTcpHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(DispatchTcpHandler.class);

    private AttributeKey<String> attributeKey = AttributeKey.valueOf("sn");

    @Value("${nginx.filter.ip}")
    private String[] filterIps;

    private static int COUNT_START = 0;

    private static int COUNT_END = 0;

    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Autowired
    protected NettyClientConnections connections;

    @Autowired
    protected SendOfflineMessageHandler sendOfflineMessageHandler;

    @Autowired
    private TerminalProtoVersionCache terminalProtoVersionCache;

    @Autowired
    private MessageChannel messageChannel;


    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Qualifier(OpentspQueues.PERSONAL)
    @Autowired
    private Queue queue;

    @Value("${opentsp.location.close.batch.offline:false}")
    private boolean isBatchOffline;


    @Value("${opentsp.location.808.auth:true}")
    private boolean is808Auth;

    @Value("${forword.808.open:false}")
    private boolean forword808Open;
    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

    @Autowired
    private Forward808Data forward808Data;

    /**
     * 先打开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelRegistered();
        String client = ctx.channel().remoteAddress().toString();
        boolean isLog = true;
        for (String ip : filterIps) {
            if (client.indexOf(ip) != -1) {
                isLog = false;
                break;
            }
        }
        if (isLog) {
            log.info("channelRegistered {}", ctx.channel().remoteAddress().toString());
        }
    }

    /**
     * 客户端连接后，再触发此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        String client = ctx.channel().remoteAddress().toString();
        boolean isLog = true;
        for (String ip : filterIps) {
            if (client.indexOf(ip) != -1) {
                isLog = false;
                break;
            }
        }
        if (isLog) {
            log.info("channelActive {} {}", ctx.channel().remoteAddress().toString(), COUNT_START);
        }
        COUNT_START++;
    }

    /**
     * 客户端主动断开连接
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String client = ctx.channel().remoteAddress().toString();
        boolean isLog = true;
        for (String ip : filterIps) {
            if (client.indexOf(ip) != -1) {
                isLog = false;
                break;
            }
        }
        if (isLog) {
            log.info("channelInactive{} {}", ctx.channel().remoteAddress().toString(), COUNT_END);
            log.error("channelInactive snd sendOfflineMsg");
            sendOfflineMessageHandler.sendOfflineMsg(this.connections.getConnection(ctx.channel()));
        }
        ctx.fireChannelInactive();
        COUNT_END++;
    }

    /**
     * 客户端异常,断开连接后，再触发此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
        log.error("[exceptionCaught] 客户端异常断开连接,[{}],{}", ctx.channel().remoteAddress().toString(), cause.getMessage());
        //sendOfflineMessageHandler.sendOfflineMsg(this.connections.getConnection(ctx.channel()));
    }

    /**
     * 心跳设置
     *
     * @param ctx
     * @param evt
     * @throws Exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state().equals(IdleState.READER_IDLE)) {
                // 超时关闭channel
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                System.out.println("WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                System.out.println("ALL_IDLE");
                // 发送心跳
                ctx.channel().write("ping\n");
            }
        }
        super.userEventTriggered(ctx, evt);
    }

    /**
     * 服务端收到客户端发送过来的消息时，触发此方法，根据command派发指定handler
     *
     * @param channelHandlerContext
     * @param o
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        if (forword808Open) {
            try {
                //taTcpClient.getChannel().writeAndFlush(o);
                //   log.info("{},{}",taTcpClient.getChannel().localAddress().toString(),taTcpClient.getChannel().remoteAddress().toString());
                forward808Data.forward((byte[]) o);
            } catch (Exception e) {
                log.error("转发808失败,{},{}", e.getMessage(), o);
            }
        }
        int serialNumber = 0;
        int cmdId = 0;
        String uniqueMark = "";
        try {

            byte[] packet = (byte[]) o;
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
            cmdId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 2), 2);
            uniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 4, 6));
            serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 10, 2), 2);
            int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 2, 2), 2);
            String hexCmdId = Convert.decimalToHexadecimal(cmdId, 4);
            Command command = this.protocolDispatcher.getHandler(hexCmdId);
//            NettyClientConnection connection = this.connections.getConnection(channelHandlerContext.channel());
//            log.info("connection id:{} uniqueMark:{} deviceId：{}", connection, uniqueMark, connection.getDevice());
            NettyClientConnection connection = connections.getConnection(channelHandlerContext.channel());
            //异常处理
            if (uniqueMark != null) {
                String id = connection.getId();
                NettyClientConnection oldConnection = (NettyClientConnection) connections.getConnectionByDevice(uniqueMark);
                if (oldConnection == null) {
                    connection.setDevice(uniqueMark);
                    log.info("terminal online " + connection.getId() + ",deviceid is " + uniqueMark);
                } else if (!id.equals(oldConnection.getId())) {
                    //相当于异常下线，并重新上线
                    connection.setDevice(uniqueMark);
                    connection.setAuthCode(oldConnection.getAuthCode());
                    oldConnection.setDevice("");
                    log.info("reconnect terminal online " + connection.getId() + ",deviceid is " + uniqueMark);
                    oldConnection.close();//关闭无效连接
                }
            }
            String protoCode = getProtoVersion(uniqueMark);
            if (command == null && org.springframework.util.StringUtils.isEmpty(protoCode)) {
                return;
            }
            if (command == null && !org.springframework.util.StringUtils.isEmpty(protoCode)) {
                command = this.protocolDispatcher.getHandler(protoCode + "_" + hexCmdId);
            }
            if (is808Auth && cmdId != 0x0100 && cmdId != 0x0102) {
                //TODO 鉴权
                if (connection.getAuthCode() == null) {
                    log.error("踢除非法终端[ " + uniqueMark + " ]");
                    //TODO 暂时处理安徽现网车辆不上线问题 hk 2015-12-21
                    Packet tempPacket = new Packet(true, 5);
                    tempPacket.setUniqueMark(uniqueMark);
                    tempPacket.setCommand(0x8001);
                    tempPacket.appendContent(Convert.longTobytes(serialNumber, 2));
                    tempPacket.appendContent(Convert.longTobytes(cmdId, 2));
                    tempPacket.appendContent(Convert.longTobytes(1, 1));
                    channelHandlerContext.writeAndFlush(tempPacket);
                    return;
                }
            }
            //缓存当前节点上的终端最后上报时间
            if (!StringUtils.isEmpty(uniqueMark) && isBatchOffline) {
                log.info("{}终端最后上报时间", uniqueMark);
                TerminalLastTimeCache.getInstance().addEntry(uniqueMark, System.currentTimeMillis());
            }
            //判断是否分包
            outpacket = MergePacketProcess.addMergePacketToAcceptor(outpacket, bytes);
            outpacket.setCommand(cmdId);
            outpacket.setSerialNumber(serialNumber);
            outpacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
            outpacket.setOriginalPacket(packet);
            outpacket.setUniqueMark(uniqueMark);
            outpacket.setFrom(Convert.uniqueMarkToLong(uniqueMark));
            long startTime = System.currentTimeMillis();
            PacketResult result = null;
            if (command != null) {
                TAMonitorCache.addTACount();
                result = command.processor(connection, outpacket);
                if (result == null) {
                    return;
                }
                if (result.getTerminalPacket() != null) {
                    channelHandlerContext.writeAndFlush(result.getTerminalPacket());
                }
                //异步发送kafka
                if (result.getKafkaPacket() != null) {
                    writeKafKaToDP(result.getKafkaPacket(), TopicConstants.POSRAW);
                }
                //鉴权成功之后，下发9102给终端
                if (result.getAnswerPacket() != null) {
                    channelHandlerContext.writeAndFlush(result.getAnswerPacket());
                }

            } else {
                log.error("指令[ " + outpacket.getCommandForHex()
                        + " ]未找到匹配的协议解析类 . ");
            }
        } catch (Exception e) {
            Packet tempPacket = new Packet(false, 5);
            tempPacket.setSerialNumber(serialNumber);
            tempPacket.setUniqueMark(uniqueMark);
            tempPacket.setCommand(0x8001);
            tempPacket.appendContent(Convert.longTobytes(serialNumber, 2));
            tempPacket.appendContent(Convert.longTobytes(cmdId, 2));
            tempPacket.appendContent(Convert.longTobytes(LCResultCode.JTTerminal.SUCCESS, 1));
            channelHandlerContext.writeAndFlush(tempPacket);
            log.error("{}，{},{},{},{}", e.getMessage(), e, Convert.bytesToHexString((byte[]) o), uniqueMark, cmdId);
        }
    }


    public String getProtoVersion(String uniqueMark) {
        String protoCode = terminalProtoVersionCache.get(uniqueMark);
        log.info("{}从缓存获取的版本号是{}", uniqueMark, protoCode);
        if (org.springframework.util.StringUtils.isEmpty(protoCode) || "0".equals(protoCode)) {
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
            //  kafkaCommand.setMessage(packet.getContent());
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
