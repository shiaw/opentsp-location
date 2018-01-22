package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.UpperOplogCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPAnno;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.PushCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.LCParameterQueryRes;
import com.navinfo.opentsp.platform.push.ResultNoticeCommand;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 参数查询应答
 */
@DPAnno(id = "3302")
public class DP_3302_ParameterQueryRes extends PushCommand {

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
            LCParameterQueryRes.ParameterQueryRes answerQuestion = LCParameterQueryRes.ParameterQueryRes
                    .parseFrom(packet.getContent());
            String upperUniqueMark = cache.getUpper(
                    packet.getUniqueMark(),
                    LCAllCommands.AllCommands.Terminal.ParameterQuery_VALUE,
                    answerQuestion.getSerialNumber(), false);
            if (upperUniqueMark == null) {
                upperUniqueMark = cache.getUpper(
                        packet.getUniqueMark(),
                        LCAllCommands.AllCommands.Terminal.QueryAppointPara_VALUE,
                        answerQuestion.getSerialNumber(), false);
                if (upperUniqueMark == null) {
                    logger.error("未找到应答业务系统.");
                }
            }
            //packet.setTo(upperUniqueMark);
            packet.setUpperUniqueMark(upperUniqueMark);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3302", e);
        }
    }
}
