package com.navinfo.opentsp.gateway.tcp.proto.location.util.lc;

import com.alibaba.fastjson.JSON;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSatusSyncCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.CANDataEntity;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalStateSync;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.*;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author:qingqi
 * */
public class LocationAdditionProcess_BigEndian {

    private static final Logger logger = LoggerFactory.getLogger(LocationAdditionProcess_BigEndian.class);

    /**
     * 状态数据
     *
     * @param statusAddition
     * @return
     */
    public static LCLocationData.VehicleStatusAddition.Builder getStatusAddition(byte[] statusAddition, String uniqueMark) {
        /**
         * 附加信息的格式
         *
         * 状态总数     是否故障         扩展ID(PGN)              数据（位置）                                                                                                         扩展ID(PGN)          数据（位置）                                         扩展ID(PGN)             数据（位置）
         *   03    01          18 FE F6 00       00    00    00    00    00    00    00    00    18 FE F6 00    00 00 00 00 00 00 00 00   18 FE F6 00     00 00 00 00 00 00 00 00
         *                                       Byte1 Byte2 Byte3 Byte4 Byte5 Byte6 Byte7 Byte8
         */
        LCLocationData.VehicleStatusAddition.Builder vsaddBuilder = LCLocationData.VehicleStatusAddition.newBuilder();
        //开始下标
        int statusAdditionIndex = 0;
        if (statusAddition.length >= 2) {
            //下标从扩展ID开始
            statusAdditionIndex += 2;
            while (statusAddition.length > statusAdditionIndex) {
                LCVehicleStatusData.VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();

                int pgnId = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex, 4), 4);
                switch (pgnId) {
                    //	Byte5-8	整车里程（0.125Km/bit）
                    case 0x18FEE000:
                        if (!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4)))) {
                            //截取的 5~8
                            double mileage = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4), 4);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            //乘以100 存储
                            statusData.setStatusValue((long) (mileage * 100 * 0.125));
                            statusData.setTypes(LCStatusType.StatusType.mileage);
                            vsaddBuilder.addStatus(statusData.build());

                            print(uniqueMark, "0x18FEE000", "mileage Byte5-8	整车里程（0.125Km/bit）:", (long) (mileage * 0.125), statusAddition, statusAdditionIndex);
                        }

                        statusAdditionIndex += 12;
                        break;
                    //    Byte1	颗粒捕集器进气压力(0.5kPa/bit)  ; Byte2	相对增压压力(2kPa/bit)  ; Byte4	绝对增压压力(2kPa/bit) ; Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
                    case 0x18FEF600:
                        //Byte1	颗粒捕集器进气压力(0.5kPa/bit)  ;
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))) {
                            //Byte1
                            double dpfPressure = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (dpfPressure * 0.5 * 100));
                            statusData.setTypes(LCStatusType.StatusType.dpfPressure);
                            vsaddBuilder.addStatus(statusData.build());

                            print(uniqueMark, "0x18FEF600", "dpfPressure Byte1	颗粒捕集器进气压力(0.5kPa/bit):", (long) (dpfPressure * 0.5), statusAddition, statusAdditionIndex);
                        }

                        //Byte2	相对增压压力(2kPa/bit)
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))) {
                            //Byte2
                            double relativePressure = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (relativePressure * 2 * 100));
                            statusData.setTypes(LCStatusType.StatusType.relativePressure);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEF600", "relativePressure Byte2	相对增压压力(2kPa/bit):", (long) (relativePressure * 2), statusAddition, statusAdditionIndex);
                        }

                        //Byte4	绝对增压压力(2kPa/bit) ;
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1)))) {
                            //Byte4
                            double absolutePressure = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (absolutePressure * 2 * 100));
                            statusData.setTypes(LCStatusType.StatusType.absolutePressure);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEF600", "absolutePressure Byte4	绝对增压压力(2kPa/bit):", (long) (absolutePressure * 2), statusAddition, statusAdditionIndex);
                        }

                        //Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 5, 2)))) {
                            //Byte6-7
                            double exhaustTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 5, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) ((exhaustTem * 0.03125 - 273) * 100));
                            statusData.setTypes(LCStatusType.StatusType.exhaustTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEF600", "exhaustTem  Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃):", (long) ((exhaustTem * 0.03125 - 273)), statusAddition, statusAdditionIndex);
                        }

                        statusAdditionIndex += 12;
                        break;
                    //	Byte1	大气压力(0.5kPa/bit,Offset:0hPa) ; Byte2-3	发动机舱内部温度(Not realized，Set 0xFFFF) ; Byte4-5	大气温度(0.03125℃/bit,Offset:-273℃) ; Byte6	进气温度(1℃/bit,Offset:-40℃);Byte7-8	路面温度(Not realized，Set 0xFFFF)
                    case 0X18FEF500:
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))) {
                            //Byte1
                            double atmosphericPressure = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (atmosphericPressure * 100 * 0.5));
                            statusData.setTypes(LCStatusType.StatusType.atmosphericPressure);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0X18FEF500", "atmosphericPressure Byte1	大气压力(0.5kPa/bit,Offset:0hPa) :", (long) (atmosphericPressure * 0.5), statusAddition, statusAdditionIndex);

                        }


                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 2)))) {
                            //Byte2-3
                            double engineTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (engineTem * 100));
                            statusData.setTypes(LCStatusType.StatusType.engineTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0X18FEF500", "engineTem Byte2-3	发动机舱内部温度(Not realized，Set 0xFFFF):", (long) (engineTem), statusAddition, statusAdditionIndex);

                        }


                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 2)))) {
                            //Byte4-5
                            double atmosphericTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) ((atmosphericTem * 0.03125 - 273) * 100));
                            statusData.setTypes(LCStatusType.StatusType.atmosphericTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0X18FEF500", "atmosphericTem  Byte4-5	大气温度(0.03125℃/bit,Offset:-273℃):", (long) ((atmosphericTem * 0.03125 - 273)), statusAddition, statusAdditionIndex);

                        }


                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 5, 1)))) {
                            //Byte6
                            double intakeAirTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 5, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) ((intakeAirTem - 40) * 100));
                            statusData.setTypes(LCStatusType.StatusType.intakeAirTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0X18FEF500", "intakeAirTem  Byte6	进气温度(1℃/bit,Offset:-40℃);:", (long) ((intakeAirTem - 40)), statusAddition, statusAdditionIndex);

                        }


                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 6, 2)))) {
                            //Byte7-8
                            double pavementTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 6, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (pavementTem * 100));
                            statusData.setTypes(LCStatusType.StatusType.pavementTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0X18FEF500", "pavementTem  Byte7-8	路面温度(Not realized，Set 0xFFFF):", (long) (pavementTem), statusAddition, statusAdditionIndex);

                        }


                        statusAdditionIndex += 12;
                        break;
                    //	Byte4	冷启动灯（1表示正常，0表示异常）
                    case 0x18FEE400:
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1)))) {
                            //Byte4
                            double lampStatus = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (lampStatus * 100));
                            statusData.setTypes(LCStatusType.StatusType.lampStatus);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEE400", "pavementTem Byte4	冷启动灯（1表示正常，0表示异常）:", (long) (lampStatus), statusAddition, statusAdditionIndex);
                        }

                        statusAdditionIndex += 12;
                        break;
                    //	Byte1Bit1-2	油中有水指示（1表示正常，0表示异常）:   Bit1-2: 000011 = 3
                    case 0x18FEFF00:
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))) {
                            //截取的 1
                            int waterInOilStatus = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            switch (waterInOilStatus & 3) {
                                // 00:表示异常   01:表示正常    10:Error Value   11:Not available
                                case 0:
                                    statusData.setStatusValue(0);
                                    statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
                                    break;
                                case 1:
                                    statusData.setStatusValue(1 * 100);
                                    statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
                                    break;
                                //10:Error Value   11:Not available   认为是数据异常
                                case 2:
                                    statusData.setStatusValue(0);
                                    statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
                                    break;
                                //10:Error Value   11:Not available   认为是数据异常
                                case 3:
                                    statusData.setStatusValue(0);
                                    statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
                                    break;
                                default:
                                    break;
                            }
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEFF00", "waterInOilStatus Byte1Bit1-2	油中有水指示（1表示正常，0表示异常）:   Bit1-2: 000011 = 3:", waterInOilStatus & 3, statusAddition, statusAdditionIndex);
                        }
                        statusAdditionIndex += 12;
                        break;
                    //燃油消耗总量(升) 大端解析（江淮星海终端）
                    case 0x18FEE900:
                        if (!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4)))) {
                            //截取的 5~8
                            double totalFuelConsumption = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4), 4);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (totalFuelConsumption * 100 * 0.5));
                            statusData.setTypes(LCStatusType.StatusType.totalFuelConsumption);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEE900", "totalFuelConsumption Byte5-8	车辆当前油量（0.5 L/bit,）: ", (long) (totalFuelConsumption * 100 * 0.5), statusAddition, statusAdditionIndex);
                        }
                        statusAdditionIndex += 12;
                        break;
