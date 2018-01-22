package com.navinfo.opentsp.platform.da.core.persistence.application;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.common.StatisticType;
import com.navinfo.opentsp.platform.da.core.persistence.DataStatisticsService;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.TotalAlarmInfoEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACommonSummaryEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DATotalAlarmInfo;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.BaseMongoDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.WFlowServiceImp;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.AllAlarmData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.MileageSummaryEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.WFSummary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 数据统计分析数据查询服务
 *
 * @author admin
 *
 */
public class DataStatisticsServiceImpl implements DataStatisticsService {
	private static Logger logger = LoggerFactory.getLogger(DataStatisticsServiceImpl.class);
	BaseMongoDaoImpl mongoService = new BaseMongoDaoImpl();

	/**
	 * 查询报警详细信息
	 *
	 * @param terminalId
	 * @param alarmType
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public List<DBObject> queryAlarmData(List<Long> terminalId, String alarmType, long beginTime, long endTime)
			throws ParseException {
		List<DBObject> result = new ArrayList<DBObject>();
		// 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
		Map<String, Map<String, Long>> listDates = _get_dateSlot(beginTime, endTime);
		for (Entry entry : listDates.entrySet()) {
			Map map = (Map) entry.getValue();
			Object beginTimeMonth = map.get("beginTime");
			Object endTimeMonth = map.get("endTime");
			List<DBObject> temp = null;
			if (alarmType.equals(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmMilages)) {
				temp = findMilagesData(terminalId, alarmType + "_" + (String) entry.getKey(),
						(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
								: endTimeMonth));
			} else if (alarmType.equals(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmWFlow)) {
				temp = findWFlowData(terminalId, alarmType + "_" + (String) entry.getKey(),
						(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
								: endTimeMonth));
			} else {
				temp = findAlarmData(terminalId, alarmType + "_" + (String) entry.getKey(),
						(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
								: endTimeMonth));
			}

			if (temp != null) {
				result.addAll(temp);
			}
		}

		return result;
	}

	/**
	 *
	 * @param terminalId
	 * @param alarmType
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public List<DBObject> queryAlarmData(List<Long> terminalId, String alarmType, long beginTime, long endTime, int type)
			throws ParseException {
		List<DBObject> result = new ArrayList<DBObject>();
		// 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
		/**
		 * beginTime 1433201014 例如 1506 endTime 1433301014
		 */
		Map<String, Map<String, Long>> listDates = _get_dateSlot(beginTime, endTime);
		for (Entry entry : listDates.entrySet()) {
			Map map = (Map) entry.getValue();
			Object beginTimeMonth = map.get("beginTime");
			Object endTimeMonth = map.get("endTime");
			List<DBObject> temp = null;
			if (alarmType.equals(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmMilages)) {

				temp = findMilagesData(terminalId, alarmType + "_" + (String) entry.getKey(),
						(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
								: endTimeMonth));
			} else if (alarmType.equals(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmWFlow)) {
				temp = findWFlowData(terminalId, alarmType + "_" + (String) entry.getKey(),
						(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
								: endTimeMonth));
			}

			else {
				temp = findAlarmData(terminalId, alarmType + "_" + (String) entry.getKey(),
						(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
								: endTimeMonth), type);
			}

			if (temp != null) {
				result.addAll(temp);
			}
		}

