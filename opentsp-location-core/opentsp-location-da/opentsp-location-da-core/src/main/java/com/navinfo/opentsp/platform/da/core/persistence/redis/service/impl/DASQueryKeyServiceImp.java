package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.IDASQueryKeyService;

import java.util.Map;

/**
 *
 * @author jin_s
 *
 */
public class DASQueryKeyServiceImp implements IDASQueryKeyService {
	RedisMapDaoImpl redisMapDao = new RedisMapDaoImpl();
	/**
	 * 查询Key的过期时间
	 */
	//private static int EXPIRE_TIME = Integer.parseInt(Configuration.getString("Rmi.Service.Expire.Time"));
	@Override
	public void test(String key, Object value) {
		redisMapDao.put(RedisConstans.RedisKey.DAS_QUERY_KEY.name(), key, value);
	}
	/**
	 * 存储key
	 */
	public void putData(String key, Object value) {
		redisMapDao.put(RedisConstans.RedisKey.DAS_QUERY_KEY.name(), key, value);
	}


	/**
	 * 查询key
	 */
	public Object findQueryKey(String  queryKey) {
		return	 (Object)  redisMapDao.get(RedisConstans.RedisKey.DAS_QUERY_KEY.name(), queryKey);
	}
	/**
	 * 查询key
	 */
	public Map<String, Object> findQueryKeys() {
		return  redisMapDao.get(RedisConstans.RedisKey.DAS_QUERY_KEY.name());

	}
	/**
	 * 删除key
	 * @param queryKey
	 * @return
	 */
	public long removeQueryKey(String  queryKey) {
		return  redisMapDao.del(RedisConstans.RedisKey.DAS_QUERY_KEY.name(), new String[]{queryKey});
	}

}
