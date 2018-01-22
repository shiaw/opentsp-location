package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import java.util.List;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans.RedisKey;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisMapDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.ServiceUniqueMark;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.IUpperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class UpperServiceImp implements IUpperService {
	private final static IRedisMapDao redis_map_dao = new RedisMapDaoImpl();
	private Logger logger = LoggerFactory.getLogger(UpperServiceImp.class);

	@Override
	public boolean saveServiceUniqueMark(ServiceUniqueMark uniqueMark) {
		boolean flag = false;
		while(true){
			flag = redis_map_dao.put(RedisKey.SERVICE_UNIQUEMARK.name(),
					String.valueOf(uniqueMark.getUniqueMark()), uniqueMark);
			Object object = redis_map_dao.get(RedisKey.SERVICE_UNIQUEMARK.name(), String.valueOf(uniqueMark.getUniqueMark()));
			if(null != object){
				logger.info("保存的ServiceUniqueMark对象是:"+object.toString());
				break;
			}else{
				logger.error("查不到key:"+RedisKey.SERVICE_UNIQUEMARK.name()+","+ String.valueOf(uniqueMark.getUniqueMark())+"继续执行保存方法");
			}
		}
		return flag;
	}

	@Override
	public void updateServiceUniqueMark(ServiceUniqueMark uniqueMark) {
		redis_map_dao.put(RedisKey.SERVICE_UNIQUEMARK.name(),
				String.valueOf(uniqueMark.getUniqueMark()), uniqueMark);
	}

	@Override
	public ServiceUniqueMark findServiceUniqueMark(long uniqueMark) {
		return (ServiceUniqueMark) redis_map_dao.get(
				RedisKey.SERVICE_UNIQUEMARK.name(), String.valueOf(uniqueMark));
	}

	@Override
	public void delServiceUniqueMark(long uniqueMark) {
		redis_map_dao.del(RedisKey.SERVICE_UNIQUEMARK.name(),
				String.valueOf(uniqueMark));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ServiceUniqueMark> findAllServiceUniqueMark() {
		return (List<ServiceUniqueMark>) redis_map_dao
				.allValues(RedisKey.SERVICE_UNIQUEMARK.name());
	}

}
