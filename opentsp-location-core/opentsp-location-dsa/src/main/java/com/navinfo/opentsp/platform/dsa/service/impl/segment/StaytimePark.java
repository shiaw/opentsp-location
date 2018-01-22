package com.navinfo.opentsp.platform.dsa.service.impl.segment;


import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.Segment;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.StaytimeParkingAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.StaytimeParkAlarm;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class StaytimePark extends ConcurrentAlg
        implements Segment
{
    @Value("${opentsp.staytime.delay:1200}")
    private Integer staytime;
    private static Logger logger = LoggerFactory.getLogger(StaytimePark.class);
    private volatile Map<Long, StaytimeParkAlarm> cache = new HashMap<>();
    public void execute(Long key, List<LocationData> data, StatisticResultEntity result)
    {
        try {
            if ((data == null) || (data.size() == 0)) {
                return;
            }
            logger.info("数据大小" + data.size());
            //logger.info("cache="+cache+"是否包含key"+key+cache.containsKey(key));
            for (int i = 0; i < data.size(); i++) {
                LocationData location = (LocationData) data.get(i);
                if (location.getLongitude() == 0 || location.getLatitude() == 0){
                    continue;
                }
                StaytimeParkAlarm alarm = cache.get(key);
                if ((location.getAlarm() >> 24 & LCAllAlarm.AlarmAddition.staytimeParkingInArea_VALUE) == LCAllAlarm.AlarmAddition.staytimeParkingInArea_VALUE) {
                    logger.info(key+"区域停留报警");
                    List parkingAdditionList = location.getStaytimeParkingAdditionList();
                    if ((null != parkingAdditionList) && (parkingAdditionList.size() > 0)) {
                        StaytimeParkingAddition areaInfo = (StaytimeParkingAddition) parkingAdditionList.get(0);
                        long areaId = areaInfo.getAreaId();
                        if (alarm != null){
                            alarm.setEndDate(location.getGpsDate());
                            alarm.setEndLat(location.getLatitude());
                            alarm.setEndLng(location.getLongitude());
                            alarm.setContinuousTime((int) (location.getGpsDate() - alarm.getBeginDate()));
                            if (i == data.size() - 1) {
                                alarm.setTailMerge(0);
                                logger.info("终端" + key + "开始时间"+alarm.getBeginDate()+"结束时间"+alarm.getEndDate()+"停留时间" + alarm.getContinuousTime());
                                if (alarm.getContinuousTime() > staytime) {
                                    List<StaytimeParkAlarm> list = result.getStaytimeParkAlarms().get(alarm.getTerminalId());
                                    if (list == null) {
                                        list = new ArrayList();
                                        result.getStaytimeParkAlarms().put(alarm.getTerminalId(), list);
                                    }
                                    list.add(alarm);
                                }
                            }
                        }
                        if (alarm == null) {
                            logger.info("终端" + key + "进入服务区" + areaId);
                            alarm = new StaytimeParkAlarm();
                            alarm.setTerminalId(key.longValue());
                            alarm.setAreaId(areaId);
                            alarm.setBeginDate(location.getGpsDate());
                            alarm.setBeginLat(location.getLatitude());
                            alarm.setBeginLng(location.getLongitude());
                            cache.put(key, alarm);
                        }
                    } else {
                        logger.error("区域停留时间报警缺少区域停留时间附加信息，请检查区域停留时间报警处理。 tid:" + key + "gps date:" + location.getGpsDate());
                    }
                } else {
                    if (alarm != null) {
                        logger.info("终端" + key + "出服务区" + alarm.getAreaId() + "纬度：" + location.getLatitude() + "时间：" + location.getGpsDate());
                        alarm.setTailMerge(1);
                        List<StaytimeParkAlarm> list = result.getStaytimeParkAlarms().get(alarm.getTerminalId());
                        if (list == null) {
                            list = new ArrayList();
                            result.getStaytimeParkAlarms().put(alarm.getTerminalId(), list);
                        }
                        if (alarm.getContinuousTime() > staytime) {
                            list.add(alarm);
                        }
                        if (cache != null && cache.containsKey(key)) {
                            cache.remove(key);
                        }
                        alarm = null;
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void saveResult(StatisticResultEntity entity)
            throws Exception
    {
        long startLong = System.currentTimeMillis();

        List insertTerminalStaytimeParkAlarm = new ArrayList();

        List updateTerminalStaytimeParkAlarm = new ArrayList();
        AlarmStatisticsQueryService currentServer = (AlarmStatisticsQueryService)getRmiclient().rmiBalancerRequest("AlarmStatisticsQueryService");
        Map<Long, StaytimeParkAlarm> all = currentServer.getStaytimeParkAlarmNoEndMerge(this.terminalsCache
                .getAllTerminals(), (int)ConcurrentAlg.getCurrentDayStart());
        Map<Long, List<StaytimeParkAlarm>> parkAlarms = entity.getStaytimeParkAlarms();
        if (parkAlarms.isEmpty()) {
            logger.warn("此次计算结果集为空，更新数据库待合并记录为合并完成。");
        }
        for(Map.Entry<Long, StaytimeParkAlarm> obj : all.entrySet()){
            if (parkAlarms.get(obj.getKey()) == null && obj.getValue().getTailMerge() == 0) {
                if (allData.get(obj.getValue().getTerminalId()) == null){
                    continue;
                }else{
                    obj.getValue().setTailMerge(1);
                    updateTerminalStaytimeParkAlarm.add(obj.getValue());
                    if(cache!=null&&cache.containsKey(obj.getKey())){
                        cache.remove(obj.getKey());
                    }
                }
            }
        }
        for (Long key : parkAlarms.keySet()) {
            List list = (List)parkAlarms.get(key);
            if ((list == null) || (list.size() == 0)) {
                continue;
            }
            StaytimeParkAlarm current = (StaytimeParkAlarm)list.get(0);
            StaytimeParkAlarm old = (StaytimeParkAlarm)all.get(key);
            if (old == null) { //新纪录
                insertTerminalStaytimeParkAlarm.addAll(list);
            }  else {
                old.setEndDate(current.getEndDate());
                old.setEndLat(current.getEndLat());
                old.setEndLng(current.getEndLng());
                old.setTailMerge(current.getTailMerge());
                old.setContinuousTime((int)(current.getEndDate() - old.getBeginDate()));
                updateTerminalStaytimeParkAlarm.add(old);
            }
        }

        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService)getRmiclient().rmiBalancerRequest("AlarmStatisticsStoreService");
        LCPlatformResponseResult.PlatformResponseResult staytimePark = daServer.saveBatchStaytimeParkAlarmInfo(insertTerminalStaytimeParkAlarm,
                getCurrentDayStart());
        if (1 != staytimePark.getNumber()) {
            logger.error("StaytimeParkAlarm store failure.");
        }
        LCPlatformResponseResult.PlatformResponseResult updateResult = daServer.updateBatchStaytimeParkAlarmInfo(updateTerminalStaytimeParkAlarm,
                getCurrentDayStart());
        if (1 != updateResult.getNumber()) {
            logger.error("StaytimeParkAlarm store update failure.");
        }
        logger.debug(">>>staytimepark数据存儲耗时:{},总结果数:insert={},update={}", new Object[] {
                Double.valueOf(System.currentTimeMillis() - startLong / 1000.0D), Integer.valueOf(insertTerminalStaytimeParkAlarm.size()),
                Integer.valueOf(updateTerminalStaytimeParkAlarm
                        .size()) });
    }
}