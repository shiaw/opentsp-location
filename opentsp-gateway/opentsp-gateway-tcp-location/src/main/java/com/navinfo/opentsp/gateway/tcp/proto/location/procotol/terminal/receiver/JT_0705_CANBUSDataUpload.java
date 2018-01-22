package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.alibaba.fastjson.JSON;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.LastMileageOilTypeCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TerminalSatusSyncCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.BatteryVehicleInfo;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.VehicleBreakdownAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.VehicleStatusAddition;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleBreakdown;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData;
import com.navinfo.opentsp.platform.location.protocol.common.LCVehicleStatusData.VehicleStatusData;
import io.netty.util.internal.logging.InternalLogger;
import io.netty.util.internal.logging.InternalLoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * CAN总线数据上传
 *
 * @author admin
 */
@LocationCommand(id = "0705")
public class JT_0705_CANBUSDataUpload extends TerminalCommand {
    private static final InternalLogger LOGGER = InternalLoggerFactory.getInstance(JT_0705_CANBUSDataUpload.class);
    private static Map<String, Packet[]> canCache = new HashMap<String, Packet[]>();

    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;

    @Autowired
    TerminalSatusSyncCache syncCache;

    /**
     * CAN时间与GPS时间在3分钟以内，认为有效。单位：分钟
     */
    @Value("${CanEffective.time.range:10}")
    private int canEffectiveTime;

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        PacketResult packetResult = new PacketResult();
        try {


            byte[] content = new byte[]{};
            int serial = packet.getPacketSerial();
            int total = packet.getPacketTotal();

            log.info("total:[" + total + "],current:[" + serial + "].");
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                    LCResultCode.JTTerminal.SUCCESS));
            if (serial > total) {
                log.error("分包数大于总包数，total:[" + total + "],current:[" + serial + "].");
                return packetResult;
            }
        LOGGER.info("total:[" + total + "],current:[" + serial + "].");
        Packet packetRes = this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0705, LCResultCode.JTTerminal.SUCCESS);

        packetResult.setTerminalPacket(packetRes);
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
            int dataCount = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
            log.info("0705完整数据包:" + Convert.bytesToHexString(content));
            log.info("CAN 总线数据项个数：[" + dataCount + "]");
            log.info("第1 条CAN 总线数据的接收时间 :" + Convert.bytesToHexString(ArraysUtils.subarrays(content, 2, 5)));
            CANDataEntity obj = new CANDataEntity();
            String canTime = Convert.bytesToHexString(ArraysUtils.subarrays(content, 2, 5));
            Boolean inRegionResult = isInRegion(canTime, packet.getUniqueMark());

            if (inRegionResult == false) {
                log.info("终端" + packet.getUniqueMark());
                //如果不在有效范围之内，则直接抛弃。2017-07-28,与康辉沟通。防止终端补传0705数据。
                //更改实现方式，如果0705时间晚于缓存中的时间，则直接抛弃。 20170731
                return packetResult;
            } else {
                Boolean canResult = checkCanTime(canTime, packet.getUniqueMark(), obj);
                if (canResult == false) {
                    return packetResult;
                }
            }
            byte[] data = ArraysUtils.subarrays(content, 7);
            for (int i = 0; i < dataCount; i++) {
                if (data.length >= 12 * (i + 1)) {
                    byte[] canId = ArraysUtils.subarrays(data, i * 12 + 0, 4);
                    canId[0] = (byte) (canId[0] & 0x1F); // 格式化CANID
                    byte[] canContent = ArraysUtils.subarrays(data, i * 12 + 4, 8);
                    log.info("CAN:[ ID:" + Convert.bytesToHexString(canId) + " , Content:"
                            + Convert.bytesToHexString(canContent) + "] ");
                    // TODO : 直接处理电动车的CAN协议，江淮原来车辆的CAN协议暂时搁置——hk
                    // 电动车协议数据解析采用小端字节序_hk
                    // CANBUSDataHandler.handle(cANBUSDataReport,
                    // Convert.byte2Int(canId, 4), canContent);
                    formatCanData(obj, canId, canContent, packet.getUniqueMark());
                    formatBreakDownData(canId, canContent, data, i, obj, packet.getUniqueMark());
                } else {
                    print("CAN 总线错误数据内容：" + Convert.bytesToHexString(ArraysUtils.subarrays(data, i * 12)));
                    break;
                }
            }
            combineCanData(packet.getUniqueMark(), obj);
            TerminalLastestCANData.putCANData(packet.getUniqueMark(), obj);
            print("电车状态:" + obj.getElectricVehicle());
        } catch (Exception ex) {

        }
