package com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisListDao;
import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;


public class RedisListDaoImpl extends RedisImp implements IRedisListDao {
	private final String QUERY_MARK = "_QUERY";
	private final String DATA_PART_MARK = "_DATA_PART";
	/***/
	private static Map<String, String> current_redis_key = new ConcurrentHashMap<String, String>();
	private static Logger logger = LoggerFactory.getLogger(RedisListDaoImpl.class);
	public String getCurrentKey(final String key){
		return current_redis_key.get(key);
	}
	/**
	 * 检查库中是否存在key
	 * @param key
	 * @return
	 */
	public boolean existKey(String key){
		Jedis jedis = null;
		boolean isBroken = false;
		boolean result = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			result = jedis.exists(key);
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}
	public void updateCurrentKey(String key , String dataKey){
		current_redis_key.put(key, dataKey);
	}
	public long pushForLimit(String key, Object... values) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			jedis.rpush(key.getBytes(),
					super.object2byte(values));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return 0;
	}

	@Override
	public long pushForLimit(String key, Object value) {
		Jedis jedis = null;
		boolean isBroken = false;
		long size = 0;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			String dataKey = this.getCurrentKey(key);
			if (dataKey == null) {
				dataKey = this.generateDataKey(key);
			}
			size = jedis.rpush(dataKey.getBytes(),
					ObjectUtils.objectToBytes(value));
			// 当集合大于最大存放条数,创建新分区key,更新当前数据key,并存入缓存
			if (size >= RedisConstans.LIST_PART_MAX_SIZE) {
				String newPartKey = key + "_" + System.nanoTime();
				this.updateCurrentKey(key, newPartKey);
				jedis.rpush((key + DATA_PART_MARK).getBytes(),
						newPartKey.getBytes());
			}
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return size;
	}

	private String generateDataKey(String key) {
		String newDataKey = key + "_" + System.nanoTime();
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			jedis.rpush((key + DATA_PART_MARK).getBytes(), newDataKey.getBytes());
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		this.updateCurrentKey(key, newDataKey);
		return newDataKey;
	}

	@Override
	public List get(String key) {
		List<Object> result = new ArrayList<Object>();
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			logger.info("查询转存/统计终端缓存的key为："+key + DATA_PART_MARK);
			List<byte[]> keyParts = jedis.lrange((key + DATA_PART_MARK).getBytes(),
					0, -1);
			for (byte[] keyPart : keyParts) {
				List<byte[]> _temp_result = jedis.lrange(keyPart, 0, -1);
				for (byte[] bytes : _temp_result) {
					result.add(super.byte2object(bytes));
				}
			}
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}
	@Override
	public List<byte[]> getForBytes(String key){
		List<byte[]> result = new ArrayList<byte[]>();
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			List<byte[]> keyParts = jedis.lrange((key + DATA_PART_MARK).getBytes(),
					0, -1);
			for (byte[] keyPart : keyParts) {
				List<byte[]> _temp_result = jedis.lrange(keyPart, 0, -1);
				result.addAll(_temp_result);
			}
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}
	@Override
	public long del(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		long result = 0;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			List<byte[]> keyParts = jedis.lrange((key + DATA_PART_MARK).getBytes(),
					0, -1);
			result = 0;
			for (byte[] dataKey : keyParts) {
				jedis.lrem((key + DATA_PART_MARK).getBytes(), 1, dataKey);
				result += (jedis.llen(dataKey));
				jedis.del(dataKey);
			}
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}

	@Override
	public long pushForLimit(byte[] key, byte[] value) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long push(String key, Object... values) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long push(String key, Object value) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public long push(byte[] key, byte[] value) {
		// TODO Auto-generated method stub
		return 0;
	}

}

class User implements Serializable {
	private static final long serialVersionUID = 1L;
	public int id;
	public String name;

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + "]";
	}

}