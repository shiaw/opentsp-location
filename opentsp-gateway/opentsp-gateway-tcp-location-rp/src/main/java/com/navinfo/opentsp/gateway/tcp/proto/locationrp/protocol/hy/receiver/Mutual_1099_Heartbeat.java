package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 平台链路心跳
 */
@LocationCommand(id = "1099")
public class Mutual_1099_Heartbeat extends RPCommand {

    private static final Logger log = LoggerFactory.getLogger(Mutual_1099_Heartbeat.class);

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        log.info("收到来自[" + packet.getFrom() + "]平台心跳数据.");
        return super.commonResponses(packet.getUniqueMark(),
                packet.getSerialNumber(),
                LCAllCommands.AllCommands.Platform.Heartbeat_VALUE,
                LCPlatformResponseResult.PlatformResponseResult.success,
                packet.getFrom());
    }

}
