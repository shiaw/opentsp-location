package com.navinfo.opentsp.platform.da.core.kafka;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.da.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRecieveDateSave;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by xubh on 2017/4/6.
 */
@KafkaConsumerHandler(topic = "daposdone",commandId = "0988")
@Component
public class Mutual_0988_TerminalRecieveDateSave extends AbstractKafkaCommandHandler {

    private static TerminalOnOffStatusServiceImpl redisTerminalOnOffService;

    private static final Logger log = LoggerFactory.getLogger(Mutual_0988_TerminalRecieveDateSave.class);

    protected Mutual_0988_TerminalRecieveDateSave() {
        super(KafkaCommand.class);
    }

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        try {
            if(redisTerminalOnOffService == null) {
                redisTerminalOnOffService = (TerminalOnOffStatusServiceImpl) SpringContextUtil.getBean("TerminalOnOffStatusServiceImpl");
            }
            Packet packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(),Packet.class);
            LCTerminalRecieveDateSave.TerminalRecieveDateSave data = LCTerminalRecieveDateSave.TerminalRecieveDateSave.parseFrom(packet.getContent());
            long time = redisTerminalOnOffService.getTerminalRecieveDate(data.getTerminalId());
            if(time==0l){
                redisTerminalOnOffService.addData(String.valueOf(data.getTerminalId()), data.getRecieveDate());
            }else {
                //说明缓存中已经存在,不处理
            }
        } catch (Exception e) {
            log.error("终端首次连接数据格式错误!",e);
        }
    }
}
