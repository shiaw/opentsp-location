package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver.jt2013;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.LastMileageOilTypeCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSatusSyncCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.StringUtils;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationAdditionProcess_BigEndian;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationAdditionProcess_LittleEndian;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationTools;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDrivingBehaviorAnalysis.DrivingBehaviorAnalysis;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@LocationCommand(id = "0200", version = "200110")
@Component("JT_0200_LocationReport_200110")
public class JT_0200_LocationReport extends TerminalCommand {
    protected static int GPS_TIME_OUT = 600;//Configuration.getInt("ta_patchDataThreshold");
    private int endianType = 1; //Configuration.getInt("PLATFORM");

    @Autowired
    private LocationTools lcTools;
    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;
    @Value("${effective.time.range:24}")
    private int effectiveTime;

    @Value("${opentsp.platform.name:qingqi}")
    private String platform;
    @Autowired
    TerminalSatusSyncCache syncCache;
    /**
     * CAN时间与GPS时间在3分钟以内，认为有效,则进行合并。单位：分钟
     */
    @Value("${CanEffective.time.range:3}")
    private int canEffectiveTime;
    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        //return 	this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS);
        PacketResult result = new PacketResult();
        try {
            byte[] content = packet.getContent();
            long alarm = Convert.byte2Long(ArraysUtils.subarrays(content, 0, 4), 4);
            long status = Convert.byte2Long(ArraysUtils.subarrays(content, 4, 4), 4);
            int latitude = Convert.byte2Int(ArraysUtils.subarrays(content, 8, 4), 4);
            int longitude = Convert.byte2Int(ArraysUtils.subarrays(content, 12, 4), 4);
//            int height = Convert.byte2Int(ArraysUtils.subarrays(content, 16, 2), 2);
            int height = Convert.byte2SignedInt(ArraysUtils.subarrays(content, 16, 2), 2);
            int speed = Convert.byte2Int(ArraysUtils.subarrays(content, 18, 2), 2) / 10;
            int direction = Convert.byte2Int(ArraysUtils.subarrays(content, 20, 2), 2);
            long currentTime = System.currentTimeMillis() / 1000;
            long gpsTime = currentTime;
            String timestr = "20" + Convert.bytesToHexString(ArraysUtils.subarrays(content, 22, 6));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

            try {
                gpsTime = sdf.parse(timestr).getTime() / 1000;
            } catch (ParseException e) {
                gpsTime = System.currentTimeMillis() / 1000;
                e.printStackTrace();
            }
            //2017-04-05 如果当前GPS时间超出合理时间范围，则直接丢弃
            if (Math.abs(gpsTime - System.currentTimeMillis() / 1000) > effectiveTime * 3600) {
                //过滤点也需要应答
                PacketResult packetResult = new PacketResult();
                packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                        LCResultCode.JTTerminal.SUCCESS));
                return packetResult;
            }
            Long lastTime = lastMileageOilTypeCache.getLastLocationTime(packet.getUniqueMark());
            if (lastTime == null || gpsTime >= lastTime) {
                lastMileageOilTypeCache.addLastLocationTimeCache(packet.getUniqueMark(), gpsTime);
            } else {
                gpsTime = System.currentTimeMillis() / 1000;
            }
            LCLocationData.LocationData.Builder builder = LCLocationData.LocationData.newBuilder();
            builder.setAlarm(alarm);
            builder.setStatus(status);
            builder.setOriginalLat(latitude);
            builder.setOriginalLng(longitude);
            builder.setGpsDate(gpsTime);
            builder.setHeight(height);
            builder.setSpeed(speed);
            builder.setDirection(direction);
            builder.setReceiveDate(currentTime);
            builder.setIsPatch(false);
            //默认为没有合并
            builder.setBatteryPower(1);


            builder.setStarStatus((int) (status & 0x000002));//isLocation	0x000002	1	0：未定位；1：定位;starStatus	星况（1正常，0不正常，默认为1）
            DrivingBehaviorAnalysis.Builder lCDrivingBehaviorAnalysis = DrivingBehaviorAnalysis.newBuilder();

            byte[] addition = ArraysUtils.subarrays(content, 28, content.length - 28);

            //测试用
