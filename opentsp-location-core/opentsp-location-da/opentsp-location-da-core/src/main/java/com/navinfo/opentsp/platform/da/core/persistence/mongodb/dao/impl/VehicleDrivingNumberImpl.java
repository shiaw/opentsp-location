package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import com.mongodb.*;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.*;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAVehicleNumInAreaAlarm;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.*;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.VehicleDrivingNumber;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesDetail;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesRecord;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesTreeNode;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCCrossGridCounts.CrossGridCounts;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.LCVehiclePassTimesRecords.VehiclePassTimesRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCCrossGridRecord.CrossGridRecord;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCGrandSonAreaTimes.GrandSonAreaTimes;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCSonAreaTimes.SonAreaTimes;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCVehiclePassTimes.VehiclePassTimes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.util.*;
import java.util.Map.Entry;

public class VehicleDrivingNumberImpl extends MongoDaoImp implements VehicleDrivingNumber {
	BaseMongoDaoImpl mongoService = new BaseMongoDaoImpl();
	MongoDao mongoDao = new MongoDaoImp();
	private Logger logger = LoggerFactory.getLogger(VehicleDrivingNumberImpl.class);
	private int eachBatch = 100;

	private static String getNextDay(String day) {
		Date date = DateUtils.parse(day, DateUtils.DateFormat.YYYYMMDD);
		Calendar calendar = DateUtils.calendar(date);
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
		String result = DateUtils.format(calendar.getTime(), DateUtils.DateFormat.YYYYMMDD);
		return result;
	}

