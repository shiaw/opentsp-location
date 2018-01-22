package com.navinfo.opentsp.platform.dsa.service;

import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlgOffline;
import com.navinfo.opentsp.platform.dsa.service.impl.offline.VehiclePassAreaTimes;
import com.navinfo.opentsp.platform.dsa.service.impl.offline.VehiclePassGridTimes;
import com.navinfo.opentsp.platform.dsa.service.interf.Dealer;
import com.navinfo.opentsp.platform.dsa.service.interf.Offline;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

@Component
@Scope("prototype")
public class OffLineService extends RealTimeService {

    @Autowired
    private VehiclePassAreaTimes vehiclePassAreaTimes;
    @Autowired
    private VehiclePassGridTimes vehiclePassGridTimes;

    @Autowired
    private List<Offline> dealers;

    private void startAllProcess(final Map<Long, List<LocationData>> allData, long start, long end, Map<String, StatisticResultEntity> result) {
        Map<String, Future<StatisticResultEntity>> allSubmits = new HashMap<String, Future<StatisticResultEntity>>();
        ExecutorService pool = Executors.newFixedThreadPool(getDealer().size());
        try {
            for (Dealer tmp : getDealer()) {
                ConcurrentAlgOffline newInstance = (ConcurrentAlgOffline) tmp;
                newInstance.st = start;
                newInstance.ed = end;
                newInstance.allData = allData;
                newInstance.entity = result.get(tmp.getClass().getSimpleName());
                allSubmits.put(tmp.getClass().getSimpleName(), pool.submit(newInstance));
            }
            pool.shutdown();
            for (String key : allSubmits.keySet()) {
                result.put(key, allSubmits.get(key).get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start(Calendar cal) throws Exception {
        // 根据日期计算开始和结束时间点
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.add(Calendar.SECOND, -1);
        // cal.add(Calendar.DATE, 1);
        long end = cal.getTimeInMillis() / 1000;
        cal.add(Calendar.SECOND, 1);
        cal.add(Calendar.DATE, -1);
        long start = cal.getTimeInMillis() / 1000;
        List<Long> tids = new ArrayList<Long>();
        List<Long> tidsFromCache = terminalsCache.getAllTerminals();
        if (tidsFromCache == null || tidsFromCache.size() == 0) {
            return;
        } else {
            tids.addAll(tidsFromCache);
        }
        // 分批次获取终端数据
        int batch = 200;
        int ceil = (int) Math.ceil(tids.size() * 1.0 / batch);
        if (ceil == 0) {
            ceil = 1;
        }
        Map<String, StatisticResultEntity> allResult = new HashMap<String, StatisticResultEntity>();
        for (int i = 0; i < ceil; i++) {
            int s = i * batch;
            int e = (i + 1) * batch;
            if (i == ceil - 1) {
                e = tids.size();
            }
            List<Long> subList = new ArrayList<>();
            subList.addAll(tids.subList(s, e));
            Map<Long, List<LocationData>> allData = rMIConnectCache.getHistoryLocationData(subList,
                    start, end);
            startAllProcess(allData, start, end, allResult);
        }
        for (String key : allResult.keySet()) {
            logger.info("离线算法{}计算结果集", key);
            if (key.equals(VehiclePassAreaTimes.class.getSimpleName())) {
                vehiclePassAreaTimes.saveResult(allResult.get(key));
            } else if (key.equals(VehiclePassGridTimes.class.getSimpleName())) {
                vehiclePassGridTimes.saveResult(allResult.get(key));
            } else {
                logger.error("离线算法数据报错失败:{}", key);
            }
        }

    }

    public List<Dealer> getDealer() {
        List<Dealer> ds = new ArrayList<Dealer>();
        ds.addAll(dealers);
        return ds;
    }
}
