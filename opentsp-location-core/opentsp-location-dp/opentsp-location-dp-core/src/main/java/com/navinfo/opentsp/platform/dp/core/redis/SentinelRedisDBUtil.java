package com.navinfo.opentsp.platform.dp.core.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 满足哨兵集群Sentinel
 * @author zhanhk
 */
public class SentinelRedisDBUtil extends RedisSource{

	private static Logger logger = LoggerFactory.getLogger(SentinelRedisDBUtil.class);
	
	private static JedisSentinelPool pool;

	static {
		logger.info("开始初始化redis连接池");
		String[] addressArr = redis_address.split(":");
		Set<String> set = new HashSet<String>();
		set.add(addressArr[0] + ":" + addressArr[1]);
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(Integer.valueOf(max_idle));
		config.setMaxTotal(Integer.valueOf(max_active));
		config.setMaxWaitMillis(Long.valueOf(max_wait));
		config.setTestOnBorrow(Boolean.valueOf(isTest));
		pool = new JedisSentinelPool(addressArr[2], set, config);
		logger.info("成功初始化redis连接池");
	}
	
	public static Jedis getRedisTemplate() {
		Jedis Jedis = pool.getResource();
		return Jedis;
	}
	
	public static void setValue(byte[] key, byte[] value) {
		Jedis jedis = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void setValue(String key, String value) {
		Jedis jedis = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void setExpireValue(byte[] key, byte[] value, int second){
		Jedis jedis = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			jedis.set(key, value);
			jedis.expire(key, second);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void setHashValue(String mapName, String key, String value){
		Jedis jedis = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			jedis.hset(mapName, key, value);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
	}

	public void setHashValue(byte[] mapName, byte[] key, byte[] value){
		Jedis jedis = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			jedis.hset(mapName, key, value);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public void zadd(String key, Map<String, Double> scoreMembers){
		Jedis jedis = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			jedis.zadd(key, scoreMembers);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
	}
	
	public Set<String> zrangeByscore(String key, String value){
		Jedis jedis = null;
		Set<String> result = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			result = jedis.zrangeByScore(key, value, "+inf",0,1);
			SentinelRedisDBUtil.closeJedis(jedis);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return result;
	}
	
	public byte[] getValue(byte[] key) {
		Jedis jedis = null;
		byte[] re = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.get(key);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public String getValue(String key) {
		Jedis jedis = null;
		String re = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.get(key);	
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public String getHashValue(String mapName, String key){
		Jedis jedis = null;
		String re = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.hget(mapName, key);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public byte[] getHashValue(byte[] mapName, byte[] key){
		Jedis jedis = null;
		byte[] re = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.hget(mapName, key);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public Map<String, String> getAllKeys(String mapName){
		Jedis jedis = null;
		Map<String, String> re = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.hgetAll(mapName);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public long incr(String key){
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.incr(key);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long setHashHincr(String key, String field , long value){
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.hincrBy(key, field, value);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long del(String key){
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.del(key);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}
	
	public long zrem(String key , String... members){
		Jedis jedis = null;
		long re = 0;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.zrem(key, members);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	public Long delHashValue(String mapName, String key) {
		Jedis jedis = null;
		Long re = null;
		try {
			jedis = SentinelRedisDBUtil.getRedisTemplate();
			re = jedis.hdel(mapName, key);
			SentinelRedisDBUtil.closeJedis(jedis);
		} catch (Exception e) {
			e.printStackTrace();
			SentinelRedisDBUtil.closeBreakJedis(jedis);
		}
		return re;
	}

	/**
	 * 正常连接池回收
	 * @param jedis
	 */
	public static void closeJedis(Jedis jedis){
		   if(jedis!=null){
			   pool.returnResource(jedis);
		   }
	}
	
	/**
	 * 异常连接池回收
	 * @param jedis
	 */
	public static void closeBreakJedis(Jedis jedis){
		if(jedis!=null){
			pool.returnBrokenResource(jedis);
		}
	}
}
