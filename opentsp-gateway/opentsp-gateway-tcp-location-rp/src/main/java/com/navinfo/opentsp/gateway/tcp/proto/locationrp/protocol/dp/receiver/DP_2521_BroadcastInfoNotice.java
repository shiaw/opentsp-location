package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;

/**
 * 广播信息通知150914
 */
@LocationCommand(id = "2521")
public class DP_2521_BroadcastInfoNotice extends RPCommand {

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        //SubscribeEntry subscribeEntry = TerminalManage.getInstance().getSubscribe(packet.getUniqueMark());
        //if (subscribeEntry != null) {
           /* List<ClientConnection> subscribe = subscribeEntry.getSubscribeSession();
            if (subscribe != null) {
                for (ClientConnection conn : subscribe) {
                    conn.send(packet);
                }
            }*/
        //}

        super.writeToUpper(packet);

        return super.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(),
                LCAllCommands.AllCommands.Platform.DataUnsubscribe_VALUE,
                LCPlatformResponseResult.PlatformResponseResult.success, packet.getFrom());
    }
}
