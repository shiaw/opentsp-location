package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.stereotype.Component;

/**
 * 终端鉴权
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3004")
@Component
public class DP_3004_TerminalAuth extends DPCommand {

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3004", e);
        }
    }
}
