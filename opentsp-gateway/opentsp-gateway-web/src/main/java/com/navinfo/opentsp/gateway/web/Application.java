package com.navinfo.opentsp.gateway.web;

import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.AmqpConfiguration;
import com.navinfo.opentsp.common.messaging.transport.amqp.configuration.CommandChannelConfiguration;
import com.navinfo.opentsp.gateway.balancer.web.proxy.common.BalancerConfiguration;
import com.navinfo.opentsp.gateway.web.config.FrontendTokenServiceConfiguration;
import com.navinfo.opentsp.gateway.web.config.SecurityConfig;
import com.navinfo.opentsp.gateway.web.proxy.ProxyCommandMapper;
import com.navinfo.opentsp.platform.configuration.EurekaClient;
import com.navinfo.opentsp.platform.configuration.RedisConfigurationFactory;
import com.navinfo.opentsp.platform.fs.frontend.FileStorageFrontendConfiguration;
import com.navinfo.opentspcore.common.cloud.service.AmqpConnectionFactoryConfig;
import com.navinfo.opentspcore.common.log.DiagnosticInfo;
import com.navinfo.opentspcore.common.log.LogConfiguration;
import com.navinfo.opentspcore.common.security.token.TokenValidatorConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackageClasses = {FrontendTokenServiceConfiguration.class, TokenValidatorConfiguration.class,
        RedisConfigurationFactory.class, ProxyCommandMapper.class,
        AmqpConnectionFactoryConfig.class, DiagnosticInfo.class})
@Import({ AmqpConfiguration.class, CommandChannelConfiguration.class,
        SecurityConfig.class, EurekaClient.class, FileStorageFrontendConfiguration.class,
        BalancerConfiguration.class, LogConfiguration.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

}
