package com.navinfo.opentsp.gateway.tcp.proto.location;

import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.gateway.tcp.config.CommandHandlerConfiguration;
import com.navinfo.opentsp.gateway.tcp.config.TspServerConfiguration;
import com.navinfo.opentsp.gateway.tcp.server.ServersManager;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.configuration.SecurityApplicationConfiguration;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

//@SpringBootApplication(exclude = MybatisAutoConfiguration.class)
@SpringBootApplication
@EnableAutoConfiguration
@EnableEurekaClient
@MapperScan(basePackages = "com.navinfo.opentsp.gateway.tcp.proto.location.mapper")
@Import({
        AmqpConfiguration.class,
        CommandChannelConfiguration.class,
        SecurityApplicationConfiguration.class,
        KafkaConfig.class,
        EurekaClient.class,
        TspServerConfiguration.class,
        CommandHandlerConfiguration.class,
        ServersManager.class
})

public class Application {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.setWebEnvironment(false);
        application.run(args);
    }

//    @Bean
//    public MessageChannel messageChanel() {
//        return Mockito.mock(MessageChannel.class);
//    }
}
