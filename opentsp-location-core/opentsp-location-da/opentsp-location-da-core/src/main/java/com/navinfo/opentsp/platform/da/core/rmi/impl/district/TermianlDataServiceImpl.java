package com.navinfo.opentsp.platform.da.core.rmi.impl.district;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.platform.da.core.cache.alarm.AlarmQueryManager;
import com.navinfo.opentsp.platform.da.core.cache.alarm.TerminalOnOffDataCache;
import com.navinfo.opentsp.platform.da.core.common.Constant;
import com.navinfo.opentsp.platform.da.core.common.schedule.ScheduleController;
import com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import com.navinfo.opentsp.platform.da.core.persistence.application.DataStatisticsServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.application.DateDiffUtil;
import com.navinfo.opentsp.platform.da.core.persistence.application.TermianlDynamicManageImpl;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.CanDataReportEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACanDataDetail;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.dsa.alarm.DACommonSummaryEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcTerminalInfoDBInfo;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.CanDataMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.ReportQueryService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.CanDataMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.ReportQueryServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcTerminalInfoDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcTerminalInfoDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisConstans;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatus;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.TerminalOnOffStatusServiceImpl;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils.DateFormat;
import com.navinfo.opentsp.platform.location.protocol.common.LCLastestTerminalStatus.LastestTerminalStatus;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCLastestLocationDataRes.LastestLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCMileageAndOilDataRes.MileageAndOilData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRecieveDateSave;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.terminal.LCTerminalRecieveDateSave.TerminalRecieveDateSave;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.CommonParameter;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.dsa.TerminalOnlinePercentage;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDataEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.GpsDetailedEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.LatestParkEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.Point;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.TermianlDataService;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBusDataRecords.CANBusDataRecords;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalFirstRecieveDateRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalFirstRecieveDateRes.GetTerminalFirstRecieveDateRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalInAreaRes.GetTerminalInAreaRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalInDistrictRes.GetTerminalInDistrictRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCGetTerminalLocationDataRes.GetTerminalLocationDataRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLastestLocationDataByAlarmFilterRess.LastestLocationDataByAlarmFilterRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLastestTerminalStatusRes.LastestTerminalStatusRes;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.LCLatestParkRecoreds;
import com.navinfo.opentsp.platform.location.protocol.webservice.newstatisticsdata.entity.LCLatestPark;
import com.navinfo.opentsp.platform.location.protocol.webservice.originaldata.LCTerminalLocationData.TerminalLocationData;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCCrossAreaCounts.CrossAreaCounts;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCCrossRecord.CrossRecord;
import com.navinfo.opentsp.platform.location.protocol.webservice.statisticsdata.entity.LCStaytimePark;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.rmi.RemoteException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;
import java.util.Map.Entry;

/**
 * DSA统计计算位置数据相关服务
 *
 * @author jin_s
 */
@Service
public class TermianlDataServiceImpl implements TermianlDataService {
    // private static int GPS_INTERVAL_TIME;//GPS上报间隔时间 单位:秒(s)
    // private static int MAX_SPEED;//最大行驶速度 km/h
    // private static int ROAD_WIDTH;//路宽 m
    // private DictManage dictM = null;
    /**
     * 版本号
     */
    private static final long serialVersionUID = 1L;
    private Logger log = LoggerFactory.getLogger(TermianlDataServiceImpl.class);

    public TermianlDataServiceImpl() throws RemoteException {
        super();

    }

    /**
     * 查询某终端的历史位置数据
     *
     * @param terminalId 终端标识
     * @param beginTime  开始时间
     * @param endTime    结束时间
     * @return List<GpsDetailedEntity> 数据集合
     */
    public GpsDataEntity queryGpsData(long terminalId, long beginTime, long endTime) {
        GpsDataEntity gpsDataEntity = null;
        TermianlDynamicManage service = new TermianlDynamicManageImpl();
        List<GpsDetailedEntityDB> dataList = service.queryGpsData(terminalId, beginTime, endTime);
        gpsDataEntity = new GpsDataEntity();
        gpsDataEntity.settId(terminalId);
        for (GpsDetailedEntityDB item : dataList) {
            GpsDetailedEntity proItem = new GpsDetailedEntity();
            proItem.set_id(item.get_id());
            proItem.setGpsTime(item.getGpsTime());
            proItem.setData(item.getData());
            gpsDataEntity.addGpsDetailed(proItem);
        }
        return gpsDataEntity;
    }

    /**
     * 查询给定终端列表的历史位置数据
     *
     * @param terminalIds 终端标识
     * @param beginTime   开始时间
     * @param endTime     结束时间
     * @return List<GpsDetailedEntity> 数据集合
     */
    @Deprecated
    public List<GpsDataEntity> queryGpsDataForTids(long[] terminalIds, long beginTime, long endTime) {
        log.info("DSA->DA st:" + beginTime + " et:" + endTime);
        List<GpsDataEntity> result = new ArrayList<>();
        //
        for (long tid : terminalIds) {
            result.add(queryGpsData(tid, beginTime, endTime));
        }
        return result;
    }

    /**
     * 检索车辆在指定的时间段是否经过区域，检索后返回车辆轨迹的第一个点和最后一个点
     *
     * @param terminalIds
     * @param beginTime
     * @param endTime
     * @param leftLongitude
     * @param rightLongitude
     * @param topLatitude
     * @param bottomLatitude
     * @return
     */

