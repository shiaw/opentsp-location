package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import  com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDataEntityDB;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.LocationMongodbService;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import  com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class LocationMongodbServiceImpl extends MongoDaoImp implements LocationMongodbService {
	final static ILocationRedisService locationRedisService = new LocationRedisServiceImpl();

	@SuppressWarnings("unchecked")
	@Override
	public List<GpsDetailedEntityDB> findLocationData(long terminalId, long beginTime, long endTime, String month) {
		List<GpsDetailedEntityDB> result = new ArrayList<GpsDetailedEntityDB>();
		List<String> days = this._format_table_name(beginTime, endTime);
		DBCollection collection = null;
		try {
			collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION, LCMongo.Collection.LC_GPS_DATA_ENTITY + month);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 查询并过滤第一天时间的数据(已过滤开始、结束在同一天的数据)
		DBObject first_day_filter = new BasicDBObject();
		first_day_filter.put("tId", terminalId);
		first_day_filter.put("day", days.get(0));
		GpsDetailedEntityDB gpsDetailedEntity = null;
		DBObject _first_db_result = collection.findOne(first_day_filter);
		if (_first_db_result != null) {
			List<DBObject> datas = (List<DBObject>) _first_db_result.get("dataList");
			if (datas != null) {
				for (DBObject dbObject : datas) {
					long gpsTime = Long.parseLong(String.valueOf(dbObject.get("gpsTime")));
					if (gpsTime >= beginTime && gpsTime < endTime) {
						gpsDetailedEntity = new GpsDetailedEntityDB(dbObject, terminalId);
						result.add(gpsDetailedEntity);
					}
				}
			}
		}
		if (days.size() > 1) {
			// 查询第二到结束前一天的所有数据
			for (int i = 1, length = days.size() - 1; i < length; i++) {
				DBObject _day_filter = new BasicDBObject();
				_day_filter.put("tId", terminalId);
				_day_filter.put("day", days.get(i));
				DBObject _db_result = collection.findOne(_day_filter);
				if (_db_result != null) {
					List<DBObject> datas = (List<DBObject>) _db_result.get("dataList");
					if (datas != null) {
						for (DBObject dbObject : datas) {
							gpsDetailedEntity = new GpsDetailedEntityDB(dbObject, terminalId);
							result.add(gpsDetailedEntity);
						}
					}
				}
			}
			// 查询并过滤最后一天的数据
			DBObject _last_day_filter = new BasicDBObject();
			_last_day_filter.put("tId", terminalId);
			_last_day_filter.put("day", days.get(days.size() - 1));
			DBObject _last_db_result = collection.findOne(_last_day_filter);
			if (_last_db_result != null) {
				List<DBObject> datas = (List<DBObject>) _last_db_result.get("dataList");
				if (datas != null) {
					for (DBObject dbObject : datas) {
						long gpsTime = Long.parseLong(String.valueOf(dbObject.get("gpsTime")));
						if (gpsTime >= beginTime && gpsTime < endTime) {
							gpsDetailedEntity = new GpsDetailedEntityDB(dbObject, terminalId);
							result.add(gpsDetailedEntity);
						}
					}
				}
			}

		}
		MongoManager.close();
		return result;
	}

	public Map<String, List<GpsDetailedEntityDB>> findLocationData(List<Long> terminalIds, long beginTime,
																   long endTime, String month) {
		Map<String, List<GpsDetailedEntityDB>> allDbs = new HashMap<String, List<GpsDetailedEntityDB>>();
		List<String> days = this._format_table_name(beginTime, endTime);
		DBCollection collection = null;
		try {
			collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION, LCMongo.Collection.LC_GPS_DATA_ENTITY + month);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 查询并过滤第一天时间的数据(已过滤开始、结束在同一天的数据)
		BasicDBList tidList = new BasicDBList();
		for (Long tid : terminalIds) {
			tidList.add(tid);
		}
		BasicDBObject allTids = new BasicDBObject("$in", tidList);
		DBObject firstDayFilter = new BasicDBObject();
		firstDayFilter.put("tId", allTids);
		firstDayFilter.put("day", days.get(0));
		GpsDetailedEntityDB gpsDetailedEntity = null;
		// 查询第一天的
		List<DBObject> firstDayDbObjects = collection.find(firstDayFilter).toArray();
		if (null != firstDayDbObjects && firstDayDbObjects.size() > 0) {
			for (DBObject firstDayObject : firstDayDbObjects) {
				List<DBObject> datas = (List<DBObject>) firstDayObject.get("dataList");
				if (datas != null) {
					long dbTid = (long) firstDayObject.get("tId");
					String dayString = String.valueOf(firstDayObject.get("day"));
					List<GpsDetailedEntityDB> entities = new ArrayList<GpsDetailedEntityDB>();
					for (DBObject dbObject : datas) {
						long gpsTime = Long.parseLong(String.valueOf(dbObject.get("gpsTime")));
						if (gpsTime >= beginTime && gpsTime < endTime) {
							gpsDetailedEntity = new GpsDetailedEntityDB(dbObject, dbTid);
							entities.add(gpsDetailedEntity);
						}
					}
					allDbs.put(dbTid + "_" + dayString, entities);
				}
			}
		}

		if (days.size() > 1) {
			// 查询第二到结束前一天的所有数据
			BasicDBList dayList = new BasicDBList();
			for (int i = 1, length = days.size() - 1; i < length; i++) {
				dayList.add(days.get(i));
			}
			BasicDBObject midDays = new BasicDBObject("$in", dayList);
			DBObject _day_filter = new BasicDBObject();
			_day_filter.put("tId", allTids);
			_day_filter.put("day", midDays);
			List<DBObject> midDayDbObjects = collection.find(_day_filter).toArray();
			if (null != midDayDbObjects && midDayDbObjects.size() > 0) {
				for (DBObject dayObject : midDayDbObjects) {
					List<DBObject> datas = (List<DBObject>) dayObject.get("dataList");
					if (datas != null) {
						long dbTid = (long) dayObject.get("tId");
						String dayString = String.valueOf(dayObject.get("day"));
						List<GpsDetailedEntityDB> entities = new ArrayList<GpsDetailedEntityDB>();
						for (DBObject dbObject : datas) {
							gpsDetailedEntity = new GpsDetailedEntityDB(dbObject, dbTid);
							entities.add(gpsDetailedEntity);
						}
						allDbs.put(dbTid + "_" + dayString, entities);
					}
				}
			}
			// 查询并过滤最后一天的数据
			DBObject _last_day_filter = new BasicDBObject();
			_last_day_filter.put("tId", allTids);
			_last_day_filter.put("day", days.get(days.size() - 1));
			List<DBObject> lastDayDbObjects = collection.find(_last_day_filter).toArray();
			if (null != lastDayDbObjects && lastDayDbObjects.size() > 0) {
				for (DBObject dayObject : lastDayDbObjects) {
					List<DBObject> datas = (List<DBObject>) dayObject.get("dataList");
					if (datas != null) {
						long dbTid = (long) dayObject.get("tId");
						String dayString = String.valueOf(dayObject.get("day"));
						List<GpsDetailedEntityDB> entities = new ArrayList<GpsDetailedEntityDB>();
						for (DBObject dbObject : datas) {
							long gpsTime = Long.parseLong(String.valueOf(dbObject.get("gpsTime")));
							if (gpsTime >= beginTime && gpsTime <= endTime) {
								gpsDetailedEntity = new GpsDetailedEntityDB(dbObject, dbTid);
								entities.add(gpsDetailedEntity);
							}
						}
						allDbs.put(dbTid + "_" + dayString, entities);
					}
				}
			}
		}
		MongoManager.close();
		return allDbs;
	}

	/**
	 * 从Redis当中获取当天的数据
	 *
	 * @param terminalId
	 * @return
	 */
	@Override
	public List<GpsDetailedEntityDB> _get_redis_gps(long terminalId) {
		Calendar calendar = DateUtils.current();
		List<LocationData> list = locationRedisService.findNormalLocation(terminalId, calendar);
		List<GpsDetailedEntityDB> _temp_result = new ArrayList<GpsDetailedEntityDB>();
		GpsDetailedEntityDB detailedEntity = null;
		for (LocationData locationData : list) {
			detailedEntity = new GpsDetailedEntityDB();
			detailedEntity.set_id(terminalId);
			detailedEntity.setGpsTime(locationData.getGpsDate());
			detailedEntity.setData(locationData.toByteArray());
			_temp_result.add(detailedEntity);
		}
		return _temp_result;
	}

	/**
	 * 数据过滤
	 *
	 * @param beginTime
	 * @param endTime
	 * @param list
	 * @return
	 */
	public List<GpsDetailedEntityDB> _filter_gps(long beginTime, long endTime, List<GpsDetailedEntityDB> list) {
		List<GpsDetailedEntityDB> result = new ArrayList<GpsDetailedEntityDB>();
		for (GpsDetailedEntityDB detailedEntity : list) {
			if (detailedEntity.getGpsTime() >= beginTime && detailedEntity.getGpsTime() <= endTime) {
				result.add(detailedEntity);
			}
		}
		return result;
	}

	private List<String> _format_table_name(long beginTime, long endTime) {
		List<String> result = new ArrayList<String>();
		Calendar beginDate = DateUtils.calendar(beginTime);
		Calendar endDate = DateUtils.calendar(endTime);

		while (true) {
			result.add(DateUtils.format(beginDate.getTime(), DateFormat.YYYYMMDD));
			beginDate.set(Calendar.DAY_OF_MONTH, beginDate.get(Calendar.DAY_OF_MONTH) + 1);
			if (beginDate.get(Calendar.DAY_OF_MONTH) == endDate.get(Calendar.DAY_OF_MONTH)) {
				result.add(DateUtils.format(beginDate.getTime(), DateFormat.YYYYMMDD));
				break;
			}
			if (beginDate.get(Calendar.DAY_OF_MONTH) > endDate.get(Calendar.DAY_OF_MONTH)) {

				break;
			}
		}
		return result;
	}

	@Override
	public boolean saveGpsData(GpsDataEntityDB dataEntities) {
		List<GpsDetailedEntityDB> datas = dataEntities.getDataList();
		List<DBObject> dbObjects = new ArrayList<DBObject>(datas.size());
		for (GpsDetailedEntityDB gpsDetailedEntity : datas) {
			dbObjects.add(gpsDetailedEntity.toDBObject());
		}
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("tId", dataEntities.gettId());
		dbObject.put("day", dataEntities.getDay());
		dbObject.put("dataList", dbObjects);

		boolean result = super.save(dbObject);
		return result;
	}

	@Override
	public Map<String, List<GpsDetailedEntityDB>> getRedisLocationData(List<Long> terminalIds) {
		Map<String, List<GpsDetailedEntityDB>> map = new HashMap<String, List<GpsDetailedEntityDB>>();
		String day = DateUtils.format(System.currentTimeMillis() / 1000, DateFormat.YYYYMMDD);
		Map<String, List<LocationData>> locations = locationRedisService.findCurrentDayLocations(terminalIds, day);
		Set<String> keySet = locations.keySet();
		for (String key : keySet) {
			List<GpsDetailedEntityDB> _temp_result = new ArrayList<GpsDetailedEntityDB>();
			GpsDetailedEntityDB detailedEntity = null;
			Long tid = Long.parseLong(key.substring(0, key.indexOf("_")));
			List<LocationData> list = locations.get(key);
			for (LocationData locationData : list) {
				detailedEntity = new GpsDetailedEntityDB();
				detailedEntity.set_id(tid);
				detailedEntity.setGpsTime(locationData.getGpsDate());
				detailedEntity.setData(locationData.toByteArray());
				_temp_result.add(detailedEntity);
			}
			map.put(key, _temp_result);
		}
		return map;
	}

	@Override
	public Map<Long, List<LocationData>> findRedisLocationData(List<Long> tids, long begin, long end) {
		String day = DateUtils.format(begin, DateFormat.YYYYMMDD);
		return locationRedisService.findCurrentDayLocations(tids, day, begin, end);
	}
}
