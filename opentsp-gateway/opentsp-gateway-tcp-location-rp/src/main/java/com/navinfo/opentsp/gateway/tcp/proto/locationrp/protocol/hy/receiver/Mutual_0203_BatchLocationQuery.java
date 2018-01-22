package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.NewestGpsDataCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.LvsConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.hy.RPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.platform.LCBatchLocationQuery.BatchLocationQuery;
import com.navinfo.opentsp.platform.location.protocol.platform.LCBatchLocationQueryRes.BatchLocationQueryRes;
import com.navinfo.opentsp.platform.location.protocol.platform.LCLocationObject.LocationObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * 批量位置查询
 */
@LocationCommand(id = "0203")
public class Mutual_0203_BatchLocationQuery extends RPCommand {

    @Autowired
    private LvsConfiguration configuration;

    @Override
    public Packet processor(NettyClientConnection connection, Packet packet) {
        try {
            BatchLocationQuery batchLocationQuery = BatchLocationQuery.parseFrom(packet.getContent());
            List<Long> terminals = batchLocationQuery.getTerminalIdentifyList();
            BatchLocationQueryRes.Builder builder = BatchLocationQueryRes.newBuilder();
            builder.setSerialNumber(packet.getSerialNumber());
            for (Long terminal : terminals) {
                LCLocationData.LocationData locationData = NewestGpsDataCache.getNewsetGps(terminal);
                LocationObject.Builder objectBuilder = LocationObject.newBuilder();
                objectBuilder.setTerminalIdentify(terminal);
                if (locationData != null)
                    objectBuilder.setLocationData(locationData);
                builder.addLocationObject(objectBuilder);
            }

            Packet _out_packet = new Packet(true);
            _out_packet.setCommand(LCAllCommands.AllCommands.Platform.BatchLocationQueryRes_VALUE);
            _out_packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
            _out_packet.setFrom(Integer.parseInt(configuration.getNodeCode()));
            _out_packet.setTo(packet.getFrom());
            _out_packet.setUniqueMark(packet.getUniqueMark());
            _out_packet.setContent(builder.build().toByteArray());
            return _out_packet;
        } catch (InvalidProtocolBufferException e) {
        }
        return null;
    }
}
