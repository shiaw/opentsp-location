package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisListDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisSetDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisListDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisSetDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILinkService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans.RedisKey;

import java.util.List;



public class LinkServiceImp implements ILinkService {
	private final static IRedisSetDao redis_set_dao = new RedisSetDaoImpl();
	private final static IRedisListDao redis_list_dao = new RedisListDaoImpl();
	
	

	@Override
	public void saveLinkExceptionData(long nodeCode, long nodeCodeTo,
			int dataType, byte[] dataValue) {
		String dataKey = com.navinfo.opentsp.platform.da.core.persistence.redis.RedisKey.getLinkExceptionKey(new Long(nodeCode).intValue(), new Long(nodeCodeTo).intValue(),
				dataType);
		redis_list_dao.pushForLimit(redis_list_dao.string2byte(dataKey), dataValue);
		redis_set_dao.add(RedisKey.EXCEPTION_LINK_DATA.name(), dataKey);
	}

	@Override
	public List<byte[]> queryLinkExceptionData(long nodeCode,
			long nodeCodeTo, int dataType, int size) {
		
		return null;
	}

	@Override
	public void delLinkExceptionData(long nodeCode, long nodeCodeTo, int dataType) {
		// TODO Auto-generated method stub
		
	}

}
