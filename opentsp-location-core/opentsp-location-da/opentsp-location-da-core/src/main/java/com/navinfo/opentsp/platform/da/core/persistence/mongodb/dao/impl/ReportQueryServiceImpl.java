
package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.navinfo.opentsp.platform.da.core.cache.ReportQueryCache;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.ThinningPoint;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatus.VehicleStatus.Status;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.Mileages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.InvalidProtocolBufferException;
import  com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import  com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import  com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalLocationData.TerminalLocationData;
import  com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalTrackRes.TerminalTrackRes;
import  com.navinfo.opentsp.platform.da.core.common.Constant;
import  com.navinfo.opentsp.platform.da.core.common.hash.DefaultHash;
import  com.navinfo.opentsp.platform.da.core.common.hash.Hash;
import  com.navinfo.opentsp.platform.da.core.persistence.application.DateDiffUtil;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.MilagesEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.BaseMongoDaoImpl;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.ReportQueryService;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

import redis.clients.jedis.Jedis;

public class ReportQueryServiceImpl implements ReportQueryService {

    private Logger logger = LoggerFactory.getLogger(ReportQueryServiceImpl.class);

    private final static String QUERY_CACHE_KEY_PREFIX = "QueryCache_";

    private static final double LimitSpeed = 2000;

    private BaseMongoDaoImpl mongoService = new BaseMongoDaoImpl();

    final static ILocationRedisService locationRedisService = new LocationRedisServiceImpl();

