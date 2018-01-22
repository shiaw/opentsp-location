package com.navinfo.opentsp.platform.dsa.service.impl.segment;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.Segment;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.StagnationTimeoutEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCOvertimeParking.OvertimeParking;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 停滞超时（部标超时停车）报警统计
 *
 * @author hk
 */
@Component
@Scope("prototype")
public class StagnationTimeout extends ConcurrentAlg implements Segment {
    private static Logger logger = LoggerFactory.getLogger(StagnationTimeout.class);

    @Override
    public void execute(Long tid, List<LocationData> data, StatisticResultEntity entity) {
        if (data == null || data.size() == 0) {
            return;
        }
        Map<String, Object> paras = new ConcurrentHashMap<String, Object>();
        paras.putAll(terminalParameterCache.getTerminalParameters());
        byte[] bytes = (byte[]) paras.get(tid + "_" + AllCommands.Terminal.OvertimeParking_VALUE);
        int limitParking = 0;
        if (bytes != null) {
            try {
                OvertimeParking build = OvertimeParking.parseFrom(bytes);
                limitParking = build.getParkingLimit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        StagnationTimeoutEntity stagnationTimeoutEntity = null;
        for (int i = 0; i < data.size(); i++) {
            LocationData location = data.get(i);
            // 超时停车报警
            if ((location.getAlarm()
                    & LCAllAlarm.Alarm.parkingTimeout_VALUE) == LCAllAlarm.Alarm.parkingTimeout_VALUE) {
                if (stagnationTimeoutEntity == null) {
                    stagnationTimeoutEntity = new StagnationTimeoutEntity();
                    stagnationTimeoutEntity.setTerminalId(tid);
                    stagnationTimeoutEntity.setBeginDate(location.getGpsDate());
                    stagnationTimeoutEntity.setBeginLat(location.getLatitude());
                    stagnationTimeoutEntity.setBeginLng(location.getLongitude());
                    stagnationTimeoutEntity.setEndDate(location.getGpsDate());
                    stagnationTimeoutEntity.setLimitParking(limitParking);
                    stagnationTimeoutEntity.setStatus(1);// 正常
                    if (i != 0) {
                        stagnationTimeoutEntity.setHeadMerge(1);
                    } else {
                        stagnationTimeoutEntity.setHeadMerge(0);
                    }
                }
                stagnationTimeoutEntity.setEndDate(location.getGpsDate());
                stagnationTimeoutEntity
                        .setContinuousTime((int) (location.getGpsDate() - stagnationTimeoutEntity.getBeginDate()));
                if (i == data.size() - 1) {
                    stagnationTimeoutEntity.setTailMerge(0);// 统计未结束，需要合并
                    List<StagnationTimeoutEntity> list = entity.getStagnationTimeouts().get(tid);
                    if (list == null) {
                        list = new ArrayList<>();
                        entity.getStagnationTimeouts().put(tid, list);
                    }
                    list.add(stagnationTimeoutEntity);
                }
            } else {// 状态切换，当前报警事件统计完成，入缓存。
                if (stagnationTimeoutEntity != null) {
                    stagnationTimeoutEntity.setTailMerge(1);
                    List<StagnationTimeoutEntity> list = entity.getStagnationTimeouts().get(tid);
                    if (list == null) {
                        list = new ArrayList<>();
                        entity.getStagnationTimeouts().put(tid, list);
                    }
                    list.add(stagnationTimeoutEntity);
                    stagnationTimeoutEntity = null;
                }
            }
        }

    }

    @Override
    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        // insert到da的结果集
        List<StagnationTimeoutEntity> insertList = new ArrayList<>();
        // update到da的结果集
        List<StagnationTimeoutEntity> updateList = new ArrayList<>();
        AlarmStatisticsQueryService currentServer = (AlarmStatisticsQueryService) getRmiclient().rmiBalancerRequest(RmiConstant.QUERY_STATIC_TER_DATA);
        Map<Long, StagnationTimeoutEntity> oldRecords = currentServer.getStagnationTimeoutAlarmNoEndMerge(
                terminalsCache.getAllTerminals(), ConcurrentAlg.getCurrentDayStart());
        Map<Long, List<StagnationTimeoutEntity>> newRecords = entity.getStagnationTimeouts();
        if (newRecords.isEmpty()) {
            logger.warn("此次计算结果集为空，更新数据库待合并记录为合并完成。");
            return;
        }
        for(Map.Entry<Long, StagnationTimeoutEntity> obj : oldRecords.entrySet()){
            if (newRecords.get(obj.getKey()) == null && obj.getValue().getTailMerge()== 0){
                if (allData.get(obj.getValue().getTerminalId()) == null){
                    continue;
                }else{
                    obj.getValue().setTailMerge(1);
                    updateList.add(obj.getValue());
                }
            }
        }
        for (Long key : newRecords.keySet()) {
            List<StagnationTimeoutEntity> list = newRecords.get(key);
            StagnationTimeoutEntity current = list.get(0);
            StagnationTimeoutEntity old = oldRecords.get(key);
            if (old == null) {
                insertList.addAll(list);
            } else {
                if (current.getHeadMerge() == 1) {
                    old.setTailMerge(1);
                    updateList.add(old);
                } else {
                    old.setEndDate(current.getEndDate());
                    old.setTailMerge(current.getTailMerge());
                    old.setContinuousTime((int) (current.getEndDate() - old.getBeginDate()));
                    updateList.add(old);
                    if (list.size() == 1) {
                        list.clear();
                    } else {
                        old.setTailMerge(1);
                        list.remove(0);
                        insertList.addAll(list);
                    }
                }
            }
        }
        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult insertResult = daServer.saveStagnationTimeoutInfo(insertList, getCurrentDayStart());
        if (PlatformResponseResult.success_VALUE != insertResult.getNumber()) {
            logger.error("StagnationTimeout store failure.");
        }
        PlatformResponseResult updateResult = daServer.updateStagnationTimeoutInfo(updateList, getCurrentDayStart());
        if (PlatformResponseResult.success_VALUE != updateResult.getNumber()) {
            logger.error("StagnationTimeout store update failure.");
        }
        logger.debug(">>>StagnationTimeout数据存儲耗时:{},总结果数:insert={},update={}",
                (System.currentTimeMillis() - startLong) / 1000.0, insertList.size(), updateList.size());

    }

}
