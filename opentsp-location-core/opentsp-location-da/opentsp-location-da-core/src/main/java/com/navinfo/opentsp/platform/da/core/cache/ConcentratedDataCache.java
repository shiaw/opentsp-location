package com.navinfo.opentsp.platform.da.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: ChenJie
 * @Description:密集采集数据缓存
 * @Date 2017/10/19
 * @Modified by:
 */
public class ConcentratedDataCache {
    public static final ConcentratedDataCache instance = new ConcentratedDataCache();

    private ConcentratedDataCache(){}

    private static ConcurrentHashMap<String,Map<Double,String>> cache = new ConcurrentHashMap<String,Map<Double,String>>();

    //缓存记录条数
    private int size =0;

    public static ConcentratedDataCache getInstance(){return instance;}

    public int addCache(String terminalId,String pb,Double gpsTime){
        Map map = cache.get(terminalId);
        if(map == null){
            map = new HashMap<Double,String>();
        }

        map.put(gpsTime,pb);
        cache.put(terminalId,map);

        return ++size ;
    }

    //根据终端id获取缓存数据
    public Map<Double,String> getCache(String terminalId){
        return cache.get(terminalId);
    }

    //获取所有缓存数据
    public Map<String,Map<Double,String>> getCache(){return cache;}

    //清空缓存数据
    public void delCache(){
        cache.clear();
        size = 0;
    }

}
