package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.netty;


import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.config.FilterCodeConfig;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.*;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.lang.ArraysUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.AttributeKey;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;


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
    public static final String UNIQUE_MARK = "UniqueMark";

    private static final AttributeKey<Integer> INTEGER_ATTRIBUTE_KEY = AttributeKey.valueOf(UNIQUE_MARK);

    @Value("${nginx.filter.ip}")
    private String[] filterIps;

    @Value("${topic.name.inner:poscan.raw}")
    private String topicNameInner;

    private static int COUNT_START = 0;

    private static int COUNT_END = 0;

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Autowired
    private FilterCodeConfig filterCodeConfig;

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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
        log.error("exceptionCaught", cause);
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
                ctx.close();
            } else if (event.state().equals(IdleState.WRITER_IDLE)) {
                log.info("WRITER_IDLE");
            } else if (event.state().equals(IdleState.ALL_IDLE)) {
                log.info("ALL_IDLE");
                // 发送心跳
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
        byte[] packet = (byte[]) o;
        try {
            StatisticsUtils.sendTotalPacketNumInner.getAndIncrement();
            //构建内部数据包对象
            Packet outpacket = new Packet();
            //去包头包尾
            byte[] tempBytes = new byte[packet.length - 2];
            System.arraycopy(packet, 1, tempBytes, 0, tempBytes.length);
            //转义还原
            byte[] bytes = PacketProcessing.unEscape(tempBytes, LCConstant.TO_ESCAPE_BYTE, LCConstant.ESCAPE_BYTE);
            //取出源数据检验码
            int checkCode = bytes[bytes.length - 1];
            //计算检验码
            int tempCode = PacketProcessing.checkPackage(bytes, 0, bytes.length - 2);
            if (checkCode != tempCode) {
                log.error("Xor error , result[" + tempCode + "],source[" + checkCode + "].source data :\t" + Convert.bytesToHexString(packet));
                return;
            }

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

            //NettyClientConnection connection = this.connections.getConnection(channelHandlerContext.channel());

            String hexCmdId = Convert.decimalToHexadecimal(cmdId, 4);
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

            //写kafka 0200 0704
            List<String> filterCodeList = filterCodeConfig.getFilterCodeList();
            if (filterCodeList.isEmpty() || filterCodeList.contains(hexCmdId)) {
                KafkaCommand kafkaCommand = new KafkaCommand();
                try {
                    //kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(outpacket));//之前写法
                    kafkaCommand.setMessage(outpacket.getContent());//改造后省流量写法
                    kafkaCommand.setCommandId(outpacket.getCommandForHex());
                    kafkaCommand.setTopic(topicNameInner);
                    kafkaCommand.setKey(outpacket.getUniqueMark());
                    kafkaMessageChannel.send(kafkaCommand);
                    StatisticsUtils.sendTotalPacketNumInner_kafka.getAndIncrement();
                } catch (Exception e) {
                    StatisticsUtils.sendTotalPacketNumInner_kafka_fail.getAndIncrement();
                    log.error("序列化出错!{}", kafkaCommand, e);
                }
            }

            channelHandlerContext.flush();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ReferenceCountUtil.release(o);
        }
        channelHandlerContext.channel().flush();
    }


}
