package com.navinfo.opentsp.platform.dp.core.cache;

import com.navinfo.opentsp.platform.dp.core.cache.entity.AreaEntity;
import com.navinfo.opentsp.platform.dp.core.common.Constant;
import com.navinfo.opentsp.platform.dp.core.redis.IRedisService;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 区域数据缓存<br>
 * Map<终端唯一标识,Map<区域ID,区域信息>> <br>
 * 参数：{@link AreaType} 预留
 *
 * @author lgw
 */
@Component("areaCache")
public class AreaCache {

    @Resource
    private IRedisService redisService;

    //
    public static Logger log = LoggerFactory.getLogger(AreaCache.class);

    private final static int MAX_AREA_SIZE = 16;

    private AreaCache() {
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
        // Map<Long , AreaEntity> map = cache.get(areaEntity.getTerminalId());
        Map<Long, AreaEntity> map = this.getAreaEntity(areaEntity.getTerminalId());
        AreaEntity result = null;
        if (map == null) {
            map = new ConcurrentHashMap<Long, AreaEntity>();
        }
        // 是否超过终端允许设置的最大区域数
        // 如果大于则删除最老的一个区域
        // 关将删除的区域ID返回
        // 如果区域是修改,则不需要判断区域个数限制
        if (map.get(areaEntity.getOriginalAreaId()) != null) {
            int areaCount = 0;
            for (Entry<Long, AreaEntity> e : map.entrySet()) {
                if (e.getValue().getAreaType() == areaEntity.getAreaType())
                    areaCount++;
            }

            // 当发现同类型区域信息对象大于给定法制，则删除最旧的（按照创建时间比较）
            if (areaCount >= MAX_AREA_SIZE) {

                Iterator<Entry<Long, AreaEntity>> iterator = map.entrySet().iterator();
                while (iterator.hasNext()) {
                    Entry<Long, AreaEntity> entry = iterator.next();
                    if (entry.getValue().getAreaType() == areaEntity.getAreaType()) {
                        AreaEntity currentEntity = entry.getValue();
                        if (result == null) {
                            result = currentEntity;
                        } else {
                            if (result.getCreateDate() > currentEntity.getCreateDate()) {
                                result = currentEntity;
                            }
                        }
                    }
                }
                map.remove(result.getOriginalAreaId());
                log.error("终端[ " + areaEntity.getTerminalId() + " ]查找到已经缓存的同类型区域数量 > " + MAX_AREA_SIZE
                        + " 上线.删除最早存储记录.[ 区域编号: " + result.getOriginalAreaId() + "]");
            }
        }

        map.put(areaEntity.getOriginalAreaId(), areaEntity);

        redisService.hset(Constant.AREA_CACHE_KEY, String.valueOf(areaEntity.getTerminalId()), map);

        return result;
    }

    public Map<Long, AreaEntity> getAreaEntity(long terminal) {
        // return cache.get(terminal);
        return redisService.getHashValue(Constant.AREA_CACHE_KEY, String.valueOf(terminal), Map.class);
    }

    public Map<Long, AreaEntity> getAreaEntity(long terminalId, AreaType areaType) {
        // Map<Long, AreaEntity> map = cache.get(terminalId);
        Map<Long, AreaEntity> map = getAreaEntity(terminalId);
        Iterator<Entry<Long, AreaEntity>> iterator = map.entrySet().iterator();
        Map<Long, AreaEntity> ret = new ConcurrentHashMap<Long, AreaEntity>();
        while (iterator.hasNext()) {
            Entry<Long, AreaEntity> e = iterator.next();
            if (e.getValue().getAreaId() == areaType.getNumber()) {
                ret.put(e.getKey(), e.getValue());
            }
        }
        return ret;
    }

    public AreaEntity getAreaEntity(long terminalId, long areaId) {
        // Map<Long, AreaEntity> map = cache.get(terminalId);
        Map<Long, AreaEntity> map = this.getAreaEntity(terminalId);
        if (map != null) {
            return map.get(areaId);
        }
        return null;
    }

    /**********************
     * 删除区域实体
     * <p>
     * 根据终端编号，区域类型删除
     *
     * @param terminalId
     * @param areaType
     * @return
     */
    public boolean removeAreaEntity(long terminalId, AreaType areaType) {
        // Map<Long, AreaEntity> map = cache.get(terminalId);
        Map<Long, AreaEntity> map = this.getAreaEntity(terminalId);
        if (map != null) {
            Iterator<Entry<Long, AreaEntity>> iterator = map.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<Long, AreaEntity> entry = iterator.next();
                if (entry.getValue().getAreaType() == areaType.getNumber()) {
                    iterator.remove();
                }
            }
            // cache.put(terminalId, map);
            redisService.hset(Constant.AREA_CACHE_KEY, String.valueOf(terminalId), map);
            return true;
        }
        return false;
    }

    /***********************
     * 删除区域实体
     * <p>
     * 根据终端编号 删除
     *
     * @param terminalId
     * @return
     */
    public boolean removeAreaEntity(long terminalId) {
        // Map<Long, AreaEntity> map = cache.get(terminalId);
        Map<Long, AreaEntity> map = this.getAreaEntity(terminalId);
        if (map != null) {
            // map.clear();
            redisService.delete(Constant.AREA_CACHE_KEY, String.valueOf(terminalId));
            return true;
        }
        return false;
    }

    /***************************
     * 删除区域实体 根据终端编号，区域编号 删除
     *
     * @param terminalId
     * @param originalAreaId
     * @param areaType       {@link AreaType} 预留
     * @return
     */
    public boolean removeAreaEntity(long terminalId, long originalAreaId, AreaType areaType) {
        // Map<Long, AreaEntity> map = cache.get(terminalId);
        Map<Long, AreaEntity> map = this.getAreaEntity(terminalId);
        if (map != null) {
            Iterator<Long> iterator = map.keySet().iterator();
            while (iterator.hasNext()) {
                Long id = iterator.next();
                AreaEntity areaEntity = map.get(id);
                if (areaEntity.getOriginalAreaId() == originalAreaId) {
                    redisService.hset(Constant.AREA_CACHE_KEY, String.valueOf(terminalId), map);
                    return true;
                }
            }
        }
        return false;
    }

    // public int size(){
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
