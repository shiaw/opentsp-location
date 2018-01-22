package com.navinfo.opentsp.platform.da.core.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhanhk on 2017/5/31.
 */
public class ReportQueryCache {

    private static Map<String, String> cache = new ConcurrentHashMap<>();

    private final static ReportQueryCache instance = new ReportQueryCache();

    private ReportQueryCache() {
    }

    public void add(String value) {
        cache.put(value,value);
    }

    public Map<String,String> getCache() {
        return cache;
    }

    public static ReportQueryCache getInstance() {
        return instance;
    }

    public void clear() {
        cache.clear();
    }
}
