package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSatusSyncCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalStateSync;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.StringUtils;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationAdditionProcess_BigEndian;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.lc.LocationAdditionProcess_LittleEndian;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCDrivingBehaviorAnalysis.DrivingBehaviorAnalysis;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationDataType.LocationDataType;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCBatchLocationDataUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author admin
 */
@LocationCommand(id = "0704")
public class JT_0704_BatchLocationDataUpload extends TerminalCommand {
    protected static int GPS_TIME_OUT = 600;//Configuration.getInt("ta_patchDataThreshold");
    private int endianType = 1; //Configuration.getInt("PLATFORM");

    private static Map<String, Packet[]> canCache = new HashMap<String, Packet[]>();
    @Value("${opentsp.platform.name:qingqi}")
    private String platform;
    @Autowired
    TerminalSatusSyncCache syncCache;

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        PacketResult packetResult = new PacketResult();
        try {
            byte[] content = new byte[]{};
            int serial = packet.getPacketSerial();
            int total = packet.getPacketTotal();

            log.info("total:[" + total + "],current:[" + serial + "].");
            if (serial > total) {
                log.error("分包数大于总包数，total:[" + total + "],current:[" + serial + "].");
                return packetResult;
            }
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                    LCResultCode.JTTerminal.SUCCESS));
            if (total != 0 || serial != 0) {
                Packet[] packets = canCache.get(packet.getUniqueMark());
                if (packets == null) {
                    packets = new Packet[total];
                    canCache.put(packet.getUniqueMark(), packets);
                }
                packets[serial - 1] = packet;
                if (serial < total) {
                    return packetResult;
                }
                // TODO ：部分数据包没有推送上来，没处理。
                for (int i = 0; i < packets.length; i++) {
                    content = ArraysUtils.arraycopy(content, packets[i].getContent());
                }
            } else {
                content = packet.getContent();
            }
            canCache.remove(packet.getUniqueMark());

//			byte[] content = packet.getContent();
            if (content == null || content.length == 0) {
                log.error("原始数据：[" + Convert.bytesToHexString(content) + "].");
//				PacketResult packetResult=new PacketResult();
                return packetResult;
            }
            int dataCount = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
            log.info("Location Data Number :[" + dataCount + "].");
            int locationDataType = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 1), 1);
            LCBatchLocationDataUpload.BatchLocationDataUpload.Builder builder = LCBatchLocationDataUpload.BatchLocationDataUpload.newBuilder();
            builder.setTypes(locationDataType == 0 ? LocationDataType.normal : LocationDataType.blindArea);
            int idx = 3;
            for (int i = 0; i < dataCount; i++) {
                if (idx + 2 > content.length) {
                    break;
                }
                int dataLength = Convert.byte2Int(ArraysUtils.subarrays(content, idx, 2), 2);
                if (idx + 2 + dataLength > content.length) {
                    break;
                }
                byte[] locationDataArray = ArraysUtils.subarrays(content, idx + 2, dataLength);
                log.info("Single Location Data Number :[" + dataLength + "].data content [ " + Convert.bytesToHexString(locationDataArray) + " ]");
                if (locationDataArray == null || locationDataArray.length == 0) {
                    log.error("locationDataArray原始数据：[" + Convert.bytesToHexString(locationDataArray) + "].{}", packet.getUniqueMark());
//                    PacketResult packetResult = new PacketResult();
                    return packetResult;
                }
                LocationData data = getLocationDate(locationDataArray, packet);
                if (data!=null&&isToday(data.getGpsDate())) {
                    builder.addDatas(data);
                }

                idx = idx + 2 + dataLength;
            }

            Packet outPacket = new Packet();
            outPacket.setCommand(LCAllCommands.AllCommands.Terminal.BatchLocationDataUpload_VALUE);
            outPacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
            outPacket.setUniqueMark(packet.getUniqueMark());
            outPacket.setSerialNumber(packet.getSerialNumber());
            outPacket.setContent(builder.build().toByteArray());
            ;
            //super.writeToDataProcessing(outPacket);
