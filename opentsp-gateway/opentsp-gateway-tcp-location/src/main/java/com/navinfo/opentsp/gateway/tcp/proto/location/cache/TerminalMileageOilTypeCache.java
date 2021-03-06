package com.navinfo.opentsp.gateway.tcp.proto.location.cache;


import com.google.common.cache.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilTypeEntry;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author wanliang
 * @version 1.0
 * @date 2017/06/19
 * @modify缓存里程油耗类型
 * @copyright opentsp
 */
@Component
public class TerminalMileageOilTypeCache {

    private static final Logger logger = LoggerFactory.getLogger(TerminalMileageOilTypeCache.class);

    private static final int expireTime = 7;

    /*周期性线程池*/
//    private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
    private final ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(1,
            new BasicThreadFactory.Builder().namingPattern("terminal-mileage-pool-%d").daemon(true).build());

    private final Cache<String, TerminalMileageOilTypeEntry> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(expireTime, TimeUnit.DAYS)//设置写缓存后3秒钟过期
            .maximumSize(50_0000).removalListener(RemovalListeners.asynchronous(new RemovalListener<Object, TerminalMileageOilTypeEntry>() {
                @Override
                public void onRemoval(RemovalNotification<Object, TerminalMileageOilTypeEntry> notification) {
                    logger.info(notification.getKey() + " was removed, cause is " + notification.getCause());
                    //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                }
            }, executorService))
            /*removalListener(new RemovalListener<Object, CommandModel>() {
                @Override
                public void onRemoval(RemovalNotification<Object, CommandModel> notification) {
                    System.out.println(notification.getKey() + " was removed, cause is " + notification.getCause());
                }
            })*/
            .build();

    public void add(String id, TerminalMileageOilTypeEntry deviceCommand) {
        cache.put(id, deviceCommand);
        //被观察者
    }

    public TerminalMileageOilTypeEntry get(String id) {
        return cache.getIfPresent(id);
    }

    public void remove(String id) {
        cache.invalidate(id);
    }

    public long getSize(){
        return cache.size();
    }

    @PostConstruct
    protected void executeCleanCache() {

		/*参数说明
         * 执行的线程任务
		 * 初始化延迟1秒则后执行
		 * 每隔1秒钟执行一次清除,本次执行结束后延迟1秒钟开始下次执行
		 * 时间单位(秒)
		 * */
        executorService.scheduleWithFixedDelay(new ClearUpExpired(), 2000L, 100L, TimeUnit.MILLISECONDS);
    }
    /**
     * @Description: 任务执行完毕之后, 关闭调度线程池
     */
    protected void shutdown() {
        executorService.shutdown();
    }

    /**
     * @Description: 立即关闭所有正在执行的线程
     */
    protected void shutdownNow() {
        executorService.shutdownNow();
    }

    /**
     * 清除过期的缓存元素
     */
    private class ClearUpExpired implements Runnable {

        @Override
        public void run() {
            /*try {
                Thread.sleep(10000000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            //System.out.println(System.currentTimeMillis());
            cache.cleanUp();
        }

    }


}
