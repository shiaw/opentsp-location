package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 位置查询
 */
@LocationCommand(id = "2050")
public class Mutual_2050_CallName extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        this.writeToTerminal(packet);
        return null;
    }

}
