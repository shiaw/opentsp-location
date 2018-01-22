package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.aop.scope.ScopedProxyFactoryBean;
import org.springframework.stereotype.Component;

/**
 * 定位数据批量上传
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3011")
@Component
public class DP_3011_BatchLocationDataUpload extends DPCommand {

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
//            super.broadcastToUpper(packet);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3011", e);
        }
    }
}
