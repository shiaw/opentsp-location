package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.ServerCommonResCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;

/**
 * 出区域限速删除150513
 */
@LocationCommand(id = "2318")
public class Mutual_2318_OutRegionToLimitSpeedDel extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        //通用应答缓存
        ServerCommonResCache.getInstance().addServerCommonResCache(packet.getUniqueMark() + "_" + packet.getSerialNumber(), packet);
        //super.writeToTerminal(packet);
        super.writeMQToDP(packet);
        return null;
    }

}
