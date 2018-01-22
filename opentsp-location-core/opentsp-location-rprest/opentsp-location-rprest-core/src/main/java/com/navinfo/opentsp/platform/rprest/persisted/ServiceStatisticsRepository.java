package com.navinfo.opentsp.platform.rprest.persisted;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.rprest.cache.TerminalRuleCache;
import com.navinfo.opentsp.platform.rprest.dto.ServiceStatisticsDto;
import com.navinfo.opentsp.platform.rprest.entity.VehiclePassInAreaDAEntity;
import com.navinfo.opentsp.platform.rprest.kit.BaseMongoDaoImp;
import com.navinfo.opentsp.platform.rprest.kit.DateUtils;
import com.navinfo.opentsp.platform.rprest.kit.ResVehiclePassInAreaRecordsQuery;
import com.navinfo.opentsp.platform.rprest.kit.VehiclePassInAreaEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.Map.*;
import java.util.regex.Pattern;

/**
 * Created by HOUQL on 2017/5/23.
 */
@Component
@Repository
public class ServiceStatisticsRepository {

    private static final Logger log = LoggerFactory.getLogger(ServiceStatisticsRepository.class);

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private TerminalRuleCache terminalRuleCache;

    /**
     * @return
     */
    public Map<Long, VehiclePassInAreaEntity> GetServiceStatistics(int district, int type, long startDate, long endDate, String methodName) {
//        log.error("区域车次检索-getVehiclePassInArea,开始时间(ms)：   " + RequestUtil.ChangeDate(startTime / 1000));
        ResVehiclePassInAreaRecordsQuery records = new ResVehiclePassInAreaRecordsQuery();
        Map<String, List<String>> listDates = splitWithDays(startDate, endDate);
        for (Entry<String, List<String>> entry : listDates.entrySet()) {
            String collectionName = "VehicleDrivingNumberInArea_" + entry.getKey();

            BasicDBObject query = new BasicDBObject();
            BasicDBList dayList = new BasicDBList();
            List<String> ms = entry.getValue();
            for (String s : ms) {
                dayList.add(Integer.parseInt(s));
            }
            BasicDBObject inDays = new BasicDBObject("$in", dayList);
            query.put("day", inDays);

            BasicDBObject order = new BasicDBObject("times", -1);

            if (type == 0) {
                if (methodName == "GetVehiclePassInArea") {//服务站能力统计
                    query.put("type", 1);//如果是0，则查所有省份
                }
                if (methodName == "GetVehicleNearByStation") {
                    query.put("type", 5);//如果是0，则查全国所有服务站
                }
            }
            if (type == 1 || type == 2) {
                query.put("type", 5);//如果是省、市，则查询改区域下的所有服务站
                if (methodName == "GetVehiclePassInArea") {//服务站能力统计,太乱了额，暂时这样吧

                    Map<Integer, List<Integer>> districtMap = terminalRuleCache.getDistrictAreaMap();


//                如果查询省、市下服务站，需要开启模糊匹配。省：匹配左边两个字符，如100000；市，则匹配左侧四个字符。
                    String filterDistrict = "";
                    if (type == 1) {
                        filterDistrict = String.valueOf(district).substring(0, 2);
                    }
                    if (type == 2) {
                        filterDistrict = String.valueOf(district).substring(0, 4);
                    }
                    BasicDBList areaList = new BasicDBList();
                    List<Integer> areaIDList = new ArrayList<>();
                    //遍历查找省市，查找该省市下的所有服务站
                    for (Entry<Integer, List<Integer>> districtEntry : districtMap.entrySet()) {
                        String disctictCode = districtEntry.getKey().toString();
                        if (disctictCode.startsWith(filterDistrict)) {
                            areaIDList.addAll(districtEntry.getValue());
                        }
                    }
                    for (Integer s : areaIDList) {
                        areaList.add(s);
                    }
                    BasicDBObject inAreaList = new BasicDBObject("$in", areaList);
                    query.put("district", inAreaList);
                }
//                Pattern pattern = Pattern.compile("^"+filterDistrict+".*$", Pattern.CASE_INSENSITIVE);
//                query.put("districtCode",district);
            }

            try {
                DBCollection collection = this.mongoTemplate.getCollection(collectionName);
                BaseMongoDaoImp mongoDaoImp = new BaseMongoDaoImp();
                List<DBObject> dbObjects = mongoDaoImp.queryByCondition(collection, query, order, -1);
//                List<BasicDBObject> dbObjects=  this.mongoTemplate.find(query,BasicDBObject.class,collectionName);
                List<VehiclePassInAreaEntity> entities = convertPassInArea(dbObjects);
                records.addVehiclePassInArea(entities);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Map<Long, VehiclePassInAreaEntity> map = new HashMap<Long, VehiclePassInAreaEntity>();
        for (VehiclePassInAreaEntity entity : records.getDataList()) {
            long districtTemp = entity.get_id();
            if (map.get(district) != null) {
                int times = map.get(district).getTimes() + entity.getTimes();
                entity.setTimes(times);
                map.put(districtTemp, entity);
            } else {
                map.put(districtTemp, entity);
            }
        }
//        collection.
        return map;
    }

    /**
     * 按天拆分 按月拼集合
     *
     * @param beginTime
     * @param endTime
     * @return Map<YYMM,List<YYYYMMDD>
     */
    private Map<String, List<String>> splitWithDays(long beginTime, long endTime) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();
        String currentDayString = DateUtils.format(beginTime, DateUtils.DateFormat.YYYYMMDD);
        String endDayString = DateUtils.format(endTime, DateUtils.DateFormat.YYYYMMDD);
        List<String> middleMonth = DateUtils.getMiddleMonth(beginTime, endTime);
        for (int i = 0; i < middleMonth.size(); i++) {
            String mon = middleMonth.get(i);
            List<String> days = new ArrayList<String>();
            if (0 != i) {
                currentDayString = mon + "01";
            }
            while (true) {
                days.add(currentDayString);
                if (currentDayString.equals(endDayString)) {
                    break;
                } else if (DateUtils.isLastDayOfMonth(DateUtils.parse(currentDayString, DateUtils.DateFormat.YYYYMMDD))) {
                    break;
                }
                currentDayString = DateUtils.getNextDay(currentDayString);
            }
            mon = mon.substring(2);
            result.put(mon, days);
        }
        return result;
    }

    private List<VehiclePassInAreaEntity> convertPassInArea(List<DBObject> dbObjects) {
        List<VehiclePassInAreaEntity> aEntity = new ArrayList<VehiclePassInAreaEntity>();
        List<VehiclePassInAreaDAEntity> daEntities = new ArrayList<VehiclePassInAreaDAEntity>();
        for (DBObject dbObject : dbObjects) {
            VehiclePassInAreaDAEntity daEntity = new VehiclePassInAreaDAEntity();
            daEntity.dbObjectToBean(dbObject);
            daEntities.add(daEntity);
        }
        List<VehiclePassInAreaDAEntity> entities = new ArrayList<VehiclePassInAreaDAEntity>();
        for (VehiclePassInAreaDAEntity v1 : daEntities) {
            boolean has = false;
            for (VehiclePassInAreaDAEntity v2 : entities) {
                if (v1.getDistrict() == v2.getDistrict()) {
                    has = true;
                    v2.setTimes(v1.getTimes() + v2.getTimes());
                }
            }
            if (!has) {
                entities.add(v1);
            }
        }
        for (VehiclePassInAreaDAEntity da : entities) {
            VehiclePassInAreaEntity entity = new VehiclePassInAreaEntity();
            entity.set_id(da.getDistrict());
            entity.setTimes(da.getTimes());
            entity.setLat(da.getLat());
            entity.setLng(da.getLng());
            aEntity.add(entity);
        }
        return aEntity;
    }
}
