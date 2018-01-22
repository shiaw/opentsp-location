package com.navinfo.opentsp.platform.da.core.persistence.redis.local;

/**
 * @author Lenovo
 * @date 2016-12-13
 * @modify
 * @copyright
 */

import com.navinfo.opentsp.platform.da.core.persistence.redis.entity.NewLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;

import java.util.List;


public class LocataDataBuilderUtil {

    public static LocationData builder(LocationData data, NewLocationData newData) {
        LocationData.Builder builder = LocationData.newBuilder();
        builder.setAlarm(data.getAlarm());
        builder.setStatus(data.getStatus());
        builder.setLongitude(newData.getLongitude());
        builder.setLatitude(newData.getLatitude());
        builder.setOriginalLat(data.getOriginalLat());
        builder.setOriginalLng(data.getOriginalLng());
        builder.setHeight(data.getHeight());
        builder.setSpeed(data.getSpeed());
        builder.setDirection(data.getDirection());
        builder.setGpsDate(data.getGpsDate());
        builder.setMileage(data.getMileage());
        builder.setReceiveDate(data.getReceiveDate());
        builder.setIsPatch(data.getIsPatch());
        builder.setOil(data.getOil());
        builder.setRecorderSpeed(data.getRecorderSpeed());
        if (data.getSpeedAdditionCount() > 0) {
            for (SpeedAddition speed : data.getSpeedAdditionList()) {
                builder.addSpeedAddition(speed);
            }
        }
        if (data.getAreaAdditionCount() > 0) {
            for (LCLocationData.AreaAddition areaAddition : data.getAreaAdditionList()) {
                builder.addAreaAddition(areaAddition);
            }
        }
        if (data.getRouteAdditionCount() > 0) {
            for (LCLocationData.RouteAddition routeAddition : data.getRouteAdditionList()) {
                builder.addRouteAddition(routeAddition);
            }
        }
        if (data.getTemAlarmCount() > 0) {
            for (LCLocationData.TemAddition terAddition : data.getTemAlarmList()) {
                builder.addTemAlarm(terAddition);
            }
        }
        builder.setStarStatus(data.getStarStatus());
        builder.setStarNumber(data.getStarNumber());
        if (data.getAlarmIdentifyCount() > 0) {
            for (Integer integer : data.getAlarmIdentifyList()) {
                builder.addAlarmIdentify(integer);
            }
        }
        builder.setSignalStatus(data.getSignalStatus());
        builder.setIoStatus(data.getIoStatus());
        builder.setAnalogAD0(data.getAnalogAD0());
        builder.setAnalogAD1(data.getAnalogAD1());
        builder.setSignalStrength(data.getSignalStrength());
        builder.setSatelliteNumber(data.getSatelliteNumber());
        builder.setIsValid(data.getIsValid());
        if (data.getDefenceAdditionCount() > 0) {
            for (KeyPointFenceAddition key : data.getDefenceAdditionList()) {
                builder.addDefenceAddition(key);
            }
        }
        if (data.getParkingAdditionCount() > 0) {
            for (LCLocationData.OvertimeParkingAddition overtimeParkingAddition : data.getParkingAdditionList()) {
                builder.addParkingAddition(overtimeParkingAddition);
            }
        }

        VehicleStatusAddition vehicle = getStatusAddtion(data.getStatusAddition(), newData);
        builder.setStatusAddition(vehicle);  //车辆状态附加信息

        builder.setBreakdownAddition(data.getBreakdownAddition());
        builder.setAdditionAlarm(data.getAdditionAlarm());
        builder.setAnalysisData(data.getAnalysisData());
        builder.setBatteryPower(data.getBatteryPower());
        if (data.getModuleVoltagesCount() > 0) {
            for (ModuleVoltage mvModuleVoltage : data.getModuleVoltagesList()) {
                builder.addModuleVoltages(mvModuleVoltage);
            }
        }
        builder.setElectricVehicle(data.getElectricVehicle());
        builder.setBatteryInfo(data.getBatteryInfo());
        builder.setAlarmFilter(data.getAlarmFilter());
        if (data.getStaytimeParkingAdditionCount() > 0) {
            for (LCLocationData.StaytimeParkingAddition overtimeParkingAddition : data.getStaytimeParkingAdditionList()) {
                builder.addStaytimeParkingAddition(overtimeParkingAddition);
            }
        }
        builder.setStandardMileage(newData.getStandardMileage());
        builder.setStandardFuelCon(newData.getStandardFuelCon());
        return builder.build();
    }

    public static VehicleStatusAddition getStatusAddtion(VehicleStatusAddition vehicle, NewLocationData newData) {
        VehicleStatusAddition.Builder builder = VehicleStatusAddition.newBuilder();
        VehicleStatusData.Builder vBuilder = VehicleStatusData.newBuilder();
        vBuilder = VehicleStatusData.newBuilder();
        vBuilder.setTypes(StatusType.mileage);
        vBuilder.setStatusValue(newData.getCanMileage());
        builder.addStatus(vBuilder.build());
        vBuilder = VehicleStatusData.newBuilder();
        vBuilder.setTypes(StatusType.oilValue);
        vBuilder.setStatusValue(newData.getOilValue());
        builder.addStatus(vBuilder.build());
        vBuilder = VehicleStatusData.newBuilder();
        vBuilder.setTypes(StatusType.rotation);
        vBuilder.setStatusValue(newData.getRotation());
        builder.addStatus(vBuilder.build());
        vBuilder = VehicleStatusData.newBuilder();
        vBuilder.setTypes(StatusType.cumulativeRunningTime);
        vBuilder.setStatusValue(newData.getRunTime());
        builder.addStatus(vBuilder.build());
        if (vehicle.getStatusCount() > 0) {
            List<VehicleStatusData> list = vehicle.getStatusList();
            for (VehicleStatusData data : list) {
                if (data.getTypes() == StatusType.mileage || data.getTypes() == StatusType.oilValue || data.getTypes() == StatusType.rotation
                        || data.getTypes() == StatusType.cumulativeRunningTime) {//整车里程
                    continue;
                } else {
                    vBuilder = VehicleStatusData.newBuilder();
                    vBuilder.setTypes(data.getTypes());
                    vBuilder.setStatusValue(data.getStatusValue());
                    builder.addStatus(vBuilder.build());
                }
            }
        }
        return builder.build();
    }
}
