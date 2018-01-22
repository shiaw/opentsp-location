package com.navinfo.opentsp.platform.location;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentsp.platform.location.kafka.Mutual_0980_LocationDataSave_Kafka;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import com.navinfo.opentspcore.common.kafka.KafkaConsumerListener;
import com.navinfo.opentspcore.common.kafka.KafkaDefaultHandler;
import com.navinfo.opentspcore.common.kafka.KafkaHandlerDispatcher;
import de.codecentric.boot.admin.config.EnableAdminServer;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.convert.converter.Converter;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;


@Configuration
@EnableAutoConfiguration
@ComponentScan
@Import({
        KafkaConsumerListener.class,
        KafkaHandlerDispatcher.class,
        KafkaDefaultHandler.class,
        KafkaConfig.class,
})
public class Application {

    @Bean
    public MessageChannel messageChanel() {
        return Mockito.mock(MessageChannel.class);
    }


    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

