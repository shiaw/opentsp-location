package com.navinfo.opentsp.platform.dsa.rmiclient;

import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsQueryService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlDataService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlRuleAndParaService;

public class RmiConstant {
    /**
     * 服务ID
     */
    public static final String RMI_DA_SERVICE_ID = "opentsp-location-da";

    public static final String QUERY_TER_GPS = "TermianlDataService"; // 查询轨迹数据
    public static final String SAVE_STATIC_TER_DATA = "AlarmStatisticsStoreService";// 统计结果存储
    public static final String QUERY_STATIC_TER_DATA = "AlarmStatisticsQueryService";// 查询结果存储
    public static final String QUERY_TER_RULES = "TermianlRuleAndParaService"; // 查询终端规则数据


    public enum RmiClassEnum {
        TermianlDataService(TermianlDataService.class),// 查询轨迹数据
        AlarmStatisticsStoreService(AlarmStatisticsStoreService.class),// 统计结果存储
        AlarmStatisticsQueryService(AlarmStatisticsQueryService.class),// 查询结果存储
        TermianlRuleAndParaService(TermianlRuleAndParaService.class);// 查询终端规则数据

        private final Class classType;

        RmiClassEnum(Class classType) {
            this.classType = classType;
        }

        public Class getClassType() {
            return classType;
        }
    }
}
