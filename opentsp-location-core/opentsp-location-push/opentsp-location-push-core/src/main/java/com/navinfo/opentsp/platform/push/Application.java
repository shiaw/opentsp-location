package com.navinfo.opentsp.platform.push;

import com.navinfo.opentsp.common.messaging.transport.amqp.RabbitMQ;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.push.configuration.CommandListenerConfiguration;
import com.navinfo.opentspcore.common.core.web.CoreWebConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * Service launcher for the User module which contains all the necessary configuration
 */
@Configuration
@ComponentScan
//@EnableAspectJAutoProxy,CatAopService.class
@EnableAutoConfiguration
@Import(value = {CommandChannelConfiguration.class, CommandListenerConfiguration.class, EurekaClient.class, CoreWebConfig.class})
@RabbitMQ
@EnableAsync
public class Application {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(true);
        application.run(args);
    }

}
