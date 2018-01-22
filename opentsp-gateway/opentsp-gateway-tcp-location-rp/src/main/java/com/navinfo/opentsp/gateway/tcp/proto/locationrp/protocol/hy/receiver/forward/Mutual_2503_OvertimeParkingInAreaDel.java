package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.ServerCommonResCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOvertimeParkingInAreaDel.OvertimeParkingInAreaDel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 区域滞留超时删除160428
 */
@LocationCommand(id = "2503")
public class Mutual_2503_OvertimeParkingInAreaDel extends RPCommand {

    @Autowired
    private LvsConfiguration configuration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            //通用应答缓存
            ServerCommonResCache.getInstance().addServerCommonResCache("_" + packet.getSerialNumber(), packet);

            OvertimeParkingInAreaDel opaDel = OvertimeParkingInAreaDel.parseFrom(packet.getContent());
            OvertimeParkingInAreaDel.Builder opaDelBuilder = OvertimeParkingInAreaDel.newBuilder();

            opaDelBuilder.addAreaIdentifys(opaDel.getAreaIdentifys(0));
            opaDelBuilder.setSaveSign(false);
            Packet _packet = new Packet(true);
            _packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet.setUniqueMark("000000" + configuration.getNodeCode());
            _packet.setSerialNumber(packet.getSerialNumber());
            _packet.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet.setCommand(AllCommands.Terminal.OvertimeParkingInAreaDel_VALUE);
            _packet.setContent(opaDelBuilder.build().toByteArray());

            opaDelBuilder.setSaveSign(true);
            Packet _packet_saveDA = new Packet(true);
            _packet_saveDA.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet_saveDA.setUniqueMark("000000" + configuration.getNodeCode());
            _packet_saveDA.setSerialNumber(packet.getSerialNumber());
            _packet_saveDA.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet_saveDA.setCommand(AllCommands.Terminal.OvertimeParkingInAreaDel_VALUE);
            _packet_saveDA.setContent(opaDelBuilder.build().toByteArray());
            super.broadcastCommon(_packet_saveDA, _packet);
        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
