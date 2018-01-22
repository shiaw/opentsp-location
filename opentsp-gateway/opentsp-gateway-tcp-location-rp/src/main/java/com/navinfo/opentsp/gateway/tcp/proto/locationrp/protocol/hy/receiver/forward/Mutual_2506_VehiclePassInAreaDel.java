package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCVehiclePassInAreaDel.VehiclePassInAreaDel;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 区域车次统计删除151008
 */
@LocationCommand(id = "2506")
public class Mutual_2506_VehiclePassInAreaDel extends RPCommand {


    @Autowired
    private LvsConfiguration configuration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {

        try {
            VehiclePassInAreaDel vehiclePassInAreaDel = VehiclePassInAreaDel.parseFrom(packet.getContent());
            Packet _packet_saveDA = new Packet(true);
            _packet_saveDA.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _packet_saveDA.setUniqueMark("000000" + configuration.getNodeCode());
            _packet_saveDA.setSerialNumber(packet.getSerialNumber());
            _packet_saveDA.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet_saveDA.setCommand(LCAllCommands.AllCommands.Terminal.VehiclePassInAreaDel_VALUE);
            _packet_saveDA.setContent(vehiclePassInAreaDel.toByteArray());


            this.broadcastCommon(_packet_saveDA, null);
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
