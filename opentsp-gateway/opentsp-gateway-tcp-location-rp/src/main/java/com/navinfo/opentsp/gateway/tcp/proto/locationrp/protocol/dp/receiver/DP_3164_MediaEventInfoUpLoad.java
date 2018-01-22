package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.stereotype.Component;

/**
 * 多媒体事件信息上传
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3164")
@Component
public class DP_3164_MediaEventInfoUpLoad extends DPCommand {

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
//            super.broadcastToUpper(packet);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3164", e);
        }
    }
}
