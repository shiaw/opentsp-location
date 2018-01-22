package com.navinfo.opentsp.platform.da.core.configuration;

import com.alibaba.fastjson.JSON;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.platform.da.core.common.ConcentratedData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.ConcentratedDataRedisImpl;
import com.navinfo.opentsp.platform.location.kit.Convert;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author: ChenJie
 * @date 2017/10/28
 * 密集采集数据kafka消费类
 */
@Component
public class KafkaConsumerListener0F37 implements MessageListener<Object, Object> {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerListener0F37.class);

    @Autowired
    protected KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${opentsp.concentrated.kafka.to.redis.batch.size:1000}")
    private int redisBatchSize;

    private ConcentratedDataRedisImpl concentratedDataRedis = new ConcentratedDataRedisImpl();

    /**
     * 计数器
     * */
    private ThreadLocal<Integer> count = new ThreadLocal<Integer>() {
        @Override
        public Integer initialValue() {
            return 0;
        }
    };

    /**
     * 队列，缓存kafka消费出的数据
     * */
    private ThreadLocal<ConcurrentLinkedQueue<ConcentratedData>> queue = new ThreadLocal<ConcurrentLinkedQueue<ConcentratedData>>(){
        @Override
        public ConcurrentLinkedQueue<ConcentratedData> initialValue() {
            return new ConcurrentLinkedQueue();
        }
    };


    @Override
    public void onMessage(ConsumerRecord<Object, Object> consumerRecord) {
        try{
            String topic = consumerRecord.topic();
            String key =(String)consumerRecord.key();
            String value = (String)consumerRecord.value();
            logger.info("消费到数据topic:{},key:{},value:{}.",topic,key,value);
            ConcentratedData concentratedData = getConcentratedData(key,value);
            if(concentratedData != null){
                //保存到本线程队列中
                queue.get().offer(concentratedData);
                count.set(count.get()+1);
            }

            //达到阈值转存redis
            if(count.get() >= redisBatchSize){
                logger.info("转存redis"+count.get());
                //kafka数据 转存到 redis
                concentratedDataRedis.zaddPipeline(queue.get());
                count.set(0);
            }
        }catch(Exception e){
            logger.error("Concentrated data consumed from kafka and save to redis error: ",e);
            e.printStackTrace();
        }
    }

    private ConcentratedData getConcentratedData(String terminalId,String kafkaData) {
        ConcentratedData concentratedData = null;
        try{
            KafkaCommand kafkaCommand = JSON.parseObject(kafkaData, KafkaCommand.class);
            if(kafkaCommand != null){
                concentratedData = new ConcentratedData();
                concentratedData.setTerminalId(terminalId);
                concentratedData.setScore(Double.parseDouble(kafkaCommand.getSerialNumber()+""));
                concentratedData.setData(Convert.bytesToHexString(kafkaCommand.getMessage()));
            }
        }
        catch(Exception e){
            logger.error("kafka密集数据转换缓存对象异常:",e);
        }
        return concentratedData;
    }

}


