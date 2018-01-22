package com.navinfo.opentsp.platform.location.persistence;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.location.entity.GpsDataEntityDB;
import com.navinfo.opentsp.platform.location.entity.GpsDetailedEntityDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wanliang on 2017/4/10.
 */
@Repository
public class LocationRepository  {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${opentsp.gps.collection.name}")
    private String gpsCollectionName;
    public void saveGpsData(GpsDataEntityDB dataEntities) {
        List<GpsDetailedEntityDB> datas = dataEntities.getDataList();
        List<DBObject> dbObjects = new ArrayList<DBObject>(datas.size());
        for (GpsDetailedEntityDB gpsDetailedEntity : datas) {
            dbObjects.add(gpsDetailedEntity.toDBObject());
        }
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("tId", dataEntities.gettId());
        dbObject.put("day", dataEntities.getDay());
        dbObject.put("dataList", dbObjects);
        mongoTemplate.save(dbObject,gpsCollectionName);
    }

    public void updateGpsData(BasicDBObject dbObject){
        mongoTemplate.save(dbObject,gpsCollectionName);
    }

    public BasicDBObject findGpsDataByDayAndTtid(long tid,String day){
      return   mongoTemplate.findOne(new Query(Criteria.where("tId").is(tid).and("day").is(day)).with(new Sort(Sort.Direction.DESC, "dataList.gpsTime ")),BasicDBObject.class,gpsCollectionName);
    }
}
