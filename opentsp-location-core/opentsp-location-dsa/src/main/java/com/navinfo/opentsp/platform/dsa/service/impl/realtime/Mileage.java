package com.navinfo.opentsp.platform.dsa.service.impl.realtime;

import com.navinfo.opentsp.platform.dsa.rmiclient.RmiConstant;
import com.navinfo.opentsp.platform.dsa.service.RealTimeService;
import com.navinfo.opentsp.platform.dsa.service.impl.ConcurrentAlg;
import com.navinfo.opentsp.platform.dsa.service.interf.RealTime;
import com.navinfo.opentsp.platform.dsa.utils.ConfigUtils;
import com.navinfo.opentsp.platform.dsa.utils.SegmentUtils;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCPlatformResponseResult.PlatformResponseResult;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatus.VehicleStatus.Status;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.alarm.Mileages;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.entity.StatisticResultEntity;
import com.navinfo.opentsp.platform.location.protocol.rmi.statistic.service.AlarmStatisticsStoreService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * 描述: 车辆里程计算 hk
 *
 * @author admin
 * @version CopyRight (c) 2016 , hbdrawn@vip.qq.com All Rights Reserved
 *          里程米，油耗 L，电耗 %
 * @date 2016年4月29日 下午1:39:56
 **/
@Component
@Scope("prototype")
public class Mileage extends ConcurrentAlg implements RealTime {
    private static final double LimitSpeed = 2000;

