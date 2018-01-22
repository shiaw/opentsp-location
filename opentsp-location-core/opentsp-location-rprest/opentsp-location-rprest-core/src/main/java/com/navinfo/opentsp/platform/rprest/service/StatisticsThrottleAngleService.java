package com.navinfo.opentsp.platform.rprest.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

/**
 * 油门开度统计分析
 * Created by Sunyu on 2017/11/29.
 */
@Service
public class StatisticsThrottleAngleService {

    private static final Logger logger = LoggerFactory.getLogger(StatisticsThrottleAngleService.class);

    /**
     * 指定时间范围内油门开度比例统计报表
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param id 车辆通讯号
     * @return Map<String, Object> 油门开度比例统计
     */
    public Map<String, Object> statistics(Date startDate, Date endDate, Long id) {
        return null;
    }
}