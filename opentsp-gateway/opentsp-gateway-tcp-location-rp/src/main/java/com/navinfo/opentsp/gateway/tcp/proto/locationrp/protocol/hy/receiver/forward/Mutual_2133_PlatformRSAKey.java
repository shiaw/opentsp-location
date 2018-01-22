package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 平台RSA公钥
 */
@LocationCommand(id = "2133")
public class Mutual_2133_PlatformRSAKey extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        this.writeToTerminal(packet);
        return null;
    }

}
