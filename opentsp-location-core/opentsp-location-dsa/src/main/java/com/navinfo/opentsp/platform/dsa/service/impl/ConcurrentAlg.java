package com.navinfo.opentsp.platform.dsa.service.impl;

import com.navinfo.opentsp.platform.dsa.cache.*;
import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.service.RealTimeService;
import com.navinfo.opentsp.platform.dsa.utils.ConfigUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * 描述: 所有并发业务计算类都需要继承此抽象类
 *
 * @author admin
 * @version CopyRight (c) 2016 , hbdrawn@vip.qq.com All Rights Reserved
 * @date 2016年4月29日 下午1:39:56
 **/

public abstract class ConcurrentAlg implements Callable<Integer> {

    public static final String KEYSEPERATOR = "#";
    public Logger logger = LoggerFactory.getLogger(getClass());
    public int currentSeg; // 当前计算的分段
    public Map<Long, List<LocationData>> allData;// 历史数据
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
    private DARmiClient rmiclient;
    @Autowired
    protected StaytimeParkCache staytimeParkCache;

    public ConcurrentAlg() {
    }

    public ConcurrentAlg(int currentSeg, Map<Long, List<LocationData>> allData) {
        this.currentSeg = currentSeg;
        this.allData = allData;
    }

    public static long getCurrentDayStart() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis() / 1000;
    }

    public DARmiClient getRmiclient() {
        return rmiclient;
    }

    public void setRmiclient(DARmiClient rmiclient) {
        this.rmiclient = rmiclient;
    }

    @Override
    public Integer call() {
        // 分段检查是否参与计算
        if (!isRunning()) {
            return -2;
        }

        try {
            // 发起计算算法，并返回计算结果
            StatisticResultEntity entity = excute();
            // 将计算结果入库保存
            saveResult(entity);
            // 将此次操作结果保存到配置文件
            ConfigUtils.writePros(getClass().getSimpleName() + RealTimeService.FINISHSEG, String.valueOf(currentSeg));
        } catch (Exception e) {
            logger.error("算法{}计算失败:currentSeg={}", getClass().getSimpleName(), currentSeg);
            e.printStackTrace();
            return -1;
        }

        return currentSeg;
    }

    protected StatisticResultEntity excute() {
        long startLong = System.currentTimeMillis();
        StatisticResultEntity result = new StatisticResultEntity();
        for (Long tid : allData.keySet()) {
            try {
                execute(tid, allData.get(tid), result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug(">>>计算数据耗时:{}", (System.currentTimeMillis() - startLong) / 1000.0);
        return result;
    }

    /*
     * 算法类的具体实现
     *
     * @param tid 终端ID
     *
     * @param GpsDataEntity 该终端所有的数据
     *
     * @return StatisticAlarmEntity 统计结果
     */
    public abstract void execute(Long tid, List<LocationData> gpsDataEntity, StatisticResultEntity entity);

    // 将结果入库保存
    public abstract void saveResult(StatisticResultEntity entity) throws Exception;

    /*
     * 根据当前计算段位，判断是否需要满足执行条件，避免重复计算
     *
     * @return boolean false=终止此次计算 true=进行计算
     */
    public boolean isRunning() {
        boolean isRunning = false;
        String segFinished = ConfigUtils.getProByRealTime(getClass().getSimpleName(), "-1");
        Integer valueOf = Integer.valueOf(segFinished);
        // ==-1时为初始化状态，即系统第一次运行
        if (valueOf == -1 || valueOf + 1 == currentSeg) {
            isRunning = true;
        } else {
            logger.warn("此次结果集已生成，忽略执行[currentSeg={},finishedSeg={}]", currentSeg, segFinished);
        }

        return isRunning;
    }
}