		return result;
	}

	/**
	 * 查询报警概要信息
	 *
	 * @param terminalId
	 * @param alarmType
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public List<DACommonSummaryEntity> queryAlarmSummaryData(List<Long> terminalId, String alarmType, long beginTime,
			long endTime) throws ParseException {
		// 返回的数据List
		List<DACommonSummaryEntity> result = new ArrayList<DACommonSummaryEntity>();
		Map<Long, DACommonSummaryEntity> summaryMap = new HashMap<Long, DACommonSummaryEntity>();
		// 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
		Map<String, Map<String, Long>> listDates = _get_dateSlot(beginTime, endTime);
		// 迭代查询说有查询段的数据
		for (Entry<String, Map<String, Long>> entry : listDates.entrySet()) {
			Map<String, Long> map = (Map<String, Long>) entry.getValue();
			// 开始时间
			Object beginTimeMonth = map.get("beginTime");
			// 结束时间
			Object endTimeMonth = map.get("endTime");
			// 调用服务从mongo中查询概要信息
			List<DACommonSummaryEntity> temp = getAlarmSummaryData(terminalId,
					alarmType + "_" + (String) entry.getKey(), (long) (beginTimeMonth == null ? 0l : beginTimeMonth),
					(long) (endTimeMonth == null ? 0l : endTimeMonth));
			if (temp != null) {
				for (DACommonSummaryEntity entity : temp) {
					DACommonSummaryEntity commonSummaryEntity = summaryMap.get(entity.getTerminalID());
					if (commonSummaryEntity == null) {
						result.add(entity);
						summaryMap.put(entity.getTerminalID(), entity);
					} else {
						// 概要数据合并
						merge(commonSummaryEntity, entity);
					}
				}
			}
		}

		return result;
	}

	/**
	 * 进出区域，路线
	 *
	 * @param terminalId
	 * @param alarmType
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public List<DACommonSummaryEntity> queryAlarmSummaryData(List<Long> terminalId, String alarmType, long beginTime,
			long endTime, int type) throws ParseException {
		List<DACommonSummaryEntity> result = new ArrayList<DACommonSummaryEntity>();
		Map<Long, DACommonSummaryEntity> summaryMap = new HashMap<Long, DACommonSummaryEntity>();
		// 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
		Map<String, Map<String, Long>> listDates = _get_dateSlot(beginTime, endTime);
		for (Entry<String, Map<String, Long>> entry : listDates.entrySet()) {
			Map<String, Long> map = (Map<String, Long>) entry.getValue();
			Object beginTimeMonth = map.get("beginTime");
			Object endTimeMonth = map.get("endTime");
			List<DACommonSummaryEntity> temp = getAlarmSummaryDataWithType(terminalId,
					alarmType + "_" + (String) entry.getKey(), (long) (beginTimeMonth == null ? 0l : beginTimeMonth),
					(long) (endTimeMonth == null ? 0l : endTimeMonth), type);
			if (temp != null) {
				for (DACommonSummaryEntity entity : temp) {
					DACommonSummaryEntity commonSummaryEntity = summaryMap.get(entity.getTerminalID());
					if (commonSummaryEntity == null) {
						result.add(entity);
						summaryMap.put(entity.getTerminalID(), entity);
					} else {
						// 概要数据合并
						merge(commonSummaryEntity, entity);
					}
				}
			}
		}

		return result;
	}

	/**
	 *
	 * @param temp
	 * @param entity
	 */
	public void merge(DACommonSummaryEntity temp, DACommonSummaryEntity entity) {
		temp.setRecordsTotal(temp.getRecordsTotal() + entity.getRecordsTotal());
		temp.setCalculatedAT(temp.getCalculatedAT() + entity.getCalculatedAT());
		temp.setStartTime(Math.min(temp.getStartTime(), entity.getStartTime()));
		temp.setEndTime(Math.max(temp.getEndTime(), entity.getEndTime()));
	}

	/**
	 * 查询汇总条数
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private int getAlarmDataCount(List<Long> terminalId, String collectionName, long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();

		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		DBObject query = new BasicDBObject();
		query.put("HA", time);
		query.put("AA", in);
		int count = mongoService.queryCountByCondition(collectionName, query);
		return count;

	}

	/**
	 * 查询报警概要信息
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<DACommonSummaryEntity> getAlarmSummaryData(List<Long> terminalId, String collectionName,
			long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();

		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		BasicDBObject query = new BasicDBObject();
		query.put("AA", in);
		List<DACommonSummaryEntity> totalSummaryEntity = null;
		if (collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmMilages)) {
			query.put("dataList.HA", time);
			// totalSummaryEntity =
			// mongoService.getTotalMilagesSummaryEntity(collectionName,
			// query,beginTime,endTime);
		} else if (collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmWFlow)) {
			query.put("DA", time);
			// totalSummaryEntity =
			// mongoService.getTotalWFlowSummaryEntity(collectionName, query,
			// beginTime, endTime);

		} else {
			query.put("HA", time);
			totalSummaryEntity = mongoService.getTotalSummaryEntity(collectionName, query);
		}

		return totalSummaryEntity;

	}

	/**
	 * 查询报警概要信息 (进出区域，路线)
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<DACommonSummaryEntity> getAlarmSummaryDataWithType(List<Long> terminalId, String collectionName,
			long beginTime, long endTime, int type) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();

		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		BasicDBObject query = new BasicDBObject();
		query.put("AA", in);
		List<DACommonSummaryEntity> totalSummaryEntity = null;

		query.put("HA", time);

		if (collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAreaINOUT)
				|| collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRouteINOUT)) {
			if (type != 100) {
				query.append("EA", type);
			}

		} else if (collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmTerminalOnOffLineStatus)) {
			if (type != 100) {
				query.put("LB", type);
			}
		}
		totalSummaryEntity = mongoService.getTotalSummaryEntity(collectionName, query);
		return totalSummaryEntity;

	}

	/**
	 * mongo查询统计数据详情数据
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<DBObject> findAlarmData(List<Long> terminalId, String collectionName, long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();
		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		DBObject query = new BasicDBObject();
		query.put("AA", in);
		if (collectionName.startsWith("DriverLogin")) {
			query.put("HB", time);
		} else {
			query.put("HA", time);
		}

		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		return queryData;

	}

	/**
	 *  查询所有终端未标记为结束的滞留记录
	 * @param terminalId
	 * @param collectionName
	 * @return
	 */
	public List<DBObject> findOverTimeParkNoMergeData(List<Long> terminalId, String collectionName) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		DBObject query = new BasicDBObject();
		query.put("AA", in);
		query.put("tailMerge", 0);

		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		return queryData;

	}

	/**
	 * mongo查询统计数据详情数据
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @param type
	 * @return
	 */
	private List<DBObject> findAlarmData(List<Long> terminalId, String collectionName, long beginTime, long endTime,
			int type) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();
		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}

		DBObject query = new BasicDBObject();
		query.put("AA", in);
		query.put("HA", time);
		if (collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmAreaINOUT)
				|| collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmRouteINOUT)) {
			if (type != 100) {
				query.put("EA", type);
			}

		} else if (collectionName.startsWith(Constant.PropertiesKey.AlarmTypeTableMapping.AlarmTerminalOnOffLineStatus)) {
			if (type != 100) {
				query.put("LB", type);
			}

		}
		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		return queryData;

	}

	/**
	 * mongo查询里程详情信息
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<DBObject> findMilagesData(List<Long> terminalId, String collectionName, long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();
		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		DBObject query = new BasicDBObject();
		query.put("AA", in);
		query.put("dataList.HA", time);

		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		return queryData;

	}

	/**
	 * mongo查询流量详情信息
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<DBObject> findWFlowData(List<Long> terminalId, String collectionName, long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();
		if (beginTime > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(beginTime * 1000);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			time.append("$gte", calendar.getTimeInMillis() / 1000);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		DBObject query = new BasicDBObject();
		query.put("AA", in);
		query.put("DA", time);

		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		return queryData;

	}

	/**
	 * beginTime 1433201014 1506 endTime 1433301014
	 *
	 *
	 * 获取时间段 ,拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间,s时间>>
	 *
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	private Map<String, Map<String, Long>> _get_dateSlot(long beginTime, long endTime) throws ParseException {
		TreeMap<String, Map<String, Long>> result = new TreeMap<String, Map<String, Long>>();

		// 日期转换 yyyy-mm-ss
		Date beginDate = new Date(beginTime * 1000);
		Date endDate = new Date(endTime * 1000);
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String getBeginMonth = formatter.format(beginDate);
		String getendMonth = formatter.format(endDate);
		// 计算月份差
		long monthDiff = DateDiffUtil.getMonthDiff(getBeginMonth, getendMonth);
		List<String> monthBetween = DateDiffUtil.getMonthBetween(getBeginMonth, getendMonth);
		if (monthDiff == 0) {

			// 是否是当前月
			Map<String, Long> timeCondition = new HashMap<String, Long>();
			timeCondition.put("beginTime", beginTime);
			timeCondition.put("endTime", endTime);
			result.put(monthBetween.get(0), timeCondition);

		} else {
			String startMonth = (new SimpleDateFormat("yyMM")).format(beginDate);
			String endMonth = (new SimpleDateFormat("yyMM")).format(endDate);
			for (String month : monthBetween) {
				Map<String, Long> timeCondition = new HashMap<String, Long>();
				if (month.equals(startMonth)) {
					// 起始月
					timeCondition.put("beginTime", beginTime);
					timeCondition.put("endTime", DateDiffUtil.getEndDayOfMonth(month));
				} else if (month.equals(endMonth)) {
					// 终止月
					timeCondition.put("beginTime", DateDiffUtil.getBeginDayOfMonth(month));
					timeCondition.put("endTime", endTime);
				} else {
					// 起止中间
					timeCondition.put("beginTime", DateDiffUtil.getBeginDayOfMonth(month));
					timeCondition.put("endTime", DateDiffUtil.getEndDayOfMonth(month));
				}
				result.put(month, timeCondition);
			}
		}
		return result;
	}

	public static void main(String[] args) {
		// 1433201014 1433301014
		// System.out.println(System.currentTimeMillis()/1000);
		/**
		 * beginTime 1433201014 1506 endTime 1433301014
		 */
		try {
			Map<String, Map<String, Long>> listDates = new DataStatisticsServiceImpl()._get_dateSlot(1433201014l,
					1433301014l);
			/*
			 * for(Map.Entry<String, Map<String,Long>> entry :
			 * listDates.entrySet()) {
			 * System.out.println("parent k="+entry.getKey()); Map<String,Long>
			 * map = entry.getValue(); for(Map.Entry<String, Long> e :
			 * map.entrySet()) {
			 * System.out.println("child k="+e.getKey()+"child v="
			 * +e.getValue()); } }
			 */

		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 查询报警汇总统计
	 *
	 * @param terminalId
	 * @param alarmType
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public List<AllAlarmData> queryAllAlarmSummaryData(List<Long> terminalId, String alarmType, long beginTime,
			long endTime) throws ParseException {

		List<AllAlarmData> result = new ArrayList<AllAlarmData>();
		Map<Long, AllAlarmData> summaryMap = new HashMap<Long, AllAlarmData>();
		// 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间key,时间>>
		Map<String, Map<String, Long>> listDates = _get_dateSlot(beginTime, endTime);
		for (Entry<String, Map<String, Long>> entry : listDates.entrySet()) {
			Map<String, Long> map = (Map<String, Long>) entry.getValue();
			Object beginTimeMonth = map.get("beginTime");
			Object endTimeMonth = map.get("endTime");
			List<AllAlarmData> temp = getAllAlarmSummaryData(terminalId, alarmType + "_" + (String) entry.getKey(),
					(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
							: endTimeMonth));
			if (temp != null) {
				for (AllAlarmData entity : temp) {
					AllAlarmData commonSummaryEntity = summaryMap.get(entity.getTerminalID());
					if (commonSummaryEntity == null) {
						result.add(entity);
						summaryMap.put(entity.getTerminalID(), entity);
					} else {
						// 概要数据合并
						mergeAllAlarm(commonSummaryEntity, entity);
					}
				}
			}
		}

		return result;

	}

	/**
	 * 合并同终端报警汇总统计
	 *
	 * @param commonSummaryEntity
	 * @param entity
	 */
	private void mergeAllAlarm(AllAlarmData commonSummaryEntity, AllAlarmData entity) {
		commonSummaryEntity.setAreaInOut(commonSummaryEntity.getAreaInOut() + entity.getAreaInOut());
		commonSummaryEntity.setAreaOS(commonSummaryEntity.getAreaOS() + entity.getAreaOS());
		commonSummaryEntity.setElectricPOff(commonSummaryEntity.getElectricPOff() + entity.getElectricPOff());
		commonSummaryEntity.setEmergecy(commonSummaryEntity.getEmergecy() + entity.getEmergecy());
		commonSummaryEntity.setFatigue(commonSummaryEntity.getFatigue() + entity.getFatigue());
		commonSummaryEntity.setOffRLine(commonSummaryEntity.getOffRLine() + entity.getOffRLine());
		commonSummaryEntity.setOverSpd(commonSummaryEntity.getOverSpd() + entity.getOverSpd());
		commonSummaryEntity.setOverTP(commonSummaryEntity.getOverTP() + entity.getOverTP());
		commonSummaryEntity.setPowerSIW(commonSummaryEntity.getPowerSIW() + entity.getPowerSIW());
		commonSummaryEntity.setrLineOSpd(commonSummaryEntity.getrLineOSpd() + entity.getrLineOSpd());
		commonSummaryEntity.setrLineOT(commonSummaryEntity.getrLineOT() + entity.getrLineOT());
		commonSummaryEntity.setRouteIO(commonSummaryEntity.getRouteIO() + entity.getRouteIO());

	}

	/**
	 * 查询报警汇总统计信息
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<AllAlarmData> getAllAlarmSummaryData(List<Long> terminalId, String collectionName, long beginTime,
			long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();

		// if(beginTime>0){
		// time.append("$gte",beginTime);
		// }
		if (beginTime > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(beginTime * 1000);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			time.append("$gte", calendar.getTimeInMillis() / 1000);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		BasicDBObject query = new BasicDBObject();
		query.put("AA", in);

		List<AllAlarmData> totalSummaryEntity = new ArrayList<AllAlarmData>();

		query.put("DA", time);
		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		if (queryData != null) {
			for (DBObject opda : queryData) {
				TotalAlarmInfoEntity item = null;
				try {
					item = TotalAlarmInfoEntity.class.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				item.dbObjectToBean(opda);
				AllAlarmData allAlarmData = new AllAlarmData();
				allAlarmData.setTerminalID(item.getTerminal_id());
				List<DATotalAlarmInfo> dataList = item.getDataList();
				for (DATotalAlarmInfo info : dataList) {
					int type = info.getType();
					switch (type) {
					case StatisticType.lossPowerAlarm:
						allAlarmData.setElectricPOff(info.getCounts());
						break;
					case StatisticType.speedingAlarm:
						allAlarmData.setOverSpd(info.getCounts());
						break;
					case StatisticType.areaInOutAlarm:
						allAlarmData.setAreaInOut(info.getCounts());
						break;
					case StatisticType.areaSpeedingAlarm:
						allAlarmData.setAreaOS(info.getCounts());
						break;
					case StatisticType.emergencyAlarm:
						allAlarmData.setEmergecy(info.getCounts());
						break;
					case StatisticType.routeDeviate:
						allAlarmData.setOffRLine(info.getCounts());
						break;
					case StatisticType.outParkingAlarm:
						allAlarmData.setOverTP(info.getCounts());
						break;
					case StatisticType.roadOvertimeAlarm:
						allAlarmData.setrLineOT(info.getCounts());
						break;
					case StatisticType.routeInOutAlarm:
						allAlarmData.setRouteIO(info.getCounts());
						break;
					case StatisticType.roadSpeedingAlarm:
						allAlarmData.setrLineOSpd(info.getCounts());
						break;
					case StatisticType.underPowerAlarm:
						allAlarmData.setPowerSIW(info.getCounts());
						break;
					case StatisticType.tiredDrivingAlarm:
						allAlarmData.setFatigue(info.getCounts());
						break;
					default:
						break;
					}
				}
				totalSummaryEntity.add(allAlarmData);
			}
			return totalSummaryEntity;
		}

		return null;
	}

	/**
	 * 查询里程概要信息
	 *
	 * @param terminalId
	 * @param alarmType
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public List<MileageSummaryEntity> queryMileageSummaryData(List<Long> terminalId, String alarmType, long beginTime,
			long endTime) throws ParseException {

		List<MileageSummaryEntity> result = new ArrayList<MileageSummaryEntity>();
		Map<Long, MileageSummaryEntity> summaryMap = new HashMap<Long, MileageSummaryEntity>();
		// 拆分查询时间段 Map<时间月份（格式yyMM）, Map<开始或结束时间key,时间>>
		Map<String, Map<String, Long>> listDates = _get_dateSlot(beginTime, endTime);
		for (Entry<String, Map<String, Long>> entry : listDates.entrySet()) {
			Map<String, Long> map = (Map<String, Long>) entry.getValue();
			Object beginTimeMonth = map.get("beginTime");
			Object endTimeMonth = map.get("endTime");
			List<MileageSummaryEntity> temp = getMileageSummaryData(terminalId,
					alarmType + "_" + (String) entry.getKey(), (long) (beginTimeMonth == null ? 0l : beginTimeMonth),
					(long) (endTimeMonth == null ? 0l : endTimeMonth));
			if (temp != null) {
				for (MileageSummaryEntity entity : temp) {
					MileageSummaryEntity commonSummaryEntity = summaryMap.get(entity.getTerminalId());
					if (commonSummaryEntity == null) {
						result.add(entity);
						summaryMap.put(entity.getTerminalId(), entity);
					} else {
						// 概要数据合并
						mergeMileage(commonSummaryEntity, entity);
					}
				}
			}
		}

		return result;

	}

	/**
	 * 合并同终端的里程概要信息
	 *
	 * @param commonSummaryEntity
	 * @param entity
	 */
	private void mergeMileage(MileageSummaryEntity commonSummaryEntity, MileageSummaryEntity entity) {
		commonSummaryEntity.setBeginDate(Math.min(commonSummaryEntity.getBeginDate(), entity.getBeginDate()));
		commonSummaryEntity.setBeginLat(Math.min(commonSummaryEntity.getBeginLat(), entity.getBeginLat()));
		commonSummaryEntity.setBeginLng(Math.min(commonSummaryEntity.getBeginLng(), entity.getBeginLng()));
		commonSummaryEntity.setBeginMileage(Math.min(commonSummaryEntity.getBeginMileage(), entity.getBeginMileage()));
		commonSummaryEntity.setEndDate(Math.max(commonSummaryEntity.getEndDate(), entity.getEndDate()));
		commonSummaryEntity.setEndLat(Math.max(commonSummaryEntity.getEndLat(), entity.getEndLat()));
		commonSummaryEntity.setEndLNG(Math.max(commonSummaryEntity.getEndLNG(), entity.getEndLNG()));
		commonSummaryEntity.setEndMileage(Math.max(commonSummaryEntity.getBeginMileage(), entity.getBeginMileage()));

	}

	/**
	 * 查询报警概要信息
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<MileageSummaryEntity> getMileageSummaryData(List<Long> terminalId, String collectionName,
			long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();

		if (beginTime > 0) {
			time.append("$gte", beginTime);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		BasicDBObject query = new BasicDBObject();
		query.put("AA", in);
		List<MileageSummaryEntity> totalSummaryEntity = null;

		query.put("dataList.HA", time);
		totalSummaryEntity = mongoService.getTotalMilagesSummaryEntity(collectionName, query, beginTime, endTime);

		return totalSummaryEntity;
	}

	/**
	 * 查询流量概要信息
	 *
	 * @param terminalId
	 * @param collectionName
	 * @param beginTime
	 * @param endTime
	 * @return
	 */
	private List<WFSummary> getWFSummaryData(List<Long> terminalId, String collectionName, long beginTime, long endTime) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();

		if (beginTime > 0) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(beginTime * 1000);
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			time.append("$gte", calendar.getTimeInMillis() / 1000);
		}
		if (endTime > 0) {
			time.append("$lt", endTime);
		}
		BasicDBObject query = new BasicDBObject();
		query.put("AA", in);
		List<WFSummary> totalSummaryEntity = null;

		query.put("DA", time);

		totalSummaryEntity = mongoService.getTotalWFlowSummaryEntity(collectionName, query, beginTime, endTime);

		return totalSummaryEntity;
	}

	/**
	 * 查询流量概要信息
	 *
	 * @param terminalId
	 * @param type
	 * @param beginTime
	 * @param endTime
	 * @return
	 * @throws ParseException
	 */
	public List<WFSummary> queryWFSummaryData(List<Long> terminalId, String type, long beginTime, long endTime) {
		List<WFSummary> result = new ArrayList<WFSummary>();
		Map<Long, WFSummary> summaryMap = new HashMap<Long, WFSummary>();
		Map<String, Map<String, Long>> listDates = null;
		try {
			listDates = _get_dateSlot(beginTime, endTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (Entry<String, Map<String, Long>> entry : listDates.entrySet()) {
			Map<String, Long> map = (Map<String, Long>) entry.getValue();
			Object beginTimeMonth = map.get("beginTime");
			Object endTimeMonth = map.get("endTime");
			List<WFSummary> temp = getWFSummaryData(terminalId, type + "_" + (String) entry.getKey(),
					(long) (beginTimeMonth == null ? 0l : beginTimeMonth), (long) (endTimeMonth == null ? 0l
							: endTimeMonth));
			if (temp != null) {
				for (WFSummary entity : temp) {
					WFSummary commonSummaryEntity = summaryMap.get(entity.getTerminalId());
					if (commonSummaryEntity == null) {
						result.add(entity);
						summaryMap.put(entity.getTerminalId(), entity);
					} else {
						// 概要数据合并
						mergeWF(commonSummaryEntity, entity);
					}
				}
			}

		}

		int endTimeDay = DateUtils.differDay(DateUtils.current(), DateUtils.calendar(endTime));
		if (endTimeDay == 0) {
			WFlowServiceImp wFlowServiceImp = new WFlowServiceImp();
			for (long terminal : terminalId) {
				Object obj = wFlowServiceImp.findTerminalData(terminal);

				if (null != obj) {
					DAWFlow value = null;
					WFSummary item = new WFSummary();
					FlowCacheEntity flowData = (FlowCacheEntity) obj;
					// Date currentFlowDate =
					// DateUtils.parse(flowData.getDate(),DateFormat.YYYYMMDD );
					String formatTime = DateUtils.format(endTime, DateUtils.DateFormat.YYYYMMDD);
					if (formatTime.equals(flowData.getDate())) {
						List<DAWFlow> dataList = flowData.getDataList();
						Calendar calendar = Calendar.getInstance();
						calendar.setTimeInMillis(endTime * 1000);
						for (int j = 0; j < dataList.size(); j++) {

							DAWFlow wFlow = dataList.get(j);
							int flowDate = wFlow.getFlowDate();
							calendar.set(Calendar.HOUR_OF_DAY, flowDate);
							long currentFlowTime = calendar.getTimeInMillis() / 1000;
							if (currentFlowTime < beginTime || currentFlowTime > endTime) {
								continue;
							} else {
								if (value == null) {
									value = new DAWFlow();
									value.setTerminalId(terminal);
									item.setBeginDate(currentFlowTime);
									item.setEndDate(currentFlowTime);
								}
								value.setDownFlow(value.getDownFlow() + wFlow.getDownFlow());
								value.setUpFlow(value.getUpFlow() + wFlow.getUpFlow());
								item.setEndDate(currentFlowTime);
							}

						}

						item.setTerminalId(terminal);
						if (item.getBeginDate() == 0) {
							item.setBeginDate(beginTime);
						}
						if (item.getEndDate() == 0) {
							item.setEndDate(endTime);
						}

						item.setUpFlow(value.getUpFlow());
						item.setDownFlow(value.getDownFlow());
						WFSummary commonSummaryEntity = summaryMap.get(item.getTerminalId());
						if (commonSummaryEntity == null) {
							result.add(item);
							summaryMap.put(item.getTerminalId(), item);
						} else {
							// 概要数据合并
							mergeWF(commonSummaryEntity, item);
						}
					}

				}

			}

		}
		return result;
	}

	/**
	 * 合并同终端的流量概要信息
	 *
	 * @param commonSummaryEntity
	 * @param entity
	 */
	private void mergeWF(WFSummary commonSummaryEntity, WFSummary entity) {
		commonSummaryEntity.setBeginDate(Math.min(commonSummaryEntity.getBeginDate(), entity.getBeginDate()));
		commonSummaryEntity.setEndDate(Math.max(commonSummaryEntity.getEndDate(), entity.getEndDate()));
		commonSummaryEntity.setDownFlow(commonSummaryEntity.getDownFlow() + entity.getDownFlow());
		commonSummaryEntity.setUpFlow(commonSummaryEntity.getUpFlow() + entity.getUpFlow());
	}

	/**
	 * 终端在线统计
	 * @param terminalIds
	 * @param alarmType
	 * @param dateSet
	 * @param type
	 * @return
	 * @throws ParseException
	 */
	public List<DACommonSummaryEntity> queryAlarmSummaryDataForTerminalOnOff(List<Long> terminalIds, String alarmType,
			Set<String> dateSet, int type) throws ParseException {
		List<DACommonSummaryEntity> result = new ArrayList<DACommonSummaryEntity>();
		Map<Long, DACommonSummaryEntity> summaryMap = new HashMap<Long, DACommonSummaryEntity>();
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		for (String date : dateSet) {
			long dateTime = sf.parse(date).getTime();
			long beginTime = dateTime / 1000;
			Calendar instance = Calendar.getInstance();
			instance.setTimeInMillis(dateTime);
			instance.add(Calendar.DAY_OF_YEAR, 1);
			long endTime = (instance.getTimeInMillis() - 1) / 1000;
			List<DACommonSummaryEntity> temp = getAlarmSummaryDataWithType(terminalIds,
					alarmType + "_" + date.substring(2, 6), beginTime, endTime, type);
			if (temp != null) {
				for (DACommonSummaryEntity entity : temp) {
					DACommonSummaryEntity commonSummaryEntity = summaryMap.get(entity.getTerminalID());
					if (commonSummaryEntity == null) {
						result.add(entity);
						summaryMap.put(entity.getTerminalID(), entity);
					} else {
						// 概要数据合并
						merge(commonSummaryEntity, entity);
					}
				}
			}
		}

		return result;
	}

	public Map<Long, Integer> getOilConsumptionData(List<Long> terminalIds, long startDate, long endDate) {
		BasicDBList values = new BasicDBList();
		for (int i = 0, size = terminalIds.size(); i < size; i++) {
			values.add(terminalIds.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		BasicDBObject time = new BasicDBObject();
		if (startDate > 0) {
			time.append("$gte", startDate);
		}
		if (endDate > 0) {
			time.append("$lte", endDate);
		}
		BasicDBObject query = new BasicDBObject();
		query.put("AA", in);
		query.put("DA", time);
		Map<Long, Integer> map = new HashMap<Long, Integer>();
		Map<String, Map<String, Long>> listDates;
		String collectionName = "";
		try {
			listDates = _get_dateSlot(startDate, endDate);
			for (Entry<String, Map<String, Long>> entry : listDates.entrySet()) {
				collectionName = Constant.PropertiesKey.AlarmTypeTableMapping.AlarmMilages + "_"
						+ (String) entry.getKey();
				logger.info("查询油耗：" + collectionName);
				map = mongoService.getOilConsumptionData(collectionName, query, startDate, endDate);
			}
		} catch (ParseException e) {
			logger.error(e.getMessage(), e);
		}
		return map;
	}
	/**
	 * 查询所有终端未标记为结束的停滞超时记录
	 * 
	 * @param terminalId
	 * @param collectionName
	 * @return
	 */
	public List<DBObject> findStagnationTimeoutNoMergeData(List<Long> terminalId, String collectionName) {
		BasicDBList values = new BasicDBList();
		for (int i = 0; i < terminalId.size(); i++) {
			values.add(terminalId.get(i));
		}
		BasicDBObject in = new BasicDBObject("$in", values);
		DBObject query = new BasicDBObject();
		query.put("tId", in);
		query.put("tail", 0);

		List<DBObject> queryData = mongoService.queryByCondition(collectionName, query);
		return queryData;

	}
}
