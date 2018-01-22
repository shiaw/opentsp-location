package com.navinfo.opentsp.platform.da.core.persistence.redis;

import com.navinfo.opentsp.platform.da.core.common.Configuration;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashSet;
import java.util.Set;


public class RedisStatic {
	/////当前redis:1=主服务;2=从服务/////
	private static int current_redis = 1;
	private static RedisStatic instance;
	////////主从redis服务连接池////////////////
	private static JedisSentinelPool masterPool;
	//	private static JedisPool slavePool;
	private static Logger logger = LoggerFactory.getLogger(RedisStatic.class);
	static{
//		String nodeCode = Configuration.getString("REDIS.STATIC.NODECODE");
//		String masterIp = Configuration.getString("REDIS.STATIC.MASTER.IP");
//		int masterPort = Configuration.getInt("REDIS.STATIC.MASTER.PORT");
//		String slaveIp = Configuration.getString("REDIS.STATIC.SLAVE.IP");
//		int slavePort = Configuration.getInt("REDIS.STATIC.SLAVE.PORT");

		int maxActive = Configuration.getInt("REDIS.POOL.MAXACTIVE");
		int maxIdle = Configuration.getInt("REDIS.POOL.MAXIDLE");
		int maxWait = Configuration.getInt("REDIS.POOL.MAXWAIT");
		boolean testOnBorrwo = Configuration.getBoolean("REDIS.POOL.TESTONBORROW");
//		boolean testOnReturn = Configuration.getBoolean("REDIS.POOL.TESTONRETURN");

//		JedisPoolConfig config = new JedisPoolConfig();
//		config.setMaxTotal(maxActive);
//		config.setMaxIdle(maxIdle);
//		config.setMaxWaitMillis(maxWait);
//		config.setTestOnBorrow(testOnBorrwo);
//		config.setTestOnReturn(testOnReturn);
//		masterPool = new JedisPool(config, masterIp, masterPort,10000,null,0);

		String redis_address = Configuration.getString("REDIS.SENTINE.NODE");
		String password = Configuration.getString("REDIS.SENTINE.PASSWORD");
		String[] addressArr = redis_address.split(":");
		Set<String> set = new HashSet<>();
		set.add(addressArr[0] + ":" + addressArr[1]);
		GenericObjectPoolConfig config = new GenericObjectPoolConfig();
		config.setMaxIdle(Integer.valueOf(maxIdle));
		config.setMaxTotal(Integer.valueOf(maxActive));
		config.setMaxWaitMillis(Long.valueOf(maxWait));
		config.setTestOnBorrow(Boolean.valueOf(testOnBorrwo));
		masterPool = new JedisSentinelPool(addressArr[2], set, config,password);
	}

	public static RedisStatic getInstance() {
		if (instance == null) {
			synchronized (RedisStatic.class) {
				if (instance == null) {
					instance = new RedisStatic();
				}
			}
		}
		return instance;
	}

	public Jedis getJedis() {
		Jedis jedis = null;
		try {
			jedis = masterPool.getResource();
		} catch (JedisException e) {
			logger.error("failed:jedisPool getResource.", e);
			if(jedis!=null){
				masterPool.returnBrokenResource(jedis);
			}
			throw e;
		}
		return jedis;
	}

	public void release(Jedis jedis, boolean isBroken) {
		if (jedis != null) {
			if (isBroken) {
				masterPool.returnBrokenResource(jedis);
			} else {
				masterPool.returnResource(jedis);
			}
		}
	}
}
