package com.navinfo.opentsp.gateway.tcp.proto.location.handler;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * User: zhanhk
 * Date: 16/8/2
 * Time: 上午10:42
 */
@Component
public class FlowMonitorHandler implements InitializingBean{

//    @Autowired
//    private SchedulerAPIService service;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${notice.notify.period.interval:1}")
    private Integer interval;

    @Value("${notice.repeat.count.interval:0}")
    private Integer repeatCount;

    public static final String FLOWTYPE_UP = "FLOWTYPE_UP";

    public static final String FLOWTYPE_DOWN = "FLOWTYPE_DOWN";

    /**
     * 添加上行流量
     * @param key deviceId
     * @param value
     */
    public void addUpFlow(String key, int value) {
        redisTemplate.opsForValue().increment(FLOWTYPE_UP + key, value);
    }

    /**
     * 添加下行流量
     * @param key deviceId
     * @param value
     */
    public void addDownFlow(String key, int value) {
        redisTemplate.opsForValue().increment(FLOWTYPE_DOWN + key, value);
    }



//    @Scheduled(fixedDelayString = "${command.fetch.delay:2000}", initialDelayString = "${command.fetch.init.delay:0}")
//    public void gatherInformation() throws InterruptedException {
//        System.out.println("fuck"+System.currentTimeMillis());
//    }
//
    @Override
    public void afterPropertiesSet() throws Exception {
//        this.gatherInformation();
//        service.newCronSchedulerBuilder()
//                .startTime(null)
//                .endTime(null)
//                .description("FlowMonitorHandler")
//                .cronEx("0/2 * * * * ?")    //every seconds
//                .command(command)
//                .buildAndSend();
    }
}
