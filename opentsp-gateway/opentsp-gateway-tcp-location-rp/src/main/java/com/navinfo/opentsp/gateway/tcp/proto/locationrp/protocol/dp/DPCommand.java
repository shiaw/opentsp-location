package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.netty.NettyChannelMap;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnections;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Map;


public abstract class DPCommand extends AbstractKafkaCommandHandler {
    public static Logger logger = LoggerFactory.getLogger(DPCommand.class);

    @Autowired
    protected NettyClientConnections connections;

    @Value("${opentsp.server.rp.master.port}")
    private int rpMasterPort;

    protected DPCommand() {
        super(KafkaCommand.class);
    }

    /**
     * 向指定业务系统发送终端数据
     *
     * @param packet
     * @return
     */
    public void writeToUpper(Packet packet) {
        for (Map.Entry<String, NettyClientConnection> obj : NettyChannelMap.deviceChannel.entrySet()) {
            NettyClientConnection connection = obj.getValue();
            if (connection != null) {
                try {
                    if(connection.getDevice().indexOf(rpMasterPort+"") > 0) {
                        logger.info(connection.getChannel()+"发送hy数据包详情：" + packet.getCommandForHex() + "," + packet.getUniqueMark());
                        connection.send(packet);
                    }
                }catch (Exception e) {
                    logger.error("发送hy数据包异常：" + packet.getCommandForHex() + "," + packet.getUniqueMark()+","+e.getMessage());
                }
            } else {
                logger.info(connection.getChannel()+"发送hy数据包异常：" + packet.getCommandForHex() + "," + packet.getUniqueMark());
            }
        }
    }
}
