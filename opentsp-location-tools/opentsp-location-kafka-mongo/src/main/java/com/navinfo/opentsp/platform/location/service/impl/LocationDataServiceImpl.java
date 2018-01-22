package com.navinfo.opentsp.platform.location.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.json.JacksonUtils;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.platform.location.entity.GpsDataComparator;
import com.navinfo.opentsp.platform.location.entity.GpsDataEntityDB;
import com.navinfo.opentsp.platform.location.entity.GpsDetailedEntityDB;
import com.navinfo.opentsp.platform.location.kafka.Mutual_0980_LocationDataSave_Kafka;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.persistence.LocationRepository;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLocationDataSave;
import com.navinfo.opentsp.platform.location.service.LocationDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Created by wanliang on 2017/4/10.
 */
@Service
public class LocationDataServiceImpl  implements LocationDataService{
    @Value("${opentsp.location.save.date:0}")
    private String  locationSaveDate;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;


    private static final Logger log = LoggerFactory.getLogger(LocationDataServiceImpl.class);
    @Override
    public boolean saveGpsData(Packet packet) {
        KafkaCommand kafkaCommand = new KafkaCommand();
        try {
            kafkaCommand.setMessage(JacksonUtils.objectMapperBuilder().writeValueAsBytes(null));
            kafkaCommand.setCommandId("80001");
            kafkaCommand.setTopic("location-monitor");
            kafkaCommand.setKey("80001");
            kafkaMessageChannel.send(kafkaCommand);
            //logger.info("dpMonitor send to kafka success !{}",locationMonitor.toString());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            //logger.error("序列化出错!{}",kafkaCommand,e);
        }
        /*try {
            LCLocationDataSave.LocationDataSave locationDataSave = LCLocationDataSave.LocationDataSave.parseFrom(packet.getContent());
            GpsDataEntityDB dataEntity = new GpsDataEntityDB();
            dataEntity.settId(Long.parseLong(packet.getUniqueMark()));
            dataEntity.setDay(DateUtils.format(locationDataSave.getLocationData().getGpsDate(),
                    DateUtils.DateFormat.YYYYMMDD));
            LCLocationData.LocationData locationData= locationDataSave.getLocationData();
            GpsDetailedEntityDB     detailedEntity = new GpsDetailedEntityDB();
            detailedEntity.setData(locationData.toByteArray());
            detailedEntity.setGpsTime(locationData.getGpsDate());
            dataEntity.addGpsDetailed(detailedEntity);
            BasicDBObject basicDBObject= locationRepository.findGpsDataByDayAndTtid(Long.parseLong(packet.getUniqueMark()),locationSaveDate);
            if(basicDBObject!=null){
               List<DBObject> dataList=( List<DBObject>)basicDBObject.get("dataList");
               for (DBObject gpsData:dataList){
                   long gpsTime=(long)gpsData.get("gpsTime");
                   if(locationData.getGpsDate()==gpsTime){
                       log.info("位置点已经存在，不进行存储 ,tid:{},gpsDate:{}",packet.getUniqueMark(),gpsTime);
                       return false;
                   }
               }
               dataList.add(detailedEntity.toDBObject());
               Collections.sort(dataList,new GpsDataComparator());
                basicDBObject.put("dataList",dataList);
                locationRepository.updateGpsData(basicDBObject);
                return true;
            }

            locationRepository.saveGpsData(dataEntity);
        }catch (IOException e) {
            e.printStackTrace();
        }*/
        return false;
    }
}
