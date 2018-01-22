package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.IDASService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author jin_s
 *
 */
public class DASTaskStatusServiceImpl implements IDASService {
	static RedisMapDaoImpl redisMapDao = new RedisMapDaoImpl();
	/**
	 * 完成状态
	 */
	public static int COMPLETE=1;
	/**
	 * 开始状态
	 */
	public static int UNCOMPLETE=0;

	public void saveDataComplete(String key) {
		redisMapDao.put(RedisConstans.RedisKey.DAS_TASK_STATUS_KEY.name(), key, COMPLETE);
	}
	/**
	 * 初始化终端计算状态
	 * @param terminalIds
	 * @return
	 */
	public PlatformResponseResult initTerminalStatus(long[] terminalIds) {
		boolean b=false;
		for(long key:terminalIds){
			b =redisMapDao.put(RedisConstans.RedisKey.DAS_TASK_STATUS_KEY.name(),String.valueOf(key), UNCOMPLETE);
		}
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}
	}


	/**
	 * 批量查询终端的计算状态
	 * @param terminalIds
	 * @return
	 */
	public Map<Long,Integer> findTerminalStatus(List<Long> terminalIds) {
		Map <Long,Integer> retMap=new HashMap<Long,Integer>();
		for(long key:terminalIds){
			Object value = redisMapDao.get(RedisConstans.RedisKey.DAS_TASK_STATUS_KEY.name(), String.valueOf(key));
			if(value!=null){
				retMap.put(key, (int)value);
			}
		}
		if(retMap.isEmpty()){
			return null;
		}

		return retMap;
	}

}
