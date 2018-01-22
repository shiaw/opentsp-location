package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.WFlowService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

/**
 * 流量数据redis操作
 * @author jin_s
 *
 */
public class WFlowServiceImp implements WFlowService {
	static RedisMapDaoImpl redisMapDao = new RedisMapDaoImpl();

	@Override
	public void test(String key, Object value) {

	}

	/**
	 * 存储流量数据
	 * @param key
	 * @param field
	 * @param value
	 */
	public void putCacheData(String key,Object value) {
		redisMapDao.put(RedisConstans.RedisKey.TERMINAL_FLOW.name(), key, value);
	}



	/**
	 * 查询当日流量缓存数据
	 * @param key
	 * @return
	 */
	public Object findTerminalData(long terminalId) {
		return redisMapDao.get(RedisConstans.RedisKey.TERMINAL_FLOW.name(),String.valueOf(terminalId));
	}
	/**
	 * 查询流量缓存数据
	 * @param key
	 * @return
	 */
	public Collection getAllCacheData(String key) {
		Map<String, Object> cacheMap=redisMapDao.get(key);
		TreeMap<String,Object> sort=new TreeMap<String,Object>();
		if(!cacheMap.isEmpty()){
			cacheMap.remove("currentDate");
			Set<Entry<String, Object>> entrySet = cacheMap.entrySet();
			ArrayList<String> keyList = new ArrayList<String>();

			for (Entry<String,Object> entry:entrySet){

				sort.put(entry.getKey(), entry.getValue());
				keyList.add((String) entry.getKey());
			}
			if(keyList.size()>0){
				String[] keys=new String [keyList.size()] ;
				for(int i=0;i<keyList.size();i++){
					keys[i]=keyList.get(i);
				}
				redisMapDao.del(key, keys);
			}

		}
		return sort.values();
	}

}