    @Override
    public TerminalTrackRes getTerminalTrack(long terminalId, long beginDate, long endDate, boolean isFilterBreakdown,
                                             long breakdownCode, boolean isThin, int level, int isAll,CommonParameter commonParameter) {
        long count = 0;
        List<GpsDetailedEntityDB> entities = null;
        if (endDate > beginDate) {
            count = this.getGpsDetailedEntitiesCount(terminalId, beginDate, endDate, isFilterBreakdown, breakdownCode);
            try {
                entities = isFilterBreakdown
                        ? this.getGpsDetailedEntitiesWithFilterBreakdown(terminalId, beginDate, endDate, breakdownCode,
                        commonParameter)
                        : this.getGpsDetailedEntitiesWithoutFilterBreakdown(terminalId, beginDate, endDate,
                        commonParameter);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        TerminalTrackRes.Builder builder = TerminalTrackRes.newBuilder();
        builder.setStatusCode(1);

        if (entities != null) {
            // 查询结果转换
            for (GpsDetailedEntityDB entity : entities) {
                try {
                    if (isAll == 0){
                        LocationData location = LocationData.parseFrom(entity.getData());
                        if (location.getBatteryPower() != 0){
                            continue;
                        }
                    }

                    TerminalLocationData.Builder b = TerminalLocationData.newBuilder();
                    b.setTerminalId(terminalId);
                    b.setLocationData(LocationData.parseFrom(entity.getData()));
                    builder.addDatas(b.build());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    continue;
                }
            }
        }
        if (isThin) {
            ThinningPoint point = new ThinningPoint();
            List<TerminalLocationData> list = point.dpComputeToListByDaYun(builder.getDatasList(), level, "", new HashMap<String, Integer>(), true);
            builder.clearDatas();
            builder.addAllDatas(list);
            builder.setTotalRecords(list.size());
        }else {
            if (isAll == 0){
                builder.setTotalRecords(builder.getDatasList().size());
            }else{
                builder.setTotalRecords((int) count);
            }

        }
        return builder.build();
    }

    private List<GpsDetailedEntityDB> getGpsDetailedEntitiesWithFilterBreakdown(long terminalId, long beginDate,
                                                                                long endDate, long breakdownCode, CommonParameter commonParameter) throws UnknownHostException {
        List<GpsDetailedEntityDB> resultEntities = new LinkedList<>();

        if (endDate > beginDate) {
            // 查询结果在结果集中的范围
            int minIndex = 0, maxIndex = Integer.MAX_VALUE;
            if (commonParameter.isMultipage()) {
                minIndex = commonParameter.getPageIndex() * commonParameter.getPageSize();
                maxIndex = minIndex + commonParameter.getPageSize();
            }
            // 标记是否需要继续在MongoDB中查询
            boolean dataInMongoRequired = false;
            long todayEnd = this.getTodayEnd();
            long todayBegin = this.getTodayBegin();

            // 筛选故障码，查询结果按时间倒序排序
            if (endDate > todayBegin) {
                // 当天的数据从REDIS获取
                List<GpsDetailedEntityDB> todayData = this.getTodayGpsDetailedEntities(terminalId, Math.max(todayBegin, beginDate),
                        Math.min(todayEnd, endDate), true, breakdownCode);
                if (todayData != null) {
                    // 判断是否需要加入查询结果
                    if (commonParameter.isMultipage()) {
                        int size = todayData.size();
                        if (minIndex < size) {
                            int m = Math.min(maxIndex, size);
                            for (int i = minIndex; i < m; i++) {
                                resultEntities.add(todayData.get(i));
                            }
                        }
                        // 未填满则需要继续查询MongoDB
                        dataInMongoRequired = (resultEntities.size() < commonParameter.getPageSize());
                    } else {
                        // 不分页，加入查询结果
                        resultEntities.addAll(todayData);
                        dataInMongoRequired = true;
                    }
                }
            } else {
                // 不查询当天数据
                dataInMongoRequired = true;
            }

            if (dataInMongoRequired) {
                // 历史数据从MongoDB获取
                if (todayBegin > beginDate) {
                    // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
                    Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(beginDate, todayBegin - 1);

                    List<String> dateKeys = new LinkedList<>();
                    for (String key : datesSlot.keySet()) {
                        dateKeys.add(key);
                    }
                    // 降序排序
                    Collections.sort(dateKeys, new Comparator<String>() {

                        @Override
                        public int compare(String str0, String str1) {
                            return Integer.parseInt(str1) - Integer.parseInt(str0);
                        }

                    });

                    // 遍历获取结果
                    int prevCount = resultEntities.size();// 用于分页查询
                    for (String dateKey : dateKeys) {
                        Map<String, Long> dateSlot = datesSlot.get(dateKey);

                        Long beginTime = (Long) dateSlot.get("beginTime");
                        Long endTime = (Long) dateSlot.get("endTime");
                        if (beginTime == null || endTime == null) {
                            continue;
                        } else {
                            beginTime = Math.max(beginDate, beginTime);
                            endTime = Math.min(endDate, endTime);
                        }
                        DBCollection collection =  MongoManager.start(LCMongo.DB.LC_GPS_LOCATION,
                                LCMongo.Collection.LC_GPS_DATA_ENTITY + dateKey);
                        BasicDBObject orderBy = new BasicDBObject();
                        orderBy.put("day", -1);

                        if (commonParameter.isMultipage()) {
                            // 分片时长，单位：小时
                            int segmentUnit = 24;
                            int stepSeconds = segmentUnit * 3600;
                            // 按开始时间分片
                            long begin = beginTime.longValue();
                            long end = 0;

                            while (begin < endTime && resultEntities.size() < commonParameter.getPageSize()) {
                                end = Math.min(endTime, begin + stepSeconds);

                                // 数据位置区间
                                int count = 0;
                                try {
                                    // 结束时间需减1，否则会多查询一个分段
                                    count = this.getGpsDetailedEntitiesCount(collection.getName(), terminalId, dateKey,
                                            begin, end - 1, true, breakdownCode);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                                int[] range = { prevCount, prevCount + count };
                                // 目标位置区间
                                int p = commonParameter.getPageIndex() * commonParameter.getPageSize();
                                int[] targetRange = { p, p + commonParameter.getPageSize() };

                                if (range[1] <= targetRange[0]) {
                                    prevCount += count;
                                    begin += stepSeconds;
                                    continue;
                                } else if (range[0] >= targetRange[1]) {
                                    break;
                                } else {
                                    // 区间有交叉，存在符合条件的结果
                                    prevCount += count;
                                    // 查询
                                    BasicDBObject query = this.buildGpsDetailedEntitiesQuery(terminalId, begin, end - 1);

                                    List<DBObject> dbObjects = mongoService.queryByCondition(collection, query,
                                            orderBy, -1);
                                    if (dbObjects != null) {
                                        List<GpsDetailedEntityDB> gpsDetailedEntities = null;
                                        try {
                                            gpsDetailedEntities = this.convert2GpsDetailedEntityDB(terminalId,
                                                    dbObjects, beginDate, endDate, true, breakdownCode);
                                        } catch (InvalidProtocolBufferException e) {
                                            e.printStackTrace();
                                        }
                                        if (gpsDetailedEntities != null) {
                                            // 计算查询结果的提取范围
                                            int minIdx = Math.max(targetRange[0], range[0]) - range[0];
                                            int maxIdx = Math.min(targetRange[1], range[1]) - range[0];
                                            maxIdx = Math.min(maxIdx, minIdx + gpsDetailedEntities.size());
                                            for (int i = minIdx; i < maxIdx; i++) {
                                                resultEntities.add(gpsDetailedEntities.get(i));
                                            }
                                        }
                                    }
                                    begin += stepSeconds;
                                }
                            }
                        } else {
                            // 不必分片，查询当前collection中所有符合条件的数据
                            BasicDBObject query = this.buildGpsDetailedEntitiesQuery(terminalId, beginDate, endDate - 1);

                            List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                            if (dbObjects != null && dbObjects.size() > 0) {
                                try {
                                    List<GpsDetailedEntityDB> temp = this.convert2GpsDetailedEntityDB(terminalId,
                                            dbObjects, beginDate, endDate, true, breakdownCode);
                                    resultEntities.addAll(temp);
                                } catch (InvalidProtocolBufferException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }

            // 排序
            Collections.sort(resultEntities, new Comparator<GpsDetailedEntityDB>() {

                @Override
                public int compare(GpsDetailedEntityDB arg0, GpsDetailedEntityDB arg1) {
                    return (int) (arg1.getGpsTime() - arg0.getGpsTime());
                }

            });
        }

        return resultEntities;
    }

    private List<GpsDetailedEntityDB> getGpsDetailedEntitiesWithoutFilterBreakdown(long terminalId, long beginDate,
                                                                                   long endDate, CommonParameter commonParameter) throws UnknownHostException {
        List<GpsDetailedEntityDB> resultEntities = new LinkedList<>();

        if (endDate > beginDate) {
            // 查询结果在结果集中的范围
            int minIndex = 0, maxIndex = Integer.MAX_VALUE;
            if (commonParameter.isMultipage()) {
                minIndex = commonParameter.getPageIndex() * commonParameter.getPageSize();
                maxIndex = minIndex + commonParameter.getPageSize();
            }
            // 标记是否需要继续在Redis中查询
            boolean dataInRedisRequired = false;
            long todayEnd = this.getTodayEnd();
            long todayBegin = this.getTodayBegin();

            // 筛选故障码，查询结果按时间升序排序

            // 历史数据从MongoDB获取
            if (beginDate < todayBegin) {
                // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
                Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(beginDate, todayBegin - 1);

                List<String> dateKeys = new LinkedList<>();
                for (String key : datesSlot.keySet()) {
                    dateKeys.add(key);
                }
                // 升序排序
                Collections.sort(dateKeys, new Comparator<String>() {

                    @Override
                    public int compare(String str0, String str1) {
                        return Integer.parseInt(str0) - Integer.parseInt(str1);
                    }

                });

                // 遍历获取结果
                int prevCount = 0;// 用于分页查询
                for (String dateKey : dateKeys) {
                    Map<String, Long> dateSlot = datesSlot.get(dateKey);

                    Long beginTime = (Long) dateSlot.get("beginTime");
                    Long endTime = (Long) dateSlot.get("endTime");
                    if (beginTime == null || endTime == null) {
                        continue;
                    } else {
                        beginTime = Math.max(beginDate, beginTime);
                        endTime = Math.min(endDate, endTime);
                    }
                    DBCollection collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION,
                            LCMongo.Collection.LC_GPS_DATA_ENTITY + dateKey);
                    BasicDBObject orderBy = new BasicDBObject();
                    orderBy.put("day", 1);

                    if (commonParameter.isMultipage()) {
                        // 分片时长，单位：小时
                        int segmentUnit = 24;
                        int stepSeconds = segmentUnit * 3600;
                        // 按开始时间分片
                        long begin = beginTime.longValue();
                        long end = 0;

                        while (begin < endTime && resultEntities.size() < commonParameter.getPageSize()) {
                            end = Math.min(endTime, begin + stepSeconds);

                            // 数据位置区间
                            int count = 0;
                            try {
                                count = this.getGpsDetailedEntitiesCount(collection.getName(), terminalId, dateKey,
                                        begin, end - 1, false, -1);
                            } catch (Exception e1) {
                                e1.printStackTrace();
                            }// 结束时间需减1，否则会一次查询两个分段
                            // System.out.println("terminalId is " + terminalId + ", begin is " + begin + ", end is " + end + ", count is " + count);
                            int[] range = { prevCount, prevCount + count };
                            // 目标位置区间
                            int p = commonParameter.getPageIndex() * commonParameter.getPageSize();
                            int[] targetRange = { p, p + commonParameter.getPageSize() };

                            if (range[1] <= targetRange[0]) {
                                prevCount += count;
                                begin += stepSeconds;
                                continue;
                            } else if (range[0] >= targetRange[1]) {
                                break;
                            } else {
                                // 区间有交叉，存在符合条件的结果
                                prevCount += count;
                                // 查询
                                BasicDBObject query = this.buildGpsDetailedEntitiesQuery(terminalId, begin, end - 1);

                                List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                                if (dbObjects != null && dbObjects.size() > 0) {
                                    List<GpsDetailedEntityDB> gpsDetailedEntities = null;
                                    try {
                                        gpsDetailedEntities = this.convert2GpsDetailedEntityDB(terminalId, dbObjects, beginDate, endDate,
                                                false, -1);
                                    } catch (InvalidProtocolBufferException e) {
                                        e.printStackTrace();
                                    }
                                    if (gpsDetailedEntities != null) {
                                        // 计算查询结果的提取范围
                                        int minIdx = Math.max(targetRange[0], range[0]) - range[0];
                                        int maxIdx = Math.min(targetRange[1], range[1]) - range[0];
                                        maxIdx = Math.min(maxIdx, minIdx + gpsDetailedEntities.size());
                                        for (int i = minIdx; i < maxIdx; i++) {
                                            resultEntities.add(gpsDetailedEntities.get(i));
                                        }
                                    }
                                }
                                begin += stepSeconds;
                            }
                        }

                        // 未填满则需要继续查询Redis
                        dataInRedisRequired = (resultEntities.size() < commonParameter.getPageSize());
                    } else {
                        dataInRedisRequired = true;
                        // 不必分片，查询当前collection中所有符合条件的数据
                        BasicDBObject query = this.buildGpsDetailedEntitiesQuery(terminalId, beginDate, endDate - 1);

                        List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                        if (dbObjects != null && dbObjects.size() > 0) {
                            try {
                                List<GpsDetailedEntityDB> temp = this.convert2GpsDetailedEntityDB(terminalId, dbObjects, beginDate, endDate,
                                        false, -1);
                                resultEntities.addAll(temp);
                            } catch (InvalidProtocolBufferException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                // 不在MongoDB中查询
                dataInRedisRequired = true;
            }

            if (dataInRedisRequired) {
                if (endDate > todayBegin) {
                    // 当天的数据从REDIS获取
                    List<GpsDetailedEntityDB> todayData = this.getTodayGpsDetailedEntities(terminalId, Math.max(todayBegin, beginDate),
                            Math.min(todayEnd, endDate), false, -1);
                    if (todayData != null) {
                        // 判断是否需要加入查询结果
                        if (commonParameter.isMultipage()) {
                            int size = todayData.size();
                            if (minIndex < size) {
                                int m = Math.min(maxIndex, size);
                                for (int i = minIndex; i < m; i++) {
                                    resultEntities.add(todayData.get(i));
                                }
                            }
                        } else {
                            // 不分页，加入查询结果
                            resultEntities.addAll(todayData);
                        }
                    }
                }
            }

            // 排序
            Collections.sort(resultEntities, new Comparator<GpsDetailedEntityDB>() {

                @Override
                public int compare(GpsDetailedEntityDB arg0, GpsDetailedEntityDB arg1) {
                    return (int) (arg0.getGpsTime() - arg1.getGpsTime());
                }

            });
        }

        return resultEntities;
    }

    /**
     * 查询指定终端指定时间段内轨迹数据总数
     *
     * @param terminalId
     * @param beginDate
     * @param endDate
     * @param isFilterBreakdown
     * @param breakdownCode
     * @return 指定终端指定时间段内轨迹数据总数
     */
    private long getGpsDetailedEntitiesCount(long terminalId, long beginDate, long endDate,
                                             final boolean isFilterBreakdown, long breakdownCode) {
        // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
        Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(beginDate, endDate);

        List<String> dateKeys = new LinkedList<>();
        for (String key : datesSlot.keySet()) {
            dateKeys.add(key);
        }
        // 降序排序
        Collections.sort(dateKeys, new Comparator<String>() {

            @Override
            public int compare(String str0, String str1) {
                return Integer.parseInt(str1) - Integer.parseInt(str0);
            }

        });

        long count = 0;
        // 查询MongoDB内数据
        for (String dateKey : dateKeys) {
            Map<String, Long> dateSlot = datesSlot.get(dateKey);

            Long beginTime = (Long) dateSlot.get("beginTime");
            Long endTime = (Long) dateSlot.get("endTime");
            if (beginTime == null || endTime == null) {
                continue;
            } else {
                beginTime = Math.max(beginDate, beginTime);
                endTime = Math.min(endDate, endTime);
            }

            int c = 0;
            try {
                c = this.getGpsDetailedEntitiesCount(LCMongo.Collection.LC_GPS_DATA_ENTITY + dateKey, terminalId,
                        dateKey, beginTime, endTime - 1, isFilterBreakdown, breakdownCode);
            } catch (Exception e) {
                e.printStackTrace();
            }
            count += c;
        }

        // 查询Redis内数据
        long todayEnd = this.getTodayEnd();
        long todayBegin = this.getTodayBegin();
        if (endDate > todayBegin) {
            // 当天的数据从REDIS获取
            List<GpsDetailedEntityDB> todayData = this.getTodayGpsDetailedEntities(terminalId, Math.max(todayBegin, beginDate),
                    Math.min(todayEnd, endDate), isFilterBreakdown, breakdownCode);
            if (todayData != null) {
                count += todayData.size();
            }
        }
        return count;
    }

    /**
     * 转换为GpsDetailedEntityDB并排序
     *
     * @param terminalId
     * @param dbObjects
     * @param beginTime
     * @param endTime
     * @param isFilterBreakdown
     * @param breakdownCode
     * @return
     * @throws InvalidProtocolBufferException
     */
    private List<GpsDetailedEntityDB> convert2GpsDetailedEntityDB(long terminalId, List<DBObject> dbObjects, long beginTime,
                                                                  long endTime,
                                                                  final boolean isFilterBreakdown, long breakdownCode) throws InvalidProtocolBufferException {
        List<GpsDetailedEntityDB> entities = new LinkedList<>();
        for (DBObject dbObject : dbObjects) {
            List<GpsDetailedEntityDB> gpsDetailedEntities = this.convert2GpsDetailedEntities(terminalId, dbObject, beginTime, endTime,
                    isFilterBreakdown, breakdownCode);
            if (gpsDetailedEntities != null) {
                entities.addAll(gpsDetailedEntities);
            }
        }
        // 排序
        Collections.sort(entities, new Comparator<GpsDetailedEntityDB>() {

            @Override
            public int compare(GpsDetailedEntityDB arg0, GpsDetailedEntityDB arg1) {
                if (isFilterBreakdown) {
                    return (int) (arg1.getGpsTime() - arg0.getGpsTime());
                } else {
                    return (int) (arg0.getGpsTime() - arg1.getGpsTime());
                }
            }

        });
        return entities;
    }

    /**
     * 转换为GpsDetailedEntityDB，并自动过滤故障码（如果需要）
     *
     * @param terminalId
     * @param dbObject
     * @param beginTime
     * @param endTime
     * @param isFilterBreakdown
     * @param breakdownCode
     * @return
     * @throws InvalidProtocolBufferException
     */
    private List<GpsDetailedEntityDB> convert2GpsDetailedEntities(long terminalId, DBObject dbObject,long beginTime,
                                                                  long endTime,
                                                                  final boolean isFilterBreakdown, long breakdownCode) throws InvalidProtocolBufferException {
        List<GpsDetailedEntityDB> entities = this.convert2GpsDetailedEntities(terminalId, dbObject);
        List<GpsDetailedEntityDB> matchedEntities = new LinkedList<>();

        for (GpsDetailedEntityDB entity : entities) {
            LocationData locationData = LocationData.parseFrom(entity.getData());
            long gt = locationData.getGpsDate();
            boolean matched = false;
            if((gt >= beginTime && gt <= endTime)) {
                if (isFilterBreakdown) {
                    List<VehicleBreakdown> breakdowns = locationData.getBreakdownAddition().getBreakdownList();
                    // 如果isFilterBreakdown为true，breakdownCode为-1，则查询有故障码的位置数据
                    // 如果isFilterBreakdown为true，breakdownCode>=0，则查询匹配故障码的位置数据
                    if (breakdowns != null && breakdowns.size() > 0) {
                        if (breakdownCode >= 0) {
                            for (VehicleBreakdown b : breakdowns) {
                                int spn = b.getBreakdownSPNValue();
                                int fmi = b.getBreakdownFMIValue();
                                int targetCode = spn * 1000 + fmi;
                                if ((int) breakdownCode == targetCode) {
                                    matched = true;
                                    break;
                                }
                            }
                        } else {
                            matched = true;
                        }
                    }
                } else {
                    matched = true;
                }
            }
            if (matched) {
                matchedEntities.add(entity);
            }
        }
        return matchedEntities;
    }

    @SuppressWarnings("unchecked")
    private List<GpsDetailedEntityDB> convert2GpsDetailedEntities(long terminalId, DBObject dbObject) {
        List<GpsDetailedEntityDB> entities = new LinkedList<>();
        List<DBObject> dataList = (List<DBObject>) dbObject.get("dataList");
        long dbTid = (long) dbObject.get("tId");
        if (dataList != null && terminalId == dbTid) {
            for (DBObject data : dataList) {
                entities.add(new GpsDetailedEntityDB(data, terminalId));
            }
        }
        return entities;
    }

    private BasicDBObject buildGpsDetailedEntitiesQuery(long terminalId, long beginDate, long endDate) {
        BasicDBObject query = new BasicDBObject();
        query.put("tId", terminalId);
        // 文档上定义的day属性为int32类型，但是实际上存储的是String
        // 因此分情况处理如下
        boolean dayStoreAsString = true;
        if (dayStoreAsString) {
            BasicDBList dayList = new BasicDBList();
            for (long d = beginDate; d <= endDate;) {
                int day = this.getDayValue("yyyyMMdd", d);
                dayList.add(String.valueOf(day));
                d += 24 * 3600;
            }
            // 如果开始时间不是0点0分0秒，则可能漏掉最后一天，补救处理如下
            int de = this.getDayValue("yyyyMMdd", endDate);
            if(!dayList.contains(String.valueOf(de))) {
                dayList.add(String.valueOf(de));
            }

            BasicDBObject inDays = new BasicDBObject("$in", dayList);
            query.put("day", inDays);
        } else {
            int beginDay = this.getDayValue("yyyyMMdd", beginDate);
            int endDay = this.getDayValue("yyyyMMdd", endDate);
            query.put("day", new BasicDBObject("$gte", beginDay).append("$lte", endDay));
        }
        return query;
    }

    private int getGpsDetailedEntitiesCount(String collectionName, long terminalId, String dateKey, long beginTime,
                                            long endTime, boolean isFilterBreakdown, long breakdownCode) throws Exception {
        if (endTime > beginTime) {
            StringBuilder cacheKey = new StringBuilder("");
            cacheKey.append(this.getClass().getName()).append("__getGpsDetailedEntitiesCount");
            cacheKey.append("_").append(collectionName);
            cacheKey.append("_").append(terminalId);
            cacheKey.append("_").append(dateKey);
            cacheKey.append("_").append(beginTime);
            cacheKey.append("_").append(endTime);
            cacheKey.append("_").append(isFilterBreakdown);
            cacheKey.append("_").append(breakdownCode);

            Hash hash = new DefaultHash();
            String key = QUERY_CACHE_KEY_PREFIX + hash.computeMd5String(cacheKey.toString(), "utf-8");
            ReportQueryCache.getInstance().add(key);

            // Redis IO
            boolean isBroken = false;
            Jedis jedis = null;
            try {
                boolean cacheEnabled = true;
                // jedis = RedisClusters.getInstance().getJedis();
                jedis = RedisStatic.getInstance().getJedis();
                String value = jedis.get(key);
                if (value != null && cacheEnabled) {
                    return Integer.parseInt(value);
                } else {
                    int count = 0;
                    // 查询并保存
                    BasicDBObject query = this.buildGpsDetailedEntitiesQuery(terminalId, beginTime, endTime);
                    // 无法直接执行统计查询，需查询出所有结果并逐个比对
                    DBCollection collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION, collectionName);
                    List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, null, -1);
                    if (dbObjects != null) {
                        try {
                            List<GpsDetailedEntityDB> temp = this.convert2GpsDetailedEntityDB(terminalId, dbObjects, beginTime, endTime,
                                    isFilterBreakdown, breakdownCode);
                            count = (temp != null) ? temp.size() : 0;
                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }
                    }
                    jedis.set(key, String.valueOf(count));
                    // 5分钟后过期
                    jedis.expireAt(key, System.currentTimeMillis() + 300000);
                    return count;
                }
            } catch (Exception e) {
                isBroken = true;
                throw e;
            } finally {
                RedisStatic.getInstance().release(jedis, isBroken);
            }
        } else {
            return 0;
        }
    }

    private int getDayValue(String dateFormat, long timeSeconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeSeconds * 1000);
        SimpleDateFormat df = new SimpleDateFormat(dateFormat);
        String text = df.format(calendar.getTime());
        return Integer.parseInt(text);
    }

    private long getTodayEnd() {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 1);

        return (calendar.getTimeInMillis() / 1000) - 1;
    }

    private long getTodayBegin() {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return (calendar.getTimeInMillis() / 1000);
    }

    /**
     * 查询指定终端当天的轨迹数据（指定时间范围，自动过滤故障码，结果排序，筛选故障码则倒序排序，否则升序排序）
     *
     * @param terminalId
     * @param beginDate
     * @param endDate
     * @param isFilterBreakdown
     * @param breakdownCode
     * @return
     */
    private List<GpsDetailedEntityDB> getTodayGpsDetailedEntities(long terminalId, long beginDate, long endDate,
                                                                  final boolean isFilterBreakdown, long breakdownCode) {
        List<GpsDetailedEntityDB> result = new LinkedList<GpsDetailedEntityDB>();

        if (endDate > beginDate) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            List<LocationData> list = locationRedisService.findNormalLocation(terminalId, calendar);

            GpsDetailedEntityDB detailedEntity = null;
            for (LocationData locationData : list) {
                long gt = locationData.getGpsDate();
                if (gt >= beginDate && gt <= endDate) {
                    boolean matched = false;
                    if (isFilterBreakdown) {
                        List<VehicleBreakdown> breakdowns = locationData.getBreakdownAddition().getBreakdownList();
                        // 如果isFilterBreakdown为true，breakdownCode为-1，则查询有故障码的位置数据
                        // 如果isFilterBreakdown为true，breakdownCode>=0，则查询匹配故障码的位置数据
                        if (breakdowns != null && breakdowns.size() > 0) {
                            if (breakdownCode >= 0) {
                                for (VehicleBreakdown b : breakdowns) {
                                    int spn = b.getBreakdownSPNValue();
                                    int fmi = b.getBreakdownFMIValue();
                                    int targetCode = spn * 1000 + fmi;
                                    if ((int) breakdownCode == targetCode) {
                                        matched = true;
                                        break;
                                    }
                                }
                            } else {
                                matched = true;
                            }
                        }
                    } else {
                        matched = true;
                    }
                    if (matched) {
                        detailedEntity = new GpsDetailedEntityDB();
                        detailedEntity.set_id(terminalId);
                        detailedEntity.setGpsTime(locationData.getGpsDate());
                        detailedEntity.setData(locationData.toByteArray());
                        result.add(detailedEntity);
                    }
                }
            }
            // 排序
            Collections.sort(result, new Comparator<GpsDetailedEntityDB>() {

                @Override
                public int compare(GpsDetailedEntityDB arg0, GpsDetailedEntityDB arg1) {
                    if (isFilterBreakdown) {
                        return (int) (arg1.getGpsTime() - arg0.getGpsTime());
                    } else {
                        return (int) (arg0.getGpsTime() - arg1.getGpsTime());
                    }
                }

            });
        }

        return result;
    }

    private void echoTimeSeconds(long seconds) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(seconds * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("echoTimeSeconds: " + seconds + " is " + dateFormat.format(calendar.getTime()));
    }

    @Override
    public List<DAMilages> getMilageRecords(List<Long> terminalIds, long startDate, long endDate) {
        List<DAMilages> milages = new ArrayList<>();
        // 查询并返回结果
        if (endDate > startDate) {
            // 构建查询条件
            BasicDBObject query = new BasicDBObject();
            BasicDBList tidList = new BasicDBList();
            for (long tid : terminalIds) {
                tidList.add(tid);
            }
            BasicDBObject inTids = new BasicDBObject("$in", tidList);
            BasicDBObject gt = new BasicDBObject("$gte", startDate);
            BasicDBObject lt = new BasicDBObject("$lte", endDate);
            query.put("start", gt);
            query.put("tid", inTids);
            query.put("end", lt);
            boolean echo = false;
//			if(echo) {
//				echoTimeSeconds(startDate);
//				echoTimeSeconds(endDate);
//			}
            // 不同日期（天）的数据存储于不同的Collection
            MilagesEntity template = new MilagesEntity();
            Map<String, Map<String, Long>> _getDateSlot = DateDiffUtil
                    .splitDateWithMonth(startDate, endDate);
            for (Entry<String, Map<String, Long>> entry : _getDateSlot
                    .entrySet()) {
                Map<String, Long> map = entry.getValue();
                long day = map.get("beginTime");
//			for (long day = startDate; day < endDate;) {
                template.setDay(day);
                day += 3600 * 24;
                List<DBObject> dbos = mongoService.findDBObjects(template, query, null, -1);
                // System.out.println("dbos is " + dbos);
                if (dbos != null) {
                    for (DBObject dbo : dbos) {
                        DAMilages dam = new DAMilages(dbo);
                        milages.add(dam);
                    }
                }
//			}
            }
        }
        return milages;
    }

    @Override
    public List<DAOvertimeParkAlarm> getOvertimeParkAlarmRecords(List<Long> terminalIds, List<Long> areaIds,
                                                                 long startDate, long endDate, CommonParameter commonParameter) {
        // 2016.05.21新增方法实现——王景康
        if (endDate > startDate) {
            Map<String, Object> extraParameters = new HashMap<>();
            extraParameters.put("areaIds", areaIds);
            List<DBObject> result;
            try {
                boolean log = true;
                long time = System.currentTimeMillis();
                result = this.queryDBObjectsByTimeSegment(
                        Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark, terminalIds, startDate, endDate,
                        extraParameters, commonParameter);
                long cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("滞留超时查询耗时（ms）： " + cost);
                }
                time += cost;
                List<DAOvertimeParkAlarm> alarms = convert2DAOvertimeParkAlarm(result);
                cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("滞留超时查询结果转换耗时（ms）： " + cost);
                }
                return alarms;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new ArrayList<DAOvertimeParkAlarm>(1);
        }
    }

    @Override
    public List<DAStaytimeParkAlarm> getStaytimeParkAlarmRecords(List<Long> terminalIds, List<Long> areaIds, long startDate, long endDate, CommonParameter commonParameter) {
        // 2016.05.21新增方法实现——王景康
        if (endDate > startDate) {
            Map<String, Object> extraParameters = new HashMap<>();
            extraParameters.put("areaIds", areaIds);
            List<DBObject> result;
            try {
                boolean log = true;
                long time = System.currentTimeMillis();
                result = this.queryDBObjectsByTimeSegment(
                        Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark, terminalIds, startDate, endDate,
                        extraParameters, commonParameter);
                long cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("区域停留时长查询耗时（ms）： " + cost);
                }
                time += cost;
                List<DAStaytimeParkAlarm> alarms = convert2DAStaytimeParkAlarm(result);
                cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("区域停留时长查询结果转换耗时（ms）： " + cost);
                }
                return alarms;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new ArrayList<DAStaytimeParkAlarm>(1);
        }
    }

    @Override
    public long getOvertimeParkAlarmRecordsCount(List<Long> terminalIds, List<Long> areaIds, long startDate,
                                                 long endDate) {
        // 2016.05.22新增方法实现——王景康
        if (endDate > startDate) {
            Map<String, Object> extraParameters = new HashMap<>();
            extraParameters.put("areaIds", areaIds);
            try {
                boolean log = true;
                long time = System.currentTimeMillis();
                long count = this.queryDBObjectsCountByTimeSegment(
                        Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark, terminalIds, startDate, endDate,
                        extraParameters);
                long cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("滞留超时总数查询耗时（ms）： " + cost);
                }
                return count;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public long getStaytimeParkAlarmRecordsCount(List<Long> terminalIds, List<Long> areaIds, long startDate,
                                                 long endDate) {
        // 2016.05.22新增方法实现——王景康
        if (endDate > startDate) {
            Map<String, Object> extraParameters = new HashMap<>();
            extraParameters.put("areaIds", areaIds);
            try {
                boolean log = true;
                long time = System.currentTimeMillis();
                long count = this.queryDBObjectsCountByTimeSegment(
                        Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark, terminalIds, startDate, endDate,
                        extraParameters);
                long cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("区域停留时长总数查询耗时（ms）： " + cost);
                }
                return count;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    @Override
    public List<DAFaultCodeAlarm> getFaultCodeAlarmRecords(List<Long> terminalIds, int spn, int fmi, long startDate,
                                                           long endDate, CommonParameter commonParameter) {
        // 2016.05.20新增方法实现——王景康
        if (endDate > startDate) {
            Map<String, Object> extraParameters = new HashMap<>();
            extraParameters.put("spn", spn);
            extraParameters.put("fmi", fmi);
            List<DBObject> result;
            try {
                boolean log = true;
                long time = System.currentTimeMillis();
                result = this.queryDBObjectsByTimeSegment(Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode,
                        terminalIds, startDate, endDate, extraParameters, commonParameter);
                long cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("故障码查询耗时（ms）： " + cost);
                }
                time += cost;
                List<DAFaultCodeAlarm> alarms = convert2DAFaultCodeAlarm(result);
                cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("故障码查询结果转换耗时（ms）： " + cost);
                }
                boolean testDisplay = false;
                if(testDisplay) {
                    for(DAFaultCodeAlarm alarm : alarms) {
                        this.echoTimeSeconds(alarm.getBeginDate());
                    }
                }
                return alarms;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return null;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new ArrayList<DAFaultCodeAlarm>(1);
        }
    }

    @Override
    public long getFaultCodeAlarmRecordsCount(List<Long> terminalIds, int spn, int fmi, long startDate, long endDate) {
        // 2016.05.22新增方法实现——王景康
        if (endDate > startDate) {
            Map<String, Object> extraParameters = new HashMap<>();
            extraParameters.put("spn", spn);
            extraParameters.put("fmi", fmi);
            try {
                boolean log = true;
                long time = System.currentTimeMillis();
                long count = this.queryDBObjectsCountByTimeSegment(Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode,
                        terminalIds, startDate, endDate, extraParameters);
                long cost = System.currentTimeMillis() - time;
                if(log) {
                    System.out.println("故障码总数查询耗时（ms）： " + cost);
                }
                return count;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return 0;
            }
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    private long queryDBObjectsCountByTimeSegment(String alarmType, List<Long> terminalIds, long startDate,
                                                  long endDate, Map<String, Object> extraParameters) throws UnsupportedEncodingException {
        if (!Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)
                && !Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType) && !Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
            throw new IllegalArgumentException("不支持该报警类型: " + alarmType);
        }
        // 2016.05.22新增方法实现——王景康
        // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
        Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(startDate, endDate);

        List<String> dateKeys = new LinkedList<>();
        for (String key : datesSlot.keySet()) {
            dateKeys.add(key);
        }
        // 降序排序
        Collections.sort(dateKeys, new Comparator<String>() {

            @Override
            public int compare(String str0, String str1) {
                return Integer.parseInt(str1) - Integer.parseInt(str0);
            }

        });

        // 不同的查询需要的参数略有区别
        List<Long> areaIds = null;
        int spn = -1, fmi = -1;
        if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)||Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
            areaIds = (List<Long>) (extraParameters.get("areaIds"));
        } else if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
            Integer spnObj = (Integer) extraParameters.get("spn"), fmiObj = (Integer) extraParameters.get("fmi");
            spn = (spnObj != null) ? spnObj.intValue() : -1;
            fmi = (fmiObj != null) ? fmiObj.intValue() : -1;
        }

        long count = 0;
        for (String dateKey : dateKeys) {
            Map<String, Long> dateSlot = datesSlot.get(dateKey);

            Long beginTime = (Long) dateSlot.get("beginTime");
            Long endTime = (Long) dateSlot.get("endTime");
            if (beginTime == null || endTime == null) {
                continue;
            } else {
                beginTime = Math.max(startDate, beginTime);
                endTime = Math.min(endDate, endTime);
            }

            String collectionName = alarmType + "_" + dateKey;
            if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)) {
                try {
                    count += this.getOvertimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, beginTime,
                            endTime - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
                try {
                    count += this.getFaultCodeAlarmsCount(collectionName, terminalIds, spn, fmi, dateKey, beginTime,
                            endTime - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
                try {
                    count += this.getStaytimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, beginTime,
                            endTime - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return count;
    }

    @SuppressWarnings("unchecked")
    private List<DBObject> queryDBObjectsByTimeSegment(String alarmType, List<Long> terminalIds, long startDate,
                                                       long endDate, Map<String, Object> extraParameters, CommonParameter commonParameter)
            throws UnsupportedEncodingException, UnknownHostException {
        if (!Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)
                && !Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType) && !Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
            throw new IllegalArgumentException("不支持该报警类型: " + alarmType);
        }
        // 2016.05.22新增方法实现——王景康
        List<DBObject> result = new LinkedList<DBObject>();
        // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
        Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(startDate, endDate);

        List<String> dateKeys = new LinkedList<>();
        for (String key : datesSlot.keySet()) {
            dateKeys.add(key);
        }
        // 降序排序
        Collections.sort(dateKeys, new Comparator<String>() {

            @Override
            public int compare(String str0, String str1) {
                return Integer.parseInt(str1) - Integer.parseInt(str0);
            }

        });

        // 不同的查询需要的参数略有区别
        List<Long> areaIds = null;
        int spn = -1, fmi = -1;
        if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)||Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
            areaIds = (List<Long>) (extraParameters.get("areaIds"));
        } else if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
            Integer spnObj = (Integer) extraParameters.get("spn"), fmiObj = (Integer) extraParameters.get("fmi");
            spn = (spnObj != null) ? spnObj.intValue() : -1;
            fmi = (fmiObj != null) ? fmiObj.intValue() : -1;
        }
        // 遍历获取结果
        int prevCount = 0;// 用于分页查询
        for (String dateKey : dateKeys) {
            Map<String, Long> dateSlot = datesSlot.get(dateKey);

            Long beginTime = (Long) dateSlot.get("beginTime");
            Long endTime = (Long) dateSlot.get("endTime");
            if (beginTime == null || endTime == null) {
                continue;
            } else {
                beginTime = Math.max(startDate, beginTime);
                endTime = Math.min(endDate, endTime);
            }

            String collectionName = alarmType + "_" + dateKey;
            BasicDBObject orderBy = new BasicDBObject();
            if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
                orderBy.put("begin", -1);
            } else if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)||Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
                orderBy.put("HA", -1);
            }

            if (commonParameter.isMultipage()) {
                // 分片时长，单位：分钟
                // 数据量巨大，需要精细分片，但是间隔不可设置过小
                int defaultSegmentUnit = 10;
                // 设置最大分片  分钟*小时*天
                int maxSegmentUnit=60*24*3;
                int segmentUnit = defaultSegmentUnit;
                // 按开始时间分片
                // 由于查询结果按时间倒序排序，因此从时间条件的尾部取数据
                long end = endTime.longValue();
                long begin = 0;
                while (end > beginTime && result.size() < commonParameter.getPageSize()) {
                    int stepSeconds = segmentUnit * 60;
                    begin = Math.max(beginTime, end - stepSeconds);
                    //System.out.println("故障码本次查询时间段:"+segmentUnit);
                    // 数据位置区间
                    int count = 0;
                    if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)) {
                        try {
                            count = this.getOvertimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, begin,
                                    end - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
                        try {
                            count = this.getStaytimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, begin,
                                    end - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
                        try {
                            count = this.getFaultCodeAlarmsCount(collectionName, terminalIds, spn, fmi, dateKey, begin,
                                    end - 1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    int[] range = { prevCount, prevCount + count };
                    // 目标位置区间
                    int p = commonParameter.getPageIndex() * commonParameter.getPageSize();
                    int[] targetRange = { p, p + commonParameter.getPageSize() };

                    if (range[1] <= targetRange[0]) {
                        prevCount += count;
                        end -= stepSeconds;
                        // 本次未查询到有效数据，扩大查询范围
                        segmentUnit = Math.min(segmentUnit << 2, maxSegmentUnit);
                        continue;
                    } else if (range[0] >= targetRange[1]) {
                        break;
                    } else {
                        // 区间有交叉，存在符合条件的结果
                        prevCount += count;
                        // 查询
                        BasicDBObject query = null;
                        if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)) {
                            query = this.buildOvertimeParkAlarmQuery(terminalIds, areaIds, begin, end - 1);
                        } else if(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)){
                            query = this.buildStaytimeParkAlarmQuery(terminalIds, areaIds, begin, end - 1);
                        }else if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
                            query = this.buildFaultCodeAlarmQuery(terminalIds, spn, fmi, begin, end - 1);
                        }

                        DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS,
                                collectionName);
                        // 计算查询结果的提取范围
                        int minIndex = Math.max(targetRange[0], range[0]) - range[0];
                        int maxIndex = Math.min(targetRange[1], range[1]) - range[0];
                        // 最大允许本次查询的记录数
                        //int maxQuery = maxIndex - minIndex;
                        int maxQuery = maxIndex;
                        //System.out.println("故障码本次查询提取记录数:"+maxQuery);
                        int addedThisTime = 0;
                        if(maxQuery > 0) {
                            List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, maxQuery);
                            if (dbObjects != null && dbObjects.size() > 0) {
                                maxIndex = Math.min(maxIndex, minIndex + dbObjects.size());
                                for (int i = minIndex; i < maxIndex; i++) {
                                    result.add(dbObjects.get(i));
                                    addedThisTime++;
                                }
                            }
                        }
                        // 重新评估 segmentUnit，假定数据在时间线上是均匀分布的
                        if(addedThisTime > 0) {
                            // 本次完成率
                            double percent = (double) addedThisTime / (double) commonParameter.getPageSize();
                            // 总完成率
                            double totalPercent = (double) result.size() / (double) commonParameter.getPageSize();
                            // 剩余百分比
                            double leftPercent = Math.max(0, 1 - totalPercent);
                            if(leftPercent <= 0) {
                                segmentUnit = defaultSegmentUnit;
                            } else {
                                segmentUnit *= (leftPercent / percent);
                                segmentUnit = Math.min(segmentUnit, maxSegmentUnit);
                            }
                        } else {
                            // 本次未查询到有效数据，扩大查询范围
                            segmentUnit = Math.min(segmentUnit << 2, maxSegmentUnit);
                        }
                        end -= stepSeconds;
                    }
                }
            } else {
                // 不必分片，查询当前collection中所有符合条件的数据
                BasicDBObject query = null;
                if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmOvertimePark.equals(alarmType)) {
                    query = this.buildOvertimeParkAlarmQuery(terminalIds, areaIds, beginTime, endTime - 1);
                }if (Constant.PropertiesKey.AlarmTypeTableMapping.AlarmStaytimePark.equals(alarmType)) {
                    query = this.buildStaytimeParkAlarmQuery(terminalIds, areaIds, beginTime, endTime - 1);
                }else if (Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode.equals(alarmType)) {
                    query = this.buildFaultCodeAlarmQuery(terminalIds, spn, fmi, beginTime, endTime - 1);
                }
                DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
                List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                if (dbObjects != null && dbObjects.size() > 0) {
                    result.addAll(dbObjects);
                }
            }
        }

        return result;
    }

    private BasicDBObject buildOvertimeParkAlarmQuery(List<Long> terminalIds, List<Long> areaIds, Object beginTime,
                                                      Object endTime) {
        BasicDBObject query = new BasicDBObject();

        BasicDBObject timeCondition = null;
        if (beginTime != null && Long.parseLong(beginTime.toString()) > 0) {
            timeCondition = new BasicDBObject("$gte", beginTime);
        }
        if (endTime != null && Long.parseLong(endTime.toString()) > 0) {
            if(timeCondition == null) {
                timeCondition = new BasicDBObject("$lte", endTime);
            } else {
                timeCondition.append("$lte", endTime);
            }
        }
        if(timeCondition != null) {
            query.put("HA", timeCondition);
        }

        BasicDBList tidList = new BasicDBList();
        for (long tid : terminalIds) {
            tidList.add(tid);
        }
        BasicDBObject inTids = new BasicDBObject("$in", tidList);
        query.put("AA", inTids);

        if (areaIds != null && areaIds.size() > 0) {
            BasicDBList areaIdList = new BasicDBList();
            for (Long areaId : areaIds) {
                areaIdList.add(areaId);
            }
            BasicDBObject inAreaIds = new BasicDBObject("$in", areaIdList);
            query.put("BA", inAreaIds);
        }
        return query;
    }

    private BasicDBObject buildStaytimeParkAlarmQuery(List<Long> terminalIds, List<Long> areaIds, Object beginTime,
                                                      Object endTime) {
        BasicDBObject query = new BasicDBObject();

        BasicDBObject timeCondition = null;
        if (beginTime != null && Long.parseLong(beginTime.toString()) > 0) {
            timeCondition = new BasicDBObject("$gte", beginTime);
        }
        if (endTime != null && Long.parseLong(endTime.toString()) > 0) {
            if(timeCondition == null) {
                timeCondition = new BasicDBObject("$lte", endTime);
            } else {
                timeCondition.append("$lte", endTime);
            }
        }
        if(timeCondition != null) {
            query.put("HA", timeCondition);
        }

        BasicDBList tidList = new BasicDBList();
        for (long tid : terminalIds) {
            tidList.add(tid);
        }
        BasicDBObject inTids = new BasicDBObject("$in", tidList);
        query.put("AA", inTids);

        if (areaIds != null && areaIds.size() > 0) {
            BasicDBList areaIdList = new BasicDBList();
            for (Long areaId : areaIds) {
                areaIdList.add(areaId);
            }
            BasicDBObject inAreaIds = new BasicDBObject("$in", areaIdList);
            query.put("BA", inAreaIds);
        }
        return query;
    }

    private int getOvertimeParkAlarmsCount(String collectionName, List<Long> terminalIds, List<Long> areaIds,
                                           String dateKey, long beginTime, long endTime) throws Exception {
        int preciseQueryRange = 24;// 精确查询时长，单位：小时
        int terminalCount=1000;//超过1000时开启模糊查询
        try {
            preciseQueryRange=new Integer(Configuration.getString("overtimePreciseQueryRange"));
            terminalCount=new Integer(Configuration.getString("TerminalCount"));
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("配置文件ServerConfig.properties中缺少配置参数：overtimePreciseQueryRange，TerminalCount");
        }

        // 读配置文件
        // TODO
        long preciseOffset = preciseQueryRange * 3600;
        boolean estimateEnabled = true;// 是否启用估算
        boolean estimate = false;// 本次查询是否需要估算
        // 查询时间跨度，单位：秒
        long offset = endTime - beginTime;
        if(estimateEnabled && offset > preciseOffset&&terminalIds.size()>=terminalCount) {
            estimate = true;
        }
        if(estimate) {
            int c1 = this.getOvertimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, beginTime, beginTime + preciseOffset);
            int c2 = this.getOvertimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, endTime - preciseOffset, endTime);
            double c = (double) (c1 + c2) / 2.0;
            return (int) Math.round(c * (double) offset / (double) preciseOffset);
        } else {
            StringBuilder cacheKey = new StringBuilder("");
            cacheKey.append(this.getClass().getName()).append("__getOvertimeParkAlarmsCount");
            cacheKey.append("_").append(collectionName);
            cacheKey.append("_").append(terminalIds.toString());
            if (areaIds != null) {
                cacheKey.append("_").append(areaIds.toString());
            }
            cacheKey.append("_").append(dateKey);
            cacheKey.append("_").append(beginTime);
            cacheKey.append("_").append(endTime);

            Hash hash = new DefaultHash();
            String key = QUERY_CACHE_KEY_PREFIX + hash.computeMd5String(cacheKey.toString(), "utf-8");
            ReportQueryCache.getInstance().add(key);

            // Redis IO
            boolean isBroken = false;
            Jedis jedis = null;
            try {
                boolean cacheEnabled = true;
                // jedis = RedisClusters.getInstance().getJedis();
                jedis = RedisStatic.getInstance().getJedis();
                String value = jedis.get(key);
                if (value != null && cacheEnabled) {
                    return Integer.parseInt(value);
                } else {
                    // 查询并保存
                    BasicDBObject query = this.buildOvertimeParkAlarmQuery(terminalIds, areaIds, beginTime, endTime);
                    DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
                    int count = mongoService.queryCountByCondition(collection, query);
                    jedis.set(key, String.valueOf(count));
                    // 5分钟后过期
                    jedis.expireAt(key, System.currentTimeMillis() + 300000);
                    return count;
                }
            } catch (Exception e) {
                isBroken = true;
                throw e;
            } finally {
                RedisStatic.getInstance().release(jedis, isBroken);
            }
        }
    }

    private int getStaytimeParkAlarmsCount(String collectionName, List<Long> terminalIds, List<Long> areaIds,
                                           String dateKey, long beginTime, long endTime) throws Exception {
        int preciseQueryRange = 24;// 精确查询时长，单位：小时
        int terminalCount=1000;//超过1000时开启模糊查询
        try {
            preciseQueryRange=new Integer(Configuration.getString("staytimePreciseQueryRange"));
            terminalCount=new Integer(Configuration.getString("staytimeTerminalCount"));
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("配置文件ServerConfig.properties中缺少配置参数：staytimePreciseQueryRange，staytimeTerminalCount");
        }

        // 读配置文件
        // TODO
        long preciseOffset = preciseQueryRange * 3600;
        boolean estimateEnabled = true;// 是否启用估算
        boolean estimate = false;// 本次查询是否需要估算
        // 查询时间跨度，单位：秒
        long offset = endTime - beginTime;
        if(estimateEnabled && offset > preciseOffset&&terminalIds.size()>=terminalCount) {
            estimate = true;
        }
        if(estimate) {
            int c1 = this.getStaytimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, beginTime, beginTime + preciseOffset);
            int c2 = this.getStaytimeParkAlarmsCount(collectionName, terminalIds, areaIds, dateKey, endTime - preciseOffset, endTime);
            double c = (double) (c1 + c2) / 2.0;
            return (int) Math.round(c * (double) offset / (double) preciseOffset);
        } else {
            StringBuilder cacheKey = new StringBuilder("");
            cacheKey.append(this.getClass().getName()).append("__getStaytimeParkAlarmsCount");
            cacheKey.append("_").append(collectionName);
            cacheKey.append("_").append(terminalIds.toString());
            if (areaIds != null) {
                cacheKey.append("_").append(areaIds.toString());
            }
            cacheKey.append("_").append(dateKey);
            cacheKey.append("_").append(beginTime);
            cacheKey.append("_").append(endTime);

            Hash hash = new DefaultHash();
            String key = QUERY_CACHE_KEY_PREFIX + hash.computeMd5String(cacheKey.toString(), "utf-8");
            ReportQueryCache.getInstance().add(key);

            // Redis IO
            boolean isBroken = false;
            Jedis jedis = null;
            try {
                boolean cacheEnabled = true;
                // jedis = RedisClusters.getInstance().getJedis();
                jedis = RedisStatic.getInstance().getJedis();
                String value = jedis.get(key);
                if (value != null && cacheEnabled) {
                    return Integer.parseInt(value);
                } else {
                    // 查询并保存
                    BasicDBObject query = this.buildOvertimeParkAlarmQuery(terminalIds, areaIds, beginTime, endTime);
                    DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
                    int count = mongoService.queryCountByCondition(collection, query);
                    jedis.set(key, String.valueOf(count));
                    // 5分钟后过期
                    jedis.expireAt(key, System.currentTimeMillis() + 300000);
                    return count;
                }
            } catch (Exception e) {
                isBroken = true;
                throw e;
            } finally {
                RedisStatic.getInstance().release(jedis, isBroken);
            }
        }
    }

    private BasicDBObject buildFaultCodeAlarmQuery(List<Long> terminalIds, int spn, int fmi, Object beginTime,
                                                   Object endTime) {
        BasicDBObject query = new BasicDBObject();
        query.put("begin", new BasicDBObject("$gte", beginTime).append("$lte", endTime));

        BasicDBList tidList = new BasicDBList();
        for (long tid : terminalIds) {
            tidList.add(tid);
        }
        BasicDBObject inTids = new BasicDBObject("$in", tidList);
        query.put("tId", inTids);
        if (spn >= 0) {
            query.put("spn", spn);
        }
        if (fmi >= 0) {
            query.put("fmi", fmi);
        }
        return query;
    }

    private int getFaultCodeAlarmsCount(String collectionName, List<Long> terminalIds, int spn, int fmi, String dateKey,
                                        long beginTime, long endTime) throws Exception {
        int preciseQueryRange = 24;// 精确查询时长，单位：小时
        int terminalCount=1000;//超过1000时开启模糊查询
        try {
            preciseQueryRange=new Integer(Configuration.getString("overtimePreciseQueryRange"));
            terminalCount=new Integer(Configuration.getString("TerminalCount"));
        } catch (Exception e) {
            // TODO: handle exception
            logger.error("配置文件ServerConfig.properties中缺少配置参数：overtimePreciseQueryRange，TerminalCount");
        }
        // 读配置文件
        // TODO
        long preciseOffset = preciseQueryRange * 3600;
        boolean estimateEnabled = true;// 是否启用估算
        boolean estimate = false;// 本次查询是否需要估算
        // 查询时间跨度，单位：秒
        long offset = endTime - beginTime;
        if(estimateEnabled && offset > preciseOffset&&terminalIds.size()>=terminalCount) {
            estimate = true;
        }
        if(estimate) {
            int c1 = this.getFaultCodeAlarmsCount(collectionName, terminalIds, spn, fmi, dateKey, beginTime, beginTime + preciseOffset);
            int c2 = this.getFaultCodeAlarmsCount(collectionName, terminalIds, spn, fmi, dateKey, endTime - preciseOffset, endTime);
            double c = (double) (c1 + c2) / 2.0;
            return (int) Math.round(c * (double) offset / (double) preciseOffset);
        } else {
            StringBuilder cacheKey = new StringBuilder("");
            cacheKey.append(this.getClass().getName()).append("__getFaultCodeAlarmsCount");
            cacheKey.append("_").append(collectionName);
            cacheKey.append("_").append(terminalIds.toString());
            if (spn >= 0) {
                cacheKey.append("_").append(spn);
            }
            if (fmi >= 0) {
                cacheKey.append("_").append(fmi);
            }
            cacheKey.append("_").append(dateKey);
            cacheKey.append("_").append(beginTime);
            cacheKey.append("_").append(endTime);

            Hash hash = new DefaultHash();
            String key = QUERY_CACHE_KEY_PREFIX + hash.computeMd5String(cacheKey.toString(), "utf-8");
            ReportQueryCache.getInstance().add(key);

            // Redis IO
            boolean isBroken = false;
            Jedis jedis = null;
            try {
                boolean cacheEnabled = true;
                // jedis = RedisClusters.getInstance().getJedis();
                jedis = RedisStatic.getInstance().getJedis();
                String value = jedis.get(key);
                if (value != null && cacheEnabled) {
                    return Integer.parseInt(value);
                } else {
                    // 查询并保存
                    BasicDBObject query = this.buildFaultCodeAlarmQuery(terminalIds, spn, fmi, beginTime, endTime);
                    DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
                    int count = mongoService.queryCountByCondition(collection, query);
                    jedis.set(key, String.valueOf(count));
                    // 5分钟后过期
                    jedis.expireAt(key, System.currentTimeMillis() + 300000);
                    return count;
                }
            } catch (Exception e) {
                isBroken = true;
                throw e;
            } finally {
                RedisStatic.getInstance().release(jedis, isBroken);
            }
        }
    }

    @Override
    public List<DAFaultCodeAlarm> queryFaultCodeAlarm(List<Long> terminalIds, int spn, int fmi, long startDate,
                                                      long endDate, CommonParameter commonParameter) {
        List<DBObject> result = new ArrayList<DBObject>();
        // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
        Map<String, Map<String, Long>> listDates = _get_dateSlot(startDate, endDate);
        for (Entry entry : listDates.entrySet()) {
            Map map = (Map) entry.getValue();
            Object beginTimeMonth = map.get("beginTime");
            Object endTimeMonth = map.get("endTime");
            String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.FaultCode + "_" + entry.getKey();
            BasicDBObject query = new BasicDBObject();
            BasicDBList tidList = new BasicDBList();
            for (long tid : terminalIds) {
                tidList.add(tid);
            }
            BasicDBObject inTids = new BasicDBObject("$in", tidList);
            query.put("tId", inTids);
            if (0 != spn)
                query.put("spn", spn);
            if (0 != fmi)
                query.put("fmi", fmi);
            // BasicDBObject gt = new BasicDBObject("$gte", beginTimeMonth);
            // BasicDBObject lt = new BasicDBObject("$lte", endTimeMonth);
            query.put("end", new BasicDBObject("$gte", beginTimeMonth).append("$lte", endTimeMonth));
            // query.put("beginDate", lt);
            List<DBObject> dbObjects = mongoService.queryByCondition(collectionName, query);
            if (dbObjects != null) {
                result.addAll(dbObjects);
            }
        }
        List<DAFaultCodeAlarm> alarms = convert2DAFaultCodeAlarm(result);
        return alarms;
    }

    private List<DAFaultCodeAlarm> convert2DAFaultCodeAlarm(List<DBObject> dbObjects) {
        List<DAFaultCodeAlarm> result = new LinkedList<DAFaultCodeAlarm>();
        if (null != dbObjects && dbObjects.size() > 0) {
            for (DBObject object : dbObjects) {
                DAFaultCodeAlarm alarm = new DAFaultCodeAlarm();
                alarm.dbObjectToBean(object);
                result.add(alarm);
            }
        }
        return result;
    }

    private List<DAOvertimeParkAlarm> convert2DAOvertimeParkAlarm(List<DBObject> dbObjects) {
        List<DAOvertimeParkAlarm> result = new LinkedList<>();
        if (null != dbObjects && dbObjects.size() > 0) {
            for (DBObject object : dbObjects) {
                DAOvertimeParkAlarm alarm = new DAOvertimeParkAlarm();
                alarm.dbObjectToBean(object);
                result.add(alarm);
            }
        }
        return result;
    }

    private List<DAStaytimeParkAlarm> convert2DAStaytimeParkAlarm(List<DBObject> dbObjects) {
        List<DAStaytimeParkAlarm> result = new LinkedList<>();
        if (null != dbObjects && dbObjects.size() > 0) {
            for (DBObject object : dbObjects) {
                DAStaytimeParkAlarm alarm = new DAStaytimeParkAlarm();
                alarm.dbObjectToBean(object);
                result.add(alarm);
            }
        }
        return result;
    }

    /**
     * 2016年5月20日添加——王景康
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private Map<String, Map<String, Long>> getDatesSlot(long beginTime, long endTime) {
        Map<String, Map<String, Long>> slot = new HashMap<>();
        Calendar begin = Calendar.getInstance(), end = Calendar.getInstance();
        begin.setTimeInMillis(beginTime * 1000);
        end.setTimeInMillis(endTime * 1000);

        int beginMonthValue = this.calculateMonthValue(begin);
        int endMonthValue = this.calculateMonthValue(end);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyMM");
        Calendar calendar = Calendar.getInstance();
        for (int i = beginMonthValue; i <= endMonthValue; i++) {
            int year = i / 12;
            int month = i % 12;
            calendar.setTimeInMillis(0);
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.HOUR_OF_DAY, 0);

            String key = dateFormat.format(calendar.getTime());
            long min = Math.max(begin.getTimeInMillis(), calendar.getTimeInMillis()) / 1000;
            calendar.set(Calendar.MONTH, month + 1);
            long max = Math.min(calendar.getTimeInMillis() / 1000 - 1, end.getTimeInMillis() / 1000);

            Map<String, Long> value = new HashMap<>();
            value.put("beginTime", min);
            value.put("endTime", max);

            slot.put(key, value);
        }

        return slot;
    }

    private int calculateMonthValue(Calendar calendar) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        return (year * 12) + month;
    }

    private Map<String, Map<String, Long>> _get_dateSlot(long beginTime, long endTime) {
        TreeMap<String, Map<String, Long>> result = new TreeMap<String, Map<String, Long>>();

        // 日期转换 yyyy-mm-ss
        Date beginDate = new Date(beginTime);
        Date endDate = new Date(endTime);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String getBeginMonth = formatter.format(beginDate);
        String getendMonth = formatter.format(endDate);
        // 计算月份差
        long monthDiff = DateDiffUtil.getMonthDiff(getBeginMonth, getendMonth);
        List<String> monthBetween = DateDiffUtil.getMonthBetween(getBeginMonth, getendMonth);
        if (monthDiff == 0) {

            // 是否是当前月
            Map<String, Long> timeCondition = new HashMap<String, Long>();
            timeCondition.put("beginTime", beginTime / 1000);
            timeCondition.put("endTime", endTime / 1000);
            result.put(monthBetween.get(0), timeCondition);

        } else {
            String startMonth = (new SimpleDateFormat("yyMM")).format(beginDate);
            String endMonth = (new SimpleDateFormat("yyMM")).format(endDate);
            for (String month : monthBetween) {
                Map<String, Long> timeCondition = new HashMap<String, Long>();
                if (month.equals(startMonth)) {
                    // 起始月
                    timeCondition.put("beginTime", beginTime / 1000);
                    timeCondition.put("endTime", DateDiffUtil.getEndDayOfMonth(month));
                } else if (month.equals(endMonth)) {
                    // 终止月
                    timeCondition.put("beginTime", DateDiffUtil.getBeginDayOfMonth(month));
                    timeCondition.put("endTime", endTime / 1000);
                } else {
                    // 起止中间
                    timeCondition.put("beginTime", DateDiffUtil.getBeginDayOfMonth(month));
                    timeCondition.put("endTime", DateDiffUtil.getEndDayOfMonth(month));
                }
                result.put(month, timeCondition);
            }
        }
        return result;
    }
    @Override
    public List<LocationData> getTerminalLocationData(long terminalId,long queryDate){
        Date date = new Date(queryDate*1000);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String day = formatter.format(date);
        List<LocationData> list = new ArrayList<LocationData>();
        DBCollection collection;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION,
                    LCMongo.Collection.LC_GPS_DATA_ENTITY + day.substring(2,6));
            BasicDBObject query = new BasicDBObject();
            query.put("tId", terminalId);
            query.put("day", day);
            BasicDBObject orderBy = new BasicDBObject();
            orderBy.put("day", -1);
            List<DBObject> dbObjects = mongoService.queryByCondition(collection, query,orderBy,-1);
            List<GpsDetailedEntityDB> resultEntities = new LinkedList<>();
            if(dbObjects.size()>0&&dbObjects!=null){
                for (DBObject dbObject : dbObjects) {
                    @SuppressWarnings("unchecked")
                    List<DBObject> dataList = (List<DBObject>) dbObject.get("dataList");
                    long dbTid = (long) dbObject.get("tId");
                    if (dataList != null && terminalId == dbTid) {
                        for (DBObject data : dataList) {
                            resultEntities.add(new GpsDetailedEntityDB(data, terminalId));
                        }
                    }
                }
            }
            if (resultEntities != null) {
                // 查询结果转换
                for (GpsDetailedEntityDB entity : resultEntities) {
                    try {
                        list.add(LocationData.parseFrom(entity.getData()));
                    } catch (InvalidProtocolBufferException e) {
                        e.printStackTrace();
                        continue;
                    }
                }
            }
            return list;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<DAStagnationTimeoutAlarm> getStagnationTimeoutRecords(
            List<Long> terminalID, long startDate, long endDate,
            CommonParameter commonParameter){
        if (endDate > startDate) {
            List<DBObject> result = new LinkedList<DBObject>();
            try {
                // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
                Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(startDate, endDate);
                List<String> dateKeys = new LinkedList<>();
                for (String key : datesSlot.keySet()) {
                    dateKeys.add(key);
                }
                // 降序排序
                Collections.sort(dateKeys, new Comparator<String>() {
                    @Override
                    public int compare(String str0, String str1) {
                        return Integer.parseInt(str1) - Integer.parseInt(str0);
                    }
                });
                // 遍历获取结果
                int prevCount = 0;// 用于分页查询
                for (String dateKey : dateKeys) {
                    Map<String, Long> dateSlot = datesSlot.get(dateKey);

                    Long beginTime = (Long) dateSlot.get("beginTime");
                    Long endTime = (Long) dateSlot.get("endTime");
                    if (beginTime == null || endTime == null) {
                        continue;
                    } else {
                        beginTime = Math.max(startDate, beginTime);
                        endTime = Math.min(endDate, endTime);
                    }
                    String collectionName = "StagnationTimeout_" + dateKey;
                    BasicDBObject orderBy = new BasicDBObject();
                    orderBy.put("begin", -1);
                    if (commonParameter.isMultipage()) {
                        // 分片时长，单位：分钟
                        // 数据量巨大，需要精细分片，但是间隔不可设置过小
                        int segmentUnit = 10;
                        // 按开始时间分片
                        // 由于查询结果按时间倒序排序，因此从时间条件的尾部取数据
                        long end = endTime.longValue();
                        long begin = 0;
                        while (end > beginTime && result.size() < commonParameter.getPageSize()) {
                            int stepSeconds = segmentUnit * 60;
                            begin = Math.max(beginTime, end - stepSeconds);
                            // 数据位置区间
                            int count = 0;
                            try {
                                count = this.getStagnationTimeoutCount(collectionName, terminalID,dateKey, begin,
                                        end - 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            int[] range = { prevCount, prevCount + count };
                            // 目标位置区间
                            int p = commonParameter.getPageIndex() * commonParameter.getPageSize();
                            int[] targetRange = { p, p + commonParameter.getPageSize() };

                            if (range[1] <= targetRange[0]) {
                                prevCount += count;
                                end -= stepSeconds;
                                // 扩大查询范围
                                segmentUnit *= 4;
                                continue;
                            } else if (range[0] >= targetRange[1]) {
                                break;
                            } else {
                                // 区间有交叉，存在符合条件的结果
                                prevCount += count;
                                // 查询
                                BasicDBObject query = null;
                                query = this.buildStagnationTimeoutQuery(terminalID,begin, end - 1);

                                DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS,
                                        collectionName);
                                List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                                int addedThisTime = 0;
                                if (dbObjects != null && dbObjects.size() > 0) {
                                    // 计算查询结果的提取范围
                                    int minIndex = Math.max(targetRange[0], range[0]) - range[0];
                                    int maxIndex = Math.min(targetRange[1], range[1]) - range[0];
                                    maxIndex = Math.min(maxIndex, minIndex + dbObjects.size());
                                    for (int i = minIndex; i < maxIndex; i++) {
                                        result.add(dbObjects.get(i));
                                        addedThisTime++;
                                    }
                                }
                                // 重新评估 segmentUnit，假定数据在时间线上是均匀分布的
                                if(addedThisTime > 0) {
                                    int left = commonParameter.getPageSize() - result.size();
                                    double scale = (double) left / (double) addedThisTime;
                                    segmentUnit = Math.max((int) (segmentUnit * scale), 1);
                                } else {
                                    // 本次未查询到有效数据，扩大查询范围
                                    segmentUnit *= 4;
                                }
                                end -= stepSeconds;
                            }
                        }
                    }else{
                        // 不必分片，查询当前collection中所有符合条件的数据
                        BasicDBObject query = null;
                        query = this.buildStagnationTimeoutQuery(terminalID, beginTime, endTime - 1);
                        DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
                        List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                        if (dbObjects != null && dbObjects.size() > 0) {
                            result.addAll(dbObjects);
                        }
                    }
                }
                List<DAStagnationTimeoutAlarm> alarms = convert2StagnationTimeoutAlarm(result);
                return alarms;
            } catch (UnknownHostException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return new ArrayList<DAStagnationTimeoutAlarm>(1);
        }
    }

    @Override
    public long getStagnationTimeoutRecordsCount(List<Long> terminalID,
                                                 long startDate, long endDate) {
        if (endDate > startDate) {
            // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
            Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(startDate, endDate);
            List<String> dateKeys = new LinkedList<>();
            for (String key : datesSlot.keySet()) {
                dateKeys.add(key);
            }
            // 降序排序
            Collections.sort(dateKeys, new Comparator<String>() {
                @Override
                public int compare(String str0, String str1) {
                    return Integer.parseInt(str1) - Integer.parseInt(str0);
                }
            });
            long count = 0;
            for (String dateKey : dateKeys) {
                Map<String, Long> dateSlot = datesSlot.get(dateKey);
                Long beginTime = (Long) dateSlot.get("beginTime");
                Long endTime = (Long) dateSlot.get("endTime");
                if (beginTime == null || endTime == null) {
                    continue;
                } else {
                    beginTime = Math.max(startDate, beginTime);
                    endTime = Math.min(endDate, endTime);
                }
                String collectionName = "StagnationTimeout_" + dateKey;
                try {
                    count += this.getStagnationTimeoutCount(collectionName, terminalID,dateKey, beginTime,
                            endTime - 1);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return count;
        }else{
            return 0;
        }
    }
    private List<DAStagnationTimeoutAlarm> convert2StagnationTimeoutAlarm(List<DBObject> dbObjects) {
        List<DAStagnationTimeoutAlarm> result = new LinkedList<>();
        if (null != dbObjects && dbObjects.size() > 0) {
            for (DBObject object : dbObjects) {
                DAStagnationTimeoutAlarm alarm = new DAStagnationTimeoutAlarm();
                alarm.dbObjectToBean(object);
                result.add(alarm);
            }
        }
        return result;
    }
    private BasicDBObject buildStagnationTimeoutQuery(List<Long> terminalIds,Object beginTime,
                                                      Object endTime) {
        BasicDBObject query = new BasicDBObject();

        BasicDBObject timeCondition = null;
        if (beginTime != null && Long.parseLong(beginTime.toString()) > 0) {
            timeCondition = new BasicDBObject("$gte", beginTime);
        }
        if (endTime != null && Long.parseLong(endTime.toString()) > 0) {
            if(timeCondition == null) {
                timeCondition = new BasicDBObject("$lte", endTime);
            } else {
                timeCondition.append("$lte", endTime);
            }
        }
        if(timeCondition != null) {
            query.put("begin", timeCondition);
        }

        BasicDBList tidList = new BasicDBList();
        for (long tid : terminalIds) {
            tidList.add(tid);
        }
        BasicDBObject inTids = new BasicDBObject("$in", tidList);
        query.put("tId", inTids);
//		query.put("tail", 1);
        return query;
    }
    private int getStagnationTimeoutCount(String collectionName, List<Long> terminalIds,
                                          String dateKey, long beginTime, long endTime) throws Exception {
        StringBuilder cacheKey = new StringBuilder("");
        cacheKey.append(this.getClass().getName()).append("__getStagnationTimeoutCount");
        cacheKey.append("_").append(collectionName);
        cacheKey.append("_").append(terminalIds.toString());
        cacheKey.append("_").append(dateKey);
        cacheKey.append("_").append(beginTime);
        cacheKey.append("_").append(endTime);

        Hash hash = new DefaultHash();
        String key = QUERY_CACHE_KEY_PREFIX + hash.computeMd5String(cacheKey.toString(), "utf-8");
        ReportQueryCache.getInstance().add(key);

        // Redis IO
        boolean isBroken = false;
        Jedis jedis = null;
        try {
            boolean cacheEnabled = true;
            // jedis = RedisClusters.getInstance().getJedis();
            jedis = RedisStatic.getInstance().getJedis();
            String value = jedis.get(key);
            if (value != null && cacheEnabled) {
                return Integer.parseInt(value);
            } else {
                // 查询并保存
                BasicDBObject query = this.buildStagnationTimeoutQuery(terminalIds, beginTime, endTime);
                DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
                int count = mongoService.queryCountByCondition(collection, query);
                jedis.set(key, String.valueOf(count));
                // 5分钟后过期
                jedis.expireAt(key, System.currentTimeMillis() + 300000);
                return count;
            }
        } catch (Exception e) {
            isBroken = true;
            throw e;
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
    }

    /**
     * 里程能耗实时计算
     */
    @Override
    public Mileages calculateMileageConsumption(long terminalId,
                                                long startDate, long endDate, long accessTocken) {
        // TODO Auto-generated method stub
        Mileages mileage = null;
        //第一步：根据时间范围查询轨迹，升序
        List<LocationData> list = new ArrayList<LocationData>();
        List<GpsDetailedEntityDB> entities = this.getGpsDetailedEntities(terminalId, startDate, endDate, accessTocken);
        if(entities != null && entities.size() > 0){
            for(GpsDetailedEntityDB db : entities){
                try {
                    list.add(LocationData.parseFrom(db.getData()));
                } catch (InvalidProtocolBufferException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
        Collections.sort(list, new Comparator<LocationData>() {
            @Override
            public int compare(LocationData arg0, LocationData arg1) {
                return (int) (arg0.getGpsDate() - arg1.getGpsDate());
            }
        });

        //第二步：根据轨迹，进行里程行驶统计
        if(list.size()>0){
            mileage = this.milExecute(terminalId, list);
        }
        return mileage;
    }
    //里程能耗实时计算     第一步：根据时间范围查询轨迹，升序
    private List<GpsDetailedEntityDB> getGpsDetailedEntities(long terminalId,
                                                             long startDate, long endDate, long accessTocken){
        List<GpsDetailedEntityDB> resultEntities = new LinkedList<>();
        // 标记是否需要继续在MongoDB中查询
        long todayEnd = this.getTodayEnd();
        long todayBegin = this.getTodayBegin();
        if(endDate > todayBegin){
            // 当天的数据从REDIS获取
            List<GpsDetailedEntityDB> todayData = this.getTodayGpsDetailedEntities(terminalId, Math.max(todayBegin, startDate),
                    Math.min(todayEnd, endDate), accessTocken);
            if (todayData != null) {
                resultEntities.addAll(todayData);
            }
        }
        // 历史数据从MongoDB获取
        if (todayBegin > startDate) {
            // 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
            Map<String, Map<String, Long>> datesSlot = this.getDatesSlot(startDate, todayBegin - 1);

            List<String> dateKeys = new LinkedList<>();
            for (String key : datesSlot.keySet()) {
                dateKeys.add(key);
            }
            // 降序排序
            Collections.sort(dateKeys, new Comparator<String>() {
                @Override
                public int compare(String str0, String str1) {
                    return Integer.parseInt(str1) - Integer.parseInt(str0);
                }
            });
            for (String dateKey : dateKeys) {
                DBCollection collection;
                try {
                    collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION,
                            LCMongo.Collection.LC_GPS_DATA_ENTITY + dateKey);
                    BasicDBObject orderBy = new BasicDBObject();
                    orderBy.put("day", -1);
                    // 不必分片，查询当前collection中所有符合条件的数据
                    BasicDBObject query = this.buildGpsDetailedEntitiesQuery(terminalId, startDate, endDate - 1);

                    List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, orderBy, -1);
                    if (dbObjects != null && dbObjects.size() > 0) {
                        try {
                            List<GpsDetailedEntityDB> temp = this.convert2GpsDetailedEntityDB(terminalId,
                                    dbObjects, startDate, endDate, false, -1);
                            resultEntities.addAll(temp);
                        } catch (InvalidProtocolBufferException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (UnknownHostException e1) {
                    e1.printStackTrace();
                }
            }
			/*// 排序
			Collections.sort(resultEntities, new Comparator<GpsDetailedEntityDB>() {

				@Override
				public int compare(GpsDetailedEntityDB arg0, GpsDetailedEntityDB arg1) {
					return (int) (arg0.getGpsTime() - arg1.getGpsTime());
				}

			});*/
        }
        return resultEntities;
    }
    private List<GpsDetailedEntityDB> getTodayGpsDetailedEntities(long terminalId,
                                                                  long startDate, long endDate, long accessTocken){
        List<GpsDetailedEntityDB> result = new LinkedList<GpsDetailedEntityDB>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        List<LocationData> list = locationRedisService.findNormalLocation(terminalId, calendar);
        GpsDetailedEntityDB detailedEntity = null;
        for (LocationData locationData : list) {
            long gt = locationData.getGpsDate();
            if (gt >= startDate && gt <= endDate) {
                detailedEntity = new GpsDetailedEntityDB();
                detailedEntity.set_id(terminalId);
                detailedEntity.setGpsTime(locationData.getGpsDate());
                detailedEntity.setData(locationData.toByteArray());
                result.add(detailedEntity);
            }
        }
		/*// 排序
		Collections.sort(result, new Comparator<GpsDetailedEntityDB>() {
			@Override
			public int compare(GpsDetailedEntityDB arg0, GpsDetailedEntityDB arg1) {
				return (int) (arg0.getGpsTime() - arg1.getGpsTime());
			}
		});*/
        return result;

    }
    private Mileages milExecute(long key , List<LocationData> data){
        Mileages mileages = null;
        logger.error("终端id为： "+key+" ,一共查询出   [   "+data.size()+"   ]条轨迹");
        for (int i = 0; i < data.size(); i++) {
            LocationData location = data.get(i);
            if ((location.getStatus() & Status.acc_VALUE) == 0) {
                continue;
            }
            if(location.getIsPatch()){ //补传不参与计算
                continue;
            }
            if (mileages == null) {
                mileages = new Mileages();
                mileages.setTerminalID(key);
                mileages.setBeginLat(location.getLatitude());
                mileages.setBeginLng(location.getLongitude());
                mileages.setEndLat(location.getLatitude());
                mileages.setEndLng(location.getLongitude());
                mileages.setStartDate(location.getGpsDate());
                mileages.setEndDate(location.getGpsDate());
                mileages.setGpsMileage(0);
                mileages.setBeginGpsMileage(location.getMileage());
                mileages.setEndGpsMileage(location.getMileage());
                for (LCVehicleStatusData.VehicleStatusData statusData : location.getStatusAddition().getStatusList()) {
                    switch (statusData.getTypes().getNumber()) {
                        case StatusType.mileage_VALUE:
                            if (statusData.getStatusValue() * 10 != 0) {
                                mileages.setBeginMileage(statusData.getStatusValue() * 10);
                                mileages.setEndMileage(statusData.getStatusValue() * 10);
                            }
                            break;
                        case StatusType.mileageDD_VALUE:
                            if (statusData.getStatusValue() * 10 != 0) {
                                //mileages.setPreMeMileage(statusData.getStatusValue() * 10);
                                mileages.setPreMeDate(location.getGpsDate());
                                mileages.setBeginMeMileage(statusData.getStatusValue() * 10);
                                mileages.setEndMeMileage(statusData.getStatusValue() * 10);
                            }
                            break;
                        case StatusType.totalFuelConsumption_VALUE:
                            if (statusData.getStatusValue() != 0) {
                                mileages.setPreOil(statusData.getStatusValue() / 100f);
                            }
                            break;
                        case StatusType.oilValue_VALUE:
                            if (statusData.getStatusValue() !=0){
                                mileages.setPreOilValue(statusData.getStatusValue() / 100f);
                            }
                            break;
                        case StatusType.integralFuelConsumption_VALUE:
                            if (statusData.getStatusValue() !=0){
                                mileages.setPreFuelOil(statusData.getStatusValue() / 100f);
                            }
                            break;
                    }
                }
                if (location.getBatteryPower() != 0) {
                    mileages.setPreElectric(location.getBatteryPower());
                }
            } else {
                mileages.setEndLat(location.getLatitude());
                mileages.setEndLng(location.getLongitude());
                mileages.setStaticDate((int) (location.getGpsDate() - mileages.getStartDate()));
                for (LCVehicleStatusData.VehicleStatusData statusData : location.getStatusAddition().getStatusList()) {
                    switch (statusData.getTypes().getNumber()) {
                        //StatusType.mileage_VALUE  ---> ECU里程、CAN里程、整车里程
                        case StatusType.mileage_VALUE:
                            long value = statusData.getStatusValue() * 10;
                            //2017-06-02 两处修改：① 整车里程为0  ②上下两个点的整车里程一样，不修改时间，保留上次正常点的结束时间
                            if(value == 0 || mileages.getEndMileage() == value){break;}
                            if (mileages.getEndMileage() != 0 && (value - mileages.getEndMileage() > 0) && checkMileage(
                                    mileages.getEndMileage(), value, mileages.getEndDate(), location.getGpsDate())) {
                                mileages.setCanMileage((value - mileages.getEndMileage()) + mileages.getCanMileage());
                            }
                            mileages.setEndMileage(value);
                            break;
                        //StatusType.mileageDD_VALUE --->仪表里程
                        case StatusType.mileageDD_VALUE:
                            long meter = statusData.getStatusValue() * 10;
                            if(meter == 0 || mileages.getEndMeMileage() == meter){break;}
                            if ((meter - mileages.getEndMeMileage() > 0) && mileages.getPreMeDate() != 0 && checkMileage(
                                    mileages.getEndMeMileage(), meter, mileages.getPreMeDate(), location.getGpsDate())) {
                                mileages.setMeterMileage((meter - mileages.getEndMeMileage()) + mileages.getMeterMileage());
                            }
                            mileages.setPreMeDate(location.getGpsDate());
                            mileages.setEndMeMileage(meter);
                            break;
                        //StatusType.totalFuelConsumption_VALUE --->ECU总油耗
                        case StatusType.totalFuelConsumption_VALUE:
                            float oil = statusData.getStatusValue() / 100f;
                            if (mileages.getPreOil() != 0 && oil - mileages.getPreOil() > 0 && oil - mileages.getPreOil() < 50) {
                                mileages.setOilConsumption(oil - mileages.getPreOil() + mileages.getOilConsumption());
                            }
                            mileages.setPreOil(oil);
                            break;
                        //StatusType.integralFuelConsumption_VALUE --> 积分总油耗
                        case StatusType.integralFuelConsumption_VALUE:
                            float fuelOil = statusData.getStatusValue() / 100f;
                            if (mileages.getPreFuelOil() != 0 && fuelOil - mileages.getPreFuelOil() > 0 && fuelOil - mileages.getPreFuelOil() < 50) {
                                mileages.setFuelOil(fuelOil - mileages.getPreFuelOil() + mileages.getFuelOil());
                            }
                            mileages.setPreFuelOil(fuelOil);
                            break;
                        //StatusType.oilValue_VALUE --》剩余油量
                        case StatusType.oilValue_VALUE:
                            float oilValue = statusData.getStatusValue() / 100f;
                            if (oilValue != 0 && mileages.getPreOilValue() - oilValue > 0){
                                mileages.setOilValue(mileages.getPreOilValue() - oilValue + mileages.getOilValue());
                            }
                            mileages.setPreOilValue(oilValue);
                            break;
                    }
                }
                if (location.getBatteryPower() != 0 && mileages.getPreElectric() - location.getBatteryPower() > 0) {
                    mileages.setElectricConsumption(
                            mileages.getPreElectric() - location.getBatteryPower() + mileages.getElectricConsumption());
                }
                mileages.setPreElectric(location.getBatteryPower());
                if (location.getMileage() > mileages.getEndGpsMileage() && checkMileage(mileages.getEndGpsMileage(),
                        location.getMileage(), mileages.getPreGpsDate(), location.getGpsDate())) {
                    mileages.setGpsMileage(mileages.getGpsMileage() + (location.getMileage() - mileages.getEndGpsMileage()));
                }
                mileages.setPreGpsDate(location.getGpsDate());
                mileages.setEndGpsMileage(location.getMileage());
                mileages.setEndDate(location.getGpsDate());

            }
        }
        logger.error(key+" 计算出的里程油耗值为： can里程="+mileages.getCanMileage()+" ,油耗="+mileages.getOilConsumption()+" ,终端里程="+mileages.getGpsMileage());
        return mileages;
    }
    /**
     * @param lastMileage
     *            Long 上个信息点的里程,单位米
     * @param currentMileage
     *            Long 当前信息点的里程,单位米
     * @param lastTime
     *            Long 上个信息点的时间,单位秒
     * @param currentTime
     *            Long 当前信息点的时间,单位秒
     * @return Long 行驶里程
     */
    private boolean checkMileage(long lastMileage, long currentMileage, Long lastTime, Long currentTime) {
        // 根据里程+时间计算出速度
        long differTime = currentTime - lastTime;
        long differMileage = currentMileage - lastMileage;
        double speed = (differMileage / (double) differTime) * 3.6;
        if (speed > LimitSpeed) {
            return false;
        }
        return true;
    }
}
