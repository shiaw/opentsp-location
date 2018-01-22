package com.navinfo.opentsp.platform.dsa.schedule;

import com.navinfo.opentsp.platform.dsa.cache.*;
import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.*;
import com.navinfo.opentsp.platform.dsa.utils.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Calendar;

@Component
public class AppTaskSched {

    private static Logger logger = LoggerFactory.getLogger(AppTaskSched.class);
    @Autowired
    protected MilleagesCache milleagesCache;
    @Autowired
    protected DistrictAndTileMappingCache districtAndTileMappingCache;
    @Autowired
    private DARmiClient rmiclient;
    @Autowired
    private TerminalsCache terminalsCache;
    @Autowired
    private TerminalRuleCache terminalRuleCache;
    @Autowired
    private TerminalParameterCache terminalParameterCache;
    @Autowired
    private RealTimeService realTimeService;
    @Autowired
    private OffLineService offLineService;
    @Autowired
    private Segment4AreaInfoService segment4AreaInfoService;
    @Autowired
    private SegmentForDistrictService segmentForDistrictService;
    @Autowired
    private SegmentService segmentService;

    @PostConstruct
    public void init() {
//        rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_RULES);
    }

    // 一天有2880段30s,[0,2880)
    @Scheduled(cron = "0/30 * * * * ?")
    public void schedRealtime() {
        try {
            Calendar cal = Calendar.getInstance();
            logger.info("==============================================");
            logger.info("开始执行实时计算任务:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            realTimeService.start(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 一天有288段5m,[0,288)
    @Scheduled(cron = "0 0/5 * * * ?")
    public void schedSegment() {
        try {
            Calendar cal = Calendar.getInstance();
            logger.info("==============================================");
            logger.info("开始执行分段计算任务:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            segmentService.start(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 一天有288段5m,[0,288),有效CAN数据计算
    @Scheduled(cron = "0 0/5 * * * ?")
    public void schedSegment4AreaInfo() {
        try {
            Calendar cal = Calendar.getInstance();
            logger.info("==============================================");
            logger.info("开始执行区域车次算法计算任务:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            segment4AreaInfoService.start(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 一天有288段5m,[0,288),有效经纬度计算
    @Scheduled(cron = "0 0/5 * * * ?")
    public void schedSegmentForDistrict() {
        try {
            Calendar cal = Calendar.getInstance();
            logger.info("==============================================");
            logger.info("开始执行行政区域车次算法计算任务:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            segmentForDistrictService.start(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 离线数据，跑昨天的所有数据
    @Scheduled(cron = "${opentsp.sched.offline.schedule.cron:1 0 0 * * ?}")
    public void schedOffline() {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            logger.info("==============================================");
            logger.info("开始执行离线计算任务:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            offLineService.start(cal);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 本地缓存更新,暂定6分钟更新一次
    @Scheduled(cron = "0 0/6 * * * ?")
    public void schedCache() {
        try {
            Calendar cal = Calendar.getInstance();
            logger.info("==============================================");
            logger.info("缓存更新调度任务开始:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            terminalsCache.reload();
            terminalRuleCache.reload();
            terminalParameterCache.reload();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0/5 * * * ?")
    public void schedClear() {
        try {
            Calendar cal = Calendar.getInstance();
            logger.info("==============================================");
            logger.info("清空分段调度任务开始:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
            TermianlDataService service = (TermianlDataService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_GPS);
            service.delGpsDataSegementThisDay();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @Scheduled(cron = "${opentsp.sched.savegpsdata.schedule.cron:0 0 2 * * ?}")
//    public void saveGpsDataTask() {
//        try {
//            Calendar cal = Calendar.getInstance();
//            logger.info("==============================================");
//            logger.info("redis转存mongo任务开始:{}", DateUtils.date2String(cal.getTime(), DateUtils.ALL));
//            TermianlDataService service = (TermianlDataService) rmiclient.rmiBalancerRequest(RmiConstant.QUERY_TER_GPS);
//            service.saveGpsDataTask();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
}
