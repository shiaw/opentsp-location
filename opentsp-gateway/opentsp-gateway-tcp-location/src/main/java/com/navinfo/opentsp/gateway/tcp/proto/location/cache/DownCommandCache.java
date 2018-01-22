package com.navinfo.opentsp.gateway.tcp.proto.location.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wanliang
 * @version 1.0
 * @date 2016/8/2
 * @modify 缓存下发指令命令UUID
 * @copyright opentsp
 */
public class DownCommandCache {

    private static Map<String, String> cache = new ConcurrentHashMap<String, String>();

    private static DownCommandCache instance = new DownCommandCache();
    private DownCommandCache(){}
    public static DownCommandCache getInstance (){
        return instance;
    }


    public Map<String, String> get(){
        Map<String, String> temp = cache;
        return temp;
    }

    public void add(String key , String value){
        cache.put(key, value);
    }

    public void remove(String key){
        cache.remove(key);
    }


    public String get(String key){

        return cache.get(key);
    }

}
