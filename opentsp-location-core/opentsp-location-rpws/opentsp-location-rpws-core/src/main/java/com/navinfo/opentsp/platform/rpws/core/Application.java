package com.navinfo.opentsp.platform.rpws.core;

import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentsp.platform.rpws.core.common.SpringContextUtil;
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
@Import({SecurityApplicationConfiguration.class, AmqpConfiguration.class, CommandChannelConfiguration.class, EurekaClient.class})
public class Application {
    @Bean
    public SpringContextUtil springContextUtil() {
        return new SpringContextUtil();
    }
    public static void main(String[] args) {
        System.setProperty("org.apache.cxf.stax.maxChildElements","150000");
        SpringApplication.run(Application.class, args);
    }

}
