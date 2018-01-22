package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver.travelrecorder;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRSpeedCollectionRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 采集速度应答
 */
@DPAnno(id = "3056")
public class DP_3056_TRSpeedCollectionRes extends PushCommand {

    @Autowired
    private UpperOplogCache cache;

    @Override
    public void handle(ResultNoticeCommand command) {
        Packet packet = new Packet();
        packet.setContent(command.getMessage());
        packet.setUniqueMark(command.getDevice());
        packet.setCommand(command.getCommand());
        packet.setProtocol(command.getProtocol());
        packet.setSerialNumber(command.getSerialNumber());
        try {
            LCTRSpeedCollectionRes.TRSpeedCollectionRes speedCollectionRes = LCTRSpeedCollectionRes.TRSpeedCollectionRes
                    .parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(), LCAllCommands.AllCommands.Terminal.TRSpeedCollect_VALUE,
                    speedCollectionRes.getSerialNumber(), true);
            packet.setUpperUniqueMark(upperUniqueMark);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3056", e);
        }
    }
}