//        finally {
//            // getBreakdownAddition(obj, canData, packet.getUniqueMark());
//            return packetResult;
//        }
        return packetResult;
    }

    /**
     * 判断CAN时间与位置数据时间是否在3分钟内，如果超出3分钟，则认为无效，直接不处理。
     *
     * @param canTime
     * @param uniqueMark
     * @return
     */
    private Boolean isInRegion(String canTime, String uniqueMark) {
        canTime = canTime.substring(0, 6);
        Long lastGpsTime = lastMileageOilTypeCache.getLastLocationTime(uniqueMark);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = sdf.format(new Date());
        timeString = timeString.substring(0, 8) + canTime;
        long gpsTime;
        try {
            gpsTime = sdf.parse(timeString).getTime() / 1000;
        } catch (Exception e) {
            // TODO: handle exception
            gpsTime = System.currentTimeMillis() / 1000;
        }
        if (lastGpsTime == null) {
            return true;
        } else {
            Date d = new Date(lastGpsTime * 1000L);
            if (Math.abs(gpsTime - lastGpsTime) > canEffectiveTime * 60) {
                log.info("终端" + uniqueMark + "CAN数据时间：" + canTime + ",GPS时间:" + sdf.format(d));
                return false;
            }
            return true;
        }
    }

    private Boolean checkCanTime(String canTime, String uniqueMark, CANDataEntity obj) {
        canTime = canTime.substring(0, 6);
        Long lastCanTime = lastMileageOilTypeCache.getLastCanTime(uniqueMark);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeString = sdf.format(new Date());
        timeString = timeString.substring(0, 8) + canTime;
        long gpsTime;
        try {
            gpsTime = sdf.parse(timeString).getTime() / 1000;
        } catch (Exception e) {
            // TODO: handle exception
            gpsTime = System.currentTimeMillis() / 1000;
        }

        if (lastCanTime == null) {
            lastMileageOilTypeCache.addLastCanTimeCache(uniqueMark, gpsTime);
            obj.setEcuTime(gpsTime);
            return true;
        } else {
            Date d = new Date(lastCanTime * 1000L);
            if ((gpsTime - lastCanTime) > 0) {
                obj.setEcuTime(gpsTime);
                lastMileageOilTypeCache.addLastCanTimeCache(uniqueMark, gpsTime);
                log.info("终端" + uniqueMark + "CAN数据时间：" + canTime + ",GPS时间:" + sdf.format(d));
                return true;
            }
            return false;
        }
    }

    /**
     * 处理CAN数据。主要解决终端在off时，不上传里程、能耗，导致轨迹回放时对应里程能耗点为0的问题
     */
    private void combineCanData(String niqueMark, CANDataEntity obj) {
        Map<LCStatusType.StatusType, Long> lastMileageOilMap = lastMileageOilTypeCache.getLastMileageOilCache(niqueMark);
        if (lastMileageOilMap != null) {
            for (Map.Entry<LCStatusType.StatusType, Long> item : lastMileageOilMap.entrySet()) {
                VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue(item.getValue());
                statusData.setTypes(item.getKey());
                log.info("hebing 0705   " + niqueMark + "   " + item.getKey().toString() + ":" + item.getValue());
                AddStatus(statusData, obj.getStatusAddition());
            }
        }
    }

    /**
     * 处理生产故障码
     *
     * @param array
     * @return
     */
    private void getBreakdownAddition(CANDataEntity obj, byte[] array, String uniqueMark) {
        print("开始解析拼接后的故障码数据包 , Content:" + Convert.bytesToHexString(array) + "] ");
        int breakdownAdditionIndex = 0;// 开始下表
        VehicleBreakdownAddition.Builder breakdownAddition = obj.getBreakdownAddition();
        if (array.length >= 1) {
            LCVehicleBreakdown.VehicleBreakdown.Builder breakdownBuilder;
            for (int i = 0; i < 4; i++) {
                breakdownBuilder = LCVehicleBreakdown.VehicleBreakdown.newBuilder();
                int SPN_1 = Convert.byte2Int(ArraysUtils.subarrays(array, breakdownAdditionIndex, 1), 1);
                int SPN_2 = Convert.byte2Int(ArraysUtils.subarrays(array, breakdownAdditionIndex + 1, 1), 1);
                int byte_3 = Convert.byte2Int(ArraysUtils.subarrays(array, breakdownAdditionIndex + 2, 1), 1);
                int SPN_MSB = byte_3 >> 5;
                int SPN = SPN_1 + (SPN_2 << 8) + (SPN_MSB << 16);
                int FMI = byte_3 & 31;
                if (SPN > -1 && FMI > -1) {
                    breakdownBuilder.setBreakdownSPNValue(SPN);
                    breakdownBuilder.setBreakdownFMIValue(FMI);
                    print("终端故障码【" + uniqueMark + "】  第【1】个故障码:   原始包: "
                            + Convert.bytesToHexString(ArraysUtils.subarrays(array, breakdownAdditionIndex, 3))
                            + " ,解析值: SPN=" + SPN + ",FMI=" + FMI);
                    breakdownAddition.addBreakdown(breakdownBuilder);
                }
                breakdownAdditionIndex += 4;
            }
        }
    }

    /**
     * 拼装故障码的三个数据包
     *
     * @param uniqueMark 存储
     * @param canId      ID
     * @param canContent CAN内容
     * @param data       原数据包内容
     * @param index      当前第几个CAN。
     */
    private void formatBreakDownData(byte[] canId, byte[] canContent, byte[] data, int index, CANDataEntity obj,
                                     String uniqueMark) {
        // 暂时先处理IdOf18FECA00
        if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FECA00)) {
            // || ArraysUtils.equals(canId, CANDataEntity.IdOf18FECB00)) {
            VehicleBreakdownAddition.Builder breakdownAddition = obj.getBreakdownAddition();
            // DM1/DM2
            LCVehicleBreakdown.VehicleBreakdown.Builder vehicleBreakdown = LCVehicleBreakdown.VehicleBreakdown
                    .newBuilder();
            int SPN_1 = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));
            int SPN_2 = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));
            int byte_3 = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));
            // 当前故障树 byte 5 bit 1-7
            int total = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) & 0x7F;
            int SPN_MSB = byte_3 >> 5;
            int SPN = SPN_1 + (SPN_2 << 8) + (SPN_MSB << 16);
            int FMI = byte_3 & 31;
            vehicleBreakdown.setBreakdownSPNValue(SPN);
            vehicleBreakdown.setBreakdownFMIValue(FMI);
            breakdownAddition.addBreakdown(vehicleBreakdown);
            print("故障码:   原始包: " + Convert.bytesToHexString(ArraysUtils.subarrays(canContent, 2, 3)) + " ,解析值: SPN="
                    + SPN + ",FMI=" + FMI + " 故障总包数:" + total);

            // 如果总包数大于0，则开始获取当前数据后的n个数据包，解析故障码
            // byte[] array = null;
            // if (total > 0) {
            // // 遍历有几个故障码数据包
            // for (int i = 0; i < total; i++) {
            // if (data.length >= 12 * (index + i + 1)) {
            // byte[] breakCanId = ArraysUtils.subarrays(data, i * 12 + 0, 4);
            // canId[0] = (byte) (breakCanId[0] & 0x1F); // 格式化CANID
            // byte[] breakCanContent = ArraysUtils.subarrays(data, (index + i)
            // * 12 + 4, 8);
            // print("开始解析故障码数据包,CAN:[ ID:" + Convert.bytesToHexString(canId) +
            // " , Content:"
            // + Convert.bytesToHexString(breakCanContent) + "] ");
            // if (ArraysUtils.equals(breakCanId, CANDataEntity.IdOf18EBFF00)) {
            // int breakDownPackIndex = Convert
            // .byte2IntLittleEndian(ArraysUtils.subarrays(breakCanContent, 0,
            // 1));// Byte1
            // if (breakDownPackIndex == 1) {
            // // 5 0x 01 第一个包 byte 4-8
            // array = ArraysUtils.subarrays(canContent, 3, 5);
            // } else if (breakDownPackIndex == 2) {
            // // 0x 02 第二个包 byte 2-8
            // ArraysUtils.arraycopy(array, ArraysUtils.subarrays(canContent, 1,
            // 7));
            // } else if (breakDownPackIndex == 3) {
            // // 0x 02 第三个包 byte 2-5
            // ArraysUtils.arraycopy(array, ArraysUtils.subarrays(canContent, 1,
            // 4));
            // }
            // }
            // }
            // }
            // }
            // // 开始解析故障码，判断自己数组中是否为空。
            // if (array != null && array.length > 2) {
            // getBreakdownAddition(obj, array, uniqueMark);
            // }
        }
    }

    /**
     * @param obj
     * @param canId
     * @param canContent
     */
    private void formatCanData(CANDataEntity obj, byte[] canId, byte[] canContent, String unimark) {
        // 根据CANID 解析数据
        BatteryVehicleInfo.Builder info = obj.getBatteryVehicleInfo();
        VehicleStatusAddition.Builder vehicleAddition = obj.getStatusAddition();
        VehicleBreakdownAddition.Builder breakdownAddition = obj.getBreakdownAddition();
        long vehicleStatus = obj.getElectricVehicle();
        if (ArraysUtils.equals(canId, CANDataEntity.IdOf10F8159E)) {
            // 电池组（SOC）剩余电量
            int power = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));
            if (power != CANDataEntity.BytesOf1) {
                if (InRegion(0, 100, power)) {
                    obj.setBatteryPower(power);
                } else {
                    PrintExceptionData("电量：", 0, 100,
                            Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1)));
                }
            }
            int status = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));
            if (status != CANDataEntity.BytesOf1) {
                info.setBmsStatus(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)));
            }
            int aveTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));
            if (aveTem != CANDataEntity.BytesOf1) {
                if (InRegion(-40, 210, aveTem - 40)) {
                    info.setBatteryAveTem(aveTem - 40);
                } else {
                    PrintExceptionData("电池组平均温度：", -40, 210,
                            Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) - 40);
                }
            }
            int current = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 2));
            if (current != CANDataEntity.BytesOf2) {
                if (InRegion(-3200, 3354, (long) (current * 0.1) - 3200)) {
                    info.setBatteryCurrent((current * 0.1f) - 3200);
                } else {
                    PrintExceptionData("电池组充放电电流：", -3200, 3354, (long) (current * 0.1) - 3200);
                }
            }
            int BatteryV = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 2));
            if (BatteryV != CANDataEntity.BytesOf2) {
                if (InRegion(0, 800,
                        (long) (BatteryV * 0.1f))) {
                    info.setTotalBatteryV(BatteryV * 0.1f);
                } else {
                    PrintExceptionData("电池组总电压：", 0, 800,
                            (long) (BatteryV * 0.1f));
                }
            }
            print("电量：[" + obj.getBatteryPower() + "]");
            print("BMS 基本状态：[" + info.getBmsStatus() + "]");
            print("电池组平均温度：[" + info.getBatteryAveTem() + "] 摄氏度");
            print("电池组充放电电流：[" + info.getBatteryCurrent() + "] A");
            print("电池组总电压：[" + info.getTotalBatteryV() + "] V");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F40217)) {
            // CANID --- 总里程
            // long mile =
            // Convert.byte2LongLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 4));
            // obj.setMileage(mile * 125);
            // print("整车里程：[" + obj.getMileage() + "] 米");
            long mileage = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4));
            if (mileage != CANDataEntity.BytesOf4) {
                VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue(
                        (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4)) * 0.125 * 100)); //
                statusData.setTypes(LCStatusType.StatusType.mileage);
                AddStatus(statusData, vehicleAddition);
                // vehicleAddition.addStatus(statusData);
                obj.setMileage(statusData.getStatusValue());
                print("总里程： km [" + statusData.getStatusValue() + "]");
            } else {
                PrintExceptionData("总里程异常:", 0, 4294967295L, mileage);
            }
        } else if (canId[0] == (byte) 0x18 && canId[2] == (byte) 0xD1 && canId[3] == (byte) 0xF3 && canId[1] <= 60) {
            for (int i = 0; i < 8; i = i + 2) {
                ModuleVoltageEntity mv = new ModuleVoltageEntity();
                mv.setModuleNum((int) canId[1]);
                mv.setNumber(canContent[i + 1] >> 4);
                int vv = ((canContent[i + 1] << 8) + canContent[i]) & 0x0FFF;
                mv.setVoltage(vv * 0.01f);
                obj.getModuleVoltageList().add(mv);
                print("模块号：[" + mv.getModuleNum() + "], 箱号:[ " + mv.getNumber() + "], 电压:[ " + mv.getVoltage() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F8229E)) {// 充电状态
            // info.setMaxChargingE(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 2)) * 0.1f);
            // print("最高允许充电电流：[" + info.getMaxChargingE() + "] A");
            // info.setMaxChargingV(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 2, 2)));// 最高允许充电电流，没有数据处理说明
            // // TODO
            // print("最高允许充电端电压：[" + info.getMaxChargingV() + "] V");
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 2)) != CANDataEntity.BytesOf2) {
                info.setMaxChargingV(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 2)) * 0.1f);
                print("最高允许充电端电压：[" + info.getMaxChargingV() + "]  V");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 2)) != CANDataEntity.BytesOf2) {
                info.setMaxChargingE(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 2)));// 最高允许充电电流，没有数据处理说明
                print("最高允许充电电流：[" + info.getMaxChargingE() + "] A");
            }
            if ((canContent[4] & 0x01) == 0x01) {// 充电机状态
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x01);
                print("充电机状态：[充电中...]" + (obj.getElectricVehicle() | 0x01));
            }
            if ((canContent[5] & 0x01) == 0x01) {// 电池充电状态
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0080);
                print("电池充电状态：[充电中...]" + (obj.getElectricVehicle() | 0x0080));
            }
            if ((canContent[5] & 0x02) == 0x02) {// 车载充电线的连接状态
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0040);
                print("车载充电线的连接状态：[充电中...]" + (obj.getElectricVehicle() | 0x0040));
            }
            if ((canContent[5] & 0x04) == 0x04) {// 快充线的连接状态
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0020);
                print("快充线的连接状态：[充电中...]" + (obj.getElectricVehicle() | 0x0020));
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf1801d0d7)) {// 整车漏电报警
            if (canContent[6] == (byte) 0x55) {
                obj.setElectricVehicle(vehicleStatus | 0x0002);
                print("整车漏电报警：[报警..]" + (vehicleStatus | 0x0002));
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F501F0)) {
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) != CANDataEntity.BytesOf1) {
                info.setMotorTemperature(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) - 40);
                print("电机温度：[" + info.getMotorTemperature() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) != CANDataEntity.BytesOf1) {
                info.setMotorControlTemp(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) - 40);
                print("电机控制器温度：[" + info.getMotorControlTemp() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1)) != CANDataEntity.BytesOf1) {
                info.setMcuFaultCode(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1)));
                print("MCU系统故障代码：[" + info.getMcuFaultCode() + "]");
            }
            if ((canContent[6] & 0x10) == 0x10) {// 电机温度报警
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0004);
                print("电机温度报警：[报警..]" + (obj.getElectricVehicle() | 0x0004));
            }
            if ((canContent[6] & 0x80) == 0x80) {// MCU 故障状态
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0008);
                print("MCU 故障状态：[故障..]" + (obj.getElectricVehicle() | 0x0008));
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf10F81D9E)) {
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) != CANDataEntity.BytesOf1) {
                info.setBatteryFaultCode(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)));
                print("电池组系统故障信息：[" + info.getBatteryFaultCode() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf10F8169E)) {
            int maxTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));
            if (maxTem != CANDataEntity.BytesOf1) {
                if (InRegion(-40, 210, maxTem - 40)) {
                    info.setBatteryMaxTem(maxTem - 40);
                } else {
                    PrintExceptionData("电池组允许最高温度", -40, 210, maxTem - 40);
                }
                print("电池组允许最高温度：[" + info.getBatteryMaxTem() + "]");
            }
            int minTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));
            if (minTem != CANDataEntity.BytesOf1) {
                if (InRegion(-40, 210, Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) - 40)) {
                    info.setBatteryMinTem(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) - 40);
                } else {
                    PrintExceptionData("电池组允许最低温度", -40, 210,
                            Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) - 40);
                }
                print("电池组允许最低温度：[" + info.getBatteryMinTem() + "]");
            }
            int MinSoc = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));
            if (MinSoc != CANDataEntity.BytesOf1) {
                if (InRegion(0, 100, Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1)))) {
                    info.setBatteryMinSoc(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1)));
                } else {
                    PrintExceptionData("电池组允许最低使用 SOC", 0, 100,
                            Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1)));
                }
                print("电池组允许最低使用 SOC：[" + info.getBatteryMinSoc() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F101D0)) {
            // info.setTotalStatus(canContent[0] >> 6);
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) != CANDataEntity.BytesOf1) {
                info.setTotalStatus(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) >> 6);
                print("整车状态：[" + info.getTotalStatus() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2)) != CANDataEntity.BytesOf2) {
                info.setVehicleSpeed(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2)) / 256f);
                print("车辆速度：[" + info.getVehicleSpeed() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) != CANDataEntity.BytesOf1) {
                info.setShiftStatus(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) - 125);// TODO
                // 是否减125
                print("档位状态：[" + info.getShiftStatus() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1)) != CANDataEntity.BytesOf1) {
                info.setVcuFaultCode(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1)));
                print("VCU 系统故障码：[" + info.getVcuFaultCode() + "]");
            }
            if ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) & 0x02) == 0x02) {// 变速器故障
                // if ((canContent[4] & 0x02) == 0x02) {// 变速器故障
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0100);
                print("变速器故障：[故障..]" + (obj.getElectricVehicle() | 0x0100));
            }
            if ((canContent[4] & 0x01) == 0x01) {// VCU 故障
                obj.setElectricVehicle(obj.getElectricVehicle() | 0x0200);
                print("VCU 故障：[故障..]" + (obj.getElectricVehicle() | 0x0200));
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F103D0)) {
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2)) != CANDataEntity.BytesOf2) {
                info.setEnduranceMileage(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2)) * 125);
                print("续航里程：[" + info.getEnduranceMileage() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) != CANDataEntity.BytesOf1) {
                info.setAcceleratorPedal(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) * 0.4f);
                print("油门踏板状态：[" + info.getAcceleratorPedal() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) != CANDataEntity.BytesOf1) {
                info.setBrakePedal(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) * 0.4f);
                print("制动踏板状态：[" + info.getBrakePedal() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1)) != CANDataEntity.BytesOf1) {
                info.setModelInfo(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1)));
                print("整车模式信息：[" + info.getModelInfo() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1)) != CANDataEntity.BytesOf1) {
                info.setSwitchInfo(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1)));
				print("整车部分开关量信息：[" + info.getSwitchInfo() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1)) != CANDataEntity.BytesOf1) {
                info.setElectricAttachment(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1)));
                print("电附件使能：[" + info.getElectricAttachment() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F502F0)) {
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 2)) != CANDataEntity.BytesOf2) {
                info.setMotorRpm(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 2)));
                print("驱动电机转速：[" + info.getMotorRpm() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf0CF00400)) {
            // byte 0
            VehicleStatusData.Builder statusengineTorModeData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusengineTorModeData.setStatusValue(
                    (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) & 0x0F) * 100);// 0-3
            // 0x0F
            // 1111
            statusengineTorModeData.setTypes(LCStatusType.StatusType.engineTorMode);
            if (InRegion(0, 11, statusengineTorModeData.getStatusValue() / 100)) {
                vehicleAddition.addStatus(statusengineTorModeData.build());
            } else {
                PrintExceptionData("转矩控制模式：", 0, 11, statusengineTorModeData.getStatusValue());
            }
            // obj.setEngineTorMode((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))&0x0F)*100);//0-3 0x0F 1111
            print("转矩控制模式：[" + statusengineTorModeData.getStatusValue() + "]");
            // byte 1

            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            //Actual engine - percent torque high resolution
            // byte1
            int perTorResolution = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-8bit 0xF0 11110000
            statusData.setStatusValue((long) (((perTorResolution & 0xF0) >> 4) * 0.125 * 100)); //

            statusData.setTypes(LCStatusType.StatusType.perTorResolution);
            vehicleAddition.addStatus(statusData);
            print("Actual engine - percent torque high resolution：[" + statusData.getStatusValue() + "]");


            VehicleStatusData.Builder DriverEnginePercentTor = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int driverEnginePercentTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));
            if (driverEnginePercentTor != CANDataEntity.BytesOf1) {
                DriverEnginePercentTor.setStatusValue((long) (driverEnginePercentTor - 125) * 100);
                // DriverEnginePercentTor.setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 1, 1))*100);
                DriverEnginePercentTor.setTypes(LCStatusType.StatusType.driverEnginePercentTor);
                if (InRegion(-125, 125, DriverEnginePercentTor.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(DriverEnginePercentTor);
                } else {
                    PrintExceptionData("驾驶员需求发动机转矩百分比：[", -125, 125, DriverEnginePercentTor.getStatusValue());
                }
                // obj.setDriverEnginePercentTor(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 1, 1))*100);
                print("驾驶员需求发动机转矩百分比：[" + DriverEnginePercentTor.getStatusValue() + "]");
            }
            // byte 2

            VehicleStatusData.Builder ActualEnginePercentTor = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int actualEnginePercentTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));
            // ActualEnginePercentTor.setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 2, 1))*100);
            if (actualEnginePercentTor != CANDataEntity.BytesOf1) {
                ActualEnginePercentTor.setStatusValue((long) (actualEnginePercentTor - 125) * 100);
                ActualEnginePercentTor.setTypes(LCStatusType.StatusType.actualEnginePercentTor);
                if (InRegion(-125, 125, ActualEnginePercentTor.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(ActualEnginePercentTor);
                } else {
                    PrintExceptionData("实际发动机转矩百分比：", -125, 125, ActualEnginePercentTor.getStatusValue());
                }

                // obj.setActualEnginePercentTor(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 1))*100);
                print("实际发动机转矩百分比：[" + ActualEnginePercentTor.getStatusValue() + "]");
            }
            // byte 3-4
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int rotation = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 2));
            if (rotation != CANDataEntity.BytesOf2) {
                statusData.setStatusValue((long) (rotation * 100 * 0.125)); // byte0
                statusData.setTypes(LCStatusType.StatusType.rotation);
                if (InRegion(0, 8032, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                } else {
                    PrintExceptionData("发动机转速：", 0, 8032, statusData.getStatusValue());
                }
                // obj.setParkingBrakeSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 0, 1))&0x06)*100); //byte0 ,2-3 0x06 110
                print("发动机转速：[" + statusData.getStatusValue() + "]");
            }
            //Source Address of Controlling Device for Engine Control/控制发动机设备源地址
            // byte6
            int addControlDevice = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1));// Byte
            if (addControlDevice != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 2 5-8bit 0xF0 11110000
                statusData.setStatusValue((long) (addControlDevice * 100)); //

                statusData.setTypes(LCStatusType.StatusType.addControlDevice);
                vehicleAddition.addStatus(statusData);
                print("Source Address of Controlling Device for Engine Control/控制发动机设备源地址：[" + statusData.getStatusValue() + "]");
            }
            //Engine Starter Mode/起动机模式
            // byte2
            int engineStartMode = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-8bit 0xF0 11110000
            statusData.setStatusValue((long) ((engineStartMode & 0x0F) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.engineStartMode);
            vehicleAddition.addStatus(statusData);
            print("Engine Starter Mode/起动机模式：[" + statusData.getStatusValue() + "]");

            //Engine Demand - Percent Torque/发动机需求扭矩百分比
            // byte7
            int engineDePerTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1));// Byte
            if (engineDePerTor != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((engineDePerTor - 125) * 100)); //

                statusData.setTypes(LCStatusType.StatusType.engineDePerTor);
                vehicleAddition.addStatus(statusData);
                print("Engine Demand - Percent Torque/发动机需求扭矩百分比：[" + statusData.getStatusValue() + "]");
            }

        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf0CF00300)) {
            // byte 0
            VehicleStatusData.Builder AccPedalLowIdleSwitch = LCVehicleStatusData.VehicleStatusData.newBuilder();
            AccPedalLowIdleSwitch.setStatusValue(
                    (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) & 0x03) * 100); // 0-1
            // 0x03
            // 11
            AccPedalLowIdleSwitch.setTypes(LCStatusType.StatusType.accPedalLowIdleSwitch);
            vehicleAddition.addStatus(AccPedalLowIdleSwitch);
            // obj.setAccPedalLowIdleSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))&0x03)*100); //0-1 0x03 11
            print("加速踏板低怠速开关：[" + AccPedalLowIdleSwitch.getStatusValue() + "]");

            AccPedalLowIdleSwitch = LCVehicleStatusData.VehicleStatusData.newBuilder();
            AccPedalLowIdleSwitch.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) & 0x0C) >> 2) * 100);// 2-3
            // 0x0C
            // 1100
            AccPedalLowIdleSwitch.setTypes(LCStatusType.StatusType.accPedalKickdownSwitch);
            vehicleAddition.addStatus(AccPedalLowIdleSwitch);
            // obj.setAccPedalKickdownSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))&0x0C)*100);//2-3 0x0C 1100
            print("加速踏板Kickdown开关：[" + AccPedalLowIdleSwitch.getStatusValue() + "]");

            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            //Road speed limit status/速度限制状态
            // byte0
            int speedLimitStatus = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 0 5-6bit 0x30 110000
            statusData.setStatusValue((long) (((speedLimitStatus & 0x30) >> 4) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.speedLimitStatus);
            vehicleAddition.addStatus(statusData);
            print("Road speed limit status/速度限制状态：[" + statusData.getStatusValue() + "]");


            // byte 1
            AccPedalLowIdleSwitch = LCVehicleStatusData.VehicleStatusData.newBuilder();
            AccPedalLowIdleSwitch.setStatusValue(
                    (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1)) * 100 * 0.4));
            AccPedalLowIdleSwitch.setTypes(LCStatusType.StatusType.accPedalPos);
            if (InRegion(0, 100, AccPedalLowIdleSwitch.getStatusValue() / 100)) {
                vehicleAddition.addStatus(AccPedalLowIdleSwitch);
            } else {
                PrintExceptionData("加速踏板开度：", 0, 100, AccPedalLowIdleSwitch.getStatusValue() / 100);
            }
            // obj.setAccPedalPos((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 1, 1))*100*0.4));
            print("加速踏板开度：[" + AccPedalLowIdleSwitch.getStatusValue() + "]");
            // byte 2
            AccPedalLowIdleSwitch = LCVehicleStatusData.VehicleStatusData.newBuilder();
            AccPedalLowIdleSwitch
                    .setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1)) * 100);
            AccPedalLowIdleSwitch.setTypes(LCStatusType.StatusType.percentLoadAtCurrentSpd);
            if (InRegion(0, 125, AccPedalLowIdleSwitch.getStatusValue() / 100)) {
                vehicleAddition.addStatus(AccPedalLowIdleSwitch);
            } else {
                PrintExceptionData("当前速度下，负载百分比：", 0, 125, AccPedalLowIdleSwitch.getStatusValue() / 100);
            }
            // obj.setPercentLoadAtCurrentSpd(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 2, 1))*100);
            print("当前速度下，负载百分比：[" + AccPedalLowIdleSwitch.getStatusValue() + "]");


            //Remote accelerator position
            // byte4
            int reAccPosition = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) (reAccPosition * 100)); //

            statusData.setTypes(LCStatusType.StatusType.reAccPosition);
            vehicleAddition.addStatus(statusData);
            print("Remote accelerator position：[" + statusData.getStatusValue() + "]");

            //Remote accelerator position2
            // byte5
            int reAccPosition2 = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) (reAccPosition2 * 100)); //

            statusData.setTypes(LCStatusType.StatusType.reAccPosition2);
            vehicleAddition.addStatus(statusData);
            print("Remote accelerator position2：[" + statusData.getStatusValue() + "]");

            //Cold Start Heater Status
            // byte6
            int maxAvailableEngPerTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) (maxAvailableEngPerTor * 0.4 * 100)); //

            statusData.setTypes(LCStatusType.StatusType.maxAvailableEngPerTor);
            vehicleAddition.addStatus(statusData);
            print("Actual maximum available engine percentage torque：[" + statusData.getStatusValue() + "]");

        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEDF00)) {
            // byte 0
            VehicleStatusData.Builder NominalFrictionPercentTrq = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int nominalFrictionPercentTrq = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));
            // NominalFrictionPercentTrq.setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))*100);
            NominalFrictionPercentTrq.setStatusValue((long) (nominalFrictionPercentTrq - 125) * 100);
            NominalFrictionPercentTrq.setTypes(LCStatusType.StatusType.nominalFrictionPercentTrq);
            if (InRegion(0, 125, NominalFrictionPercentTrq.getStatusValue() / 100)) {
                vehicleAddition.addStatus(NominalFrictionPercentTrq);
            } else {
                PrintExceptionData("名义摩擦力矩百分比：", 0, 125, NominalFrictionPercentTrq.getStatusValue());
            }
            // obj.setNominalFrictionPercentTrq(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))*100);
            print("名义摩擦力矩百分比：[" + NominalFrictionPercentTrq.getStatusValue() + "]");


            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            //engine's desired operating speed/发动机目标运行速度
            // byte1-2
            int desirOperaSpeed = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) (desirOperaSpeed * 0.125 * 100)); //

            statusData.setTypes(LCStatusType.StatusType.desirOperaSpeed);
            vehicleAddition.addStatus(statusData);
            print("engine's desired operating speed/发动机目标运行速度：[" + statusData.getStatusValue() + "]");

            //engine's operating speed asymmetry adjustment/发动机平稳调整
            // byte3
            int engAsyAdjust = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) (engAsyAdjust * 100)); //

            statusData.setTypes(LCStatusType.StatusType.engAsyAdjust);
            vehicleAddition.addStatus(statusData);
            print("engine's operating speed asymmetry adjustment/发动机平稳调整：[" + statusData.getStatusValue() + "]");

            //Estimated Engine Parasitic Losses
            // byte4
            int EstimaParLoss = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) (EstimaParLoss * 100)); //

            statusData.setTypes(LCStatusType.StatusType.EstimaParLoss);
            vehicleAddition.addStatus(statusData);
            print("Estimated Engine Parasitic Losses：[" + statusData.getStatusValue() + "]");


            //Exhaust gas mass flow
            // byte5-6
            int exhGasMassflow = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 2));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-8bit 0xF0 11110000
            statusData.setStatusValue((long) (exhGasMassflow * 0.2 * 100)); //

            statusData.setTypes(LCStatusType.StatusType.exhGasMassflow);
            vehicleAddition.addStatus(statusData);
            print("Exhaust gas mass flow：[" + statusData.getStatusValue() + "]");

            //Cold Start Heater Status
            // byte7
            int af1Intake = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 1-2bit 0x03 00000011
            statusData.setStatusValue((long) ((af1Intake & 0x03) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.af1Intake);
            vehicleAddition.addStatus(statusData);
            print("Aftertreatment 1 Intake Dew Point：[" + statusData.getStatusValue() + "]");


            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 3-4bit 0x0C 00001100
            statusData.setStatusValue((long) (((af1Intake & 0x0C) >> 2) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.af1Exhaust);
            vehicleAddition.addStatus(statusData);
            print("Aftertreatment 1 Intake Dew Point：[" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 5-6bit 0x0C 00110000
            statusData.setStatusValue((long) (((af1Intake & 0x30) >> 4) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.af2Intake);
            vehicleAddition.addStatus(statusData);
            print("Aftertreatment 2 Intake Dew Point：[" + statusData.getStatusValue() + "]");


            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 7-8bit 0xC0 11000000
            statusData.setStatusValue((long) (((af1Intake & 0xC0) >> 6) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.af2Exhaust);
            vehicleAddition.addStatus(statusData);
            print("Aftertreatment 2 Exhaust Dew Point：[" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEF100)) {
            // byte 0
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) & 0x0C) >> 2) * 100); // byte0
            // ,3-4
            // 0x0c
            // 1100
            statusData.setTypes(LCStatusType.StatusType.parkingBrakeSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setParkingBrakeSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))&0x06)*100); //byte0 ,2-3 0x06 110
            print("驻车制动器开关：[" + statusData.getStatusValue() + "]");


            //Cold Start Heater Status
            // byte0
            int cruPauseSwitch = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-6bit 0xC0 00110000
            statusData.setStatusValue((long) (((cruPauseSwitch & 0x30) >> 4) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.cruPauseSwitch);
            vehicleAddition.addStatus(statusData);
            print("Cruise control Pause Switch：[" + statusData.getStatusValue() + "]");

            // byte1-2
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2)) * 100 / 256)); // byte1-2
            statusData.setTypes(LCStatusType.StatusType.wheelBasedVehicleSpd);
            if (InRegion(0, 251, statusData.getStatusValue() / 100)) {
                vehicleAddition.addStatus(statusData);
            } else {
                PrintExceptionData("基于车轮的车速：[", 0, 251, statusData.getStatusValue() / 100);
            }
            // obj.setWheelBasedVehicleSpd((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 1, 2))*100/256)); //byte1-2
            print("基于车轮的车速：[" + statusData.getStatusValue() + "]");

            // byte 3
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) & 0x03) * 100); // 0-1
            // ,0x03
            // 11
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlActive);
            vehicleAddition.addStatus(statusData);
            // obj.setCruiseCtrlActive((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 3, 1))&0x03)*100); //0-1 ,0x03 11
            print("巡航控制开关状态：[" + statusData.getStatusValue() + "]");


            // byte 3
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) & 0x0C) >> 2) * 100); // 2-3
            // ,0x0C
            // 1100
            statusData.setTypes(LCStatusType.StatusType.cruiControlEnable);
            vehicleAddition.addStatus(statusData);
            // obj.setCruiseCtrlActive((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 3, 1))&0x03)*100); //0-1 ,0x03 11
            print("Cruise control enable/巡航控制使能：[" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) & 0x30) >> 4) * 100); // 4-5
            // 0x30
            // 110000
            statusData.setTypes(LCStatusType.StatusType.brakeSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setBrakeSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,3,
            // 1))&0x30)*100); // 4-5 0x30 110000
            print("制动开关：[" + statusData.getStatusValue() + "]");
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1)) & 0xC0) >> 6) * 100);// 6-7
            // 0xC0
            // 11000000
            statusData.setTypes(LCStatusType.StatusType.clutchSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setClutchSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 3, 1))&0xC0)*100);//6-7 0xC0 11000000
            print("离合器开关：[" + statusData.getStatusValue() + "]");

            // byte 4
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) & 0x03) * 100); // 0-1
            // ,0x03
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlSetSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setCruiseCtrlSetSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 4, 1))&0x03)*100); //0-1 ,0x03 11
            print("巡航控制设置开关：[" + statusData.getStatusValue() + "]");
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) & 0x0C) >> 2) * 100); // 2-3
            // ,0x03
            // 1100
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlCoastSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setCruiseCtrlCoastSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 4, 1))&0x0C)*100); //2-3 ,0x03 1100
            print("巡航控制减速开关：[" + statusData.getStatusValue() + "]");
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) & 0x30) >> 4) * 100); // 4-5
            // 0x30
            // 110000
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlResumeSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setCruiseCtrlResumeSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,4,
            // 1))&0x30)*100); // 4-5 0x30 110000
            print("巡航控制恢复开关：[" + statusData.getStatusValue() + "]");
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1)) & 0xC0) >> 6) * 100);// 6-7
            // 0xC0
            // 11000000
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlAccSwitch);
            vehicleAddition.addStatus(statusData);
            // obj.setCruiseCtrlAccSwitch((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 4, 1))&0xC0)*100);//6-7 0xC0 11000000
            print("巡航控制加速开关：[" + statusData.getStatusValue() + "]");

            // byte 5
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1)) * 100); //
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlSetSpd);
            if (InRegion(0, 250, statusData.getStatusValue() / 100)) {
                vehicleAddition.addStatus(statusData);
            } else {
                PrintExceptionData("巡航控制设置速度：", 0, 250, statusData.getStatusValue());
            }
            // obj.setCruiseCtrlSetSpd(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 5, 1))*100); //
            print("巡航控制设置速度：[" + statusData.getStatusValue() + "]");

            // byte 6
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    ((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1)) & 0xE0) >> 5) * 100);// 5-7
            // 0xE0
            // 11100000
            statusData.setTypes(LCStatusType.StatusType.cruiseCtrlStates);
            if (InRegion(0, 6, statusData.getStatusValue() / 100)) {
                vehicleAddition.addStatus(statusData);
            } else {
                PrintExceptionData("巡航控制状态：", 0, 6, statusData.getStatusValue());
            }
            // obj.setCruiseCtrlStates((Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 6, 1))&0xE0)*100);//5-7 0xE0 11100000
            print("巡航控制状态：[" + statusData.getStatusValue() + "]");


            //Cold Start Heater Status
            // byte6
            int ptoState = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 6 5-8bit 0x1F 11111
            statusData.setStatusValue((long) ((ptoState & 0x1F) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.ptoState);
            vehicleAddition.addStatus(statusData);
            print("PTO state/PTO 状态：[" + statusData.getStatusValue() + "]");

			/*
             * 以下为第8个字节内容
			 */

            int engShutOverSwitch = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 6-8bit 0xC0 11000000
            statusData.setStatusValue((long) (((engShutOverSwitch & 0xC0) >> 6) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.engShutOverSwitch);
            vehicleAddition.addStatus(statusData);
            print("Engine shut down override switch：[" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 5-6bit 0xC0 00110000
            statusData.setStatusValue((long) (((engShutOverSwitch & 0x30) >> 4) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.engTestModeWwitch);
            vehicleAddition.addStatus(statusData);
            print("Engine Test Mode Switch：[" + statusData.getStatusValue() + "]");


            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 3-4bit 0xC0 00001100
            statusData.setStatusValue((long) (((engShutOverSwitch & 0x0C) >> 2) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.engIdleDecSwitch);
            vehicleAddition.addStatus(statusData);
            print("Engine Idle decrement Switch：[" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 7 1-2bit 0xC0 00000011
            statusData.setStatusValue((long) ((engShutOverSwitch & 0x03) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.engIdleIncSwitch);
            vehicleAddition.addStatus(statusData);
            print("Engine Idle increment switch：[" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEE000)) {
            // byte 0-3
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4)) * 0.125 * 100)); //
            statusData.setTypes(LCStatusType.StatusType.tripDistance);
            vehicleAddition.addStatus(statusData);
            // obj.setTripDistance((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 4))*100*0.125)); //
            print("日里程： km [" + statusData.getStatusValue() + "]");

            // byte 4-7
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            double mileage = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.125;
            if (mileage > 5000000) {// 如果上传数值超过500万公里，肯定数据有 问题，直接将里程赋值为0.
                statusData.setStatusValue(0);
            } else {
                statusData.setStatusValue(
                        (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.125 * 100)); //
            }
            statusData.setTypes(LCStatusType.StatusType.mileage);
            AddStatus(statusData, vehicleAddition);
            addLastOilMileageCache(unimark, statusData);
            // vehicleAddition.addStatus(statusData);
            // obj.setMileage(statusData.getStatusValue());
            // obj.setTripDistance((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 4))*100*0.125)); //
            print("总里程： km [" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEF200)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 2)) != CANDataEntity.BytesOf2) {
                statusData.setStatusValue((long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 2))
                        * 100 * 0.001953125));// byte
                // 3-4
                statusData.setTypes(LCStatusType.StatusType.realTimeOilConsumption);
                vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("实时油耗率： Scale:0.001953125Km/L/bit[" + statusData.getStatusValue() + "]");
            }
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int fuelConsumptionRate = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 2));// byte
            // 1-2
            // statusData.setStatusValue((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 2))*100*0.05));
            if (fuelConsumptionRate != CANDataEntity.BytesOf2) {
                statusData.setStatusValue((long) (fuelConsumptionRate * 1000 * 100 * 0.05));
                statusData.setTypes(LCStatusType.StatusType.fuelConsumptionRate);
                vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("发动机燃油消耗率： Scale:0.05L/h/bit [" + statusData.getStatusValue() + "]");
            }
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int averageFuelConsumption = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 2)); // byte
            // 5-6
            // statusData.setStatusValue((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 2))*100*0.05));
            if (averageFuelConsumption != CANDataEntity.BytesOf2) {
                statusData.setStatusValue((long) (averageFuelConsumption * 0.001953125) * 100);
                statusData.setTypes(LCStatusType.StatusType.averageFuelConsumption);
                vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("平均燃油消耗率： Scale:0.001953125Km/L/bit [" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEEE00)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int coolingWaterTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));
            // statusData.setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))*100);
            if (coolingWaterTem != CANDataEntity.BytesOf1) {
                statusData.setStatusValue((long) (coolingWaterTem * 1 - 40) * 100);
                statusData.setTypes(LCStatusType.StatusType.coolingWaterTem);
                vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("发动机冷却水温度：[" + statusData.getStatusValue() + "]");
            }
            // 燃油温度 byte 2
            int fuelTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            // 5-8
            if (fuelTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (fuelTem * 1 - 40) * 100);
                statusData.setTypes(LCStatusType.StatusType.fuelTem);
                vehicleAddition.addStatus(statusData.build());
                print("燃油温度  Scale:1℃/bit Offset:-40℃：[" + statusData.getStatusValue() + "]");
            }
            // /机油温度 byte 3-4
            int oilTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 2));// Byte
            // 5-8
            if (oilTem != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (oilTem * 0.03125 - 273) * 100);
                statusData.setTypes(LCStatusType.StatusType.oilTem);
                vehicleAddition.addStatus(statusData.build());
                print("/机油温度  Scale:0.03125℃/bit Offset:-273℃：[" + statusData.getStatusValue() + "]");
            }
            // /增压器机油温度 5-6
            int truboOilTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 2));// Byte
            // 5-8
            if (truboOilTem != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) truboOilTem * 100);
                statusData.setTypes(LCStatusType.StatusType.truboOilTem);
                vehicleAddition.addStatus(statusData.build());
                print("/Turbo Oil Temperature增压器机油温度：[" + statusData.getStatusValue() + "]");
            }
            // Engine Intercooler Temperature 7-8
            int engineInterTemper = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 2));// Byte
            // 5-8
            if (engineInterTemper != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) engineInterTemper * 100);
                statusData.setTypes(LCStatusType.StatusType.engineInterTemper);
                vehicleAddition.addStatus(statusData.build());
                print("/Engine Intercooler Temperature：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FE5600)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int ureaTankLiquidLevel = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));
            if (ureaTankLiquidLevel != CANDataEntity.BytesOf1) {
                statusData.setStatusValue(
                        (long) (ureaTankLiquidLevel * 100 * 0.4));
                statusData.setTypes(LCStatusType.StatusType.ureaTankLiquidLevel);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                }
                print("尿素液位：[" + statusData.getStatusValue() + "]");
            }

            // 当前档位 byte 2
            int ureaTankTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            if (ureaTankTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ureaTankTem * 100);
                statusData.setTypes(LCStatusType.StatusType.ureaTankTem);
                vehicleAddition.addStatus(statusData);
                print("尿素箱温度：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEF700)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // Battery Potential/Power Input 1
            int batteryPotInput1 = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 2));
            // statusData.setStatusValue(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 1))*100);
            if (batteryPotInput1 != CANDataEntity.BytesOf1) {
                statusData.setStatusValue((long) (batteryPotInput1 * 0.05) * 100);
                statusData.setTypes(LCStatusType.StatusType.batteryPotInput1);
                vehicleAddition.addStatus(statusData);
                print("Battery Potential/Power Input 1：[" + statusData.getStatusValue() + "]");
            }
            // Battery Potential（SPN 158）
            int batteryPot = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 2));// Byte
            // 7-8
            if (batteryPot != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (batteryPot * 0.05) * 100);
                statusData.setTypes(LCStatusType.StatusType.batteryPot);
                vehicleAddition.addStatus(statusData.build());
                print("Battery Potential（SPN 158）：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf0CFE6CEE)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 2)) != CANDataEntity.BytesOf2) {
                statusData.setStatusValue(
                        (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 2)) * 100 * 0.125));
                statusData.setTypes(LCStatusType.StatusType.outPutRoateSpeed);
                vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("输出抽转速：[" + statusData.getStatusValue() + "]");
            }
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 2)) != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 2))
                        * 100 * 0.00390625));
                statusData.setTypes(LCStatusType.StatusType.tachographVehicleSpeed);
                vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("车速：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEE900)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) != CANDataEntity.BytesOf4) {
                long totalFuelConsumption = (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 100 * 0.5);
                statusData.setStatusValue(totalFuelConsumption);
                statusData.setTypes(LCStatusType.StatusType.totalFuelConsumption);
                AddStatus(statusData, vehicleAddition);
                addLastOilMileageCache(unimark, statusData);
                // vehicleAddition.addStatus(statusData);
                // obj.setRealTimeOilConsumption((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 2, 2))*100*0.001953125)); //
                print("燃油消耗总量：[" + statusData.getStatusValue() + "]");
            }
            // 每天燃油消耗 byte 1-4
            long dayFuelConsumption = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4));// Byte
            // 5-8
            if (dayFuelConsumption != CANDataEntity.BytesOf4) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (dayFuelConsumption * 0.5 * 100));
                statusData.setTypes(LCStatusType.StatusType.dayFuelConsumption);
                AddStatus(statusData, vehicleAddition);
                // vehicleAddition.addStatus(statusData.build());
                print("每天燃油消耗：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEE500)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long cumulativeRunningTime = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4));// Byte1-4
            if (cumulativeRunningTime != CANDataEntity.BytesOf4) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (cumulativeRunningTime * 100 * 0.05 * 3600));// 转化为秒
                statusData.setTypes(LCStatusType.StatusType.cumulativeRunningTime);
                vehicleAddition.addStatus(statusData);
                print("发动机累计运行时间：[" + statusData.getStatusValue() + "]");
            }
            long cumulativeTurningNumber = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4));// Byte5-8
            if (cumulativeTurningNumber != CANDataEntity.BytesOf4) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (cumulativeTurningNumber * 1000) * 100);
                statusData.setTypes(LCStatusType.StatusType.cumulativeTurningNumber);
                vehicleAddition.addStatus(statusData.build());
                print("发动机累计转数：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEEF00)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int oilPressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte4
            if (oilPressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (oilPressure * 100 * 4));// （4Kpa/bit）
                statusData.setTypes(LCStatusType.StatusType.oilPressure);
                vehicleAddition.addStatus(statusData);
                print("机油压力（4Kpa/bit）：[" + statusData.getStatusValue() + "]");
            }
            // 燃油压力
            int fuelPressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte1
            if (fuelPressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (fuelPressure * 100 * 4));// （4Kpa/bit）
                statusData.setTypes(LCStatusType.StatusType.fuelPressure);
                vehicleAddition.addStatus(statusData);
                print("燃油压力（4Kpa/bit）：[" + statusData.getStatusValue() + "]");
            }
            // Extended Crankcase Blow-by Pressure
            int extendCrankcase = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte1
            if (extendCrankcase != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (extendCrankcase * 100 * 0.4));// （4Kpa/bit）
                statusData.setTypes(LCStatusType.StatusType.extendCrankcase);
                vehicleAddition.addStatus(statusData);
                print("Extended Crankcase Blow-by Pressure（:0.4%/bit）：[" + statusData.getStatusValue() + "]");
            }

            // 机油液位（百分比 Byte 3
            int oilLevel = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));//
            if (oilLevel != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (oilLevel * 100 * 0.4));
                statusData.setTypes(LCStatusType.StatusType.oilLevel);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                }
                print("机油液位 Scale:0.4%/bit[" + statusData.getStatusValue() + "]");
            }
            // 冷却液液位（百分比 Byte 8
            int coolantLevel = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1));//
            if (coolantLevel != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (coolantLevel * 100 * 0.4));
                statusData.setTypes(LCStatusType.StatusType.coolantLevel);
                if (InRegion(0, 100, statusData.getStatusValue())) {
                    vehicleAddition.addStatus(statusData);
                }
                print("冷却液液位  Scale:0.4%/bit[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEF600)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int exhaustTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 2));// Byte
            // 3
            if (exhaustTem != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((exhaustTem * 0.03125 - 273) * 100));
                statusData.setTypes(LCStatusType.StatusType.exhaustTem);
                vehicleAddition.addStatus(statusData);
                print("排气温度：[" + statusData.getStatusValue() + "]");
            } else {
                print("排气温度异常：[" + exhaustTem + "]");
            }
            // 颗粒捕集器进气压力 byte 0
            int dpfPressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            // 0
            if (dpfPressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((dpfPressure * 0.5) * 100));
                statusData.setTypes(LCStatusType.StatusType.dpfPressure);
                vehicleAddition.addStatus(statusData);
                print("颗粒捕集器进气压力  0.5kPa/bit：[" + statusData.getStatusValue() + "]");
            }
            // 相对增压压力 byte 1
            int relativePressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            // 1
            if (relativePressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((relativePressure * 2) * 100));
                statusData.setTypes(LCStatusType.StatusType.relativePressure);
                vehicleAddition.addStatus(statusData);
                print("相对增压压力  :2kPa/bit：[" + statusData.getStatusValue() + "]");
            }
            // 进气温度 byte 2
            int intakeAirTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));// Byte
            // 2
            if (intakeAirTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((intakeAirTem - 40) * 100));
                statusData.setTypes(LCStatusType.StatusType.intakeAirTem);
                vehicleAddition.addStatus(statusData);
                print("进气温度  :Offset: 1℃/bit -40℃：[" + statusData.getStatusValue() + "]");
            }
            // 绝对增压压力 byte 3
            int absolutePressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte
            // 3
            if (absolutePressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (absolutePressure * 2 * 100));
                statusData.setTypes(LCStatusType.StatusType.absolutePressure);
                vehicleAddition.addStatus(statusData);
                print("绝对增压压力  :Offset: 2kPa/bit：[" + statusData.getStatusValue() + "]");
            }
            // Air filter 1 differential pressure/空滤器压差 byte 4
            int airFilterPre = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));// Byte
            // 3
            if (airFilterPre != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (airFilterPre * 100));
                statusData.setTypes(LCStatusType.StatusType.airFilterPre);
                vehicleAddition.addStatus(statusData);
                print("Air filter 1 differential pressure/空滤器压差  :Offset: 2kPa/bit：[" + statusData.getStatusValue() + "]");
            }
            // Coolant filter differential temperature/冷却器温差 byte 7
            int CoolDifferTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1));// Byte
            // 3
            if (CoolDifferTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (CoolDifferTem * 100));
                statusData.setTypes(LCStatusType.StatusType.CoolDifferTem);
                vehicleAddition.addStatus(statusData);
                print("Coolant filter differential temperature/冷却器温差：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEF500)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 大气压力 byte 0
            int atmosphericPressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte3
            if (atmosphericPressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (atmosphericPressure * 0.5 * 100));
                statusData.setTypes(LCStatusType.StatusType.atmosphericPressure);
                vehicleAddition.addStatus(statusData);
                print("大气压力  :0.5kPa/bit：[" + statusData.getStatusValue() + "]");
            }
            // 发动机舱内部温度 byte 1-2
            int engineTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 2));// Byte1-2
            if (engineTem != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (engineTem * 100));
                statusData.setTypes(LCStatusType.StatusType.engineTem);
                vehicleAddition.addStatus(statusData);
                print("发动机舱内部温度  :Not realized，Set 0xFFFF：[" + statusData.getStatusValue() + "]");
            }
            // 大气温度 byte 3-4
            int atmosphericTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 2));// Byte3-4
            // 3-4
            if (atmosphericTem != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((atmosphericTem * 0.03125 - 273) * 100));
                statusData.setTypes(LCStatusType.StatusType.atmosphericTem);
                vehicleAddition.addStatus(statusData);
                print("大气温度  :Scale:0.03125℃/bit Offset:-273℃：[" + statusData.getStatusValue() + "]");
            }
            // Air inlet temperature/进气温度 byte 3-4
            int AirInTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1));// Byte3-4
            if (AirInTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((AirInTem - 40) * 100));
                statusData.setTypes(LCStatusType.StatusType.AirInTem);
                vehicleAddition.addStatus(statusData);
                print("Air inlet temperature/进气温度 Scale:1℃/bit Offset:-40℃：[" + statusData.getStatusValue() + "]");
            }

            // 路面温度 byte 6-7
            int pavementTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 2));// Byte6-7
            if (pavementTem != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (pavementTem * 100));
                statusData.setTypes(LCStatusType.StatusType.pavementTem);
                vehicleAddition.addStatus(statusData);
                print("路面温度  :Not realized，Set 0xFFFF：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEE400)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 冷启动灯 byte 3
            int lampStatus = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte
            // 3

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 3 1-2bit 0x03 11
            statusData.setStatusValue((long) ((lampStatus & 0x03) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.lampStatus);
            vehicleAddition.addStatus(statusData);
            print("冷启动灯  :00:Lamp OFF 01:Lamp ON：[" + statusData.getStatusValue() + "]");

            //Engine protection system has shutdown engine
            // byte 5
            int engineProtection = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));// Byte
            // 3
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 3 1-2bit 0x03 11
            statusData.setStatusValue((long) ((engineProtection & 0x03) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.engineProtection);
            vehicleAddition.addStatus(statusData);
            print("Engine protection system has shutdown engine  :00:not 01:active：[" + statusData.getStatusValue() + "]");

        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FF0800)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // Relative Engine oil pressure byte 3
            int RelEngOilPressure = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            if (RelEngOilPressure != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 1
                statusData.setStatusValue((long) ((RelEngOilPressure * 4) * 100)); //
                statusData.setTypes(LCStatusType.StatusType.RelEngOilPressure);
                vehicleAddition.addStatus(statusData);
                print("Relative Engine oil pressure：[" + statusData.getStatusValue() + "]");
            }
            // Engine Oil Pressure Low
            // byte2
            int engOilPreLow = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            if (engOilPreLow != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 2 7-8bit 0xc0 11000000
                statusData.setStatusValue((long) (((engOilPreLow & 0xC0) >> 6) * 100)); //

                statusData.setTypes(LCStatusType.StatusType.engOilPreLow);
                vehicleAddition.addStatus(statusData);
                print("Engine Oil Pressure Low：[" + statusData.getStatusValue() + "]");
            }

            //Engine Over Coolant Temperature
            // byte2
            int engCoolTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            if (engCoolTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 2 5-6bit 0x30 110000
                statusData.setStatusValue((long) (((engCoolTem & 0x30) >> 4) * 100)); //

                statusData.setTypes(LCStatusType.StatusType.engCoolTem);
                vehicleAddition.addStatus(statusData);
                print("Engine Over Coolant Temperature：[" + statusData.getStatusValue() + "]");
            }
            //Cold Start Heater Status
            // byte2
            int StartHeartStat = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-8bit 0xF0 11110000
            statusData.setStatusValue((long) (((StartHeartStat & 0xF0) >> 4) * 100)); //

            statusData.setTypes(LCStatusType.StatusType.StartHeartStat);
            vehicleAddition.addStatus(statusData);
            print("Cold Start Heater Status：[" + statusData.getStatusValue() + "]");


            //OBD Lamp Status
            // byte2
            int OBDLampStatus = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-8bit 0xF0 11110000
            statusData.setStatusValue((long) (OBDLampStatus * 100)); //

            statusData.setTypes(LCStatusType.StatusType.OBDLampStatus);
            vehicleAddition.addStatus(statusData);
            print("OBD Lamp Status：[" + statusData.getStatusValue() + "]");


            //Exhaust flap valve output
            // byte2
            int ExOutput = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));// Byte
            if (ExOutput != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 2 5-8bit 0xF0 11110000
                statusData.setStatusValue((long) (ExOutput * 0.4 * 100)); //

                statusData.setTypes(LCStatusType.StatusType.ExOutput);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                } else {
                    PrintExceptionData("Exhaust flap valve output", 0, 100, statusData.getStatusValue());
                }
                print("Exhaust flap valve output：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEC117)) {
            // byte 4-7
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) != CANDataEntity.BytesOf4) {
                statusData.setStatusValue(
                        (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.005 * 100)); //
                statusData.setTypes(LCStatusType.StatusType.tripDistance);
                vehicleAddition.addStatus(statusData);
                // obj.setTripDistance((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 0, 4))*100*0.125)); //
                print("日里程：0.005 Km[" + statusData.getStatusValue() + "]");
            }
            // 总里程 byte 0-3
            if (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4)) != CANDataEntity.BytesOf4) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue(
                        (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4)) * 0.005 * 100)); //
                statusData.setTypes(LCStatusType.StatusType.mileage);
                AddStatus(statusData, vehicleAddition);
                // vehicleAddition.addStatus(statusData);
                // obj.setMileage(statusData.getStatusValue());
                // obj.setTripDistance((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
                // 0, 4))*100*0.125)); //
                print("总里程：0.005 Km[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEFF00)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 油中有水指示 waterInOilStatus byte 0
            int waterInOilStatus = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1)) & 0x03;// Byte0
            // 0
            // 3
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 3 1-2bit 0x03 11
            statusData.setStatusValue((long) (Math.abs(waterInOilStatus - 1) * 100));
            statusData.setTypes(LCStatusType.StatusType.waterInOilStatus);
            vehicleAddition.addStatus(statusData);
            print("油中有水指示  :（1表示正常，0表示异常）：[" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEFC21) || ArraysUtils.equals(canId, CANDataEntity.IdOf18FEFCFD)|| ArraysUtils.equals(canId, CANDataEntity.IdOf18FEFC17)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 车辆当前油量（百分比） byte 0
            int oilValue = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            // 0
            // 3
            if (oilValue != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 3 1-2bit 0x03 11
                statusData.setStatusValue((long) (oilValue * 0.4 * 100));
                statusData.setTypes(LCStatusType.StatusType.oilValue);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                    addLastOilMileageCache(unimark, statusData);
                }
                print("车辆当前油量（百分比） 0.4：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F00503)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 当前档位 byte 3
            int currentGear = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte
            if (currentGear != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // byte 3 1-2bit 0x03 11
                statusData.setStatusValue((long) ((currentGear - 125) * 100));
                statusData.setTypes(LCStatusType.StatusType.currentGear);
                if (InRegion(-125, 126, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                } else {
                    PrintExceptionData("当前档位;", -125, 126, statusData.getStatusValue() / 100);
                }
                print("当前档位：cale:1 gear/bit Offset:-125：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FE563D)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int ureaTankLiquidLevel = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));
            if (ureaTankLiquidLevel != CANDataEntity.BytesOf1) {
                statusData.setStatusValue(
                        (long) (ureaTankLiquidLevel * 100 * 0.4));
                statusData.setTypes(LCStatusType.StatusType.ureaTankLiquidLevel);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                }
                print("尿素液位：[" + statusData.getStatusValue() + "]");
            }
            // 当前档位 byte 2
            int ureaTankTem = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            if (ureaTankTem != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ureaTankTem * 100);
                statusData.setTypes(LCStatusType.StatusType.ureaTankTem);
                vehicleAddition.addStatus(statusData);
                print("尿素箱温度：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEBD00)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int EsFanSp = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            if (EsFanSp != CANDataEntity.BytesOf1) {
                statusData.setStatusValue((long) (EsFanSp * 0.4 * 100));
                statusData.setTypes(LCStatusType.StatusType.EsFanSp);
                vehicleAddition.addStatus(statusData);
                print("Estimated percent Fan speed：[" + statusData.getStatusValue() + "]");
            }
            // 当前档位 byte 2
            int FanDriState = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            if (FanDriState != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) FanDriState * 100);
                statusData.setTypes(LCStatusType.StatusType.FanDriState);
                vehicleAddition.addStatus(statusData);
                print("Fan Drive state：[" + statusData.getStatusValue() + "]");
            }
            // Fan speed当前档位 byte 2
            int FanSpeed = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 2));// Byte
            if (FanSpeed != CANDataEntity.BytesOf2) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (FanSpeed * 0.125 * 100));
                statusData.setTypes(LCStatusType.StatusType.FanSpeed);
                vehicleAddition.addStatus(statusData);
                print("FanSpeed：[" + statusData.getStatusValue() + "]");
            }
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18F0000F)) {
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            int retarTorMode = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 1));// Byte
            // byte 1 1-4bit 0x0F 1111
            statusData.setStatusValue((long) ((retarTorMode & 0x0F) * 100));
            statusData.setTypes(LCStatusType.StatusType.retarTorMode);
            vehicleAddition.addStatus(statusData);
            print("Retarder Torque Mode/缓速器扭矩模式：[" + statusData.getStatusValue() + "]");

            //Retarder Enable - Brake Assist Switch/刹车辅助开关
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 5-6bit 0xF0 00110000
            statusData.setStatusValue((long) (((retarTorMode & 0x30) >> 4) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.retBrakeAssSwitch);
            vehicleAddition.addStatus(statusData);
            print("Retarder Enable - Brake Assist Switch/刹车辅助开关：[" + statusData.getStatusValue() + "]");

            //Retarder Enable - Shift Assist Switch/换挡辅助开关
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // byte 2 7-8bit 0xC0 0011000000
            statusData.setStatusValue((long) (((retarTorMode & 0xC0) >> 6) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.retShiftAssSwitch);
            vehicleAddition.addStatus(statusData);
            print("Retarder Enable - Shift Assist Switch/换挡辅助开关：[" + statusData.getStatusValue() + "]");


            //Actual Retarder Torque Percentage//缓速器实际扭矩百分比
            // byte2
            int actRetTorPer = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 1, 1));// Byte
            if (actRetTorPer != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((actRetTorPer - 125) * 100)); //
                statusData.setTypes(LCStatusType.StatusType.actRetTorPer);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                    print("Actual Retarder Torque Percentage//缓速器实际扭矩百分比：[" + statusData.getStatusValue() + "]");
                }
            }
            //Intended Retarder percent Torque/缓速器需求扭矩
            // byte3
            int intRetPerTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 2, 1));// Byte
            if (intRetPerTor != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((intRetPerTor - 125) * 100)); //
                statusData.setTypes(LCStatusType.StatusType.intRetPerTor);
                if (InRegion(0, 100, statusData.getStatusValue() / 100)) {
                    vehicleAddition.addStatus(statusData);
                    print("Intended Retarder percent Torque/缓速器需求扭矩比：[" + statusData.getStatusValue() + "]");
                }
            }
            //Coolant Load Increase
            // byte4
            int coolLoadIncrease = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 3, 1));// Byte

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 1-2bit 0x03 0011
            statusData.setStatusValue((long) ((coolLoadIncrease & 0x03) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.coolLoadIncrease);
            vehicleAddition.addStatus(statusData);
            print("Coolant Load Increase：[" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            // 3-4bit 0x0C 001100
            statusData.setStatusValue((long) (((coolLoadIncrease & 0x0C) >> 2) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.retReqBrakeLight);
            vehicleAddition.addStatus(statusData);
            print("Retarder Requesting Brake Light/缓速器需求灯：[" + statusData.getStatusValue() + "]");


            //Source Address of controlling device for retarder			control/缓速器控制单元地址
            // byte5
            int addOfControDevice = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 1));// Byte
            if (addOfControDevice != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                // 1-2bit 0x03 0011
                statusData.setStatusValue((long) (addOfControDevice * 100)); //
                statusData.setTypes(LCStatusType.StatusType.addOfControDevice);
                vehicleAddition.addStatus(statusData);
                print("Source Address of controlling device for retarder control/缓速器控制单元地址：[" + statusData.getStatusValue() + "]");
            }
            //Driver’s Demand Retarder Percent Torque/缓速器需求扭矩百分比
            // byte6
            int demRetPerTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 1));// Byte
            if (demRetPerTor != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) ((demRetPerTor - 125) * 100)); //
                statusData.setTypes(LCStatusType.StatusType.demRetPerTor);
                vehicleAddition.addStatus(statusData);
                print("Driver’s Demand Retarder Percent Torque/缓速器需求扭矩百分比：[" + statusData.getStatusValue() + "]");
            }
            //Retarder Switch Percent Torque/缓速器开关扭矩百分比
            // byte7
            int retSwitchPerTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 6, 1));// Byte
            if (retSwitchPerTor != CANDataEntity.BytesOf1) {
                statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
                statusData.setStatusValue((long) (retSwitchPerTor * 100)); //
                statusData.setTypes(LCStatusType.StatusType.retSwitchPerTor);
                vehicleAddition.addStatus(statusData);
                print("Retarder Switch Percent Torque/缓速器开关扭矩百分比：[" + statusData.getStatusValue() + "]");
            }
            //Actual Maximum Available retarder Percent Torque/			实际最大缓速器扭矩百分比
            // byte8
            int actMaxAvailableRetPerTor = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 7, 1));// Byte
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((long) ((actMaxAvailableRetPerTor - 125) * 100)); //
            statusData.setTypes(LCStatusType.StatusType.actMaxAvailableRetPerTor);
            vehicleAddition.addStatus(statusData);
            print("Actual Maximum Available retarder Percent Torque/实际最大缓速器扭矩百分比：[" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEE017)) {
            // byte 0-3
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue(
                    (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4)) * 0.125 * 100)); //
            statusData.setTypes(LCStatusType.StatusType.TripDistanceDD);
            vehicleAddition.addStatus(statusData);
            // obj.setTripDistance((long)(Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent,
            // 0, 4))*100*0.125)); //
            print("小计里程（仪表）： km [" + statusData.getStatusValue() + "]");

            // byte 4-7
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            double mileage = Convert.byte2LongLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.125;
            if (mileage >= 5000000) {// 如果上传数值超过500万公里，肯定数据有 问题，直接将里程赋值为0.
                statusData.setStatusValue(0);
            } else {
                statusData.setStatusValue(
                        (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.125 * 100)); //
            }
            statusData.setTypes(LCStatusType.StatusType.mileageDD);
            vehicleAddition.addStatus(statusData);
            addLastOilMileageCache(unimark, statusData);
            print("总里程（仪表）： km [" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FEF2FD)) {
            //byte 0-3 积分里程
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long differentialMileage = (long) (Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 0, 4)) * 100 / 1000);
            statusData.setStatusValue(differentialMileage); //
            statusData.setTypes(LCStatusType.StatusType.differentialMileage);
            vehicleAddition.addStatus(statusData);

            // byte 4-8 积分油耗
            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            long integralFuelConsumption = (long) (Convert.byte2LongLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.5 * 100 / 1000);
            statusData.setStatusValue(integralFuelConsumption);
            statusData.setTypes(LCStatusType.StatusType.integralFuelConsumption);
            vehicleAddition.addStatus(statusData);
            addLastOilMileageCache(unimark, statusData);
            //缓存积分里程油耗,9102终端校对用
