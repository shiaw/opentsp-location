package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 驾驶行为设置151118
 */
@LocationCommand(id = "2270")
public class Mutual_2270_DrivingBehaviorSetting extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        this.writeToTerminal(packet);
        return null;
    }
}
