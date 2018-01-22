package com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty;


import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.Command;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.ProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.PacketProcessing;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 服务分发
 */
@Component
@ChannelHandler.Sharable
public class DispatchTcpHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(DispatchTcpHandler.class);

    /**
     * 链路唯一标识Key
     */
    public static final String UniqueMark = "UniqueMark";

    private static final AttributeKey<Integer> attributeKey = AttributeKey.valueOf(UniqueMark);

    @Value("${nginx.filter.ip}")
    private String[] filterIps;

    private static int COUNT_START = 0;

    private static int COUNT_END = 0;

    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Autowired
    protected NettyClientConnections connections;

//    @Autowired
//    protected SendOfflineMessageHandler sendOfflineMessageHandler;

    @Autowired
    private MessageChannel messageChannel;

    @Qualifier(OpentspQueues.PERSONAL)
    @Autowired
    private Queue queue;

    @Autowired
    private UpperOplogCache cache;

    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();

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
            log.info("channelRegistered {} ", ctx.channel().remoteAddress().toString());
        }
    }

    /**
     * 客户端连接后，再触发此方法
     *
     * @param ctx
     * @throws Exception
     */
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
            COUNT_START++;
        }
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
            COUNT_END++;
            NettyClientConnection connection = this.connections.getConnection(ctx.channel());
            NettyChannelMap.userMap.remove(connection.getId());
            NettyChannelMap.remove(connection);
            //sendOfflineMessageHandler.sendOfflineMsg(this.connections.getConnection(ctx.channel()));
        }
        ctx.fireChannelInactive();

    }

    /**
     * 客户端异常,断开连接后，再触发此方法
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        NettyClientConnection connection = this.connections.getConnection(ctx.channel());
        NettyChannelMap.userMap.remove(connection.getId());
        NettyChannelMap.remove(connection);
        ctx.fireExceptionCaught(cause);
        log.error("exceptionCaught", cause);
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
                NettyClientConnection connection = this.connections.getConnection(ctx.channel());
                NettyChannelMap.userMap.remove(connection.getId());
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                log.info("WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                log.info("ALL_IDLE");
                // 发送心跳
                //ctx.channel().write("ping\n");
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
    public void channelRead(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        byte[] packet = (byte[]) o;
        try {
            //构建内部数据包对象
            Packet outpacket = new Packet();
            //去包头包尾
            byte[] tempBytes = new byte[packet.length - 2];
            System.arraycopy(packet, 1, tempBytes, 0, tempBytes.length);
            //转义还原
            byte[] bytes = PacketProcessing.unEscape(tempBytes, LCConstant.toEscapeByte, LCConstant.escapeByte);
            //取出源数据检验码
            int checkCode = bytes[bytes.length - 1];
            //计算检验码
            int tempCode = PacketProcessing.checkPackage(bytes, 0, bytes.length - 2);
            if (checkCode != tempCode) {
                log.error("Xor error , result[" + tempCode + "],source[" + checkCode + "].source data :\t" + Convert.bytesToHexString(packet));
                return;
            }
            /**
             *  消息ID	WORD
             消息体属性	WORD
             唯一标识	BCD[6]
             消息流水号	WORD
             消息封包项
             */
            int messageType = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 1), 1);
            int cmdId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 1, 2), 2);
            int cmdProperty = Convert.byte2Int(ArraysUtils.subarrays(bytes, 3, 2), 2);
            String tempUniqueMark = Convert.bytesToHexString(ArraysUtils.subarrays(bytes, 5, 6));
            int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 11, 2), 2);
            //构建内部数据包对象
            outpacket.setCommand(cmdId);
            outpacket.setSerialNumber(serialNumber);
            outpacket.setProtocol(messageType);
            outpacket.setOriginalPacket(packet);
            outpacket.setUniqueMark(tempUniqueMark);
            outpacket.setFrom(Long.parseLong(tempUniqueMark));

            //获取链路标识
            //如果标识不存,指令也不为登录或者重连指令,则断开链路
            NettyClientConnection connection = this.connections.getConnection(channelHandlerContext.channel());
            log.info("connection id:{} uniqueMark:{} deviceId：{}", connection, tempUniqueMark, connection.getDevice());

            String hexCmdId = Convert.decimalToHexadecimal(cmdId, 4);
            Command command = this.protocolDispatcher.getHandler(hexCmdId);
            //connection.setDevice(outpacket.getUniqueMark());
            String upperUniqueMark = connection.getDevice();
            Attribute<Integer> attribute = channelHandlerContext.channel().attr(attributeKey);
            if (upperUniqueMark == null) {
                if (cmdId == AllCommands.Platform.Heartbeat_VALUE || cmdId == AllCommands.Platform.SubscribeRequest_VALUE || cmdId == AllCommands.Platform.RequestLoginKey_VALUE || cmdId == AllCommands.Platform.MultiServerAuth_VALUE || cmdId == AllCommands.Platform.ServerLogin_VALUE || cmdId == AllCommands.Platform.Reconnect_VALUE || cmdId == AllCommands.Platform.Logout_VALUE) {
                    //session.setAttribute(Constant.UniqueMark , tempUniqueMark);
                    //outpacket.addParameter(RPCommand.P_SESSION_ID, attribute.get());
                    outpacket.addParameter(RPCommand.P_SESSION_ID, connection.getId());
                } else {
                    log.info("非法链路,服务器主动断开.");
                    //channelHandlerContext.close();
                    //return;
                }
            }
            //判断是否分包
            if ((cmdProperty & 8192) > 0) {
                int total = Convert.byte2Int(ArraysUtils.subarrays(bytes, 13, 2), 2);// 消息包封装项,包总数
                int serial = Convert.byte2Int(ArraysUtils.subarrays(bytes, 15, 2), 2);// 消息包封装项,包序号
                int blockId = Convert.byte2Int(ArraysUtils.subarrays(bytes, 17, 2), 2);//消息包封装项,包块ID
                byte[] content = ArraysUtils.subarrays(bytes, 19, bytes.length - 20);
                outpacket.setPacketTotal(total);
                outpacket.setPacketSerial(serial);
                outpacket.setBlockId(blockId);
                outpacket.setContent(content);
                boolean isComplete = PacketProcessing.mergeBlock(outpacket);
                if (isComplete) {
                    outpacket = PacketProcessing.getCompletePacket(PacketProcessing.getCacheBlockId(tempUniqueMark, blockId));
                } else {
                    return;
                }

            } else {
                byte[] content = ArraysUtils.subarrays(bytes, 13, bytes.length - 14);
                outpacket.setContent(content);
            }

            if (outpacket.getProtocol() == LCConstant.LCMessageType.TERMINAL) {
                // 缓存此系统的操作记录,提供给终端答应寻址
                //UpperOplogCache.addOplog(outpacket.getUniqueMark(), outpacket.getCommand(), outpacket.getSerialNumber(), attribute.get());
                cache.addOplog(outpacket.getUniqueMark(), outpacket.getCommand(), outpacket.getSerialNumber(), upperUniqueMark);
                outpacket.setUpperUniqueMark(upperUniqueMark);
            }
            Packet result = null;
            if (command != null) {
                //向业务系统发送数据
                result = command.processor(connection, outpacket);
                if (result == null)
                    return;
            } else {
                log.error("指令[ " + outpacket.getCommandForHex() + " ]未找到匹配的协议解析类 . ");
            }
            channelHandlerContext.writeAndFlush(result);
//            List<byte[]> list = (List<byte[]>) o;
//            for (byte[] packet : list) {
//
//            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ReferenceCountUtil.release(packet);  // ((ByteBuf) o).release();
        }
        channelHandlerContext.channel().flush();
    }

}
