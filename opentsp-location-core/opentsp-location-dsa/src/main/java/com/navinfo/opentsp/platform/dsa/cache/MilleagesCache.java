package com.navinfo.opentsp.platform.dsa.cache;

import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.Mileages;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

@Component
public class MilleagesCache implements CommandLineRunner {

    private Logger logger = LoggerFactory.getLogger(MilleagesCache.class);
    private volatile Map<Long, Mileages> allMileages = new HashMap<Long, Mileages>();

    private Object mux = new Object();
    @Autowired
    private DARmiClient rmiclient;
    /*
     * 初始化方法，从da拉取数据，并初始化到本地缓存
     */
    private void init() {
        try {
            logger.info("开始加载[里程缓存]...");
            long ss = System.currentTimeMillis();
            AlarmStatisticsQueryService currentServer = (AlarmStatisticsQueryService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_STATIC_TER_DATA);
            Map<Long, Mileages> map = currentServer.getAllMileageAndOilDataFromRedis();
            if (map != null && map.size() > 0) {
                allMileages.putAll(map);
                logger.info("加载[里程缓存]成功, 条数:{}[耗时:{}]", allMileages.size(), (System.currentTimeMillis() - ss) / 1000L);
            } else {
                logger.warn("初始化[里程缓存]出错，加载结果为0，请检查[耗时:{}]", (System.currentTimeMillis() - ss) / 1000L);
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Map<Long, Mileages> getAll() {
        if (allMileages.size() < 1) {
            synchronized (mux) {
                if (allMileages.size() < 1) {
                    init();
                }
            }
        }

        return allMileages;
    }

    public void reload() {
        allMileages.clear();
        getAll();
    }

    @Override
    public void run(String... strings) throws Exception {
        this.reload();
    }
}
