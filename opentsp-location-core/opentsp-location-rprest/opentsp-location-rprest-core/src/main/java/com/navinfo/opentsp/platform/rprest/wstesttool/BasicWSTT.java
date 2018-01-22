package com.navinfo.opentsp.platform.rprest.wstesttool;

import com.lc.rp.webService.service.DataAnalysisWebService;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.rprest.common.ClientWebservice;
import java.util.List;

public class BasicWSTT
{
    public static byte[] getStaytimeParkRecords(List<Long> terminalIds, List<Long> areaIds, long startDate, long endDate)
    {
        try
        {
            CommonParameter commonParameter = new CommonParameter();
            commonParameter.setAccessTocken(1073L);
            commonParameter.setQueryKey("");
            commonParameter.setCode(0L);
            commonParameter.setSort(false);
            commonParameter.setSortTerminal(true);
            DataAnalysisWebService service = (DataAnalysisWebService)ClientWebservice.getWebserviceD(DataAnalysisWebService.class);
            return service.getStaytimeParkRecords(terminalIds, areaIds, startDate, endDate, commonParameter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}