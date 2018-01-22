package com.navinfo.opentsp.gateway.tcp.proto.location.schedule;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalLastTimeCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalOnlineSwitch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/9/12
 * @modify 下发业务指令，对超时指令进行相应的处理
 * @copyright opentsp
 */
@Component
@Configurable
@EnableScheduling
public class TerminalLastTimeTask {



    private static final Logger log = LoggerFactory.getLogger(TerminalLastTimeTask.class);

    private final MessageChannel messageChannel;

    private final KafkaMessageChannel kafkaMessageChannel;

    @Value("${opentsp.location.close.batch.offline:false}")
    private boolean isBatchOffline;

    @Autowired
    public TerminalLastTimeTask(MessageChannel messageChannel, KafkaMessageChannel kafkaMessageChannel) {
        this.messageChannel = messageChannel;
        this.kafkaMessageChannel=kafkaMessageChannel;
    }

    @Scheduled(cron = "${opentsp.lastTime.offline.schedule.cron:0 0/2 *  * * ?}")
    public void reportCurrentTime() {
        if(!isBatchOffline){
            return;
        }
        log.info("运行扫描终端最后上报时间缓存");
        List<String> terminalIds = TerminalLastTimeCache.getInstance().getTimeoutTerminals();
        if (terminalIds != null) {
            for (String terminalId : terminalIds) {
                log.info("运行扫描终端最后上报时间缓存--通知dp,{}下线",terminalId);
                //通知dp
                LCTerminalOnlineSwitch.TerminalOnlineSwitch.Builder builder = LCTerminalOnlineSwitch.TerminalOnlineSwitch.newBuilder();
                builder.setSwitchDate(System.currentTimeMillis() / 1000);
                builder.setStatus(false);
                Packet pk = new Packet(true);
                pk.setCommand(LCAllCommands.AllCommands.Terminal.TerminalOnlineSwitch_VALUE);
                pk.setProtocol(LCConstant.LCMessageType.PLATFORM);
                pk.setUniqueMark(terminalId);
                pk.setFrom(0L);
                pk.setContent(builder.build().toByteArray());

                KafkaCommand kafkaCommand = new KafkaCommand();
                try {
                    kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(pk));
                    kafkaCommand.setCommandId(pk.getCommandForHex());
                    kafkaCommand.setTopic(TopicConstants.POSRAW);
                    kafkaCommand.setKey(terminalId);
                    kafkaMessageChannel.send(kafkaCommand);
                } catch (JsonProcessingException e) {
                    log.error("序列化出错!{}", kafkaCommand, e);
                }
            }
        }

    }

}
