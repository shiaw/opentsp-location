package com.navinfo.opentsp.platform.dp.core.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 满足集群分片Shard
 * @author zhanhk
 */
public class ShardRedisDBUtil extends RedisSource{

	private static Logger logger = LoggerFactory.getLogger(ShardRedisDBUtil.class);
	private static ShardedJedisPool pool;
	static {
		logger.info("开始初始化redis连接池");
		JedisPoolConfig config = new JedisPoolConfig();
		config.setMaxTotal(Integer.valueOf(max_active));
		config.setMaxIdle(Integer.valueOf(max_idle));
		config.setMaxWaitMillis(Integer.valueOf(max_wait));
		config.setTestOnBorrow(Boolean.valueOf(isTest));
		List<JedisShardInfo> list = new ArrayList<JedisShardInfo>();
		String[] addressArr = redis_address.split(",");
		for (String str : addressArr) {
			JedisShardInfo shardInfo = new JedisShardInfo(str.split(":")[0], Integer.parseInt(str.split(":")[1]),str.split(":")[2]);
			//shardInfo.setTimeout(Integer.valueOf(timeout));
			list.add(shardInfo);
		}
		pool = new ShardedJedisPool(config, list);
		logger.info("成功初始化redis连接池");
	}

	public static ShardedJedis getRedisTemplate() {
		ShardedJedis shardedJedis = pool.getResource();
		return shardedJedis;
	}

	public void setValue(String key, String value) {
		ShardedJedis jedis = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void setExpireValue(byte[] key, byte[] value, int second){
		ShardedJedis jedis = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			jedis.expire(key, second);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void setHashValue(String mapName, String key, String value){
		ShardedJedis jedis = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			jedis.hset(mapName, key, value);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public void setHashValue(byte[] mapName, byte[] key, byte[] value){
		ShardedJedis jedis = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			jedis.hset(mapName, key, value);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void zadd(String key, Map<String, Double> scoreMembers){
		ShardedJedis jedis = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			jedis.zadd(key, scoreMembers);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public Set<String> zrangeByscore(String key, String value){
		ShardedJedis jedis = null;
		Set<String> result = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			result = jedis.zrangeByScore(key, value, "+inf",0,1);
			ShardRedisDBUtil.closeJedis(jedis);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return result;
	}
	
	public byte[] getValue(byte[] key) {
		ShardedJedis jedis = null;
		byte[] re = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.get(key);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public String getValue(String key) {
		ShardedJedis jedis = null;
		String re = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.get(key);	
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public String getHashValue(String mapName, String key){
		ShardedJedis jedis = null;
		String re = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.hget(mapName, key);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public Long delHashValue(String mapName, String key){
		ShardedJedis jedis = null;
		Long re = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.hdel(mapName, key);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public byte[] getHashValue(byte[] mapName, byte[] key){
		ShardedJedis jedis = null;
		byte[] re = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.hget(mapName, key);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public Map<String, String> getAllKeys(String mapName){
		ShardedJedis jedis = null;
		Map<String, String> re = null;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.hgetAll(mapName);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long incr(String key){
		ShardedJedis jedis = null;
		long re = 0;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.incr(key);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long setHashHincr(String key, String field , long value){
		ShardedJedis jedis = null;
		long re = 0;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.hincrBy(key, field, value);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long del(String key){
		ShardedJedis jedis = null;
		long re = 0;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.del(key);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long zrem(String key , String... members){
		ShardedJedis jedis = null;
		long re = 0;
		try {
			jedis = ShardRedisDBUtil.getRedisTemplate();
			re = jedis.zrem(key, members);
			ShardRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			ShardRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	
	/**
	 * 正常连接池回收
	 * @param jedis
	 */
	public static void closeJedis(ShardedJedis jedis){
		   if(jedis!=null){
			   pool.returnResource(jedis);
		   }
	}
	
	/**
	 * 异常连接池回收
	 * @param jedis
	 */
	public static void closeBreakJedis(ShardedJedis jedis){
		if(jedis!=null){
			pool.returnBrokenResource(jedis);
		}
	}
}