    @Override
    public void execute(Long key, List<LocationData> data, StatisticResultEntity result) {
        if (data == null || data.size() == 0) {
            return;
        }
        Mileages mileages = milleagesCache.getAll().get(key);
        for (int i = 0; i < data.size(); i++) {
            LocationData location = data.get(i);
            if ((location.getStatus() & Status.acc_VALUE) == 0) {
                continue;
            }
            if (mileages == null || DateUtils.differDay(DateUtils.calendar(mileages.getStartDate()),
                    DateUtils.calendar(location.getGpsDate())) != 0) {
                mileages = new Mileages();
                mileages.setTerminalID(key);
                mileages.setBeginLat(location.getLatitude());
                mileages.setBeginLng(location.getLongitude());
                mileages.setEndLat(location.getLatitude());
                mileages.setEndLng(location.getLongitude());
                mileages.setStartDate(location.getGpsDate());
                mileages.setEndDate(location.getGpsDate());
                mileages.setGpsMileage(0);
                mileages.setBeginGpsMileage(location.getMileage());
                mileages.setEndGpsMileage(location.getMileage());
                for (VehicleStatusData statusData : location.getStatusAddition().getStatusList()) {
                    switch (statusData.getTypes().getNumber()) {
                        case StatusType.mileage_VALUE:
                            if (statusData.getStatusValue() * 10 != 0) {
                                mileages.setBeginMileage(statusData.getStatusValue() * 10);
                                mileages.setEndMileage(statusData.getStatusValue() * 10);
                            }
                            break;
                        case StatusType.mileageDD_VALUE:
                            if (statusData.getStatusValue() * 10 != 0) {
                                //mileages.setPreMeMileage(statusData.getStatusValue() * 10);
                                mileages.setPreMeDate(location.getGpsDate());
                                mileages.setBeginMeMileage(statusData.getStatusValue() * 10);
                                mileages.setEndMeMileage(statusData.getStatusValue() * 10);
                            }
                            break;
                        case StatusType.totalFuelConsumption_VALUE:
                            if (statusData.getStatusValue() != 0) {
                                mileages.setPreOil(statusData.getStatusValue() / 100f);
                            }
                            break;
                        case StatusType.oilValue_VALUE:
                            if (statusData.getStatusValue() !=0){
                                mileages.setPreOilValue(statusData.getStatusValue() / 100f);
                            }
                            break;
                        case StatusType.integralFuelConsumption_VALUE:
                            if (statusData.getStatusValue() !=0){
                                mileages.setPreFuelOil(statusData.getStatusValue() / 100f);
                            }
                            break;
                    }
                }
                if (location.getBatteryPower() != 0) {
                    mileages.setPreElectric(location.getBatteryPower());
                }
                //mileages.setPreGpsMileages(location.getMileage());
                milleagesCache.getAll().put(key, mileages);
            } else {
                mileages.setEndLat(location.getLatitude());
                mileages.setEndLng(location.getLongitude());
                mileages.setStaticDate((int) (location.getGpsDate() - mileages.getStartDate()));
                for (VehicleStatusData statusData : location.getStatusAddition().getStatusList()) {
                    switch (statusData.getTypes().getNumber()) {
                        //StatusType.mileage_VALUE  ---> ECU里程、CAN里程、整车里程
                        case StatusType.mileage_VALUE:
                            long value = statusData.getStatusValue() * 10;
                            //2017-06-02 两处修改：① 整车里程为0  ②上下两个点的整车里程一样，不修改时间，保留上次正常点的结束时间
                            if(value == 0 || mileages.getEndMileage() == value){break;}
                            if (mileages.getEndMileage() != 0 && (value - mileages.getEndMileage() > 0) && checkMileage(
                                    mileages.getEndMileage(), value, mileages.getEndDate(), location.getGpsDate())) {
                                mileages.setCanMileage((value - mileages.getEndMileage()) + mileages.getCanMileage());
                            }
                            mileages.setEndMileage(value);
                            break;
                        //StatusType.mileageDD_VALUE --->仪表里程
                        case StatusType.mileageDD_VALUE:
                            long meter = statusData.getStatusValue() * 10;
                            if(meter == 0 || mileages.getEndMeMileage() == meter){break;}
                            if ((meter - mileages.getEndMeMileage() > 0) && mileages.getPreMeDate() != 0 && checkMileage(
                                    mileages.getEndMeMileage(), meter, mileages.getPreMeDate(), location.getGpsDate())) {
                                mileages.setMeterMileage((meter - mileages.getEndMeMileage()) + mileages.getMeterMileage());
                            }
                            mileages.setPreMeDate(location.getGpsDate());
                            mileages.setEndMeMileage(meter);
                            break;
                        //StatusType.totalFuelConsumption_VALUE --->ECU总油耗
                        case StatusType.totalFuelConsumption_VALUE:
                            float oil = statusData.getStatusValue() / 100f;
                            if (mileages.getPreOil() != 0 && oil - mileages.getPreOil() > 0 && oil - mileages.getPreOil() < 50) {
                                mileages.setOilConsumption(oil - mileages.getPreOil() + mileages.getOilConsumption());
                            }
                            mileages.setPreOil(oil);
                            break;
                        //StatusType.integralFuelConsumption_VALUE --> 积分总油耗
                        case StatusType.integralFuelConsumption_VALUE:
                            float fuelOil = statusData.getStatusValue() / 100f;
                            if (mileages.getPreFuelOil() != 0 && fuelOil - mileages.getPreFuelOil() > 0 && fuelOil - mileages.getPreFuelOil() < 50) {
                                mileages.setFuelOil(fuelOil - mileages.getPreFuelOil() + mileages.getFuelOil());
                            }
                            mileages.setPreFuelOil(fuelOil);
                            break;
                        //StatusType.oilValue_VALUE --》剩余油量
                        case StatusType.oilValue_VALUE:
                            float oilValue = statusData.getStatusValue() / 100f;
                            if (oilValue != 0 && mileages.getPreOilValue() - oilValue > 0){
                                mileages.setOilValue(mileages.getPreOilValue() - oilValue + mileages.getOilValue());
                            }
                            mileages.setPreOilValue(oilValue);
                            break;
                    }
                }
                if (location.getBatteryPower() != 0 && mileages.getPreElectric() - location.getBatteryPower() > 0) {
                    mileages.setElectricConsumption(
                            mileages.getPreElectric() - location.getBatteryPower() + mileages.getElectricConsumption());
                }
                mileages.setPreElectric(location.getBatteryPower());
                if (location.getMileage() > mileages.getEndGpsMileage() && checkMileage(mileages.getEndGpsMileage(),
                        location.getMileage(), mileages.getPreGpsDate(), location.getGpsDate())) {
                    mileages.setGpsMileage(mileages.getGpsMileage() + (location.getMileage() - mileages.getEndGpsMileage()));
                }
                mileages.setPreGpsDate(location.getGpsDate());
                mileages.setEndGpsMileage(location.getMileage());
                mileages.setEndDate(location.getGpsDate());
            }
        }
        if (mileages != null) {
            result.getMileage().add(mileages);
        }

    }

