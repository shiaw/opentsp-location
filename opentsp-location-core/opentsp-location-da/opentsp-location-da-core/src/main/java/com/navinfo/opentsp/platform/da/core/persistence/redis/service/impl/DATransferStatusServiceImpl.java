package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisStatic;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisMapDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.IDASService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;


/**
 *
 * @author jin_s
 *
 */
public class DATransferStatusServiceImpl implements IDASService {
	private static Logger logger = LoggerFactory.getLogger(DATransferStatusServiceImpl.class);
	static IRedisMapDao redisMapDao = new RedisMapDaoImpl();
	/**
	 * 完成状态
	 */
	public static int COMPLETE=1;
	/**
	 * 开始状态
	 */
	public static int UNCOMPLETE=0;

	public void saveDataComplete(Long terminalId) {
		redisMapDao.put(RedisConstans.RedisKey.DA_TRANSFER_STATUS_KEY.name(), String.valueOf(terminalId), COMPLETE);
	}
	/**
	 * 初始化终端计算状态
	 * @param terminalIds
	 * @return
	 */
	public PlatformResponseResult initTerminalStatus(List<Long> terminalIds) {
		boolean b=false;
		for(long key:terminalIds){
			b =redisMapDao.put(RedisConstans.RedisKey.DA_TRANSFER_STATUS_KEY.name(),String.valueOf(key), UNCOMPLETE);
		}
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}
	}
	/**
	 * 查询单终端的计算状态
	 * @param terminalId
	 * @return
	 */

	public int findTerminalStatus(long terminalId) {
		return (int) redisMapDao.get(RedisConstans.RedisKey.DA_TRANSFER_STATUS_KEY.name(), String.valueOf(terminalId));
	}

	/**
	 * 批量查询终端的转存状态
	 * @param terminalIds
	 * @return
	 */
	public Map<Long,Integer> findTerminalStatus(List<Long> terminalIds) {
		Map <Long,Integer> retMap=new HashMap<Long,Integer>();
		for(long key:terminalIds){
			Object value = redisMapDao.get(RedisConstans.RedisKey.DA_TRANSFER_STATUS_KEY.name(), String.valueOf(key));
			if(value!=null){
				retMap.put(key, (int)value);
			}
		}
		if(retMap.isEmpty()){
			return null;
		}
		return retMap;
	}
	/**
	 * 初始化终端计算状态
	 * @param terminalIds
	 * @return
	 */
	public PlatformResponseResult initCANStatus(List<Long> terminalIds) {
		boolean b=false;
		for(long key:terminalIds){
			b =redisMapDao.put(RedisConstans.RedisKey.DA_TRANSFER_CAN_STATUS_KEY.name(),String.valueOf(key), UNCOMPLETE);
		}
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}
	}
	public void saveCANDataComplete(Long terminalId) {
		redisMapDao.put(RedisConstans.RedisKey.DA_TRANSFER_CAN_STATUS_KEY.name(), String.valueOf(terminalId), COMPLETE);
	}
	public Map<Long,Integer> findCANStatus(List<Long> terminalIds) {
		Map <Long,Integer> retMap=new HashMap<Long,Integer>();
		Jedis jedis = null;
		boolean isBroken = false;
		List<Object> list = null;
		try {
			jedis = RedisStatic.getInstance().getJedis();
			Pipeline pipelined = jedis.pipelined();
			for(long key:terminalIds){
				pipelined.hget(RedisConstans.RedisKey.DA_TRANSFER_CAN_STATUS_KEY.name(),String.valueOf(key));
			}
			list = pipelined.syncAndReturnAll();
		} catch (JedisException e) {
			isBroken = true;
			logger.error(e.getMessage(),e);
		}finally{
			RedisStatic.getInstance().release(jedis,isBroken);
		}
		for(int i=0;i<terminalIds.size();i++){
			Object object = list.get(i);
			if(null != object){
				retMap.put(terminalIds.get(i), (int)object);
			}
		}
		if(retMap.isEmpty()){
			return null;
		}
		return retMap;
	}
}
