package com.navinfo.opentsp.platform.da.core.configuration;

import com.alibaba.fastjson.JSON;
import com.mongodb.WriteResult;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.SpecialCanMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.SpecialCanMongodbServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCFaultInfo;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author: ChenJie
 * @date 2017/11/9
 * 0F39 故障信息上报数据kafka消费类
 */
@Component
public class KafkaConsumerListener0F39 implements MessageListener<Object, Object> {
    private static Logger logger = LoggerFactory.getLogger(KafkaConsumerListener0F39.class);

    @Value("${opentsp.0f39.kafka.to.mongodb.batch.size:10}")
    private int mongoBatchSize;

    SpecialCanMongodbService specialCanMongodbService = new SpecialCanMongodbServiceImpl();

    @Override
    public void onMessage(ConsumerRecord<Object, Object> consumerRecord) {
        try{
            String topic = consumerRecord.topic();
            String key =(String)consumerRecord.key();
            String value = (String)consumerRecord.value();
            logger.info("KafkaConsumerListener0F39 consume topic:{},key:{},value:{}.",topic,key,value);
            LCFaultInfo.FaultInfo faultData = getFaultData(value);
            //转存mongo
            if(faultData != null){
                WriteResult result = specialCanMongodbService.saveFaultData(key,faultData);
                logger.info("save faultInfo 0F39 [{}] to mongodb result:{}.",faultData,result);
            }
        }catch(Exception e){
            logger.error("0F39 data consumed from kafka and save to mongodb error: ",e);
        }
    }

    /**
     * pb转换
     * @param kafkaData
     * @return LCFaultInfo.FaultInfo
     * */
    private LCFaultInfo.FaultInfo getFaultData(String kafkaData) {
        LCFaultInfo.FaultInfo faultInfo = null;
        try{
            KafkaCommand kafkaCommand = JSON.parseObject(kafkaData, KafkaCommand.class);
            if(kafkaCommand != null){
                faultInfo = LCFaultInfo.FaultInfo.parseFrom(kafkaCommand.getMessage());
            }
        }
        catch(Exception e){
            logger.error("kafka 0F39 parse to LCFaultInfoPB error:",e);
        }
        return faultInfo;
    }

}


