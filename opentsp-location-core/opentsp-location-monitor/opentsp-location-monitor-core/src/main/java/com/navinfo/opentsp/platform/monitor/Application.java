package com.navinfo.opentsp.platform.monitor;

import com.navinfo.opentsp.common.messaging.transport.amqp.RabbitMQ;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.monitor.common.SpringContextUtil;
import com.navinfo.opentsp.platform.monitor.configuration.CommandListenerConfiguration;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Service launcher for the User module which contains all the necessary configuration
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({CommandChannelConfiguration.class, CommandListenerConfiguration.class, EurekaClient.class, KafkaConfig.class})
@RabbitMQ
@EnableScheduling
public class Application {
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(true);
        application.run(args);
    }


}
