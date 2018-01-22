package com.navinfo.opentsp.platform.dsa.cache;

import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.location.protocol.common.LCRegularCode.RegularCode;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化规则数据，目前需要加载的有滞留超时阀值、服务站区域（用来做车次统计）。
 *
 * @author hk
 */
@Component
public class TerminalRuleCache {

    private Logger logger = LoggerFactory.getLogger(TerminalRuleCache.class);
    private volatile List<AreaInfo> areas = new ArrayList<AreaInfo>();
    private volatile Map<Long, Integer> areaOverTimeCache = new HashMap<>();
    private volatile Map<Long, Integer> areaStayTimeCache = new HashMap<>();
    @Autowired
    private DARmiClient rmiclient;
    private Object mux = new Object();

    private void initAreaOverTimeInfo() {
        try {
            logger.info("开始加载[滞留超时规则]...");
            long ss = System.currentTimeMillis();
            TermianlRuleAndParaService currentServer = (TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
            // 加载服务站车次统计使用信息。
            Map<Long, Integer> areaOverTimeData = currentServer.getRegularDatasByRegularCode(RegularCode.overtimePark);
            if (null != areaOverTimeData && areaOverTimeData.size() > 0) {
                areaOverTimeCache = areaOverTimeData;
                logger.info("加载[滞留超时规则]完成，共加载条数:{}[耗时:{}]", areaOverTimeCache.size(),
                        (System.currentTimeMillis() - ss) / 1000L);
            } else {
                logger.warn("加载[滞留超时规则]出错，加载结果为空，请检查[耗时:{}]", (System.currentTimeMillis() - ss) / 1000L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAreaInfoForStatistic() {
        try {
            logger.info("开始加载[服务站缓存]...");
            TermianlRuleAndParaService currentServer = (TermianlRuleAndParaService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
            // 加载服务站车次统计使用信息。
            List<AreaInfo> areaInfos = currentServer.getAreaInfoForStatistic();
            if (null != areaInfos && areaInfos.size() > 0) {
                areas = areaInfos;
                logger.info("加载[服务站缓存]完成，共加载数据条数：" + areas.size());
            } else {
                logger.warn("加载[服务站缓存]出错，加载结果为空，请检查");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<AreaInfo> getAreaInfoForStatistic() {
        if (areas.size() < 1) {
            synchronized (mux) {
                if (areas.size() < 1) {
                    initAreaInfoForStatistic();
                }
            }
        }
        return areas;
    }

    public Map<Long, Integer> getAreaOverTimeInfo() {
        if (areaOverTimeCache.size() < 1) {
            synchronized (mux) {
                if (areaOverTimeCache.size() < 1) {
                    initAreaOverTimeInfo();
                }
            }
        }
        return areaOverTimeCache;
    }

    public void reload() {
        areas.clear();
        getAreaInfoForStatistic();
        areaOverTimeCache.clear();
        getAreaOverTimeInfo();
    }
}