	/**
	 * * 判断给定日期是否为月末的一天 *
	 *
	 * @param date
	 * @return true搜索:是|false:不是
	 */
	private static boolean isLastDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1));
		if (calendar.get(Calendar.DAY_OF_MONTH) == 1) {
			return true;
		}
		return false;
	}

	public VehiclePassTimesRecords getVehiclePassTimesRecords(int districtCode, long startDate, long endDate) {
		VehiclePassTimesRecords.Builder builder = VehiclePassTimesRecords.newBuilder();
		if (endDate > startDate) {
			Map<String, List<String>> listDates = splitWithTenDays(startDate, endDate);
			List<VehiclePassTimes> entities = new LinkedList<VehiclePassTimes>();
			for (Entry<String, List<String>> entry : listDates.entrySet()) {
				String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInGrid + "_"
						+ entry.getKey();
				BasicDBObject query = new BasicDBObject();
				BasicDBList monthList = new BasicDBList();
				List<String> ms = entry.getValue();
				for (String s : ms) {
					monthList.add(Integer.parseInt(s));
				}
				// 确认in关键字是否有长度限制，确认in关键字的性能如何，未来是否有替代优化方案。——hk
				BasicDBObject inMonth = new BasicDBObject("$in", monthList);
				query.put("month", inMonth);
				query.put("district", districtCode);
				DBCollection collection;
				try {
					collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
					List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, null, -1);
					List<VehiclePassTimes> el = convert2VehiclePassTimes(dbObjects);
					if(el != null) {
						entities.addAll(el);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			builder.addAllDataList(entities);
			builder.setStatusCode(PlatformResponseResult.success_VALUE);
			builder.setTotalRecords(entities.size());
		} else {
			builder.setStatusCode(PlatformResponseResult.failure_VALUE);
			builder.setTotalRecords(0);
		}
		return builder.build();
	}

	@Override
	public ResVehiclePassTimesRecordsQuery queryVehiclePassTimesRecords(int districtCode, long startDate,
			long endDate) {
		ResVehiclePassTimesRecordsQuery records = new ResVehiclePassTimesRecordsQuery();
		Map<String, List<String>> listDates = splitWithTenDays(startDate, endDate);
		for (Entry<String, List<String>> entry : listDates.entrySet()) {
			String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInGrid + "_"
					+ entry.getKey();
			BasicDBObject query = new BasicDBObject();
			BasicDBList monthList = new BasicDBList();
			List<String> ms = entry.getValue();
			for (String s : ms) {
				monthList.add(Integer.parseInt(s));
			}
			// 确认in关键字是否有长度限制，确认in关键字的性能如何，未来是否有替代优化方案。——hk
			BasicDBObject inMonth = new BasicDBObject("$in", monthList);
			query.put("month", inMonth);
			query.put("district", districtCode);
			List<DBObject> dbObjects = mongoService.queryByCondition(collectionName, query);
			List<VehiclePassTimesEntity> entities = convert(dbObjects);

			records.addVehiclePassTimes(entities);
		}
		records.setStatusCode(PlatformResponseResult.success_VALUE);
		records.setTotalRecords(records.getDataList().size());
		return records;
	}

	private List<VehiclePassTimes> convert2VehiclePassTimes(List<DBObject> dbObjects) {
		List<VehiclePassTimes> entities = new ArrayList<VehiclePassTimes>();
		List<VehicleDrivingNumberInGridDAEntity> daEntities = new ArrayList<VehicleDrivingNumberInGridDAEntity>();
		for (DBObject dbObject : dbObjects) {
			VehicleDrivingNumberInGridDAEntity daEntity = new VehicleDrivingNumberInGridDAEntity();
			daEntity.dbObjectToBean(dbObject);
			daEntities.add(daEntity);
		}
		// daEntities中tile相同的需要合并
		List<VehicleDrivingNumberInGridDAEntity> da_filter = new ArrayList<VehicleDrivingNumberInGridDAEntity>();
		for (VehicleDrivingNumberInGridDAEntity v1 : daEntities) {
			boolean has = false;
			for (VehicleDrivingNumberInGridDAEntity v2 : da_filter) {
				if (v1.get_id() == v2.get_id()) {
					has = true;
					VehicleDrivingNumberInGridDAEntity merge = mergeGrid(v1, v2);
					v2.setTile(merge.getTile());
					v2.setTimes(merge.getTimes());
					v2.setSonAreaTimes(merge.getSonAreaTimes());
				}
			}
			if (!has) {
				da_filter.add(v1);
			}
		}
		for (VehicleDrivingNumberInGridDAEntity daEntity : da_filter) {
			VehiclePassTimes.Builder entityBuilder = VehiclePassTimes.newBuilder();
			entityBuilder.setId(daEntity.getTile());
			entityBuilder.setTimes(daEntity.getTimes());
			List<SonAreaTimes> sons = new ArrayList<SonAreaTimes>();
			List<SonAreaTimesDAEntity> sonAreaTimes = daEntity.getSonAreaTimes();
			if (null != sonAreaTimes && sonAreaTimes.size() > 0) {
				for (SonAreaTimesDAEntity sonDA : sonAreaTimes) {
					SonAreaTimes.Builder sonBuilder = SonAreaTimes.newBuilder();
					sonBuilder.setId(sonDA.getTile());
					sonBuilder.setTimes(sonDA.getTimes());
					List<GrandSonAreaTimes> grands = new ArrayList<GrandSonAreaTimes>();
					List<GrandSonAreaTimesDAEntity> grandDas = sonDA.getGrandSonAreaTimes();
					if (null != grandDas && grandDas.size() > 0) {
						for (GrandSonAreaTimesDAEntity grandDa : grandDas) {
							GrandSonAreaTimes.Builder grandBuilder = GrandSonAreaTimes.newBuilder();
							grandBuilder.setId(grandDa.getTile());
							grandBuilder.setTimes(grandDa.getTimes());
							grands.add(grandBuilder.build());
						}
					}
					sonBuilder.addAllGrandsonAreaTimes(grands);
					sons.add(sonBuilder.build());
				}
			}
			entityBuilder.addAllSonAreaTimes(sons);
			entities.add(entityBuilder.build());
		}
		return entities;
	}

	private List<VehiclePassTimesEntity> convert(List<DBObject> dbObjects) {
		List<VehiclePassTimesEntity> entities = new ArrayList<VehiclePassTimesEntity>();
		List<VehicleDrivingNumberInGridDAEntity> daEntities = new ArrayList<VehicleDrivingNumberInGridDAEntity>();
		for (DBObject dbObject : dbObjects) {
			VehicleDrivingNumberInGridDAEntity daEntity = new VehicleDrivingNumberInGridDAEntity();
			daEntity.dbObjectToBean(dbObject);
			daEntities.add(daEntity);
		}
		// daEntities中tile相同的需要合并
		List<VehicleDrivingNumberInGridDAEntity> da_filter = new ArrayList<VehicleDrivingNumberInGridDAEntity>();
		for (VehicleDrivingNumberInGridDAEntity v1 : daEntities) {
			boolean has = false;
			for (VehicleDrivingNumberInGridDAEntity v2 : da_filter) {
				if (v1.get_id() == v2.get_id()) {
					has = true;
					VehicleDrivingNumberInGridDAEntity merge = mergeGrid(v1, v2);
					v2.setTile(merge.getTile());
					v2.setTimes(merge.getTimes());
					v2.setSonAreaTimes(merge.getSonAreaTimes());
				}
			}
			if (!has) {
				da_filter.add(v1);
			}
		}
		for (VehicleDrivingNumberInGridDAEntity daEntity : da_filter) {
			VehiclePassTimesEntity entity = new VehiclePassTimesEntity();
			entity.set_id(daEntity.getTile());
			entity.setTimes(daEntity.getTimes());
			List<SonAreaTimesEntity> sons = new ArrayList<SonAreaTimesEntity>();
			List<SonAreaTimesDAEntity> sonAreaTimes = daEntity.getSonAreaTimes();
			if (null != sonAreaTimes && sonAreaTimes.size() > 0) {
				for (SonAreaTimesDAEntity sonDA : sonAreaTimes) {
					SonAreaTimesEntity son = new SonAreaTimesEntity();
					son.set_id(sonDA.getTile());
					son.setTimes(sonDA.getTimes());
					List<GrandSonAreaTimesEntity> grands = new ArrayList<GrandSonAreaTimesEntity>();
					List<GrandSonAreaTimesDAEntity> grandDas = sonDA.getGrandSonAreaTimes();
					if (null != grandDas && grandDas.size() > 0) {
						for (GrandSonAreaTimesDAEntity grandDa : grandDas) {
							GrandSonAreaTimesEntity grand = new GrandSonAreaTimesEntity();
							grand.set_id(grandDa.getTile());
							grand.setTimes(grandDa.getTimes());
							grands.add(grand);
						}
					}
					son.setGrandSonAreaTimes(grands);
					sons.add(son);
				}
			}
			entity.setSonAreaTimes(sons);
			entities.add(entity);
		}

		return entities;
	}

	private Map<Long, Integer> convertToTerminalTimes(List<DBObject> dbObjects, List<Long> terminalIds) {
		Map<Long, Integer> tidTimesMap = new HashMap<Long, Integer>();
		Map<Long,Integer> timeMap = new HashMap<Long, Integer>();
		List<VehicleDrivingNumberInGridDAEntity> daEntities = new ArrayList<VehicleDrivingNumberInGridDAEntity>();
		for (DBObject dbObject : dbObjects) {
			VehicleDrivingNumberInGridDAEntity daEntity = new VehicleDrivingNumberInGridDAEntity();
			daEntity.dbObjectToBean(dbObject);
			daEntities.add(daEntity);
		}
		for (VehicleDrivingNumberInGridDAEntity daEntity : daEntities) {
			List<SonAreaTimesDAEntity> sonAreaTimes = daEntity.getSonAreaTimes();
			if (null != sonAreaTimes && sonAreaTimes.size() > 0) {
				for (SonAreaTimesDAEntity sonDA : sonAreaTimes) {
					List<GrandSonAreaTimesDAEntity> grandDas = sonDA.getGrandSonAreaTimes();
					if (null != grandDas && grandDas.size() > 0) {
						for (GrandSonAreaTimesDAEntity grandDa : grandDas) {
							Set<AreaTerminalTimesDAEntity> tids = grandDa.getTids();
							if (null != tids && tids.size() > 0) {
								for (long tid : terminalIds) {
									int times = 0;
									for (AreaTerminalTimesDAEntity att : tids) {
										if (tid == att.getTid()) {
											times++;
										}
									}
									//同一个document车次取最大值，不同document车次进行累加
									if (0 != times) {
										Integer oldTimes = tidTimesMap.get(tid);
										if (null == oldTimes) {
											tidTimesMap.put(tid, times);
										} else if (oldTimes < times) {
											tidTimesMap.put(tid, times);
										}
									}
								}
							}
						}
					}
				}
				//不同document车次进行累加
				if(tidTimesMap!=null){
					for(Map.Entry<Long, Integer> map : tidTimesMap.entrySet()){
						if(timeMap.containsKey(map.getKey())){
							timeMap.put(map.getKey(), timeMap.get(map.getKey())+map.getValue());
						}else{
							timeMap.put(map.getKey(), map.getValue());
						}
					}
					tidTimesMap.clear();
				}

			}
		}
		return timeMap;
	}

	@Override
	public ResVehiclePassInAreaRecordsQuery queryVehiclePassInAreaRecords(List<Integer> districtCodes, int type,
			long startDate, long endDate) {
		ResVehiclePassInAreaRecordsQuery records = new ResVehiclePassInAreaRecordsQuery();
		Map<String, List<String>> listDates = splitWithDays(startDate, endDate);
		for (Entry<String, List<String>> entry : listDates.entrySet()) {
			String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInArea + "_"
					+ entry.getKey();
			BasicDBObject query = new BasicDBObject();
			BasicDBList dayList = new BasicDBList();
			List<String> ms = entry.getValue();
			for (String s : ms) {
				dayList.add(Integer.parseInt(s));
			}
			BasicDBObject inDays = new BasicDBObject("$in", dayList);
			query.put("day", inDays);
			BasicDBList districtList = new BasicDBList();
			for (Integer s : districtCodes) {
				districtList.add(s);
			}
			BasicDBObject inDis = new BasicDBObject("$in", districtList);
			query.put("district", inDis);
			query.put("type", type);
			try {
				DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
				List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, null, -1);
				List<VehiclePassInAreaEntity> entities = convertPassInArea(dbObjects);
				records.addVehiclePassInArea(entities);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}
		Map<Long, VehiclePassInAreaEntity> map = new HashMap<Long, VehiclePassInAreaEntity>();
		for (VehiclePassInAreaEntity entity : records.getDataList()) {
			long district = entity.get_id();
			if (map.get(district) != null) {
				int times = map.get(district).getTimes() + entity.getTimes();
				entity.setTimes(times);
				if (entity.getDataList().size() >= 0) {
					List<VehiclePassDetail> dataList = mergePassInArea1(entity.getDataList(),
							map.get(district).getDataList());
					entity.setDataList(dataList);
				}
				map.put(district, entity);
			} else {
				map.put(district, entity);
			}
		}
		records.getDataList().clear();
		records.getDataList().addAll(map.values());
		records.setStatusCode(PlatformResponseResult.success_VALUE);
		records.setTotalRecords(records.getDataList().size());
		map.clear();
		return records;
	}

	private List<VehiclePassDetail> mergePassInArea1(List<VehiclePassDetail> alarm1, List<VehiclePassDetail> alarm2) {
		Map<Long, VehiclePassDetail> map = new HashMap<Long, VehiclePassDetail>();
		alarm1.addAll(alarm2);
		for (VehiclePassDetail a1 : alarm1) {
			if (map.get(a1.getTid()) != null) {
				int times = map.get(a1.getTid()).getTime() + a1.getTime();
				if (a1.isHasFaultCode() || map.get(a1.getTid()).isHasFaultCode()) {
					a1.setHasFaultCode(true);
				}
				a1.setTime(times);
				map.put(a1.getTid(), a1);
			} else {
				map.put(a1.getTid(), a1);
			}
		}
		alarm1.clear();
		alarm1.addAll(map.values());
		return alarm1;
	}

	private List<DAVehicleNumInAreaAlarm> mergePassInArea(List<DAVehicleNumInAreaAlarm> alarm1,
			List<DAVehicleNumInAreaAlarm> alarm2) {
		Map<Long, DAVehicleNumInAreaAlarm> map = new HashMap<Long, DAVehicleNumInAreaAlarm>();
		List<DAVehicleNumInAreaAlarm> allAlarms = new LinkedList<DAVehicleNumInAreaAlarm>();
		if (alarm1 != null) {
			allAlarms.addAll(alarm1);
		}
		if (alarm2 != null) {
			allAlarms.addAll(alarm2);
		}
		for (DAVehicleNumInAreaAlarm alarm : allAlarms) {
			long tId = alarm.getTerminalId();
			if (map.containsKey(tId)) {
				DAVehicleNumInAreaAlarm current = map.get(tId);
				DAVehicleNumInAreaAlarm result = new DAVehicleNumInAreaAlarm();
				result.setTerminalId(tId);
				int times = current.getTimes() + alarm.getTimes();
				result.setTimes(times);

				if (alarm.getContinuousTime() > current.getContinuousTime()) {
					result.setContinuousTime(alarm.getContinuousTime());
				} else {
					result.setContinuousTime(current.getContinuousTime());
				}
				if (alarm.getMileage() > current.getMileage()) {
					result.setMileage(alarm.getMileage());
				} else {
					result.setMileage(current.getMileage());
				}
				if (alarm.isFault() || current.isFault()) {
					result.setFault(true);
				} else {
					result.setFault(false);
				}
				map.put(tId, result);
			} else {
				map.put(tId, alarm);
			}
		}
		List<DAVehicleNumInAreaAlarm> result = new ArrayList<DAVehicleNumInAreaAlarm>();
		result.addAll(map.values());
		map.clear();
		return result;
	}

	private List<VehiclePassInAreaEntity> convertPassInArea(List<DBObject> dbObjects) {
		List<VehiclePassInAreaEntity> aEntity = new ArrayList<VehiclePassInAreaEntity>();
		List<VehiclePassInAreaDAEntity> daEntities = new ArrayList<VehiclePassInAreaDAEntity>();
		for (DBObject dbObject : dbObjects) {
			VehiclePassInAreaDAEntity daEntity = new VehiclePassInAreaDAEntity();
			daEntity.dbObjectToBean(dbObject);
			daEntities.add(daEntity);
		}
		List<VehiclePassInAreaDAEntity> entities = new ArrayList<VehiclePassInAreaDAEntity>();
		for (VehiclePassInAreaDAEntity v1 : daEntities) {
			boolean has = false;
			for (VehiclePassInAreaDAEntity v2 : entities) {
				if (v1.getDistrict() == v2.getDistrict()) {
					has = true;
					v2.setTimes(v1.getTimes() + v2.getTimes());
					List<DAVehicleNumInAreaAlarm> alarmList = mergePassInArea(v1.getDataList(), v2.getDataList());
					v2.setDataList(alarmList);
				}
			}
			if (!has) {
				entities.add(v1);
			}
		}
		for (VehiclePassInAreaDAEntity da : entities) {
			VehiclePassInAreaEntity entity = new VehiclePassInAreaEntity();
			entity.set_id(da.getDistrict());
			entity.setTimes(da.getTimes());
			List<VehiclePassDetail> list = new ArrayList<VehiclePassDetail>();
			if (da.getDataList() != null && da.getDataList().size() > 0) {
				for (DAVehicleNumInAreaAlarm aAlarm : da.getDataList()) {
					VehiclePassDetail detail = new VehiclePassDetail();
					detail.setTid(aAlarm.getTerminalId());
					detail.setTime(aAlarm.getTimes());
					detail.setRunTime(aAlarm.getContinuousTime());
					detail.setHasFaultCode(aAlarm.isFault());
					detail.setMileage(aAlarm.getMileage());
					list.add(detail);
				}
			}
			entity.setDataList(list);
			aEntity.add(entity);
		}
		return aEntity;
	}

	/**
	 * 拆分查询时间段
	 *
	 * @param beginTime
	 * @param endTime
	 * @return Map<时间月份（格式yyMM）, List<YYYYMMZZ(01,02,03)分别代表上中下三旬>>
	 * @throws java.text.ParseException
	 */
	private Map<String, List<String>> splitWithTenDays(long beginTime, long endTime) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		String beginIndexString = getTenDayIndex(beginTime);
		String endIndexString = getTenDayIndex(endTime);
		int endIndex = Integer.parseInt(endIndexString);
		List<String> middleMonth = getMiddleMonth(beginTime, endTime);
		int currentIndex = 0;
		int dayEnd = 0;
		for (int i = 0; i < middleMonth.size(); i++) {
			String mon = middleMonth.get(i);
			List<String> tenDays = new ArrayList<String>();
			if (0 == i) {
				currentIndex = Integer.parseInt(beginIndexString);
				dayEnd = currentIndex % 10;
			} else {
				currentIndex = Integer.parseInt(mon + "01");
				dayEnd = 1;
			}
			while (dayEnd <= 3) {
				tenDays.add(currentIndex + "");
				if (endIndex == currentIndex)
					break;
				currentIndex++;
				dayEnd = currentIndex % 10;
			}
			mon = mon.substring(2);
			result.put(mon, tenDays);
		}
		return result;
	}

	/**
	 * 按天拆分 按月拼集合
	 *
	 * @param beginTime
	 * @param endTime
	 * @return Map<YYMM,List<YYYYMMDD>
	 */
	private Map<String, List<String>> splitWithDays(long beginTime, long endTime) {
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		String currentDayString = DateUtils.format(beginTime, DateUtils.DateFormat.YYYYMMDD);
		String endDayString = DateUtils.format(endTime, DateUtils.DateFormat.YYYYMMDD);
		List<String> middleMonth = getMiddleMonth(beginTime, endTime);
		for (int i = 0; i < middleMonth.size(); i++) {
			String mon = middleMonth.get(i);
			List<String> days = new ArrayList<String>();
			if (0 != i) {
				currentDayString = mon + "01";
			}
			while (true) {
				days.add(currentDayString);
				if (currentDayString.equals(endDayString)) {
					break;
				} else if (isLastDayOfMonth(DateUtils.parse(currentDayString, DateUtils.DateFormat.YYYYMMDD))) {
					break;
				}
				currentDayString = getNextDay(currentDayString);
			}
			mon = mon.substring(2);
			result.put(mon, days);
		}
		return result;
	}

	/**
	 * 获得当前时间是上中下旬 例如：yyyyMM02(01,02,03)分别代表上中下三旬
	 *
	 * @param time
	 * @return 1=上旬 2=中旬 3=下旬
	 */
	private String getTenDayIndex(long time) {
		Calendar calendar = DateUtils.calendar(time);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		String index = "";
		if (10 >= dayOfMonth) {
			index = "01";
		} else if (10 < dayOfMonth && 20 >= dayOfMonth) {
			index = "02";
		} else {
			index = "03";
		}
		String yyyyMM = DateUtils.format(time, DateUtils.DateFormat.YYYYMM);
		String result = yyyyMM + index;
		return result;
	}

	/**
	 * 返回中间的月份，至少返回一个，格式为201509,201510,201511
	 *
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<String> getMiddleMonth(long beginTime, long endTime) {
		List<String> result = new ArrayList<String>();
		String endMonth = DateUtils.format(endTime, DateUtils.DateFormat.YYYYMM);
		int endMon = Integer.parseInt(endMonth);
		int currentMon = 0;
		long currentTime = beginTime;
		do {
			String beginMonth = DateUtils.format(currentTime, DateUtils.DateFormat.YYYYMM);
			result.add(beginMonth);
			Calendar c = DateUtils.calendar(currentTime);
			c.set(Calendar.MONTH, c.get(Calendar.MONTH) + 1);
			currentTime = c.getTimeInMillis() / 1000;
			String currentMonth = DateUtils.format(currentTime, DateUtils.DateFormat.YYYYMM);
			currentMon = Integer.parseInt(currentMonth);
		} while (currentMon <= endMon);
		return result;
	}

	/**
	 * 此方法后续还需要进一步整理——hk
	 */
	@Override
	public PlatformResponseResult saveVehiclePassGridTimes(List<VehiclePassTimesTreeNode> grids) {
		try {
			Map<Long, VehicleDrivingNumberInGridDAEntity> gridsMap = convertGridRmiToDa(grids);
			if (gridsMap == null || gridsMap.size() == 0) {
				return PlatformResponseResult.success;
			}
			logger.error(">>>dsa区域车次统计数据存储结果数：  " + grids.size());
			logger.info("all grids tileId size :" + gridsMap.size());
			int yyMM = grids.get(0).getMonth();
			String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInGrid + "_"
					+ (yyMM + "").substring(2, 6);
			DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
			int index = 0;
			List<List<Long>> times = new ArrayList<>();
			List<Long> batchIds = null;
			for (Entry<Long, VehicleDrivingNumberInGridDAEntity> entity : gridsMap.entrySet()) {
				if (index % eachBatch == 0) {
					batchIds = new ArrayList<>();
					times.add(batchIds);
				}
				batchIds.add(entity.getKey());
				index++;
			}
			// 每次批量处理100个瓦片数据的存储和合并
			for (List<Long> batch : times) {
				Map<Long, VehicleDrivingNumberInGridDAEntity> batchMap = new HashMap<>();
				for (Long id : batch) {
					batchMap.put(id, gridsMap.get(id));
				}
				BasicDBObject query = new BasicDBObject();
				BasicDBList dayList = new BasicDBList();
				dayList.addAll(batch);
				BasicDBObject inTileIds = new BasicDBObject("$in", dayList);
				query.put("tile", inTileIds);
				query.put("month", yyMM);
				DBCursor find = collection.find(query);
				List<DBObject> findByField = find.toArray();
				List<VehicleDrivingNumberInGridDAEntity> list = new ArrayList<>();
				for (DBObject entity : findByField) {
					VehicleDrivingNumberInGridDAEntity e = new VehicleDrivingNumberInGridDAEntity();
					e.dbObjectToBean(entity);
					if (batchMap.get(e.getTile()) != null) {
						e = mergeGrid(e, batchMap.get(e.getTile()));
						batchMap.remove(e.getTile());
						mongoDao.delObjectById(collection, entity.get("_id").toString());
						list.add(e);
					}
				}
				List<DBObject> dbObjects = new ArrayList<>();
				for (VehicleDrivingNumberInGridDAEntity entity : list) {
					DBObject dbObject = entity.toDBObject();
					dbObjects.add(dbObject);
				}
				for (Entry<Long, VehicleDrivingNumberInGridDAEntity> entity : batchMap.entrySet()) {
					dbObjects.add(entity.getValue().toDBObject());
				}
				logger.info("each save grids tileId size :" + dbObjects.size());
				collection.insert(dbObjects);
			}
			MongoManager.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return PlatformResponseResult.failure;
		} finally {
			MongoManager.close();
		}
		return PlatformResponseResult.success;
	}

	/**
	 * 从List<DBObject>中找到相同month（旬）和相同顶级瓦片ID的数据，没有找到返回null
	 *
	 * @param e1
	 * @param e2
	 * @return
	 */
	// private VehicleDrivingNumberInGridDAEntity filterCurrentTenDays(long
	// tileId,int month,List<DBObject> objects){
	// for(DBObject object : objects){
	// VehicleDrivingNumberInGridDAEntity e = new
	// VehicleDrivingNumberInGridDAEntity();
	// e.dbObjectToBean(object);
	// if(month == e.getMonth() && tileId == e.getTile()){
	// return e;
	// }
	// }
	// return null;
	// }
	private VehicleDrivingNumberInGridDAEntity mergeGrid(VehicleDrivingNumberInGridDAEntity e1,
			VehicleDrivingNumberInGridDAEntity e2) {
		if (e1.getTile() == e2.getTile()) {
			e1.setTimes(e1.getTimes() + e2.getTimes());
			List<SonAreaTimesDAEntity> e1son = e1.getSonAreaTimes();
			List<SonAreaTimesDAEntity> e2son = e2.getSonAreaTimes();
			e1.setSonAreaTimes(mergeSonGrid(e1son, e2son));
		} else {
			logger.error("传入的两个对象瓦片ID不相同" + e1.toString() + "-----" + e2.toString());
		}
		return e1;
	}

	private List<SonAreaTimesDAEntity> mergeSonGrid(List<SonAreaTimesDAEntity> son1, List<SonAreaTimesDAEntity> son2) {
		for (SonAreaTimesDAEntity s1 : son1) {
			for (SonAreaTimesDAEntity s2 : son2) {
				if (s1.getTile() == s2.getTile()) {
					s1.setTimes(s1.getTimes() + s2.getTimes());
					s1.setGrandSonAreaTimes(mergeGrandGrid(s1.getGrandSonAreaTimes(), s2.getGrandSonAreaTimes()));
				}
			}
		}
		return son1;
	}

	private List<GrandSonAreaTimesDAEntity> mergeGrandGrid(List<GrandSonAreaTimesDAEntity> son1,
			List<GrandSonAreaTimesDAEntity> son2) {
		for (GrandSonAreaTimesDAEntity s1 : son1) {
			for (GrandSonAreaTimesDAEntity s2 : son2) {
				if (s1.getTile() == s2.getTile()) {
					s1.setTimes(s1.getTimes() + s2.getTimes());
					s1.setTids(mergeTerminalTimes(s1.getTids(), s2.getTids()));
				}
			}
		}
		return son1;
	}

	private Set<AreaTerminalTimesDAEntity> mergeTerminalTimes(Set<AreaTerminalTimesDAEntity> m1,
			Set<AreaTerminalTimesDAEntity> m2) {
		m1.addAll(m2);
		return m1;
	}

	/**
	 * rmi对象转换为DA对象
	 *
	 * @param grids
	 * @return
	 */
	private Map<Long, VehicleDrivingNumberInGridDAEntity> convertGridRmiToDa(List<VehiclePassTimesTreeNode> grids) {
		Map<Long, VehicleDrivingNumberInGridDAEntity> daEntities = new HashMap<Long, VehicleDrivingNumberInGridDAEntity>();
		if (null != grids && grids.size() > 0) {
			for (VehiclePassTimesTreeNode node : grids) {
				VehicleDrivingNumberInGridDAEntity daEntity = new VehicleDrivingNumberInGridDAEntity();
				daEntity.setTile(node.getTileId());
				daEntity.setTimes(node.getTimes());
				daEntity.setDistrict(node.getDistrict());
				daEntity.setMonth(node.getMonth());
				List<SonAreaTimesDAEntity> sonAreaTimes = new ArrayList<SonAreaTimesDAEntity>();
				List<VehiclePassTimesTreeNode> children = node.getChildren();
				if (null != children && children.size() > 0) {
					for (VehiclePassTimesTreeNode sNode : children) {
						SonAreaTimesDAEntity sonDaEntity = new SonAreaTimesDAEntity();
						sonDaEntity.setTile(sNode.getTileId());
						sonDaEntity.setTimes(sNode.getTimes());
						List<GrandSonAreaTimesDAEntity> grandSonAreaTimes = new ArrayList<GrandSonAreaTimesDAEntity>();
						List<VehiclePassTimesTreeNode> grands = sNode.getChildren();
						if (null != grands && grands.size() > 0) {
							for (VehiclePassTimesTreeNode gNode : grands) {
								GrandSonAreaTimesDAEntity grandDaEntity = new GrandSonAreaTimesDAEntity();
								grandDaEntity.setTile(gNode.getTileId());
								grandDaEntity.setTimes(gNode.getTimes());
								Map<Long, Integer> terminals = gNode.getTerminals();
								if (null != terminals && terminals.size() > 0) {
									Set<Long> tidSet = terminals.keySet();
									Calendar current = DateUtils.current();
									current.set(Calendar.DAY_OF_YEAR, current.get(Calendar.DAY_OF_YEAR) - 1);
									int yesterday = current.get(Calendar.DAY_OF_MONTH);
									Set<AreaTerminalTimesDAEntity> terminalDays = new HashSet<AreaTerminalTimesDAEntity>();
									for (long tid : tidSet) {
										AreaTerminalTimesDAEntity e = new AreaTerminalTimesDAEntity();
										e.setTid(tid);
										e.setDay(yesterday);
										terminalDays.add(e);
									}
									grandDaEntity.setTids(terminalDays);
								}
								grandSonAreaTimes.add(grandDaEntity);
							}
						}
						sonDaEntity.setGrandSonAreaTimes(grandSonAreaTimes);
						sonAreaTimes.add(sonDaEntity);
					}
				}
				daEntity.setSonAreaTimes(sonAreaTimes);
				daEntities.put(daEntity.getTile(), daEntity);
			}
		}
		return daEntities;
	}

	@Override
	public PlatformResponseResult saveVehiclePassAreaTimes(List<VehiclePassTimesRecord> areas) {
		try {
			Map<Integer, VehiclePassInAreaDAEntity> areaMap = convertAreaRmiToDa(areas);
			if (areaMap == null || areaMap.size() == 0) {
				return PlatformResponseResult.success;
			}
			logger.error(">>>dsa区域车次统计数据存储结果数： " + areas.size());
			logger.info("all areas data size :" + areaMap.size());
			int day = areas.get(0).getDay();
			String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInArea + "_"
					+ (day + "").substring(2, 6);
			DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
			BasicDBObject query = new BasicDBObject();
			BasicDBList districtList = new BasicDBList();
			for (Integer district : areaMap.keySet()) {
				district = district / 10;
				districtList.add(district);
			}
			BasicDBObject inDistrict = new BasicDBObject("$in", districtList);
			query.put("district", inDistrict);
			query.put("day", day);
			DBCursor find = collection.find(query);
			List<DBObject> findByField = find.toArray();
			List<VehiclePassInAreaDAEntity> list = new ArrayList<>();
			for (DBObject entity : findByField) {
				VehiclePassInAreaDAEntity e = new VehiclePassInAreaDAEntity();
				e.dbObjectToBean(entity);
				if (areaMap.get(e.getDistrict() * 10 + e.getType()) != null) {
					e = mergeArea(e, areaMap.get(e.getDistrict() * 10 + e.getType()));
					areaMap.remove(e.getDistrict() * 10 + e.getType());
					mongoDao.delObjectById(collection, entity.get("_id").toString());
					list.add(e);
				}
			}
			List<DBObject> dbObjects = new ArrayList<>();
			for (VehiclePassInAreaDAEntity entity : list) {
				DBObject dbObject = entity.toDBObject();
				dbObjects.add(dbObject);
			}
			for (Entry<Integer, VehiclePassInAreaDAEntity> entity : areaMap.entrySet()) {
				dbObjects.add(entity.getValue().toDBObject());
			}
			logger.info("update areas data size :" + dbObjects.size());
			collection.insert(dbObjects);
			MongoManager.close();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			return PlatformResponseResult.failure;
		} finally {
			MongoManager.close();
		}
		return PlatformResponseResult.success;
	}

	/**
	 * 把区域统计结果对象转换为DA对象
	 *
	 * @param areas
	 * @return
	 */
	private Map<Integer, VehiclePassInAreaDAEntity> convertAreaRmiToDa(List<VehiclePassTimesRecord> areas) {
		Map<Integer, VehiclePassInAreaDAEntity> entities = new HashMap<>();
		if (null != areas && areas.size() > 0) {
			for (VehiclePassTimesRecord record : areas) {
				VehiclePassInAreaDAEntity entity = new VehiclePassInAreaDAEntity();
				entity.setDistrict(record.getDistrict());
				entity.setType(record.getType());
				entity.setTimes(record.getTimes());
				entity.setThisDay(record.getDay());
				// 当type=5服务站时，区域有终端信息，需要转换
				//if (record.getType() == 5) {
					entity.setLng(record.getLng());
					entity.setLat(record.getLat());
					entity.setDistrictCode(record.getDistrictCode());
					List<Long> tids = record.getTerminals();
					List<DAVehicleNumInAreaAlarm> alarmList = new ArrayList<DAVehicleNumInAreaAlarm>();
					for (Long tid : tids) {
						DAVehicleNumInAreaAlarm daVehicleNumInAreaAlarm = new DAVehicleNumInAreaAlarm();
						VehiclePassTimesDetail vehiclePassTimesDetail = record.getVehiclePassTimesDetail(tid);
						if (vehiclePassTimesDetail != null) {
							daVehicleNumInAreaAlarm.setTerminalId(vehiclePassTimesDetail.getTid());
							daVehicleNumInAreaAlarm.setMileage(vehiclePassTimesDetail.getMileage());
							daVehicleNumInAreaAlarm.setContinuousTime(vehiclePassTimesDetail.getRunTime());
							daVehicleNumInAreaAlarm.setFault(vehiclePassTimesDetail.getFaultCode());
							alarmList.add(daVehicleNumInAreaAlarm);
						}
					}
					entity.setDataList(alarmList);
				//}
				entities.put(entity.getDistrict() * 10 + entity.getType(), entity);
			}
		}
		return entities;
	}

	// private VehiclePassInAreaDAEntity filterCurrentDays(int day,int
	// type,List<DBObject> objects){
	// for(DBObject object : objects){
	// VehiclePassInAreaDAEntity e = new VehiclePassInAreaDAEntity();
	// e.dbObjectToBean(object);
	// if(day == e.getThisDay() && type == e.getType()){
	// return e;
	// }
	// }
	// return null;
	// }
	private VehiclePassInAreaDAEntity mergeArea(VehiclePassInAreaDAEntity e1, VehiclePassInAreaDAEntity e2) {
		if (e1.getDistrict() == e2.getDistrict() && e1.getType() == e2.getType() && e1.getDay() == e2.getDay()) {
			e1.setTimes(e1.getTimes() + e2.getTimes());
			// //type=服务站时，list合并
			if (e1.getType() == 5) {
				e1.getDataList().addAll(e2.getDataList());
			}
		} else {
			logger.error("传入的两个区域对象不能合并:" + e1.toString() + "-----" + e2.toString());
		}
		return e1;
	}

	@Override
	public CrossGridCounts getGridCrossCounts(List<Long> terminalIds, List<Long> tileId, long startDate, long endDate) {
		Map<String, List<String>> listDates = splitWithTenDays(startDate, endDate);
		Map<Long, Integer> tidTimesMap = new HashMap<Long, Integer>();
		for (Entry<String, List<String>> entry : listDates.entrySet()) {
			String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInGrid + "_"
					+ entry.getKey();
			BasicDBObject query = new BasicDBObject();
			BasicDBList monthList = new BasicDBList();
			List<String> ms = entry.getValue();
			for (String s : ms) {
				monthList.add(Integer.parseInt(s));
			}
			BasicDBList tileList = new BasicDBList();
			for (long tile : tileId) {
				tileList.add(tile);
			}
			BasicDBObject inMonth = new BasicDBObject("$in", monthList);
			BasicDBObject inTile = new BasicDBObject("$in", tileList);
			query.put("month", inMonth);
			query.put("tile", inTile);
			List<DBObject> dbObjects = mongoService.queryByCondition(collectionName, query);
			Map<Long, Integer> terminalTimes = convertToTerminalTimes(dbObjects, terminalIds);
			for (long tid : terminalIds) {
				Integer maxTimes = tidTimesMap.get(tid);
				Integer currentTimes = terminalTimes.get(tid);
				if (null != currentTimes) {
					if (null == maxTimes) {
						tidTimesMap.put(tid, currentTimes);
					} else {
						tidTimesMap.put(tid, maxTimes + currentTimes);
					}
				}
			}
		}
		CrossGridCounts result = convert(tidTimesMap);
		return result;
	}

	private CrossGridCounts convert(Map<Long, Integer> tidTimesMap) {
		CrossGridCounts.Builder counts = CrossGridCounts.newBuilder();
		Set<Long> keySet = tidTimesMap.keySet();
		for (long tid : keySet) {
			CrossGridRecord.Builder record = CrossGridRecord.newBuilder();
			record.setTerminalId(tid);
			record.setCounts(tidTimesMap.get(tid));
			counts.addRecords(record);
		}
		return counts.build();
	}

	@Override
	public ResVehiclePassTimesRecordsQuery getVehiclePassTimesBytileId(List<Long> tileIds, long startDate,
			long endDate) {
		ResVehiclePassTimesRecordsQuery records = new ResVehiclePassTimesRecordsQuery();
		// List<VehiclePassTimesEntity> list = new
		// ArrayList<VehiclePassTimesEntity>();
		Map<String, List<String>> listDates = splitWithTenDays(startDate, endDate);
		for (Entry<String, List<String>> entry : listDates.entrySet()) {
			String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInGrid + "_"
					+ entry.getKey();
			BasicDBObject query = new BasicDBObject();
			BasicDBList monthList = new BasicDBList();
			List<String> ms = entry.getValue();
			for (String s : ms) {
				monthList.add(Integer.parseInt(s));
			}
			BasicDBObject inMonth = new BasicDBObject("$in", monthList);
			BasicDBList tileList = new BasicDBList();
			for (Long s : tileIds) {
				tileList.add(s);
			}
			BasicDBObject inTile = new BasicDBObject("$in", tileList);
			query.put("month", inMonth);
			query.put("tile", inTile);
			List<DBObject> dbObjects = mongoService.queryByCondition(collectionName, query);
			List<VehiclePassTimesEntity> entities = convert(dbObjects);
			records.getDataList().addAll(entities);
		}
		Map<Long, VehiclePassTimesEntity> map = new HashMap<Long, VehiclePassTimesEntity>();
		for (int i = 0; i < records.getDataList().size(); i++) {
			long id = records.getDataList().get(i).get_id();
			if (map.get(id) == null) {
				map.put(id, records.getDataList().get(i));
			} else {
				map.put(id, mergeGrid(records.getDataList().get(i), map.get(id)));
			}
		}
		records.getDataList().clear();
		records.getDataList().addAll(map.values());
		records.setStatusCode(PlatformResponseResult.success_VALUE);
		records.setTotalRecords(records.getDataList().size());
		return records;
	}

	private VehiclePassTimesEntity mergeGrid(VehiclePassTimesEntity e1, VehiclePassTimesEntity e2) {
		if (e1.get_id() == e2.get_id()) {
			e1.setTimes(e1.getTimes() + e2.getTimes());
			List<SonAreaTimesEntity> e1son = e1.getSonAreaTimes();
			List<SonAreaTimesEntity> e2son = e2.getSonAreaTimes();
			e1.setSonAreaTimes(mergeSonGrid1(e1son, e2son));
		} else {
			logger.error("传入的两个对象瓦片ID不相同" + e1.toString() + "-----" + e2.toString());
		}
		return e1;
	}

	private List<SonAreaTimesEntity> mergeSonGrid1(List<SonAreaTimesEntity> son1, List<SonAreaTimesEntity> son2) {
		for (SonAreaTimesEntity s1 : son1) {
			for (SonAreaTimesEntity s2 : son2) {
				if (s1.get_id() == s2.get_id()) {
					s1.setTimes(s1.getTimes() + s2.getTimes());
					s1.setGrandSonAreaTimes(mergeGrandGrid1(s1.getGrandSonAreaTimes(), s2.getGrandSonAreaTimes()));
				}
			}
		}
		return son1;
	}

	private List<GrandSonAreaTimesEntity> mergeGrandGrid1(List<GrandSonAreaTimesEntity> son1,
			List<GrandSonAreaTimesEntity> son2) {
		for (GrandSonAreaTimesEntity s1 : son1) {
			for (GrandSonAreaTimesEntity s2 : son2) {
				if (s1.get_id() == s2.get_id()) {
					s1.setTimes(s1.getTimes() + s2.getTimes());
				}
			}
		}
		return son1;
	}

	private VehiclePassTimes mergeGridNew(VehiclePassTimes e1, VehiclePassTimes e2) {
		VehiclePassTimes.Builder builder = VehiclePassTimes.newBuilder();
		if(e1.getId() == e2.getId()) {
			builder.setTimes(e1.getTimes() + e2.getTimes());
			List<SonAreaTimes> e1son = e1.getSonAreaTimesList();
			List<SonAreaTimes> e2son = e2.getSonAreaTimesList();
			List<SonAreaTimes> merged = this.mergeSonGridNew(e1son, e2son);
			builder.addAllSonAreaTimes(merged);
		} else {
			logger.error("传入的两个对象瓦片ID不相同" + e1.toString() + "-----" + e2.toString());
			throw new IllegalArgumentException("传入的两个对象瓦片ID不相同" + e1.toString() + "-----" + e2.toString());
		}
		return builder.build();
	}
	
	private List<SonAreaTimes> mergeSonGridNew(List<SonAreaTimes> son1, List<SonAreaTimes> son2) {
		List<SonAreaTimes> result = new ArrayList<SonAreaTimes>();
		for (SonAreaTimes s1 : son1) {
			SonAreaTimes.Builder builder = SonAreaTimes.newBuilder();
			int times = s1.getTimes();
			List<GrandSonAreaTimes> grands = s1.getGrandsonAreaTimesList();
			for (SonAreaTimes s2 : son2) {
				if(s1.getId() == s2.getId()) {
					times += s2.getTimes();
					grands = this.mergeGrandSonGridNew(grands, s2.getGrandsonAreaTimesList());
				}
			}
			builder.setId(s1.getId());
			builder.setTimes(times);
			builder.addAllGrandsonAreaTimes(grands);
			result.add(builder.build());
		}
		return result;
	}
	
	private List<GrandSonAreaTimes> mergeGrandSonGridNew(List<GrandSonAreaTimes> son1, List<GrandSonAreaTimes> son2) {
		List<GrandSonAreaTimes> result = new ArrayList<GrandSonAreaTimes>();
		for (GrandSonAreaTimes s1 : son1) {
			GrandSonAreaTimes.Builder builder = GrandSonAreaTimes.newBuilder();
			int times = s1.getTimes();
			for (GrandSonAreaTimes s2 : son2) {
				if (s1.getId() == s2.getId()) {
					times += s2.getTimes();
				}
			}
			builder.setId(s1.getId());
			builder.setTimes(times);
			result.add(builder.build());
		}
		return result;
	}

	@Override
	public VehiclePassTimesRecords getVehiclePassTimesByTileId(List<Long> tileIds, long startDate, long endDate) {
		VehiclePassTimesRecords.Builder builder = VehiclePassTimesRecords.newBuilder();
		if (endDate > startDate) {
			Map<String, List<String>> listDates = splitWithTenDays(startDate, endDate);
			List<VehiclePassTimes> entities = new LinkedList<VehiclePassTimes>();
			for (Entry<String, List<String>> entry : listDates.entrySet()) {
				String collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.VehicleDrivingNumberInGrid + "_"
						+ entry.getKey();
				BasicDBObject query = new BasicDBObject();
				BasicDBList monthList = new BasicDBList();
				List<String> ms = entry.getValue();
				for (String s : ms) {
					monthList.add(Integer.parseInt(s));
				}
				BasicDBObject inMonth = new BasicDBObject("$in", monthList);
				BasicDBList tileList = new BasicDBList();
				for (Long s : tileIds) {
					tileList.add(s);
				}
				BasicDBObject inTile = new BasicDBObject("$in", tileList);
				query.put("month", inMonth);
				query.put("tile", inTile);

				try {
					DBCollection collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
					List<DBObject> dbObjects = mongoService.queryByCondition(collection, query, null, -1);
					List<VehiclePassTimes> el = convert2VehiclePassTimes(dbObjects);
					if(el != null) {
						entities.addAll(el);
					}
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
			}
			Map<Long, VehiclePassTimes> map = new HashMap<Long, VehiclePassTimes>();
			for (int i = 0; i < entities.size(); i++) {
				VehiclePassTimes entity = entities.get(i);
				long id = entity.getId();
				if(map.containsKey(id)) {
					VehiclePassTimes current = map.get(i);
					VehiclePassTimes merged = this.mergeGridNew(current, entity);
					map.put(id, merged);
				} else {
					map.put(id, entity);
				}
			}
			builder.addAllDataList(map.values());
			builder.setStatusCode(PlatformResponseResult.success_VALUE);
			builder.setTotalRecords(map.size());
		} else {
			builder.setStatusCode(PlatformResponseResult.failure_VALUE);
			builder.setTotalRecords(0);
		}
		return builder.build();
	}
}