//				case 0x18FEE900:  //	Byte5-8	车辆当前油量（0.5 L/bit,）
                    //当前油量 协议中没有定义，暂时为空
//					if(!Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+4, 4)).equals("FFFFFFFF")){
//						double oilValue =  Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+4, 4)), 4);//截取的 5~8
//						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
//						statusData.setStatusValue((long) (oilValue*100*0.5));
//						statusData.setTypes(LCStatusType.StatusType.oilValue);
//						vsaddBuilder.addStatus(statusData.build());
//						print(uniqueMark,"0x18FEE900","oilValue Byte5-8	车辆当前油量（0.5 L/bit,）: " ,(long) (oilValue*100*0.5),statusAddition, statusAdditionIndex );
//					}

//					statusAdditionIndex +=12;
//					break;
                    //	Byte4-5	车辆当前转速(0.125rpm/bit)
                    case 0x0CF00400:
                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 2)))) {
                            //Byte4-5
                            double rotation = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (rotation * 100 * 0.125));
                            statusData.setTypes(LCStatusType.StatusType.rotation);
                            vsaddBuilder.addStatus(statusData.build());

                            print(uniqueMark, "0x0CF00400", "rotation Byte4-5	车辆当前转速(0.125rpm/bit): ", (long) (rotation * 0.125), statusAddition, statusAdditionIndex);

                        }

                        statusAdditionIndex += 12;
                        break;
                    //Byte 1	燃油压力（4Kpa/bit）  ;Byte 3	机油液位（0.4%/bit） ;Byte 4	机油压力（4Kpa/bit）;Byte 8	冷却液液位（0.4%/bit）.
                    case 0x18FEEF00:
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))) {
                            //Byte1
                            double fuelPressure = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (fuelPressure * 100 * 4));
                            statusData.setTypes(LCStatusType.StatusType.fuelPressure);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEF00", "fuelPressure Byte 1	燃油压力（4Kpa/bit） : ", (long) (fuelPressure * 4), statusAddition, statusAdditionIndex);
                        }


                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 2, 1)))) {
                            //Byte 3
                            double oilLevel = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 2, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (oilLevel * 100 * 0.4));
                            statusData.setTypes(LCStatusType.StatusType.oilLevel);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEF00", "oilLevel Byte 3	机油液位（0.4%/bit）: ", (long) (oilLevel * 0.4), statusAddition, statusAdditionIndex);
                        }


                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1)))) {
                            //Byte 4
                            double oilPressure = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 3, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (oilPressure * 100 * 4));
                            statusData.setTypes(LCStatusType.StatusType.oilPressure);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEF00", "oilPressure Byte 4	机油压力（4Kpa/bit）:", (long) (oilPressure * 4), statusAddition, statusAdditionIndex);
                        }


                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 7, 1)))) {
                            //Byte 8
                            double coolantLevel = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 7, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (coolantLevel * 100 * 0.4));
                            statusData.setTypes(LCStatusType.StatusType.coolantLevel);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEF00", "coolantLevel Byte 8	冷却液液位（0.4%/bit）:", (long) (coolantLevel * 0.4), statusAddition, statusAdditionIndex);
                        }

                        statusAdditionIndex += 12;
                        break;
                    //Byte 1	发动机冷却水温度（1℃/bit，Offset:-40℃）;Byte 2	燃油温度（1℃/bit，Offset:-40℃）;Byte3-4	机油温度（0.03125℃/bit，Offset:-273℃）
                    case 0x18FEEE00:

                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))) {
                            //Byte 1
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            double coolingWaterTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1), 1);
                            statusData.setStatusValue((long) (coolingWaterTem * 1 - 40) * 100);
                            statusData.setTypes(LCStatusType.StatusType.coolingWaterTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEE00", "coolingWaterTem Byte 1	发动机冷却水温度（1℃/bit，Offset:-40℃）;: ", (long) (coolingWaterTem * 1 - 40), statusAddition, statusAdditionIndex);

                        }


                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))) {
                            //Byte 2
                            double fuelTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (fuelTem * 1 - 40) * 100);
                            statusData.setTypes(LCStatusType.StatusType.fuelTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEE00", "fuelTem Byte 2	燃油温度（1℃/bit，Offset:-40℃）;:", (long) (fuelTem * 1 - 40), statusAddition, statusAdditionIndex);

                        }


                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 2, 2)))) {
                            //Byte 3~4
                            double oilTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 2, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (oilTem * 0.03125 - 273) * 100);
                            statusData.setTypes(LCStatusType.StatusType.oilTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEEE00", "oilTem Byte3-4	机油温度（0.03125℃/bit，Offset:-273℃））:", (long) (oilTem * 0.03125 - 273), statusAddition, statusAdditionIndex);

                        }

                        statusAdditionIndex += 12;
                        break;
                    case 0x18FE5600:
                        //Byte 1	尿素箱液位（0.4%/bit，Offset:0）;Byte 2	尿素箱温度（1℃/bit，Offset:-40℃）
                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1)))) {
                            //Byte1
                            double ureaTankLiquidLevel = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (ureaTankLiquidLevel * 100 * 0.4));
                            statusData.setTypes(LCStatusType.StatusType.ureaTankLiquidLevel);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FE5600", "ureaTankLiquidLevel Byte 1	尿素箱液位（0.4%/bit，Offset:0）;:", (long) (ureaTankLiquidLevel * 0.4), statusAddition, statusAdditionIndex);

                        }


                        if (!"FF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))) {
                            //Byte 2
                            double ureaTankTem = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (ureaTankTem * 1 - 40) * 100);
                            statusData.setTypes(LCStatusType.StatusType.ureaTankTem);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FE5600", "ureaTankTem Byte 8	Byte 2	尿素箱温度（1℃/bit，Offset:-40℃）:", (long) (ureaTankTem * 1 - 40), statusAddition, statusAdditionIndex);

                        }

                        statusAdditionIndex += 12;
                        break;
                    case 0x18FEE500:
                        //Byte1-4	发动机累计运行时间（0.05h/bit） ;Byte5-8	发动机累计转数（1000rpm/bit）
                        if (!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 4)))) {
                            //Byte1
                            double cumulativeRunningTime = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 4), 4);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            //转化为秒
                            statusData.setStatusValue((long) (cumulativeRunningTime * 100 * 0.05 * 3600));
                            statusData.setTypes(LCStatusType.StatusType.cumulativeRunningTime);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEE500", "cumulativeRunningTime Byte1-4	发动机累计运行时间（0.05h/bit） ;:", (long) (cumulativeRunningTime * 0.05), statusAddition, statusAdditionIndex);

                        }


                        if (!"FFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4)))) {
                            //Byte 2
                            double cumulativeTurningNumber = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 4), 4);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (cumulativeTurningNumber * 1000) * 100);
                            statusData.setTypes(LCStatusType.StatusType.cumulativeTurningNumber);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEE500", "cumulativeTurningNumber Byte5-8	发动机累计转数（1000rpm/bit）:", (long) (cumulativeTurningNumber * 1000), statusAddition, statusAdditionIndex);

                        }

                        statusAdditionIndex += 12;
                        break;
                    //Byte1-2	发动机燃油消耗率（0.05L/h/bit）;Byte5-6	平均燃油消耗率（0.001953125Km/L/bit）
                    case 0x18FEF200:
                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 2)))) {
                            //Byte1
                            double fuelConsumptionRate = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (fuelConsumptionRate * 1000 * 100 * 0.05));
                            statusData.setTypes(LCStatusType.StatusType.fuelConsumptionRate);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEF200", "fuelConsumptionRate Byte1-2	发动机燃油消耗率（0.05L/h/bit）;:", (long) (fuelConsumptionRate * 0.05), statusAddition, statusAdditionIndex);

                        }


                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 2)))) {
                            //Byte 2
                            double averageFuelConsumption = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 4, 2), 2);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((long) (averageFuelConsumption * 0.001953125) * 100);
                            statusData.setTypes(LCStatusType.StatusType.averageFuelConsumption);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEF200", "averageFuelConsumption Byte5-6	平均燃油消耗率（0.001953125Km/L/bit）:", (long) (averageFuelConsumption * 0.001953125), statusAddition, statusAdditionIndex);

                        }
                        statusAdditionIndex += 12;
                        break;

