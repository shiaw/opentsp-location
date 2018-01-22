package com.navinfo.opentsp.platform.monitor.wstesttool;

import com.lc.rp.webService.service.BasicDataQueryWebService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.monitor.common.ClientWebservice;

import java.util.List;

/**
 * Created by machi1 on 2017/6/26.
 */
public class BasicWSTT {

    /**
     * @param terminalIds
     * @param condition
     * @param type
     * @param commonParameter
     * @return
     * @throws Exception
     */
    public static byte[] getLastestLocationDataC(List<Long> terminalIds, String condition, int type, CommonParameter commonParameter) throws Exception{
        BasicDataQueryWebService service = (BasicDataQueryWebService) ClientWebservice.getWebservice(BasicDataQueryWebService.class);
        return service.getLastestLocationData(terminalIds,condition, type,commonParameter);
    }
}
