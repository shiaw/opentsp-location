package com.navinfo.opentsp.platform.monitor.scheduler;

import com.navinfo.opentsp.platform.location.kit.LocationMonitor;
import com.navinfo.opentsp.platform.monitor.cache.LocationMonitorCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by zhangyue on 2017/6/22.
 */
@Component
@Configurable
@EnableScheduling
public class MonitorTask {

    private static Logger logger = LoggerFactory.getLogger(MonitorTask.class);

    @Value("${opentsp.updateMonitorCache.timeInterval:1}")
    private Integer timeInterval;

    /*
        定时检查一次统计的缓存数据（若缓存中某项key的value值超过预定时间没更新，则将其清空）
     */
    @Scheduled(cron = "${opentsp.updateMonitorCache.schedule.cron:0 0/1 * * * ?}")
    public void updateMonitorCache() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            long currentTime = System.currentTimeMillis();
            logger.info("==============================================");
            logger.info("开始检查统计缓存数据:{}", sdf.format(new Date(currentTime)));
            Map<String,LocationMonitor> cache = LocationMonitorCache.getInstance().getCache();
            for (Map.Entry<String, LocationMonitor> entry : cache.entrySet()) {
                LocationMonitor locationMonitor = entry.getValue();
                long interval = currentTime - locationMonitor.getCurrentTime();
                if( interval > timeInterval*1000*60){ //间隔太长没更新
                    logger.info("清空{}统计缓存数据，最后统计时间为{}",entry.getKey(),sdf.format(new Date(locationMonitor.getCurrentTime())));
                    //清空key
                    cache.remove(entry.getKey());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("检查统计缓存数据报错");
        }
    }
}
