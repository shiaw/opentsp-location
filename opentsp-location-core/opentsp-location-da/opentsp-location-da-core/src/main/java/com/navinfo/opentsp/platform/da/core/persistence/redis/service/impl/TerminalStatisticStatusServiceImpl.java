package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Set;

import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisSetDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisSetDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.TerminalStatisticStatusService;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllAlarm;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TerminalStatisticStatusServiceImpl implements
		TerminalStatisticStatusService {

	private IRedisSetDao dao = new RedisSetDaoImpl();
	private static Logger logger = LoggerFactory.getLogger(TerminalStatisticStatusServiceImpl.class);

	@Override
	public PlatformResponseResult saveStatus(int type, long terminalId) {
		if(type == LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE){
			String dayString = DateUtils.format(new Date(), DateFormat.YYYYMMDD);
			logger.info("保存滞留超时报警缓存："+terminalId);
			dao.add(RedisConstans.RedisKey.STATISTIC_STATUS_OVERTIMEPARKINAREA.name()+"_"+dayString, terminalId);
			return PlatformResponseResult.success;
		}else{
			logger.error("参数错误，type"+type+",tid:"+terminalId);
		}
		return PlatformResponseResult.failure;
	}

	@Override
	public Set<Long> getTerminalsByType(int type) {
		if(type == LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE){
			String dayString = DateUtils.format(new Date(), DateFormat.YYYYMMDD);
			Set set = dao.get(RedisConstans.RedisKey.STATISTIC_STATUS_OVERTIMEPARKINAREA.name()+"_"+dayString);
			logger.info("查询滞留超时报警缓存个数："+set.size());
			return set;
		}
		return null;
	}

	@Override
	public boolean removeLastDayStatus(int type) {
		if(type == LCAllAlarm.AlarmAddition.overtimeParkingInArea_VALUE){
			Calendar current = DateUtils.current();
			current.set(Calendar.DAY_OF_YEAR, current.get(Calendar.DAY_OF_YEAR)-1);
			String dayString = DateUtils.format(current.getTime(), DateFormat.YYYYMMDD);
			dao.del(RedisConstans.RedisKey.STATISTIC_STATUS_OVERTIMEPARKINAREA.name()+"_"+dayString);
			return true;
		}
		return false;
	}
}
