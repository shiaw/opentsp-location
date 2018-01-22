package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 终端信息广播开关150914
 */
@LocationCommand(id = "2520")
public class Mutual_2520_TerminalBroadcastSwitch extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        super.writeMQToDP(packet);
        return null;
    }
}
