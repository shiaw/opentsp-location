package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.common.Configuration;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.DASTerminalStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 终端上下线状态缓存
 * @author jin_s
 *
 */
public class TerminalOnOffStatusServiceImpl extends RedisImp {

	final static RedisMapDaoImpl redisMapDao = new RedisMapDaoImpl();
	private static List<TerminalOnOffStatus> cache = new ArrayList<TerminalOnOffStatus>();
	private final static int GPS_LOCAL_TO_REDIS_COMMIT_SIZE = Configuration
			.getInt(Constant.ConfigKey.GPS_LOCAL_TO_REDIS_COMMIT_SIZE);

//	private static RedisTemplate redisTemplate;

	/**
	 *  添加或更新终端状态
	 * @param key
	 * @param value
	 */
	public void putData(String key, TerminalOnOffStatus value) {
		cache.add(value);
		if(cache.size()>= GPS_LOCAL_TO_REDIS_COMMIT_SIZE){
			List<TerminalOnOffStatus> temp = cache;
			cache = new ArrayList<TerminalOnOffStatus>();
			saveAllterminalStatus(temp);
			temp = null;
		}
//		redisMapDao.put(RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name(), key, value);
	}
	/**
	 *  添加】终端状态
	 * @param status
	 */
	public PlatformResponseResult addTerminalStatusSimple(DASTerminalStatus status) {
		boolean b=redisMapDao.put(RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name(), String.valueOf(status.getTerminalId()), status);
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}
	}
	/**
	 * 批量 添加终端状态
	 * @param status
	 */
	public PlatformResponseResult addTerminalStatus(List<DASTerminalStatus> status) {
		String[] fields = new String[status.size()];
		Object[] values = new Object[status.size()];
		for(int i =0 , length = status.size(); i< length ; i++){
			fields[i] = String.valueOf(status.get(i).getTerminalId());
			values[i] = status.get(i);
		}
		boolean b=redisMapDao.put(RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name(), fields, values);
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}

	}

	/**
	 * 查询终端下线状态缓存
	 * @param terminalId
	 * @return
	 */
	public TerminalOnOffStatus getTerminalOnOffStatus(long terminalId) {

		Object status=redisMapDao.get(RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name(), String.valueOf(terminalId));
		if(null!=status){
			return (TerminalOnOffStatus)status;
		}
		return null;
	}


	public Map<Long, TerminalOnOffStatus> getAllTerminalStatus() {
		Map<Long, TerminalOnOffStatus> map = new HashMap<Long, TerminalOnOffStatus>();
		String mapName = RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name();
		Map<String, Object> result = redisMapDao.get(mapName);
		if (result != null) {
			for (Map.Entry<String, Object> entry : result.entrySet()) {
				try {
					Long key = string2Long(entry.getKey());
					map.put(key, (TerminalOnOffStatus) entry.getValue());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return map;
		}
		return null;
	}

	public Map<Long, TerminalOnOffStatus> getTerminalStatus(List<Long> tids) {
		Map<Long, TerminalOnOffStatus> map = new HashMap<Long, TerminalOnOffStatus>();
		String mapName = RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name();
		Map<Long, Object> result = redisMapDao.pipelineObjects(mapName,tids);
		if (result != null) {
			for (Map.Entry<Long, Object> entry : result.entrySet()) {
				try {
					map.put(entry.getKey(), (TerminalOnOffStatus) entry.getValue());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			return map;
		}
		return null;
	}
	public  void saveAllterminalStatus(List<TerminalOnOffStatus> statusList) {
		List<String> terminals = new ArrayList<String>();
		List<byte[]> statusDatas = new ArrayList<byte[]>();
		for (TerminalOnOffStatus status : statusList) {
			terminals.add(String.valueOf(status.getTerminalId()));
			statusDatas.add(super.object2byte(status));
		}

		redisMapDao.saveTostaticRedisInBatch(RedisConstans.RedisKey.DA_TERMINAL_ON_OFF_STATUS.name(), terminals,
				statusDatas);
	}

	public  void saveStatusDataToRedis(){
		List<TerminalOnOffStatus> temp = cache;
		cache = new ArrayList<TerminalOnOffStatus>();
		saveAllterminalStatus(temp);
		temp = null;
	}

	public static Map<Long, Long> getTerminalFirstRecieveDate(List<Long> tids){
		Map<Long, Long> map = new HashMap<Long,Long>();
		String mapName = RedisConstans.RedisKey.DA_TERMINAL_FIRST_RECIEVE.name();
		Map<Long, Object> result = null;
		Map<String, Object> resultMap = null;
		if(tids.size()>0&&tids!=null){
			result = redisMapDao.pipelineObjects(mapName,tids);
			if (result != null) {
				for (Map.Entry<Long, Object> entry : result.entrySet()) {
					try {
						map.put(entry.getKey(),(long) entry.getValue());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				return map;
			}
		}else{
			resultMap = redisMapDao.get(mapName);
			if (resultMap != null) {
				for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
					try {
						Long key = string2Long(entry.getKey());
						map.put(key, (long) entry.getValue());
					} catch (Exception e) {
						// TODO: handle exception
					}
				}
				return map;
			}
		}
		return null;
	}

	public static Long string2Long(String src) {
		try {
			return Long.parseLong(src);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Long getTerminalRecieveDate(long terminalId){
		Object time = redisMapDao.get(RedisConstans.RedisKey.DA_TERMINAL_FIRST_RECIEVE.name(),String.valueOf(terminalId));
		if(time!=null){
			return (long)time;
		}
		return 0L;
	}

	/**
	 * 终端首次接入时间
	 * @param key
	 * @param value
	 */
	public void addData(String key,long value){
		redisMapDao.put(RedisConstans.RedisKey.DA_TERMINAL_FIRST_RECIEVE.name(), key, value);
	}
}