//				case 0X00FEF616:
//					//Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃)
//					if(!Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+5, 2)).equals("FFFF")){
//						double exhaustTem = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4+5, 2)), 2);//Byte6-7
//						statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
//						statusData.setStatusValue((long) ((exhaustTem*0.03125-273)*100));
//						statusData.setTypes(LCStatusType.StatusType.exhaustTem);
//						vsaddBuilder.addStatus(statusData.build());
//						print(uniqueMark,"0X00FEF616","exhaustTem  Byte6-7	排气温度(0.03125℃/bit,Offset:-273℃):" ,(long) ((exhaustTem*0.03125-273)*100),statusAddition, statusAdditionIndex );
//
//					}
//					statusAdditionIndex +=12;
//					break;

                    case 0xFFFFFF01:
                        //每天燃油消耗 Byte1-8	Byte1-4	（ml/bit）
                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 4)))) {
                            //Byte1-4
                            long dayFuelConsumption = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 0, 4), 4);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((dayFuelConsumption * 100 / 1000));
                            statusData.setTypes(LCStatusType.StatusType.dayFuelConsumption);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0xFFFFFF01", "dayFuelConsumption  Byte1-8	每天燃油消耗（ml/bit）:", (long) (dayFuelConsumption), statusAddition, statusAdditionIndex);

                        }
                        statusAdditionIndex += 12;
                        break;

                    case 0x18FEFC17:
                        //Byte1 	Byte2	剩余油量（ml/bit）
                        if (!"FFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1)))) {
                            //Byte1-4
                            long oilValue = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, statusAdditionIndex + 4 + 1, 1), 1);
                            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            statusData.setStatusValue((oilValue * 40));
                            statusData.setTypes(LCStatusType.StatusType.oilValue);
                            vsaddBuilder.addStatus(statusData.build());
                            print(uniqueMark, "0x18FEFC17", "oilValue  Byte2	剩余油量:", (double) (oilValue), statusAddition, statusAdditionIndex);

                        }
                        statusAdditionIndex += 12;
                        break;

                    default:
                        statusAdditionIndex += 12;
                        break;
                }
            }
        }
        return vsaddBuilder;
    }

    private static boolean inRegion(long minValue, long maxValue, long currentValue) {
        if (currentValue >= minValue && currentValue <= maxValue) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 红岩附加信息 AA：普通 CAN 总线数据
     * 小端模式
     * */
    public static LCLocationData.VehicleStatusAddition.Builder getStatusAdditionByAA(byte[] statusAddition, String uniqueMark, LCLocationData.VehicleStatusAddition.Builder vehicleStatusAddition) {
        if (statusAddition.length == 13) {
            // byte 1   转矩控制模式
            LCVehicleStatusData.VehicleStatusData.Builder builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            builderData.setStatusValue(
                    (Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 1, 1))) * 100);
            builderData.setTypes(LCStatusType.StatusType.engineTorMode);
            addStatus(builderData, vehicleStatusAddition);

            //byte 2 驾驶员需求发动机转矩百分比
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long driverEnginePercentTor = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 2, 1));
            if (driverEnginePercentTor != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((driverEnginePercentTor - 125) * 100);
                builderData.setTypes(LCStatusType.StatusType.driverEnginePercentTor);
                if (inRegion(-125, 125, builderData.getStatusValue() / 100)) {
                    addStatus(builderData, vehicleStatusAddition);
                }
            }

            //byte 3 实际发动机转矩百分比
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long actualEnginePercentTor = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 3, 1));
            if (actualEnginePercentTor != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((actualEnginePercentTor - 125) * 100);
                builderData.setTypes(LCStatusType.StatusType.actualEnginePercentTor);
                if (inRegion(-125, 125, builderData.getStatusValue() / 100)) {
                    addStatus(builderData, vehicleStatusAddition);
                }
            }

            // byte 4-5 发动机转速
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long rotation = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 4, 2));
            if (rotation != CANDataEntity.BytesOf2) {
                builderData.setStatusValue((long) (rotation * 100 * 0.125));
                builderData.setTypes(LCStatusType.StatusType.rotation);
                if (inRegion(0, 8032, builderData.getStatusValue() / 100)) {
                    vehicleStatusAddition.addStatus(builderData);
                }
            }
            // byte 6 发动机冷却水温度
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long coolingWaterTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 6, 1));
            if (coolingWaterTem != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((coolingWaterTem - 40) * 100);
                builderData.setTypes(LCStatusType.StatusType.coolingWaterTem);
                if(inRegion(-40,210,builderData.getStatusValue() / 100)){
                    addStatus(builderData, vehicleStatusAddition);
                }
            }

            // byte 7 机油压力（4Kpa/bit）
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long oilPressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 7, 1));
            if (oilPressure != CANDataEntity.BytesOf1) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue((oilPressure * 100 * 4));
                builderData.setTypes(LCStatusType.StatusType.oilPressure);
                if(inRegion(0,1000,builderData.getStatusValue() / 100)){
                    addStatus(builderData, vehicleStatusAddition);
                }
            }

            // byte 8-12 总的消耗燃料
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long totalFuelConsumption = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 8, 4));
            if (totalFuelConsumption != CANDataEntity.BytesOf4) {
                builderData.setStatusValue((long)(totalFuelConsumption * 100 * 0.5));
                builderData.setTypes(LCStatusType.StatusType.totalFuelConsumption);
                if(inRegion(0,2105540608L,builderData.getStatusValue() / 100)){
                    addStatus(builderData, vehicleStatusAddition);
                }
            }

            //// 车辆当前油量（百分比） byte 0
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int oilValue = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(statusAddition, 12, 1));
            if (oilValue != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((long) (oilValue * 0.4 * 100));
                builderData.setTypes(LCStatusType.StatusType.oilValue);
                if (inRegion(0, 100, builderData.getStatusValue() / 100)) {
                    vehicleStatusAddition.addStatus(builderData);
                }
            }

        } else {
            logger.error("0xAA报文长度有误:" + Convert.bytesToHexString(statusAddition));
        }

        return vehicleStatusAddition;
    }


    /**
     * CAN总线数据，见附录一
     * 注：在红岩项目中，如果在附加信息0xAA，0xEC有的CAN总线数据，在这个附加信息中不能重复上传，其他项目中，不上传0xAA和0xEC对应的CAN总线数据，只传该附加信息对应的附录一中的CAN总线数据。
     *
     * @param statusAddition
     * @param uniqueMark
     * @param vehicleStatusAddition
     * @return
     */
    public static LCLocationData.VehicleStatusAddition.Builder getStatusAdditionByE6(byte[] statusAddition, String uniqueMark, LCLocationData.VehicleStatusAddition.Builder vehicleStatusAddition,TerminalSatusSyncCache syncCache) {
        //默认数据项ID 2个字节，长度1个字节，+数据内容
        if (statusAddition.length >= 3) {
            int additionIndex = 0;
            while (statusAddition.length > additionIndex) {
                int additionId = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, additionIndex, 2), 2);
                int additionLength = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, additionIndex + 2, 1), 1);
                long additionContent = Convert.byte2Long(ArraysUtils.subarrays(statusAddition, additionIndex + 3, additionLength), additionLength);
                VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                switch (additionId) {
                    case 1:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue(additionContent * 100 / 256);
                        statusData.setTypes(LCStatusType.StatusType.tachographVehicleSpeed);
                        if (inRegion(0, 251, statusData.getStatusValue() / 100)) {
                            vehicleStatusAddition.addStatus(statusData);
                        } else {
                            printExceptionData("基于转速器的车速：[", 0, 251, statusData.getStatusValue() / 100);
                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 2:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((additionContent & 0x0F) * 100);
                        statusData.setTypes(LCStatusType.StatusType.engineTorMode);
                        if (inRegion(0, 11, statusData.getStatusValue() / 100)) {
                            vehicleStatusAddition.addStatus(statusData);
                        } else {
                            printExceptionData("转矩控制模式：", 0, 11, statusData.getStatusValue());
                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 3:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((additionContent - 125) * 100);
                        statusData.setTypes(LCStatusType.StatusType.driverEnginePercentTor);
                        if (inRegion(-125, 125, statusData.getStatusValue() / 100)) {
                            vehicleStatusAddition.addStatus(statusData);
                        } else {
                            printExceptionData("驾驶员需求发动机转矩百分比：[", -125, 125, statusData.getStatusValue());
                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 4:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((additionContent - 125) * 100);
                        statusData.setTypes(LCStatusType.StatusType.actualEnginePercentTor);
                        if (inRegion(-125, 125, statusData.getStatusValue() / 100)) {
                            vehicleStatusAddition.addStatus(statusData);
                        } else {
                            printExceptionData("实际发动机转矩百分比：：[", -125, 125, statusData.getStatusValue());
                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 5:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100 * 0.125));
                        statusData.setTypes(LCStatusType.StatusType.rotation);
                        if (inRegion(-0, 8032, statusData.getStatusValue() / 100)) {
                            vehicleStatusAddition.addStatus(statusData);
                        } else {
                            printExceptionData("发动机转速：：：[", 0, 8032, statusData.getStatusValue());
                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 6:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 1 - 40) * 100);
                        statusData.setTypes(LCStatusType.StatusType.coolingWaterTem);
                        if (inRegion(-40, 210, statusData.getStatusValue() / 100)) {
                            vehicleStatusAddition.addStatus(statusData);
                        } else {
                            printExceptionData("冷却液温度：：：[", -40, 210, statusData.getStatusValue());
                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 7:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) additionContent * 4 * 100);
                        statusData.setTypes(LCStatusType.StatusType.oilPressure);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 8:
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.5 * 100));
                        statusData.setTypes(LCStatusType.StatusType.totalFuelConsumption);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 9:
                        //加速踏板开度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.4 * 100));
                        statusData.setTypes(LCStatusType.StatusType.accPedalPos);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 10:
                        //驻车制动器开关：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (((additionContent & 0x0C) >> 2) * 100));
                        statusData.setTypes(LCStatusType.StatusType.parkingBrakeSwitch);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 11:
                        //基于车轮的车速：：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100 / 256));
                        statusData.setTypes(LCStatusType.StatusType.wheelBasedVehicleSpd);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 12:
                        //离合器开关：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (((additionContent & 0xC0) >> 6) * 100));
                        statusData.setTypes(LCStatusType.StatusType.clutchSwitch);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 13:
                        //制动开关：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (((additionContent & 0x30) >> 4) * 100));
                        statusData.setTypes(LCStatusType.StatusType.brakeSwitch);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 14:
                        //尿素液位：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100 * 0.4));
                        statusData.setTypes(LCStatusType.StatusType.ureaTankLiquidLevel);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 15:
                        //尿素箱温度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) ((additionContent - 40) * 100));
                        statusData.setTypes(LCStatusType.StatusType.ureaTankTem);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 16:
                        //发动机输入电压
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100 * 0.05));
                        statusData.setTypes(LCStatusType.StatusType.batteryPotInput1);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 17:
                        //点火开关电压
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100 * 0.05));
                        statusData.setTypes(LCStatusType.StatusType.batteryPot);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 18:
                        //发动机累计运行时间
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100 * 0.05 * 3600));
                        statusData.setTypes(LCStatusType.StatusType.cumulativeRunningTime);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 19:
                        //发动机累计转数：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 1000 * 100));
                        statusData.setTypes(LCStatusType.StatusType.cumulativeTurningNumber);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 20:
                        //发动机燃油消耗率：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 1000 * 0.05 * 100));
                        statusData.setTypes(LCStatusType.StatusType.fuelConsumptionRate);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 21:
                        //发动机燃油消耗率：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent / 512 * 100));
                        statusData.setTypes(LCStatusType.StatusType.realTimeOilConsumption);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 22:
                        //平均燃油消耗率
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent / 512 * 100));
                        statusData.setTypes(LCStatusType.StatusType.averageFuelConsumption);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 23:
                        //颗粒捕集器进气压力
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.5 * 100));
                        statusData.setTypes(LCStatusType.StatusType.dpfPressure);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 24:
                        //相对增压压力
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 2 * 100));
                        statusData.setTypes(LCStatusType.StatusType.relativePressure);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 25:
                        //进气温度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) ((additionContent - 40) * 100));
                        statusData.setTypes(LCStatusType.StatusType.intakeAirTem);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 26:
                        //绝对增压压力
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 2 * 100));
                        statusData.setTypes(LCStatusType.StatusType.absolutePressure);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 27:
                        //排气温度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) ((additionContent * 0.03125 - 273) * 100));
                        statusData.setTypes(LCStatusType.StatusType.exhaustTem);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 28:
                        //大气压力
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.5 * 100));
                        statusData.setTypes(LCStatusType.StatusType.atmosphericPressure);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 29:
                        //发动机舱内部温度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.03125 - 273) * 100);
                        statusData.setTypes(LCStatusType.StatusType.engineTem);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 30:
                        //大气温度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.03125 - 273) * 100);
                        statusData.setTypes(LCStatusType.StatusType.atmosphericTem);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 31:
                        //进气温度
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent - 40) * 100);
                        statusData.setTypes(LCStatusType.StatusType.AirInTem);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 32:
                        //冷启动灯
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) additionContent * 100);
                        statusData.setTypes(LCStatusType.StatusType.lampStatus);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 33:
                        //本次驾驶循环里程
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.125 * 100));
                        statusData.setTypes(LCStatusType.StatusType.tripDistance);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 34:
                        //总里程：
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.125 * 100));
                        statusData.setTypes(LCStatusType.StatusType.mileage);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 35:
                        //油中含水指示
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) ((additionContent & 0x03) * 100));
                        statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 36:
                        //目标档位
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100));
                        statusData.setTypes(LCStatusType.StatusType.targetGear);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 37:
                        //实际速比
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.001 * 100));
                        statusData.setTypes(LCStatusType.StatusType.realSpeedRatio);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 38:
                        //当前档位
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 100));
                        statusData.setTypes(LCStatusType.StatusType.currentGear);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;

                    case 39:
                        //仪表燃油液位
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.4 * 100));
                        statusData.setTypes(LCStatusType.StatusType.oilValue);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 40:
                        //仪表小计里程
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.125 * 100));
                        statusData.setTypes(LCStatusType.StatusType.TripDistanceDD);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 41:
                        //仪表总里程
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long) (additionContent * 0.125 * 100));
                        statusData.setTypes(LCStatusType.StatusType.mileageDD);
                        vehicleStatusAddition.addStatus(statusData);
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 42:
                        //积分里程
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue(additionContent * 100 / 1000);
                        statusData.setTypes(LCStatusType.StatusType.differentialMileage);
                        vehicleStatusAddition.addStatus(statusData);
                        //缓存积分里程,9102终端校对用
