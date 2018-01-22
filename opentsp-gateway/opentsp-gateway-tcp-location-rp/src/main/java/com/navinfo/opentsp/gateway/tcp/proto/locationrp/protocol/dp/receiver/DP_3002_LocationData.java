package com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.NewestGpsDataCache;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.protocol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 位置数据汇报 kafka消费
 */
@KafkaConsumerHandler(topic = "rpposdone", commandId = "3002")
@Component
public class DP_3002_LocationData extends DPCommand {

    public static Logger logger = LoggerFactory.getLogger(DP_3002_LocationData.class);

    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        //kafkaCommand为消息对象，可直接获，进行对应的业务处理
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
            LCLocationData.LocationData locationData = LCLocationData.LocationData.parseFrom(packet.getContent());
            //NodeHelper.DP3002Num++;
            NewestGpsDataCache.addNewsetGps(packet.getFrom(), locationData);
            super.writeToUpper(packet);
        } catch (Exception e) {
            logger.error("位置数据转换Protobuf异常.", e);
        }
    }
}
