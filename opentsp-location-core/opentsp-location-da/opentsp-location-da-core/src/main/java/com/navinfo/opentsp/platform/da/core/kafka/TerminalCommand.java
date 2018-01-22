package com.navinfo.opentsp.platform.da.core.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.platform.location.kit.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//@LocationCommand(id = "0200", version = "200110")
@Component
public class TerminalCommand {

    public static Logger log = LoggerFactory.getLogger(TerminalCommand.class);

    @Autowired
    private void test() {
        System.out.println("aaa");
    }

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    /**
     * 发送kafka数据到DP
     * @param packet
     * @param topicName
     */
	public void writeKafKaToDP(Packet packet,String topicName){
        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
//            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
//            kafkaCommand.setCommandId(packet.getCommandForHex());
//            kafkaCommand.setTopic(topicName);
//            kafkaCommand.setKey(packet.getUniqueMark());
//            kafkaMessageChannel.send(kafkaCommand);

            System.out.println("***********************************************s");

            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
//            packet.setCommandId(3002);
            packet.setContent("hehe".getBytes());
            kafkaCommand.setTopic(topicName);
            kafkaMessageChannel.send(kafkaCommand);

            System.out.println("***********************************************e");

        } catch (JsonProcessingException e) {
            log.error("序列化出错!{}",kafkaCommand,e);
        }
	}

}
