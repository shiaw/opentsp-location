package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.TerminalManage;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.SubscribeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty.NettyChannelMap;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import com.navinfo.opentspcore.common.gateway.ClientConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * push直接回复给RP的
 *
 * @author Lenovo
 * @date 2016-09-29
 * @modify
 * @copyright
 */
public abstract class PushCommand {
    public static Logger logger = LoggerFactory.getLogger(PushCommand.class);

    @Autowired
    protected NettyClientConnections connections;

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
                            logger.info("发送hy数据包详情：" + packet.getCommandForHex() + "," + packet.getUniqueMark());
                            connection.send(packet);
                        }
                    }
                }
                break;
            }
        }
    }


    public abstract void handle(ResultNoticeCommand command);
}
