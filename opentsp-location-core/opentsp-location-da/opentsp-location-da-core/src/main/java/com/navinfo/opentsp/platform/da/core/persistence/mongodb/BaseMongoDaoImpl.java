package com.navinfo.opentsp.platform.da.core.persistence.mongodb;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.alibaba.fastjson.JSON;
import com.navinfo.opentsp.platform.da.core.common.Constant.PropertiesKey.AlarmTypeTableMapping;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbIndex;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCDbName;
import com.navinfo.opentsp.platform.da.core.persistence.common.LCTable;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.*;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACommonSummaryEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DAWFlow;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.*;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * MongoDB操作相关
 *
 * @author jin_s
 */
public class BaseMongoDaoImpl implements BaseMongoDao {

    private Logger logger = LoggerFactory.getLogger(BaseMongoDaoImpl.class);

    private final static boolean QUERY_LOG_ENABELD = false;

    /**
     * Collection检测（根据代码，所有collection均在数据库LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS中，建议修改方法名称明确含义——王景康）
     *
     * @param collectionName
     */
    public void exsitNotOrCreate(String collectionName) {
        try {
            // 获取统计分析数据库
            DB db = MongoManager.getDB(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS);
            // 判断是否存在集合collectionName
            boolean isExist = db.collectionExists(collectionName);
            if (!isExist) {
                DBCollection collection = db.getCollection(collectionName);
                DBObject keys = new BasicDBObject();
                if (collectionName.startsWith("MileageConsumption")) {
                    // 索引列表
                    keys.put("start", 1);
                    keys.put("tid", 1);
                    // 创建索引
                    collection.createIndex(keys);
                    logger.error("里程创建索引  start_1_tid_1");
                } else if (collectionName.startsWith("WFlow")) {
                    keys.put("AA", 1);
                    keys.put("DA", 1);
                    // 创建索引
                    collection.createIndex(keys);
                    logger.error("WFlow 创建索引  AA_1_DA_1");
                } else if (collectionName.startsWith("OvertimeParkingInArea")) {
                    keys.put("HA", 1);
                    keys.put("AA", 1);
                    keys.put("BA", 1);
                    collection.createIndex(keys);
                    logger.error("滞留超时 创建索引  HA_1_AA_1_BA_1");
                } else if (collectionName.startsWith("StaytimeParkingInArea")) {
                    keys.put("HA", 1);
                    keys.put("AA", 1);
                    keys.put("BA", 1);
                    collection.createIndex(keys);
                    logger.error("区域停留时间 创建索引  HA_1_AA_1_BA_1");
                } else if (collectionName.startsWith("VehicleDrivingNumberInGrid")) {
                    DBObject index1 = new BasicDBObject();
                    index1.put("month", 1);
                    index1.put("district", 1);
                    collection.createIndex(index1);
                    DBObject index2 = new BasicDBObject();
                    index2.put("month", 1);
                    index2.put("tile", 1);
                    collection.createIndex(index2);
                    logger.error("热力图  创建索引  month_1_district_1");
                    logger.error("热力图  创建索引  month_1_tile_1");
                } else if (collectionName.startsWith("VehicleDrivingNumberInArea")) {
                    keys.put("day", 1);
                    keys.put("district", 1);
                    collection.createIndex(keys);
                    logger.error("区域车次  创建索引  day_1_district_1");
                } else if (collectionName.startsWith("FaultCode")) {
                    keys.put("begin", 1);
                    keys.put("tId", 1);
                    keys.put("spn", 1);
                    keys.put("fmi", 1);
                    collection.createIndex(keys);
                    logger.error("故障码 创建索引  begin_1_tId_1_spn_fmi");
                } else if (collectionName.startsWith("StagnationTimeout")) {
                    keys.put("begin", 1);
                    keys.put("tId", 1);
                    collection.createIndex(keys);
                    logger.error("停滞超时 创建索引  begin_1_tId_1");
                } else {
                    // 索引列表
                    keys.put("AA", 1);
                    keys.put("HA", 1);
                    // 创建索引
                    collection.createIndex(keys);
                }
            } else {
                DBCollection collection = db.getCollection(collectionName);
                List<DBObject> obj = collection.getIndexInfo();
                boolean index = false;
                if (collectionName.startsWith("MileageConsumption")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("start_1_tid_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("里程未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("start", 1);
                        keys.put("tid", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("里程未发现指定索引创建，重建索引");
                    }
                } else if (collectionName.startsWith("WFlow")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("AA_1_DA_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("WFlow 未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("AA", 1);
                        keys.put("DA", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("WFlow 未发现指定索引创建，重建索引");
                    }
                } else if (collectionName.startsWith("OvertimeParkingInArea")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("HA_1_AA_1_BA_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("滞留超时 未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("HA", 1);
                        keys.put("AA", 1);
                        keys.put("BA", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("滞留超时 未发现指定索引创建，重建索引");
                    }
                } else if (collectionName.startsWith("VehicleDrivingNumberInGrid")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("month_1_district_1") == -1 && name.indexOf("month_1_tile_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("热力图 未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("month", 1);
                        keys.put("district", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("热力图 未发现指定索引创建，重建索引  month_1_district_1");
                        DBObject keys2 = new BasicDBObject();
                        keys2.put("month", 1);
                        keys2.put("tile", 1);
                        collection.createIndex(keys2, new BasicDBObject("background",
                                true));
                        logger.error("热力图 未发现指定索引创建，重建索引  month_1_tile_1");
                    }
                } else if (collectionName.startsWith("VehicleDrivingNumberInArea")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("day_1_district_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("区域车次 未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("day", 1);
                        keys.put("district", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("区域车次 未发现指定索引创建，重建索引");
                    }
                } else if (collectionName.startsWith("FaultCode")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("begin_1_tId_1_spn_1_fmi_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("故障码 未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("begin", 1);
                        keys.put("tId", 1);
                        keys.put("spn", 1);
                        keys.put("fmi", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("故障码 未发现指定索引创建，重建索引");
                    }
                } else if (collectionName.startsWith("StagnationTimeout")) {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("begin_1_tId_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                            logger.error("停滞超时 未发现指定索引创建，删除索引");
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("begin", 1);
                        keys.put("tId", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                        logger.error("停滞超时 未发现指定索引创建，重建索引");
                    }
                } else {
                    if (obj.size() == 1) {
                        index = true;
                    }
                    for (DBObject dbo : obj) {
                        String name = (String) dbo.get("name");
                        if (name.indexOf("_id_") != -1) {
                            continue;
                        }
                        if (name.indexOf("AA_1_HA_1") == -1) {
                            index = true;
                            collection.dropIndexes();
                        } else {
                            continue;
                        }
                    }
                    if (index) {
                        DBObject keys = new BasicDBObject();
                        keys.put("AA", 1);
                        keys.put("HA", 1);
                        collection.createIndex(keys, new BasicDBObject("background",
                                true));
                    }
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 查询指定条件的报警统计信息（同上，数据固定采用LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，建议修改方法名称明确其含义——王景康）
     *
     * @param collectionName
     * @param terminalIds
     * @param st
     * @param et
     * @return
     */

    public List<DACommonSummaryEntity> getTotalSummaryEntity(String collectionName, long[] terminalIds, long st, long et) {
        BasicDBList values = new BasicDBList();
        // 终端标识列表
        for (int i = 0; i < terminalIds.length; i++) {
            values.add(terminalIds[i]);
        }
        // 终端列表
        BasicDBObject in = new BasicDBObject("$in", values);

        // 首先利$match筛选出where条件
        BasicDBObject[] array = {new BasicDBObject("HA", new BasicDBObject("$gte", st)),
                new BasicDBObject("HA", new BasicDBObject("$lt", et))};

        BasicDBObject cond = new BasicDBObject();
        cond.put("$and", array);
        cond.put("AA", in);
        DBObject match = new BasicDBObject("$match", cond);

        // 利用$project拼装group需要的数据，包含optCode列、processTime列
        DBObject fields = new BasicDBObject("AA", 1); // 接口
        fields.put("KA", 1);
        fields.put("HA", 1);
        fields.put("IA", 1);
        DBObject project = new BasicDBObject("$project", fields);

        // $group终端编号利用进行分组
        DBObject _group = new BasicDBObject("AA", "$AA");

        // docId
        DBObject groupFields = new BasicDBObject("_id", _group);
        // 总数
        groupFields.put("count", new BasicDBObject("$sum", 1));
        // 报警累计时间求和
        groupFields.put("processTime_sum", new BasicDBObject("$sum", "$KA"));
        // 获取集合中所有文档对应值得最小值
        groupFields.put("startTime", new BasicDBObject("$min", "$HA"));
        // 获取集合中所有文档对应值得最大值
        groupFields.put("endTime", new BasicDBObject("$max", "$IA"));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AggregationOutput output = collection.aggregate(match, project, group);

        List<DBObject> results = (List<DBObject>) output.results();
        List<DACommonSummaryEntity> commonSummaryEntityList = new ArrayList<DACommonSummaryEntity>();
        for (DBObject o : results) {
            DBObject s = (DBObject) o.get("_id");
            DACommonSummaryEntity commonSummaryEntity = new DACommonSummaryEntity();
            commonSummaryEntity.setRecordsTotal((int) o.get("count"));
            commonSummaryEntity.setCalculatedAT((int) o.get("processTime_sum"));
            commonSummaryEntity.setStartTime((long) o.get("startTime"));
            commonSummaryEntity.setEndTime((long) o.get("endTime"));
            commonSummaryEntity.setTerminalID((long) s.get("AA"));
            commonSummaryEntityList.add(commonSummaryEntity);
        }
        return commonSummaryEntityList;
    }

    /**
     * 查询指定条件的报警统计信息（同上，建议修改方法名称明确其含义——王景康）
     *
     * @param collectionName
     * @param cond
     * @return
     */

    public List<DACommonSummaryEntity> getTotalSummaryEntity(String collectionName, BasicDBObject cond) {

        DBObject match = new BasicDBObject("$match", cond);

        // 利用$project拼装group需要的数据，包含optCode列、processTime列
        DBObject fields = new BasicDBObject("AA", 1); // 接口
        fields.put("KA", 1);
        fields.put("HA", 1);
        fields.put("IA", 1);
        DBObject project = new BasicDBObject("$project", fields);

        // $group终端编号利用进行分组
        DBObject _group = new BasicDBObject("AA", "$AA");

        // docId
        DBObject groupFields = new BasicDBObject("_id", _group);
        // 总数
        groupFields.put("count", new BasicDBObject("$sum", 1));
        // 报警累计时间求和
        groupFields.put("processTime_sum", new BasicDBObject("$sum", "$KA"));
        // 获取集合中所有文档对应值得最小值
        groupFields.put("startTime", new BasicDBObject("$min", "$HA"));
        // 获取集合中所有文档对应值得最大值
        groupFields.put("endTime", new BasicDBObject("$max", "$IA"));
        DBObject group = new BasicDBObject("$group", groupFields);
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        AggregationOutput output = collection.aggregate(match, project, group);

        List<DBObject> results = (List<DBObject>) output.results();
        List<DACommonSummaryEntity> commonSummaryEntityList = new ArrayList<DACommonSummaryEntity>();
        for (DBObject o : results) {
            DBObject s = (DBObject) o.get("_id");
            DACommonSummaryEntity commonSummaryEntity = new DACommonSummaryEntity();
            if (collectionName.startsWith(AlarmTypeTableMapping.AlarmTerminalOnOffLineStatus)) {
                commonSummaryEntity.setRecordsTotal((int) o.get("count") > 0 ? 1 : (int) o.get("count"));
            } else {
                commonSummaryEntity.setRecordsTotal((int) o.get("count"));
                commonSummaryEntity.setCalculatedAT((int) o.get("processTime_sum"));
            }

            commonSummaryEntity.setStartTime((long) o.get("startTime"));
            commonSummaryEntity.setEndTime((long) o.get("endTime"));
            commonSummaryEntity.setTerminalID((long) s.get("AA"));
            commonSummaryEntityList.add(commonSummaryEntity);
        }
        return commonSummaryEntityList;
    }

    /**
     * 查询指定条件的里程统计信息（无效方法？——王景康）
     *
     * @param collectionName
     * @param cond
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<MileageSummaryEntity> getTotalMilagesSummaryEntity(String collectionName, BasicDBObject cond,
                                                                   long beginTime, long endTime) {
        /*
		BaseMongoDaoImpl mongoService = new BaseMongoDaoImpl();
		List<DBObject> queryData = mongoService.queryByCondition(collectionName, cond);
		if (queryData != null) {
			List<MilagesEntity> returnList = new ArrayList<MilagesEntity>();
			for (DBObject opda : queryData) {
				MilagesEntity item = null;
				try {
					item = MilagesEntity.class.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				item.dbObjectToBean(opda);
				returnList.add(item);
			}
			Map<Long, List<MilagesEntity>> retMap = new HashMap<Long, List<MilagesEntity>>();
			for (MilagesEntity entity : returnList) {
				List<MilagesEntity> reList = (List<MilagesEntity>) retMap.get(entity.getTerminal_id());
				if (reList == null) {
					reList = new ArrayList<MilagesEntity>();
					retMap.put(entity.getTerminal_id(), reList);
				}
				reList.add(entity);
			}
			if (!retMap.isEmpty()) {
				List<MileageSummaryEntity> commonSummaryEntityList = new ArrayList<MileageSummaryEntity>();

				Set<Entry<Long, List<MilagesEntity>>> values = retMap.entrySet();
				for (Entry<Long, List<MilagesEntity>> milagesentry : values) {
					long terminalId = (long) milagesentry.getKey();
					List<MilagesEntity> entityList = (List<MilagesEntity>) milagesentry.getValue();

					MileageSummaryEntity value = null;

					for (MilagesEntity milagesEntity : entityList) {

						List<DAMilages> dataList = milagesEntity.getDataList();
						long dayStartTime = 0;
						long dayEndTime = 0;
						if (dataList != null) {
							for (int j = 0; j < dataList.size(); j++) {
								DAMilages milages = dataList.get(j);
								long beginDate = milages.getBeginDate();
								if (beginTime > 0) {
									if (beginDate < beginTime) {
										continue;
									}
								}
								if (dayStartTime == 0) {
									dayStartTime = milages.getBeginDate();
									dayEndTime = milages.getEndDate();
								}
								if (value == null) {
									value = new MileageSummaryEntity();
									value.setTerminalId(terminalId);
									value.setBeginDate(milages.getBeginDate());
									value.setBeginLat(milages.getBeginLat());
									value.setBeginLng(milages.getBeginLng());
									value.setBeginMileage(milages.getBeginMileage());
									value.setEndDate(milages.getEndDate());
									value.setEndLat(milages.getEndLat());
									value.setEndLNG(milages.getEndLNG());
									value.setEndMileage(milages.getEndMileage());
									value.setMileage(value.getMileage() + milages.getMileage());
									value.setOil(value.getOil());
								}
								if (endTime > 0) {
									if (beginDate > endTime) {
										break;
									}
								}

								dayEndTime = milages.getEndDate();
								value.setEndDate(milages.getEndDate());
								value.setEndLat(milages.getEndLat());
								value.setEndLNG(milages.getEndLNG());
								value.setEndMileage(milages.getEndMileage());
								value.setMileage(value.getMileage() + milages.getMileage());
							}

						}
						value.setTotalTime(value.getTotalTime() + (dayEndTime - dayStartTime));

					}
					commonSummaryEntityList.add(value);
				}
				return commonSummaryEntityList;
			}

		}
		*/
        return null;
    }

    /**
     * 查询指定条件的流量统计信息
     *
     * @param collectionName
     * @param cond
     * @param beginTime
     * @param endTime
     * @return
     */
    public List<WFSummary> getTotalWFlowSummaryEntity(String collectionName, BasicDBObject cond, long beginTime,
                                                      long endTime) {

        BaseMongoDaoImpl mongoService = new BaseMongoDaoImpl();
        // 使用了过时的方法，建议修改该行代码——王景康
        List<DBObject> queryData = mongoService.queryByCondition(collectionName, cond);
        if (queryData != null) {
            List<WFlowEntity> returnList = new ArrayList<WFlowEntity>();
            for (DBObject opda : queryData) {
                WFlowEntity item = null;
                try {
                    item = WFlowEntity.class.newInstance();
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                item.dbObjectToBean(opda);
                returnList.add(item);
            }
            Map<Long, List<WFlowEntity>> retMap = new HashMap<Long, List<WFlowEntity>>();
            for (WFlowEntity entity : returnList) {
                List<WFlowEntity> reList = (List<WFlowEntity>) retMap.get(entity.getTerminal_id());
                if (reList == null) {
                    reList = new ArrayList<WFlowEntity>();
                    retMap.put(entity.getTerminal_id(), reList);
                }
                reList.add(entity);
            }
            if (!retMap.isEmpty()) {
                List<WFSummary> commonSummaryEntityList = new ArrayList<WFSummary>();

                Set<Entry<Long, List<WFlowEntity>>> values = retMap.entrySet();
                for (Entry<Long, List<WFlowEntity>> flowEntry : values) {
                    long terminalId = (long) flowEntry.getKey();
                    List<WFlowEntity> entityList = (List<WFlowEntity>) flowEntry.getValue();
                    WFSummary commonSummaryEntity = null;

                    float totalDownFlow = 0;
                    float totalUpFlow = 0;
                    for (WFlowEntity entity : entityList) {
                        long day = entity.getDay();
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(day * 1000);
                        List<DAWFlow> dataList = entity.getDataList();
                        if (dataList != null) {

                            for (int j = 0; j < dataList.size(); j++) {
                                DAWFlow wFlow = dataList.get(j);
                                int flowDate = wFlow.getFlowDate();
                                calendar.set(Calendar.HOUR_OF_DAY, flowDate);
                                long currentFlowTime = calendar.getTimeInMillis() / 1000;
                                if (currentFlowTime < beginTime || currentFlowTime > endTime) {
                                    continue;
                                } else {
                                    if (null == commonSummaryEntity) {
                                        commonSummaryEntity = new WFSummary();
                                        commonSummaryEntity.setTerminalId(terminalId);
                                        commonSummaryEntity.setBeginDate(currentFlowTime);
                                        commonSummaryEntity.setEndDate(currentFlowTime);
                                    }
                                    totalDownFlow += wFlow.getDownFlow();
                                    totalUpFlow += wFlow.getUpFlow();

                                    commonSummaryEntity.setEndDate(currentFlowTime);
                                }
                            }
                        }
                    }
                    if (commonSummaryEntity != null) {
                        commonSummaryEntity.setDownFlow(totalDownFlow);
                        commonSummaryEntity.setUpFlow(totalUpFlow);
                        if (commonSummaryEntity.getBeginDate() == 0) {
                            commonSummaryEntity.setBeginDate(beginTime);
                        }
                        if (commonSummaryEntity.getEndDate() == 0) {
                            commonSummaryEntity.setEndDate(endTime);
                        }

                        commonSummaryEntityList.add(commonSummaryEntity);
                    }

                }
                return commonSummaryEntityList;
            }
        }
        return null;
    }

    /**
     * 统计报警持续时间（并不是任意的Collection均包含代码内要求的查询字段如AA、HA等，方法定义有问题，建议明确——王景康）
     *
     * @param collection
     * @param terminalId
     * @param st
     * @param et
     * @return
     * @throws UnknownHostException
     */
    public static long findNewMessage(DBCollection collection, long terminalId, long st, long et)
            throws UnknownHostException {
        Long total = 0l;
        String reduce = "function(doc, aggr){" + "            aggr.total += doc.KA;" + "        }";
        DBObject query = new BasicDBObject();
        query.put("AA", terminalId);
        query.put("HA", new BasicDBObject("$gte", st).append("$lte", et));
        DBObject result = collection
                .group(new BasicDBObject("AA", 1), query, new BasicDBObject("total", total), reduce);

        Map<String, BasicDBObject> map = result.toMap();
        // System.out.println(map.size());
        if (map.size() > 0) {
            BasicDBObject bdbo = map.get("0");
            if (bdbo != null && bdbo.get("total") != null)
                total = bdbo.getLong("total");
        }

        // System.out.println(total);
        return total;

    }

    /**
     * 查询指定条件的报警统计信息（同上，固定查询LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，建议修改方法名称，明确含义——王景康）
     *
     * @param collectionName
     * @param terminalId
     * @param st
     * @param et
     * @return
     */
    public DACommonSummaryEntity getSummaryByCondition(String collectionName, long terminalId, long st, long et) {

        DBObject query = new BasicDBObject();
        query.put("AA", terminalId);
        query.put("HA", new BasicDBObject("$gte", st).append("$lt", et));
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        DACommonSummaryEntity commonSummaryEntity = new DACommonSummaryEntity();
        DBCursor cursor = collection.find(new BasicDBObject(), query);
        int count = cursor.count();
        long calculatedAT = 0;
        try {
            calculatedAT = findNewMessage(collection, terminalId, st, et);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        if (count > 0) {

            for (int i = 0; i < count; i++) {
                DBObject dob = cursor.next();
                if (i == 0) {
                    commonSummaryEntity.setStartTime((long) dob.get("HA"));
                }
                if (i == count - 1) {
                    commonSummaryEntity.setEndTime((long) dob.get("IA"));
                }
                calculatedAT += (int) dob.get("KA");
            }
        }

        commonSummaryEntity.setRecordsTotal(count);
        commonSummaryEntity.setTerminalID(terminalId);
        commonSummaryEntity.setCalculatedAT((int) calculatedAT);
        return commonSummaryEntity;

    }

    /**
     * 查询指定条件的报警数量（同上，固定查询LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，建议修改方法名称，明确含义——王景康）
     *
     * @param collectionName
     * @param terminalIds
     * @param st
     * @param et
     * @return
     */
    public int getCountByCondition(String collectionName, long[] terminalIds, long st, long et) {

        BasicDBList values = new BasicDBList();
        for (int i = 0; i < terminalIds.length; i++) {
            values.add(terminalIds[i]);
        }
        BasicDBObject in = new BasicDBObject("$in", values);
        DBObject query = new BasicDBObject();
        query.put("HA", new BasicDBObject("$gte", st).append("$lt", et));
        query.put("AA", in);
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return collection.find(new BasicDBObject(), query).count();

    }

    /**
     * 查询指定条件的报警数量（该方法意图定义为一个通用基础方法，但是固定的使用数据库LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，因此不符合方法命名规范，建议删除——王景康）
     *
     * @param collectionName
     * @param query
     * @return
     */
    @Deprecated
    public int queryCountByCondition(String collectionName, DBObject query) {
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return collection.find(query).count();
    }

    /**
     * 查询指定条件的结果总数——王景康
     *
     * @param collection
     * @param query
     * @return
     */
    public int queryCountByCondition(DBCollection collection, DBObject query) {
        long time = System.currentTimeMillis();
        int count = (int) collection.count(query);
        if (QUERY_LOG_ENABELD) {
            long cost = System.currentTimeMillis() - time;
            System.out.println("collection: " + collection + ", query: " + query + ",查询总数耗时(ms): " + cost);
        }
        return count;
    }

    /**
     * 查询轨迹数量
     *
     * @param collectionName
     * @param terminalIds
     * @param st
     * @param et
     * @return
     */
    public int queryGpsCountByCondition(String collectionName, DBObject query) {
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_GPS_LOCATION, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return collection.find(new BasicDBObject(), query).count();
    }

    /**
     * 查询统计分析结果集（该方法意图定义为一个通用基础方法，但是固定的使用数据库LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，因此不符合方法命名规范，建议删除——王景康）
     *
     * @param collectionName
     * @param terminal_id
     * @param st
     * @param et
     * @return
     */
    @Deprecated
    public List<DBObject> queryByCondition(String collectionName, long terminal_id, long st, long et) {
        DBObject query = new BasicDBObject();
        query.put("AA", terminal_id);
        query.put("HA", new BasicDBObject("$gte", st).append("$lte", et));
        // BasicDBList list = new BasicDBList();
        // list.add(new BasicDBObject("status",new BasicDBObject("$gte",0)));
        // list.add(new BasicDBObject("conType", new BasicDBObject("$gt",0)));
        // query.put("$or",list);
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return collection.find(query).toArray();
    }

    /**
     * 查询统计分析结果集（该方法意图定义为一个通用基础方法，但是固定的使用数据库LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，因此不符合方法命名规范，建议删除——王景康）
     *
     * @param collectionName
     * @param query
     * @return
     */
    @Deprecated
    public List<DBObject> queryByCondition(String collectionName, DBObject query) {

        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        DBCursor find = collection.find(query);
        List<DBObject> array = find.toArray();

        return array;
    }

    /**
     * 查询统计分析结果集并排序（该方法意图定义为一个通用基础方法，但是固定的使用数据库LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS，因此不符合方法命名规范，建议删除——王景康）
     *
     * @param collectionName
     * @param query
     * @param orderBy
     * @return
     */
    @Deprecated
    public List<DBObject> queryByCondition(String collectionName, DBObject query, DBObject orderBy) {
        DBCollection collection = null;
        try {
            collection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS, collectionName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 2016年5月25日修改
        return this.queryByCondition(collection, query, orderBy, -1);
    }

    /**
     * 查询结果集并排序，2016年5月25日增加——王景康
     *
     * @param collection
     * @param query
     * @param orderBy
     * @param limit
     * @return
     */
    public List<DBObject> queryByCondition(DBCollection collection, DBObject query, DBObject orderBy, int limit) {
        long time = System.currentTimeMillis();
        DBCursor find = collection.find(query);
        if (limit > 0) {
            find.limit(limit);
        }
        if (orderBy != null) {
            find = find.sort(orderBy);
        }
        List<DBObject> array = find.toArray();
        if (QUERY_LOG_ENABELD) {
            long cost = System.currentTimeMillis() - time;
            System.out.println("collection: " + collection + ", query: " + query + ",查询耗时(ms): " + cost);
        }
        return array;
    }

    /**
     * 分页优化，2016年9月23日增加——hxw
     *
     * @param collection
     * @param query
     * @param orderBy
     * @param limit
     * @return
     */
    public List<DBObject> queryLimitByCondition(DBCollection collection, DBObject query, DBObject orderBy, int skip, int limit) {
        long time = System.currentTimeMillis();
        DBCursor find = collection.find(query);
        if (orderBy != null) {
            find = find.sort(orderBy);
        }
        if (limit > 0) {
            find.limit(limit);
        }
        if (skip > 0) {
            find.skip(skip);
        }
        List<DBObject> array = find.toArray();
        if (QUERY_LOG_ENABELD) {
            long cost = System.currentTimeMillis() - time;
            System.out.println("collection: " + collection + ", query: " + query + ",查询耗时(ms): " + cost);
        }
        return array;
    }

    /**
     * 查询指定对象结果集并排序，2016年5月25日增加——王景康
     *
     * @param targetEntityTemplate
     * @param query
     * @param orderBy
     * @param limit
     * @return
     */
    public List<DBObject> findDBObjects(BaseMongoEntity targetEntityTemplate, DBObject query, DBObject orderBy, int limit) {
        DBCollection collection = getCollection(targetEntityTemplate);
        return this.queryByCondition(collection, query, orderBy, limit);
    }

    /**
     * 添加索引
     *
     * @param baseMongoEntity
     * @param collection
     */
    public void ensureIndex(BaseMongo baseMongoEntity, DBCollection collection) {
        // 索引列表
        String[] lcDbIndex = null;
        LCDbIndex lcIndex = baseMongoEntity.getClass().getAnnotation(LCDbIndex.class);

        if (lcIndex != null) {
            lcDbIndex = lcIndex.name();

            if (lcDbIndex != null && lcDbIndex.length != 0) {
                DBObject keys = new BasicDBObject();
                for (String indexKey : lcDbIndex) {
                    keys.put(indexKey, 1);
                }
                collection.createIndex(keys, new BasicDBObject("background", true));
            }
        }
    }

    /**
     * 保存数据文档
     *
     * @param baseMongoEntity
     */
    public void save(BaseMongo baseMongoEntity) {
        DBCollection collection = getCollection(baseMongoEntity);
        collection.save(baseMongoEntity.toDBObject());

        MongoManager.close();
    }

    /**
     * 批量保存数据文档
     *
     * @param baseMongoEntity
     */
    public void saveForBatch(BaseMongoEntity baseMongoEntity) {
         DBCollection collection = getCollection(baseMongoEntity);
        collection.insert(baseMongoEntity.toBatchDBObject());
        MongoManager.close();
    }

    /**
     * 批量保存数据文档
     * <p>
     * 不建议使用，有类型与Collection错配风险，除非调用者可以保证入参数组的对象类型完全一致——王景康，2016.05.26
     *
     * @param dbObjects
     */
    @Deprecated
    public void bacthSave(List<BaseMongoEntity> baseMongoEntitys) {
        DBCollection collection = getCollection(baseMongoEntitys.get(0));
        List<DBObject> dbObjects = new ArrayList<DBObject>();
        for (BaseMongoEntity entity : baseMongoEntitys) {
            dbObjects.add(entity.toDBObject());
        }
        collection.insert(dbObjects);
        MongoManager.close();
    }

    public DBObject findByObjectId(ObjectId objectId) {
        DBObject dbObject = new BasicDBObject("_id", objectId);
        DBCollection collection = MongoManager.get();
        return this.wrap(collection.find(dbObject));
    }

    public List<DBObject> findByField(String fieldName, Object value) {
        DBObject dbObject = new BasicDBObject(fieldName, value);
        DBCollection collection = MongoManager.get();
        return this.wraps(collection.find(dbObject));
    }

    protected DBObject wrap(DBCursor cursor) {
        if (cursor.hasNext())
            return (DBObject) cursor.next();
        return null;
    }

    protected List<DBObject> wraps(DBCursor cursor) {
        if (cursor.hasNext()) {
            List<DBObject> array = cursor.toArray();
            return array;
        }
        return null;
    }

    public void uploadGridfs(String fileCode, byte[] content) {
        GridFS myFS = new GridFS(null, "");
        GridFSInputFile gridFSInputFile = myFS.createFile(content);
        gridFSInputFile.setFilename(fileCode);
        gridFSInputFile.save();
    }

    private DBCollection getCollection(BaseMongo baseMongoEntity) {
        // 数据库名称
        String lcDbName = null;
        LCDbName lcDb = baseMongoEntity.getClass().getAnnotation(LCDbName.class);

        if (lcDb != null) {
            lcDbName = lcDb.name();
        }
        // 集合名称
        LCTable lcTable = baseMongoEntity.getClass().getAnnotation(LCTable.class);
        String tableName = null;
        if (lcTable != null) {
            tableName = lcTable.name();
        }
        long day = baseMongoEntity.getDay();
        if (day != 0) {
            Date date = new Date((long) day * 1000);
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
            String dateString = formatter.format(date);
            if (dateString.length() == 6) {
                dateString = String.valueOf(dateString).substring(0, 4);
            } else if (dateString.length() == 8) {
                dateString = String.valueOf(dateString).substring(2, 6);
            } else {
            }

            tableName = tableName + "_" + dateString;
        }
        // System.out.println("DB: " + lcDbName + ", table: " + tableName);
        DBCollection collection = null;
        try {
            collection = MongoManager.start(lcDbName, tableName);
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return collection;
    }

    /**
     * 无效方法？——王景康
     *
     * @param collectionName
     * @param cond
     * @param beginTime
     * @param endTime
     * @return
     */
    public Map<Long, Integer> getOilConsumptionData(String collectionName, BasicDBObject cond, long beginTime,
                                                    long endTime) {
		/*
		BaseMongoDaoImpl mongoService = new BaseMongoDaoImpl();
		List<DBObject> queryData = mongoService.queryByCondition(collectionName, cond);
		if (queryData != null) {
			Map<Long, Integer> map = new HashMap<Long, Integer>();
			for (DBObject opda : queryData) {
				MilagesEntity item = null;
				try {
					item = MilagesEntity.class.newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					e.printStackTrace();
				}
				item.dbObjectToBean(opda);
				long terminal_id = item.getTerminal_id();
				int oil = item.getOil();
				map.put(terminal_id, oil);
			}
			return map;
		}
		*/
        return null;
    }

    @Override
    public void updateOverTimeEntity(BaseMongoEntity baseMongoEntity, List<OvertimeParkAlarm> list) {
        DBCollection collection = getCollection(baseMongoEntity);
        for (OvertimeParkAlarm overTimeParkAlarm : list) {
            collection.update(new BasicDBObject("_id", new ObjectId(overTimeParkAlarm.get_id())),
                    new BasicDBObject("$set",
                            new BasicDBObject().append("IA", overTimeParkAlarm.getEndDate())
                                    .append("YA", overTimeParkAlarm.getEndLat())
                                    .append("ZA", overTimeParkAlarm.getEndLng())
                                    .append("KA", overTimeParkAlarm.getContinuousTime())
                                    .append("tailMerge", overTimeParkAlarm.getTailMerge())), false, false);
        }
        MongoManager.close();
    }

    @Override
    public void updateStayTimeEntity(BaseMongoEntity baseMongoEntity, List<StaytimeParkAlarm> list) {
        DBCollection collection = getCollection(baseMongoEntity);
        for (StaytimeParkAlarm stayTimeParkAlarm : list) {
            collection.update(new BasicDBObject("_id", new ObjectId(stayTimeParkAlarm.get_id())),
                    new BasicDBObject("$set",
                            new BasicDBObject().append("IA", stayTimeParkAlarm.getEndDate())
                                    .append("YA", stayTimeParkAlarm.getEndLat())
                                    .append("ZA", stayTimeParkAlarm.getEndLng())
                                    .append("KA", stayTimeParkAlarm.getContinuousTime())
                                    .append("tailMerge", stayTimeParkAlarm.getTailMerge())), false, false);
        }
        MongoManager.close();
    }

    @Override
    public void updateFaultCodeEntity(FaultCodeDaAlarmEntity entity, List<FaultCodeEntity> list) {
        DBCollection collection = getCollection(entity);
        for (FaultCodeEntity faultCodeEntity : list) {
            collection.update(
                    new BasicDBObject().append("_id", new ObjectId(faultCodeEntity.get_id())),
                    new BasicDBObject().append(
                            "$set",
                            new BasicDBObject().append("end", faultCodeEntity.getEndDate())
                                    .append("con", faultCodeEntity.getContinueTime())
                                    .append("eLat", faultCodeEntity.getEndLat())
                                    .append("eLng", faultCodeEntity.getEndLng())));

        }
        MongoManager.close();
    }

    @Override
    public void updateStagnationTimeoutEntity(
            StagnationTimeoutAlarmEntity entity,
            List<StagnationTimeoutEntity> StagnationTimeoutAlarmList) {
        DBCollection collection = getCollection(entity);
        for (StagnationTimeoutEntity staEntity : StagnationTimeoutAlarmList) {
            collection.update(
                    new BasicDBObject().append("_id", new ObjectId(staEntity.get_id())),
                    new BasicDBObject().append(
                            "$set",
                            new BasicDBObject().append("end", staEntity.getEndDate())
                                    .append("con", staEntity.getContinuousTime())
                                    .append("st", staEntity.getStatus())
                                    .append("tail", staEntity.getTailMerge())));
        }
        MongoManager.close();
    }

    /**
     * 停滞超时撤销和恢复
     */
    @Override
    public void updateStagnationTimeoutCancelOrNot(String _id,
                                                   boolean isCancel, long recordDate) {
        Calendar calendar = DateUtils.calendar(recordDate);
        int mon = calendar.get(Calendar.MONTH) + 1;
        String month = "";
        if (mon < 10) {
            month = "0" + mon;
        } else {
            month = "" + mon;
        }
        String s = calendar.get(Calendar.YEAR) + month;
        if (s.length() == 6) {
            s = s.substring(2);
            logger.info("停滞超时撤销和恢复：" + _id + " 日期：" + s);
            DBCollection dbCollection = null;
            try {
                dbCollection = MongoManager.start(LCMongo.DB.LC_DATA_STATISTIC_ANALYSIS,
                        "StagnationTimeout_" + s);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
            int status = 0;
            if (isCancel) {
                status = 0;
            } else {
                status = 1;
            }
            dbCollection.update(
                    new BasicDBObject().append("_id", new ObjectId(_id)),
                    new BasicDBObject().append(
                            "$set",
                            new BasicDBObject().append("st", status)));

            MongoManager.close();
        } else {
            logger.error("停滞超时撤销和恢复：" + _id + "时解析日期出错：" + recordDate + "---" + s);
        }
    }
}
