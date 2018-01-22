package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataQueryRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * CAN数据查询应答151118
 */
@DPAnno(id = "3135")
public class DP_3135_CANBUSDataQueryRes extends PushCommand {

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
            LCCANBUSDataQueryRes.CANBUSDataQueryRes cANBUSDataQueryRes = LCCANBUSDataQueryRes.CANBUSDataQueryRes
                    .parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(),
                    LCAllCommands.AllCommands.Terminal.CANBUSDataQuery_VALUE,
                    cANBUSDataQueryRes.getSerialNumber(), false);
            //packet.setTo(upperUniqueMark);
            packet.setUpperUniqueMark(upperUniqueMark);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3135", e);
        }
    }
}
