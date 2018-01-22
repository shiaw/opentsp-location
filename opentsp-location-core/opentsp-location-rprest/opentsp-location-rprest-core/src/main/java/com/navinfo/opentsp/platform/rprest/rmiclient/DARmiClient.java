package com.navinfo.opentsp.platform.rprest.rmiclient;

import org.springframework.stereotype.Component;

/**
 * Created by Administrator on 2016/9/22.
 */
@Component
public class DARmiClient extends RmiBalancer {

    public Object rmiBalancerRequest(final String interfaceName) {
        if (interfaceName.equals(RmiConstant.QUERY_STATIC_TER_DATA)) {
            return super.rmiBalancerRequest(RmiConstant.RMI_DA_SERVICE_ID, RmiConstant.QUERY_STATIC_TER_DATA, RmiConstant.RmiClassEnum.AlarmStatisticsQueryService.getClassType());
        } else if (interfaceName.equals(RmiConstant.QUERY_TER_GPS)) {
            return super.rmiBalancerRequest(RmiConstant.RMI_DA_SERVICE_ID, RmiConstant.QUERY_TER_GPS, RmiConstant.RmiClassEnum.TermianlDataService.getClassType());
        } else if (interfaceName.equals(RmiConstant.QUERY_TER_RULES)) {
            return super.rmiBalancerRequest(RmiConstant.RMI_DA_SERVICE_ID, RmiConstant.QUERY_TER_RULES, RmiConstant.RmiClassEnum.TermianlRuleAndParaService.getClassType());
        } else if (interfaceName.equals(RmiConstant.SAVE_STATIC_TER_DATA)) {
            return super.rmiBalancerRequest(RmiConstant.RMI_DA_SERVICE_ID, RmiConstant.SAVE_STATIC_TER_DATA, RmiConstant.RmiClassEnum.AlarmStatisticsStoreService.getClassType());
        }
        return null;
    }
}
