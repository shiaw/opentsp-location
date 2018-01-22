package com.navinfo.opentsp.platform.da.core.configuration;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisClusters;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsDataEntry;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import com.navinfo.opentsp.platform.da.core.service.LocationDataQueueService;
import com.navinfo.opentsp.platform.da.core.startup.DARun;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by gx on 2017/11/23.
 * @author gx
 * 0980协议，消费kafka存储位置点数据到redis中
 * 有2处存储redis数据的地方，目前一个用的codis，另一个用的是哨兵
 */

@Component
public class KafkaConsumerListener0980 implements MessageListener<String, KafkaCommand>{

    private static Logger log = LoggerFactory.getLogger(KafkaConsumerListener0980.class);

    @Value("${opentsp.monitor.timeOut:60000}")
    private long timeOut;
    @Value("${consumer.batchNum:1000}")
    private int batchNum;
    private volatile long timeCache = 0;
    public static final String LOCATION_DATA_PROTOCOL = "0980";
    private static ILocationRedisService iLocationRedisService = new LocationRedisServiceImpl();
    static ThreadLocal<List<TempGpsDataEntry>> tempGpsDataQueue = new ThreadLocal<List<TempGpsDataEntry>>(){
        @Override
        protected List<TempGpsDataEntry> initialValue() {
            return new ArrayList<>();
        }
    };

    @Override
    public void onMessage(ConsumerRecord<String, KafkaCommand> consumerRecord) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {

            KafkaCommand kafkaCommand = (KafkaCommand) consumerRecord.value();
            if(kafkaCommand == null || !kafkaCommand.getCommandId().equals(LOCATION_DATA_PROTOCOL)){
                return;
            }

            Packet packet = JacksonUtils.objectMapperBuilder().readValue(kafkaCommand.getMessage(),Packet.class);
            if(packet == null){
                return;
            }
            log.info("DP --> DA SUCESS!{}", packet.toString());
            LCLocationDataSave.LocationDataSave locationDataSave = LCLocationDataSave.LocationDataSave.parseFrom(packet.getContent());
            if(locationDataSave != null){
                tempGpsDataQueue.get().add(new TempGpsDataEntry(locationDataSave));
                if(tempGpsDataQueue.get().size() >= batchNum){
                    iLocationRedisService.savaNormalLocation(null, tempGpsDataQueue.get());
                    TempGpsData.saveLastestLcToStaticRedis(tempGpsDataQueue.get());
                    tempGpsDataQueue.get().clear();
                }
            }

            log.info("~~~~~~末次位置首次将daHost与时间存入redis~~~~~");
            jedis = RedisClusters.getInstance().getJedis();
            if(timeCache == 0){
                timeCache = System.currentTimeMillis();
                jedis.set(DARun.localhost,String.valueOf(timeCache));
            } else {
                long time = System.currentTimeMillis();
                if(time - timeCache > timeOut) {
                    timeCache = time;
                    jedis.set(DARun.localhost,String.valueOf(timeCache));
                }
            }
            log.info("~~~~~~redis存储结束~~~~~");
        } catch (IOException e) {
            isBroken = true;
            log.error("", e);
        }finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
    }







    private void sendData(){
//        int count = 0;
//        for(int i=1;i<50000000;i++){
//            count ++ ;
//            packet.setContent(locationDataSave.toBuilder().setTerminalId(i + 50000000).build().toByteArray());
//            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(packet));
//            dpKafkaTemplate.send("daposdonenew", kafkaCommand);
//            if(count > 1000){
//                count=0;
//                Thread.sleep(100);
//            }
//        }
    }
}
