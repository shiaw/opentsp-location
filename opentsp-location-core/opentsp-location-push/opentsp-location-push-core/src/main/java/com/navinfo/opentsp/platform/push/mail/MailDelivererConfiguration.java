package com.navinfo.opentsp.platform.push.mail;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 */
@Configuration
@EnableConfigurationProperties(MailDelivererConfigBean.class)
public class MailDelivererConfiguration {
}
