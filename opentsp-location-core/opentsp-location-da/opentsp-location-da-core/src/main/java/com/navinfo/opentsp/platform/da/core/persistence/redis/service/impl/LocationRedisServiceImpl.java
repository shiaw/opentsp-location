package com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisClusters;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisKey;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisListDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.IRedisSetDao;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisListDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.dao.impl.RedisSetDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempCanData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsDataEntry;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.exceptions.JedisException;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class LocationRedisServiceImpl implements ILocationRedisService {
	private IRedisListDao redis_list_dao = new RedisListDaoImpl();
	private IRedisSetDao redis_set_dao = new RedisSetDaoImpl();
	private Logger log = LoggerFactory.getLogger(LocationRedisServiceImpl.class);

	@Override
	public void savaNormalLocation(long terminalId, LocationData locationData) {
		// 本地缓存最新位置数据
		// LatestGpsData.put(terminalId, locationData);
		// Redis缓存终端数据
		// 1、获取终端所在节点编号
		String nodeCode = RedisClusters.getInstance().getNode(terminalId);
		// System.out.println("nodeCode--->" + nodeCode);
		// 放入本地临时缓存
		TempGpsData.add(nodeCode, terminalId, locationData);
		// 判断redis中是否已存在此终端的数据key名称
	}

	// private static Map<Long,Integer> historySeg = new HashMap<>();
	@Override
	public void savaNormalLocation(String nodeCode, List<TempGpsDataEntry> list) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			long startTime = System.currentTimeMillis();
			//log.error("redis位置数据存储,当前时间(ms)：   " + startTime);
			jedis = RedisClusters.getInstance().getJedis();
			Pipeline pipeline = jedis.pipelined();
			StringBuilder segmentDataKey = new StringBuilder("");
			Calendar calendar = Calendar.getInstance();
			int hour = calendar.get(Calendar.HOUR_OF_DAY);
			int minute = calendar.get(Calendar.MINUTE);
			int segment = (hour * 60 + minute) / 5;// [0, 288)
			for (TempGpsDataEntry tempGpsDataEntry : list) {
				String dataKey = RedisKey.getGpsKey(tempGpsDataEntry.getTerminalId(), tempGpsDataEntry.getDay());
				pipeline.zadd(redis_list_dao.string2byte(dataKey), tempGpsDataEntry.getGpsTime(), tempGpsDataEntry
						.getLocationData().toByteArray());
				segmentDataKey.setLength(0);
				segmentDataKey.append(dataKey).append("_").append(segment);
				// Stringkey = segmentDataKey.toStrin g();
				byte[] k = redis_list_dao.string2byte(segmentDataKey.toString());
				pipeline.zadd(k, tempGpsDataEntry.getGpsTime(), tempGpsDataEntry.getLocationData().toByteArray());
				// if (historySeg.get(tempGpsDataEntry.getTerminalId()) == null
				// || historySeg.get(tempGpsDataEntry.getTerminalId()) !=
				// segment) {
				// pipeline.pexpire(key, 900000);
				// historySeg.put(tempGpsDataEntry.getTerminalId(), segment);
				// }
			}
			pipeline.sync();
			long endTime = System.currentTimeMillis();
			//log.error("redis位置数据存储耗时：" + (endTime - startTime) / 1000.0 + " 秒");
		} catch (Exception e) {
			isBroken = true;
			log.error(e.getMessage(), e);
		} finally {
			RedisClusters.getInstance().release(jedis, isBroken);
		}
	}

	/**
	 * 用于生成5分钟一段的位置数据临时缓存（2016年04月20日新增）每5分钟一段，每天从0点0分开始，分为288段，每段均为左闭右开区间
	 *
//	 * @param jedis
//	 * @param list
	 * @author 王景康
	 */
	/*
	 * private void saveNormalLocationSegments(Jedis jedis,
	 * List<TempGpsDataEntry> list) throws Exception { StringBuilder
	 * segmentDataKey = new StringBuilder(""); Calendar calendar =
	 * Calendar.getInstance(); Pipeline pipeline = jedis.pipelined();
	 * List<String> keys = new ArrayList<>(); for (TempGpsDataEntry
	 * tempGpsDataEntry : list) { String dataKey = RedisKey
	 * .getGpsKey(tempGpsDataEntry.getTerminalId(), tempGpsDataEntry.getDay());
	 * calendar.setTimeInMillis(tempGpsDataEntry.getGpsTime() * 1000); int hour
	 * = calendar.get(Calendar.HOUR_OF_DAY); int minute =
	 * calendar.get(Calendar.MINUTE); int segment = (hour * 60 + minute) / 5;//
	 * [0, 288) segmentDataKey.setLength(0);
	 * segmentDataKey.append(dataKey).append("_").append(segment);
	 *
	 * String key = segmentDataKey.toString(); if (!keys.contains(key)) {
	 * keys.add(key); } byte[] k =
	 * redis_list_dao.string2byte(segmentDataKey.toString()); pipeline.zadd(k,
	 * tempGpsDataEntry.getGpsTime(), tempGpsDataEntry
	 * .getLocationData().toByteArray()); } pipeline.syncAndReturnAll(); long
	 * expireTimeMillis = System.currentTimeMillis() + 900000; for (String key :
	 * keys) { jedis.pexpireAt(key, expireTimeMillis); } }
	 */

	@Override
	public List<LocationData> findNormalLocation(long terminalId, Calendar date) {
		return this.findNormalLocation(
				RedisKey.getGpsKey(terminalId, DateUtils.format(date.getTime(), DateFormat.YYYYMMDD)), false);
	}

	@SuppressWarnings("unused")
	private void updateLatestLocation(long terminalId, LocationData locationData) {

	}

	@Override
	public String findGpsDataKey(Calendar calendar) {
		Object object = redis_set_dao.pop(RedisKey.getGpsTitleKey(calendar));
		if (object != null) {
			return object.toString();
		}
		return null;
	}

	@Override
	public boolean removeGpsDataKey(String setName, Object values) {
		long num = redis_set_dao.remove(setName, values);
		if (num > 0) {
			return true;
		}
		return false;
	}

	@Override
	public List<LocationData> findNormalLocation(String dataKey, boolean isDelete) {
		// 分解dataKey,通过一致性hash寻找终端数据所在redis节点
		String[] dataKeys = dataKey.split("_");
		String nodeCode = RedisClusters.getInstance().getNode(Long.parseLong(dataKeys[0]));
		Jedis jedis = null;
		boolean isBroken = false;
		// 改为查询有序集合
		Set<byte[]> list = null;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			list = jedis.zrangeByScore(redis_list_dao.string2byte(dataKey), 0l, Double.MAX_VALUE);
		} catch (Exception e1) {
			isBroken = true;
			log.error(e1.getMessage(), e1);
		} finally {
			RedisClusters.getInstance().release(jedis, isBroken);
		}
		if (isDelete) {
			// 获取数据后,从redis中删除
			while (true) {
				if (this.delGpsData(nodeCode, dataKey)) {
					break;
				} else {
					try {
						TimeUnit.SECONDS.sleep(1);
					} catch (InterruptedException e) {
					}
				}
			}
		}

		List<LocationData> result = new ArrayList<LocationData>();
		try {
			for (byte[] bytes : list) {
				result.add(LocationData.parseFrom(bytes));
			}
			return result;
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public List<LocationData> findAllLocationDatas(String dataKey) {
		// 获取所有集群redis节点编号
		Set<byte[]> locations = new HashSet<byte[]>(1000);
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Set<byte[]> set = jedis.zrangeByScore(redis_list_dao.string2byte(dataKey), 0l, Double.MAX_VALUE);
			locations.addAll(set);
		} catch (Exception e1) {
			isBroken = true;
			log.error(e1.getMessage(), e1);
		} finally {
			RedisClusters.getInstance().release(jedis, isBroken);
		}
		List<LocationData> result = new ArrayList<LocationData>();
		try {
			for (byte[] bytes : locations) {
				result.add(LocationData.parseFrom(bytes));
			}
			return result;
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	private boolean delGpsData(String redisNodeCode, String key) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			jedis.del(key.getBytes("utf-8"));
			return true;
		} catch (UnsupportedEncodingException e) {
			isBroken = true;
			log.error("Redis删除[ " + key + " ]Key失败.");
			return false;
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
	}

	@Override
	public boolean deleteLocationData(String dataKey) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			jedis.del(dataKey.getBytes("utf-8"));
			return true;
		} catch (Exception e) {
			isBroken = true;
			log.error("Redis删除[ " + dataKey + " ]Key失败.", e);
			return false;
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
	}

	// 以下方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存
	@Override
	public Map<Long, List<LocationData>> findCurrentDaySegmentLocations(List<Long> tids, int segment) {
		Map<Long, List<LocationData>> map = new HashMap<Long, List<LocationData>>();
		String currentDayString;
		if (segment != 287) {
			currentDayString = DateUtils.format((System.currentTimeMillis() / 1000), DateFormat.YYYYMMDD);
		} else {
			// 0点减去1分钟，获取昨天的日期格式
			currentDayString = DateUtils.format((System.currentTimeMillis() / 1000 - 60),DateFormat.YYYYMMDD);
		}
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Pipeline pipeline = jedis.pipelined();
			List<String> keys = new ArrayList<>(tids.size());
			StringBuilder key = new StringBuilder("");
			for (Long tid : tids) {
				key.setLength(0);
				key.append(tid).append("_").append(currentDayString).append("_").append(segment);
				String k = key.toString();
				keys.add(k);
				pipeline.zrangeByScore(redis_list_dao.string2byte(k), 0l, Double.MAX_VALUE);
			}
			List<Object> all = pipeline.syncAndReturnAll();
			try {
				for (int i = 0, size = all.size(); i < size; i++) {
					Object o = all.get(i);
					List<LocationData> temp = new ArrayList<LocationData>();
					Set<byte[]> set = (Set<byte[]>) o;
					if (set.size() == 0) {
						continue;
					}
					for (byte[] bytes : set) {
						temp.add(LocationData.parseFrom(bytes));
					}
					Long mapKey = tids.get(i);
					List<LocationData> list = map.get(mapKey);
					if (null == list || list.size() <= 0) {
						map.put(mapKey, temp);
					} else {
						// 合并
						list.addAll(temp);
						map.put(mapKey, list);
					}
				}
			} catch (InvalidProtocolBufferException e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
		return map;
	}

	@Override
	public Map<Long, List<LocationData>> findCurrentDayLocationsBySegment(List<Long> tids, int segment) {
		long begin = System.currentTimeMillis();
		Map<Long, LocationData> gpsMap = new HashMap<Long, LocationData>();
		Map<Long, List<LocationData>> locations = this.findCurrentDaySegmentLocations(tids, segment);
		log.error("获取[" + gpsMap.size() + "]分段终端数据耗时(ms)：" + (System.currentTimeMillis() - begin));
		return locations;
	}

	// 以上方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

	@Override
	public Map<String, List<LocationData>> findCurrentDayLocations(List<Long> tids, String day) {
		Map<String, List<LocationData>> map = new HashMap<String, List<LocationData>>();
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Pipeline pipeline = jedis.pipelined();
			for (Long tid : tids) {
				pipeline.zrangeByScore(redis_list_dao.string2byte(tid + "_" + day), 0l, Double.MAX_VALUE);
			}
			List<Object> all = pipeline.syncAndReturnAll();
			try {
				for (int i = 0, size = all.size(); i < size; i++) {
					Object o = all.get(i);
					List<LocationData> temp = new ArrayList<LocationData>();
					Set<byte[]> set = (Set<byte[]>) o;
					for (byte[] bytes : set) {
						temp.add(LocationData.parseFrom(bytes));
					}
					String mapKey = tids.get(i) + "_" + day;
					List<LocationData> list = map.get(mapKey);
					if (null == list || list.size() <= 0) {
						map.put(mapKey, temp);
					} else {
						// 合并
						list.addAll(temp);
						map.put(mapKey, list);
					}
				}
			} catch (InvalidProtocolBufferException e) {
				log.error(e.getMessage(), e);
			}
		} catch (Exception e) {
			isBroken = true;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
		return map;
	}

	@Override
	public void saveCanData(String nodeCode, List<TempCanData> canDatas) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Pipeline pipeline = jedis.pipelined();
			for (TempCanData tempGpsDataEntry : canDatas) {
				String dataKey = tempGpsDataEntry.getTerminalId() + "_can_" + tempGpsDataEntry.getDay();
				pipeline.zadd(redis_list_dao.string2byte(dataKey), tempGpsDataEntry.getGpsTime(), tempGpsDataEntry
						.getData().toByteArray());
				// pipeline.rpush(redis_list_dao.string2byte(dataKey),tempGpsDataEntry.getLocationData().toByteArray());
			}
			pipeline.syncAndReturnAll();
		} catch (Exception e) {
			isBroken = true;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
	}

	@Override
	public List<LCCANBUSDataReport.CANBUSDataReport> findCANDatas(String dataKey) {
		// 获取所有集群redis节点编号
		Set<byte[]> canSet = new HashSet<byte[]>(1000);
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Set<byte[]> set = jedis.zrangeByScore(redis_list_dao.string2byte(dataKey), 0l, Double.MAX_VALUE);
			canSet.addAll(set);
		} catch (Exception e) {
			isBroken = true;
			log.error(e.getMessage(), e);
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
		List<CANBUSDataReport> result = new ArrayList<CANBUSDataReport>();
		try {
			for (byte[] bytes : canSet) {
				CANBUSDataReport report = CANBUSDataReport.parseFrom(bytes);
				result.add(report);
			}
			return result;
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void delGpsDataSegementThisDay(String mapName) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Set<String> keys = jedis.keys(mapName);
			Pipeline pipelined = jedis.pipelined();
			for (String key : keys) {
				// log.error("删除key:{}", key);
				pipelined.del(key);
			}
			pipelined.sync();
		} catch (JedisException e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			RedisClusters.getInstance().release(jedis, isBroken);
		}
	}

	@Override
	public void delGpsDataSegementByKeyArr(String[] keyArr) {
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Pipeline pipelined = jedis.pipelined();
			pipelined.del(keyArr);
			pipelined.sync();
		} catch (JedisException e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			RedisClusters.getInstance().release(jedis, isBroken);
		}
	}

	@Override
	public Map<Long, List<LocationData>> findCurrentDayLocations(List<Long> tids, String day, long start, long end) {
		Map<Long, List<LocationData>> map = new HashMap<Long, List<LocationData>>();
		Jedis jedis = null;
		boolean isBroken = false;
		try {
			jedis = RedisClusters.getInstance().getJedis();
			Pipeline pipeline = jedis.pipelined();
			for (Long tid : tids) {
				pipeline.zrangeByScore(redis_list_dao.string2byte(tid + "_" + day), start, end);
			}
			List<Object> all = pipeline.syncAndReturnAll();
			try {
				for (int i = 0, size = all.size(); i < size; i++) {
					Object o = all.get(i);
					List<LocationData> temp = new ArrayList<LocationData>();
					@SuppressWarnings("unchecked")
					Set<byte[]> set = (Set<byte[]>) o;
					for (byte[] bytes : set) {
						temp.add(LocationData.parseFrom(bytes));
					}
					if (temp.isEmpty()) {
						continue;
					}
					map.put(tids.get(i), temp);
				}
			} catch (InvalidProtocolBufferException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			isBroken = true;
			e.printStackTrace();
		} finally {
			if (null != jedis) {
				RedisClusters.getInstance().release(jedis, isBroken);
			}
		}
		return map;
	}
}
