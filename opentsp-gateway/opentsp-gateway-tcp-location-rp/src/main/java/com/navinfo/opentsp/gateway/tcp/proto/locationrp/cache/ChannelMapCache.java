package com.navinfo.opentsp.gateway.tcp.proto.locationrp.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelMapCache {

    private static Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private static ChannelMapCache instance = new ChannelMapCache();

    private ChannelMapCache() {
    }

    public static ChannelMapCache getInstance() {
        return instance;
    }


    public Map<String, String> get() {
        Map<String, String> temp = cache;
        return temp;
    }

    public void add(String key, String value) {
        cache.put(key, value);
    }

    public void remove(String key) {
        cache.remove(key);
    }


    public String get(String key) {
        return cache.get(key);
    }

}
