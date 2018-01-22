package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.common.tile.AreaToTile;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用规则区域数据缓存<br>
 * Map<区域ID,区域信息> <br>
 * 参数：{@link AreaType} 预留
 *
 * @author lgw
 */
@Component("areaCommonCache")
public class AreaCommonCache {

    public static Logger log = LoggerFactory.getLogger(AreaCommonCache.class);

    private static Map<Long, AreaEntity> cache = new ConcurrentHashMap<>();

    private static Map<Long, List<Long>> areaTiles = new HashMap<>();

    private static int zoom = 13;

    @Resource
    private IRedisService redisService;// redis 通用dao

    private AreaCommonCache() {
    }

    /*****************************
     * 添加区域<br>
     * 如果超过终端允许设置的最大区域数<br>
     * 则删除最老的一个区域<br>
     * 并返回删除的区域信息
     *
     * @param areaEntity {@link AreaEntity}
     * @return {@link AreaEntity}
     */
    public AreaEntity addAreaEntity(AreaEntity areaEntity) {
        redisService.hset(Constant.AREA_COMMON_CACHE_KEY, String.valueOf(areaEntity.getAreaId()), areaEntity);
        cache.put(areaEntity.getAreaId(), areaEntity);
        List<Long> list = AreaToTile.areaToTiles(areaEntity, zoom);
        for (Long tileId : list) {
            List<Long> areaIds = areaTiles.get(tileId);
            if (areaIds == null) {
                areaIds = new ArrayList<>();
                areaTiles.put(tileId, areaIds);
            }
            if (!areaIds.contains(areaEntity.getAreaId())){
                areaIds.add(areaEntity.getAreaId());
            }
        }
        return areaEntity;
    }

    /**
     * 初始化加载
     * @param areaEntity
     * @return
     */
    public AreaEntity initAddAreaEntity(AreaEntity areaEntity) {
        cache.put(areaEntity.getAreaId(), areaEntity);
        List<Long> list = AreaToTile.areaToTiles(areaEntity, zoom);
        for (Long tileId : list) {
            List<Long> areaIds = areaTiles.get(tileId);
            if (areaIds == null) {
                areaIds = new ArrayList<>();
                areaTiles.put(tileId, areaIds);
            }
            if (!areaIds.contains(areaEntity.getAreaId())){
                areaIds.add(areaEntity.getAreaId());
            }
        }
        return areaEntity;
    }

    public Map<Long, AreaEntity> getAreaEntity() {
        return cache;
    }

    public Map<Long, List<Long>> getAreaTiles(){
        return areaTiles;
    }

    /**
     * 批量set
     * @param key
     * @param mapCache
     */
    public void batchSet(String key , Map mapCache) {
        redisService.batchSet(key,mapCache);
    }

    public AreaEntity getAreaEntity(long areaId) {
        if(cache.containsKey(areaId)) {
            return cache.get(areaId);
        }
        return redisService.getHashValue(Constant.AREA_COMMON_CACHE_KEY, String.valueOf(areaId), AreaEntity.class);
    }

    public boolean areaCommonCacheisExists(long areaId) {
        if (redisService.exists(Constant.AREA_COMMON_CACHE_KEY, areaId)) {
            return true;
        }
        return false;
    }

    /***********************
     * 删除区域实体
     * <p>
     * 根据终端编号 删除
     *
     * @param areaId
     * @return
     */
    public void removeAreaEntity(long areaId) {
        AreaEntity areaEntity = null;
        if(cache.containsKey(areaId)) {
            areaEntity = cache.get(areaId);
        }else{
            areaEntity = redisService.getHashValue(Constant.AREA_COMMON_CACHE_KEY, String.valueOf(areaId), AreaEntity.class);
        }
        if (areaEntity != null){
            List<Long> list = AreaToTile.areaToTiles(areaEntity, zoom);
            for (Long tileId : list) {
                List<Long> areaIds = areaTiles.get(tileId);
                if (areaIds!=null){
                    if (areaIds.contains(areaId)){
                        areaIds.remove(areaId);
                    }
                }
            }
        }
        redisService.delete(Constant.AREA_COMMON_CACHE_KEY, String.valueOf(areaId));
    }
    /***************************
     * 删除区域实体 根据终端编号，区域编号 删除
     *
     * @param terminalId
     * @param originalAreaId
     * @param areaType {@link AreaType} 预留
     * @return
     */
    // public boolean removeAreaEntity(long areaId , AreaType areaType) {
    // Map<Long, AreaEntity> map = cache.get(originalAreaId);
    // if(map != null){
    // Iterator<Map.Entry<Long,AreaEntity>> iterator = map.entrySet().iterator();
    // while(iterator.hasNext()){
    // Map.Entry<Long,AreaEntity> entry = iterator.next();
    // AreaEntity areaEntity = entry.getValue();
    // if(areaEntity.getOriginalAreaId() == originalAreaId){
    // iterator.remove();
    // return true;
    // }
    // }
    // }
    // return false;
    // }

    // public int size()
    // {
    // return cache.size();
    // }

    /**
     * 缓存中是否存在
     *
     * @param entity
     * @return
     */
    public boolean isExists(AreaEntity entity) {
        long terminalId = entity.getTerminalId();
        if (redisService.exists(Constant.AREA_CACHE_KEY, String.valueOf(terminalId))) {
            return true;
        }
        return false;
    }
}
