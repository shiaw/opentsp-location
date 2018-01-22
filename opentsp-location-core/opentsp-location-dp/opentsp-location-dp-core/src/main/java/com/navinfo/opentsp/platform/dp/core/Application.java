package com.navinfo.opentsp.platform.dp.core;

import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentsp.platform.dp.core.common.SpringContextUtil;
import com.navinfo.opentsp.platform.dp.core.configuration.CommandListenerConfiguration;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import com.navinfo.opentspcore.common.log.LogConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Service launcher for the User module which contains all the necessary configuration
 */
@Configuration
@ComponentScan
@EnableAutoConfiguration
@Import({ AmqpConfiguration.class,
        CommandChannelConfiguration.class,
        CommandListenerConfiguration.class,
        SecurityApplicationConfiguration.class,
        EurekaClient.class,
        LogConfiguration.class,
        KafkaConfig.class})
public class Application {

    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
