package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import com.google.common.cache.*;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 业务系统操作终端记录缓存<br>
 * 提供给RP收到终端应答后,寻找业务系统<br>
 * <br>
 * key:终端标识_指令号_流水号<br>
 * value:业务系统标识
 *
 * @author xubh
 */
@Component
public class UpperOplogCache {

    private static final Logger logger = LoggerFactory.getLogger(UpperOplogCache.class);

    @Value("${upper.oplog.cache.expireTime:180}")
    private int expireTime;

    @Autowired
    private MessageChannel messageChannel;

    /*周期性线程池*/
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private Cache<String, String> cache = null;

    public void add(String key, String value) {
        cache.put(key, value);
        //被观察者
    }

    public void addOplog(String terminal, int command, int serialNumber, String upperMark) {
        String key = terminal + "_" + command + "_" + serialNumber;
        add(key, upperMark);
    }

    /**
     * @param terminal
     * @param command
     * @param serialNumber
     * @param optType      ture:更新; false：删除
     * @return
     */
    public String getUpper(String terminal, int command, int serialNumber, boolean optType) {
        String key = terminal + "_" + command + "_" + serialNumber;
        String value = get(key);
        if (value == null) {
            return null;
        }
        if (optType) {
            cache.put(key, value);
        } else {
            remove(key);
        }
        return value;
    }

    public String get(String id) {
        return cache.getIfPresent(id);
    }

    public void remove(String id) {
        cache.invalidate(id);
    }

    public long getSize() {
        return cache.size();
    }

    @PostConstruct
    protected void executeCleanCache() {

        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireTime, TimeUnit.SECONDS)//设置写缓存后3秒钟过期
                .maximumSize(10000).removalListener(RemovalListeners.asynchronous(new RemovalListener<String, String>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, String> notification) {
                        logger.info("key=" + notification.getKey() + " value=" + notification.getValue() + " was removed, cause is " + notification.getCause());
                        //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                       /* if (notification.getCause().name().equals("EXPIRED")) {
                            DownStatusCommand downCommand = new DownStatusCommand();
                            if (notification.getValue() != null) {
                                downCommand.setId(notification.getKey());
                            }
                            downCommand.setState(DownCommandState.W_TIMEOUT);
                            messageChannel.send(downCommand);
                        }*/
                    }
                }, executorService)).build();

		/*参数说明
         * 执行的线程任务
		 * 初始化延迟1秒则后执行
		 * 每隔1秒钟执行一次清除,本次执行结束后延迟1秒钟开始下次执行
		 * 时间单位(秒)
		 * */
        executorService.scheduleWithFixedDelay(new ClearUpExpired(), 1000L, 100L, TimeUnit.MILLISECONDS);
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
