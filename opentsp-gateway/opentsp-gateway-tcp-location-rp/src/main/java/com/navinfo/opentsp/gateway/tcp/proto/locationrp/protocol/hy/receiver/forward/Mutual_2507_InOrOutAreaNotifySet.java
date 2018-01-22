package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * @author Lenovo
 * @date 2016-12-14
 * @modify
 * @copyright
 */
@LocationCommand(id = "2507")
public class Mutual_2507_InOrOutAreaNotifySet extends RPCommand {


    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        this.writeMQToDP(packet);
        return null;
    }
}
