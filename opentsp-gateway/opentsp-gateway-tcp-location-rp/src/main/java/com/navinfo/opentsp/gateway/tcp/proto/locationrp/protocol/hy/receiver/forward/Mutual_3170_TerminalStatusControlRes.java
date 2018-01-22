package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 外设控制应答151021
 */
@LocationCommand(id = "3170")
public class Mutual_3170_TerminalStatusControlRes extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        return packet;
    }
}