//            PacketResult packetResult = new PacketResult();
            packetResult.setKafkaPacket(outPacket);
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0704, LCResultCode.JTTerminal.SUCCESS));
            //super.writeKafKaToDP(outPacket,TopicConstants.POSRAW);
            return packetResult;
        } catch (Exception e) {
            log.error("0704数据格式转换错误，uniqueMark:{},content:{}", packet.getUniqueMark(), packet.getContentForHex());
//            return null;//-1;
        }
        return packetResult;
    }

    /***
     * 判断0704补传是否是当天的数据。如果不是当天，则不进行存储
     * @param gpsTime
     * @return
     */
    private boolean isToday(long gpsTime){
        Date gpsDate = new Date(gpsTime * 1000L);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String gpsDateString = sdf.format(gpsDate);
        Date currentDate = new Date();
        String CurrentTimeString = sdf.format(currentDate);

        if (gpsDateString.equals(CurrentTimeString)) {
            return true;//今天的数据,则存储
        } else {
            return false;//"非当天的数据，抛弃");
        }

    }
    private LocationData getLocationDate(byte[] content, Packet packet) {
        try {

            long alarm = Convert.byte2Long(ArraysUtils.subarrays(content, 0, 4), 4);
            long status = Convert.byte2Long(ArraysUtils.subarrays(content, 4, 4), 4);
            int latitude = Convert.byte2Int(ArraysUtils.subarrays(content, 8, 4), 4);
            int longitude = Convert.byte2Int(ArraysUtils.subarrays(content, 12, 4), 4);
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
                e.printStackTrace();
            }

            LocationData.Builder builder = LocationData.newBuilder();
            builder.setAlarm(alarm);
            builder.setStatus(status);
            builder.setOriginalLat(latitude);
            builder.setOriginalLng(longitude);
            builder.setGpsDate(gpsTime);
            builder.setHeight(height);
            builder.setSpeed(speed);
            builder.setDirection(direction);
            builder.setReceiveDate(currentTime);
            builder.setIsPatch(true);
            DrivingBehaviorAnalysis.Builder lCDrivingBehaviorAnalysis = DrivingBehaviorAnalysis.newBuilder();
            builder.setAnalysisData(lCDrivingBehaviorAnalysis);
            builder.setBatteryPower(1);
            byte[] addition = ArraysUtils.subarrays(content, 28, content.length - 28);

            int additionIndex = 0;
            if (addition.length >= 2) {
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
                                builder.setMileage(mileage);
                                //缓存gps里程、gps日期
//                                if (mileage>0) {
//                                    TerminalStateSync sync = syncCache.get(packet.getUniqueMark());
//                                    if (sync != null) {
//                                        sync.setGpsMileage(mileage);
//                                        sync.setGpsDate(gpsTime);
//                                    }else{
//                                        sync = new TerminalStateSync();
//                                        sync.setGpsMileage(mileage);
//                                        sync.setGpsDate(gpsTime);
//                                        syncCache.add(packet.getUniqueMark(),sync);
//                                    }
//                                }
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
                        case 0x25:// 扩展车辆信号状态
                            int signalStatus = Convert.byte2Int(
                                    ArraysUtils.subarrays(addition, additionIndex + 2, additionLength), additionLength);
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
                            builder.setAdditionAlarm(ByteString.copyFrom(alermstatusAddition));
						    //设置 附加报警位 到LocationData的additonAlarm字段上
//						AdditionAlarmParser.parseAlermstatusAddition(alermstatusAddition,packet.getUniqueMark());
                            break;
                        case 0x74:// 车辆状态附加信息:
                            // 取出附件信息
                            byte[] statusAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            if (endianType == 1) {
                                builder.setStatusAddition(LocationAdditionProcess_BigEndian
                                        .getStatusAddition(statusAddition, packet.getUniqueMark()));
                            } else {
                                builder.setStatusAddition(LocationAdditionProcess_LittleEndian
                                        .getStatusAddition(statusAddition, packet.getUniqueMark()));
                            }
                            break;
                        case 0x83:// 车辆故障附加信息
                            byte[] breakdownAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            if (endianType == 1) {
                                builder.setBreakdownAddition(LocationAdditionProcess_BigEndian
                                        .getBreakdownAddition(breakdownAddition, packet.getUniqueMark()));
                            } else {
                                builder.setBreakdownAddition(LocationAdditionProcess_LittleEndian
                                        .getBreakdownAddition(breakdownAddition, packet.getUniqueMark()));
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
                            byte[] LampAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            long lampStatus =(long) (Convert.byte2Long(ArraysUtils.subarrays(LampAddition, 0, 1), 1)*100);
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
                            byte[] TireTemperatureAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
//                            if (endianType == 1) {
//                                builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            } else {
//                                builder.setStatusAddition(LocationAdditionProcess_LittleEndian.getStatusAddition(statusAddition, packet.getUniqueMark()));
//                            }
                            builder.setTemperatureAddition(LocationAdditionProcess_BigEndian.getStatusAdditionByE5(TireTemperatureAddition, packet.getUniqueMark()));
                            break;
                        case 0xE6: //CAN总线数据
                            //取出附件信息
                            byte[] dynamicCanAddition = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                            additionIndex = additionIndex + 2 + additionLength;
                            builder.setStatusAddition(LocationAdditionProcess_BigEndian.getStatusAdditionByE6(dynamicCanAddition, packet.getUniqueMark(),vsaddBuilder,syncCache));
                            //设为已合并，否则导出车况为空
                            builder.setBatteryPower(0);
                            break;
                        case 0xF0://急转弯参数，发生急转弯时的当前角度值
                            byte[] turningAngle = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F0:"+Convert.byte2Int(turningAngle, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setTurningAngle(Convert.byte2Int(turningAngle, 2));
                            break;
                        case 0xF1://发生低油量行驶报警时的油量值
                            byte[] lowOilDrivingValue = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F1:"+Convert.byte2Int(lowOilDrivingValue, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setLowOilDrivingValue(Convert.byte2Int(lowOilDrivingValue, 2));
                            break;
                        case 0xF2://获取到ECU传的车辆速度，该值为当前车辆速度
                            byte[] vehicleSpeedFromEcu = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F2:"+Convert.byte2Int(vehicleSpeedFromEcu, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setVehicleSpeedFromEcu(Convert.byte2Int(vehicleSpeedFromEcu, 2));
                            break;
                        case 0xF3://发生报警时的发动机转速当前值
                            byte[] rpmWhenAlarming = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F3:"+Convert.byte2Int(rpmWhenAlarming, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setRpmWhenAlarming(Convert.byte2Int(rpmWhenAlarming, 2));
                            break;
                        case 0xF4://发生急加速、急减速的当前值
                            byte[] velocityChangeValue = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F4:"+Convert.byte2Int(velocityChangeValue, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setVelocityChangeValue(Convert.byte2Int(velocityChangeValue, 2));
                            break;
                        case 0xF5://车辆当前档位  1个byte
                            byte[] currentGearshift = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F5:"+Convert.byte2Int(currentGearshift, 1)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setCurrentGearshift(Convert.byte2Int(currentGearshift, 1));
                            break;
                        case 0xF6://车辆当前发送机转速
                            byte[] currentRPM = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F6:"+Convert.byte2Int(currentRPM, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setCurrentRPM(Convert.byte2Int(currentRPM, 2));
                            break;
                        case 0xF7://当车辆ACC开启时，终端查询并存储的北斗终端ID号，如果查询不到填0xFF。
                            if (!"FFFFFFFFFFFFFF".equals(Convert
                                    .bytesToHexString(ArraysUtils.subarrays(addition, additionIndex + 2, additionLength)))) {
                                byte[] deviceIdentity = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
                                additionIndex = additionIndex + 2 + additionLength;
                                String deviceIdentityStr = StringUtils.bytesToGbkString(deviceIdentity);
//					    	buffer.append("[F7:"+deviceIdentityStr+"]");
                                lCDrivingBehaviorAnalysis.setDeviceIdentity(deviceIdentityStr);
                            } else {
                                additionIndex = additionIndex + 2 + additionLength;
                            }
                            break;
                        case 0xF8://刹车区间次数
                            byte[] brakeTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F8:"+Convert.byte2Int(brakeTimes, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setBrakeTimes(Convert.byte2Int(brakeTimes, 2));
                            break;
                        case 0xF9://离合区间次数
                            byte[] clutchTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[F9:"+Convert.byte2Int(clutchTimes, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setClutchTimes(Convert.byte2Int(clutchTimes, 2));
                            break;
                        case 0xFA://缓速器次数
                            byte[] retarderTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[FA:"+Convert.byte2Int(retarderTimes, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setRetarderTimes(Convert.byte2Int(retarderTimes, 2));
                            break;
                        case 0xFB://ABS次数
                            byte[] absTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[FB:"+Convert.byte2Int(absTimes, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setAbsTimes(Convert.byte2Int(absTimes, 2));
                            break;
                        case 0xFC://倒档次数
                            byte[] reverseTimes = ArraysUtils.subarrays(addition, additionIndex + 2, additionLength);
//				    	buffer.append("[FC:"+Convert.byte2Int(reverseTimes, 2)+"]");
                            additionIndex = additionIndex + 2 + additionLength;
                            lCDrivingBehaviorAnalysis.setReverseTimes(Convert.byte2Int(reverseTimes, 2));
                            break;
                        default:
                            additionIndex = additionIndex + 2 + additionLength;
                            break;
                    }
                }
//				log.error(buffer.toString());
            }
//			
//			int validResult = this.isValidGps(latitude, longitude, gpsTime, currentTime);
//			if(validResult == 1){
//				//合法
//			}else if(validResult == 2){
//				//非法位置
//			}else if(validResult == 3){
//				//非法时间
//			}
            return builder.build();
        } catch (Exception e) {
            log.error("解析位置数据异常,原始数据：[" + Convert.bytesToHexString(content) + "].", e);
        }

        return null;

    }

//	private int leftTopLatitude = Configuration.getInt("LEFT.TOP.LATITUDE");
//	private int leftTopLongitude = Configuration.getInt("LEFT.TOP.LONGITUDE");
//	private int rightBottomLatitude = Configuration.getInt("RIGHT.BOTTOM.LATITUDE");
//	private int rightBottomLongitude = Configuration.getInt("RIGHT.BOTTOM.LONGITUDE");
//	private int legalGpsTime = Configuration.getInt("LEGAL.GPS.TIME");
//	
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
