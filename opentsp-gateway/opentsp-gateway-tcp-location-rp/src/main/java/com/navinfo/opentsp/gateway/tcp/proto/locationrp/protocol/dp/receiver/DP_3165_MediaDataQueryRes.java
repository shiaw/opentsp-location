package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaDataQueryRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 存储多媒体数据检索应答
 */
@DPAnno(id = "3165")
public class DP_3165_MediaDataQueryRes extends PushCommand {

    @Autowired
    private UpperOplogCache cache;

    @Autowired
    private MessageChannel messageChannel;

    @Override
    public void handle(ResultNoticeCommand command) {
        Packet packet = new Packet();
        packet.setContent(command.getMessage());
        packet.setUniqueMark(command.getDevice());
        packet.setCommand(command.getCommand());
        packet.setProtocol(command.getProtocol());
        packet.setSerialNumber(command.getSerialNumber());
        try {
            LCMediaDataQueryRes.MediaDataQueryRes mediaDataQueryRes = LCMediaDataQueryRes.MediaDataQueryRes
                    .parseFrom(packet.getContent());
            String upper = cache.getUpper(packet.getUniqueMark(),
                    LCAllCommands.AllCommands.Terminal.MediaDataQuery_VALUE,
                    mediaDataQueryRes.getSerialNumber(), true);
            packet.setUpperUniqueMark(upper);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3164", e);
        }
    }
}
