package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRPulseCollectionRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 查询终端属性应答
 */
@DPAnno(id = "3134")
public class DP_3134_QueryTerminalPropertyRes extends PushCommand {

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
            LCTRPulseCollectionRes.TRPulseCollectionRes pulseCollectionRes = LCTRPulseCollectionRes.TRPulseCollectionRes.parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(),
                    LCAllCommands.AllCommands.Terminal.QueryTerminalProperty_VALUE,
                    pulseCollectionRes.getSerialNumber(), false);
            //packet.setTo(upperUniqueMark);
            packet.setUpperUniqueMark(upperUniqueMark);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3134", e);
        }
    }
}