//            TerminalStateSync sync = syncCache.get(unimark);
            //缓存积分里程，老终端通过0705上传积分里程，但是传的是随机数，不能用，采用gps里程作为积分里程缓存起来
            //gps里程单位是m，需要换算成积分里程单位Km并且放大100倍
//            if(sync != null){
//                sync.setIntegralMileage(sync.getGpsMileage() / 10);
//                sync.setGpsDate(System.currentTimeMillis() / 1000);
//            }

            long ifc = (long) (Convert.byte2LongLittleEndian(ArraysUtils.subarrays(canContent, 4, 4)) * 0.5 * 10000 / 1000);
            //缓存积分油耗
//            if (integralFuelConsumption > 0) {
//                if (sync != null) {
//                    //如果积分油耗>0,则放到缓存里
//                    sync.setIntegralConsumption(ifc);
//                    //设置gps时间为当前时间，定时任务扫描近2分钟数据进redis
//                    sync.setGpsDate(System.currentTimeMillis() / 1000);
//                } else {
//                    sync = new TerminalStateSync();
//                    //如果积分油耗>0,则放到缓存里
//                    sync.setIntegralConsumption(ifc);
//                    sync.setGpsDate(System.currentTimeMillis() / 1000);
//                    syncCache.add(unimark, sync);
//                }
//
//                log.info("vin-->{}, sync_0705_data --> {}", unimark, JSON.toJSONString(sync));
//
//                TerminalStateSync tmp = syncCache.get(unimark);
//                log.info("tmp vin-->{}, sync_0705_data --> {}", unimark, JSON.toJSONString(tmp));
//            }

            print("瞬时油耗积分计算累计燃油消耗量： L [" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FFEB4E)) {

            int lng = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 3));

            // byte 4-9
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng >> 12) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng1Surplus);
            vehicleAddition.addStatus(statusData);

            print("LNG气瓶1剩余容量： L [" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng & 0xFFF) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng1Gross);
            vehicleAddition.addStatus(statusData);
            print("LNG气瓶1总容量： L [" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FFEB4F)) {

            int lng = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 3));

            // byte 4-9
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng >> 12) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng2Surplus);
            vehicleAddition.addStatus(statusData);

            print("LNG气瓶2剩余容量： L [" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng & 0xFFF) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng2Gross);
            vehicleAddition.addStatus(statusData);
            print("LNG气瓶2总容量： L [" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FFEB50)) {

            int lng = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 3));

            // byte 4-9
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng >> 12) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng3Surplus);
            vehicleAddition.addStatus(statusData);

            print("LNG气瓶3剩余容量： L [" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng & 0xFFF) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng3Gross);
            vehicleAddition.addStatus(statusData);
            print("LNG气瓶3总容量： L [" + statusData.getStatusValue() + "]");
        } else if (ArraysUtils.equals(canId, CANDataEntity.IdOf18FFEB51)) {

            int lng = Convert.byte2IntLittleEndian(ArraysUtils.subarrays(canContent, 5, 3));

            // byte 4-9
            VehicleStatusData.Builder statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng >> 12) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng4Surplus);
            vehicleAddition.addStatus(statusData);

            print("LNG气瓶4剩余容量： L [" + statusData.getStatusValue() + "]");

            statusData = LCVehicleStatusData.VehicleStatusData.newBuilder();
            statusData.setStatusValue((lng & 0xFFF) * 100); //
            statusData.setTypes(LCStatusType.StatusType.lng4Gross);
            vehicleAddition.addStatus(statusData);
            print("LNG气瓶4总容量： L [" + statusData.getStatusValue() + "]");
        }
    }

    public static void print(String message) {
        //log.error(message);
    }

    public static void PrintExceptionData(String message, long minValue, long maxValue, long currentValue) {
        log.info("数据异常  " + message + " 数据区间：[" + minValue + "," + maxValue + "]" + ",当前数值为:" + currentValue);
    }

    private static void AddStatus(VehicleStatusData.Builder statusData, VehicleStatusAddition.Builder statusAddition) {
        if (statusAddition != null) {
            int index = -1;
            for (int i = 0; i < statusAddition.getStatusList().size(); i++) {
                LCVehicleStatusData.VehicleStatusData statusDataTemp = statusAddition.getStatusList().get(i);
                if (statusData.getTypes().getNumber() == statusDataTemp.getTypes().getNumber()) {
                    index = i;
                    break;
                }
                // }
            }
            if (index == -1) {
                statusAddition.addStatus(statusData);
            }
        }
    }

    /**
     * 判断是否在合理区间内
     *
     * @param minValue
     * @param maxValue
     * @param currentValue
     * @return
     */
    private static boolean InRegion(long minValue, long maxValue, long currentValue) {
        if (currentValue >= minValue && currentValue <= maxValue) {
            return true;
        } else {
            return false;
        }
    }

    private void addLastOilMileageCache(String unimark, VehicleStatusData.Builder statusData) {
        if (statusData.getStatusValue() > 0) {
            Map<LCStatusType.StatusType, Long> map = lastMileageOilTypeCache.getLastMileageOilCache(unimark);
            if (map == null) {
                map = new ConcurrentHashMap<>();
            }
            map.put(statusData.getTypes(), statusData.getStatusValue());
            lastMileageOilTypeCache.addLastMileageOilCache(unimark, map);
        }
    }

}