//                        if (additionContent > 0) {
//                            TerminalStateSync sync = syncCache.get(uniqueMark);
//                            if (sync != null) {
//                                //如果积分里程>0,则放到缓存里
//                                sync.setIntegralMileage(statusData.getStatusValue());
//                                //设置gps时间为当前时间，定时任务扫描近2分钟数据进redis
//                                sync.setGpsDate(System.currentTimeMillis() / 1000);
//                            } else {
//                                sync = new TerminalStateSync();
//                                //如果积分里程>0,则放到缓存里
//                                sync.setIntegralMileage(statusData.getStatusValue());
//                                sync.setGpsDate(System.currentTimeMillis() / 1000);
//                                syncCache.add(uniqueMark, sync);
//                            }
//
//                            logger.info("sync_e6_differentialMileage_data --> {}", JSON.toJSONString(sync));
//                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    case 43:
                        //积分油耗
                        statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                        statusData.setStatusValue((long)(additionContent*0.5* 100/1000));
                        statusData.setTypes(LCStatusType.StatusType.integralFuelConsumption);
                        vehicleStatusAddition.addStatus(statusData);
                        //缓存积分油耗,9102终端校对用
//                        if (additionContent > 0) {
//                            TerminalStateSync sync = syncCache.get(uniqueMark);
//                            if (sync != null) {
//                                //如果积分油耗>0,则放到缓存里
//                                sync.setIntegralConsumption((long)(additionContent*0.5* 10000/1000));
//                                //设置gps时间为当前时间，定时任务扫描近2分钟数据进redis
//                                sync.setGpsDate(System.currentTimeMillis() / 1000);
//                            } else {
//                                sync = new TerminalStateSync();
//                                //如果积分油耗>0,则放到缓存里
//                                sync.setIntegralConsumption((long)(additionContent*0.5* 10000/1000));
//                                sync.setGpsDate(System.currentTimeMillis() / 1000);
//                                syncCache.add(uniqueMark, sync);
//                            }
//
//                            logger.info("vin-->{}, sync_e6_integralFuelConsumption_data --> {}", uniqueMark, JSON.toJSONString(sync));
//                        }
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                    default:
                        additionIndex = additionIndex + 3 + additionLength;
                        break;
                }
            }

        }


        return vehicleStatusAddition;
    }

    public static LCLocationData.VehicleStatusAddition.Builder getStatusAdditionByEC(byte[] statusAddition, String uniqueMark, LCLocationData.VehicleStatusAddition.Builder vehicleStatusAddition) {

        try {
            LCVehicleStatusData.VehicleStatusData.Builder builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            builderData.setStatusValue(
                    (Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 0, 1))) * 100);
            // byte 0   转矩控制模式
            builderData.setTypes(LCStatusType.StatusType.engineTorMode);
            addStatus(builderData, vehicleStatusAddition);

            //byte 1 驾驶员需求发动机转矩百分比
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long driverEnginePercentTor = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 1, 1));
            if (driverEnginePercentTor != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((long) (driverEnginePercentTor - 125) * 100);
                builderData.setTypes(LCStatusType.StatusType.driverEnginePercentTor);
                if (inRegion(-125, 125, builderData.getStatusValue() / 100)) {
                    addStatus(builderData, vehicleStatusAddition);
                }
            }
            //byte 2 实际发动机转矩百分比
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long actualEnginePercentTor = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 2, 1));
            if (actualEnginePercentTor != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((long) (actualEnginePercentTor - 125) * 100);
                builderData.setTypes(LCStatusType.StatusType.actualEnginePercentTor);
                if (inRegion(-125, 125, builderData.getStatusValue() / 100)) {
                    addStatus(builderData, vehicleStatusAddition);
                }
            }

            // byte 3-4 发动机转速
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long rotation = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 3, 2));
            if (rotation != CANDataEntity.BytesOf2) {
                builderData.setStatusValue((long) (rotation * 100 * 0.125));
                builderData.setTypes(LCStatusType.StatusType.rotation);
                if (inRegion(0, 8032, builderData.getStatusValue() / 100)) {
                    addStatus(builderData, vehicleStatusAddition);
                }
            }
            // byte 5 发动机冷却水温度
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long coolingWaterTem = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 5, 1));
            if (coolingWaterTem != CANDataEntity.BytesOf1) {
                builderData.setStatusValue((coolingWaterTem - 40) * 100);
                builderData.setTypes(LCStatusType.StatusType.coolingWaterTem);
                addStatus(builderData, vehicleStatusAddition);
            }
            // byte 6 机油压力（4Kpa/bit）
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long oilPressure = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 6, 1));
            if (oilPressure != CANDataEntity.BytesOf1) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue( (oilPressure * 100 * 4));
                builderData.setTypes(LCStatusType.StatusType.oilPressure);
                addStatus(builderData, vehicleStatusAddition);
            }

            // byte 7 加速踏板开度/油门开度
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long accPedalPos = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 7, 1));
            if (accPedalPos != CANDataEntity.BytesOf1) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue((long) (accPedalPos * 100 * 0.4));
                builderData.setTypes(LCStatusType.StatusType.accPedalPos);
                vehicleStatusAddition.addStatus(builderData);
            }
            // byte 8-9 平均气耗
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // Byte4
            long avgGasConsumption = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 8, 2));
            if (avgGasConsumption != CANDataEntity.BytesOf2) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue((long) (avgGasConsumption * 100 * 0.1));
                builderData.setTypes(LCStatusType.StatusType.avgGasConsumption);
                vehicleStatusAddition.addStatus(builderData);
            }
            // byte 10-13 总气耗
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // Byte4
            long totalGasConsumption = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 10, 4));
            if (totalGasConsumption != CANDataEntity.BytesOf4) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue((long) (totalGasConsumption * 100 * 0.1));
                builderData.setTypes(LCStatusType.StatusType.totalGasConsumption);
                vehicleStatusAddition.addStatus(builderData);
            }

            // byte 14 瞬时气耗
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long integralGasConsumption = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 14, 2));
            if (integralGasConsumption != CANDataEntity.BytesOf2) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue((long) (integralGasConsumption * 100 * 0.1));
                builderData.setTypes(LCStatusType.StatusType.integralGasConsumption);
                vehicleStatusAddition.addStatus(builderData);
            }


            // byte 16-17 气瓶气量1
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long lng1Surplus = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 16, 2));
            if (lng1Surplus != CANDataEntity.BytesOf2) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue((long) (lng1Surplus * 100));
                builderData.setTypes(LCStatusType.StatusType.lng1Surplus);
                vehicleStatusAddition.addStatus(builderData);
            }

            // byte 18-19 气瓶气量2
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long lng2Surplus = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 18, 2));
            if (lng2Surplus != CANDataEntity.BytesOf2) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue(lng2Surplus * 100);
                builderData.setTypes(LCStatusType.StatusType.lng2Surplus);
                vehicleStatusAddition.addStatus(builderData);
            }

            // byte 20-21 气瓶气量3
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long lng3Surplus = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 20, 2));
            if (lng3Surplus != CANDataEntity.BytesOf2) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue(lng3Surplus * 100);
                builderData.setTypes(LCStatusType.StatusType.lng3Surplus);
                vehicleStatusAddition.addStatus(builderData);
            }

            // byte 22-23 气瓶气量4
            builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long lng4Surplus = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 22, 2));
            if (lng4Surplus != CANDataEntity.BytesOf2) {
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                builderData.setStatusValue(lng4Surplus * 100);
                builderData.setTypes(LCStatusType.StatusType.lng4Surplus);
                vehicleStatusAddition.addStatus(builderData);
            }
            //Todo  气量还没有做
            //适配红岩老设备，只传24个字节
            if (statusAddition.length == 25) {
                // byte 24 Actual Pump State
                builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                long actualPumpState = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(statusAddition, 24, 1));
                if (actualPumpState != CANDataEntity.BytesOf1) {
                    builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                    builderData.setStatusValue((actualPumpState * 100));
                    builderData.setTypes(LCStatusType.StatusType.actualPumpState);
                    vehicleStatusAddition.addStatus(builderData);
                }

            }
        }
        catch(Exception e){
            logger.error("0xEC parse error:",e);
        }
        return vehicleStatusAddition;
    }

    /**
     * 解析胎压、胎温数据
     *
     * @param statusAddition
     * @param uniqueMark
     * @return
     */
    public static LCTireTemperatureAddition.TireTemperatureAddition.Builder getStatusAdditionByE5(byte[] statusAddition, String uniqueMark) {
        LCTireTemperatureAddition.TireTemperatureAddition.Builder trieAddition = null;
        try {
            trieAddition = LCTireTemperatureAddition.TireTemperatureAddition.newBuilder();
            //byte 0  车型定义
            int vehType = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 0, 1), 1);

            //byte 1  轮胎项个数  包含的轮胎项个数，>=0，默认0
            int tyreCount = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 1, 1), 1);

