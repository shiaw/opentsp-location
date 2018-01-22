package com.navinfo.opentsp.platform.dsa.service.impl.segdistrict;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.SegmentForDistrict;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassInDistrict;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.VehiclePassTimesQuadtree;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope("prototype")
public class LastestVehiclePassDistrictTimes extends ConcurrentAlg implements SegmentForDistrict {
    private static final int minZoom = 15;
    public static Logger logger = LoggerFactory.getLogger(LastestVehiclePassDistrictTimes.class);

    @Override
    public void execute(Long tid, List<LocationData> datas, StatisticResultEntity entity) {
        if (null == datas || datas.size() == 0) {
            return;
        }
        for (LocationData location : datas) {
            districtStatistic(tid, location.getOriginalLat(), location.getOriginalLng(), entity.getDistricts());
        }
    }

    private void districtStatistic(long tid, long lat, long lon, List<VehiclePassInDistrict> list) {
        long tileIdMin = VehiclePassTimesQuadtree.getTileNumber(lat, lon, minZoom);
        Integer[] district = districtAndTileMappingCache.getAllMap().get(tileIdMin);
        if (district == null) {
            return;
        }
        if (district[1] != 0) {
            VehiclePassInDistrict city = getEntity(list, district[0]);
            vehiclePassTimesUpdate(city, tid);
            VehiclePassInDistrict province = getEntity(list, district[1]);
            vehiclePassTimesUpdate(province, tid);
        } else {
            VehiclePassInDistrict province = getEntity(list, district[0]);
            vehiclePassTimesUpdate(province, tid);
        }
    }

    private VehiclePassInDistrict getEntity(List<VehiclePassInDistrict> list, int district) {
        for (VehiclePassInDistrict entity : list) {
            if (entity.getDistrict() == district) {
                return entity;
            }
        }
        VehiclePassInDistrict entity = new VehiclePassInDistrict();
        entity.setDistrict(district);
        list.add(entity);
        return entity;
    }

    private void vehiclePassTimesUpdate(VehiclePassInDistrict entity, long tid) {
        List<Long> terminals = entity.getTerminals();
        if (terminals.size() >= 0 && !terminals.contains(tid)) {
            terminals.add(tid);
        }
    }

    @Override
    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        List<VehiclePassInDistrict> districts = entity.getDistricts();
        if (districts != null && districts.size() > 0) {
            AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
            PlatformResponseResult faultResult = daServer.saveDistrictTimesToStaticRedis(districts);
            if (PlatformResponseResult.success_VALUE != faultResult.getNumber()) {
                logger.error("districts store failure.");
            }
            logger.debug(">>>行政区域车次统计数据存儲耗时:{},总结果数:{}", (System.currentTimeMillis() - startLong) / 1000.0,
                    districts.size());
        }
    }
}
