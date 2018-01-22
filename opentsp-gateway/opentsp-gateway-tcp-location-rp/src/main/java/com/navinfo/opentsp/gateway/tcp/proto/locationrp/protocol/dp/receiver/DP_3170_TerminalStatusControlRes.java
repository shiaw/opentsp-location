package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalManage;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.SubscribeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;

/**
 * 外设控制应答151021
 */
@DPAnno(id = "3170")
public class DP_3170_TerminalStatusControlRes extends PushCommand {

    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    @Autowired
    private UpperOplogCache cache;

    @Autowired
    private NettyClientConnections nettyClientConnections;

    @Override
    public void handle(ResultNoticeCommand command) {
        Packet packet = new Packet();
        packet.setContent(command.getMessage());
        packet.setUniqueMark(command.getDevice());
        packet.setCommand(command.getCommand());
        packet.setProtocol(command.getProtocol());
        packet.setSerialNumber(command.getSerialNumber());

        try {
            //SubscribeEntry subscribeEntry = TerminalManage.getInstance().getSubscribe(command.getDevice());
            //if (subscribeEntry != null) {
               /* List<ClientConnection> subscribe = subscribeEntry.getSubscribeSession();
                if (subscribe != null) {
                    for (ClientConnection clientConnection : subscribe) {
                        if (clientConnection != null) {
                            clientConnection.send(packet);
                        }
                    }
                }*/
                super.writeToUpper(packet);
                /*if (subscribe.size() == 0) {
                    logger.error(df.format(new Date()) + " :终端[" + packet.getUniqueMark() + "] : 获取到 List<MutualSession> 对象的长度为0，没有获取到MutualSession");
                }*/
            //}
        } catch (Exception e) {
            logger.error("3170", e);
        }
    }
}
