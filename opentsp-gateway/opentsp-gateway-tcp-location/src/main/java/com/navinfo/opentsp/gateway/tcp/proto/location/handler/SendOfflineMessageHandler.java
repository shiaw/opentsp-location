package com.navinfo.opentsp.gateway.tcp.proto.location.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.api.ConnectionEventHub;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalMileageOilTypeCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalOnlineSwitch;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalOnlineSwitch.TerminalOnlineSwitch;
import com.navinfo.opentsp.platform.push.ClientOfflineCommand;
import com.sun.javafx.scene.NodeHelper;
import io.netty.util.internal.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * 发送下线消息处理类
 * User: zhanhk
 * Date: 16/7/26
 * Time: 下午3:10
 */
@Component
public class SendOfflineMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(SendOfflineMessageHandler.class);

    private final MessageChannel messageChannel;

    private final String queueName;

    private final KafkaMessageChannel kafkaMessageChannel;

    private final TerminalMileageOilTypeCache terminalMileageOilTypeCache;


    @Autowired
    public SendOfflineMessageHandler(TerminalMileageOilTypeCache terminalMileageOilTypeCache,MessageChannel messageChannel, @Qualifier(OpentspQueues.PERSONAL) Queue queue, KafkaMessageChannel kafkaMessageChannel) {
        this.terminalMileageOilTypeCache=terminalMileageOilTypeCache;
        this.messageChannel = messageChannel;
        this.queueName = queue.getName();
        this.kafkaMessageChannel = kafkaMessageChannel;
    }

    /**
     * 发送终端设备下线消息到push,不需要push响应
     *
     * @param nettyClientConnection 当前连接信息
     */
    public void sendOfflineMsg(NettyClientConnection nettyClientConnection) {
        try {
            String device = nettyClientConnection.getDevice();
            if (device != null && !"".equals(device)) {
                ClientOfflineCommand clientOfflineCommand = new ClientOfflineCommand();
                clientOfflineCommand.setDeveiceId(device);
                clientOfflineCommand.setQueueName(queueName);
                messageChannel.send(clientOfflineCommand);
                //通知dp
                TerminalOnlineSwitch.Builder builder = TerminalOnlineSwitch.newBuilder();
                builder.setSwitchDate(System.currentTimeMillis() / 1000);
                builder.setStatus(false);
                log.info("掉线终端 [ " + device + " ]");
//				TerminalOnlineSwitchInfoSave.Builder builder = TerminalOnlineSwitchInfoSave.newBuilder();
//				builder.setTerminalId(terminal.getTerminalId());
//				builder.setStatus(false);
//				builder.setSwitchDate(NodeHelper.getCurrentTime());

                Packet pk = new Packet(true);
                pk.setCommand(LCAllCommands.AllCommands.Terminal.TerminalOnlineSwitch_VALUE);
                pk.setProtocol(LCConstant.LCMessageType.TERMINAL);
                pk.setUniqueMark(device);
                pk.setFrom(0L);
                pk.setContent(builder.build().toByteArray());

                KafkaCommand kafkaCommand = new KafkaCommand();
                try {
                    kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(pk));
                    kafkaCommand.setCommandId(pk.getCommandForHex());
                    kafkaCommand.setTopic(TopicConstants.POSRAW);
                    kafkaCommand.setKey(device);
                    kafkaMessageChannel.send(kafkaCommand);

                    if("3007".equals(kafkaCommand.getCommandId())){
                        log.info("3007 发送RPPOSDONE");
                        kafkaCommand.setTopic(TopicConstants.RPPOSDONE);
                        kafkaMessageChannel.send(kafkaCommand);
                    }
                } catch (JsonProcessingException e) {
                    log.error("序列化出错!{}", kafkaCommand, e);
                }
                terminalMileageOilTypeCache.remove(device);
                log.info("send offline command :" + clientOfflineCommand.toString());
            } else {
                log.error("device is null !", nettyClientConnection.getId());
            }
        } catch (Exception e) {
            log.error("send offine command error", e);
        }
    }
}
