package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 调度短信
 */
@LocationCommand(id = "2151")
public class Mutual_2151_DispatchMessage extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        //this.writeToTerminal(packet);
        super.writeMQToDP(packet);
        return null;
    }
}