//			if(packet.getUniqueMark().equals("10000000001")){
//				System.out.println();
//				builder.setStatusAddition(getStatusAddition_bak(content,packet.getUniqueMark()));
//			}
//			builder.setStatusAddition(getStatusAddition_bak(content,packet.getUniqueMark()));
//			LCLocationData.VehicleBreakdownAddition.Builder breakdownBuilder = LCLocationData.VehicleBreakdownAddition.newBuilder();
//			LCVehicleBreakdown.VehicleBreakdown.Builder vehicleBreakdown = LCVehicleBreakdown.VehicleBreakdown.newBuilder();
//			int SPN=520204;
//			int FMI = 11;	
//			vehicleBreakdown.setBreakdownSPNValue(SPN);
//			vehicleBreakdown.setBreakdownFMIValue(FMI);
//			breakdownBuilder.addBreakdown(vehicleBreakdown);
//			builder.setBreakdownAddition(breakdownBuilder);
//			System.err.println("故障码 SPN： "+SPN+"      FMI："+FMI);


            int additionIndex = 0;
            if (addition.length >= 2) {
                StringBuffer buffer = new StringBuffer();
                buffer.append("终端[ " + packet.getUniqueMark() + " ]位置附加信息扩展:");
                LCLocationData.VehicleStatusAddition.Builder vsaddBuilder = LCLocationData.VehicleStatusAddition.newBuilder();

                while (addition.length > additionIndex) {
                    int additionId = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex, 1), 1);
                    int additionLength = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex + 1, 1), 1);
                    switch (additionId) {
                        case 1:
                            long mileage = Convert.byte2Long(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength) * 100;
                            additionIndex = additionIndex + 2 + additionLength;

                            //青汽协议0x01表示gps里程，红岩协议0x01表示ecu里程
                            if("hongyan".equals(platform)){
                                LCVehicleStatusData.VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                                //ecu里程是附加信息，单位是km并且放大100倍
                                statusData.setStatusValue(mileage/10);
                                statusData.setTypes(LCStatusType.StatusType.mileage);
                                vsaddBuilder.addStatus(statusData);
                                builder.setStatusAddition(vsaddBuilder);
                            }
                            else{
                                if (mileage >= 500 * 10000 * 1000) { // 500万公里*1000米，进行单位转换
                                    builder.setMileage(0);
                                } else {
                                    builder.setMileage(mileage);
                                }

                                //缓存gps里程、gps日期
                                if (mileage>0) {
                                    TerminalStateSync sync = syncCache.get(packet.getUniqueMark());
                                    if (sync != null) {
                                        sync.setGpsMileage(mileage);
                                        sync.setGpsDate(gpsTime);
                                    }else{
                                        sync = new TerminalStateSync();
                                        sync.setGpsMileage(mileage);
                                        sync.setGpsDate(gpsTime);
                                        syncCache.add(packet.getUniqueMark(),sync);
                                    }
                                }
                            }

                            break;
                        case 2:
                            int oil = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setOil(oil);
                            break;
                        case 3:
                            int recorderSpeed = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setRecorderSpeed(recorderSpeed);
                            break;
                        case 4: //需要人工确认报警事件的ID,WORD,从1 开始计数
                            int alarmIdentify = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.addAlarmIdentify(alarmIdentify);
                            break;
                        case 0x25://扩展车辆信号状态
                            int signalStatus = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setSignalStatus(signalStatus);
                            break;
                        case 0x31://星数（默认为1）
                            int starNumber = Convert.byte2Int(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setStarNumber(starNumber);
                            break;
                        case 0xEF://EF  附加信息ID = 0x EF， 附加信息长度=8   扩展状态第二个字节   16进制：EF 08 00 40 00 00 00 00 00 00
                            //取出附加信息
                            byte[] alermstatusAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);//00 40 00 00 00 00 00 00
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setAdditionAlarm(ByteString.copyFrom(alermstatusAddition));//设置 附加报警位 到LocationData的additonAlarm字段上
//						AdditionAlarmParser.parseAlermstatusAddition(alermstatusAddition,packet.getUniqueMark());
                            break;
                        case 0x74:// 车辆状态附加信息:
                            //取出附件信息
                            byte[] statusAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            if (endianType == 1) {
                                builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
                            } else {
                                builder.setStatusAddition(LocationAdditionProcess_LittleEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
                            }
                            break;
                        case 0x83:// 车辆故障附加信息
                            byte[] breakdownAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            if (endianType == 1) {
                                builder.setBreakdownAddition(LocationAdditionProcess_BigEndian.getBreakdownAddition(breakdownAddition, packet.getUniqueMark()));
                            } else {
                                builder.setBreakdownAddition(LocationAdditionProcess_LittleEndian.getBreakdownAddition(breakdownAddition, packet.getUniqueMark()));
                            }
                            break;
                        case 0xAA: //常用CAN 总线数据
                            //取出附件信息
                            byte[] canAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
//                            if (endianType == 1) {
//                                builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            } else {
//                                builder.setStatusAddition(LocationAdditionProcess_LittleEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            }
                            builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAdditionByAA(canAddition, packet.getUniqueMark(),vsaddBuilder));
                            //设为已合并，否则导出车况为空
                            builder.setBatteryPower(0);
							break;
                        case 0xEC: //其他CAN 数据上传
                            //取出附件信息
                            byte[] otherCanAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
//                            if (endianType == 1) {
//                                builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            } else {
//                                builder.setStatusAddition(LocationAdditionProcess_LittleEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            }
                            builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAdditionByEC(otherCanAddition, packet.getUniqueMark(),vsaddBuilder));
                            //设为已合并，否则导出车况为空
                            builder.setBatteryPower(0);
							break;
                        case 0xE1: //动态载重
                            byte[] dynamicLoadAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            long dynamicLoad =(long) (Convert.byte2Long(ArraysUtils.subarrays(dynamicLoadAddition, 0, 2), 2)*0.1*100);
                            LCVehicleStatusData.VehicleStatusData.Builder builderData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            builderData.setStatusValue(dynamicLoad);
                            builderData.setTypes(LCStatusType.StatusType.dynamicLoad);
                            vsaddBuilder.addStatus(builderData);
                            builder.setStatusAddition(vsaddBuilder);
                            break;
                        case 0xE2: //灯状态
                            byte[] lampAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            long lampStatus =(long) (Convert.byte2Long(ArraysUtils.subarrays(lampAddition, 0, 1), 1)*100);
                            LCVehicleStatusData.VehicleStatusData.Builder builderDataLamp = LCVehicleStatusData.VehicleStatusData.newBuilder();
                            builderDataLamp.setStatusValue(lampStatus);
                            builderDataLamp.setTypes(LCStatusType.StatusType.lampState);
                            vsaddBuilder.addStatus(builderDataLamp);
                            builder.setStatusAddition(vsaddBuilder);
                            break;
                        case 0xE3: //当前故障码
                            byte[] crrentDownAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setBreakdownAddition(LocationAdditionProcess_BigEndian.getBreakdownAdditionHY(crrentDownAddition, packet.getUniqueMark()));
                            break;
                        case 0xE4: //消失故障码
                            byte[] cancelDownAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setCancleBreakdownAddition(LocationAdditionProcess_BigEndian.getBreakdownAdditionHY(cancelDownAddition, packet.getUniqueMark()));
                            break;
                        case 0xE5: //胎压胎温 数据上传
                            //取出附件信息
                            byte[] tireTemperatureAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
