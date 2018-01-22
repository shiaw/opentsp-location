package com.navinfo.opentsp.platform.dsa.service.impl.segment;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.Segment;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.OvertimeParkingAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.OvertimeParkAlarm;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 滞留超时报警统计
 *
 * @author hk
 */
@Component
@Scope("prototype")
public class OvertimePark extends ConcurrentAlg implements Segment {
    private static Logger logger = LoggerFactory.getLogger(OvertimePark.class);

    @Override
    public void execute(Long key, List<LocationData> data, StatisticResultEntity result) {
        if (data == null || data.size() == 0) {
            return;
        }
        OvertimeParkAlarm alarm = null;

        for (int i = 0; i < data.size(); i++) {
            LocationData location = data.get(i);
            // 判断当前点位是否为滞留超时报警
            if ((location.getAlarm() >> 24 & LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE) == LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE) {
                List<OvertimeParkingAddition> parkingAdditionList = location.getParkingAdditionList();
                if (null != parkingAdditionList && parkingAdditionList.size() > 0) {
                    // 服务站不存在相交的情况，一个车同一时间只能在一个服务站发生滞留超时报警
                    OvertimeParkingAddition areaInfo = parkingAdditionList.get(0);
                    long areaId = areaInfo.getAreaId();
                    if (alarm == null) {
                        alarm = new OvertimeParkAlarm();
                        alarm.setTerminalId(key);
                        alarm.setAreaId(areaId);
                        alarm.setLimitParking(terminalRuleCache.getAreaOverTimeInfo().get(areaId));
                        alarm.setBeginDate(location.getGpsDate());
                        alarm.setBeginLat(location.getLatitude());
                        alarm.setBeginLng(location.getLongitude());
                        if (i != 0) {
                            alarm.setHeadMerge(1);
                        } else {
                            alarm.setHeadMerge(0);
                        }
                    }
                    alarm.setEndDate(location.getGpsDate());
                    alarm.setEndLat(location.getLatitude());
                    alarm.setEndLng(location.getLongitude());
                    alarm.setContinuousTime((int) (location.getGpsDate() - alarm.getBeginDate()));
                    if (i == data.size() - 1) {
                        alarm.setTailMerge(0);// 统计未结束，需要合并
                        List<OvertimeParkAlarm> list = result.getOvertimeParkAlarms().get(alarm.getTerminalId());
                        if (list == null) {
                            list = new ArrayList<>();
                            result.getOvertimeParkAlarms().put(alarm.getTerminalId(), list);
                        }
                        list.add(alarm);
                    }
                } else {
                    logger.error("滞留超时报警缺少滞留附加信息，请检查滞留超时报警处理。 tid:" + key + "gps date:" + location.getGpsDate());
                }
            } else {// 状态切换，存储滞留超时报警,统计结束
                if (alarm != null) {
                    alarm.setTailMerge(1);
                    List<OvertimeParkAlarm> list = result.getOvertimeParkAlarms().get(alarm.getTerminalId());
                    if (list == null) {
                        list = new ArrayList<>();
                        result.getOvertimeParkAlarms().put(alarm.getTerminalId(), list);
                    }
                    list.add(alarm);
                    alarm = null;
                }
            }
        }

    }

    @Override
    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        // insert到da的结果集
        List<OvertimeParkAlarm> insertTerminalOvertimeParkAlarm = new ArrayList<>();
        // update到da的结果集
        List<OvertimeParkAlarm> updateTerminalOvertimeParkAlarm = new ArrayList<>();
        AlarmStatisticsQueryService currentServer = (AlarmStatisticsQueryService) getRmiclient().rmiBalancerRequest(RmiConstant.QUERY_STATIC_TER_DATA);
        Map<Long, OvertimeParkAlarm> all = currentServer.getOvertimeParkAlarmNoEndMerge(
                terminalsCache.getAllTerminals(), (int) ConcurrentAlg.getCurrentDayStart());
        Map<Long, List<OvertimeParkAlarm>> parkAlarms = entity.getOvertimeParkAlarms();
        if (parkAlarms.isEmpty()) {
            logger.warn("此次计算结果集为空，更新数据库待合并记录为合并完成。");
        }
        for(Map.Entry<Long, OvertimeParkAlarm> obj : all.entrySet()){
            if (parkAlarms.get(obj.getKey()) == null && obj.getValue().getTailMerge() == 0) {
                if (allData.get(obj.getValue().getTerminalId()) == null){
                    continue;
                }else{
                    obj.getValue().setTailMerge(1);
                    updateTerminalOvertimeParkAlarm.add(obj.getValue());
                }
            }
        }
        for (Long key : parkAlarms.keySet()) {
            List<OvertimeParkAlarm> list = parkAlarms.get(key);
            OvertimeParkAlarm current = list.get(0);
            OvertimeParkAlarm old = all.get(key);
            if (old == null) {
                insertTerminalOvertimeParkAlarm.addAll(list);
            } else {
                if (current.getHeadMerge() == 1) {
                    old.setTailMerge(1);
                    updateTerminalOvertimeParkAlarm.add(old);
                } else {
                    old.setEndDate(current.getEndDate());
                    old.setEndLat(current.getEndLat());
                    old.setEndLng(current.getEndLng());
                    old.setTailMerge(current.getTailMerge());
                    old.setContinuousTime((int) (current.getEndDate() - old.getBeginDate()));
                    updateTerminalOvertimeParkAlarm.add(old);
                    if (list.size() == 1) {
                        list.clear();
                    } else {
                        old.setTailMerge(1);
                        list.remove(0);
                        insertTerminalOvertimeParkAlarm.addAll(list);
                    }
                }
            }
        }
        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult overtimePark = daServer.saveBatchOvertimeParkAlarmInfo(insertTerminalOvertimeParkAlarm,
                getCurrentDayStart());
        if (PlatformResponseResult.success_VALUE != overtimePark.getNumber()) {
            logger.error("OvertimeParkAlarm store failure.");
        }
        PlatformResponseResult updateResult = daServer.updateBatchOvertimeParkAlarmInfo(
                updateTerminalOvertimeParkAlarm, getCurrentDayStart());
        if (PlatformResponseResult.success_VALUE != updateResult.getNumber()) {
            logger.error("OvertimeParkAlarm store update failure.");
        }
        logger.debug(">>>overtimepark数据存儲耗时:{},总结果数:insert={},update={}",
                (System.currentTimeMillis() - startLong) / 1000.0, insertTerminalOvertimeParkAlarm.size(),
                updateTerminalOvertimeParkAlarm.size());

    }
}
