package com.navinfo.opentsp.tools.parse;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Lenovo
 * @date 2016-11-16
 * @modify
 * @copyright
 */
@ComponentScan
@EnableAutoConfiguration
@EnableScheduling
@Configuration
public class Application {

    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(Application.class);
        app.run(args);
    }
}
