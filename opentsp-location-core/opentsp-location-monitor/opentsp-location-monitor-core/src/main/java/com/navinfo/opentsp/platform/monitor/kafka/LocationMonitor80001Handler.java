package com.navinfo.opentsp.platform.monitor.kafka;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.location.kit.LocationMonitor;
import com.navinfo.opentsp.platform.monitor.cache.LocationMonitorCache;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyue on 2017/6/14.
 */
//@KafkaConsumerHandler(topic = "location-monitor",commandId = "80001")
@KafkaConsumerHandler(topic = "location-monitor",commandId = "80001")
@Component
public class LocationMonitor80001Handler  extends AbstractKafkaCommandHandler {
    private static final Logger logger = LoggerFactory.getLogger(LocationMonitor80001Handler.class);

    protected LocationMonitor80001Handler() {
        super(KafkaCommand.class);
    }
    @Override
    public void handle(KafkaCommand kafkaCommand) {
        logger.info("消费location-monitor80001数据");
        try {
            LocationMonitor locationMonitor = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(),LocationMonitor.class);
            LocationMonitorCache.getInstance().addCache(locationMonitor.getModuleName(),locationMonitor);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("消费location-monitor80001数据出现异常！");
        }
    }
}
