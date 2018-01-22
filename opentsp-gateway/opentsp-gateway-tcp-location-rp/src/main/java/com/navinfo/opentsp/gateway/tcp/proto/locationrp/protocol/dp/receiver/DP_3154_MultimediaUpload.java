package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.MultimediaOperateQueue;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.springframework.stereotype.Component;

/**
 * 多媒体数据上传
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3154")
@Component
public class DP_3154_MultimediaUpload extends DPCommand {

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
            //从多媒体操作列表当中,获取对此终端最先操作的业务系统标识
            String upper = MultimediaOperateQueue.getMultimediaOperateUpper(packet.getUniqueMark());
            //packet.setTo(upper);
            packet.setUpperUniqueMark(upper);
            if (upper == null) {
//                super.broadcastToUpper(packet);

            }
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("3154", e);
        }
    }
}
