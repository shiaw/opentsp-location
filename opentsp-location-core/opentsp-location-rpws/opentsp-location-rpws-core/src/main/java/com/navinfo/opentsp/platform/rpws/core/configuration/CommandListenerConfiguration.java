package com.navinfo.opentsp.platform.rpws.core.configuration;

import com.navinfo.opentsp.common.messaging.transport.amqp.RabbitMQ;
import com.navinfo.opentsp.platform.configuration.AbstractCommandListenerConfiguration;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentspcore.common.CoreCommandHandler;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Configuration for the user module which contains all the necessary for working with command queue via RabbitMQ
 */
@Configuration
@ComponentScan(basePackageClasses = {CoreCommandHandler.class})
@Import({SecurityApplicationConfiguration.class})
@RabbitMQ
public class CommandListenerConfiguration extends AbstractCommandListenerConfiguration {
    @Bean
    @Qualifier(value = "command")
    @RabbitMQ
    public Queue commandQueue(RabbitAdmin rabbitAdmin) {
        Queue queue = new Queue("operate-rprest", false, false, false);
        rabbitAdmin.declareQueue(queue);
        return queue;
    }

}
