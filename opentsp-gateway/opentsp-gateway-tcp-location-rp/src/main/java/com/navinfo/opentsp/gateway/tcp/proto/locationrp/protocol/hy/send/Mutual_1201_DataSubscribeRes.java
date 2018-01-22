package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.send;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 数据订阅应答
 */
@LocationCommand(id = "1201")
public class Mutual_1201_DataSubscribeRes extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        return null;
    }
}
