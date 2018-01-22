package com.navinfo.opentsp.platform.computing.parallel.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.util.Pool;
import scala.Tuple2;

import java.io.Serializable;
import java.util.*;

/**
 * 哨兵模式连接redis集群
 *
 * @author lichao
 */
public class RedisUtil implements Serializable {

    private static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
    private Pool pool;

    private static String REDISSERVER = "redis.server";
    private static String REDISNODE = "redis.node";
    private static String REDISHOST = "redis.host";
    private static String REDISPORT ="redis.port";
    /**
     * redis db索引
     */
    private int dbIndex = 0;
    /**
     * Redis实例
     */
    private static volatile RedisUtil instance = null;
    public static RedisUtil getInstance() {
        if (instance == null) {
            throw new RuntimeException("Redis Do not initialize!!!");
        }
        return instance;
    }
    /**
     * 实例化redis对象
     *
     * @param conf 配置参数
     */
    public static void buildInstance(Map conf) throws Exception {
        if (instance == null) {
            synchronized (RedisUtil.class) {
                if (instance == null) {
                    RedisUtil local = new RedisUtil();
                    local.init(conf);
                    instance = local;
                }
            }
        }
    }
    /**
     * 初始化
     *
     * @param conf 配置参数
     */
    private void init(Map conf) {
        logger.info("开始初始化redis连接池");
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(Integer.parseInt((String) (conf.get("redis.max_idle"))));
        config.setTestOnBorrow(Boolean.valueOf((String) (conf.get("redis.isTest"))));
        config.setMaxTotal(Integer.parseInt((String) (conf.get("redis.maxTotal"))));
        String password = (String) conf.get("redis.password");
        int timeOut = 200000;
        if (conf.get(REDISSERVER) != null && conf.get(REDISNODE) != null) {
            String[] addressArr = ((String) (conf.get(REDISSERVER))).split(",");
            String node = (String) (conf.get(REDISNODE));
            Set<String> sentinels = new HashSet<String>();
            for (String str : addressArr) {
                sentinels.add(str);
            }
            if (StringUtils.isEmpty(password)) {
                pool = new JedisSentinelPool(node, sentinels, config, timeOut);
            } else {
                pool = new JedisSentinelPool(node, sentinels, config, timeOut, password);
            }
        } else if (conf.get(REDISHOST) != null && conf.get(REDISPORT) != null) {
            String host = (String) conf.get(REDISHOST);
            int port = Integer.parseInt((String) conf.get(REDISPORT));
            if (StringUtils.isEmpty(password)) {
                pool = new JedisPool(config, host, port, timeOut);
            } else {
                pool = new JedisPool(config, host, port, timeOut, password);
            }
        } else {
            logger.error("参数配置错误，如果使用哨兵连接池需要配置redis.server与redis.node");
            logger.error("如果使用普通连接池需要配置redis.host与redis.port");
        }
        logger.info("成功初始化redis连接池");
    }

    /**
     * 返回jredis对象
     *
     * @return jredis
     */
    public Jedis getRedisTemplate() {
        Jedis resource = (Jedis) pool.getResource();
        resource.select(dbIndex);
        return resource;
    }

