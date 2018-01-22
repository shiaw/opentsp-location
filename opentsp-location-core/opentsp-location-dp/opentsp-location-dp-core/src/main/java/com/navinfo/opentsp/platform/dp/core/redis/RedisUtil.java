package com.navinfo.opentsp.platform.dp.core.redis;

/**
 * 支持可配置集群分片(Shard)部署,或者哨兵方式(Sentinel)部署
 * User: zhanhk
 * Date: 16/8/9
 * Time: 下午4:00
 */
public class RedisUtil {

    private static String redisType = "sharde";

    private static RedisDao redisDao = null;

    static {
        if(Configuration.getString("redis.cluster.type") != null) {
            redisType = Configuration.getString("redis.cluster.type");
        }
    }

    private RedisUtil(){}

    public static RedisDao getRedis(){
        if(redisDao == null) {
            if(redisType.equals("sharde")) {
                redisDao = new ShardRedisDBUtil();
            } else {
                redisDao = new SentinelRedisDBUtil();
            }
        }
        return redisDao;
    }
}
