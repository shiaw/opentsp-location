package com.navinfo.opentsp.platform.rprest.kafka;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalOnlineSwitch;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 终端上下线状态汇报 kafka消费
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3007")
@Component
public class TerminalOnlineSwitch3007Handler extends AbstractKafkaCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(TerminalOnlineSwitch3007Handler.class);

    protected TerminalOnlineSwitch3007Handler() {
        super(KafkaCommand.class);
    }



    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
         LCTerminalOnlineSwitch.TerminalOnlineSwitch terminalOnlineSwitch= LCTerminalOnlineSwitch.TerminalOnlineSwitch.parseFrom(packet.getContent());
//        if (terminalOnlineSwitch.getStatus()){
//            log.info("终端{}上线",kafkaCommand.getKey());
//        }else{
//            log.info("终端{}下线",kafkaCommand.getKey());
//        }
        } catch (Exception e) {
            log.error("3007", e);
        }
    }
}
