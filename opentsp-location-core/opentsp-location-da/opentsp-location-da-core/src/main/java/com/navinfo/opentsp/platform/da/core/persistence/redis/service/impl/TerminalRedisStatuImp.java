package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisMapDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.RDTerminalStatus;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ITerminalRedis;

import java.util.List;



public class TerminalRedisStatuImp implements ITerminalRedis {
	final static IRedisMapDao redis_map_dao = new RedisMapDaoImpl();
	@Override
	public void addTerminalStatus(RDTerminalStatus status) {
		redis_map_dao.put(RedisConstans.RedisKey.TERMINAL_STATUS.name(), String.valueOf(status.getTerminalId()), status);
	}

	@Override
	public void addTerminalStatus(RDTerminalStatus... status) {
		String[] fields = new String[status.length];
		Object[] values = new Object[status.length];
		for(int i =0 , length = status.length; i< length ; i++){
			fields[i] = String.valueOf(status[i].getTerminalId());
			values[i] = status[i];
		}
		redis_map_dao.put(RedisConstans.RedisKey.TERMINAL_STATUS.name(), fields, values);
	}

	@Override
	public RDTerminalStatus findTerminalStatus(long terminalId) {
		return (RDTerminalStatus) redis_map_dao.get(RedisConstans.RedisKey.TERMINAL_STATUS.name(), String.valueOf(terminalId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RDTerminalStatus> findTerminalStatus(long... terminalIds) {
		String[] fields = new String[terminalIds.length];
		for(int i = 0 , length = terminalIds.length ; i < length; i++){
			fields[i] = String.valueOf(terminalIds[i]);
		}
		return redis_map_dao.get(RedisConstans.RedisKey.TERMINAL_STATUS.name(), fields);
	}

	@Override
	public void delTerminalStatus(long terminalId) {
		redis_map_dao.del(RedisConstans.RedisKey.TERMINAL_STATUS.name(), new String[]{String.valueOf(terminalId)});
	}

	@Override
	public void updateTerminalStatus(RDTerminalStatus status) {
		this.addTerminalStatus(status);
	}

}
