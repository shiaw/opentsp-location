package com.navinfo.opentsp.platform.dp.core.cache;

/**
 * 缓存线程对应DPMonitorCache
 * @author zhanhk
 * @version 1.0
 * @date 2015-10-09
 * @modify
 */
public class DPMonitorCacheHolder {

    private static final ThreadLocal<DPMonitorCache> dpMonitorCacheThreadLocal = new ThreadLocal<>();

    public static void setDPMonitorCache(DPMonitorCache dpMonitorCache){
        dpMonitorCacheThreadLocal.set(dpMonitorCache);
    }

    public static DPMonitorCache getDPMonitorCache(){
        return dpMonitorCacheThreadLocal.get();
    }
}
