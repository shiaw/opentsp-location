package com.navinfo.tasktracker.rprest.entity;

import com.alibaba.fastjson.JSON;
import com.navinfo.tasktracker.rprest.domain.LcDistrictAndTileMapping;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by cj on 2017/8/10.
 * 维护内存中的基础数据集
 */
public class MemoryData {

    private static Logger logger = LoggerFactory.getLogger(MemoryData.class);
    // 瓦片与区域List的对应关系MAP
    public static Map<Long, List<Long>> tileAreaRelationMap = new ConcurrentHashMap();
    // 缓存省份统计结果MAP
    public static Map<String, Set<String>> provCountMap = new ConcurrentHashMap();
    // 缓存地市统计结果MAP
    public static Map<String, Set<String>> cityCountMap = new ConcurrentHashMap();

    // 区域信息MAP
    public static Map<Long, LcDistrictAndTileMapping> AreaInfoM = new ConcurrentHashMap();

    public static void addAreaData(List<LcDistrictAndTileMapping> areList) {
        for (LcDistrictAndTileMapping area : areList) {
            AreaInfoM.put(area.getTileId(), area);
        }
    }
}
