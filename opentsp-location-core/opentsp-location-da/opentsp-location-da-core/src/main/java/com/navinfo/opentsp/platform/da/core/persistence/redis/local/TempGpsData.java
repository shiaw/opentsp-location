package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

import com.alibaba.fastjson.JSON;
import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.redis.JacksonUtil;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.NewLocationData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatus.VehicleStatus.Status;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.StaytimeParkAlarm;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.LatestParkEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassInDistrict;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetLastestVehiclePassInAreaRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCMileageConsumption;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCVehiclePassInAreaInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TempGpsData {
    private final static int GPS_LOCAL_TO_REDIS_COMMIT_SIZE = Configuration
            .getInt(Constant.ConfigKey.GPS_LOCAL_TO_REDIS_COMMIT_SIZE);
    private static Map<String, List<TempGpsDataEntry>> cache = new ConcurrentHashMap<String, List<TempGpsDataEntry>>();
    private static Map<String, List<TempCanData>> canDataCache = new ConcurrentHashMap<String, List<TempCanData>>();
    private static ILocationRedisService iLocationRedisService = new LocationRedisServiceImpl();
    private static Logger log = LoggerFactory.getLogger(TempGpsData.class);
    public static Map<Long, LocationData> previousLocations;// 上30秒的所有终端轨迹
    public static Map<Long, LCMileageAndOilDataRes.MileageAndOilData> mileageAndOilDataCache;
    final static ILocationRedisService locationRedisService = new LocationRedisServiceImpl();
    private static Map<Long, NewLocationData> newMap = new ConcurrentHashMap<Long, NewLocationData>();

    public static void putMileageAndOilData(Long terminalId, LCMileageAndOilDataRes.MileageAndOilData data) {
        if (TempGpsData.mileageAndOilDataCache == null) {
            TempGpsData.mileageAndOilDataCache = new HashMap<Long, LCMileageAndOilDataRes.MileageAndOilData>();
        }
        TempGpsData.mileageAndOilDataCache.put(terminalId, data);
        saveMileageAndOilDataToStaticRedis(terminalId, data);
    }

    /**
     * 初始化加载 type=3的末次缓存
     */
    public static void init(){
        previousLocations = getAllLastestLcFromRedis(RedisConstans.RedisKey.LASTEST_COMBINATION_LOCATION_DATA.name());
        if(previousLocations!=null){
            for(Map.Entry<Long, LocationData> map : previousLocations.entrySet()){
                NewLocationData data = new NewLocationData();
                long key = map.getKey();
                LocationData locationData = map.getValue();
                if(locationData.getLongitude()>0&&locationData.getLatitude()>0){
                    data.setLatitude(locationData.getLatitude());
                    data.setLongitude(locationData.getLongitude());
                }
                if(locationData.getStatusAddition().getStatusCount()>0){
                    List<VehicleStatusData> statusDatas = locationData.getStatusAddition().getStatusList();
                    for(VehicleStatusData statusData : statusDatas){
                        if(statusData.getTypes() == StatusType.mileage){
                            if(statusData.getStatusValue()>0){
                                data.setCanMileage(statusData.getStatusValue());
                            }
                            continue;
                        }
                        if(statusData.getTypes() == StatusType.oilValue){
                            if(statusData.getStatusValue()>0){
                                data.setOilValue(statusData.getStatusValue());
                            }
                            continue;
                        }
                        if(statusData.getTypes() == StatusType.rotation){
                            if(statusData.getStatusValue()>0){
                                data.setRotation(statusData.getStatusValue());
                            }
                            continue;
                        }
                        if(statusData.getTypes() == StatusType.cumulativeRunningTime){
                            if(statusData.getStatusValue()>0){
                                data.setRunTime(statusData.getStatusValue());
                            }
                            continue;
                        }
                    }
                }
                newMap.put(key, data);
            }
        }
        previousLocations = null;
    }

    public static void add(String nodeCode, long terminalId, LocationData locationData) {
        TempGpsDataEntry dataEntry = new TempGpsDataEntry();
        dataEntry.setTerminalId(terminalId);
        dataEntry.setGpsTime(locationData.getGpsDate());
        dataEntry.setLocationData(locationData);
        String day = DateUtils.format(locationData.getGpsDate(), DateUtils.DateFormat.YYYYMMDD);
        dataEntry.setDay(day);
        List<TempGpsDataEntry> list = cache.get(nodeCode);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(dataEntry);
        if (list.size() >= GPS_LOCAL_TO_REDIS_COMMIT_SIZE) {
            // 往Redis转移
            List<TempGpsDataEntry> temp = list;
            list = new ArrayList<TempGpsDataEntry>();
            cache.put(nodeCode, list);
            iLocationRedisService.savaNormalLocation(nodeCode, temp);
            saveLastestLcToStaticRedis(temp);
            temp = null;
        } else {
            cache.put(nodeCode, list);
        }
    }

    static RedisMapDaoImpl mapDao = new RedisMapDaoImpl();

    /**
     * hxw
     *
     * @param list
     */
    public static void saveLastestLcToStaticRedis(List<TempGpsDataEntry> list) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            long startTime = System.currentTimeMillis();
//			log.error("集中式redis末次位置数据存储,当前时间："+new Date(startTime).toLocaleString());
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            RedisImp redisImp = new RedisImp();
            for (TempGpsDataEntry entity : list) {
                LocationData locationData = entity.getLocationData();
                if (locationData == null || locationData.getIsPatch()) {
                    continue;
                }
                //CAN数据有效
                if ((locationData.getStatus() & Status.acc_VALUE) == 1) {
                    long mileage = 0l;
                    float oilValue = 0;
                    long mileageDD = 0l;
                    float jfOilValue = 0;
                    float curOilValue = 0;
                    List<VehicleStatusData> vehicleStatusDatas = locationData.getStatusAddition().getStatusList();
                    for (VehicleStatusData data : vehicleStatusDatas) {
                        if (data.getTypes() == StatusType.mileage) {
                            mileage = data.getStatusValue() * 10;
                        }
                        if (data.getTypes() == StatusType.totalFuelConsumption) {
                            oilValue = data.getStatusValue() / 100f;
                        }
                        if (data.getTypes() == StatusType.mileageDD) {
                            mileageDD = data.getStatusValue() * 10;
                        }
                        if (data.getTypes() == StatusType.integralFuelConsumption) {
                            jfOilValue = data.getStatusValue() / 100f;
                        }
                        if (data.getTypes() == StatusType.oilValue) {
                            curOilValue = data.getStatusValue() / 100f;
                        }
                    }
                    int batteryPower = locationData.getBatteryPower();
                    if (mileage != 0 || oilValue != 0 || batteryPower != 0 || mileageDD !=0 || jfOilValue != 0 || curOilValue !=0 ) {
                        pipeline.hset(redisImp.string2byte(RedisConstans.RedisKey.LASTEST_LOCATION_DATA.name()),
                                redisImp.string2byte(String.valueOf(entity.getTerminalId())),
                                locationData.toByteArray());
                    }
                }
                int lng = locationData.getLongitude();
                int lat = locationData.getLatitude();
                //经纬度有效
                if (lng != 0 && lat != 0) {
                    pipeline.hset(redisImp.string2byte(RedisConstans.RedisKey.LASTEST_LOCATION_DATA_LATLNG.name()),
                            redisImp.string2byte(String.valueOf(entity.getTerminalId())), locationData.toByteArray());
                }
                //所有数据
                pipeline.hset(redisImp.string2byte(RedisConstans.RedisKey.LASTEST_LOCATION_DATA_ALL.name()),
                        redisImp.string2byte(String.valueOf(entity.getTerminalId())), locationData.toByteArray());
                //type=3
                locationData = getCombination(entity);
                pipeline.hset(redisImp.string2byte(RedisConstans.RedisKey.LASTEST_COMBINATION_LOCATION_DATA.name()),
                        redisImp.string2byte(String.valueOf(entity.getTerminalId())), locationData.toByteArray());

            }
            pipeline.syncAndReturnAll();
            long endTime = System.currentTimeMillis();
            //log.error("集中式redis末次位置数据存储 耗时："+(endTime-startTime)/1000.0+"  秒");
        } catch (Exception e) {
            isBroken = true;
            log.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
    }

    public static LocationData getCombination(TempGpsDataEntry entity){
        LocationData locationData = entity.getLocationData();
        if(newMap.containsKey(entity.getTerminalId())){
            NewLocationData data  = newMap.get(entity.getTerminalId());
            if(locationData.getLongitude()>0&&locationData.getLatitude()>0){
                data.setLongitude(locationData.getLongitude());
                data.setLatitude(locationData.getLatitude());
            }
            if(locationData.getStatusAddition().getStatusCount()>0){
                List<VehicleStatusData> statusDatas = locationData.getStatusAddition().getStatusList();
                for(VehicleStatusData statusData : statusDatas){
                    if(statusData.getTypes() == StatusType.mileage){
                        if(statusData.getStatusValue()>0){
                            data.setCanMileage(statusData.getStatusValue());
                        }
                        continue;
                    }
                    if(statusData.getTypes() == StatusType.oilValue){
                        if(statusData.getStatusValue()>0){
                            data.setOilValue(statusData.getStatusValue());
                        }
                        continue;
                    }
                    if(statusData.getTypes() == StatusType.rotation){
                        if(statusData.getStatusValue()>0){
                            data.setRotation(statusData.getStatusValue());
                        }
                        continue;
                    }
                    if(statusData.getTypes() == StatusType.cumulativeRunningTime){
                        if(statusData.getStatusValue()>0){
                            data.setRunTime(statusData.getStatusValue());
                        }
                        continue;
                    }
                }
            }
            if (locationData.getStandardFuelCon()>0){
                data.setStandardFuelCon(locationData.getStandardFuelCon());
            }
            if (locationData.getStandardMileage()>0){
                data.setStandardMileage(locationData.getStandardMileage());
            }
            locationData = LocataDataBuilderUtil.builder(locationData, data);
        }else{
            NewLocationData data =new NewLocationData();
            newMap.put(entity.getTerminalId(), data);
        }
        return locationData;
    }

    public static void delMileageAndOilDataStatisticCache() {
        log.info("删除集中式Redis中昨天统计的里程和油耗,删除前打印昨天统计的结果如下");
        for (Iterator<Entry<Long, LCMileageAndOilDataRes.MileageAndOilData>> it = TempGpsData.mileageAndOilDataCache.entrySet().iterator(); it
                .hasNext(); ) {
            Entry<Long, LCMileageAndOilDataRes.MileageAndOilData> entry = it.next();
            Long key = entry.getKey();
            LCMileageAndOilDataRes.MileageAndOilData data = entry.getValue();
            log.info("[key=" + key + "," + data + "]");
        }
        TempGpsData.mileageAndOilDataCache = null;
        TempGpsData.previousLocations = null;
        mapDao.del(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name());
    }

    public static void saveMileageAndOilDataToStaticRedis(long terminalId, LCMileageAndOilDataRes.MileageAndOilData mileageAndOilData) {
        mapDao.saveTostaticRedis(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name(), String.valueOf(terminalId),
                mileageAndOilData.toByteArray());
    }

    // 获取所有终端MileageAndOilData <TerminalId,MileageAndOilData>
    public static Map<Long, LCMileageAndOilDataRes.MileageAndOilData> getAllMileageAndOilDataFromStaticRedis() {
        Map<Long, LCMileageAndOilDataRes.MileageAndOilData> map = new HashMap<Long, LCMileageAndOilDataRes.MileageAndOilData>();
        Map<byte[], byte[]> result = mapDao.getFromStaticRedis(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name());
        if (result != null) {
            for (Entry<byte[], byte[]> entry : result.entrySet()) {
                try {
                    Long key = string2Long(byte2string(entry.getKey()));
                    map.put(key, LCMileageAndOilDataRes.MileageAndOilData.parseFrom(entry.getValue()));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return map;
        }
        return null;
    }

    // 获取所有终端最新一次轨迹 <TerminalId,LocationData>
    public static Map<Long, LocationData> getAllLastestLcFromStaticRedis(int type) {
        Map<Long, LocationData> map = new HashMap<Long, LocationData>();
        String mapName;
        //type 0：有效位置（经纬度）1：有效CAN数据 2：所有数据
        if (type == 0) {
            mapName = RedisConstans.RedisKey.LASTEST_LOCATION_DATA_LATLNG.name();
        } else if (type == 1) {
            mapName = RedisConstans.RedisKey.LASTEST_LOCATION_DATA.name();
        } else {
            mapName = RedisConstans.RedisKey.LASTEST_LOCATION_DATA_ALL.name();
        }
        Map<byte[], byte[]> result = mapDao.getFromStaticRedis(mapName);
        if (result != null) {
            for (Entry<byte[], byte[]> entry : result.entrySet()) {
                try {
                    Long key = string2Long(byte2string(entry.getKey()));
                    map.put(key, LocationData.parseFrom(entry.getValue()));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return map;
        }
        return null;
    }

    /**
     * 获取Redis中末次位置
     *
     * @param mapName
     * @return
     */
    public static Map<Long, LocationData> getAllLastestLcFromRedis(String mapName) {
        Map<Long, LocationData> map = new HashMap<Long, LocationData>();
        Map<byte[], byte[]> result = mapDao.getFromStaticRedis(mapName);
        if (result != null) {
            for (Entry<byte[], byte[]> entry : result.entrySet()) {
                try {
                    Long key = string2Long(byte2string(entry.getKey()));
                    map.put(key, LocationData.parseFrom(entry.getValue()));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return map;
        }
        return null;
    }

    public static String byte2string(byte[] bytes) {
        try {
            return new String(bytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            return "";
        }
    }

    public static Long string2Long(String src) {
        try {
            return Long.parseLong(src);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void saveGpsDataToRedis() {
//        Map<String, List<TempGpsDataEntry>> temp = cache;
//        cache = new ConcurrentHashMap<String, List<TempGpsDataEntry>>();
//        Set<String> keys = temp.keySet();
//        for (String key : keys) {
//            List<TempGpsDataEntry> list = temp.get(key);
//            if (null != list && list.size() > 0)
//                iLocationRedisService.savaNormalLocation(key, list);
//            saveLastestLcToStaticRedis(list);
//        }
//        keys = null;
//        temp.clear();
//        temp = null;

        Map<String, List<TempCanData>> tempCanData = canDataCache;
        canDataCache = new ConcurrentHashMap<String, List<TempCanData>>();
        Set<String> keys = tempCanData.keySet();
        for (String key : keys) {
            List<TempCanData> list = tempCanData.get(key);
            if (null != list && list.size() > 0) {
                iLocationRedisService.saveCanData(key, tempCanData.get(key));
            }
        }
        tempCanData.clear();
//        tempCanData = null;
    }

    public static void addCanData(String nodeCode, long terminalId, LCCANBUSDataReport.CANBUSDataReport report) {
        TempCanData dataEntry = new TempCanData();
        dataEntry.setTerminalId(terminalId);
        dataEntry.setGpsTime(report.getReportDate());
        dataEntry.setData(report);
        String day = DateUtils.format(report.getReportDate(), DateUtils.DateFormat.YYYYMMDD);
        dataEntry.setDay(day);
        List<TempCanData> list = canDataCache.get(nodeCode);
        if (list == null) {
            list = new ArrayList<>();
        }
        list.add(dataEntry);
        if (list.size() >= GPS_LOCAL_TO_REDIS_COMMIT_SIZE) {
            // 往Redis转移
            List<TempCanData> temp = list;
            list = new ArrayList<TempCanData>();
            canDataCache.put(nodeCode, list);
            iLocationRedisService.saveCanData(nodeCode, temp);
            temp = null;
        } else {
            canDataCache.put(nodeCode, list);
        }
    }

    /*
     * add by zhangyj
     *
     * 里程油耗入库接口
     */
    public static void saveMileageAndOilDataToStaticRedis(List<String> terminalIds, List<byte[]> mileageAndOilDatas) {
        mapDao.saveTostaticRedisInBatch(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name(), terminalIds,
                mileageAndOilDatas);
    }

    /*
     * add by zhangyj
     *
     * 拉取所有终端最新里程油耗数据接口
     */
    public static Map<Long, LCMileageConsumption.MileageConsumption> getAllMileageAndOilDataFromRedis() {
        Map<Long, LCMileageConsumption.MileageConsumption> map = new HashMap<Long, LCMileageConsumption.MileageConsumption>();
        Map<byte[], byte[]> result = mapDao.getFromStaticRedis(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name());
        if (result != null) {
            for (Entry<byte[], byte[]> entry : result.entrySet()) {
                try {
                    Long key = string2Long(byte2string(entry.getKey()));
                    map.put(key, LCMileageConsumption.MileageConsumption.parseFrom(entry.getValue()));
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    /*
     * add by zhangyj
     *
     * 拉取所有终端最新里程油耗数据接口
     */
    public static void saveLastestVehicleInfoToStaticRedis(int district, LCVehiclePassInAreaInfo.VehiclePassInAreaInfo build) {
        mapDao.saveTostaticRedis(RedisConstans.RedisKey.LASTEST_VEHICLE_PASS_AREA_TIMES.name(),
                String.valueOf(district), build.toByteArray());

    }

    public static void saveDistrictTimesToStaticRedis(List<VehiclePassInDistrict> list) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            long startTime = System.currentTimeMillis();
            // log.error("集中式redis末次位置数据存储,当前时间："+new
            // Date(startTime).toLocaleString());
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            RedisImp redisImp = new RedisImp();
            for (VehiclePassInDistrict entity : list) {

                pipeline.hset(redisImp.string2byte(RedisConstans.RedisKey.LASTEST_VEHICLE_PASS_IN_DISTRICT.name()),
                        redisImp.string2byte(String.valueOf(entity.getDistrict())),
                        redisImp.object2byte(entity.getTerminals()));
            }
            pipeline.syncAndReturnAll();
            long endTime = System.currentTimeMillis();
            log.error("集中式redis行政区域车次 耗时：" + (endTime - startTime) / 1000.0 + "  秒");
        } catch (Exception e) {
            isBroken = true;
            log.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
    }

    /*
     * add by zhangyj
     *
     * 拉取查询终端的最新里程数据，供ws使用 hxw:后续改进，用list为条件查redis
     */
    public static Map<Long, LCMileageConsumption.MileageConsumption> findAllMileageAndOilDataFromRedis(List<Long> tids) {
        Map<Long, LCMileageConsumption.MileageConsumption> map = new HashMap<Long, LCMileageConsumption.MileageConsumption>();
        Map<byte[], byte[]> result = mapDao.getFromStaticRedis(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name());
        if (result != null) {
            for (Entry<byte[], byte[]> entry : result.entrySet()) {
                try {
                    Long key = string2Long(byte2string(entry.getKey()));
                    if (tids.contains(key)) {
                        map.put(key, LCMileageConsumption.MileageConsumption.parseFrom(entry.getValue()));
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static Map<Long, LCMileageConsumption.MileageConsumption> findMileageConsumption(List<Long> tids) {
        Map<Long, LCMileageConsumption.MileageConsumption> map = new HashMap<Long, LCMileageConsumption.MileageConsumption>();
        try {
            for (long tid : tids) {
                byte[] result = mapDao.hget(RedisConstans.RedisKey.DAY_MILEAGE_OIL_VALUE.name(), String.valueOf(tid));
                if (result != null) {
                    LCMileageConsumption.MileageConsumption mileageConsumption = LCMileageConsumption.MileageConsumption.parseFrom(result);
                    map.put(tid, mileageConsumption);
                }
            }
            return map;
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public static List<Long> findTerminalInDistrict(List<Long> tids, int districtCode) {
//		Map<Integer,List<Long>> list = new HashMap<Integer,List<Long>>();
        List<Long> list = new ArrayList<Long>();
        RedisImp redisImp = new RedisImp();
        try {
            byte[] result = mapDao.hget(RedisConstans.RedisKey.LASTEST_VEHICLE_PASS_IN_DISTRICT.name(),
                    String.valueOf(districtCode));
            if (result != null) {
                List<Long> listResult = (List<Long>) redisImp.byte2object(result);
                if (listResult != null) {
                    listResult.retainAll(tids);//去交集
                    return listResult;
                }
            }
            return list;
        } catch (Exception e) {
            e.getStackTrace();
        }

        return list;
    }

    public static Map<Long,LocationData> findLocationData(List<Long> tids, int type) {
        Map<Long,LocationData> list = new HashMap<Long,LocationData>();
        try {
            if (type == 0) {
                list = mapDao.pipeline(RedisConstans.RedisKey.LASTEST_LOCATION_DATA_LATLNG.name(), tids);
            } else if(type == 1) {
                list = mapDao.pipeline(RedisConstans.RedisKey.LASTEST_LOCATION_DATA.name(), tids);
            } else if(type == 2){
                list = mapDao.pipeline(RedisConstans.RedisKey.LASTEST_LOCATION_DATA_ALL.name(), tids);
            } else{
                list = mapDao.pipeline(RedisConstans.RedisKey.LASTEST_COMBINATION_LOCATION_DATA.name(), tids);
            }
            if (list != null) {
                return list;
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    public static List<LatestParkEntity> findLatestParkData(List<Long> tids,List<Long> areaIds) {
        List<LatestParkEntity> list = new ArrayList<>();
        try {

            List<Object> result = mapDao.pipelineGetObjects("LATEST_PARK_DATA",tids,areaIds);
            if (result != null) {
                for (Object entry : result) {
                    try {
                        list.add(JacksonUtil.readValue(String.valueOf(entry),LatestParkEntity.class));
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                return list;
            }
//            return result;

        } catch (Exception e) {
            e.getStackTrace();

        }
        return null;
    }

    public static LCGetLastestVehiclePassInAreaRes.GetLastestVehiclePassInAreaRes findVehiclePassInAreaInfo(List<Integer> districtCodes)
            throws InvalidProtocolBufferException {
        LCGetLastestVehiclePassInAreaRes.GetLastestVehiclePassInAreaRes.Builder builder = LCGetLastestVehiclePassInAreaRes.GetLastestVehiclePassInAreaRes.newBuilder();
        for (int district : districtCodes) {
            byte[] result = mapDao.hget(RedisConstans.RedisKey.LASTEST_VEHICLE_PASS_AREA_TIMES.name(),
                    String.valueOf(district));
            if (result != null) {
                LCVehiclePassInAreaInfo.VehiclePassInAreaInfo info = LCVehiclePassInAreaInfo.VehiclePassInAreaInfo.parseFrom(result);
                builder.addInfos(info);
            }
        }
        builder.setStatusCode(LCPlatformResponseResult.PlatformResponseResult.success_VALUE);
        builder.setTotalRecords(builder.getInfosCount());
        return builder.build();
    }

    public static List<LocationData> getTerminalLocationData(long terminalId) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        List<LocationData> list = locationRedisService.findNormalLocation(terminalId, calendar);
        if (list != null && list.size() > 0) {
            return list;
        }
        return null;
    }

    public static Map<Long,StaytimeParkAlarm> getAllStaytimeParkCacheFromRedis() {
        Map<Long, StaytimeParkAlarm> map = new HashMap<Long, StaytimeParkAlarm>();
        Map<byte[], byte[]> result = mapDao.getFromStaticRedis(RedisConstans.RedisKey.STAYTIME_PARK_ALARM.name());
        if (result != null) {
            for (Entry<byte[], byte[]> entry : result.entrySet()) {
                try {
                    Long key = string2Long(byte2string(entry.getKey()));
                    String value = byte2string(entry.getValue());
                    map.put(key, JSON.parseObject(value,StaytimeParkAlarm.class));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return map;
    }

    public static void saveBatchStaytimeParkCache(Map<Long, StaytimeParkAlarm> cache) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            long startTime = System.currentTimeMillis();
            jedis = RedisStatic.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            RedisImp redisImp = new RedisImp();
            for(Map.Entry<Long, StaytimeParkAlarm> obj : cache.entrySet()){
                pipeline.hset(redisImp.string2byte(RedisConstans.RedisKey.STAYTIME_PARK_ALARM.name()),
                        redisImp.string2byte(String.valueOf(obj.getKey())),
                        redisImp.object2byte(obj.getValue()));
            }
            pipeline.syncAndReturnAll();
            long endTime = System.currentTimeMillis();
            log.error("保存区域停留缓存 耗时：" + (endTime - startTime) / 1000.0 + "  秒");
        } catch (Exception e) {
            isBroken = true;
            log.error(e.getMessage(), e);
        } finally {
            RedisStatic.getInstance().release(jedis, isBroken);
        }
    }
}
