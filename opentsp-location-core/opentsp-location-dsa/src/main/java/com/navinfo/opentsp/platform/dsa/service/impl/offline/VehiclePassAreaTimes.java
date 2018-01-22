package com.navinfo.opentsp.platform.dsa.service.impl.offline;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlgOffline;
import com.navinfo.opentsp.platform.dsa.service.interf.Offline;
import com.navinfo.opentsp.platform.location.protocol.common.LCAreaType.AreaType;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesDetail;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesQuadtree;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesRecord;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 统计服务站车次、省级和市级行政区域车次 服务站内车次增加各个终端车次，当天最新发动机运行时长，当天最新整车里程，是否有故障码
 * <p>
 * 计算过程中完成了相同类型的区域的数据合并。
 *
 * @author hk
 */
@Component
@Scope("prototype")
public class VehiclePassAreaTimes extends ConcurrentAlgOffline implements Offline {


    private static final double EARTH_RADIUS = 6378.137;
    private static final int filterMileage = 100000;// 米
    private static final int binaryDepth = 5; // 33个点
    private static final int minZoom = 15;
    public static Logger logger = LoggerFactory.getLogger(VehiclePassAreaTimes.class);

    private static void vehiclePassTimesDetailUpdate(VehiclePassTimesRecord entity, long tid, LocationData location) {
        // 只增加服务站的车辆经过详情
        //if (entity.getType() == 5) {
            VehiclePassTimesDetail detail = entity.getVehiclePassTimesDetail(tid);
            if (detail == null) {
                detail = new VehiclePassTimesDetail();
                detail.setTid(tid);
                detail.setFaultCode(false);
                entity.addVehiclePassTimesDetail(tid, detail);
            }
            for (VehicleStatusData data : location.getStatusAddition().getStatusList()) {
                if (data.getTypes().getNumber() == StatusType.mileage_VALUE) {
                    detail.setMileage(data.getStatusValue() * 10);
                }
                if (data.getTypes().getNumber() == StatusType.cumulativeRunningTime_VALUE) {
                    detail.setRunTime(data.getStatusValue() / 100);
                }
            }
            if (!detail.getFaultCode()) {
                for (VehicleBreakdown breakdown : location.getBreakdownAddition().getBreakdownList()) {
                    if (breakdown.getBreakdownFMIValue() != 0 && breakdown.getBreakdownSPNValue() != 0) {
                        detail.setFaultCode(true);
                        break;
                    }
                }
            }
        //}

    }

    private static boolean filter(List<LocationData> list, AreaInfo areaInfo) {
        List<Double> distances = new ArrayList<>();
        for (LocationData location : list) {
            if (areaInfo.getTypes() != AreaType.circle && areaInfo.getDatasCount() == 0) {
                return true;
            }
            double lat1 = ((double) areaInfo.getDatas(0).getLatitude()) / Math.pow(10, 6);
            double lon1 = ((double) areaInfo.getDatas(0).getLongitude()) / Math.pow(10, 6);
            double lat2 = ((double) location.getLatitude()) / Math.pow(10, 6);
            double lon2 = ((double) location.getLongitude()) / Math.pow(10, 6);
            distances.add((getDistance(lat1, lon1, lat2, lon2, areaInfo.getDatas(0).getRadiusLength())));
        }
        for (Double distance : distances) {
            if (distance <= filterMileage) {
                return false;
            }
        }
        return true;
    }

