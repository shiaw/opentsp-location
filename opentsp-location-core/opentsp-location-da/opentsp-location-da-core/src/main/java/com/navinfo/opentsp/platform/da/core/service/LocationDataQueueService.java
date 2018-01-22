package com.navinfo.opentsp.platform.da.core.service;

import com.navinfo.opentsp.platform.da.core.common.DaemonThreadFactory;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsDataEntry;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by Administrator on 2017/11/24.
 * @author gx
 */
public class LocationDataQueueService {

    private int batchNum;
    private int consumerNum;
    private BlockingQueue<TempGpsDataEntry> queue;
    private static ExecutorService executorService;

    private static ILocationRedisService iLocationRedisService = new LocationRedisServiceImpl();

    public LocationDataQueueService(int consumerNum, BlockingQueue<TempGpsDataEntry> queue, int batchNum){
        this.consumerNum = consumerNum;
        this.queue = queue;
        this.batchNum = batchNum;
        executorService = new ThreadPoolExecutor(
                this.consumerNum, 20, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1000), new DaemonThreadFactory("locationDataSave"), new ThreadPoolExecutor.DiscardPolicy());
    }

    /**
     * 提交任务到线程池
     */
    public void start(){
        for(int i=0; i < consumerNum;i++){
            executorService.execute(new Consumer(queue, batchNum));
        }
    }

    /**
     * 消息队列消费者，用来消费队列中的数据，达到1000后存入到redis中
     */
    private static class Consumer implements Runnable {
        private List<TempGpsDataEntry> dataList = new ArrayList<TempGpsDataEntry>();
        private BlockingQueue<TempGpsDataEntry> queue;
        private static Logger logger = LoggerFactory.getLogger(Consumer.class);
        private int batchNum;
        private Consumer(BlockingQueue<TempGpsDataEntry> queue, int batchNum){
            this.queue = queue;
            this.batchNum = batchNum;
        }

        @Override
        public void run() {
            logger.error("======================={} start job=======================", Thread.currentThread().getName());
                while(true){
                    try {
                        TempGpsDataEntry tempGpsDataEntry = queue.take();
                        dataList.add(tempGpsDataEntry);
                        if(dataList.size()  >= batchNum){
                            iLocationRedisService.savaNormalLocation(null, dataList);
                            TempGpsData.saveLastestLcToStaticRedis(dataList);
                            dataList.clear();
                        }
                    } catch (InterruptedException e) {
                        logger.error("", e);
                    }
            }
        }
    }

    /**
     * 异步储存任务，将数据存储到redis中
     */
    private static class SaveTask implements Runnable {
        public SaveTask(List<TempGpsDataEntry> list){
            this.list = list;
        }
        private List<TempGpsDataEntry> list;

        @Override
        public void run() {
            TempGpsData.saveLastestLcToStaticRedis(list);
        }
    }
}
