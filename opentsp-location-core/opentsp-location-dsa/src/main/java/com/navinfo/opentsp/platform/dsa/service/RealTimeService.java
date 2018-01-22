package com.navinfo.opentsp.platform.dsa.service;

import com.navinfo.opentsp.platform.dsa.cache.*;
import com.navinfo.opentsp.platform.dsa.rmiclient.RMIConnectCache;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.Dealer;
import com.navinfo.opentsp.platform.dsa.service.interf.RealTime;
import com.navinfo.opentsp.platform.dsa.utils.ClassUtils;
import com.navinfo.opentsp.platform.dsa.utils.ConfigUtils;
import com.navinfo.opentsp.platform.dsa.utils.DateUtils;
import com.navinfo.opentsp.platform.dsa.utils.SegmentUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class RealTimeService {

    public static final String FINISHSEG = ".finish.seg";
    public static final String RLBATCHSWITCH = "batch.realtime.switch";
    public static final String SGBATCHSWITCH = "batch.segment.switch";
    public static final String SGAREAINFOBATCHSWITCH = "batch.segment4areainfo.switch";
    public static final String SGDISTRICTBATCHSWITCH = "batch.SegmentForDistrictService.switch";
    public static final String OFFLINEBATCHSWITCH = "batch.offline.switch";
    public static final String RLPACKAGEPATH = "com.navinfo.opentsp.platform.dsa.service.impl.realtime";
    public static final String SGAREAINFOPACKAGEPATH = "com.navinfo.opentsp.platform.dsa.service.impl.segareainfo";
    public static final String SGDISTRICTPACKAGEPATH = "com.navinfo.opentsp.platform.dsa.service.impl.segdistrict";
    public static final String SGPACKAGEPATH = "com.navinfo.opentsp.platform.dsa.service.impl.segment";
    public static final String OFFLINEPACKAGEPATH = "com.navinfo.opentsp.platform.dsa.service.impl.offline";
    public static final long HISTORYSECONDS = 1200000;
    public static Map<String, Integer> errorCountMap = new HashMap<String, Integer>();
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    protected RMIConnectCache rMIConnectCache;
    @Autowired
    protected MilleagesCache milleagesCache;
    @Autowired
    protected TerminalRuleCache terminalRuleCache;
    @Autowired
    protected DistrictAndTileMappingCache districtAndTileMappingCache;
    @Autowired
    protected TerminalsCache terminalsCache;
    @Autowired
    protected TerminalParameterCache terminalParameterCache;
    @Autowired
    private List<RealTime> dealers;

    private boolean startAllProcess(final Map<Long, List<LocationData>> allData, int currentSeg) {
        boolean result = true;
        Map<String, Future<Integer>> allSubmits = new HashMap<String, Future<Integer>>();
        ExecutorService pool = Executors.newFixedThreadPool(getDealer().size());
        try {
            for (Dealer tmp : getDealer()) {
                ConcurrentAlg newInstance = (ConcurrentAlg) tmp;
                newInstance.currentSeg = currentSeg;
                newInstance.allData = allData;
                allSubmits.put(tmp.getClass().getSimpleName(), pool.submit(newInstance));
            }
            pool.awaitTermination(60 * 60 * 1000, TimeUnit.MICROSECONDS);
            pool.shutdown();
            for (String key : allSubmits.keySet()) {
                Integer integer = allSubmits.get(key).get();
                if (integer == -1) {
                    result = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void start(Calendar cal) throws Exception {
        // 读取配置文件，获取相关算法类,找出最早的时间节点
        List<Class<?>> realTimeClasses = ClassUtils.getClasses(getPackagePath());
        int min = -1;
        for (Class<?> tmp : realTimeClasses) {
            String proByRealTime = ConfigUtils.getProByRealTime(tmp.getSimpleName() + FINISHSEG, getLastTime() + "");
            Integer valueOf = Integer.valueOf(proByRealTime);
            if (min == -1) {
                min = valueOf;
            }
            if (min > valueOf) {
                min = valueOf;
            }
        }

        // 正常发起条件：min = getSeg(cal) - 1
        int currentSeg = getSegmentByDate(cal);
        Map<Long, List<LocationData>> allData = null;
        List<Long> tids = new ArrayList<Long>();
        List<Long> tidsFromCache = terminalsCache.getAllTerminals();
        if (tidsFromCache == null || tidsFromCache.size() == 0) {
            long rtEndTime = getEndTime(currentSeg);
            logger.warn(">>>终端为空此次忽略执行:{}:{}-{}", currentSeg,
                    DateUtils.long2Datestr(getStartTime(rtEndTime), DateUtils.ALL),
                    DateUtils.long2Datestr(rtEndTime, DateUtils.ALL));
            return;
        } else {
            tids.addAll(tidsFromCache);
        }
        long startTime = System.currentTimeMillis();
        boolean isNull = false;
        if (min == currentSeg - 1 || min == getLastTime()) {
            // 1正常拉取数据，调用实时接口
            allData = getData(tids, currentSeg);
            logger.info("拉取基础数据耗时:{}", (System.currentTimeMillis() - startTime) / 1000.0);
            // 2数据不为空的情况下，并发开启计算线程
            if (allData != null && allData.size() > 0) {
                startAllProcess(allData, currentSeg);
            } else {
                logger.warn("拉取基础数据为空");
                isNull = true;
            }
        } else {
            String switchStr = ConfigUtils.getProByRealTime(getBatchSwitch(), "0");
            int switchValue = Integer.valueOf(switchStr);
            if (switchValue == 1) {
                Integer integer = errorCountMap.get(getBatchSwitch());
                if (integer == null) {
                    errorCountMap.put(getBatchSwitch(), 1);
                } else {
                    errorCountMap.put(getBatchSwitch(), 1 + integer);
                }

                if (integer != null && integer.intValue() >= 3) {
                    errorCountMap.put(getBatchSwitch(), 0);
                    logger.warn("批量算法恢复超过三次，清理标志位，自动恢复执行:{}", currentSeg);
                } else {
                    logger.warn("批量算法正在执行，本次触发将忽略处理:{}", currentSeg);
                    return;
                }

            }
            // 将全局定时开关关闭，直到
            // 1 循环次数：min+1->currentSeg
            // 循环拉取数据，并发起并发线程
            if (min + 1 > currentSeg) {
                logger.error("系统批量恢复不支持隔天任务，请联系管理员处理");
                return;
            }
            try {
                logger.info("批量恢复开始执行:{}", currentSeg);
                ConfigUtils.writePros(getBatchSwitch(), "1");
                long end = getEndTime(currentSeg);
                long start = getEndTime(min);
                // long start = getStartTime(send);

                // 分段回滚规则：以20分钟为一次，每次5w个终端
                int ceil = (int) Math.ceil((end - start) * 1.0 / HISTORYSECONDS);
                logger.debug("end={},start={},ceil={}", end, start, ceil);
                for (int i = 0; i < ceil; i++) {
                    long ss = start + i * HISTORYSECONDS;
                    long ee = ss + HISTORYSECONDS;
                    if (i == ceil - 1) {
                        ee = end;
                    }

                    int startSeg = 0, endSeg = 0;
                    if (logger.isWarnEnabled()) {
                        Calendar calLog = Calendar.getInstance();
                        calLog.setTimeInMillis(ss);
                        startSeg = getSegmentByDate(calLog) + 1;
                        calLog.setTimeInMillis(ee);
                        endSeg = getSegmentByDate(calLog);
                    }
                    startTime = System.currentTimeMillis();
                    allData = rMIConnectCache.getHistoryLocationData(tids, ss / 1000, ee / 1000);
                    logger.info("拉取基础数据耗时:{}", (System.currentTimeMillis() - startTime) / 1000.0);
                    if (allData != null && allData.size() > 0) {
                        boolean result = startAllProcess(allData, currentSeg);
                        if (!result) {
                            logger.error(
                                    "批量分批[{}]回滚计算出错",
                                    DateUtils.long2Datestr(ss, DateUtils.ALL) + "->"
                                            + DateUtils.long2Datestr(ee, DateUtils.ALL));
                            ConfigUtils.writePros(getBatchSwitch(), "0");
                            return;
                        }
                        logger.info("批量分批[{}]计算完毕:segment={}", DateUtils.long2Datestr(ss, DateUtils.ALL) + "->"
                                + DateUtils.long2Datestr(ee, DateUtils.ALL), startSeg + " -> " + endSeg);
                    } else {
                        logger.warn("批量分批[{}]计算异常，历史数据获取为空:segment={}", DateUtils.long2Datestr(ss, DateUtils.ALL)
                                + "->" + DateUtils.long2Datestr(ee, DateUtils.ALL), startSeg + " -> " + endSeg);
                        isNull = true;
                    }
                }

                // ConfigUtils.writePros(getBatchSwitch(), "0");

            } catch (Exception e) {
                logger.error("批量恢复执行异常:{}", e.getLocalizedMessage());
                e.printStackTrace();
            } finally {

                ConfigUtils.writePros(getBatchSwitch(), "0");
            }
        }
        // 批量回滚操作完毕后，将开关打开
        if (isNull) {
            for (Class<?> tmp : realTimeClasses) {
                String proByRealTime = ConfigUtils
                        .getProByRealTime(tmp.getSimpleName() + FINISHSEG, getLastTime() + "");
                Integer valueOf = Integer.valueOf(proByRealTime);
                if (valueOf < currentSeg) {
                    ConfigUtils.writePros(tmp.getSimpleName() + FINISHSEG, currentSeg + "");
                    logger.warn("此次计算因为有历史结果为空，易忽略请将结果标记为完成：{}", currentSeg);
                }
            }
        }
    }

    protected int getLastTime() {
        return 2879;
    }

    protected String getBatchSwitch() {
        return RLBATCHSWITCH;
    }

    protected String getPackagePath() {
        return RLPACKAGEPATH;
    }

    protected int getSegmentByDate(Calendar cal) {
        return SegmentUtils.getRTSeg(cal);
    }

    protected long getStartTime(long endTime) {
        return endTime - 30000;
    }

    protected long getEndTime(int segment) {
        return SegmentUtils.getRTEndTime(segment).getTimeInMillis();
    }

    protected Map<Long, List<LocationData>> getData(List<Long> tids, int segment) throws Exception {
        //0：有效位置（经纬度）1：有效CAN数据 2：所有数据
        return rMIConnectCache.getLastLocationData(tids, 1);
    }

    public List<Dealer> getDealer() {
        List<Dealer> ds = new ArrayList<Dealer>();
        ds.addAll(dealers);
        return ds;
    }
}
