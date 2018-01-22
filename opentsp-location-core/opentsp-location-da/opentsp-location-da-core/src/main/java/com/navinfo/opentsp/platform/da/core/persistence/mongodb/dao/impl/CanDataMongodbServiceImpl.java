package com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataReportEntity;
import  com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACanDataDetail;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.LCMongo;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoDaoImp;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import  com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.CanDataMongodbService;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;

public class CanDataMongodbServiceImpl extends MongoDaoImp implements
		CanDataMongodbService {

	@Override
	public boolean saveCanData(CanDataEntity dataEntity) {
		List<DACanDataDetail> datas = dataEntity.getDataList();
		List<DBObject> dbObjects = new ArrayList<DBObject>(datas.size());
		for (DACanDataDetail can : datas) {
			dbObjects.add(can.toDBObject());
		}
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("tId", dataEntity.gettId());
		dbObject.put("time", dataEntity.getTime());
		dbObject.put("dataList", dbObjects);

		boolean result = super.save(dbObject);
		return result;
	}

	@Override
	public boolean saveReportCanData(CanDataReportEntity dataEntity) {
		List<DACanDataDetail> datas = dataEntity.getDataList();
		List<DBObject> dbObjects = new ArrayList<DBObject>(datas.size());
		for (DACanDataDetail can : datas) {
			dbObjects.add(can.toDBObject());
		}
		BasicDBObject dbObject = new BasicDBObject();
		dbObject.put("tId", dataEntity.gettId());
		dbObject.put("day", dataEntity.getDay());
		dbObject.put("dataList", dbObjects);

		boolean result = super.save(dbObject);
		return result;
	}

	private List<String> splitWithDay(long beginTime, long endTime) {
		List<String> result = new ArrayList<String>();
		Calendar beginDate = DateUtils.calendar(beginTime);
		Calendar endDate = DateUtils.calendar(endTime);
		while (true) {
			result.add(DateUtils.format(beginDate.getTime(),
					DateFormat.YYYYMMDD));
			beginDate.set(Calendar.DAY_OF_MONTH,
					beginDate.get(Calendar.DAY_OF_MONTH) + 1);
			if (beginDate.get(Calendar.DAY_OF_MONTH) == endDate
					.get(Calendar.DAY_OF_MONTH)) {
				result.add(DateUtils.format(beginDate.getTime(),
						DateFormat.YYYYMMDD));
				break;
			}
			if (beginDate.get(Calendar.DAY_OF_MONTH) >endDate
					.get(Calendar.DAY_OF_MONTH)) {

				break;
			}
		}
		return result;
	}

	@Override
	public List<CanDataEntity> findCanData(List<Long> terminalIds,
										   long beginTime, long endTime, int pageNum, int pageSize) {
		List<CanDataEntity> allDbs = new ArrayList<CanDataEntity>();
		DBCollection collection = null;
		try {
			collection = MongoManager.start(LCMongo.DB.LC_GPS_CANDATA,LCMongo.Collection.LC_CANDATA);
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
		BasicDBObject gte = new BasicDBObject("$gte", beginTime);
		BasicDBObject lte = new BasicDBObject("$lte", endTime);
		DBObject filter = new BasicDBObject();
		filter.put("tId", allTids);
		filter.put("time", gte);
		filter.put("time", lte);
		List<DBObject> objects = null;
		if(0==pageNum && 0==pageSize){
			objects = collection.find(filter).toArray();
		}else{
			int beginIndex = (pageNum-1)*pageSize;
			objects = collection.find(filter).skip(beginIndex).limit(pageSize).toArray();
		}
		if (null != objects && objects.size() > 0) {
			for(DBObject object : objects){
				CanDataEntity entity = new CanDataEntity();
				List<DBObject> datas = (List<DBObject>) object.get("dataList");
				long dbTid = (long)object.get("tId");
				long dbTime = (long)object.get("time");
				List<DACanDataDetail> entities = new ArrayList<DACanDataDetail>();
				if (null != datas) {
					for (DBObject dbObject : datas) {
						long detailTime = Long.parseLong(String.valueOf(dbObject.get("time")));
						if (detailTime >= beginTime && detailTime <= endTime) {
							DACanDataDetail detail = new DACanDataDetail(dbObject);
							entities.add(detail);
						}
					}
				}
				entity.settId(dbTid);
				entity.setTime(dbTime);
				entity.setDataList(entities);
				allDbs.add(entity);
			}
		}
		MongoManager.close();
		return allDbs;
	}

	@Override
	public List<CanDataReportEntity> findReportCanData(List<Long> terminalIds,
													   long beginTime, long endTime, String month, int pageNum,
													   int pageSize) {
		List<CanDataReportEntity> allDbs = new ArrayList<CanDataReportEntity>();
		List<String> days = splitWithDay(beginTime, endTime);
		DBCollection collection = null;
		try {
			collection = MongoManager.start(LCMongo.DB.LC_GPS_CANDATA,LCMongo.Collection.LC_CANDATA_REPORT+month);
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
		BasicDBList dayList = new BasicDBList();
		for (String day : days) {
			dayList.add(day);
		}
		BasicDBObject allDays = new BasicDBObject("$in", dayList);
		DBObject filter = new BasicDBObject();
		filter.put("tId", allTids);
		filter.put("day", allDays);
		List<DBObject> objects = null;
		if(0==pageNum && 0==pageSize){
			objects = collection.find(filter).toArray();
		}else{
			int beginIndex = (pageNum-1)*pageSize;
			objects = collection.find(filter).skip(beginIndex).limit(pageSize).toArray();
		}
		if (null != objects && objects.size() > 0) {
			for(DBObject object : objects){
				CanDataReportEntity entity = new CanDataReportEntity();
				List<DBObject> datas = (List<DBObject>) object.get("dataList");
				long dbTid = (long)object.get("tId");
				String dayString = String.valueOf(object.get("day"));
				List<DACanDataDetail> entities = new ArrayList<DACanDataDetail>();
				if (null != datas) {
					for (DBObject dbObject : datas) {
						long detailTime = Long.parseLong(String.valueOf(dbObject.get("time")));
						if (detailTime >= beginTime && detailTime <= endTime) {
							DACanDataDetail detail = new DACanDataDetail(dbObject);
							entities.add(detail);
						}
					}
				}
				entity.settId(dbTid);
				entity.setDay(dayString);
				entity.setDataList(entities);
				allDbs.add(entity);
			}
		}
		MongoManager.close();
		return allDbs;
	}

}
