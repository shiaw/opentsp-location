package com.navinfo.opentsp.platform.dsa.service.impl.segment;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.Segment;
import com.navinfo.opentsp.platform.dsa.utils.SegmentUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.VehicleBreakdownAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.FaultCodeEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 故障码统计算法,现在统计间隔为5分钟,但是统计的时间阀值为10分钟,每次统计有故障码就新增对象,存储时做阀值判断处理存储合并工作
 *
 * @author hk
 */
@Component
@Scope("prototype")
public class FaultCode extends ConcurrentAlg implements Segment {

    public static final long GAPFAULTCODE = 600; // 10分钟,以秒为单位

    @Override
    public void execute(Long key, List<LocationData> gpsData, StatisticResultEntity entity) {
        if (gpsData == null || gpsData.size() == 0) {
            return;
        }

        Map<Integer, FaultCodeEntity> breakdownMap = new HashMap<Integer, FaultCodeEntity>();
        for (int i = 0; i < gpsData.size(); i++) {
            LocationData location = gpsData.get(i);
            long gpsDate = location.getGpsDate();
            VehicleBreakdownAddition breakdownAddition = location.getBreakdownAddition();
            if (null != breakdownAddition && null != breakdownAddition.getBreakdownList()
                    && breakdownAddition.getBreakdownList().size() > 0) {
                List<VehicleBreakdown> breakdownList = breakdownAddition.getBreakdownList();
                for (VehicleBreakdown breakdown : breakdownList) {
                    int spn = breakdown.getBreakdownSPNValue();
                    int fmi = breakdown.getBreakdownFMIValue();
                    if (spn == 0 && fmi == 0) {
                        continue;
                    }
                    int code = spn * 1000 + fmi;
                    FaultCodeEntity alarm = breakdownMap.get(code);
                    if (alarm != null) {
                        if (gpsDate - alarm.getEndDate() < GAPFAULTCODE) {
                            alarm.setEndDate(gpsDate);
                            alarm.setEndLat(location.getLatitude());
                            alarm.setEndLng(location.getLongitude());
                            alarm.setContinueTime((int) (alarm.getEndDate() - alarm.getBeginDate()));
                        } else {
                            List<FaultCodeEntity> faults = entity.getFaultCodeEntities().get(key);
                            if (faults == null) {
                                faults = new ArrayList<>();
                            }
                            faults.add(alarm);
                            entity.getFaultCodeEntities().put(key, faults);
                            breakdownMap.remove(code);
                            alarm = null;
                        }
                    }
                    if (null == alarm) {
                        alarm = new FaultCodeEntity();
                        alarm.setTerminalId(key);
                        alarm.setSpn(spn);
                        alarm.setFmi(fmi);
                        alarm.setBeginDate(gpsDate);
                        alarm.setBeginLat(location.getLatitude());
                        alarm.setBeginLng(location.getLongitude());
                        alarm.setEndDate(gpsDate);
                        alarm.setEndLat(location.getLatitude());
                        alarm.setEndLng(location.getLongitude());
                        alarm.setContinueTime(0);
                        breakdownMap.put(code, alarm);
                    }
                }
            }
        }
        for (Map.Entry<Integer, FaultCodeEntity> value : breakdownMap.entrySet()) {
            List<FaultCodeEntity> list = entity.getFaultCodeEntities().get(value.getValue().getTerminalId());
            if (list == null) {
                list = new ArrayList<>();
            }
            list.add(value.getValue());
            entity.getFaultCodeEntities().put(key, list);
        }

    }

    @Override
    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        Map<Long, List<FaultCodeEntity>> map = entity.getFaultCodeEntities();
        if (map.size() == 0) {
            logger.warn("此次计算结果集为空,不处理");
            return;
        }
        // 需要insert到da的结果集，且需要直接插入缓存
        List<FaultCodeEntity> insrtTerminalFaultCodeAlarms = new ArrayList<FaultCodeEntity>();
        // 需要更新到da的结果集
        List<FaultCodeEntity> updateTerminalFaultCodeAlarms = new ArrayList<FaultCodeEntity>();
        // 对比缓存数据(即上次最新的保存结果)
        // 计算得出需要update的List和需要insert的List
        AlarmStatisticsQueryService currentServer = (AlarmStatisticsQueryService) getRmiclient().rmiBalancerRequest(RmiConstant.QUERY_STATIC_TER_DATA);
        Calendar cal = Calendar.getInstance();
        int rtSeg = SegmentUtils.getSGSeg(cal);
        Calendar rtEndTime = SegmentUtils.getSGEndTime(rtSeg);
        long end = rtEndTime.getTimeInMillis();
        rtEndTime.add(Calendar.MINUTE, -14);
        long start = rtEndTime.getTimeInMillis();
        Map<String, FaultCodeEntity> allCache = currentServer.getLastFaultCode(terminalsCache.getAllTerminals(), start,
                end);
        // Map<String, FaultCodeEntity> allCache = FaultCodeCache.getAll();
        for (Long tid : map.keySet()) {
            List<FaultCodeEntity> list = map.get(tid);
            for (FaultCodeEntity en : list) {
                String key = en.getTerminalId() + KEYSEPERATOR + en.getFmi() + KEYSEPERATOR + en.getSpn();
                FaultCodeEntity faultCodeEntity = allCache.get(key);
                if (faultCodeEntity != null) {
                    // 10分钟内合并
                    if (en.getBeginDate() - faultCodeEntity.getEndDate() <= GAPFAULTCODE) {
                        faultCodeEntity.setEndDate(en.getEndDate());
                        faultCodeEntity.setEndLat(en.getEndLat());
                        faultCodeEntity.setEndLng(en.getEndLng());
                        faultCodeEntity.setContinueTime((int) (en.getEndDate() - faultCodeEntity.getBeginDate()));
                        updateTerminalFaultCodeAlarms.add(faultCodeEntity);
                        continue;
                    }
                }
                // 新纪录产生
                insrtTerminalFaultCodeAlarms.add(en);
                // allCache.put(key, en);
            }
        }

        // 这里对allCache进行清理，针对超过10分钟的数据踢出缓存
        // 超时踢出掉缓存的结果集
        // long secNow = System.currentTimeMillis() / 1000;
        // Set<Entry<String, FaultCodeEntity>> entrySet = allCache.entrySet();
        // Iterator<Entry<String, FaultCodeEntity>> iterator =
        // entrySet.iterator();
        // while (iterator.hasNext()) {
        // Entry<String, FaultCodeEntity> next = iterator.next();
        // FaultCodeEntity value = next.getValue();
        // if (secNow - value.getEndDate() > GAPFAULTCODE) {
        // iterator.remove();
        // }
        // }
        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult faultResult = daServer.saveFaultCodeStatistic(insrtTerminalFaultCodeAlarms,
                getCurrentDayStart());
        if (PlatformResponseResult.success_VALUE != faultResult.getNumber()) {
            logger.error("FaultCode store failure.");
        }
        PlatformResponseResult updateResult = daServer.updateFaultCodeStatistic(updateTerminalFaultCodeAlarms,
                getCurrentDayStart());
        if (PlatformResponseResult.success_VALUE != updateResult.getNumber()) {
            logger.error("FaultCode store update failure.");
        }
        logger.debug(">>>故障率统计数据存儲耗时:{},总结果数:insert={},update={}", (System.currentTimeMillis() - startLong) / 1000.0,
                insrtTerminalFaultCodeAlarms.size(), updateTerminalFaultCodeAlarms.size());

    }
}
