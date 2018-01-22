package com.navinfo.opentsp.platform.monitor.cache;

import com.navinfo.opentsp.platform.location.kit.LocationMonitor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangyue on 2017/6/15.
 */
public class LocationMonitorCache {
    private static LocationMonitorCache instance = new LocationMonitorCache();
    private  static Map<String,LocationMonitor> mapCache = new ConcurrentHashMap<>();

    private LocationMonitorCache(){}

    public static LocationMonitorCache getInstance(){
        return  instance;
    }
    public static  Map<String,LocationMonitor>  getCache(){
        return mapCache;
    }

    public static void addCache(String key,LocationMonitor value){
        mapCache.put(key,value);
    }

    public static  LocationMonitor getCache(String key){
        return mapCache.get(key);
    }

}
