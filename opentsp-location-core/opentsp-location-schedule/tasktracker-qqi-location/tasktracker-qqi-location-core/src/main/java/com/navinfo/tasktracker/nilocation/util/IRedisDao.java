package com.navinfo.tasktracker.nilocation.util;

import org.springframework.data.redis.connection.RedisZSetCommands;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author zhangyue
 */
public interface IRedisDao {

    /**
     *s et
     * @param key
     * @param value
     * @return boolean
     */
    public Boolean set(String key, String value);
    /**
     * set
     * @param key
     * @param value
     * @return boolean
     */
    public Boolean set(String key, byte[] value);
    /**
     *setWithExpire
     * @param key
     * @param value
     * @param expireSeconds
     * @return boolean
     */
    public Boolean setWithExpire(final String key, final String value, final int expireSeconds);
    /**
     * get
     * @param key
     * @return T
     */
    public <T> T get(String key);

    /**
     * getForBytes
     * @param key
     * @return byte[]
     */
    public byte[] getForBytes(String key);
    /**
     * expire
     * @param key
     * @param seconds
     * @return boolean
     */
    public boolean expire(String key, long seconds);
    /**
     * incr
     * @param key
     * @return long
     */
    public long incr(String key);
    /**
     * incrBy
     * @param key
     * @param delta
     * @return long
     */
    public long incrBy(String key, long delta);
    /**
     * decr
     * @param key
     * @return long
     */
    public long decr(String key);
    /**
     * decrBy
     * @param key
     * @param delta
     * @return long
     */
    public long decrBy(String key, long delta);
    /**
     * del
     * @param key
     * @return boolean
     */
    public boolean del(final String ... key);
    /**
     * hSet
     * @param key
     * @param hashkey
     * @param value
     * @return boolean
     */
    public boolean hSet(String key, String hashkey, String value);
    /**
     * hSet
     * @param key
     * @param hashkey
     * @param value
     * @return boolean
     */
    public boolean hSet(String key, String hashkey, byte[] value);
    /**
     * hGetForBytes
     * @param key
     * @param hashKey
     * @return byte
     */
    public byte[] hGetForBytes(String key, String hashKey);
    /**
     * hGetForBytes
     * @param listKey
     * @param min
     * @param max
     * @return Boolean
     */
    public Boolean removeZSetList(final List<String> listKey,final long min,final long max);

    /**
     * hGet
     * @param key
     * @param hashKey
     * @return String
     */
    public String hGet(String key, String hashKey);

    /**
     *批量查询，key对应值为空的也要不返回，返回结果集中不包含NULL值
     * hMGet
     * @param key
     * @param  hashkeys
     * @return List
     */
    public List<String> hMGet(final String key, final String... hashkeys);
    /**
     *批量查询，key对应值为空的也要返回null，返回结果集个数与键个数相同
     * hMGetOnKeyCount
     * @param key
     * @param hashkeys
     * @return List
     */
    public List<String> hMGetOnKeyCount(final String key, final String... hashkeys);
    /**
     * hMGetForBytes
     * @param key
     * @param hashkeys
     * @return List
     */
    public List<byte[]> hMGetForBytes(final String key, final String... hashkeys);
    /**
     * hMGet
     * @param key
     * @param hashkeys
     * @return List
     */
    public List<byte[]> hMGet(String key, byte[]... hashkeys);
    /**
     * hMSetForBytes
     * @param key
     * @param params
     * @return boolean
     */
    public Boolean hMSetForBytes(String key, Map<String, byte[]> params);
    /**
     * hGetAll
     * @param key
     * @return T
     */
    public <T> Map<byte[], byte[]> hGetAll(String key);
    /**
     * hGetAllForBytes
     * @param key
     * @return List
     */
    public  List<byte[]> hGetAllForBytes(final String key);
    /**
     * hMGetALL
     * @param keys
     * @return List
     */
    public  List<Object> hMGetALL(final Map<String, List<String>> keys);

    /**
     * zRangeByScore
     * @param key
     * @param min
     * @param max
     * @return Set
     */
    public Set<byte[]> zRangeByScore(final String key, final double min, final double max);
    /**
     * zRevRangeByScore
     * @param key
     * @param min
     * @param max
     * @return Set
     */
    public Set<byte[]> zRevRangeByScore(final byte[] key, final double min, final double max);
    /**
     * sMembers
     * @param key
     * @return Set
     */
    public Set<String> sMembers(String key);
    /**
     *按时间取zset
     * @param key
     * @param min
     * @param max
     * @return Set
     */
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(final String key,final double min,final double max);
    /**
     *按大小删除zset
     * removeRangeByScore
     * @param key
     * @param min
     * @param max
     * @return Boolean
     */
    public Boolean removeRangeByScore(final String key,final double min,final double max);
    /**
     * zRangeByScoreALL
     * @param keys
     * @param start
     * @param end
     * @return Map
     */
    public Map<String, Set<RedisZSetCommands.Tuple>> zRevRangeByScoreALL(final List<String> keys,final long start,final long end);
    /**
     * sMembers
     * @param key
     * @return Set
     */
    public Set<String> sMembers(String... key);
    /**
     * zSetCarALL
     * @param keys
     * @return Map
     */
    public Map<String, Long> zSetCarALL(final List<String> keys);
    /**
     * zRangeByScoreWithScoresAll
     * @param keys
     * @param start
     * @param end
     * @return Set
     */
    public Map<String, Set<RedisZSetCommands.Tuple>> zRangeByScoreWithScoresAll(final List<String> keys,final long start,final long end);
    /**
     * removeRangeByScoreAll
     * @param keys
     * @param listmin
     * @param max
     * @return Set
     */
    public Boolean removeRangeByScoreAll(final List<String> keys,final List listmin,final List max);
}
