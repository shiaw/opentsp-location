/**
 *
 */
package com.navinfo.opentsp.platform.dsa.cache;

import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 行政区域和瓦片映射缓存 ,基础数据初始化启动的时候加载一次即可——hk
 */
@Component
public class DistrictAndTileMappingCache implements CommandLineRunner {
    private Logger logger = LoggerFactory.getLogger(DistrictAndTileMappingCache.class);

    private Object mux = new Object();
    @Autowired
    private DARmiClient rmiclient;
    /**
     * 行政区域和瓦片映射缓存 Map<行政区域ID，瓦片ID>
     */
    private Map<Long, Integer[]> districtMap = new HashMap<Long, Integer[]>();

    /**
     * 获取所有区域和瓦片的映射
     *
     * @return
     */
    public Map<Long, Integer[]> getAllMap() {
        if (districtMap.isEmpty()) {
            synchronized (mux) {
                if (districtMap.isEmpty()) {
                    init();
                }
            }
        }
        return districtMap;
    }

    private void init() {
        // 从DA获取行政区域和瓦片映射关系
        try {
            TermianlRuleAndParaService currentServer = (TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
            int pageNum = 1;
            int pagesize = 500000;
            long t1 = System.currentTimeMillis();
            long last = t1;
            logger.info("开始加载[行政区域-瓦片映射]...");
            while (true) {
                Map<Long, Integer[]> pageMap = currentServer.getDistrictAndTileMapPage(pageNum, pagesize);
                pageNum++;
                if (null != pageMap && pageMap.size() > 0) {
                    districtMap.putAll(pageMap);
                    if (pagesize > pageMap.size()) {
                        break;
                    }
                } else {
                    break;
                }
                logger.info(pageNum + "   " + (System.currentTimeMillis() - last));
                last = System.currentTimeMillis();
            }
            logger.info("加载[行政区域-瓦片映射]完成，共加载条数：{}[耗时:{}]", districtMap.size(),
                    (System.currentTimeMillis() - t1) / 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
        districtMap.clear();
        getAllMap();
    }

    @Override
    public void run(String... strings) throws Exception {
        this.reload();
    }
}
