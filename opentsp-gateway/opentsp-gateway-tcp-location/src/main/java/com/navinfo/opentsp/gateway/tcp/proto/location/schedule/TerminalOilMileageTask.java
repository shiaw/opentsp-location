package com.navinfo.opentsp.gateway.tcp.proto.location.schedule;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.LastMileageOilTypeCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TerminalOilMileageTask {

    private static final Logger log = LoggerFactory.getLogger(TerminalOilMileageTask.class);

    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;
    @Scheduled(cron = "${opentsp.refresh.oilMileaty.schedule.cron:0 0/2 *  * * ?}")
    public void refreshOilMileageType(){
        lastMileageOilTypeCache.loadOilMileageType();
        log.info("刷新终端里程、油耗标记..");
    }

}
