package com.navinfo.opentsp.platform.da.core.persistence.redis.service;

import com.navinfo.opentsp.platform.da.core.common.ConcentratedData;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author: ChenJie
 * @Description:
 * @Date 2017/10/20
 * @Modified by:
 */
public interface ConcentratedDataRedis {
    /**
     * 向有序集合中添加多个元素
     *
     * @param queue 从kafka消费的密集采集数据
     */
    void zaddPipeline(ConcurrentLinkedQueue<ConcentratedData> queue);

    /**
     * 获取有序集合的成员数量
     *
     * @param keys
     * @return
     */
    Map<String, Response<Long>> zcardPipeline(Set<String> keys);

    /**
     * 获取指定范围内的成员
     *
     * @param keys
     * @param startIndex 第一个元素为0
     * @param endIndex   最后一个元素是-1，倒数第二个是-2
     * @return Set<Tuple>
     */
    Map<String, Response<Set<Tuple>>> zrangeWithScoresPipeline(List<String> keys, long startIndex, long endIndex);

    /**
     * 获取指定key的最小分数
     *
     * @param keys
     * @return Map
     */
    Map<String, Double> zMinScore(List<String> keys);

    /**
     * 获取指定key的最大分数
     * @param keys
     * @return Map
     */
    Map<String, Double> zMaxScore(List<String> keys);

    /**
     * 删除指定分数的数据
     * @param key
     * @param start  开始分数
     * @param end   接收分数
     */
    void zremrangeByScore(String key, Double start, Double end);

    /**
     * 获取set的元素
     * @param setName
     * @return Set
     * */
    Set<String> get(String setName);
}
