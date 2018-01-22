package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisMapDaoImpl;

public class TerminalRuleData {
	static RedisMapDaoImpl mapDao = new RedisMapDaoImpl();

	// 获取所有终端停滞超时报警参数
	//see
	public static Map<Long, Long> getTerminalRuleFromStaticRedis() {
		Map<Long, Long> map = new HashMap<Long, Long>();
		RedisImp redisImp = new RedisImp();
		Map<byte[], byte[]> result = mapDao.getFromStaticRedis(RedisConstans.RedisKey.ALARM_CANCEL_REGULAR.name());
		if (result != null) {
			for (Map.Entry<byte[], byte[]> entry : result.entrySet()) {
				try {
					Long key = Long.parseLong(redisImp.byte2string(entry.getKey()));
					map.put(key, Long.parseLong(redisImp.byte2string(entry.getValue())));
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			return map;
		}
		return null;
	}

	/**
	 * 保存或删除超时告警设置
	 *
	 * @param tid
	 * @param ruleid
	 * @return
	 */
	public static boolean SaveOrDelAlarmRule(long tid, long ruleid, boolean isSave) {
		boolean result = true;
		RedisImp redisImp = new RedisImp();
		try {
			if (isSave) {
				return mapDao.saveTostaticRedis(RedisConstans.RedisKey.ALARM_CANCEL_REGULAR.name(), String.valueOf(tid),
						redisImp.string2byte(String.valueOf(ruleid)));
			}
			else {
				mapDao.del(RedisConstans.RedisKey.ALARM_CANCEL_REGULAR.name(),new String[]{String.valueOf(tid)});
				return true;
			}
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}
}
