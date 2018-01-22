package com.navinfo.opentsp.platform.push.configuration;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.platform.push.impl.MessageScheduleEventCommandProducer;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for part which translate event from core event bus to rabbit
 */
@Configuration
public class EventCommandProducerConfiguration {

    @Bean
    MessageScheduleEventCommandProducer messageScheduleEventCommandProducer(ObjectFactory<MessageChannel> messageChannel) {
        return new MessageScheduleEventCommandProducer(messageChannel);
    }


}
