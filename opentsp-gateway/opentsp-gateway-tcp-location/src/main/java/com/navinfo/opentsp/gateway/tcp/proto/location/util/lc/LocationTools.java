package com.navinfo.opentsp.gateway.tcp.proto.location.util.lc;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.LastMileageOilTypeCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilType;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType.StatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author:HOUQL on 2017/6/19.
 * 处理位置数据中的相关逻辑
 * @modified by chenjie 2017/12/18
 * 1、里程标记增加积分里程(3),终端里程改为4
 * 2、新增速度标记
 */
@Component
public class LocationTools {
    public  Logger log = LoggerFactory.getLogger(LocationTools.class);

    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;
    /**
     * 将里程、油耗合并到标准值上
     * 里程有效性规则由高到低为：1 仪表里程km(mileageDD)、2 CAN里程（ECU） km(mileage)、3、积分里程km(differentialMileage)、4 终端里程(m)(mileage)
     * 油耗有效性规则由高到低为：1 CAN燃油总消耗L(totalFuelConsumption)、2 积分油耗L(integralFuelConsumption)
     * 速度有效性规则由高到低为：1 仪表速度Km/h(tachographVehicleSpeed)、2 车轮速度Km/h(wheelBasedVehicleSpd)、3 GPS速度Km/h(speed)
     *
     * @param builder
     * @param uniquMark
     */
    public void combineStandValue(LCLocationData.LocationData.Builder builder, String uniquMark) {

        TerminalMileageOilType mileageOilTypeEntry=lastMileageOilTypeCache.getOilMileageTime(uniquMark);
        log.error("终端按照规则进行数据合并:"+uniquMark);
        //仪表里程
        float mileageDD = 0L;
        //can里程(ecu)
        float canMileage = 0L;
        // 积分里程
        long differentialMileage = 0L;
        //gps里程
        long terminalMileage = builder.getMileage();

        //燃油总消耗
        float totalFuelConsumption = 0L;
        //积分油耗
        float integralFuelConsumption = 0L;

        // 仪表车速
        long tachographVehicleSpeed = 0L;
        // 车轮车速
        long wheelBasedVehicleSpd = 0L;
        // gps车速   标准车速统一放大100倍以保留精度
        int gpsSpeed = builder.getSpeed() * 100;

        List<VehicleStatusData> vehicleStatusDatas = builder.getStatusAddition().getStatusList();
        //有附加信息则解析出来，如果没有则相关值为0
        if(vehicleStatusDatas != null && vehicleStatusDatas.size() > 0){
            for (VehicleStatusData vehicleStatusData : vehicleStatusDatas) {
                //can里程(ecu)
                if (vehicleStatusData.getTypes() == StatusType.mileage) {
                    canMileage = (float) vehicleStatusData.getStatusValue() / 100;
                }
                //仪表里程
                else if (vehicleStatusData.getTypes() == StatusType.mileageDD) {
                    mileageDD = (float)vehicleStatusData.getStatusValue() / 100;
                }
                // 积分里程
                else if (vehicleStatusData.getTypes() == StatusType.differentialMileage) {
                    differentialMileage = vehicleStatusData.getStatusValue() / 100;
                }
                //燃油总消耗
                else if (vehicleStatusData.getTypes() == StatusType.totalFuelConsumption) {
                    totalFuelConsumption =(float) vehicleStatusData.getStatusValue() / 100;
                }
                //积分油耗
                else if (vehicleStatusData.getTypes() == StatusType.integralFuelConsumption) {
                    integralFuelConsumption = (float)vehicleStatusData.getStatusValue() / 100;
                }
                // 仪表车速
                else if (vehicleStatusData.getTypes() == StatusType.tachographVehicleSpeed) {
                    tachographVehicleSpeed = vehicleStatusData.getStatusValue();
                }
                // 车轮车速
                else if (vehicleStatusData.getTypes() == StatusType.wheelBasedVehicleSpd) {
                    wheelBasedVehicleSpd = vehicleStatusData.getStatusValue();
                }
            }
        }

        //如果有标记则根据标记赋标准值
        if (mileageOilTypeEntry != null) {
            //标准里程
            int mileageType = mileageOilTypeEntry.getMileageType();
            //仪表里程
            if (mileageType == Constant.BOARD_MILEAGE) {
                builder.setStandardMileage(mileageDD);
            }
            //can里程
            else if (mileageType == Constant.CAN_MILEAGE) {
                builder.setStandardMileage(canMileage);
            }
            //积分里程
            else if (mileageType == Constant.DIFFERENTIAL_MILEAGE) {
                builder.setStandardMileage((float)differentialMileage);
            }
            //GPS里程/终端里程  或 没打上标记
            else if (mileageType == Constant.GPS_MILEAGE || mileageType == Constant.NO_FLAG) {
                //原单位是m，改成km
                builder.setStandardMileage(new BigDecimal(terminalMileage).divide(new BigDecimal(1000),
                        3, BigDecimal.ROUND_HALF_UP).floatValue());
            }

            //标准油耗
            int oilType = mileageOilTypeEntry.getOilType();
            //燃油总消耗
            if (oilType == Constant.TOTAL_FUEL_CONSUMPTION) {
                builder.setStandardFuelCon(totalFuelConsumption);
            }
            //积分油耗  或者没打上标记
            else if (oilType == Constant.INTEGRAL_CONSUMPTION || oilType == Constant.NO_FLAG) {
                builder.setStandardFuelCon(integralFuelConsumption);
            }

            //标准速度
            int speedType = mileageOilTypeEntry.getSpeedType();
            //仪表速度
            if(speedType == Constant.BOARD_SPEED){
                builder.setElectricVehicle(tachographVehicleSpeed);
            }
            //车轮速度
            else if(speedType == Constant.WHEEL_SPEED){
                builder.setElectricVehicle(wheelBasedVehicleSpd);
            }
            //GPS速度 或 没打上标记
            else if(speedType == Constant.GPS_SPEED || speedType == Constant.NO_FLAG){
                builder.setElectricVehicle(gpsSpeed);
            }

            log.info("终端:"+uniquMark+",里程类型："+mileageType+",油耗类型:"+oilType+",速度类型:"+speedType);
        }
        //尚未打上标记，默认取最低级别
        else{
            //原单位是m，改成km
            builder.setStandardMileage(new BigDecimal(terminalMileage).divide(new BigDecimal(1000),
                    3, BigDecimal.ROUND_HALF_UP).floatValue());
            builder.setStandardFuelCon(integralFuelConsumption);
            builder.setElectricVehicle(gpsSpeed);
        }
    }
}