    @Deprecated
    @Override
    public Map<Long, GpsDataEntity> getCrossAreaTrack(List<Long> terminalIds, long beginTime, long endTime,
                                                      int leftLongitude, int rightLongitude, int topLatitude, int bottomLatitude) {
        log.info("DSA->DA st:" + beginTime + " et:" + endTime);
        Map<Long, GpsDataEntity> resultMap = new HashMap<Long, GpsDataEntity>();

        for (long terminalId : terminalIds) {
            // 查询 每一个终端在 此时间范围内 的 GPS数据
            GpsDataEntity queryGpsData = queryGpsData(terminalId, beginTime, endTime);
            List<GpsDetailedEntity> dataList = queryGpsData.getDataList();

            List<GpsDetailedEntity> resultDataList = new ArrayList<>();
            try {

                for (GpsDetailedEntity gpsData : dataList) {
                    byte[] data = gpsData.getData();
                    LCLocationData.LocationData location = LCLocationData.LocationData.parseFrom(data);
                    int latitude = location.getLatitude();
                    int longitude = location.getLongitude();

                    if (longitude >= leftLongitude && longitude <= rightLongitude && latitude >= bottomLatitude
                            && latitude <= topLatitude) {
                        resultDataList.add(gpsData);
                    }
                }
                if (resultDataList.size() > 0) {
                    List<GpsDetailedEntity> areaTrackDataList = new ArrayList<>(
                            2);
                    areaTrackDataList.add(resultDataList.get(0));
                    areaTrackDataList.add(resultDataList.get(resultDataList.size() - 1));
                    queryGpsData.setDataList(areaTrackDataList);
                    resultMap.put(terminalId, queryGpsData);
                }
            } catch (InvalidProtocolBufferException e) {
                e.printStackTrace();
            }

        }
        if (!resultMap.isEmpty()) {
            return resultMap;
        }

        return null;
    }

    private GpsDetailedEntity changeObject(GpsDetailedEntityDB item) {
        GpsDetailedEntity proItem = new GpsDetailedEntity();
        proItem.set_id(item.get_id());
        proItem.setGpsTime(item.getGpsTime());
        proItem.setData(item.getData());
        return proItem;
    }

    @Deprecated
    @Override
    public GpsDataEntity queryGpsData(List<Long> terminalIds, long beginTime, long endTime, boolean isFilterAlarm,
                                      boolean isAllAlarm, long alarms, boolean isFilterBreakdown, long breakdownCode,
                                      CommonParameter comParameter, String queryKey) {
        log.info("DSA->DA st:" + beginTime + " et:" + endTime + " tid:" + terminalIds + " isFilterAlarm:"
                + isFilterAlarm + " isAllAlarm:" + isAllAlarm + " alarms:" + alarms + " isFilterBreakdown:"
                + isFilterBreakdown + " breakdownCode:" + breakdownCode + "分页：" + comParameter.isMultipage());
        try {
            TermianlDynamicManage service = new TermianlDynamicManageImpl();
            List<GpsDetailedEntity> dataList = new ArrayList<GpsDetailedEntity>();
            Map<String, List<GpsDetailedEntityDB>> gpsMap = service.queryGpsData(terminalIds, beginTime, endTime);
            // 简单写法，性能非最优
            for (Map.Entry<String, List<GpsDetailedEntityDB>> entity : gpsMap.entrySet()) {
                for (GpsDetailedEntityDB dbEntity : entity.getValue()) {
                    if (dbEntity.getData() == null) {
                        continue;
                    }
                    if (isFilterBreakdown) {
                        LocationData locationData = LocationData.parseFrom(dbEntity.getData());
                        if (locationData.getBreakdownAddition() == null
                                || locationData.getBreakdownAddition().getBreakdownList().size() == 0) {
                            continue;
                        }
                        if (breakdownCode == 0) {
                            dataList.add(changeObject(dbEntity));
                        } else {
                            List<VehicleBreakdown> list = locationData.getBreakdownAddition().getBreakdownList();
                            for (VehicleBreakdown vehicleBreakdown : list) {
                                if (vehicleBreakdown.getBreakdownSPNValue() == breakdownCode / 1000
                                        && vehicleBreakdown.getBreakdownFMIValue() == breakdownCode % 1000) {
                                    dataList.add(changeObject(dbEntity));
                                }
                            }
                        }
                    } else {
                        dataList.add(changeObject(dbEntity));
                    }
                }
            }
            GpsDataEntity gpsDataEntity = new GpsDataEntity();
            if (null != dataList) {
                gpsDataEntity.addAllGpsDetailed(dataList);
            }
            return gpsDataEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*
         * List<GpsDetailedEntityDB> dataList = new
		 * ArrayList<GpsDetailedEntityDB>(); if (comParameter.isMultipage()) {
		 * //分页 // 根据鉴权码获取概要缓存对象 GpsDataCache instance = (GpsDataCache)
		 * AlarmQueryManager.getGpsDataCacheByKey(queryKey);
		 *
		 * // 构造返回结果对象 if (instance == null) { instance = new GpsDataCache();
		 * boolean initCache = instance.queryGpsData(terminalIds, beginTime,
		 * endTime, isFilterAlarm, isAllAlarm,
		 * alarms,isFilterBreakdown,breakdownCode); if (initCache) {
		 * AlarmQueryManager.putGpsData(queryKey, instance); dataList =
		 * instance.getGpsData(terminalIds, beginTime, endTime, isFilterAlarm,
		 * isAllAlarm, alarms, comParameter, queryKey); } } else { dataList =
		 * instance.getGpsData(terminalIds, beginTime, endTime, isFilterAlarm,
		 * isAllAlarm, alarms, comParameter, queryKey); } GpsDataEntity
		 * gpsDataEntity = new GpsDataEntity(); if (instance != null) {
		 * gpsDataEntity.setTotal(instance.totalRecords); }
		 *
		 * if (null != dataList) { for (GpsDetailedEntityDB item : dataList) {
		 * GpsDetailedEntity proItem = new GpsDetailedEntity();
		 * proItem.set_id(item.get_id()); proItem.setGpsTime(item.getGpsTime());
		 * proItem.setData(item.getData());
		 * gpsDataEntity.addGpsDetailed(proItem); } } return gpsDataEntity; }
		 * else { //不分页 TermianlDynamicManage service = new
		 * TermianlDynamicManageImpl(); for (long terminalId : terminalIds) {
		 * List<GpsDetailedEntityDB> terminalDataList = service
		 * .queryGpsData(terminalId, beginTime, endTime); if (terminalDataList
		 * != null && terminalDataList.size() > 0) {
		 * dataList.addAll(terminalDataList); } }
		 *
		 * GpsDataEntity gpsDataEntity = new GpsDataEntity(); if (null !=
		 * dataList) { for (GpsDetailedEntityDB item : dataList) {
		 * GpsDetailedEntity proItem = new GpsDetailedEntity();
		 * proItem.set_id(item.get_id()); proItem.setGpsTime(item.getGpsTime());
		 * proItem.setData(item.getData());
		 * gpsDataEntity.addGpsDetailed(proItem); } }
		 *
		 * return gpsDataEntity; }
		 */
        return null;
    }

