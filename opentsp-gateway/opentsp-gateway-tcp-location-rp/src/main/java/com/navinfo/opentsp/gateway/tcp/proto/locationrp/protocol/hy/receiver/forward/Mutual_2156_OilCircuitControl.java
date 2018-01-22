package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 油路/电路控制
 */
@LocationCommand(id = "2156")
public class Mutual_2156_OilCircuitControl extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        //super.writeToTerminal(packet);
        super.writeMQToDP(packet);
        return null;
    }

}