    public Set keys(String pattern) {
        Jedis jedis = null;
        Set<String> re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.keys(pattern);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public void setValue(byte[] key, byte[] value) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.set(key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }


    public void setValue(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.set(key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    /**
     * 保存list到redis
     *
     * @param key   键
     * @param value 字符串数组
     */
    public void addToList(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.sadd(key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    /**
     * 返回redis中list
     *
     * @param key 键
     * @return list value
     */
    public Set<String> getList(String key) {
        Jedis jedis = null;
        Set<String> re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.smembers(key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }


    public void delKey(String key) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.del(key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public void delKey(String... keys) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.del(keys);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public void setExpireValue(byte[] key, byte[] value, int second) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.set(key, value);
            jedis.expire(key, second);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public void setExpireValue(String key, String value, int second) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.set(key, value);
            jedis.expire(key, second);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    /**
     * 设置redis中map的指定key的value
     *
     * @param mapName
     * @param key
     * @param value
     */
    public void setHashValue(String mapName, String key, String value) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hset(mapName, key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public void setHashValue(byte[] mapName, byte[] key, byte[] value){
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hset(mapName, key, value);
            closeJedis(jedis);
        }catch (Exception e){
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public long incrValue(String key) {
        Jedis jedis = null;
        long r = 0;
        try {
            jedis = getRedisTemplate();
            r = jedis.incrBy(key, 1);
            closeJedis(jedis);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return r;
    }

    /**
     * 将redis中map的value加1，由redis保证原子性
     *
     * @param mapName
     * @param key
     */
    public long incrHashValue(String mapName, String key) {
        Jedis jedis = null;
        long r = 0;
        try {
            jedis = getRedisTemplate();
            r = jedis.hincrBy(mapName, key, 1);
            closeJedis(jedis);
            return r;
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return r;
    }

    /**
     * 将redis中map的value加上指定值，由redis保证原子性
     *
     * @param mapName
     * @param key
     * @param value
     */
    public void incrHashValue(String mapName, String key, long value) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hincrBy(mapName, key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    /**
     * 将redis中map的value加上指定值，由redis保证原子性
     *
     * @param mapName
     * @param key
     * @param value
     */
    public void incrHashValue(String mapName, String key, double value) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hincrByFloat(mapName, key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public void delHashValue(String mapName, String key) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hdel(mapName, key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public byte[] getValue(byte[] key) {
        Jedis jedis = null;
        byte[] re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.get(key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public String getValue(String key) {
        Jedis jedis = null;
        String re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.get(key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public String getHashValue(String mapName, String key) {
        Jedis jedis = null;
        String re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.hget(mapName, key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public Map<String, String> getHashValues(String mapName) {
        Jedis jedis = null;
        Map<String, String> re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.hgetAll(mapName);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public byte[] getHashValue(byte[] mapName, byte[] key) {
        Jedis jedis = null;
        byte[] re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.hget(mapName, key);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }



    /**
     * 获取key对应的数据list
     *
     * @param key   key
     * @param start 开始下标
     * @param end   结束下标
     * @return List<byte[]>
     * @author hanzhu
     */
    public List<byte[]> getValueList(byte[] key, int start, int end) {
        Jedis jedis = null;
        List<byte[]> list = null;
        try {
            jedis = getRedisTemplate();
            list = jedis.lrange(key, start, end);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return list;
    }

    /**
     * value类型为list
     *
     * @param key   key
     * @param value value
     * @author hanzhu
     */
    public void setValueListObject(byte[] key, byte[] value) {

        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.lpush(key, value);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    /**
     * 删除value(Map)中指定的key
     * @param mapName mapName
     * @param fields key
     */
    public void delMKey(String mapName, String... fields){
        Jedis jedis = null;
        try{
            jedis = getRedisTemplate();
            jedis.hdel(mapName,fields);
            closeJedis(jedis);
        } catch (Exception e){
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public Map<String, String> getAllKeys(String mapName) {
        Jedis jedis = null;
        Map<String, String> re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.hgetAll(mapName);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public Map<byte[],byte[]> getAllKeys(byte[] mapName){
        Jedis jedis = null;
        Map<byte[], byte[]> re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.hgetAll(mapName);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public List<String> getMapValue(String key, String... fields) {
        Jedis jedis = null;
        List<String> re = null;
        try {
            jedis = getRedisTemplate();
            re = jedis.hmget(key, fields);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return re;
    }

    public void setMapValue(String key, Map<String, String> map) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hmset(key, map);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public void setMapValue(byte[] key, Map<byte[],byte[]> map){
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            jedis.hmset(key, map);
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    /**
     * 将多条查询合并成一条
     *
     * @param keys
     * @return
     */
    public Map<String, String> getValuePipeline(List<String> keys) {
        Jedis jedis = null;
        HashMap<String, String> result = new HashMap<String, String>(keys.size());
        try {
            Map<String, Response<String>> responses = new HashMap<String, Response<String>>(keys.size());
            jedis = getRedisTemplate();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                responses.put(key, p.get(key));
            }
            p.sync();
            for (String k : responses.keySet()) {
                result.put(k, responses.get(k).get());
            }
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return result;
    }

    /**
     * 将多条赋值合并成一条
     *
     * @param values
     */
    public void setValuePipeline(Map<String, String> values) {
        Jedis jedis = null;
        try {
            jedis = getRedisTemplate();
            Pipeline p = jedis.pipelined();
            for (Map.Entry<String, String> entry : values.entrySet()) {
                p.set(entry.getKey(), entry.getValue());
            }
            p.sync();
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
    }

    public List<Tuple2<String,Set<byte[]>>> getPipeline(List<String> keys) {
        Jedis jedis = null;
        List<Tuple2<String,Set<byte[]>>> result = new ArrayList<Tuple2<String,Set<byte[]>>>();
        try {
            Map<String, Response<Set<byte[]>>> responses = new HashMap<String, Response<Set<byte[]>>>(keys.size());
            jedis = getRedisTemplate();
            Pipeline p = jedis.pipelined();
            for (String key : keys) {
                responses.put(key, p.zrangeByScore(key.getBytes("utf-8"), 0D, Double.MAX_VALUE));
            }
            p.sync();
            for (String k : responses.keySet()) {
                if(responses.get(k).get()!=null&&responses.get(k).get().size()!=0) {
                    result.add(new Tuple2<>(k, responses.get(k).get()));
                }
            }
            closeJedis(jedis);
        } catch (Exception e) {
            e.printStackTrace();
            closeBreakJedis(jedis);
        }
        return result;
    }

    // ZhaiCY Add-End.

    /**
     * 正常连接池回收
     *
     * @param jedis
     */
    public void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 异常连接池回收
     *
     * @param jedis
     */
    public void closeBreakJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static void main(String[] args) {
        HashMap<String, String> param = new HashMap<>(16);
        param.put("redis.host", "10.30.10.13");
        param.put("redis.port", "19001");
        param.put("redis.max_idle", "2000");
        param.put("redis.maxTotal", "100");
        param.put("redis.timeout", "20000");
        param.put("redis.isTest", "false");
        param.put("redis.db.index", "0");

        try {
            RedisUtil.buildInstance(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

        RedisUtil instance = RedisUtil.getInstance();

        instance.setValue("qweasdzxc", "2222222222222");
        instance.keys("*");
        String qweasdzxc = instance.getValue("qweasdzxc");
        System.out.println(qweasdzxc);

    }
}
