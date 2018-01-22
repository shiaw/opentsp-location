package com.navinfo.opentsp.platform.dsa.service.impl.segareainfo;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.Segment4AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown.VehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.dataaccess.common.LCAreaInfo.AreaInfo;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesDetail;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesRecord;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 服务站内最新车辆信息，存储到redis中，覆盖存储。
 *
 * @author hk
 */
@Component
@Scope("prototype")
public class LastestVehiclePassAreaTimes extends ConcurrentAlg implements Segment4AreaInfo {
    private static final double EARTH_RADIUS = 6378.137;

    private static void vehiclePassTimesDetailUpdate(VehiclePassTimesRecord entity, long tid, LocationData location) {
        // 只增加服务站的车辆经过详情
        VehiclePassTimesDetail detail = entity.getVehiclePassTimesDetail(tid);
        if (detail == null) {
            detail = new VehiclePassTimesDetail();
            detail.setTid(tid);
            detail.setFaultCode(false);
            entity.addVehiclePassTimesDetail(tid, detail);
            entity.getTerminals().add(tid);
            entity.setTimes(entity.getTimes() + 1);
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
    }

    private static VehiclePassTimesRecord getEntity(List<VehiclePassTimesRecord> list, int district) {
        for (VehiclePassTimesRecord entity : list) {
            if (entity.getDistrict() == district) {
                return entity;
            }
        }
        VehiclePassTimesRecord entity = new VehiclePassTimesRecord();
        entity.setDistrict(district);
        entity.setType(5);
        list.add(entity);
        return entity;
    }
    private void setVehiclePassTimesLngLat(VehiclePassTimesRecord entity, long lng,long lat) {
        entity.setLat(lat);
        entity.setLng(lng);
    }
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

    @Override
    public void execute(Long tid, List<LocationData> gpsdata, StatisticResultEntity result) {
        if (null == gpsdata || gpsdata.size() == 0) {
            return;
        }

        List<AreaInfo> allAreaInfos = new ArrayList<AreaInfo>();
        List<AreaInfo> allAreaInfosCache = terminalRuleCache.getAreaInfoForStatistic();
        if (allAreaInfosCache != null && allAreaInfosCache.size() > 0) {
            allAreaInfos.addAll(allAreaInfosCache);
        }

        if (allAreaInfos == null || allAreaInfos.size() == 0) {
            return;
        }
        for (AreaInfo areaInfo : allAreaInfos) {
            LocationData location = gpsdata.get(0);
            double lat1 = ((double) areaInfo.getDatas(0).getLatitude()) / Math.pow(10, 6);
            double lon1 = ((double) areaInfo.getDatas(0).getLongitude()) / Math.pow(10, 6);
            double lat2 = ((double) location.getLatitude()) / Math.pow(10, 6);
            double lon2 = ((double) location.getLongitude()) / Math.pow(10, 6);
            if (getDistance(lat1, lon1, lat2, lon2, areaInfo.getDatas(0).getRadiusLength()) < 0) {
                VehiclePassTimesRecord entity = getEntity(result.getLastestAreas(), (int) areaInfo.getAreaIdentify());
                vehiclePassTimesDetailUpdate(entity, tid, location);
                setVehiclePassTimesLngLat(entity,areaInfo.getDatas(0).getLongitude(),areaInfo.getDatas(0).getLatitude());
                break;
            }
        }

    }

    @Override
    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        if (entity.getLastestAreas() == null || entity.getLastestAreas().size() == 0) {
            logger.error("LastestVehicleInfo size 0 ..");
            return;
        }
        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult toStaticRedis = daServer.saveLastestVehicleInfoToStaticRedis(entity.getLastestAreas());
        if (PlatformResponseResult.success_VALUE != toStaticRedis.getNumber()) {
            logger.error("LastestVehicleInfo store failure.");
        }
        logger.debug(">>>车次区域数据存儲耗时:{},总结果数:{}", (System.currentTimeMillis() - startLong) / 1000.0, entity
                .getLastestAreas().size());
    }

}
