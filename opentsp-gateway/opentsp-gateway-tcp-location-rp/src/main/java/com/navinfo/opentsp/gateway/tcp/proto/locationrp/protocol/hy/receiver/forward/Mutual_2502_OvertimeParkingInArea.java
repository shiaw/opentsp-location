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
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCOvertimeParkingInArea.OvertimeParkingInArea;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 区域滞留超时160428
 */
@LocationCommand(id = "2502")
public class Mutual_2502_OvertimeParkingInArea extends RPCommand {

    @Autowired
    private LvsConfiguration configuration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {

        try {
            //通用应答缓存
            ServerCommonResCache.getInstance().addServerCommonResCache("_" + packet.getSerialNumber(), packet);

            OvertimeParkingInArea opa = OvertimeParkingInArea.parseFrom(packet.getContent());
            OvertimeParkingInArea.Builder opaBuilder = OvertimeParkingInArea.newBuilder();

            opaBuilder.setAreaInfo(opa.getAreaInfo());
            opaBuilder.setParkingTime(opa.getParkingTime());

            opaBuilder.setSaveSign(false);//
            Packet _packet = new Packet(true);
            _packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet.setUniqueMark("000000" + configuration.getNodeCode());
            _packet.setSerialNumber(packet.getSerialNumber());
            _packet.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet.setCommand(AllCommands.Terminal.OvertimeParkingInArea_VALUE);
            _packet.setContent(opaBuilder.build().toByteArray());


            opaBuilder.setSaveSign(true);//
            Packet _packet_saveDA = new Packet(true);
            _packet_saveDA.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet_saveDA.setUniqueMark("000000" + configuration.getNodeCode());
            _packet_saveDA.setSerialNumber(packet.getSerialNumber());
            _packet_saveDA.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet_saveDA.setCommand(AllCommands.Terminal.OvertimeParkingInArea_VALUE);
            _packet_saveDA.setContent(opaBuilder.build().toByteArray());

            super.broadcastCommon(_packet_saveDA, _packet);

        } catch (InvalidProtocolBufferException e) {
            e.printStackTrace();
        }

        return null;
    }
}
