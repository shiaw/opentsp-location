package com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka;

import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.config.TspServerConfiguration;
import com.navinfo.opentsp.gateway.tcp.proto.zhpacket2kafka.util.SpringContextUtil;
import com.navinfo.opentspcore.common.kafka.KafkaConfig;
import com.navinfo.opentspcore.common.kafka.KafkaConsumerListener;
import com.navinfo.opentspcore.common.kafka.KafkaDefaultHandler;
import com.navinfo.opentspcore.common.kafka.KafkaHandlerDispatcher;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@Import({
        TspServerConfiguration.class,
        KafkaConsumerListener.class,
        KafkaHandlerDispatcher.class,
        KafkaDefaultHandler.class,
        KafkaConfig.class
})
public class Application {

    @Bean
    public MessageChannel messageChanel() {
        return Mockito.mock(MessageChannel.class);
    }

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
