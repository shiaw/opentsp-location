package com.navinfo.opentsp.gateway.tcp.proto.locationrp;

import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.gateway.tcp.config.CommandHandlerConfiguration;
import com.navinfo.opentsp.gateway.tcp.config.TspServerConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableAutoConfiguration
@EnableScheduling
@Import({
        TspServerConfiguration.class,
        AmqpConfiguration.class,
        CommandChannelConfiguration.class,
        SecurityApplicationConfiguration.class,
        KafkaConfig.class,
        CommandHandlerConfiguration.class,
        EurekaClient.class
})
public class Application {
    public static void main(String[] args) {
//        SpringApplication application = new SpringApplication(com.navinfo.opentsp.gateway.tcp.Application.class);
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(false);
        application.run(args);
    }
}
