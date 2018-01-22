package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver.forward;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCVehiclePassInArea.VehiclePassInArea;
import org.springframework.beans.factory.annotation.Autowired;

import static com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;

/**
 * 区域车次统计151008
 */
@LocationCommand(id = "2505")
public class Mutual_2505_VehiclePassInArea extends RPCommand {

    @Autowired
    private LvsConfiguration configuration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            VehiclePassInArea vehiclePassInArea = VehiclePassInArea.parseFrom(packet.getContent());
            Packet _packet_saveDA = new Packet(true);
            _packet_saveDA.setProtocol(LCConstant.LCMessageType.PLATFORM);
//			_packet_saveDA.setUniqueMark(packet.getUniqueMark());
            _packet_saveDA.setUniqueMark("000000" + configuration.getNodeCode());
            _packet_saveDA.setSerialNumber(packet.getSerialNumber());
            _packet_saveDA.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _packet_saveDA.setCommand(AllCommands.Terminal.VehiclePassInArea_VALUE);
            _packet_saveDA.setContent(vehiclePassInArea.toByteArray());
            this.broadcastCommon(_packet_saveDA, null);
        } catch (InvalidProtocolBufferException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        return null;
    }
}
