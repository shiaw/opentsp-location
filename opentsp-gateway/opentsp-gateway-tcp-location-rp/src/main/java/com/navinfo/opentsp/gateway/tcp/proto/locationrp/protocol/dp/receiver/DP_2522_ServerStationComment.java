package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 服务站点评通知 kafka消费
 */
@LocationCommand(id = "2522")
public class DP_2522_ServerStationComment extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        return packet;
    }
}
