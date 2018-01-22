package com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl;

import java.util.HashSet;
import java.util.Set;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisSetDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;


public class RedisSetDaoImpl extends RedisImp implements IRedisSetDao {
	private static Logger logger = LoggerFactory.getLogger(RedisSetDaoImpl.class);
	@Override
	public long add(String setName, Object value) {
		Jedis jedis = null;
		boolean isBroken = false;
		long result = 0;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			result = jedis.sadd(super.string2byte(setName), super.object2byte(value));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}

	@Override
	public long add(String setName, Object... values) {
		Jedis jedis = null;
		boolean isBroken = false;
		long result = 0;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			result = jedis.sadd(super.string2byte(setName), super.object2byte(values));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}

	@Override
	public Set get(String setName) {
		Set<Object> result = new HashSet<Object>();
		Jedis jedis = null;
		boolean isBroken = false;
		Set<byte[]> set = null;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			set = jedis.smembers(super.string2byte(setName));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		if(set != null){
			for (byte[] bytes : set) {
				result.add(super.byte2object(bytes));
			}
		}
		return result;
	}

	@Override
	public long remove(String setName, Object... values) {
		Jedis jedis = null;
		boolean isBroken = false;
		long result = 0;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			result = jedis.zrem(super.string2byte(setName), super.object2byte(values));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return result;
	}

	@Override
	public Object pop(String setName) {
		Jedis jedis = null;
		boolean isBroken = false;
		byte[] bytes = null;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			bytes = jedis.spop(super.string2byte(setName));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		if(bytes != null){
			return super.byte2object(bytes);
		}else{
			return null;
		}
	}
	
	public Object randmember(String setName) {
		Jedis jedis = null;
		boolean isBroken = false;
		byte[] bytes = null;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			bytes = jedis.srandmember(super.string2byte(setName));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		if(bytes != null){
			return super.byte2object(bytes);
		}else{
			return null;
		}
	}

	@Override
	public void del(String setName) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			jedis.del(super.string2byte(setName));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
	}
}
