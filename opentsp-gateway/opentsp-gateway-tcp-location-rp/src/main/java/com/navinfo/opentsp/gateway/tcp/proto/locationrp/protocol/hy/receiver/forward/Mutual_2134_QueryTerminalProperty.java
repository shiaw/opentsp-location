package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 查询终端属性
 */
@LocationCommand(id = "2134")
public class Mutual_2134_QueryTerminalProperty extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        this.writeToTerminal(packet);
        return null;
    }

}
