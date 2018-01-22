package com.navinfo.opentsp.platform.monitor.common;

import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLastestLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.monitor.wstesttool.BasicWSTT;
import com.navinfo.opentsp.platform.monitor.utils.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.List;

/**
 * Created by machi1 on 2017/6/26.
 */
public class WsService {
    protected static final Logger log = LoggerFactory.getLogger(WsService.class);

    public int getTerminalRegular(List<Long> terminalIds) {
        try {
            CommonParameter commonParameter = new CommonParameter();
   /*         String accessTokenString = Configuration.getString("OVERTIMEPARK.ACCESSTOKEN");
            long accToken = Long.parseLong(accessTokenString);*/
            commonParameter.setAccessTocken(1073);
            commonParameter.setQueryKey("");
            commonParameter.setCode(0);
            commonParameter.setSort(false);
            commonParameter.setSortTerminal(true);

            byte[] result = BasicWSTT.getLastestLocationDataC(terminalIds, "", 2, commonParameter);
            if(result != null){
                return 1;
            }else{
                return 0;
            }
        } catch (Exception e) {
            log.error("调用webService出错:" + e);
        }
        return 0;
    }
}
