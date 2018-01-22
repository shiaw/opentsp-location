package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatus;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Lenovo
 * @date 2016-12-13
 * @modify
 * @copyright
 */
public class TerminalOnLineData {
    static RedisMapDaoImpl mapDao = new RedisMapDaoImpl();
    /*
     * 将redis中存储的车辆在线状态全部初始化为离线
     */
    public static void InitOff(){
        TerminalOnOffStatusServiceImpl onOffStatus=new TerminalOnOffStatusServiceImpl();
        Map<Long, TerminalOnOffStatus> statusMap=onOffStatus.getAllTerminalStatus();
        List<TerminalOnOffStatus> statusList=new ArrayList<>();
        if (statusMap != null) {
            for (Map.Entry<Long, TerminalOnOffStatus> entry : statusMap.entrySet()) {
                try {
                    TerminalOnOffStatus status=entry.getValue();
                    status.setStatus(0);
                    status.setEndTime(System.currentTimeMillis()/1000);
                    statusList.add(status);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        }

        onOffStatus.saveAllterminalStatus(statusList);
    }
}

