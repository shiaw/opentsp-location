package com.navinfo.opentsp.platform.rprest.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;


public class RedisClusters {
	private static Logger logger = LoggerFactory.getLogger(RedisClusters.class);
	//redis节点列表：key=节点编号;value=节点信息
	static Map<String, Node> redisNodes = new ConcurrentHashMap<String, Node>();
	private static JedisPool pool = null;
	final static RedisClusters instance = new RedisClusters();
	private RedisClusters(){}
	static {
		List<Node> nodes = new ArrayList<Node>();
//		int clustersCount = Configuration.getInt("REDIS.CLUSTERS.COUNT");
		for (int i = 1; i <= 1; i++) {
			String nodeCode = Configuration.getString("REDIS.CLUSTERS." + i + ".NODECODE");
			String masterIp = Configuration.getString("REDIS.CLUSTERS." + i + ".MASTER.IP");
			int masterPort = Configuration.getInt("REDIS.CLUSTERS." + i + ".MASTER.PORT");
//			String slaveIp = Configuration.getString("REDIS.CLUSTERS." + i+ ".SLAVE.IP");
//			int slavePort = Configuration.getInt("REDIS.CLUSTERS." + i	+ ".SLAVE.PORT");

			Node node = new Node();
			node.setNodeCode(nodeCode);
			node.setMasterIp(masterIp);
			node.setMasterPort(masterPort);
//			node.setSlaveIp(slaveIp);
//			node.setSlavePort(slavePort);
			nodes.add(node);
			logger.info("redis Node:"+nodeCode);
			redisNodes.put(nodeCode, node);

			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(200);
			config.setMaxIdle(50);
			config.setMinIdle(8);
			config.setMaxWaitMillis(10000);
			config.setTestOnBorrow(true);
			config.setTestOnReturn(true);

			config.setTestWhileIdle(true);
			//表示idle object evitor两次扫描之间要sleep的毫秒数
			config.setTimeBetweenEvictionRunsMillis(30000);
			//表示idle object evitor每次扫描的最多的对象数
			config.setNumTestsPerEvictionRun(10);
			//表示一个对象至少停留在idle状态的最短时间，然后才能被idle object evitor扫描并驱逐；这一项只有在timeBetweenEvictionRunsMillis大于0时才有意义
			config.setMinEvictableIdleTimeMillis(60000);

			pool = new JedisPool(config, masterIp, masterPort, 10000, null, 0);
		}
	}

	public final static RedisClusters getInstance(){
		return instance;
	}
	/**
	 * 获取终端所在节点
	 * @param terminalId {@link Long} 终端ID
	 * @return {@link String} 节点编号
	 */
	public String getNode(long terminalId){
		return Configuration.getString("REDIS.CLUSTERS.1.NODECODE");
	}
	/**
	 * 根据节点编号获取redis连接
	 * @return
	 */
	public Jedis getJedis(){
		Jedis jedis = null;
		int i=0;
		while(i<3){
			try {
	            jedis = pool.getResource();
	            break;
	        } catch (JedisException e) {
	        	if(2 == i){
	        		logger.error("failed:jedisPool getResource.", e);
	        	}else{
	        		logger.error("获取jedis连接失败，重新获取."+i);
	        	}
	            if(jedis!=null){
	            	pool.returnBrokenResource(jedis);
	            }
	        }
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			i++;
		}
        return jedis;
	}

	public void release(Jedis jedis, boolean isBroken) {
		if (jedis != null) {
            if (isBroken) {
            	pool.returnBrokenResource(jedis);
            } else {
            	pool.returnResource(jedis);
            }
        }
	}

	/*Jedis get(JedisPool jedisPool) {
		Jedis jedis = jedisPool.getResource();
		if (jedis.isConnected()) {
			return jedis;
		} else {
			return null;
		}
	}*/
	
	public Set<String> getAllRedisNodeCode(){
		Set<String> keySet = redisNodes.keySet();
		return keySet;
	}
}