    /**
     * @param lastMileage    Long 上个信息点的里程,单位米
     * @param currentMileage Long 当前信息点的里程,单位米
     * @param lastTime       Long 上个信息点的时间,单位秒
     * @param currentTime    Long 当前信息点的时间,单位秒
     * @return Long 行驶里程
     */
    private boolean checkMileage(long lastMileage, long currentMileage, Long lastTime, Long currentTime) {
        // 根据里程+时间计算出速度
        long differTime = currentTime - lastTime;
        long differMileage = currentMileage - lastMileage;
        double speed = (differMileage / (double) differTime) * 3.6;
        if (speed > LimitSpeed) {
            return false;
        }
        return true;
    }

    @Override
    public void saveResult(StatisticResultEntity entity) throws Exception {
        long startLong = System.currentTimeMillis();
        // 結果入库
        AlarmStatisticsStoreService daServer = (AlarmStatisticsStoreService) getRmiclient().rmiBalancerRequest(RmiConstant.SAVE_STATIC_TER_DATA);
        PlatformResponseResult redis = daServer.saveMileageAndOilDataToStaticRedis(entity.getMileage());
        if (PlatformResponseResult.success_VALUE != redis.getNumber()) {
            logger.error("Milage store  to redis failure.");
        }
        // 当天最后一次，即segment=2880时，遍历本地里程缓存，将当天里程缓存推送至mongo
        if (currentSeg == 2879) {
            List<Mileages> lastMileages = new ArrayList<Mileages>();
            Calendar cal = SegmentUtils.getRTEndTime(2879);
            cal.add(Calendar.DATE, -1);
            long dayStart = cal.getTimeInMillis() / 1000;
            cal.add(Calendar.DATE, 1);
            long dayEnd = cal.getTimeInMillis() / 1000;
            logger.debug(">>>>>推送至mongo:{}-{}", DateUtils.format(dayStart, DateUtils.DateFormat.YYMMDDHHMMSS),
                    DateUtils.format(dayEnd, DateUtils.DateFormat.YYMMDDHHMMSS));
            Map<Long, Mileages> all = milleagesCache.getAll();
            for (Long key : all.keySet()) {
                Mileages mileages = all.get(key);
                if (mileages.getStartDate() >= dayStart && mileages.getStartDate() < dayEnd) {
                    lastMileages.add(mileages);
                }
            }
            PlatformResponseResult milageStatus = daServer.saveBatchMilagesInfo(lastMileages, dayStart);
            if (PlatformResponseResult.success_VALUE != milageStatus.getNumber()) {
                logger.error("Milage store failure.");
            }
            logger.debug(">>>>>推送至mongo成功:{}", lastMileages.size());
        }
        logger.debug(">>>里程数据存儲耗时:{},总结果数:{}", (System.currentTimeMillis() - startLong) / 1000.0,
                entity.getMileage().size());

    }

    /*
     * 历史回滚实现逻辑，生产环境请注释该方法
     */
    public void historyCal() {
        try {
            // 发起计算算法，并返回计算结果
            StatisticResultEntity entity = excute();
            // 将计算结果入库保存
            saveResult(entity);
            // 将此次操作结果保存到配置文件
            ConfigUtils.writePros("history.milleage" + RealTimeService.FINISHSEG, String.valueOf(currentSeg));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
