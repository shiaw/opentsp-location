package com.navinfo.opentsp.platform.dp.core.acceptor.ta.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.KafkaConsumerHandler;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.dp.core.cache.DPMonitorCache;
import com.navinfo.opentsp.platform.dp.core.cache.DPMonitorCacheHolder;
import com.navinfo.opentsp.platform.dp.core.common.entity.GpsLocationDataEntity;
import com.navinfo.opentsp.platform.dp.core.rule.RegularHandler;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCBatchLocationDataUpload;
import com.navinfo.opentspcore.common.handler.AbstractKafkaCommandHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 位置数据处理
 *
 * @author zhanhk
 */
@KafkaConsumerHandler(topic = "posraw", commandId = "3011")
@Component
public class TA_3011_BatchLocationDataUpload extends AbstractKafkaCommandHandler {

    private static final Logger log = LoggerFactory.getLogger(TA_3011_BatchLocationDataUpload.class);

    protected TA_3011_BatchLocationDataUpload() {
        super(KafkaCommand.class);
    }

    @Autowired
    public RegularHandler regularHandler;

    //	@Value("{dp.location.queueSize:100}")
//	private static int queueSize;
//
    @Value("${dp.location.threadCount:5}")
    private int threadCount;

    /**
     * 点缓存队列
     */
    public static BlockingQueue<GpsLocationDataEntity> queue = new LinkedBlockingQueue<>(500);

    /**
     * 消费执行线程池
     */
    static ExecutorService executorServicePool = Executors.newCachedThreadPool();

    /**
     * 线程监控缓存对象数组
     */
    private DPMonitorCache[] dPMonitorCacheArr;

    public DPMonitorCache[] getdPMonitorCacheArr() {
        return dPMonitorCacheArr;
    }

    public void setdPMonitorCacheArr(DPMonitorCache[] dPMonitorCacheArr) {
        this.dPMonitorCacheArr = dPMonitorCacheArr;
    }

    /**
     * 初始化消费者线程池
     */
    @PostConstruct
    public void statConsumer() {
        dPMonitorCacheArr = new DPMonitorCache[threadCount];
        for (int i = 0; i < threadCount; i++) {
            DPMonitorCache dpMonitorCache = new DPMonitorCache();
            dPMonitorCacheArr[i] = dpMonitorCache;
            executorServicePool.execute(new Consumer(queue, regularHandler, dpMonitorCache));
        }
    }

    @Override
    public void handle(KafkaCommand kafkaCommand) {
        Packet packet;
        try {
            packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(), Packet.class);
            log.info("TA--0704 --> DP SUCCESS!{}", packet.toString());
            LCBatchLocationDataUpload.BatchLocationDataUpload batch = LCBatchLocationDataUpload.BatchLocationDataUpload.parseFrom(packet.getContent());
            List<LCLocationData.LocationData> locationDatas = batch.getDatasList();
            for (LCLocationData.LocationData locationData : locationDatas) {

                GpsLocationDataEntity dataEntity = new GpsLocationDataEntity(locationData, packet.getUniqueMark(), packet.getSerialNumber(), AllCommands.Terminal.ReportLocationData_VALUE);
                //进入规则运算
                try {
                    if (!queue.offer(dataEntity)) {
                        log.debug("放入数据失败：" + dataEntity);
                    }
                    log.debug("缓存队列:" + queue.size());
                    dataEntity = null;
                } catch (NullPointerException e) {
                    log.error("放入队列异常", e);
                }
            }
        } catch (Exception e) {
            log.error("数据格式错误!", e);
        }
    }

    /**
     * 消费者线程
     */
    public static class Consumer implements Runnable {
        private BlockingQueue<GpsLocationDataEntity> queue;

        private RegularHandler regularHandler;

        private DPMonitorCache dpMonitorCache;

        public Consumer(BlockingQueue<GpsLocationDataEntity> queue, RegularHandler regularHandler, DPMonitorCache dpMonitorCache) {
            this.queue = queue;
            this.regularHandler = regularHandler;
            this.dpMonitorCache = dpMonitorCache;
        }

        public void run() {
            String threadName = Thread.currentThread().getName();
            dpMonitorCache.setName(threadName);
            //ThreadLocal缓存对应监控统计对象
            DPMonitorCacheHolder.setDPMonitorCache(dpMonitorCache);
            boolean isRunning = true;
            while (isRunning) {
                try {
                    long s = System.currentTimeMillis();
                    GpsLocationDataEntity dataEntity = queue.poll();
                    long end = System.currentTimeMillis() - s;
                    if (end > 200)
                        log.error("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!获取queue的时间为：" + end);
                    if (null != dataEntity) {
                        dpMonitorCache.addDPCount();
                        s = System.currentTimeMillis();
                        //进入规则运算
                        regularHandler.handler(dataEntity);
                        long e = System.currentTimeMillis() - s;
                        //记录统计信息
                        if (!dataEntity.getDetailMap().isEmpty()) {
                            dpMonitorCache.setMaxTime(e, dataEntity.getDetailMap(),
                                    dataEntity.getProcessFilterTime(), dataEntity.getRuleProcessTime(),
                                    dataEntity.getRuleSignTime(), dataEntity.getEncryptTime(),
                                    dataEntity.getForwardTime());
                            dpMonitorCache.setTid(dataEntity.getTerminalId());
                        }
                        log.info(threadName + "-" + dataEntity.getUniqueMark() + "_" + dataEntity.getGpsDate() + ",待处理队列:" + queue.size() + ",规则处理总时间:" + e + "ms");
                        dataEntity = null;
                    } else {
                        //队列为空，等待2s
                        Thread.sleep(2000);
                        log.info(threadName + "waitting ...");
                    }
                } catch (Exception e) {
                    dpMonitorCache.decDPCount();
                    log.error("DP 3002规则链处理错误,{}", e);
                } finally {
                    log.debug("退出消费者线程！" + threadName);
                }
            }
        }
    }
}
