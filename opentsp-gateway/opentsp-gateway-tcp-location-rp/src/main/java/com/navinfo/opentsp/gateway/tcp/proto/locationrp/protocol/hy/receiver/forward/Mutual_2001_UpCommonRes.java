package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 上行消息的通用应答
 */
@LocationCommand(id = "2001")
public class Mutual_2001_UpCommonRes extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        //super.writeToTerminal(packet);
        return null;
    }

}
