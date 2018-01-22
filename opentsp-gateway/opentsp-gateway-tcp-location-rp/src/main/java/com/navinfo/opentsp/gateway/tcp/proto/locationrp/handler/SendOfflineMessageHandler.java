package com.navinfo.opentsp.gateway.tcp.proto.locationrp.handler;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.push.ClientOfflineCommand;
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

    @Autowired
    public SendOfflineMessageHandler(MessageChannel messageChannel, @Qualifier(OpentspQueues.PERSONAL) Queue queue) {
        this.messageChannel = messageChannel;
        this.queueName = queue.getName();
    }

    /**
     * 发送终端设备下线消息到push,不需要push响应
     * @param nettyClientConnection 当前连接信息
     */
    public void sendOfflineMsg(NettyClientConnection nettyClientConnection) {
        try {
            String device = nettyClientConnection.getDevice();
            if(device!=null&&!"".equals(device)) {
                ClientOfflineCommand clientOfflineCommand = new ClientOfflineCommand();
                clientOfflineCommand.setDeveiceId(device);
                clientOfflineCommand.setQueueName(queueName);
                messageChannel.send(clientOfflineCommand);
                log.info("send offline command :" + clientOfflineCommand.toString());
            } else {
                log.error("device is null !",nettyClientConnection.getId());
            }
        }catch (Exception e) {
            log.error("send offine command error",e);
        }
    }
}