    /**
     * 返回点到圆的距离，单位米
     *
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @param r
     * @return
     */
    private static double getDistance(double lat1, double lng1, double lat2, double lng2, double r) {
        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);
        double a = Math.abs(radLat1 - radLat2);
        double b = Math.abs(rad(lng1) - rad(lng2));

        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2)
                * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        return (Math.round(s * 1000) - r);
    }

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    private static <T> List<T> getBinaryValue(List<T> list, int startIndex, int endIndex, int deep) {
        List<T> result = new ArrayList<T>();
        if (deep <= 0) {
            return result;
        }
        int middle = (startIndex + endIndex) >> 1;
        if (middle < 0)
            return result;
        T midValue = list.get(middle);
        deep = deep - 1;
        if (deep > 0) {
            List<T> r1 = getBinaryValue(list, startIndex, middle, deep);
            result.addAll(r1);
            List<T> r2 = getBinaryValue(list, middle, endIndex, deep);
            result.addAll(r2);
        }
        result.add(midValue);
        return result;
    }

    private static int getDay(long begin) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(begin * 1000);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int result = year * 10000 + month * 100 + day;
        return result;
    }

    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        List<VehiclePassTimesRecord> areas = entity.getAreas();
        //在这里开始对统计后的数据，添加服务站所在市代码和服务站经纬度
        List<AreaInfo> allAreaInfosFromCache = terminalRuleCache.getAreaInfoForStatistic();

        if(areas!=null&&areas.size()>0){
            for (VehiclePassTimesRecord passTimesRecord : areas) {
                //如果是服务站，则计算服务站所在的市区编码
                if(passTimesRecord.getType()==5){
                    long tileIdMin = VehiclePassTimesQuadtree.getTileNumber(passTimesRecord.getLat(), passTimesRecord.getLng(), minZoom);
                    Integer[] district = districtAndTileMappingCache.getAllMap().get(tileIdMin);
                    if (district != null) {
                      passTimesRecord.setDistrictCode(district[0]);
                    }

                }
            }
        }

        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult faultResult = daServer.saveVehiclePassAreaTimes(areas);
        if (PlatformResponseResult.success_VALUE != faultResult.getNumber()) {
            logger.error("Areas store failure.");
        }
        logger.info(">>>区域车次统计数据存儲耗时:{},总结果数:{}", (System.currentTimeMillis() - startLong) / 1000.0, areas.size());
    }

    @Override
    public void execute(Long tid, List<LocationData> gpsdata, StatisticResultEntity result) {
        if (null == gpsdata || gpsdata.size() == 0) {
            return;
        }
        List<AreaInfo> allAreaInfos = new ArrayList<AreaInfo>();
        List<AreaInfo> allAreaInfosFromCache = terminalRuleCache.getAreaInfoForStatistic();
        if (allAreaInfosFromCache != null && allAreaInfosFromCache.size() > 0) {
            allAreaInfos.addAll(allAreaInfosFromCache);
        }

        // 行政区域
        int length = gpsdata.size();
        List<LocationData> list = getBinaryValue(gpsdata, 0, length - 1, binaryDepth);
        list.add(gpsdata.get(0));
        list.add(gpsdata.get(length - 1));
        for (LocationData entity : list) {
            LocationData location = entity;
            districtStatistic(tid, location, result.getAreas(), st);
        }
        // 服务站
        if (allAreaInfos == null || allAreaInfos.size() == 0) {
            return;
        }
        for (AreaInfo areaInfo : allAreaInfos) {
            if (filter(list, areaInfo)) {
                continue;
            }
            for (LocationData location : gpsdata) {
                double lat1 = ((double) areaInfo.getDatas(0).getLatitude()) / Math.pow(10, 6);
                double lon1 = ((double) areaInfo.getDatas(0).getLongitude()) / Math.pow(10, 6);
                double lat2 = ((double) location.getLatitude()) / Math.pow(10, 6);
                double lon2 = ((double) location.getLongitude()) / Math.pow(10, 6);
                if (getDistance(lat1, lon1, lat2, lon2, areaInfo.getDatas(0).getRadiusLength()) < 0) {
                    VehiclePassTimesRecord entity = getEntity(result.getAreas(), (int) areaInfo.getAreaIdentify(), 5,
                            st);
                    vehiclePassTimesUpdate(entity, tid);
                    vehiclePassTimesDetailUpdate(entity, tid, location);
                    setVehiclePassTimesLngLat(entity,areaInfo.getDatas(0).getLongitude(),areaInfo.getDatas(0).getLatitude());
                    break;
                }
            }
        }

    }

    private void districtStatistic(long tid, LocationData location, List<VehiclePassTimesRecord> list, long st) {
        int lat = location.getOriginalLat();
        int lon = location.getOriginalLng();
        long tileIdMin = VehiclePassTimesQuadtree.getTileNumber(lat, lon, minZoom);
        Integer[] district = districtAndTileMappingCache.getAllMap().get(tileIdMin);
        if (district == null) {
            return;
        }
        if (district[1] != 0) {
            VehiclePassTimesRecord city = getEntity(list, district[0], 2, st);
            vehiclePassTimesUpdate(city, tid);
            vehiclePassTimesDetailUpdate(city, tid, location);
            VehiclePassTimesRecord province = getEntity(list, district[1], 1, st);
            vehiclePassTimesUpdate(province, tid);
            vehiclePassTimesDetailUpdate(province, tid, location);
        } else {
            VehiclePassTimesRecord province = getEntity(list, district[0], 1, st);
            vehiclePassTimesUpdate(province, tid);
            vehiclePassTimesDetailUpdate(province, tid, location);
        }
    }

    private void vehiclePassTimesUpdate(VehiclePassTimesRecord entity, long tid) {
        List<Long> terminals = entity.getTerminals();
        if (terminals.size() >= 0 && !terminals.contains(tid)) {
            terminals.add(tid);
            entity.setTimes(entity.getTimes() + 1);
        }
    }
    private void setVehiclePassTimesLngLat(VehiclePassTimesRecord entity, long lng,long lat) {
       entity.setLat(lat);
        entity.setLng(lng);
    }
    private VehiclePassTimesRecord getEntity(List<VehiclePassTimesRecord> list, int district, int type, long begin) {
        for (VehiclePassTimesRecord entity : list) {
            if (entity.getType() == type && entity.getDistrict() == district) {
                return entity;
            }
        }
        VehiclePassTimesRecord entity = new VehiclePassTimesRecord();
        entity.setDay(getDay(begin));
        entity.setDistrict(district);
        entity.setType(type);
        list.add(entity);
        return entity;
    }
}
