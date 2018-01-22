package com.navinfo.tasktracker.rprest.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/26.
 */
public interface IRedisCommonService {

    //object
    public boolean set(String key, String value);
    public boolean set(String key, byte[] value);
    //    public boolean set(String key, Object o);
    public boolean setWithExpire(final String key, final String value, final int expireSeconds);
    public <T> T get(String key);

//    public List<byte[]> mget();

    public byte[] getForBytes(String key);
    public boolean expire(String key, long seconds);
    public long incr(String key);
    public long incrBy(String key, long delta);
    public long decr(String key);
    public long decrBy(String key, long delta);
    public boolean setNX(final String key, final String value);
    public boolean del(final String key);

    public boolean hSet(String key, String hashkey, String value);
    public boolean hSet(String key, String hashkey, byte[] value);
    public byte[] hGetForBytes(String key, String hashKey);
    public String hGet(String key, String hashKey);

    /**
     * 批量查询，key对应值为空的也要不返回，返回结果集中不包含NULL值
     */
    public List<String> hMGet(final String key, final String... hashkeys);
    /**
     * 批量查询，key对应值为空的也要返回null，返回结果集个数与键个数相同
     */
    public List<String> hMGetOnKeyCount(final String key, final String... hashkeys);
    public List<byte[]> hMGetForBytes(final String key, final String... hashkeys);
    public List<byte[]> hMGet(String key, byte[]... hashkeys);
    public boolean hMSet(String key, Map<String, String> params);
    public boolean hMSetForBytes(String key, Map<String, byte[]> params);
    public <T> Map<byte[], byte[]> hGetAll(String key);
    public  List<byte[]> hGetAllForBytes(final String key);
    public  List<Object> hMGetALL(final Map<String, List<String>> keys);
    public boolean hDel(String key, String... files);
    public Set<String> hkeys(final String key);
    //SortedSet
    public Set<byte[]> zRangeByScore(final String key, final double min, final double max);
    public Set<byte[]> zRevRangeByScore(final byte[] key, final double min, final double max);
    //先写这些吧，事后再补充

    public Set<String> sMembers(String key);

    public Set<String> sMembers(String... key);
}
