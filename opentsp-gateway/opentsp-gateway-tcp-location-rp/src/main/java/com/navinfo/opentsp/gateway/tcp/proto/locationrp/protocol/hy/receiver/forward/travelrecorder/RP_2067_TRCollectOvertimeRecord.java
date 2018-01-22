package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward.travelrecorder;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 采集指定的参数修改记录
 */
@LocationCommand(id = "2067")
public class RP_2067_TRCollectOvertimeRecord extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        super.writeToTerminal(packet);
        return null;
    }

}
