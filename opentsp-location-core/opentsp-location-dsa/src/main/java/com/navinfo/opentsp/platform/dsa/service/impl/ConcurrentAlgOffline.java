package com.navinfo.opentsp.platform.dsa.service.impl;

import com.navinfo.opentsp.platform.dsa.cache.DistrictAndTileMappingCache;
import com.navinfo.opentsp.platform.dsa.cache.TerminalRuleCache;
import com.navinfo.opentsp.platform.dsa.rmiclient.DARmiClient;
import com.navinfo.opentsp.platform.dsa.utils.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDataEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
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

public abstract class ConcurrentAlgOffline implements Callable<StatisticResultEntity> {

    public Logger logger = LoggerFactory.getLogger(getClass());

    public long st; // 起始时间节点
    public long ed; // 结束时间节点

    public Map<Long, List<LocationData>> allData;// 历史数据

    public StatisticResultEntity entity; // 上一次的结果集缓存
    @Autowired
    protected DistrictAndTileMappingCache districtAndTileMappingCache;
    @Autowired
    protected TerminalRuleCache terminalRuleCache;
    @Autowired
    private DARmiClient rmiclient;

    public ConcurrentAlgOffline() {
    }

    public ConcurrentAlgOffline(int start, int end, Map<Long, GpsDataEntity> allData) {
        this.st = start;
        this.ed = end;
    }

    public DARmiClient getRmiclient() {
        return rmiclient;
    }

    public void setRmiclient(DARmiClient rmiclient) {
        this.rmiclient = rmiclient;
    }

    @Override
    public StatisticResultEntity call() throws ParseException {
        // StatisticResultEntity result = null;
        try {
            // 发起计算算法，并返回计算结果
            if (entity == null) {
                entity = new StatisticResultEntity();
            }
            excute(entity);
        } catch (Exception e) {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(st);
            logger.error("算法{}计算失败:startTime={},allData={}", getClass().getSimpleName(),
                    DateUtils.date2String(cal.getTime(), DateUtils.ALL), allData.size());
            e.printStackTrace();
        }

        return entity;
    }

    private void excute(StatisticResultEntity result) {
        long startLong = System.currentTimeMillis();
        // StatisticResultEntity result = new StatisticResultEntity();
        for (Long tid : allData.keySet()) {
            try {
                execute(tid, allData.get(tid), result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        logger.debug(">>>计算数据[{}]耗时:{}", getClass().getSimpleName(), (System.currentTimeMillis() - startLong) / 1000.0);
        // return result;
    }

    /*
     * 算法类的具体实现,实现一个终端的计算逻辑
     *
     * @param tid 终端ID
     *
     * @param GpsDataEntity 该终端所有的数据
     *
     * @return StatisticAlarmEntity 统计结果
     */
    public abstract void execute(Long tid, List<LocationData> gpsDataEntity, StatisticResultEntity entity);

    // 将结果入库保存
    // 将所有结果合并后入库逻辑
    // public abstract void saveResult(StatisticResultEntity entity)
    // throws Exception;

}