//            if (statusAddition.length == (2 + tyreCount * 11)) {
//                trieAddition.setVehType(vehType);
//                trieAddition.setTyreCount(tyreCount);
//
//                LCTireTemperatureAddition.TireConditionItem.Builder builder = null;
//                // 2	轮胎项	BYTE[11]	每个轮胎的状态占用 11 个字节。轮胎状态项目字节长度等于轮胎数量 x 11。数组定义参考 27-4-1 轮胎状态定义
//                for (int i = 0; i < tyreCount; i++) {
//                    builder = LCTireTemperatureAddition.TireConditionItem.newBuilder();
//
//                    // 0	轮胎位置	BYTE
//                    int tyrePosition = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11, 1), 1);
//                    // 低4位
//                    int tyreHorizontalPosition = tyrePosition & 0xf;
//                    builder.setTyreHorizontalPosition(tyreHorizontalPosition);
//                    // 高4位
//                    int tyreVerticalPosition = (tyrePosition >> 4) & 0xf;
//                    builder.setTyreVerticalPosition(tyreVerticalPosition);
//
//
//                    // 1	轮胎状态	WORD
//                    int tyreStatus = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 1, 2), 2);
//                    int batteryStatus = tyreStatus & 1;
//                    builder.setBatteryStatus(batteryStatus);
//
//                    int unBalance = (tyreStatus >> 1) & 1;
//                    builder.setUnBalance(unBalance);
//
//                    int airLeakage = (tyreStatus >> 2) & 1;
//                    builder.setAirLeakage(airLeakage);
//
//                    int highTemperature = (tyreStatus >> 3) & 1;
//                    builder.setHighTemperature(highTemperature);
//
//                    int noRFSignal = (tyreStatus >> 4) & 1;
//                    builder.setNoRFSignal(noRFSignal);
//
//                    int noMatch = (tyreStatus >> 6) & 1;
//                    builder.setNoMatch(noMatch);
//
//                    int tirePressureAlarm = (tyreStatus >> 13) & 7;
//                    builder.setTirePressureAlarm(tirePressureAlarm);
//
//
//                    // 3	轮胎压力	BYTE	单位 4kPa，范围 0~1000kPa
//                    int tyrePressure = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 3, 1), 1);
//                    if (InRegion(0, 250, tyrePressure)) {
//                        builder.setTyrePressure(tyrePressure * 4);
//                    }
//
//
//                    // 5	轮胎温度	WORD	单位 0.03125 deg C， 偏移 -273 deg C，范围 -273 ~ 1734.96875 deg C
//                    int tyreTemperature = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 5, 2), 2);
//                    builder.setTyreTemperature((int) ((tyreTemperature * 0.03125 - 273) * 100));
//
//
//                    // 7	温度报警阈值	WORD	单位 0.03125 deg C， 偏移 -273 deg C，范围 -273 ~ 1734.96875 deg C
//                    int temAlarmThreshold = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 7, 2), 2);
//                    builder.setTemAlarmThreshold((int) ((temAlarmThreshold * 0.03125 - 273) * 100));
//
//
//                    // 9	胎压高报警阈值	BYTE	预留，填充0xFF
//                    int tyreHalarmThreshold = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 9, 1), 1);
//                    builder.setTyreHalarmThreshold(tyreHalarmThreshold);
//
//
//                    // 10	胎压低报警阈值	BYTE	预留，填充0xFF
//                    int tyreLalarmThreshold = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 10, 1), 1);
//                    builder.setTyreLalarmThreshold(tyreLalarmThreshold);
//
//
//                    // 11	轮胎气压标称值	BYTE	预留，填充0xFF
//                    int tyreNomimalValue = Convert.byte2Int(ArraysUtils.subarrays(statusAddition, 2 + i * 11 + 11, 1), 1);
//                    builder.setTyreNomimalValue(tyreNomimalValue);
//
//                    trieAddition.addTireConditionItem(builder);
//
//                    logger.info("vehType:{}, tyreCount:{}, tyrePosition:{}, tyreHorizontalPosition:{}, tyreVerticalPosition:{}, tyreStatus:{}, batteryStatus:{}, unBalance:{}, airLeakage:{}, " +
//                                    "highTemperature:{}, noRFSignal:{}, noMatch:{}, tirePressureAlarm:{}, tyrePressure:{}, tyreTemperature:{}, temAlarmThreshold:{}, tyreLalarmThreshold:{}, tyreNomimalValue:{}.",
//                            vehType, tyreCount, tyrePosition, tyreHorizontalPosition, tyreVerticalPosition, tyreStatus, batteryStatus, unBalance, airLeakage, highTemperature,
//                            noRFSignal, noMatch, tirePressureAlarm, tyrePressure, tyreTemperature, temAlarmThreshold, tyreHalarmThreshold, tyreLalarmThreshold, tyreNomimalValue);
//                }
//            } else {
//                logger.error("0xE5 content noncompliance.");
//            }
        } catch (Exception e) {
            logger.error("0xE5 parse error.", e);
        }

        return trieAddition;
    }


    private static void addStatus(VehicleStatusData.Builder statusData, LCLocationData.VehicleStatusAddition.Builder statusAddition) {
        if (statusAddition != null) {
            int index = -1;
            for (int i = 0; i < statusAddition.getStatusList().size(); i++) {
                LCVehicleStatusData.VehicleStatusData statusDataTemp = statusAddition.getStatusList().get(i);
                if (statusData.getTypes().getNumber() == statusDataTemp.getTypes().getNumber()) {
                    index = i;
                    break;
                }
            }
            if (index == -1) {
                statusAddition.addStatus(statusData);
            }
        }
    }

    /**
     * 红岩协议故障码信息
     * @param breakdownAddition
     * @param uniqueMark
     * @return
     */

    public static LCLocationData.VehicleBreakdownAddition.Builder getBreakdownAdditionHY(byte[] breakdownAddition, String uniqueMark) {
        LCLocationData.VehicleBreakdownAddition.Builder breakdownBuilder = LCLocationData.VehicleBreakdownAddition.newBuilder();
        int breakdownAdditionIndex = 1;
        if (breakdownAddition.length >= 5) {
            LCVehicleBreakdown.VehicleBreakdown.Builder vehicleBreakdown = null;
            while (breakdownAddition.length > breakdownAdditionIndex) {
                vehicleBreakdown = LCVehicleBreakdown.VehicleBreakdown.newBuilder();
                int spn = Convert.byte2Int(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 3), 3);
                int fmi = Convert.byte2Int(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex+3, 1), 1);
                vehicleBreakdown.setBreakdownSPNValue(spn);
                vehicleBreakdown.setBreakdownFMIValue(fmi);
                breakdownBuilder.addBreakdown(vehicleBreakdown);
                logger.info("终端故障码【" + uniqueMark + "】故障码:   原始包: " + Convert.bytesToHexString(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 3)) + " ,解析值: SPN=" + spn + ",FMI=" + fmi);
                breakdownAdditionIndex += 4;
            }
        }
        return  breakdownBuilder;
    }


    /**
     * 故障数据
     *
     * @param
     * @return
     */

    public static LCLocationData.VehicleBreakdownAddition.Builder getBreakdownAddition(byte[] breakdownAddition, String uniqueMark) {
        /**
         * 故障信息的格式
         *
         * 故障总数              SPN 第一字节,  SPN 第二字节  ,  SPN MSB ,FMI 码
         *             Byte1       Byte2                 Byte3
         *                                            Bit8-6,Bit 5-1
         */
        LCLocationData.VehicleBreakdownAddition.Builder breakdownBuilder = LCLocationData.VehicleBreakdownAddition.newBuilder();
        //开始下标
        int breakdownAdditionIndex = 0;
        if (breakdownAddition.length >= 1) {
            int breakdownTotal = Convert.byte2Int(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 1), 1);
            //下标从故障项列表ID开始
            breakdownAdditionIndex += 1;
            LCVehicleBreakdown.VehicleBreakdown.Builder vehicleBreakdown = null;
            for (int i = 0; i < breakdownTotal; i++) {
                vehicleBreakdown = LCVehicleBreakdown.VehicleBreakdown.newBuilder();
                int spn1 = Convert.byte2Int(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 1), 1);
                int spn2 = Convert.byte2Int(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex + 1, 1), 1);
                int byte3 = Convert.byte2Int(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex + 2, 1), 1);
                int spnMsb = byte3 >> 5;
                int spn = spn1 + (spn2 << 8) + (spnMsb << 16);
                int fmi = byte3 & 31;
                vehicleBreakdown.setBreakdownSPNValue(spn);
                vehicleBreakdown.setBreakdownFMIValue(fmi);
                breakdownBuilder.addBreakdown(vehicleBreakdown);
                logger.info("终端故障码【" + uniqueMark + "】  第【" + (i + 1) + "】个故障码:   原始包: " + Convert.bytesToHexString(ArraysUtils.subarrays(breakdownAddition, breakdownAdditionIndex, 3)) + " ,解析值: SPN=" + spn + ",FMI=" + fmi);
                breakdownAdditionIndex += 3;
            }
        }
        return breakdownBuilder;
    }


    public static void print(String uniqueMark, String id, String name, double value, byte[] statusAddition, int statusAdditionIndex) {
//		logger.info(uniqueMark+" :  "+id+": "+Convert.bytesToHexString(ArraysUtils.subarrays(statusAddition, statusAdditionIndex+4, 8))+" :  " +value+ ":          "+name);
    }

    public static void printExceptionData(String message, long minValue, long maxValue, long currentValue) {
        logger.info("数据异常  " + message + " 数据区间：[" + minValue + "," + maxValue + "]" + ",当前数值为:" + currentValue);
    }

    public static void main(String[] args) {

        System.out.println(Convert.byte2Long(Convert.hexStringToBytes("2C80"), 2));
        System.out.println(Convert.byte2Long(Convert.hexStringToBytes("2C80"), 2) * 0.03125 - 273);

    }

}