//                            if (endianType == 1) {
//                                builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            } else {
//                                builder.setStatusAddition(LocationAdditionProcess_LittleEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            }
                            builder.setTemperatureAddition(LocationAdditionProcess_BigEndian.getStatusAdditionByE5(tireTemperatureAddition, packet.getUniqueMark()));
                            break;
                        case 0xE6: //CAN总线数据
                            //取出附件信息
                            byte[] dynamicCanAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAdditionByE6(dynamicCanAddition, packet.getUniqueMark(),vsaddBuilder,syncCache));
                            //设为已合并，否则导出车况为空
                            builder.setBatteryPower(0);
                            break;
                        case 0XF0://急转弯参数，发生急转弯时的当前角度值
                            byte[] turningAngle = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F0:" + Convert.byte2Int(turningAngle, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setTurningAngle(Convert.byte2Int(turningAngle, 2));
                            break;
                        case 0xF1://发生低油量行驶报警时的油量值
                            byte[] lowOilDrivingValue = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F1:" + Convert.byte2Int(lowOilDrivingValue, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setLowOilDrivingValue(Convert.byte2Int(lowOilDrivingValue, 2));
                            break;
                        case 0xF2://获取到ECU传的车辆速度，该值为当前车辆速度
                            byte[] vehicleSpeedFromEcu = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F2:" + Convert.byte2Int(vehicleSpeedFromEcu, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setVehicleSpeedFromEcu(Convert.byte2Int(vehicleSpeedFromEcu, 2));
                            break;
                        case 0xF3://发生报警时的发动机转速当前值
                            byte[] rpmWhenAlarming = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F3:" + Convert.byte2Int(rpmWhenAlarming, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setRpmWhenAlarming(Convert.byte2Int(rpmWhenAlarming, 2));
                            break;
                        case 0xF4://发生急加速、急减速的当前值
                            byte[] velocityChangeValue = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F4:" + Convert.byte2Int(velocityChangeValue, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setVelocityChangeValue(Convert.byte2Int(velocityChangeValue, 2));
                            break;
                        case 0xF5://车辆当前档位  1个byte
                            byte[] currentGearshift = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F5:" + Convert.byte2Int(currentGearshift, 1) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setCurrentGearshift(Convert.byte2Int(currentGearshift, 1));
                            break;
                        case 0xF6://车辆当前发送机转速
                            byte[] currentRPM = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F6:" + Convert.byte2Int(currentRPM, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setCurrentRPM(Convert.byte2Int(currentRPM, 2));
                            break;
                        case 0xF7://当车辆ACC开启时，终端查询并存储的北斗终端ID号，如果查询不到填0xFF。
                            if (!"FFFFFFFFFFFFFF".equals(Convert.bytesToHexString(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength)))) {
                                byte[] deviceIdentity = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                                additionIndex = additionIndex + 2 + additionLength;
                                String deviceIdentityStr = StringUtils.bytesToGbkString(deviceIdentity);
                                buffer.append("[F7:" + deviceIdentityStr + "]");
                                lCDrivingBehaviorAnalysis.setDeviceIdentity(deviceIdentityStr);
                            } else {
                                additionIndex = additionIndex + 2 + additionLength;
                            }
                            break;
                        case 0xF8://刹车区间次数
                            byte[] brakeTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F8:" + Convert.byte2Int(brakeTimes, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setBrakeTimes(Convert.byte2Int(brakeTimes, 2));
                            break;
                        case 0xF9://离合区间次数
                            byte[] clutchTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[F9:" + Convert.byte2Int(clutchTimes, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setClutchTimes(Convert.byte2Int(clutchTimes, 2));
                            break;
                        case 0xFA://缓速器次数
                            byte[] retarderTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[FA:" + Convert.byte2Int(retarderTimes, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setRetarderTimes(Convert.byte2Int(retarderTimes, 2));
                            break;
                        case 0xFB://ABS次数
                            byte[] absTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[FB:" + Convert.byte2Int(absTimes, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setAbsTimes(Convert.byte2Int(absTimes, 2));
                            break;
                        case 0xFC://倒档次数
                            byte[] reverseTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            buffer.append("[FC:" + Convert.byte2Int(reverseTimes, 2) + "]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setReverseTimes(Convert.byte2Int(reverseTimes, 2));
                            break;
                        default:
                            additionIndex = additionIndex + 2 + additionLength;
                    }
                }
//				log.error(buffer.toString());
            }
            builder.setAnalysisData(lCDrivingBehaviorAnalysis);
            //TODO : 此处附加最新CAN数据到位置数据中去.
            // 2016-04-12 追加CAN数据   ---------- start
            synchronized (TerminalLastestCANData.canMap) {
                CANDataEntity cANDataEntity = TerminalLastestCANData.getLastCANData(packet.getUniqueMark());
                if (cANDataEntity != null) {
                    if (Math.abs(gpsTime - cANDataEntity.getEcuTime()) >= canEffectiveTime * 60) {
                        builder.setBatteryPower(1);
                    } else {
                        builder.setEcuDate(cANDataEntity.getEcuTime());
                        // 电量
                        builder.setBatteryPower(cANDataEntity.getCombineTimes() == 0 ? 0 : 1);
                        // 里程
                        LCLocationData.VehicleStatusAddition.Builder vsaddBuilder = builder.getStatusAdditionBuilder();
                        vsaddBuilder = cANDataEntity.getStatusAddition();
                        builder.setStatusAddition(vsaddBuilder);
                        builder.setBreakdownAddition(cANDataEntity.getBreakdownAddition());
                        cANDataEntity.setCombineTimes(1);
                        // 电源箱号、电压
                        for (ModuleVoltageEntity mv : cANDataEntity.getModuleVoltageList()) {
                            LCLocationData.ModuleVoltage.Builder mvBuilder = LCLocationData.ModuleVoltage.newBuilder();
                            mvBuilder.setModuleNum(mv.getModuleNum());
                            mvBuilder.setNumber(mv.getNumber());
                            mvBuilder.setVoltage(mv.getVoltage());
                            builder.addModuleVoltages(mvBuilder);
                        }
                        builder.setElectricVehicle(cANDataEntity.getElectricVehicle());
                        builder.setBatteryInfo(cANDataEntity.getBatteryVehicleInfo());
                    }
                }
            }
            //按照终端规则，合并里程、油耗到标准值。
            lcTools.combineStandValue(builder, packet.getUniqueMark());
            // 2016-04-12 追加CAN数据   ---------- end
//			if(validResult == 1){
            //合法
            Packet outPacket = new Packet();
            outPacket
                    .setCommand(LCAllCommands.AllCommands.Terminal.ReportLocationData_VALUE);
            outPacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
            outPacket.setUniqueMark(packet.getUniqueMark());
            outPacket.setSerialNumber(packet.getSerialNumber());
            outPacket.setContent(builder.build().toByteArray());
            result.setKafkaPacket(outPacket);
            result.setTerminalPacket(super.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS));
            return result;
