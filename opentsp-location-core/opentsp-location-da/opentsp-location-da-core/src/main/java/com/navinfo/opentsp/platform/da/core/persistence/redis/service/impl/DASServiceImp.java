package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.IDASService;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.DASTerminalStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 *
 * @author jin_s
 *
 */
public class DASServiceImp implements IDASService {
	static RedisMapDaoImpl redisMapDao = new RedisMapDaoImpl();
	private Logger log = LoggerFactory.getLogger(DASServiceImp.class);

	public void putData(String key, Object value) {
		redisMapDao.put(RedisConstans.RedisKey.DAS_STATUS.name(), key, value);
	}

	public PlatformResponseResult addTerminalStatusSimple(DASTerminalStatus status) {
		boolean b=redisMapDao.put(RedisConstans.RedisKey.DAS_STATUS.name(), String.valueOf(status.getTerminalId()), status);
//		log.info("save status:"+RedisConstans.RedisKey.DAS_STATUS.name()+"---"+String.valueOf(status.getTerminalId()));
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}
	}
	public PlatformResponseResult addTerminalStatus(List<DASTerminalStatus> status) {
		String[] fields = new String[status.size()];
		Object[] values = new Object[status.size()];
		for(int i =0 , length = status.size(); i< length ; i++){
			fields[i] = String.valueOf(status.get(i).getTerminalId());
			values[i] = status.get(i);
		}
		boolean b=redisMapDao.put(RedisConstans.RedisKey.DAS_STATUS.name(), fields, values);
		if(b){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}

	}


	public DASTerminalStatus findTerminalStatus(long terminalId) {
		return (DASTerminalStatus) redisMapDao.get(RedisConstans.RedisKey.DAS_STATUS.name(), String.valueOf(terminalId));
	}


	public List<DASTerminalStatus> findTerminalStatus(long... terminalIds) {
		String[] fields = new String[terminalIds.length];
		for(int i = 0 , length = terminalIds.length ; i < length; i++){
			fields[i] = String.valueOf(terminalIds[i]);
		}
		List<DASTerminalStatus> list = redisMapDao.get(RedisConstans.RedisKey.DAS_STATUS.name(), fields);
		if(list==null||list.size()==0){
			return null;
		}
		return list;
	}
	public PlatformResponseResult removeTerminalStatusSimple(String[] tids) {
		long b=redisMapDao.del(RedisConstans.RedisKey.DAS_STATUS.name(), tids);
		if(b>0){
			return PlatformResponseResult.success;
		}else{
			return PlatformResponseResult.failure;
		}
	}
}
