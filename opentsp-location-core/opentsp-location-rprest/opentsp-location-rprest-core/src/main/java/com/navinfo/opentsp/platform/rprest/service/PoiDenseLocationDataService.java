package com.navinfo.opentsp.platform.rprest.service;

import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.protocol.common.LCConcentratedRealTimeData;
import com.navinfo.opentsp.platform.location.protocol.common.PoiDenseLocationPb;
import com.navinfo.opentsp.platform.location.protocol.common.RealTimeDataPb;
import com.navinfo.opentsp.platform.rprest.PoiDenseLocationCommand;
import com.navinfo.opentsp.platform.rprest.utils.RedisClusters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;

/**
 * @author zhangyue
 */
@Component
public class PoiDenseLocationDataService {

    protected static final Logger logger = LoggerFactory.getLogger(PoiDenseLocationDataService.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 存实际数据redis key
     */
    private final String CONCENTRATED = "concentrated_";
    private final String CONCENTRATED_DATA = "concentrated_data_";
    private final int CONCENTRATEDTAB = 7;
    private final int tempMonth = 12;
    private final int tempTime = 10;
    private final String data = "data";

    /**
     * PoiDenseLocationHandler调用接口主方法
     */
    public byte[] getPoiLocationData(PoiDenseLocationCommand command,List month){
        logger.info("PoiDenseLocationDataService getPoiLocationData start:" + command);
        try{
            //查询mongodb结果
            PoiDenseLocationPb.PoiDenseLocation.Builder retRes = PoiDenseLocationPb.PoiDenseLocation.newBuilder();
            retRes.setTerminalId(command.getTerminalId());
            List<RealTimeDataPb.RealTimeData> mongoData = this.getMongodbPoiDenseLocation(command,month);
            if(mongoData != null && mongoData.size() > 0){
                retRes.addAllDataList(mongoData);
            }
            //查询redis结果a
            List<RealTimeDataPb.RealTimeData> redisData = this.getRedisPoiDenseLocation(command);
            if(redisData != null && redisData.size() > 0){
                retRes.addAllDataList(redisData);
            }
            if(retRes.getDataListList().size() > 0) {
                logger.info("PoiDenseLocationDataService getPoiLocationData getDataListList > 0:" + retRes.getDataListList().size());
                return retRes.build().toByteArray();
            }else{
                logger.info("PoiDenseLocationDataService getPoiLocationData getDataListList null:");
                return null;
            }
        }catch (Exception e){
            logger.error("PoiDenseLocationDataService getPoiLocationData:" + e.getMessage());
            return null;
        }
    }

    /**
     * 处理redis数据取出到bean
     */
    public List<RealTimeDataPb.RealTimeData> getRedisPoiDenseLocation(PoiDenseLocationCommand command){
        try {
            logger.info("PoiDenseLocationDataService getRedisPoiDenseLocation start :");
            Set<Tuple> setCont = rangeByScoreWithScores(this.CONCENTRATED + "0" + command.getTerminalId(), command.getBeginDate() / 1000, command.getEndDate() / 1000);
            List<RealTimeDataPb.RealTimeData> poiBean = new ArrayList<>();
            if (setCont != null) {
                List<Tuple> list = new ArrayList<Tuple>(setCont);
                logger.info("PoiDenseLocationDataService getRedisPoiDenseLocation setCont !=null");
                for(int i = 0;i < list.size(); i ++){
                    Tuple reTyple = list.get(i);
                    LCConcentratedRealTimeData.SpecialRealTimeData concentratedRealTimeData = LCConcentratedRealTimeData.SpecialRealTimeData.
                            parseFrom(Convert.hexStringToBytes(reTyple.getElement()));
                    LCConcentratedRealTimeData.GpsLocationData gpsLocationData = concentratedRealTimeData.getGpsLocationData();
                    List<LCConcentratedRealTimeData.RealTimeDataUnit> realTimeDataUnit = concentratedRealTimeData.getRealTimeDataUnitList();
                    int time = 0;
                    if(realTimeDataUnit.size() > 0){
                        time = 1000 / realTimeDataUnit.size();
                    }
                    for(int j = 0;j < realTimeDataUnit.size(); j++){
                        RealTimeDataPb.RealTimeData.Builder realBuilder = RealTimeDataPb.RealTimeData.newBuilder();
                        long gpsTime = gpsLocationData.getGpsTime() * 1000 + time * j;
                        realBuilder.setGpsTime(gpsTime);
                        realBuilder.setLatitude(gpsLocationData.getLatitude());
                        realBuilder.setLongitude(gpsLocationData.getLongitude());
                        realBuilder.setHeight(gpsLocationData.getHeight());
                        realBuilder.setEngineOutputTorque(realTimeDataUnit.get(j).getEngineOutputTorque());
                        realBuilder.setSpeed(realTimeDataUnit.get(j).getSpeed());
                        realBuilder.setAccelerator(realTimeDataUnit.get(j).getAccelerator());
                        realBuilder.setBrake(realTimeDataUnit.get(j).getBrake());
                        realBuilder.setRotation(realTimeDataUnit.get(j).getRotation());
                        realBuilder.setGear(realTimeDataUnit.get(j).getGear());
                        realBuilder.setClutchSwitch(realTimeDataUnit.get(j).getClutchSwitch());
                        realBuilder.setRealTimeOilConsumption(realTimeDataUnit.get(j).getRealTimeOilConsumption());
                        realBuilder.setFuelConsumptionRate(realTimeDataUnit.get(j).getFuelConsumptionRate());
                        poiBean.add(realBuilder.build());
                    }
                }
            }
            logger.info("PoiDenseLocationDataService getRedisPoiDenseLocation end");
            return poiBean;
        }catch (Exception e){
            logger.error("PoiDenseLocationDataService getRedisPoiDenseLocation:" + e.getMessage());
            return null;
        }
    }

    /**
     * 处理mongodb数据按条件查询取出到bean
     */
    public List<RealTimeDataPb.RealTimeData> getMongodbPoiDenseLocation(PoiDenseLocationCommand command,List month) throws Exception{
        try {
            logger.info("PoiDenseLocationDataService getMongodbPoiDenseLocation start :");
            long startDate = command.getBeginDate();
            long endDate = command.getEndDate();
            //获取要查询哪个月表
            //添加查询表名
            List list = new ArrayList<>();
            // 添加本选时间月表
            list.addAll(month);
            //如果是每月的一号
            if(getDay(startDate) == 1){
                String upperMonth = upperYearMonth(startDate);
                if(upperMonth == null || "".equals(upperMonth)){
                    return null;
                }
                // 添加本选时间月表
                list.add(CONCENTRATED_DATA + upperMonth);
            }

            Criteria criteriaStart = new Criteria();
            criteriaStart.andOperator(
                    Criteria.where("terminalId").is("0" + command.getTerminalId()),
                    Criteria.where("endDate").gte(startDate),
                    Criteria.where("beginDate").lte(endDate));

            Query query = new Query(criteriaStart);
            List<DBObject> listFin = new ArrayList<>();
            if(list != null && list.size() > 0) {
                //按月表查询
                for(int i2 = 0 ;i2 < list.size() ;i2++) {
                    List<DBObject> listTemp = mongoTemplate.find(query, DBObject.class, list.get(i2).toString());
                    if(listTemp.size() > 0){
                        listFin.addAll(listTemp);
                    }
                }
            }
            //结果集
            List<RealTimeDataPb.RealTimeData> realDenseLocationBeanData = null;
            if (listFin != null && listFin.size() > 0) {
                realDenseLocationBeanData = convertPassInArea(listFin, command);
            }
            logger.info("PoiDenseLocationDataService getMongodbPoiDenseLocation end :");
            return realDenseLocationBeanData;

        }catch (Exception e){
            logger.error("PoiDenseLocationDataService getMongodbPoiDenseLocation:" + e.getMessage());
            return null;
        }
    }

    /**
     * 处理mongodb数据组合返回内容
     */
    private List<RealTimeDataPb.RealTimeData> convertPassInArea(List<DBObject> dbObjects, PoiDenseLocationCommand command) {
        try {
            //循环所有符合条件的结果集
            List<RealTimeDataPb.RealTimeData> listDb = new ArrayList<>();
            for (int j1 = 0;j1 < dbObjects.size();j1++) {
                List<RealTimeDataPb.RealTimeData> list = new ArrayList<>();
                //取得mongodb里一个set里符合时间范围的所有pb
                list = dbObjectToBean(dbObjects.get(j1), command);
                if (list != null && list.size() > 0) {
                    listDb.addAll(list);
                }
            }
            return listDb;

        }catch (Exception e){
            logger.error("PoiDenseLocationDataService convertPassInArea:" + e.getMessage());
            return null;
        }
    }

    /**
     * 取得一条数据中在时间范围内所有有效pb
     */
    public List<RealTimeDataPb.RealTimeData> dbObjectToBean(DBObject db, PoiDenseLocationCommand command){
        List<RealTimeDataPb.RealTimeData> daEntities = new ArrayList<>();
        PoiDenseLocationPb.PoiDenseLocation.Builder poiDenseLocationPb = PoiDenseLocationPb.PoiDenseLocation.newBuilder();
        logger.info("PoiDenseLocationDataService dbObjectToBean start :");
        try {
            if(db.get(data) != null && ((LinkedHashMap)db.get(data)).size() > 0){
                LinkedHashMap<String,byte[]> liMap = (LinkedHashMap)db.get(data);
                for(Map.Entry<String,byte[]> map : liMap.entrySet()) {
                    byte[] byRes = map.getValue();
                    long scoreTime = Long.parseLong(map.getKey());
                    if (scoreTime >= command.getBeginDate() && scoreTime <= command.getEndDate()) {
                        daEntities.add(RealTimeDataPb.RealTimeData.parseFrom(byRes));
                    }
                }
            }
            logger.info("PoiDenseLocationDataService dbObjectToBean end :");
            return daEntities;
        }catch(Exception e){
            logger.error("dbObjectToBean error:" + e.getMessage());
            return null;
        }
    }
    public String upperYearMonth(long time){
        try {
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTime(da);
            c.add(Calendar.MONTH, -1);
            int re = c.get(Calendar.MONTH) + 1;
            String s = "";
            if(re < tempTime){
                s = "0" + re;
            }else{
                s = "" + re;
            }
            return c.get(Calendar.YEAR) + s;
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl upperMonth");
            return "";
        }
    }
    public String getMonth(long time){
        try {
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTime(da);
            int re = c.get(Calendar.MONTH) + 1;
            String s = "";
            if(re < tempTime){
                s = "0" + re;
            }else{
                s = "" + re;
            }

            return (c.get(Calendar.YEAR)) + s;
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl upperMonth");
            return "";
        }
    }

    /**
     * 获取当前天
     */
    public int getDay(long time){
        try {
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTimeInMillis(time);
            return c.get(Calendar.DAY_OF_MONTH);
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl getDay");
        }
        return -1;
    }

    public String tempYearMonth(long time,int i){
        try {
            //先解析给定的时间
            Date da = new Date(time);
            Calendar c = Calendar.getInstance();
            c.setTime(da);

            int re = c.get(Calendar.MONTH) + i;
            int year = (c.get(Calendar.YEAR));
            if(re > tempMonth){
                re = (re - tempMonth);
                year = c.get(Calendar.YEAR) + 1;
            }
            String s = "";
            if(re < tempTime){
                s = "0" + re;
            }else{
                s = "" + re;
            }

            return year + s;
        }catch (Exception e){
            logger.error(e.getMessage() + "OilingStealOilServiceImpl upperMonth");
            return "";
        }
    }

    public List nextTimme(long startTime,long endTime){
        int i = 0;
        List listTabl = new ArrayList();
        String end = this.tempYearMonth(endTime, 1);
        int endin = Integer.valueOf(end);
        while (i < CONCENTRATEDTAB){
            i ++;
            String st = this.tempYearMonth(startTime, i);
            int stin = Integer.valueOf(st);

            if((stin) <= endin){
                listTabl.add(CONCENTRATED_DATA + stin);
            }
        }
        return listTabl;
    }
    public Set<Tuple> rangeByScoreWithScores(String keys, long startIndex, long endIndex) {
        Jedis jedis = null;
        boolean isBroken = false;

        try {
            jedis = RedisClusters.getInstance().getJedis();
            Set<Tuple> response = jedis.zrangeByScoreWithScores( keys, startIndex, endIndex);
            //提交
            return response;
        } catch (JedisException e) {
             isBroken = true;
            logger.error("redis zrangePipeline exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
        return null;
    }
}
