package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.netty808;

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
 *
 * @author zhanhk
 * @version 1.0
 * @date 2015-09-01
 * @modify
 * @copyright Navi Tsp
 */
@Component
@ChannelHandler.Sharable
public class Dispatch808TcpHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(Dispatch808TcpHandler.class);

    private AttributeKey<String> attributeKey = AttributeKey.valueOf("sn");

    @Value("${nginx.filter.ip}")
    private String[] filterIps;

    private static int COUNT_START = 0;

    private static int COUNT_END = 0;

    @Value("${topic.name.808:poscan.raw.808}")
    private String topicName;

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
        }
        COUNT_END++;
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
        log.error("[exceptionCaught] 客户端异常断开连接,[{}],{}", ctx.channel().remoteAddress().toString(), cause.getMessage());
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
            StatisticsUtils.sendTotalPacketNum808.getAndIncrement();
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

            String hexCmdId = Convert.decimalToHexadecimal(cmdId, 4);

            //判断是否分包
            outpacket = MergePacketProcess.addMergePacketToAcceptor(outpacket, bytes);
            outpacket.setCommand(cmdId);
            outpacket.setSerialNumber(serialNumber);
            outpacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
            outpacket.setOriginalPacket(packet);
            outpacket.setUniqueMark(uniqueMark);
            outpacket.setFrom(Convert.uniqueMarkToLong(uniqueMark));

            //写kafka 0200 0704
            List<String> filterCodeList = filterCodeConfig.getFilterCodeList();
            if (filterCodeList.isEmpty() || filterCodeList.contains(hexCmdId)) {
                KafkaCommand kafkaCommand = new KafkaCommand();
                try {
                    kafkaCommand.setMessage(outpacket.getContent());
                    kafkaCommand.setCommandId(outpacket.getCommandForHex());
                    kafkaCommand.setTopic(topicName);
                    kafkaCommand.setKey(outpacket.getUniqueMark());
                    kafkaMessageChannel.send(kafkaCommand);
                    StatisticsUtils.sendTotalPacketNum808_kafka.getAndIncrement();
                } catch (Exception e) {
                    StatisticsUtils.sendTotalPacketNum808_kafka_fail.getAndIncrement();
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
