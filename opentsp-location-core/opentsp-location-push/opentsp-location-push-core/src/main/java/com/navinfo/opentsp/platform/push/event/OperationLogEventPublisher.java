//package com.navinfo.opentsp.platform.push.event;
//
//import org.jboss.netty.channel.MessageEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.context.ApplicationEventPublisherAware;
//
///**
// * @author wanliang
// * @version 1.0
// * @date 2016/9/22
// * @modify
// * @copyright opentsp
// */
//public class OperationLogEventPublisher implements ApplicationEventPublisherAware {
//
//    private static final Logger logger = LoggerFactory.getLogger(OperationLogEventPublisher.class);
//    private ApplicationEventPublisher publisher;
//
//    @Override
//    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
//        this.publisher = applicationEventPublisher;
//    }
//
//    @Async
//    public void asyncSendMessage(String message) {
//        MessageEvent event = new MessageEvent(this, message);
//        logger.debug(new StringBuilder("Send Async Event").toString());
//        publisher.publishEvent(event);
//        logger.debug(new StringBuilder("Async Event Done").toString());
//    }
//}
