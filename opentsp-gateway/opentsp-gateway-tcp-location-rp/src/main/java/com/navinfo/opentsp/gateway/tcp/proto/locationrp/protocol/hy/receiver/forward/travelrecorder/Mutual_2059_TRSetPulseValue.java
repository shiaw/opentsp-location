package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward.travelrecorder;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 采集累计行驶里程
 */
@LocationCommand(id = "2059")
public class Mutual_2059_TRSetPulseValue extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        super.writeToTerminal(packet);
        return null;
    }

}
