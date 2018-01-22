package com.navinfo.opentsp.gateway.tcp.proto.locationrp.common.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

//@Component
public class RecoverAnswerCache {
    private final static Logger log = LoggerFactory.getLogger(RecoverAnswerCache.class);


    @Scheduled(cron = "5/9 * * * * ? ")
    public void run() {
        try {
            log.info("加载超时缓存回收任务");
        } catch (Exception e) {
        }
    }


}
