package com.navinfo.opentsp.platform.da.core.configuration;

import com.alibaba.fastjson.JSON;
import com.mongodb.WriteResult;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.SpecialCanMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.SpecialCanMongodbServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatisticData;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author: ChenJie
 * @date 2017/11/8
 * 0F38 can周期上报数据kafka消费类
 */
@Component
public class KafkaConsumerListener0F38 implements MessageListener<Object, Object> {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerListener0F38.class);

    @Value("${opentsp.0f38.kafka.to.mongodb.batch.size:10}")
    private int mongoBatchSize;

    SpecialCanMongodbService specialCanMongodbService = new SpecialCanMongodbServiceImpl();

    @Override
    public void onMessage(ConsumerRecord<Object, Object> consumerRecord) {
        try{
            String topic = consumerRecord.topic();
            String key =(String)consumerRecord.key();
            String value = (String)consumerRecord.value();
            logger.info("KafkaConsumerListener0F38 consume topic:{},key:{},value:{}.",topic,key,value);
            LCTerminalStatisticData.StatisticData statisticData = getTerminalStatisticData(value);
            //转存mongo
            if(statisticData != null){
                WriteResult result = specialCanMongodbService.saveTerminalStatisticData(key,statisticData);
                logger.info("save 0F38 [{}] to mongodb result:{}.",statisticData,result);
            }
        }catch(Exception e){
            logger.error("0F38 data consumed from kafka and save to mongodb error: ",e);
        }
    }

    /**
     * pb转java
     * */
    private LCTerminalStatisticData.StatisticData getTerminalStatisticData(String kafkaData) {
        LCTerminalStatisticData.StatisticData statisticData = null;
        try{
            KafkaCommand kafkaCommand = JSON.parseObject(kafkaData, KafkaCommand.class);
            if(kafkaCommand != null){
                statisticData = LCTerminalStatisticData.StatisticData.parseFrom(kafkaCommand.getMessage());
            }
        }
        catch(Exception e){
            logger.error("kafka 0F38 parse to LCTerminalStatisticDataPB error:",e);
        }
        return statisticData;
    }

}


