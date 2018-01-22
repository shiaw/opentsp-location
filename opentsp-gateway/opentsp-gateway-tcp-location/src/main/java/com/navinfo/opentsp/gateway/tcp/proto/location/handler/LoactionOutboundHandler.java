package com.navinfo.opentsp.gateway.tcp.proto.location.handler;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.Command;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.ProtocolDispatcher;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.IdGenerateUtil;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.push.DeviceCommand;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Adapter which handle outgoing messages and convert its into protocol packages.
 */
@ChannelHandler.Sharable
@Component
public class LoactionOutboundHandler extends ChannelOutboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(LoactionOutboundHandler.class);
    @Autowired
    private ProtocolDispatcher protocolDispatcher;

    @Autowired
    protected NettyClientConnections connections;

    private static final String GET_PROTO_VERSION_COMMAND = "getProtoVersion";

    @Autowired
    private TerminalProtoVersionCache terminalProtoVersionCache;

    @Value("${opentsp.down.command.reply}")
    private String[] downCommandReply;
    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Assert.notNull(msg, "msg is null");
        Packet answerPacket = null;
        if ((msg instanceof DeviceCommand)) {
            DeviceCommand command = (DeviceCommand) msg;
            String cmd = command.getCommand();
            String sendId = command.getId();
            Map arguments = command.getArguments();

            //获取配置协议
            if (GET_PROTO_VERSION_COMMAND.equals(cmd)) {
                String protoVersion = (String) arguments.get("protoVersion");
                terminalProtoVersionCache.add(command.getDevice(), protoVersion);
                return;
            }
            Command terminalCommand = this.protocolDispatcher.getHandler(cmd);
            if (terminalCommand != null) {
                NettyClientConnection connection = this.connections.getConnection(ctx.channel());
                int serialnumber=arguments.get("serialnumber")==null?-1:(int)arguments.get("serialnumber");
                log.info("执行下发指令[{}],connectionId={},serialnumber={}, uniqueMark={},deviceId={}", cmd, connection.getId(),serialnumber, command.getDevice(), connection.getDevice());
                Packet packet = new Packet();
                packet.setCommand(Integer.parseInt(cmd, 16));
                packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
                packet.setSerialNumber((serialnumber==-1)?IdGenerateUtil.getId():serialnumber);
                packet.setUniqueMark(command.getDevice());
                // packet.setOriginalPacket(packet);
                packet.setUniqueMark(command.getDevice());
                packet.setResultCode(command.getResultCode());
                packet.setPushArguments(arguments);
                // packet.setFrom(terminalInfo == null ? Convert.uniqueMarkToLong(uniqueMark) : terminalInfo.getTerminalId());
                PacketArguments packetArguments = PacketCache.getInstance().get(sendId);
                if (packetArguments != null) {
                    packet.setSerialNumber(packetArguments.getSerialNumber());
                    PacketCache.getInstance().remove(sendId);
                } else {
                    String reply="";
                    for(String replyCommand:downCommandReply){
                        if(replyCommand.startsWith(command.getCommand())){
                            reply=replyCommand;
                            break;
                        }
                    }

                    if(!StringUtils.isEmpty(reply)){
                        String[] replayArr=reply.split("-");
                        int replyCount=Integer.parseInt(replayArr[1]);
                        for(int n=1;n<=replyCount;n++) {
                            //下发指令，缓存UUID
                            log.info(replyCount+"次回复，下发指令缓存:[commandId={},serialNumber={},sendId={},key={}]", command.getCommand(), packet.getSerialNumber(), sendId,command.getDevice() + "-" + packet.getSerialNumber()+"-"+n);
                            DownCommandCache.getInstance().add(command.getDevice() + "-" + packet.getSerialNumber()+"-"+n, sendId);

                        }

                    }else {
                        //下发指令，缓存UUID
                        log.info("一次回复，下发指令缓存:[commandId={},serialNumber={},sendId={}]", command.getCommand(), packet.getSerialNumber(), sendId);
                        DownCommandCache.getInstance().add(command.getDevice() + "-" + packet.getSerialNumber(), sendId);
                    }
                }
                //传输pb字节数组
                String packetContent = (String) arguments.get("packetContent");
                if (!StringUtils.isEmpty(packetContent)) {
                    packet.setContent(Convert.hexStringToBytes(packetContent));
                }
                PacketResult packetResult = terminalCommand.processor(connection, packet);
                if(packetResult==null){
                    return;
                }
                if(packetResult.getKafkaPacket()!=null){
                    writeKafKaToDP(packetResult.getKafkaPacket(), TopicConstants.POSRAW);
                    if("3007".equals(packetResult.getKafkaPacket().getCommandForHex())){
                        writeKafKaToDP(packetResult.getKafkaPacket(), TopicConstants.RPPOSDONE);
                    }
                }
                answerPacket = packetResult.getAnswerPacket();
                msg=packetResult.getTerminalPacket();
            } else {
                log.error("下发指令[ {} ]未找到匹配的协议解析类 . ", cmd);
            }
        }
        super.write(ctx,msg, promise);

        if(answerPacket != null){
            super.write(ctx,answerPacket,promise);
        }

    }

    /**
     * 发送kafka数据到DP
     * @param packet
     * @param topicName
     */
    public void writeKafKaToDP(Packet packet,String topicName){

        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
            long startTime=System.currentTimeMillis();
            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(topicName);
            kafkaCommand.setKey(packet.getUniqueMark());
            kafkaMessageChannel.send(kafkaCommand);
            TAMonitorCache.addKafkaTime(System.currentTimeMillis()-startTime);
            TAMonitorCache.addKafkaCount();
        } catch (Exception e) {
            log.error("序列化出错!{}",kafkaCommand,e);
        }
    }
}
