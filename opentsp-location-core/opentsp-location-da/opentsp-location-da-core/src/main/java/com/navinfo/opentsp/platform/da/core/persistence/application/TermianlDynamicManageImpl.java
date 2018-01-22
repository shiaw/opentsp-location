package com.navinfo.opentsp.platform.da.core.persistence.application;

import com.google.protobuf.TextFormat.ParseException;
import com.navinfo.opentsp.platform.da.core.cache.ReportQueryCache;
import com.navinfo.opentsp.platform.da.core.cache.TerminalIdCache;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.kit.lang.ObjectUtils;
import com.navinfo.opentsp.platform.da.core.persistence.TermianlDynamicManage;
import com.navinfo.opentsp.platform.da.core.persistence.common.LcDateFromat;
import com.navinfo.opentsp.platform.da.core.persistence.entity.extend.MediaFileModel;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDetailedEntityDB;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsDrivingRecorderEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.GpsTransferEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mongodb.PictureEntity;
import com.navinfo.opentsp.platform.da.core.persistence.entity.mysql.LcMultimediaParaDBEntity;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.MongoManager;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.DrivingRecorderMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.LocationMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.MultimediaMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.TransparentDataMongodbService;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.DrivingRecorderMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.LocationMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.MultimediaMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mongodb.dao.impl.TransparentDataMongodbServiceImpl;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.LcMultimediaParaDao;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.common.MySqlConnPoolUtil;
import com.navinfo.opentsp.platform.da.core.persistence.mysql.impl.LcMultimediaParaDaoImpl;
import com.navinfo.opentsp.platform.da.core.persistence.redis.RedisClusters;
import com.navinfo.opentsp.platform.da.core.persistence.redis.local.TempGpsData;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILinkService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.ILocationRedisService;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LinkServiceImp;
import com.navinfo.opentsp.platform.da.core.persistence.redis.service.impl.LocationRedisServiceImpl;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.CANBUSData.LCCANBUSDataReport.CANBUSDataReport;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class TermianlDynamicManageImpl implements TermianlDynamicManage {
    private Logger logger = LoggerFactory.getLogger(TermianlDynamicManageImpl.class);
    final static ILocationRedisService locationRedisService = new LocationRedisServiceImpl();

    @Override
    public void saveGpsData(long terminalId, LocationData locationData) {
        // System.out.println("Gps data -->"+terminalId);
        // redis中存储
        locationRedisService.savaNormalLocation(terminalId, locationData);
    }

    @Override
    public List<GpsDetailedEntityDB> queryGpsData(long terminalId, long beginTime, long endTime) {
        List<GpsDetailedEntityDB> result = new ArrayList<GpsDetailedEntityDB>();
        // 当天的从redis中查，往期的从mongodb中查找
        LocationMongodbService locationMongodbService = new LocationMongodbServiceImpl();

        int startTimeDay = DateUtils.differDay(DateUtils.current(), DateUtils.calendar(beginTime));
        // 查询开始日期不是今天
        if (startTimeDay != 0) {
            Map<String, Map<String, Long>> _getDateSlot = DateDiffUtil.splitDateWithMonth(beginTime, endTime);
            List<GpsDetailedEntityDB> temp = null;
            for (Entry<String, Map<String, Long>> entry : _getDateSlot.entrySet()) {
                String month = entry.getKey();
                Map<String, Long> map = (Map<String, Long>) entry.getValue();
                long beginTimeMonth = map.get("beginTime");
                long endTimeMonth = map.get("endTime");
                temp = locationMongodbService.findLocationData(terminalId, beginTimeMonth, endTimeMonth, month);
                if (temp != null && temp.size() > 0) {
                    result.addAll(temp);
                }
            }
        }
        int endTimeDay = DateUtils.differDay(DateUtils.current(), DateUtils.calendar(endTime));
        // 如果查询的结束日期是今天
        if (endTimeDay == 0) {
            List<GpsDetailedEntityDB> _redis_data = locationMongodbService._get_redis_gps(terminalId);

            List<GpsDetailedEntityDB> _filter_gps = locationMongodbService._filter_gps(beginTime, endTime, _redis_data);
            if (_filter_gps != null && _filter_gps.size() > 0) {
                result.addAll(_filter_gps);
            }
        }

        return result;
    }

    /**
     * 从Redis获取指定时间段内的位置数据。
     *
     * @param tids
     * @param begin
     * @param end
     * @return
     */
    public Map<Long, List<LocationData>> queryRedisGpsData(List<Long> tids, long begin, long end) {
        logger.error("查询时间 ：begin(s):" + begin + " end(s): " + end + "终端数量 :" + tids.size());
        long start = System.currentTimeMillis();
        int dayDifference = DateUtils.differDay(DateUtils.calendar(begin), DateUtils.calendar(end));
        if (dayDifference != 0) {
            logger.error("该接口只支持一天内数据查询..");
            return null;
        }
        LocationMongodbService locationMongodbService = new LocationMongodbServiceImpl();
        Map<Long, List<LocationData>> gpsMap = locationMongodbService.findRedisLocationData(tids, begin, end);
        if (gpsMap == null) {
            return new HashMap<>(1);
        }
        logger.error("获取[" + gpsMap.size() + "]终端数据耗时(ms)：" + (System.currentTimeMillis() - start));
        return gpsMap;
    }

    // 以下方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存
    public Map<Long, List<LocationData>> queryGpsDataSegementThisDay(List<Long> tids, int segment) {
        return locationRedisService.findCurrentDayLocationsBySegment(tids, segment);
    }

    public static void main(String[] args) {
        List<Long> tIdList = new ArrayList<>();

        for (Long i = 14000000000L; i < 14000000001L; i++) {
            tIdList.add(i);
        }
        TerminalIdCache.getInstance().add(tIdList);
        new TermianlDynamicManageImpl().delGpsDataSegementThisDay();
    }

    // added by zhangyj : 删除过期分段的位置数据
    public void delGpsDataSegementThisDay() {
        Calendar cal = Calendar.getInstance();
        long millis = cal.getTimeInMillis();
        // Calendar todayCal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        long today = cal.getTimeInMillis();
        long seg = (millis - today) / (300 * 1000);
        seg = seg - 3;
//        if (seg <= 0) {
//            seg = seg + 287;
//            cal.add(Calendar.MINUTE, -5);
//        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String format = sdf.format(cal.getTime());
        long s = System.currentTimeMillis();
        try {
//            //清除终端分段历史数据
//            int size = TerminalIdCache.getInstance().getAll().size();
//            String[] keyArr = new String[size];
//            if(seg >= 3) {
//                for(int i = (int) (seg - 3); i<= seg; i++) {
//                    int j = 0;
//                    for(long tid :TerminalIdCache.getInstance().getAll()) {
//                        keyArr[j] = tid +"_" + format + "_" + i;
//                        j++;
//                    }
//                    locationRedisService.delGpsDataSegementByKeyArr(keyArr);
//                }
//            }
//            logger.error("删除当日终端分段数据:{},终端数量:{},耗时:{}ms",format+"_"+seg,
//                    TerminalIdCache.getInstance().getAll().size(),
//                    System.currentTimeMillis()-s);
            if (seg == 0) {
//                s = System.currentTimeMillis();
//                cal.add(Calendar.HOUR, -3);
//                format = sdf.format(cal.getTime());
//                //清除昨天数据
//                for(int i = 0 ; i< 288; i++) {
//                    int j = 0;
//                    for(long tid :TerminalIdCache.getInstance().getAll()) {
//                        keyArr[j] = tid +"_" + format + "_" + i;
//                        j++;
//                    }
//                    locationRedisService.delGpsDataSegementByKeyArr(keyArr);
//                }
//                logger.error("删除昨日终端分段数据:{},终端数量:{},耗时:{}",format+"_"+seg,
//                        TerminalIdCache.getInstance().getAll().size(),
//                        System.currentTimeMillis()-s);

                //清除查询QueryCache_*缓存
                s = System.currentTimeMillis();
                int size = ReportQueryCache.getInstance().getCache().size();
                if(size > 0) {
                    String[] reportArr = new String [size];
                    int j = 0;
                    for(Entry<String, String> temp :ReportQueryCache.getInstance().getCache().entrySet()) {
                        reportArr[j] = temp.getKey();
                        j++;
                    }
                    if(reportArr.length > 0) {
                        locationRedisService.delGpsDataSegementByKeyArr(reportArr);
                        ReportQueryCache.getInstance().clear();
                    }
                    logger.error("清除QueryCache缓存:{},数量:{},耗时:{}",format+"_"+seg,size,System.currentTimeMillis()-s);
                }
            }
        }catch (NullPointerException e) {
            logger.error("删除分段数据错误",e);
        }
    }

    public List<LocationData> queryGpsDataSegementThisDay(long tid, int segment) {
        List<Long> tids = new ArrayList<>(1);
        tids.add(tid);
        Map<Long, List<LocationData>> temp = this.queryGpsDataSegementThisDay(tids, segment);
        return (temp != null) ? temp.get(tid) : null;
    }

    // 以上方法于2016年4月21日新增，用于查询5分钟一段的位置数据临时缓存

    public Map<String, List<GpsDetailedEntityDB>> queryGpsData(List<Long> terminalIds, long beginTime, long endTime) {
        Map<String, List<GpsDetailedEntityDB>> result = new HashMap<String, List<GpsDetailedEntityDB>>();
        // 当天的从redis中查，往期的从mongodb中查找
        LocationMongodbService locationMongodbService = new LocationMongodbServiceImpl();
        int startTimeDay = DateUtils.differDay(DateUtils.current(), DateUtils.calendar(beginTime));
        // 查询开始日期不是今天
        if (startTimeDay != 0) {
            Map<String, Map<String, Long>> _getDateSlot = DateDiffUtil.splitDateWithMonth(beginTime, endTime);
            Map<String, List<GpsDetailedEntityDB>> temp = null;
            for (Entry<String, Map<String, Long>> entry : _getDateSlot.entrySet()) {
                String month = entry.getKey();
                Map<String, Long> map = (Map<String, Long>) entry.getValue();
                long beginTimeMonth = map.get("beginTime");
                long endTimeMonth = map.get("endTime");
                temp = locationMongodbService.findLocationData(terminalIds, beginTimeMonth, endTimeMonth, month);
                if (temp != null && temp.size() > 0) {
                    result.putAll(temp);
                }
            }
        }
        int endTimeDay = DateUtils.differDay(DateUtils.current(), DateUtils.calendar(endTime));
        // 如果查询的结束日期是今天
        if (endTimeDay == 0) {
            Map<String, List<GpsDetailedEntityDB>> redisLocations = locationMongodbService
                    .getRedisLocationData(terminalIds);
            Set<String> keySet = redisLocations.keySet();
            for (String key : keySet) {
                List<GpsDetailedEntityDB> _redis_data = redisLocations.get(key);
                List<GpsDetailedEntityDB> _filter_gps = locationMongodbService._filter_gps(beginTime, endTime,
                        _redis_data);
                if (null != _filter_gps && _filter_gps.size() > 0) {
                    result.put(key, _filter_gps);
                }
            }
        }
        return result;
    }

    @Override
    public void saveLinkExceptionData(long nodeCode, long nodeCodeTo, int dataType, byte[] dataValue) {
        // redis中存储
        ILinkService linkService = new LinkServiceImp();
        linkService.saveLinkExceptionData(nodeCode, nodeCodeTo, dataType, dataValue);
    }

    @Override
    public List<byte[]> queryLinkExceptionData(long nodeCode, long nodeCodeTo, int dataType, int size) {
        // redis中查询
        ILinkService linkService = new LinkServiceImp();
        List<byte[]> list = linkService.queryLinkExceptionData(nodeCode, nodeCodeTo, dataType, size);
        return list;
    }

    @Override
    public void savePictureData(long terminalId, LcMultimediaParaDBEntity multimediaTopic, PictureEntity pictureEntity) {
        try {
            // mysql和mongodb中存
            // mysql存
            MySqlConnPoolUtil.startTransaction();
            LcMultimediaParaDao multimediaParaDao = new LcMultimediaParaDaoImpl();
            int result = multimediaParaDao.add(multimediaTopic);
            if (result == PlatformResponseResult.success_VALUE) {
                MySqlConnPoolUtil.commit();
                // mongodb存
                // MongoManager.start("GpsMultimediaData", "PictureData_YYMM");
                // DB db=MongoManager.getDB("GpsMultimediaData");
                MultimediaMongodbService multimediaMongodbService = new MultimediaMongodbServiceImpl();
                multimediaMongodbService.savePicture(pictureEntity);
                // MongoManager.close();
            } else {
                MySqlConnPoolUtil.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
            MySqlConnPoolUtil.rollback();
        } finally {
            MySqlConnPoolUtil.close();
        }

    }

    @Override
    public List<PictureEntity> queryPictureData(String... picName) {
        // 此方法存在问题：1.collection有问题。2.query的返回值有问题
        // mongodb中查找
        // 初始化mongodb
        // MongoManager.start("GpsMultimediaData", "PictureData_YYMM");
        // DB db=MongoManager.getDB("GpsMultimediaData");
        MultimediaMongodbService multimediaMongodbService = new MultimediaMongodbServiceImpl();
        List<PictureEntity> result = new ArrayList<PictureEntity>();
        for (int i = 0; i < picName.length; i++) {
            byte[] picture = multimediaMongodbService.queryPicture(picName[i]);
            if (picture != null) {
                PictureEntity pict = (PictureEntity) ObjectUtils.bytesToObject(picture);
                result.add(pict);
            } else {
                continue;
            }
        }
        // 关闭mongodb
        // MongoManager.close();
        return result;
    }

    @Override
    public List<LcMultimediaParaDBEntity> queryMultimedia(long[] terminalId, int multimediaType, long beginTime,
                                                          long endTime) {
        // 从mysql单表查询
        try {
            MySqlConnPoolUtil.startTransaction();
            LcMultimediaParaDao multimediaParaDao = new LcMultimediaParaDaoImpl();
            List<LcMultimediaParaDBEntity> result = multimediaParaDao.getMultimedia(terminalId, multimediaType,
                    beginTime, endTime, 0, 0);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            MySqlConnPoolUtil.close();
        }
    }

    @Override
    public void savePassThrough(List<GpsTransferEntity> transferEntity) {
        if (CollectionUtils.isNotEmpty(transferEntity)) {
            try {
                MongoManager.start(" GpsTransferData", "TransferData_YYMM");
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            TransparentDataMongodbService service = new TransparentDataMongodbServiceImpl();
            service.savePassThrough(transferEntity);
            MongoManager.close();
        }
    }

    @Override
    public void saveDrivingRecorder(GpsDrivingRecorderEntity gpsDrivingRecorderEntity) {
        if (gpsDrivingRecorderEntity != null) {
            try {
                MongoManager.start("GpsDrivingRecorderData", "RecorderData_YYMM");
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            DrivingRecorderMongodbService service = new DrivingRecorderMongodbServiceImpl();
            service.saveDrivingRecorder(gpsDrivingRecorderEntity);
            MongoManager.close();
        }

    }

    /**
     * [自定义方法]获取时间段
     *
     * @param beginTime
     * @param endTime
     * @return
     */
    private List<Map<String, Long>> _get_dateSlot(long beginTime, long endTime) {
        List<Map<String, Long>> result = new ArrayList<Map<String, Long>>();
        // 日期转换 yyyy-mm-ss
        Date beginDate = new Date(beginTime * 1000);
        Date endDate = new Date(endTime * 1000);
        SimpleDateFormat formatter = new SimpleDateFormat("MM");
        String getBeginMonth = formatter.format(beginDate);
        String getendMonth = formatter.format(endDate);
        if (getBeginMonth.equals(getendMonth)) {
            // 是否是当前月
            Map<String, Long> map = new ConcurrentHashMap<String, Long>();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            result.add(map);
        } else {
            SimpleDateFormat formatters = null;
            try {
                // 开始月
                formatters = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                String beginMonth = formatters.format(beginTime * 1000);
                beginMonth = LcDateFromat.getMaxMonthDate(beginMonth);
                // 格式化时间，月份最后日期小时为yyyy-MM-dd 23:59:59
                beginMonth = LcDateFromat.getSubStringMonthDate(beginMonth);
                Map<String, Long> map = new ConcurrentHashMap<String, Long>();
                map.put("beginTime", beginTime);
                map.put("endTime", formatters.parse(beginMonth).getTime() / 1000);
                result.add(map);
                // 获取开始月与结束月中间隔几个月
                int sumCenterMonth = Integer.valueOf(getendMonth) - Integer.valueOf(getBeginMonth);
                if (sumCenterMonth == 1) {
                    // 结束月
                    String endMonth = formatters.format(endTime * 1000);
                    Map<String, Long> endMap = new ConcurrentHashMap<String, Long>();
                    endMap.put("beginTime", formatters.parse(LcDateFromat.getMinMonthDate(endMonth)).getTime() / 1000);
                    endMap.put("endTime", endTime);
                    result.add(endMap);
                } else if (sumCenterMonth != 0 && sumCenterMonth != 1) {
                    formatters = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    String ter = formatters.format(beginTime);
                    for (int i = 1; i <= sumCenterMonth; i++) {
                        int sum = Integer.valueOf(getBeginMonth) + i;
                        String[] beginStr = ter.split("-");
                        beginStr[1] = String.valueOf(sum);
                        // 中间月 centerMm
                        String centerMonth = "";
                        for (int j = 0; j < beginStr.length; j++) {
                            centerMonth = centerMonth + beginStr[j];
                        }
                        if (sumCenterMonth == i) {
                            // 最后月
                            String endMonth = formatters.format(beginTime);
                            // 格式化时间，月份最后日期小时为yyyy-MM-dd 23:59:59
                            endMonth = LcDateFromat.getSubStringMonthDate(endMonth);
                            Map<String, Long> endMap = new ConcurrentHashMap<String, Long>();
                            map.put("beginTime", formatters.parse(LcDateFromat.getMinMonthDate(endMonth)).getTime());
                            map.put("endTime", endTime);
                            result.add(endMap);
                        } else {
                            // 中间月 月末
                            // 格式化时间，月份最后日期小时为yyyy-MM-dd 23:59:59
                            centerMonth = LcDateFromat.getSubStringMonthDate(centerMonth);
                            // 中间月
                            Map<String, Long> centerMap = new ConcurrentHashMap<String, Long>();
                            map.put("beginTime", formatters.parse(LcDateFromat.getMinMonthDate(centerMonth)).getTime());
                            map.put("endTime", formatters.parse(centerMonth).getTime());
                            result.add(centerMap);
                        }
                    }
                }
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();

            }
        }
        return result;
    }

    @Override
    public MediaFileModel queryMediaFileRes(String fileCode, long terminalId) {
        // 多媒体文件查询
        // 初始化mongodb
        MediaFileModel mediaFileModel = new MediaFileModel();
        // MongoManager.start("GpsMultimediaData", "PictureData_YYMM");
        MultimediaMongodbService multimediaMongodbService = new MultimediaMongodbServiceImpl();
        byte[] picture = multimediaMongodbService.queryPicture(fileCode);
        mediaFileModel.setFile(picture);
        PictureEntity pict = (PictureEntity) ObjectUtils.bytesToObject(picture);
        // 得到文件类型
        // mediaFileModel.setFileType(fileType);
        // 关闭mongodb
        // MongoManager.close();
        // 获得位置数据
        // mediaFileModel.setLocationData(locationData);
        return mediaFileModel;
    }

    @Override
    public void saveCanData(long terminalId, CANBUSDataReport canData) {
        String nodeCode = RedisClusters.getInstance().getNode(terminalId);
        // 放入本地临时缓存
        TempGpsData.addCanData(nodeCode, terminalId, canData);
    }
}