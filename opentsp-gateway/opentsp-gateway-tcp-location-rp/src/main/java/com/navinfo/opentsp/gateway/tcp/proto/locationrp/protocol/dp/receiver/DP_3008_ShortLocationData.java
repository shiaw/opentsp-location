package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.stereotype.Component;

/**
 * 精简位置数据汇报 kafka消费
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3008")
@Component
public class DP_3008_ShortLocationData extends DPCommand {


    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet = null;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3008", e);
        }
    }
}
