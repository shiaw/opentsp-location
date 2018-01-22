package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.reveive;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.config.FilterCodeConfig;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.util.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 位置数据汇报 kafka消费
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3002")
@Component
public class DP_3002_LocationData extends BaseDPCommand {

    public static Logger logger = LoggerFactory.getLogger(DP_3002_LocationData.class);

    @Value("${topic.name.808:poscan.raw.808}")
    private String topicName;

    private static KafkaMessageChannel instance;

    public static KafkaMessageChannel getInstance() {
        if (instance == null) {
            instance = (KafkaMessageChannel)SpringContextUtil.getBean("kafkaMessageChannel");
        }
        return instance;
    }

    @Autowired
    private FilterCodeConfig filterCodeConfig;

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        try {
            List<String> filterCodeList = filterCodeConfig.getFilterCodeList();
            if (filterCodeList.isEmpty() || filterCodeList.contains(kafkaCommand.getCommandId())) {
                //写kafka 0200 0704
                Packet packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
                kafkaCommand.setMessage(packet.getContent());
                kafkaCommand.setCommandId(packet.getCommandForHex());
                kafkaCommand.setTopic(topicName);
                kafkaCommand.setKey(packet.getUniqueMark());
                if(instance == null) {
                    getInstance();
                }
                instance.send(kafkaCommand);
            }
        } catch (Exception e) {
            logger.error("发送kafka出错", e);
        }
    }
}
