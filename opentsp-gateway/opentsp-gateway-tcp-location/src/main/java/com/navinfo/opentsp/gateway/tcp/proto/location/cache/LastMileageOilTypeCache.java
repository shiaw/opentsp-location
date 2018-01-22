package com.navinfo.opentsp.gateway.tcp.proto.location.cache;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilType;
import com.navinfo.opentsp.gateway.tcp.proto.location.service.TerminalMileageOilTypeService;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LastMileageOilTypeCache {
    private static Logger log = LoggerFactory.getLogger(LastMileageOilTypeCache.class);


    @Autowired
    TerminalMileageOilTypeService terminalMileageOilTypeService;
    private  Long lastRefreshTime=0L;
    /**
     * 最后一条0200数据时间。
     */
    private static Map<String,Long> lastLocationTimeCache=new ConcurrentHashMap<>();

    /**
     * 最后一条0705 CAN数据时间
     */
    private static Map<String,Long> lastCanTimeCache=new ConcurrentHashMap<>();

    private static Map<String,TerminalMileageOilType> oilMileageCache=new ConcurrentHashMap<>();

    //存储最近一次有效的里程、油耗数据。
    private static Map<String, Map<LCStatusType.StatusType, Long>> lastMileageOilCache = new ConcurrentHashMap<>();

    public void addLastMileageOilCache(String uniqemark, Map<LCStatusType.StatusType, Long> cache) {
        lastMileageOilCache.put(uniqemark, cache);
    }

    public Map<LCStatusType.StatusType, Long> getLastMileageOilCache(String uniqemark) {
        return lastMileageOilCache.get(uniqemark);
    }

    public void delLastMileageOilCache(String uniqemark) {
        if (lastMileageOilCache.containsKey(uniqemark)) {
            lastMileageOilCache.remove(uniqemark);
        }
    }

    public void addLastLocationTimeCache(String uniquemark,Long gpsTime){
        lastLocationTimeCache.put(uniquemark,gpsTime);
    }

    public Long getLastLocationTime(String uniquemark){
        return lastLocationTimeCache.get(uniquemark);
    }


    public void addLastCanTimeCache(String uniquemark,Long gpsTime){
        lastCanTimeCache.put(uniquemark,gpsTime);
    }

    public Long getLastCanTime(String uniquemark){
        return lastCanTimeCache.get(uniquemark);
    }


    public void addOilMileageCache(String uniquemark,TerminalMileageOilType terminalMileageOilType){
        oilMileageCache.put(uniquemark,terminalMileageOilType);
    }

    public TerminalMileageOilType getOilMileageTime(String uniquemark){
        return oilMileageCache.get(uniquemark);
    }

    public void loadOilMileageType(){
        Long currentTime=System.currentTimeMillis()/1000;
        List<TerminalMileageOilType> temp=terminalMileageOilTypeService.getMileageOil(lastRefreshTime);
        if (temp!=null&&temp.size()>0){
            for (TerminalMileageOilType terminal:temp){
                addOilMileageCache("0"+terminal.getTerminalId(),terminal);
            }
            log.info("获取新增（修改）标记共:"+temp.size()+"");
            lastRefreshTime=currentTime-60*60;//默认往前扫描1个小时，防止有数据被遗漏
        }

    }
}
