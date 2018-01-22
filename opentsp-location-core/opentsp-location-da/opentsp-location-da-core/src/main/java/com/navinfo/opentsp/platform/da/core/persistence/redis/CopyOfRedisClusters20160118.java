package com.navinfo.opentsp.platform.da.core.persistence.redis;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.hash.AlgorithmHash;
import com.navinfo.opentsp.platform.da.core.common.hash.Hash;
import com.navinfo.opentsp.platform.da.core.common.hash.KetamaNodeLocator;
import com.navinfo.opentsp.platform.da.core.common.hash.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;


public class CopyOfRedisClusters20160118 {
	private static Logger logger = LoggerFactory.getLogger(CopyOfRedisClusters20160118.class);
	//redis池列表：key=ip+port;value=redis池
	private static Map<String, JedisPool> redis_clusters = new ConcurrentHashMap<String, JedisPool>();
	//redis节点列表：key=节点编号;value=节点信息
	static Map<String, Node> redisNodes = new ConcurrentHashMap<String, Node>();
	//redis连接线程上下文
	final static ThreadLocal<Jedis> threadLocalJedis = new ThreadLocal<Jedis>();
	//redis连接池线程上下文(redis连接回收使用)
	final static ThreadLocal<JedisPool> threadLocalJedisPool = new ThreadLocal<JedisPool>();
	//一致性hash
	static KetamaNodeLocator nodeLocator = null;
	static Hash hash = new AlgorithmHash();
	final static CopyOfRedisClusters20160118 instance = new CopyOfRedisClusters20160118();
	private CopyOfRedisClusters20160118(){}
	static {
		////////////////////////加载Redis集群////////////////////////////////
		/////因多个Redis可能存在于同一机器,故纯Ip无法标识一个Redis服务
		//////////此处采用Redis服务的Ip+端口号进行标识
		//Properties redisProperties = PropertiesUtil.getInstance().getProp();
		List<Node> nodes = new ArrayList<Node>();
		int clustersCount = Configuration.getInt("REDIS.CLUSTERS.COUNT");
		for (int i = 1; i <= clustersCount; i++) {
			String nodeCode = Configuration.getString("REDIS.CLUSTERS." + i	+ ".NODECODE");
			String masterIp = Configuration.getString("REDIS.CLUSTERS." + i	+ ".MASTER.IP");
			int masterPort = Configuration.getInt("REDIS.CLUSTERS." + i	+ ".MASTER.PORT");
			String slaveIp = Configuration.getString("REDIS.CLUSTERS." + i+ ".SLAVE.IP");
			int slavePort = Configuration.getInt("REDIS.CLUSTERS." + i	+ ".SLAVE.PORT");

			Node node = new Node();
			node.setNodeCode(nodeCode);
			node.setMasterIp(masterIp);
			node.setMasterPort(masterPort);
			node.setSlaveIp(slaveIp);
			node.setSlavePort(slavePort);
			nodes.add(node);
			logger.info("redis Node:"+nodeCode);
			redisNodes.put(nodeCode, node);

			JedisPoolConfig config = new JedisPoolConfig();
			config.setMaxTotal(Configuration.getInt("REDIS.POOL.MAXACTIVE"));
			config.setMaxIdle(Configuration.getInt("REDIS.POOL.MAXIDLE"));
			config.setMaxWaitMillis(Configuration.getInt("REDIS.POOL.MAXWAIT"));
			config.setTestOnBorrow(Configuration.getBoolean("REDIS.POOL.TESTONBORROW"));
			config.setTestOnReturn(Configuration.getBoolean("REDIS.POOL.TESTONRETURN"));

			JedisPool masterPool = new JedisPool(config, masterIp, masterPort);
			redis_clusters.put(masterIp+"_"+masterPort, masterPool);
			JedisPool slavePool = new JedisPool(config, slaveIp, slavePort);
			redis_clusters.put(slaveIp+"_"+slavePort, slavePool);
		}
		int virtualNumber = Configuration.getInt("REDIS.CLUSTERS.VIRTUAL.NUMBER");
		nodeLocator = new KetamaNodeLocator(nodes, hash, virtualNumber);
	}

	public final static CopyOfRedisClusters20160118 getInstance(){
		return instance;
	}
	/**
	 * 获取终端所在节点
	 * @param terminalId {@link Long} 终端ID
	 * @return {@link String} 节点编号
	 */
	public String getNode(long terminalId){
		return nodeLocator.getPrimary(terminalId).getNodeCode();
	}
	/**
	 * 根据节点编号获取redis连接
	 * @param nodeCode
	 * @return
	 */
	public Jedis getJedis(String nodeCode){
		Jedis jedis = threadLocalJedis.get();
		if(jedis != null && jedis.isConnected()){
			return jedis;
		}
		Node node = redisNodes.get(nodeCode);
		JedisPool masterPool = redis_clusters.get(node.getMaster());
		jedis = this.get(masterPool);

		if (jedis == null) {
			JedisPool slavePool = redis_clusters.get(node.getSlave());
			return this.get(slavePool);
		} else {
			return jedis;
		}
	}
//	/**
//	 * 获取Redis连接
//	 *
//	 * @param node
//	 *            {@link Node} Redis节点信息
//	 * @return {@link Jedis}
//	 */
//	public Jedis get(long terminalId) {
//		Node node = nodeLocator.getPrimary(terminalId);		
//		JedisPool masterPool = redis_clusters.get(node.getMasterIp());
//		Jedis jedis = get(masterPool);
//
//		if (jedis == null) {
//			JedisPool slavePool = redis_clusters.get(node.getSlaveIp());
//			return this.get(slavePool);
//		} else {
//			return jedis;
//		}
//	}

	public void release() {
		Jedis jedis = threadLocalJedis.get();
		JedisPool jedisPool = threadLocalJedisPool.get();
		if (jedis != null) {
			jedisPool.returnResource(jedis);
			threadLocalJedis.remove();
			threadLocalJedisPool.remove();
		}
	}

	Jedis get(JedisPool jedisPool) {
		Jedis jedis = jedisPool.getResource();
		if (jedis.isConnected()) {
			threadLocalJedis.set(jedis);
			threadLocalJedisPool.set(jedisPool);
			return jedis;
		} else {
			return null;
		}
	}
	public Set<String> getAllRedisNodeCode(){
		Set<String> keySet = redisNodes.keySet();
		return keySet;
	}
}