//			}else if(validResult == 2){
//				//非法位置
//				log.error("非法位置:terminalId="+packet.getUniqueMark()+",原始数据：["+Convert.bytesToHexString(packet.getOriginalPacket())+"]");
//			}else if(validResult == 3){
//				//非法时间
//				log.error("非法时间:terminalId="+packet.getUniqueMark()+",原始数据：["+Convert.bytesToHexString(packet.getOriginalPacket())+"]");
//			}
            //TODO : 此处附加最新CAN数据到位置数据中去.


        } catch (Exception e) {
            log.error("解析位置数据异常,原始数据：[" + Convert.bytesToHexString(packet.getOriginalPacket()) + "].", e);
        }
        PacketResult packetResult = new PacketResult();
        packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0200, LCResultCode.JTTerminal.FAILURE));
        return packetResult;
    }

//	private int leftTopLatitude = Configuration.getInt("LEFT.TOP.LATITUDE");
//	private int leftTopLongitude = Configuration.getInt("LEFT.TOP.LONGITUDE");
//	private int rightBottomLatitude = Configuration.getInt("RIGHT.BOTTOM.LATITUDE");
//	private int rightBottomLongitude = Configuration.getInt("RIGHT.BOTTOM.LONGITUDE");
//	private int legalGpsTime = Configuration.getInt("LEGAL.GPS.TIME");

    /**
     * 验证位置合法性<br>
     * 1=合法;2=非法经纬度;3=非法Gps时间
     * @param latitude
     * @param longitude
     * @param gpsTime
     * @param currentTime
     * @return
     */
//	private int isValidGps(int latitude , int longitude , long gpsTime , long currentTime){
//		if(latitude > leftTopLatitude || latitude < rightBottomLatitude || longitude < leftTopLongitude || longitude > rightBottomLongitude){
//			return 2;
//		}else if(Math.abs(currentTime - gpsTime) > legalGpsTime){
//			return 3;
//		}
//		return 1;
//	}
}
















