package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import com.google.common.cache.*;
import com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache.entry.AnswerEntry;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 指令缓存<br>
 * <li>指令超时</li> <li>通道寻找</li> <br>
 * 缓存key=数据包唯一标识_流水号<br>
 * packet.getUniqueMark() + &quot;_&quot; + packet.getSerialNumber()
 *
 * @author xubh
 */
@Component
public class AnswerCommandCache {

    private static final Logger logger = LoggerFactory.getLogger(AnswerCommandCache.class);

    @Value("${answer.command.cache.expireTime:30}")
    private int expireTime;

    /*周期性线程池*/
    private ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private Cache<String, AnswerEntry> cache = null;

    /**
     * 添加一个指令信息到缓存<br>
     *
     * @param answerEntry
     */
    public void addEntry(String bizChannel, int serialNumber, AnswerEntry answerEntry) {
        answerEntry.setUniqueMark(bizChannel);
        cache.put(bizChannel + "_" + serialNumber, answerEntry);
    }

    /**
     * 获取一个指令信息
     *
     * @param bizChannel   {@link String} 数据包唯一标识
     * @param serialNumber {@link Integer} 流水号
     * @param isRemove     {@link Boolean}是否删除缓存中消息
     * @return
     */
    public AnswerEntry getAnswerEntry(String bizChannel, int serialNumber, boolean isRemove) {
        if (isRemove) {
            this.remove(bizChannel, serialNumber);
            return null;
        } else {
            return cache.getIfPresent(bizChannel + "_" + serialNumber);
        }
    }

    /**
     * 移除缓存中一个指令消息
     *
     * @param bizChannel
     * @param serialNumber
     * @return
     */
    public void remove(String bizChannel, int serialNumber) {
        cache.invalidate(bizChannel + "_" + serialNumber);
    }

    public long getSize() {
        return cache.size();
    }

    @PostConstruct
    protected void executeCleanCache() {

        cache = CacheBuilder.newBuilder().expireAfterWrite(expireTime, TimeUnit.SECONDS)//设置写缓存后3秒钟过期
                .maximumSize(10000).removalListener(RemovalListeners.asynchronous(new RemovalListener<String, AnswerEntry>() {
                    @Override
                    public void onRemoval(RemovalNotification<String, AnswerEntry> notification) {
                        logger.info(notification.getKey() + " was removed, cause is " + notification.getCause());
                        //如果是正常移除不操作，如果是过期移除操作redis更新状态。
                        /*if (notification.getCause().name().equals("EXPIRED")) {
                            AnswerEntry answerEntry = notification.getValue();
                            if (answerEntry != null) {
                                Packet packet = new Packet(true);
                                if (commands.contains(answerEntry.getInternalCommand())) {
                                    packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
                                    packet.setUniqueMark(answerEntry.getUniqueMark());

                                    switch (answerEntry.getInternalCommand()) {
                                        case LCAllCommands.AllCommands.Platform.ServerLogin_VALUE:
                                            LCServerLoginRes.ServerLoginRes.Builder builder = LCServerLoginRes.ServerLoginRes.newBuilder();
                                            builder.setResults(LCPlatformResponseResult.PlatformResponseResult.requestTimeOut);
                                            builder.setSerialNumber(answerEntry.getSerialNumber());
                                            packet.setCommand(LCAllCommands.AllCommands.Platform.ServerLoginRes_VALUE);
                                            packet.setContent(builder.build().toByteArray());
                                        default:
                                            break;
                                    }
                                } else {
                                    LCServerCommonRes.ServerCommonRes.Builder builder = LCServerCommonRes.ServerCommonRes.newBuilder();
                                    builder.setSerialNumber(answerEntry.getSerialNumber());
                                    builder.setResponseId(answerEntry.getInternalCommand());
                                    builder.setResults(LCPlatformResponseResult.PlatformResponseResult.requestTimeOut);

                                    packet.setCommand(LCAllCommands.AllCommands.Platform.ServerCommonRes_VALUE);
                                    packet.setUniqueMark(answerEntry.getUniqueMark());
                                    packet.setProtocol(LCConstant.LCMessageType.PLATFORM);
                                    packet.setContent(builder.build().toByteArray());
                                }
                            }
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

    private static Set<Integer> commands = new HashSet<Integer>();

    static {
        commands.add(LCAllCommands.AllCommands.Platform.ServerLogin_VALUE);
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
            cache.cleanUp();
        }

    }
}
