package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver.travelrecorder;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.travelrecorder.LCTRPulseCollectionRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 采集记录仪唯一性编号应答
 */
@DPAnno(id = "3065")
public class DP_3065_TRCollectOnlyCodeRes extends PushCommand {
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
            LCTRPulseCollectionRes.TRPulseCollectionRes pulseCollectionRes = LCTRPulseCollectionRes.TRPulseCollectionRes
                    .parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(),
                    LCAllCommands.AllCommands.Terminal.TRCollectOnlyCode_VALUE,
                    pulseCollectionRes.getSerialNumber(), false);
            packet.setUpperUniqueMark(upperUniqueMark);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3065", e);
        }
    }
}
