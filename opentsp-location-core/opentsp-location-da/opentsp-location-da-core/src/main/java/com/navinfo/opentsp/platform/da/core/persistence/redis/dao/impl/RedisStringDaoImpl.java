package com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisStringDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;


public class RedisStringDaoImpl extends RedisImp implements IRedisStringDao {
	private static Logger logger = LoggerFactory.getLogger(RedisStringDaoImpl.class);
	@Override
	public void set(String key , Object value){
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			jedis.set(super.string2byte(key), super.object2byte(value));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
	}

	@Override
	public void set(String key, Object value, int expire){
		if (expire == -1){
			this.set(key, value);
			return;
		}
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			jedis.setex(super.string2byte(key), expire, super.object2byte(value));
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		return ;
	}

	@Override
	public Object get(String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		byte[] bytes = null;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			bytes = jedis.get(super.string2byte(key));
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
	public String getString(String key) {
		Object object = this.get(key);
		if(object != null){
			return object.toString();
		}else{
			return "";
		}
	}
}
