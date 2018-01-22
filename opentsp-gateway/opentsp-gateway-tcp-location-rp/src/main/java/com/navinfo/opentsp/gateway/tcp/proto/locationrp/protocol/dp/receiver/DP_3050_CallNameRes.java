package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCCallNameRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 位置查询应答
 */
@DPAnno(id = "3050")
public class DP_3050_CallNameRes extends PushCommand {

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
            LCCallNameRes.CallNameRes callNameRes = LCCallNameRes.CallNameRes.parseFrom(packet.getContent());
            String upper = cache.getUpper(packet.getUniqueMark(),
                    LCAllCommands.AllCommands.Terminal.CallName_VALUE,
                    callNameRes.getSerialNumber(), false);
            //packet.setTo(upper);
            packet.setUpperUniqueMark(upper);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3050", e);
        }
    }
}
