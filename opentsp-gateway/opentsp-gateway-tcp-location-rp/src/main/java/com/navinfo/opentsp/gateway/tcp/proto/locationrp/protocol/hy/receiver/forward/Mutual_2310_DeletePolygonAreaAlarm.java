package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 删除多边形区域
 */
@LocationCommand(id = "2310")
public class Mutual_2310_DeletePolygonAreaAlarm extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        //super.writeToTerminal(packet);
        super.writeMQToDP(packet);
        return null;
    }

}
