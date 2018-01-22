package com.navinfo.tasktracker.nilocation.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisZSetCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * zhangyue
 * @author
 */
@Component
public class RedisDaoTem implements IRedisDao {
    protected static final Logger logger = LoggerFactory.getLogger(RedisDaoTem.class);
    @Resource
    private RedisTemplate<Serializable, Serializable> redisTemplate;

    private byte[] getBytes(String key){
        if(key == null){
            return null;
        }
        byte[] ret = null;
        try {
            ret = key.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return ret;
    }

    @Override
    public boolean del(final String ... keys) {
        if(keys == null){
            return false;
        }
        Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException{
                byte[][] keyBytes = new byte[keys.length][];
                int index = 0;
                for(String k : keys ){
                    keyBytes[index] = getBytes(k);
                    index++;
                }
                return redisConnection.del(keyBytes) > 0;
            }
        });
        return ret;
    }

    @Override
    public Boolean set(final String key, final String value) {
        Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(getBytes(key), getBytes(value));
                return Boolean.TRUE;
            }
        });
        return ret;
    }

    @Override
    public Boolean set(final String key, final  byte[] value) {
        Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.set(getBytes(key), value);
                return Boolean.TRUE;
            }
        });
        return ret;
    }

    @Override
    public Boolean setWithExpire(final String key, final String value, final int expireSeconds) {
       Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                redisConnection.set(getBytes(key), getBytes(value));
                redisConnection.expire(getBytes(key), expireSeconds);
                redisConnection.closePipeline();
                return Boolean.TRUE;
            }
        });
        return ret;
    }

    private String getString(byte[] data){
        if(data == null){
            return null;
        }
        String ret = null;
        try {
            ret = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public String get(final String key) {
        String ret = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] value = redisConnection.get(getBytes(key));
                return getString(value);
            }
        });
        return ret;
    }
    /**
     * 时间有限，暂时简单封装，解析交给调用者，后期再优化
     * @param key
     * @return
     */
    @Override
    public byte[] getForBytes(final String key) {
        byte[] ret = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.get(getBytes(key));
            }
        });
        return ret;
    }


    @Override
    public boolean expire(String key, final long timeout) {
        return redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    @Override
    public long incr(final String key) {
        return incrBy(key, 1);
    }

    @Override
    public long incrBy(final String key, final long delta) {
        return redisTemplate.execute(new RedisCallback<Long>() {
            @Override
            public Long doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.incrBy(getBytes(key), delta);
            }
        });
    }

    @Override
    public long decr(String key) {
        return incrBy(key, -1L);
    }

    @Override
    public long decrBy(String key, final long delta) {
        return incrBy(key, delta > 0 ? -delta : delta);
    }

    @Override
    public boolean hSet(final String key, final String hashkey, final String value) {
        redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hSet(getBytes(key), getBytes(hashkey), getBytes(value));
            }
        });
        return false;
    }

    @Override
    public boolean hSet(final String key, final String hashkey, final byte[] value)
    {
        return redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hSet(getBytes(key), getBytes(hashkey), value);
            }
        });
    }

    @Override
    public byte[] hGetForBytes(final String key, final String hashKey) {
        byte[] ret = redisTemplate.execute(new RedisCallback<byte[]>() {
            @Override
            public byte[] doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.hGet(getBytes(key), getBytes(hashKey));
            }
        });
        return ret;
    }

    @Override
    public String hGet(final String key, final String hashKey) {
        String ret = redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] v = redisConnection.hGet(getBytes(key), getBytes(hashKey));
                return getString(v);
            }
        });
        return ret;
    }


    @Override
    public Boolean hMSetForBytes(final String key, final Map<String,byte[]> params) {
        Boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Map<byte[], byte[]> p = new HashMap<byte[], byte[]>(params.size());
                for(Map.Entry<String, byte[]> entry : params.entrySet()){
                    p.put(getBytes(entry.getKey()), entry.getValue());
                }
                redisConnection.hMSet(getBytes(key), p);
                return true;
            }
        });
        return ret;
    }

    @Override
    public List<String> hMGet(final String key, final String ... hashkeys ){
        List<String> ret = redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[][] bytes = new byte[hashkeys.length][];
                for(int i=0;i<hashkeys.length;i++){
                    bytes[i] = getBytes(hashkeys[i]);
                }
                List<byte[]> values = redisConnection.hMGet(getBytes(key), bytes);
                List<String> retList = new ArrayList<String>();
                if(values != null){
                    for(byte[] v : values){
                        if(v != null){
                            retList.add(getString(v));
                        }
                    }
                }
                return retList;
            }
        });
        return ret;
    }

    @Override
    public List<String> hMGetOnKeyCount(final String key, final String ... hashkeys ){
        List<String> ret = redisTemplate.execute(new RedisCallback<List<String>>() {
            @Override
            public List<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[][] bytes = new byte[hashkeys.length][];
                for(int i=0;i<hashkeys.length;i++){
                    bytes[i] = getBytes(hashkeys[i]);
                }
                List<byte[]> values = redisConnection.hMGet(getBytes(key), bytes);
                List<String> retList = new ArrayList<String>();
                if(values != null){
                    for(byte[] v : values){
                        retList.add(getString(v));
                    }
                }
                return retList;
            }
        });
        return ret;
    }

    @Override
    public List<byte[]> hMGetForBytes(final String key, final String ... hashkeys ){
        List<byte[]> ret = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[][] bytes = new byte[hashkeys.length][];
                for(int i=0;i<hashkeys.length;i++){
                    bytes[i] = getBytes(hashkeys[i]);
                }
                List<byte[]> values = redisConnection.hMGet(getBytes(key), bytes);
                List<byte[]> retList = new ArrayList<byte[]>();
                if(values != null){
                    for(byte[] v : values){
                            retList.add(v);
                    }
                }
                return retList;
            }
        });
        return ret;
    }

    @Override
    public List<byte[]> hMGet(final String key, final byte[] ... value ){
        List<byte[]> ret = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                List<byte[]> values = redisConnection.hMGet(getBytes(key), value);
                List<byte[]> retList = new ArrayList<byte[]>();
                if(values != null){
                    for(byte[] v : values){
                        if(v != null){
                            retList.add(v);
                        }
                    }
                }
                return retList;
            }
        });
        return ret;
    }

    @Override
    public Map<byte[], byte[]> hGetAll(final String key) {
        Map<byte[], byte[]> ret = redisTemplate.execute(new RedisCallback<Map<byte[], byte[]>>() {
            @Override
            public Map<byte[], byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                Map<byte[], byte[]> dataMap = redisConnection.hGetAll(getBytes(key));
                return dataMap;
            }
        });
        return ret;
    }

    @Override
    public  List<byte[]> hGetAllForBytes(final String key) {
        List<byte[]> ret = redisTemplate.execute(new RedisCallback<List<byte[]>>() {
            @Override
            public List<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                List<byte[]> l = new ArrayList<byte[]>();
                Map<byte[], byte[]> dataMap = redisConnection.hGetAll(getBytes(key));
                for(Map.Entry<byte[], byte[]> entry : dataMap.entrySet()){
                    if(entry.getValue() != null){
                        l.add(entry.getValue());
                    }
                }
                return l;
            }
        });
        return ret;
    }

    @Override
    public List<Object> hMGetALL(final Map<String, List<String>> keys){
        List<Object> ret = redisTemplate.execute(new RedisCallback<List<Object>>() {
            @Override
            public List<Object> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for(Map.Entry<String, List<String>> entry : keys.entrySet()) {
                    byte[] keyBytes = getBytes(entry.getKey());
                    List<String> hashkeyList = entry.getValue();
                    int index = 0;
                    byte[][] hashkeyBytes = new byte[hashkeyList.size()][];
                    for(String hashKey : hashkeyList){
                        hashkeyBytes[index] = getBytes(hashKey);
                        index++;
                    }
                    redisConnection.hMGet(keyBytes, hashkeyBytes);
                }
                List<Object> list = redisConnection.closePipeline();
                return list;
            }
        });
        return ret;
    }
    public Boolean hMSetALL(final Map<String, Map<String,Object>> keys){
        boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for(Map.Entry<String, Map<String,Object>> entry : keys.entrySet()) {
                    byte[] keyBytes = getBytes(entry.getKey());
                    Map<String,Object> hashkeyMap = entry.getValue();
                    int index = 0;
                    byte[] hashkeyBytes = new byte[hashkeyMap.size()];
                    byte[] hashValueBytes = new byte[hashkeyMap.size()];
                    Map<byte[],byte[]> resultMap = new HashMap<byte[], byte[]>(hashkeyMap.size());
                    for(Map.Entry<String,Object> entryMap : hashkeyMap.entrySet()){
                        hashkeyBytes = getBytes(entryMap.getKey().toString());
                        hashValueBytes = (byte[])entryMap.getValue();
                        resultMap.put(hashkeyBytes,hashValueBytes);
                    }
                    redisConnection.hMSet(keyBytes, resultMap);
                }
                redisConnection.closePipeline();
                return true;
            }
        });
        return ret;
    }

    @Override
    public Set<RedisZSetCommands.Tuple> zRangeByScoreWithScores(final String key,final double min,final double max) {
        Set<RedisZSetCommands.Tuple> ret = redisTemplate.execute(new RedisCallback<Set<RedisZSetCommands.Tuple>>() {
            @Override
            public Set<RedisZSetCommands.Tuple> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] keyBytes = getBytes(key);
                return redisConnection.zRangeByScoreWithScores(keyBytes, min, max);
            }
        });
        return ret;
    }

    @Override
    public Boolean removeRangeByScore(final String key,final double min,final double max) {
        boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                byte[] keyBytes = getBytes(key);
                redisConnection.zRemRangeByScore(keyBytes, min, max);
                return true;
            }
        });
        return true;
    }

    @Override
    public Boolean removeRangeByScoreAll(final List<String> keys,final List listmin,final List max) {
        boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                int j = 0;
                if(keys.size() ==  listmin.size() && keys.size() == max.size()){

                    for(String key : keys) {
                        byte[] keyBytes = getBytes(key);
                        redisConnection.zRemRangeByScore(keyBytes, (long) listmin.get(j), (long) max.get(j));
                        j++;
                    }
                    List<Object> ret = redisConnection.closePipeline();
                    return true;
                    }else{
                        return false;
                    }
            }
        });
        return true;
    }
    @Override
    public Boolean removeZSetList(final List<String> listKey,final long min,final long max) {
        boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for(String key : listKey) {
                    byte[] keyBytes = getBytes(key);
                    redisConnection.zRemRange(keyBytes, min, max);
                }
                List<Object> ret = redisConnection.closePipeline();
                return true;
            }
        });
        return true;
    }

    public Boolean addZSetList(final String key, final double score, final String value) {
        boolean ret = redisTemplate.execute(new RedisCallback<Boolean>() {
            @Override
            public Boolean doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                byte[] keyBytes = getBytes(key);
                byte[] valueBytes = getBytes(value);

                redisConnection.zAdd(keyBytes, score, valueBytes);
                List<Object> ret = redisConnection.closePipeline();
                return true;
            }
        });
        return true;
    }
    @Override
    public Map<String, Set<RedisZSetCommands.Tuple>> zRevRangeByScoreALL(final List<String> keys,final long start,final long end){
        Map<String, Set<RedisZSetCommands.Tuple>> ret = redisTemplate.execute(new RedisCallback<Map<String, Set<RedisZSetCommands.Tuple>>>() {
            @Override
            public Map<String, Set<RedisZSetCommands.Tuple>> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for(String key : keys) {
                    byte[] keyBytes = getBytes(key);
                    redisConnection.zRevRangeWithScores(keyBytes, start, end);
                }
                List<Object> ret = redisConnection.closePipeline();
                Map<String, Set<RedisZSetCommands.Tuple>> keyMaps = new HashMap(ret.size());
                int index = 0;
                for (Object retKey : ret){
                    Set<RedisZSetCommands.Tuple> byteTet = (Set<RedisZSetCommands.Tuple>)retKey;
                    if(byteTet.size() > 0) {
                        keyMaps.put(keys.get(index), byteTet);
                    }
                    index++;
                }
                return keyMaps;
            }
        });
        return ret;
    }

    @Override
    public Map<String, Set<RedisZSetCommands.Tuple>> zRangeByScoreWithScoresAll(final List<String> keys,final long start,final long end){
        Map<String, Set<RedisZSetCommands.Tuple>> ret = redisTemplate.execute(new RedisCallback<Map<String, Set<RedisZSetCommands.Tuple>>>() {
            @Override
            public Map<String, Set<RedisZSetCommands.Tuple>> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for(String key : keys) {
                    byte[] keyBytes = getBytes(key);
                    redisConnection.zRangeWithScores(keyBytes, start, end);
                }
                List<Object> ret = redisConnection.closePipeline();
                Map<String, Set<RedisZSetCommands.Tuple>> keyMaps = new HashMap(ret.size());
                int index = 0;
                for (Object retKey : ret){
                    Set<RedisZSetCommands.Tuple> byteTet = (Set<RedisZSetCommands.Tuple>)retKey;
                    if(byteTet.size() > 0) {
                        keyMaps.put(keys.get(index), byteTet);
                    }
                    index++;
                }
                return keyMaps;
            }
        });
        return ret;
    }

    @Override
    public Map<String, Long> zSetCarALL(final List<String> keys){
        Map<String, Long> ret = redisTemplate.execute(new RedisCallback<Map<String, Long>>() {
            @Override
            public Map<String, Long> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for (String key : keys) {
                    byte[] keyBytes = getBytes(key);
                    redisConnection.zCard(keyBytes);
                }
                List<Object> ret = redisConnection.closePipeline();
                Map<String, Long> res = new HashMap(ret.size());
                if (ret != null && ret.size() > 0){
                    int index = 0;
                    for (Object retKey : ret) {
                        Long byteTet = (Long) retKey;
                        res.put(keys.get(index),byteTet);
                        index++;
                    }
                 }else{
                    return null;
                }
                return res;
            }
        });
        return ret;
    }
    @Override
    public Set<byte[]> zRangeByScore(final String key, final double min, final double max){
       return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return redisConnection.zRangeByScore(getBytes(key), min, max);
            }
        });
    }

    @Override
    public Set<byte[]> zRevRangeByScore(final byte[] key, final double min, final double max){
        return redisTemplate.execute(new RedisCallback<Set<byte[]>>() {
            @Override
            public Set<byte[]> doInRedis(RedisConnection redisConnection) throws DataAccessException {
               return redisConnection.zRevRangeByScore(key, min, max);
            }
        });
    }


    @Override
    public Set<String> sMembers(final String key) {
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                try {
                    Set<byte[]> bytes = redisConnection.sMembers(getBytes(key));
                    Set<String> retSet = new HashSet<>();
                    for (byte[] byteS : bytes) {
                        retSet.add(getString(byteS));
                    }
                    return retSet;
                }catch (Exception e){
                    logger.error("redis:" + e.getMessage());
                    return null;
                }

            }
        });
    }

    @Override
    public Set<String> sMembers(final String... keys) {
        return redisTemplate.execute(new RedisCallback<Set<String>>() {
            @Override
            public Set<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.openPipeline();
                for(String key : keys){
                    redisConnection.sMembers(getBytes(key));
                }
                List<Object> objects = redisConnection.closePipeline();
                Set<String> retSet = new HashSet<>();
                for(Object obj : objects){
                    Set<byte[]> bytes = (Set<byte[]>)obj;
                    for(byte[] byteS : bytes){
                        retSet.add(getString(byteS));
                    }
                }
                return retSet;
            }
        });
    }
}
