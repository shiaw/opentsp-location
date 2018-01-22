package com.navinfo.tasktracker.nilocation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author zhangyue
 */
@Component
public class ApplicationStartup implements CommandLineRunner {
    protected static final Logger logger = LoggerFactory.getLogger(ApplicationStartup.class);

    @Override
    public void run(String... args) throws Exception {
        logger.info(">>>>>>>>>>>>>>>task data start  <<<<<<<<<<<<<");
        logger.info(">>>>>>>>>>>>>>>task data end  <<<<<<<<<<<<<" );
    }
}