    @Override
    public List<TerminalOnlinePercentage> getTerminalOnlinePercentage(List<Long> terminalIds, long beginTime,
                                                                      long endTime, CommonParameter commonParameter) {
        log.info("DSA->DA st:" + beginTime + " et:" + endTime);
        Set<String> statisticDay = DateDiffUtil.getDatesDiff(beginTime, endTime);
        List<TerminalOnlinePercentage> terminalOnlinePercentageList = new ArrayList<TerminalOnlinePercentage>();
        List<DACommonSummaryEntity> temps = null;
        if (commonParameter.isMultipage()) {
            // 根据鉴权码获取概要缓存对象
            TerminalOnOffDataCache instance = (TerminalOnOffDataCache) AlarmQueryManager
                    .getTerminalOnOffDataCacheByKey(commonParameter.getQueryKey());

            // 构造返回结果对象

            if (instance == null) {
                instance = new TerminalOnOffDataCache(terminalIds, commonParameter.getQueryKey());
                boolean initCache = instance.queryTerminalOnlinePercentageData(terminalIds, beginTime, endTime,
                        statisticDay);
                if (initCache) {
                    AlarmQueryManager.putTerminalOnlineData(commonParameter.getQueryKey(), instance);
                    terminalOnlinePercentageList = instance.getTerminalOnlinePercentageData(terminalIds, beginTime,
                            endTime, commonParameter, commonParameter.getQueryKey());
                }
            } else {
                terminalOnlinePercentageList = instance.getTerminalOnlinePercentageData(terminalIds, beginTime, endTime,
                        commonParameter, commonParameter.getQueryKey());
            }
            if (terminalOnlinePercentageList != null && terminalOnlinePercentageList.size() > 0) {
                List<TerminalOnlinePercentage> returnList = new ArrayList<TerminalOnlinePercentage>();
                for (TerminalOnlinePercentage entity : terminalOnlinePercentageList) {
                    returnList.add(entity);
                }
                return returnList;
            }

        } else {
            DataStatisticsServiceImpl service = new DataStatisticsServiceImpl();
            try {
                temps = service.queryAlarmSummaryDataForTerminalOnOff(terminalIds,
                        Constant.PropertiesKey.AlarmTypeTableMapping.AlarmTerminalOnOffLineStatus, statisticDay, 1);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            Map<Long, DACommonSummaryEntity> dataMap = new HashMap<Long, DACommonSummaryEntity>();

            for (DACommonSummaryEntity summary : temps) {
                dataMap.put(summary.getTerminalID(), summary);
            }
            NumberFormat nf = NumberFormat.getNumberInstance();
            nf.setMaximumFractionDigits(4);

            for (long terminalId : terminalIds) {
                TerminalOnlinePercentage terminalOnlinePercentage = new TerminalOnlinePercentage();
                terminalOnlinePercentage.setTerminalId(terminalId);
                terminalOnlinePercentage.setStatisticDay(statisticDay.size());
                DACommonSummaryEntity daCommonSummaryEntity = dataMap.get(terminalId);
                if (daCommonSummaryEntity != null) {
                    int recordsTotal = daCommonSummaryEntity.getRecordsTotal();
                    terminalOnlinePercentage.setOnlineDay(recordsTotal);
                    float percentage = (float) recordsTotal / statisticDay.size();
                    terminalOnlinePercentage.setOnlinePercentage(Float.parseFloat(nf.format(percentage)));
                } else {
                    terminalOnlinePercentage.setOnlineDay(0);
                    terminalOnlinePercentage.setOnlinePercentage(0);
                }
                terminalOnlinePercentageList.add(terminalOnlinePercentage);
            }
            return terminalOnlinePercentageList;

        }

        return null;
    }

    // private void initDictValue() {
    // if(MAX_SPEED == 0) {
    // if(dictM == null) {
    // dictM = new DictManageImpl();
    // }
    // MAX_SPEED =
    // Integer.parseInt(dictM.getDictByCode(210003).getDict_data());//区域内最大行驶速度
    // }
    // if(ROAD_WIDTH == 0) {
    // if(dictM == null) {
    // dictM = new DictManageImpl();
    // }
    // ROAD_WIDTH =
    // Integer.parseInt(dictM.getDictByCode(210004).getDict_data());//路宽
    // }
    // if(GPS_INTERVAL_TIME == 0) {
    // if(dictM == null) {
    // dictM = new DictManageImpl();
    // }
    // GPS_INTERVAL_TIME =
    // Integer.parseInt(dictM.getDictByCode(210005).getDict_data());//GPS上报时间间隔
    // }
    // }

    /**
     *
     */
    @Override
    public byte[] getCrossAreaCounts(List<Long> terminalIds, long beginTime, long endTime, int areaType,
                                     List<Point> points) {
        return new byte[]{};
        /*
         * log.info("DSA->DA st:"+beginTime+" et:"+endTime); try {
		 * initDictValue(); //int minJumpPoints = minJumpPoints(areaType,
		 * points); int minJumpPoints = 0; CrossAreaCounts.Builder crossCount =
		 * CrossAreaCounts.newBuilder(); for (long terminalId : terminalIds) {
		 * int count = 0; GpsDataEntity queryGpsData =
		 * queryGpsData(terminalId,beginTime, endTime); CrossRecord.Builder
		 * record = CrossRecord.newBuilder(); record.setTerminalId(terminalId);
		 * boolean isIncome = false; List<GpsDetailedEntity> dataList =
		 * queryGpsData.getDataList(); for (int i = 0; i < dataList.size(); i++)
		 * { byte[] data = dataList.get(i).getData();
		 * LCLocationData.LocationData location =
		 * LCLocationData.LocationData.parseFrom(data); int remainingPoint = 0;
		 * switch (areaType) { case 0: if
		 * (CrossAreaCountCalculation.isBelongRectangle
		 * (location.getLongitude(),location.getLatitude(), points)) {
		 * if(isIncome == false) { count++; isIncome = true; }
		 *
		 * // remainingPoint = dataList.size() - (i + 1); // if (remainingPoint
		 * > minJumpPoints) { // i += minJumpPoints; // } // else { // i +=
		 * remainingPoint - 1; // } } else { if (isIncome){ isIncome =false;
		 * count++; } } break; case 1: if
		 * (CrossAreaCountCalculation.isBelonglineArea
		 * (location.getLongitude(),location.getLatitude(), points,ROAD_WIDTH))
		 * { if(isIncome == false) { count++; isIncome = true; }
		 *
		 * // remainingPoint = dataList.size() - (i + 1); // if (remainingPoint
		 * > minJumpPoints) // i += minJumpPoints; // else // i +=
		 * remainingPoint - 1; } else { if (isIncome) { count++; isIncome =
		 * false; } } break; } // System.out.println("terminalId"+terminalId+
		 * "--count111== ------------------------------------------------------"
		 * +count); } // System.out.println("terminalId"+terminalId+
		 * "--count22== ------------------------------------------------------"
		 * +count); record.setCounts(count); crossCount.addRecords(record); }
		 * return crossCount.build().toByteArray(); } catch (Exception e) {
		 * e.printStackTrace(); return null; }
		 */
    }

    @Deprecated
    @Override
    public byte[] getCrossAreaCounts2(List<Long> terminalIds, long beginTime, long endTime, int areaType,
                                      List<Point> points, int width) {
        // return new byte[]{};
        log.info("DSA->DA st:" + beginTime + " et:" + endTime);
        if (null == points) {
            log.error("查询条件中的区域点为空，返回null");
            return null;
        }
        if (null == terminalIds) {
            // 查询所有的终端
            log.error("查询条件中的终端列表为空，返回null");
            return null;
        }
        try {
            long stime = System.currentTimeMillis();
            CrossAreaCounts.Builder crossCount = CrossAreaCounts.newBuilder();
            List<Long[]> middleDays = DateUtils.getMiddleDays(beginTime, endTime);
            List<List<Long>> terSplitWith100 = terminalSplitWith100(terminalIds);
            TermianlDynamicManage service = new TermianlDynamicManageImpl();
            for (List<Long> tempTid : terSplitWith100) {
                // System.err.println(tempTid.size());
                // long t1 = System.currentTimeMillis();
                Map<String, List<GpsDetailedEntityDB>> queryGpsData = service.queryGpsData(tempTid, beginTime, endTime);
                // long t2 = System.currentTimeMillis();
                // System.err.println(queryGpsData.size()
                // +"query time:"+(t2-t1));
                for (Long terminalId : tempTid) {
                    // }
                    // }
                    // for (long terminalId : terminalIds) {
                    // 次数
                    int count = 0;
                    CrossRecord.Builder record = CrossRecord.newBuilder();
                    record.setTerminalId(terminalId);
                    for (Long[] dayTimes : middleDays) {
                        long beginDayTime = dayTimes[0];
                        long endDayTime = dayTimes[1];
                        // GpsDataEntity queryGpsData =
                        // queryGpsData(terminalId,beginDayTime, endDayTime);
                        // List<com.lc.core.protocol.rmi.statistic.entity.GpsDetailedEntity>
                        // dataList = queryGpsData.getDataList();
                        String dayString = DateUtils.format(beginDayTime, DateFormat.YYYYMMDD);
                        List<GpsDetailedEntityDB> dataList = queryGpsData.get(terminalId + "_" + dayString);
                        long queryTime = endDayTime - beginDayTime;
                        // 判断轨迹是否有可能经过此区域
                        boolean pass = CrossAreaCountCalculation.isFilterPositions(dataList, areaType, width, points,
                                queryTime);
                        // log.error("terminalId:"+terminalId+"
                        // date:"+DateUtils.format(beginDayTime,
                        // DateFormat.YYYYMMDD)+" isPass:"+pass);
                        if (pass) {
                            // 可能通过此区域
                            count += CrossAreaCountCalculation.calculationCounts(dataList, areaType, width, points);
                        }
                    }
                    record.setCounts(count);
                    crossCount.addRecords(record);
                }
                // long t3 = System.currentTimeMillis();
                // System.out.println("calculate time:"+(t3-t2));
                queryGpsData.clear();
                queryGpsData = null;
            }
            log.error("time is :" + (System.currentTimeMillis() - stime));
            return crossCount.build().toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }

    }

    private List<List<Long>> terminalSplitWith100(List<Long> terminals) {
        List<List<Long>> result = new ArrayList<List<Long>>();
        int splitNum = 100;
        int size = terminals.size() / splitNum + 1;
        for (int i = 1; i <= size; i++) {
            int startIndex = (i - 1) * splitNum;
            int endIndex = i * splitNum;
            if (i == size) {
                endIndex = terminals.size();
            }
            if (startIndex == endIndex) {
                break;
            }
            List<Long> tempList = terminals.subList(startIndex, endIndex);
            result.add(tempList);
        }
        return result;
    }

    @Override
    public List<Long> getByNodeCodeAndDistrict(long node_code, int district) {
        log.info("DAS->DA");
        try {
            MySqlConnPoolUtil.startTransaction();
            LcTerminalInfoDao dao = new LcTerminalInfoDaoImpl();
            List<LcTerminalInfoDBInfo> terminals = dao.getByNodeCodeAndDistrict(district);
            List<Long> ids = new ArrayList<Long>();
            for (LcTerminalInfoDBInfo info : terminals) {
                ids.add(info.getTerminal_id());
            }
            return ids;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            MySqlConnPoolUtil.close();
        }
        return null;
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    @Override
    public CANBusDataRecords getCANBusData(List<Long> terminalID, int type, long startDate, long endDate,
                                           CommonParameter commonParameter) throws RemoteException {
        CANBusDataRecords.Builder records = CANBusDataRecords.newBuilder();
        CanDataMongodbService service = new CanDataMongodbServiceImpl();
        if (1 == type) {
            List<CanDataReportEntity> result = new ArrayList<CanDataReportEntity>();
            Map<String, Map<String, Long>> _getDateSlot = DateDiffUtil.splitDateWithMonth(startDate, endDate);
            for (Entry<String, Map<String, Long>> entry : _getDateSlot.entrySet()) {
                String month = entry.getKey();
                Map<String, Long> map = (Map<String, Long>) entry.getValue();
                long beginTimeMonth = map.get("beginTime");
                long endTimeMonth = map.get("endTime");
                List<CanDataReportEntity> reportCanData = service.findReportCanData(terminalID, beginTimeMonth,
                        endTimeMonth, month, 0, 0);
                result.addAll(reportCanData);
            }
            for (CanDataReportEntity entity : result) {
                List<DACanDataDetail> dataList = entity.getDataList();
                if (null != dataList && dataList.size() > 0) {
                    for (DACanDataDetail detail : dataList) {
                        try {
                            records.addRecords(CANBUSDataReport.parseFrom(detail.getData()));
                        } catch (InvalidProtocolBufferException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        } else {
            int pageNum = commonParameter.getPageIndex();
            int pageSize = commonParameter.getPageSize();
            List<CanDataEntity> canData = service.findCanData(terminalID, startDate, endDate, pageNum, pageSize);
            log.info("page:" + pageNum + ",size:" + pageSize + ",result size:" + canData.size());
            for (CanDataEntity entity : canData) {
                List<DACanDataDetail> dataList = entity.getDataList();
                if (null != dataList && dataList.size() > 0) {
                    for (DACanDataDetail detail : dataList) {
                        try {
                            records.addRecords(CANBUSDataReport.parseFrom(detail.getData()));
                        } catch (InvalidProtocolBufferException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
        return records.build();
    }

    @Override
    public Map<Long, List<LocationData>> queryGpsDataFromRedis(List<Long> tids, long begin, long end) {
        TermianlDynamicManage termianlDynamicManage = new TermianlDynamicManageImpl();
        return termianlDynamicManage.queryRedisGpsData(tids, begin, end);
    }

    // 以下方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存
    @Override
    public Map<Long, List<LocationData>> queryGpsDataSegementThisDay(List<Long> tids, int segment) {
        TermianlDynamicManage termianlDynamicManage = new TermianlDynamicManageImpl();
        return termianlDynamicManage.queryGpsDataSegementThisDay(tids, segment);
    }

    @Override
    public List<LocationData> queryGpsDataSegementThisDay(long tid, int segment) {
        List<Long> tids = new ArrayList<>(1);
        tids.add(tid);
        Map<Long, List<LocationData>> temp = this.queryGpsDataSegementThisDay(tids, segment);
        return (temp != null) ? temp.get(tid) : null;
    }

    // 以上方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

    @Override
    public List<MileageAndOilData> getMileageAndOilData(List<Long> terminalIds) {
        log.info("RPWS->DA 里程油量数据检索开始......");
        List<MileageAndOilData> list = new ArrayList<MileageAndOilData>();
        if (TempGpsData.mileageAndOilDataCache != null) {
            Map<Long, MileageAndOilData> map = TempGpsData.mileageAndOilDataCache;
            for (long terminalId : terminalIds) {
                if (map.containsKey(terminalId)) {
                    MileageAndOilData mileageAndOilData = map.get(terminalId);
                    list.add(mileageAndOilData);
                }
            }
            return list;
        }
        return null;
    }

    @Override
    public LastestLocationDataRes getLastestLocationData(List<Long> terminalIds, String mileageRange, int type,
                                                         CommonParameter commonParameter) throws RemoteException {
        log.info("RPWS->DA 最新位置数据检索开始......");
        try {
            byte[] symbol = null;
            String[] num = null;
            if (!mileageRange.equals("")) {
                symbol = mileageRange.replaceAll("[\\d|$]+,?[\\d|$]+", "").trim().getBytes();
                num = mileageRange.replaceAll("[^0-9|^,|^$]", "").split(",");
            }
            List<TerminalLocationData> list = new ArrayList<TerminalLocationData>();
            Map<Long, LocationData> dataList = TempGpsData.findLocationData(terminalIds, type);
            if (dataList != null && dataList.size() > 0) {
                for (Map.Entry<Long, LocationData> entry : dataList.entrySet()) {
                    TerminalLocationData.Builder buider = TerminalLocationData.newBuilder();
                    LocationData locationData = entry.getValue();
                    long mileage = (long) locationData.getStandardMileage();
                    if (mileage < 0) {
                        mileage = 0;
                    }
					/*List<VehicleStatusData> vehicleStatusDatas = locationData.getStatusAddition().getStatusList();
					for (VehicleStatusData vehicleStatusData : vehicleStatusDatas) {
						if (vehicleStatusData.getTypes() == StatusType.mileage) {
							mileage = vehicleStatusData.getStatusValue();
							if (mileage < 0) {
								mileage = 0;
							}
							break;
						}
					}*/
                    if (!mileageRange.equals("")) {
                        // Integer nMin = num[0].equals("$") ? Integer.MIN_VALUE
                        // : Integer.valueOf(num[0]);
                        // Integer nMax = num[1].equals("$") ? Integer.MAX_VALUE
                        // : Integer.valueOf(num[1]);
                        long nMin = Long.parseLong(num[0]);
                        if (!(nMin <= mileage) || (symbol[0] == '(' && nMin == mileage))
                            continue;
                        if (!num[1].equals("$")) {
                            long nMax = Long.parseLong(num[1]);
                            if (!(nMax >= mileage) || (symbol[1] == ')' && nMax == mileage))
                                continue;
                        }

                    }
                    buider.setTerminalId(entry.getKey());
                    buider.setLocationData(locationData);
                    list.add(buider.build());
                }
            }
            LastestLocationDataRes.Builder builder = LastestLocationDataRes.newBuilder();
            builder.setStatusCode(1);
            builder.setTotalRecords(list.size());
            if (commonParameter.isMultipage()) {
                int minIndex = commonParameter.getPageIndex() * commonParameter.getPageSize();
                int maxIndex = (commonParameter.getPageIndex() + 1) * commonParameter.getPageSize();
                maxIndex = Math.min(maxIndex, list.size());
                for (int i = minIndex; i < maxIndex; i++) {
                    builder.addDatas(list.get(i));
                }
            } else {
                builder.addAllDatas(list);
            }
            return builder.build();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    /*
     * 此处需要改造，多次返回数据类型转化——hk add by zhangyj
     *
     * 返回所有最新位置数据，不根据终端id进行过滤。 通过设备Id，获取该设备最新的数据
     *
     * 接口有问题，没有通过终端id查询。改——hk
     */
    @Override
    public Map<Long, List<LocationData>> getLastLocationData(List<Long> terminalIds, int type) throws RemoteException {
        long begin = System.currentTimeMillis();
        Map<Long, List<LocationData>> result = new HashMap<Long, List<LocationData>>();
        Map<Long, LocationData> previousLocations = TempGpsData.getAllLastestLcFromStaticRedis(type);
        if (previousLocations != null) {
            Iterator<Entry<Long, LocationData>> iterator = previousLocations.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, LocationData> entry = iterator.next();
                List<LocationData> list = new ArrayList<LocationData>();
                LocationData locationData = entry.getValue();
                list.add(locationData);
                result.put((Long) entry.getKey(), list);
            }
        }
        log.error("获取[" + result.size() + " 条]最新位置数据耗时(ms)：" + (System.currentTimeMillis() - begin));
        return result;
    }

    @Override
    public void delGpsDataSegementThisDay() throws RemoteException {
        TermianlDynamicManage termianlDynamicManage = new TermianlDynamicManageImpl();
        termianlDynamicManage.delGpsDataSegementThisDay();
    }

    @Override
    public GetTerminalLocationDataRes getTerminalLocationData(long terminalId, long queryDate, int index,
                                                              long accessTocken) throws RemoteException {
        List<LocationData> list = null;
        long todayBegin = this.getTodayBegin();
        GetTerminalLocationDataRes.Builder builder = GetTerminalLocationDataRes.newBuilder();
        builder.setStatusCode(1);
        builder.setTotalRecords(1);
        if (queryDate < todayBegin) { // mongo获取
            ReportQueryService service = new ReportQueryServiceImpl();
            list = service.getTerminalLocationData(terminalId, queryDate);
        } else { // 当天 redis获取
            list = TempGpsData.getTerminalLocationData(terminalId);
        }
        if (list != null && list.size() > 0) {
            if (index >= 0) {
                builder.setLocationData(list.get(index));
                builder.setCurrent(index);
                return builder.build();
            } else {
                // 二分法获取查询时间点的最近一条数据
                int num = search(list, queryDate);
                builder.setLocationData(list.get(num));
                builder.setCurrent(num);
                return builder.build();
            }
        }
        return null;
    }

    private static int search(List<LocationData> list, long queryDate) {
        int low = 0;
        int mid = 0;
        int height = list.size() - 1;
        while (low <= height) {
            // 位运算提高效率
            mid = (low + height) >>> 1;
            Integer midal = (int) list.get(mid).getGpsDate();
            // 与key比较 比key大 返回1 否则返回-1 相等返回0
            int copInt = midal.compareTo((int) queryDate);
            if (copInt < 0) {
                low = mid + 1;
            } else if (copInt > 0) {
                height = mid - 1;
            } else {
                return mid;
            }
        }
        if (height == -1) {
            height = 0;
        }
        return height;
    }

    private long getTodayBegin() {
        long now = System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(now);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return (calendar.getTimeInMillis() / 1000);
    }

    /**
     * 区域查车，支持1：圆形，2：矩形，3：多边形
     *
     * @param type
     * @param points
     * @param radius
     * @return
     */
    @Override
    public GetTerminalInAreaRes getTerminalInArea(List<Long> terminalIds, int type, List<Point> points, int radius)
            throws RemoteException {
        GetTerminalInAreaRes.Builder inAreaRes = GetTerminalInAreaRes.newBuilder();
        // 这里是单线程进行计算，如果入网车辆比较多，性能会有影响，可以考虑多线程。http://blog.csdn.net/aboy123/article/details/38307539
        try {
            Map<Long, LocationData> dataList = TempGpsData
                    .getAllLastestLcFromRedis(RedisConstans.RedisKey.LASTEST_LOCATION_DATA_LATLNG.name());
            int count = 0;
            if (dataList != null && dataList.size() > 0) {
                Map<Long, LocationData> map = new HashMap<Long, LocationData>();// 缓存，记录终端的位置数据
                for (Long id : terminalIds) {
                    if (dataList.containsKey(id)) {
                        map.put(id, dataList.get(id));
                    }
                }
                for (Map.Entry<Long, LocationData> entry : map.entrySet()) {
                    LocationData locationData = entry.getValue();
                    // if(entry.getKey()==19900117001l){
                    // log.info("19900117001l");
                    // }
                    boolean inArea = CrossAreaCountCalculation.isInAreaV2(locationData, type, radius, points);
                    if (inArea) {
                        inAreaRes.addTerminalIds(entry.getKey());
                        count += 1;
                    }
                }
            }
            inAreaRes.setStatusCode(1);
            inAreaRes.setTotalRecords(count);
        } catch (Exception e) {
            inAreaRes.setStatusCode(0);
            inAreaRes.setTotalRecords(0);
        }
        return inAreaRes.build();
    }

    @Override
    public GetTerminalInDistrictRes GetTerminalInDistrict(List<Long> terminalIds, int districtCode)
            throws RemoteException {
        GetTerminalInDistrictRes.Builder inDistrictRes = GetTerminalInDistrictRes.newBuilder();
        try {
            List<Long> list = TempGpsData.findTerminalInDistrict(terminalIds, districtCode);
            inDistrictRes.setStatusCode(1);
            inDistrictRes.setTotalRecords(list.size());
            inDistrictRes.addAllTerminalIds(list);
        } catch (Exception e) {
            inDistrictRes.setStatusCode(0);
            inDistrictRes.setTotalRecords(0);
            inDistrictRes.addTerminalIds(0l);
        }
        return inDistrictRes.build();
    }

    @Override
    public LastestLocationDataByAlarmFilterRes getLastestLocationDataByAlarmFilter(List<Long> terminalIds,
                                                                                   boolean isFilter, long alarmType, int isCancel, int type, CommonParameter commonParameter)
            throws RemoteException {
        try {
            List<TerminalLocationData> list = new ArrayList<TerminalLocationData>();
            Map<Long, LocationData> dataList = TempGpsData.findLocationData(terminalIds, type);
            // 过滤分四种情况：
            // ①isFilter = true，筛选报警数据，=false时，直接返回所有数据
            // ②isFilter = true,isCancel=0,返回所有 报警类型=0x080000为超时停车的数据
            // ③isFilter = true,isCancel=1,返回 报警撤销alarmFilter=1(撤销)的超时停车数据
            // (isCancel,alarmFilter分别做& alarmType运算做判断)
            // ④isFilter = true,isCancel=2,返回
            // 报警撤销alarmFilter=0(正常)的超时停车数据（alarmFilter详见api接口文档-位置数据项）
            if (dataList != null && dataList.size() > 0) {
                for (Map.Entry<Long, LocationData> entry : dataList.entrySet()) {
                    TerminalLocationData.Builder buider = TerminalLocationData.newBuilder();
                    LocationData locationData = entry.getValue();
                    if (isFilter == true) {// ②
                        if ((locationData.getAlarm() & alarmType) == 0x080000) {
                            if (isCancel == 1) {// ③
                                if ((locationData.getAlarmFilter() & alarmType) != 0x080000) {
                                    continue;
                                }
                            } else if (isCancel == 2) { // ④
                                if ((locationData.getAlarmFilter() & alarmType) != 0) {
                                    continue;
                                }
                            }
                        } else {
                            continue; // 当isFilter=true,对非超时停车数据进行过滤
                        }
                    }
                    buider.setTerminalId(entry.getKey());
                    buider.setLocationData(locationData);
                    list.add(buider.build());
                }
            }
            // 终端id降序排序
            // 降序排序
            Collections.sort(list, new Comparator<TerminalLocationData>() {
                @Override
                public int compare(TerminalLocationData o1, TerminalLocationData o2) {
                    return (int) (o2.getTerminalId() - o1.getTerminalId());
                }
            });
            LastestLocationDataByAlarmFilterRes.Builder builder = LastestLocationDataByAlarmFilterRes.newBuilder();
            builder.setStatusCode(1);
            builder.setTotalRecords(list.size());
            if (commonParameter.isMultipage()) {
                int minIndex = commonParameter.getPageIndex() * commonParameter.getPageSize();
                int maxIndex = (commonParameter.getPageIndex() + 1) * commonParameter.getPageSize();
                maxIndex = Math.min(maxIndex, list.size());
                for (int i = minIndex; i < maxIndex; i++) {
                    builder.addDatas(list.get(i));
                }
            } else {
                builder.addAllDatas(list);
            }

            return builder.build();
        } catch (Exception e) {
            e.getStackTrace();
        }
        return null;
    }

    @Override
    public LastestTerminalStatusRes GetLastestTerminalStatus(List<Long> terminalIds) throws RemoteException {
        // TODO Auto-generated method stub
        try {
            TerminalOnOffStatusServiceImpl onOffStatus = new TerminalOnOffStatusServiceImpl();
            Map<Long, TerminalOnOffStatus> statusMap = new HashMap<Long, TerminalOnOffStatus>();
            List<LastestTerminalStatusRes> statusList = new ArrayList<>();
            if (terminalIds != null && terminalIds.size() > 0) {
                statusMap = onOffStatus.getTerminalStatus(terminalIds);

            } else {
                statusMap = onOffStatus.getAllTerminalStatus();
            }

            LastestTerminalStatusRes.Builder lastStatusRes = LastestTerminalStatusRes.newBuilder();
            lastStatusRes.setStatusCode(1);
            for (Map.Entry<Long, TerminalOnOffStatus> entry : statusMap.entrySet()) {
                try {
                    TerminalOnOffStatus status = entry.getValue();
                    LastestTerminalStatus.Builder lastestStatus = LastestTerminalStatus.newBuilder();
                    lastestStatus.setTerminalId(status.getTerminalId());
                    lastestStatus.setStatus(status.getStatus() == 1 ? true : false);
                    lastStatusRes.addStatus(lastestStatus);
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
            return lastStatusRes.build();

        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @Override
    public GetTerminalFirstRecieveDateRes GetTerminalFirstRecieveDate(
            List<Long> terminalIds) throws RemoteException {
        // TODO Auto-generated method stub
        try {
            TerminalOnOffStatusServiceImpl onOffStatus = new TerminalOnOffStatusServiceImpl();
            Map<Long, Long> statusMap = onOffStatus.getTerminalFirstRecieveDate(terminalIds);
            if (statusMap != null && statusMap.size() > 0) {
                GetTerminalFirstRecieveDateRes.Builder recieveRes = GetTerminalFirstRecieveDateRes.newBuilder();
                recieveRes.setStatusCode(1);
                for (Map.Entry<Long, Long> entry : statusMap.entrySet()) {
                    try {
                        TerminalRecieveDateSave.Builder recieveBuilder = TerminalRecieveDateSave.newBuilder();
                        recieveBuilder.setTerminalId(entry.getKey());
                        recieveBuilder.setRecieveDate(entry.getValue());
                        recieveRes.addRecords(recieveBuilder);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
                return recieveRes.build();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return null;
    }

    @Override
    public void saveGpsDataTask() throws RemoteException {
        //启动定时任务管理器
        ScheduleController.instance().saveGpsDataTask();
    }

    /**
     * 获取车辆最后进入区域 信息
     *
     * @param terminalIds 终端列表
     * @param areaIds     区域列表
     * @return
     * @throws Exception
     */
    @Override
    public LCLatestParkRecoreds.LatestParkRecoreds GetLatestParkRecordsRes(List<Long> terminalIds, List<Long> areaIds) throws RemoteException {
        log.info("开始查询车辆最后进入区域...共查询{0}车、{1}区域", terminalIds.size(), areaIds.size());
        if (terminalIds.size() != areaIds.size()) {
            log.error("开始查询车辆最后进入区域...共查询{0}车、{1}区域,车次与区域数据不匹配", terminalIds.size(), areaIds.size());
            return null;
        }
        try {
            List<LatestParkEntity> list = TempGpsData.findLatestParkData(terminalIds, areaIds);
            LCLatestParkRecoreds.LatestParkRecoreds.Builder latestParkRecoreds = LCLatestParkRecoreds.LatestParkRecoreds.newBuilder();
            latestParkRecoreds.setStatusCode(1);

            List<LCLatestPark.LatestPark> tempList = new ArrayList<>();
            for (LatestParkEntity entry : list) {
                LCLatestPark.LatestPark.Builder builder = LCLatestPark.LatestPark.newBuilder();
                builder.setAreaId(entry.getAreaId());
                builder.setBeginDate(entry.getGpsTime());
                builder.setTerminalID(entry.getTerminalId());
                tempList.add(builder.build());
            }
            latestParkRecoreds.setTotalRecords(tempList.size());
            latestParkRecoreds.addAllDataList(tempList);
            return latestParkRecoreds.build();
        } catch (Exception ex) {

            return null;
        }
    }
}

