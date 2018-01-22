package com.navinfo.opentsp.platform.rprest.cache;

import com.navinfo.opentsp.platform.rprest.dto.GetLocationDataDto;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hxw on 2017/5/24.
 * 末次位置缓存--5分钟跟新一次
 */
public class LastestLocationCache {

    private static LastestLocationCache instance = new LastestLocationCache();
    private  static Map<String,List<GetLocationDataDto>> mapCache = new ConcurrentHashMap<>();

    private LastestLocationCache(){}

    public static LastestLocationCache getInstance(){
        return  instance;
    }

    public static void addCache(List<GetLocationDataDto> list){
        mapCache.put("location",list);
    }
    public static  List<GetLocationDataDto> getCache(String key){
        return mapCache.get(key);
    }
}
