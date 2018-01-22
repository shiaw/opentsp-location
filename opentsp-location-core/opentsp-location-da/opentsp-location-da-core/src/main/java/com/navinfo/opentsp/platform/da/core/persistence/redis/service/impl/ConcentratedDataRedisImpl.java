package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.common.ConcentratedData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisClusters;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ConcentratedDataRedis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.Tuple;
import redis.clients.jedis.exceptions.JedisException;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author: ChenJie
 * @Description:
 * @Date 2017/10/20
 * @Modified by:
 */
public class ConcentratedDataRedisImpl implements ConcentratedDataRedis {
    private static Logger logger = LoggerFactory.getLogger(ConcentratedDataRedisImpl.class);

    /**
     * 把终端缓存的密集采集数据 采用管道保存到redis
     * @param queue 缓存数据
     * */
    @Override
    public void zaddPipeline(ConcurrentLinkedQueue<ConcentratedData> queue) {
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            //开启管道
            jedis = RedisClusters.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            Set<String> set = new HashSet();
            //遍历缓存的密集采集数据
            while(!queue.isEmpty()){
                try{
                    ConcentratedData concentratedData = queue.poll();
                    pipeline.zadd("concentrated_" + concentratedData.getTerminalId(),concentratedData.getScore(),concentratedData.getData());
                    set.add("concentrated_" + concentratedData.getTerminalId());
                }
                catch(Exception e){
                    logger.error("concentrated data parse error,can not save to redis:{}",e);
                }
            }
            //把所有的终端id保存到set：terminal_id
            String [] terminals = new String [set.size()];
            pipeline.sadd("terminal_id",set.toArray(terminals));
            //提交
            pipeline.sync();

        } catch (JedisException e) {
            isBroken = true;
            logger.error("redis zaddPipeline exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }

    }

    /**
     * 获取有序集合的成员数量
     * @param keys
     * @return
     * */
    @Override
    public Map<String, Response<Long>> zcardPipeline(Set<String> keys) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map<String, Response<Long>> map = new HashMap(keys.size());

        try {
            //开启管道
            jedis = RedisClusters.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();

            for(String key:keys){
                Response<Long> response = pipeline.zcard(key);
                map.put(key,response);
            }

            //提交
            pipeline.sync();

        } catch (JedisException e) {
            isBroken = true;
            logger.error("redis zcardPipeline exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
        return map;
    }

    /**
     * 获取指定范围内的成员
     * @param keys
     * @param startIndex 第一个元素为0
     * @param endIndex 最后一个元素是-1，倒数第二个是-2
     * @return Set<Tuple>
     * */
    @Override
    public Map<String, Response<Set<Tuple>>> zrangeWithScoresPipeline(List<String> keys, long startIndex, long endIndex) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map<String, Response<Set<Tuple>>> map = new HashMap(keys.size());

        try {
            //开启管道
            jedis = RedisClusters.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();
            for(String key:keys){
                Response<Set<Tuple>> response = pipeline.zrangeWithScores(key,startIndex,endIndex);
                map.put(key,response);
            }
            //提交
            pipeline.sync();

        } catch (JedisException e) {
            isBroken = true;
            logger.error("redis zrangePipeline exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
        return map;
    }

    /**
     * 获取指定key的最小分数
     * @param keys
     *
     * */
    @Override
    public Map<String, Double> zMinScore(List<String> keys) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map<String, Double> scores = new HashMap(keys.size());
        try {
            //开启管道
            jedis = RedisClusters.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();

            Map<String, Response<Set<Tuple>>> map = new HashMap(keys.size());
            for(String key:keys){
                Response<Set<Tuple>> response = pipeline.zrangeWithScores(key,0,0);
                map.put(key,response);
            }
            //提交
            pipeline.sync();

            //取出分数
            for(String key:keys){
                Response<Set<Tuple>> setResponse = map.get(key);
                Set<Tuple> tuples = setResponse.get();
                for(Tuple tuple : tuples){
                    Double score = tuple.getScore();
                    scores.put(key,score);
                }
            }
        } catch (JedisException e) {
            isBroken = true;
            logger.error("redis zMinScore exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
        return scores;
    }

    /**
     * 获取指定key的最大分数
     * @param keys
     * */
    @Override
    public Map<String, Double> zMaxScore(List<String> keys) {
        Jedis jedis = null;
        boolean isBroken = false;
        Map<String, Double> scores = new HashMap();

        try {
            //开启管道
            jedis = RedisClusters.getInstance().getJedis();
            Pipeline pipeline = jedis.pipelined();

            Map<String, Response<Set<Tuple>>> map = new HashMap();
            for(String key:keys){
                Response<Set<Tuple>> response = pipeline.zrangeWithScores(key,-1,-1);
                map.put(key,response);
            }
            //提交
            pipeline.sync();

            //取出分数
            for(String key:keys){
                Response<Set<Tuple>> setResponse = map.get(key);
                Set<Tuple> tuples = setResponse.get();
                for(Tuple tuple : tuples){
                    Double score = tuple.getScore();
                    scores.put(key,score);
                }
            }
        } catch (JedisException e) {
            isBroken = true;
            logger.error("redis zMaxScore exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
        return scores;
    }

    @Override
    public void zremrangeByScore(String key,Double start,Double end){
        Jedis jedis = null;
        boolean isBroken = false;
        try {
            //开启管道
            jedis = RedisClusters.getInstance().getJedis();

            jedis.zremrangeByScore(key,start,end);


        } catch (JedisException e) {
            isBroken = true;
            logger.error("redis zremByScore exception:", e);
        } finally {
            RedisClusters.getInstance().release(jedis, isBroken);
        }
    }

    @Override
    public Set<String> get(String setName) {
        Set<String> result = new HashSet<String>();
        Jedis jedis = null;
        boolean isBroken = false;
        Set<String> set = null;
        try {
            jedis = RedisClusters.getInstance().getJedis();
            set = jedis.smembers(setName);
        } catch (JedisException e) {
            isBroken = true;
            logger.error(e.getMessage(),e);
        }finally{
            RedisClusters.getInstance().release(jedis,isBroken);
        }
        if(set != null){
            for (String tid : set) {
                result.add(tid);
            }
        }
        return result;
    }
}
