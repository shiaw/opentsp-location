package com.navinfo.opentsp.platform.push.impl;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Config fo delivery processor part.
 */
@Configuration
@EnableConfigurationProperties(DeliveryProcessConfigurationBean.class)
@ComponentScan(basePackageClasses = DeliveryProcessor.class)
public class DeliveryProcessorConfiguration {

}
