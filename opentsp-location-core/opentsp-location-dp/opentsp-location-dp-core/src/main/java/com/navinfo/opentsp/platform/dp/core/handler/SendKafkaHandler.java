package com.navinfo.opentsp.platform.dp.core.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.platform.dp.core.cache.DPMonitorCacheHolder;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * User: zhanhk
 * Date: 16/9/20
 * Time: 上午11:21
 */
@Component
public class SendKafkaHandler {

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Value("${location.rp.topic:rpposdone}")
    private String rpTopic;

    @Value("${location.da.topic:daposdone}")
    private String daTopic;

    public static Logger log = LoggerFactory.getLogger(SendKafkaHandler.class);

    /**
     * 发送kafka数据到RP
     * @param packet
     */
    public void writeKafKaToRP(Packet packet){
        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(rpTopic);
            kafkaCommand.setKey(packet.getUniqueMark());
            kafkaMessageChannel.send(kafkaCommand);
            log.info("kafka send to rp success !{}",packet.toString());
            if(DPMonitorCacheHolder.getDPMonitorCache() != null) {
                DPMonitorCacheHolder.getDPMonitorCache().addRPCount();
            }
        } catch (JsonProcessingException e) {
            log.error("序列化出错!{}",kafkaCommand,e);
        }
    }

    /**
     * 发送kafka数据到DA
     * @param packet
     */
    public void writeKafKaToDA(Packet packet){
        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(daTopic);
            kafkaCommand.setKey(packet.getUniqueMark());
            kafkaMessageChannel.send(kafkaCommand);
            log.info("kafka send to da success !{}",packet.toString());
            if(DPMonitorCacheHolder.getDPMonitorCache() != null) {
                DPMonitorCacheHolder.getDPMonitorCache().addDACount();
            }
        } catch (JsonProcessingException e) {
            log.error("序列化出错!{}",kafkaCommand,e);
        }
    }
}
