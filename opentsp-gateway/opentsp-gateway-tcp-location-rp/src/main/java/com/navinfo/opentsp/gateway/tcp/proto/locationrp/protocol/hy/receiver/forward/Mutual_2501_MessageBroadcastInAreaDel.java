package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.ServerCommonResCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCMessageBroadcastInAreaDel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 区域播报信息删除150914
 */
@LocationCommand(id = "2501")
public class Mutual_2501_MessageBroadcastInAreaDel extends RPCommand {

    @Autowired
    private LvsConfiguration configuration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            //通用应答缓存
            ServerCommonResCache.getInstance().addServerCommonResCache("_" + packet.getSerialNumber(), packet);

            LCMessageBroadcastInAreaDel.MessageBroadcastInAreaDel mbad = LCMessageBroadcastInAreaDel.MessageBroadcastInAreaDel.parseFrom(packet.getContent());
            LCMessageBroadcastInAreaDel.MessageBroadcastInAreaDel.Builder mbadBuilder = LCMessageBroadcastInAreaDel.MessageBroadcastInAreaDel.newBuilder();

            mbadBuilder.addAreaIdentifys(mbad.getAreaIdentifys(0));
            mbadBuilder.setSaveSign(false);//设置保存数据库标示为 false

            Packet _packet = new Packet(true);
            _packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet.setUniqueMark("000000" + configuration.getNodeCode());
            _packet.setSerialNumber(packet.getSerialNumber());
            _packet.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet.setCommand(LCAllCommands.AllCommands.Terminal.MessageBroadcastInAreaDel_VALUE);
            _packet.setContent(mbadBuilder.build().toByteArray());

            mbadBuilder.setSaveSign(true);//设置保存数据库标示为 false
            Packet _packet_saveDA = new Packet(true);
            _packet_saveDA.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet_saveDA.setUniqueMark("000000" + configuration.getNodeCode());
            _packet_saveDA.setSerialNumber(packet.getSerialNumber());
            _packet_saveDA.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet_saveDA.setCommand(LCAllCommands.AllCommands.Terminal.MessageBroadcastInAreaDel_VALUE);
            _packet_saveDA.setContent(mbadBuilder.build().toByteArray());

            super.broadcastCommon(_packet_saveDA, _packet);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }
        return null;
    }
}
