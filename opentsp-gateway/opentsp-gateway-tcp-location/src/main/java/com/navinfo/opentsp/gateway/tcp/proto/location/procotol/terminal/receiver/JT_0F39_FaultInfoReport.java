package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.navinfo.opentsp.common.messaging.KafkaCommand;
import com.navinfo.opentsp.common.messaging.transport.kafka.KafkaMessageChannel;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.TAMonitorCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCFaultInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangyd
 * @date 2017-10-26
 * @modified by chenjie 转存kafka
 */
@LocationCommand(id = "0F39")
public class JT_0F39_FaultInfoReport extends TerminalCommand {

    public static Logger logger = LoggerFactory.getLogger(JT_0F39_FaultInfoReport.class);

    private static final int FAULT_APPEAR = 1;

    private static final int FAULT_DISAPPEAR = 2;

    /**
     *分包缓存
     */
    private static Map<String, Packet[]> faultInfoCache = new HashMap();

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        PacketResult packetResult = new PacketResult();
        LCFaultInfo.FaultInfo.Builder lcFaultInfo = LCFaultInfo.FaultInfo.newBuilder();
        try {
            //分包
            byte[] content = new byte[]{};
            int serial = packet.getPacketSerial();
            int total = packet.getPacketTotal();
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                    LCResultCode.JTTerminal.FAILURE));
            log.info("total:[" + total + "],current:[" + serial + "].");
            if (serial > total) {
                log.error("分包数大于总包数，total:[" + total + "],current:[" + serial + "].");
                return packetResult;
            }
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                    LCResultCode.JTTerminal.SUCCESS));
            if (total != 0 || serial != 0) {
                Packet[] packets = faultInfoCache.get(packet.getUniqueMark());
                if (packets == null) {
                    packets = new Packet[total];
                }
                packets[serial - 1] = packet;
                faultInfoCache.put(packet.getUniqueMark(), packets);

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
            faultInfoCache.remove(packet.getUniqueMark());

            String vin = packet.getUniqueMark();

            LCFaultInfo.GpsLocationData.Builder gpsLocationData = LCFaultInfo.GpsLocationData.newBuilder();
//            0	时间	BCD[6]	YY-MM-DD-hh-mm-ss（GMT+8 时间，本标准中之后涉及的时间均采用此时区）
            String timeStr = "20" + Convert.bytesToHexString(ArraysUtils.subarrays(content, 0, 6));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            long time = 0L;
            try {
                time = sdf.parse(timeStr).getTime() / 1000;
            } catch (ParseException e) {
                time = System.currentTimeMillis() / 1000;
            }
            gpsLocationData.setGpsTime(time);

//            6	纬度	DWORD	以度为单位的纬度值乘以 10 的 6 次方，精确到百万分之一度
            int latitude = Convert.byte2Int(ArraysUtils.subarrays(content, 6, 4), 4);
            gpsLocationData.setLatitude(latitude);

//            10	经度	DWORD	以度为单位的经度值乘以 10 的 6 次方，精确到百万分之一度
            int longitude = Convert.byte2Int(ArraysUtils.subarrays(content, 10, 4), 4);
            gpsLocationData.setLongitude(longitude);

//            14	高程	WORD	海拔高度，单位为米（m）
            int height = Convert.byte2SignedInt(ArraysUtils.subarrays(content, 14, 2), 2);

//            16	方向	WORD	0-359，正北为 0，顺时针
            int direction = Convert.byte2Int(ArraysUtils.subarrays(content, 16, 2), 2);
            gpsLocationData.setDirection(direction);

//            18	速度	WORD	1/10km/h
            int speed = Convert.byte2Int(ArraysUtils.subarrays(content, 18, 2), 2) / 10;
            logger.info("0F39 vin:{}, time:{}, latitude:{}, longitude:{}, height:{}, direction:{}, speed:{}.", vin, time, latitude, longitude, height, direction, speed);
            gpsLocationData.setSpeed(speed);

            lcFaultInfo.setGpsLocationData(gpsLocationData);

            content = ArraysUtils.subarrays(content, 20);

            // 故障信息项列表
            while(content.length > 0) {
                LCFaultInfo.FaultListData.Builder faultListData = LCFaultInfo.FaultListData.newBuilder();
                int type = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 1), 1);
                faultListData.setFaultType(type);

                int length = Convert.byte2Int(ArraysUtils.subarrays(content, 1, 1), 1);
                if (type == FAULT_APPEAR) {
                    byte[] fault = ArraysUtils.subarrays(content, 2);
//                  0	车速	WORD	单位：Km/h
                    int speed1 = Convert.byte2Int(ArraysUtils.subarrays(fault, 0, 2), 2) * 100 / 256;
                    faultListData.setSubSpeed(speed1);

//                  2	油门	BYTE	单位：%
                    int accelerator = Convert.byte2Int(ArraysUtils.subarrays(fault, 2, 1), 1) * 100 * 4 / 10;
                    faultListData.setAccelerator(accelerator);

//                  3	制动信号	BYTE
                    int brakeSignal = (Convert.byte2Int(ArraysUtils.subarrays(fault, 3, 1), 1) & 3) * 100;
                    faultListData.setBrakeSignal(brakeSignal);

//                  4	发动机转速	WORD	单位：RPM
                    int engineSpeed = Convert.byte2Int(ArraysUtils.subarrays(fault, 4, 2), 2) * 100 * 125 / 1000;
                    faultListData.setRotation(engineSpeed);

//                  6	发动机涡轮增压压力	BYTE	单位：KPa
                    int engineTurbochargedPressure = Convert.byte2Int(ArraysUtils.subarrays(fault, 6, 1), 1) * 200;
                    faultListData.setEngineTurbochargedPressure(engineTurbochargedPressure);

//                  7	发动机进气压力	BYTE	单位：KPa
                    int engineIntakePressure = Convert.byte2Int(ArraysUtils.subarrays(fault, 7, 1), 1) * 200;
                    faultListData.setEngineIntakePressure(engineIntakePressure);

//                  8	发动机排气温度	WORD	单位：℃
                    int engineExhaustTemperature = Convert.byte2Int(ArraysUtils.subarrays(fault, 8, 2), 2) * 100 * 3125 / 100000;
                    faultListData.setEngineExhaustTemperature(engineExhaustTemperature);

//                  10	发动机水温	BYTE	单位：℃
                    int engineCoolantTemperature = (Convert.byte2Int(ArraysUtils.subarrays(fault, 10, 1), 1) - 40) * 100;
                    faultListData.setEngineCoolantTemperature(engineCoolantTemperature);

//                  11	油门变化率	WORD	单位：% TODO 单位换算
                    int accelerationRate = Convert.byte2Int(ArraysUtils.subarrays(fault, 11, 2), 2) * 100;
                    faultListData.setAccelerationRate(accelerationRate);

//                  13	挡位	BYTE
                    int gear = (Convert.byte2Int(ArraysUtils.subarrays(fault, 13, 1), 1) - 125) * 100;
                    faultListData.setGear(gear);

//                  14	发动机输出扭矩	BYTE	单位：%
                    int engineOutputTorque = (Convert.byte2Int(ArraysUtils.subarrays(fault, 14, 1), 1) - 125) * 100;
                    faultListData.setEngineOutputTorque(engineOutputTorque);

//                  15	载荷	DWORD
                    int load = Convert.byte2Int(ArraysUtils.subarrays(fault, 15, 4), 4) * 100;
                    faultListData.setLoad(load);

//                  19	发动机负荷	BYTE	单位：%
                    int engineLoad = Convert.byte2Int(ArraysUtils.subarrays(fault, 19, 1), 1) * 100;
                    faultListData.setEngineLoad(engineLoad);

//                  20	车辆加速度	WORD	单位：m/s
                    int vehicleAcceleration = Convert.byte2Int(ArraysUtils.subarrays(fault, 20, 2), 2);
                    faultListData.setVehicleAcceleration(vehicleAcceleration);

//                  22	车辆减速度	WORD	单位：m/s
                    byte[] vsr = ArraysUtils.subarrays(fault, 22, 2);
                    vsr[0] = (byte)(vsr[0] & 0x7F);
                    int vehicleSpeedReduction = - Convert.byte2SignedInt(vsr, 2);
                    faultListData.setVehicleSpeedReduction(vehicleSpeedReduction);

                    // 24	故障码列表
                    int count = Convert.byte2Int(ArraysUtils.subarrays(fault, 24, 1), 1);
                    faultListData.setFaultCodeCount(count);
                    byte[] faultCodes = ArraysUtils.subarrays(fault, 25);

                    // 解析故障码
                    parseFaultItem(faultListData, faultCodes, count);

                    lcFaultInfo.addFaultListData(faultListData);

                    content = ArraysUtils.subarrays(content, 0,2 + 24 + 1 + count * 4);

                    logger.info("0F39 fault appear vin:{}, type:{}, length:{}, speed1:{}, accelerator:{}, brakeSignal:{}, engineSpeed:{}, engineSpeed:{}, engineTurbochargedPressure:{}, engineIntakePressure:{}, engineExhaustTemperature:{}," +
                                    "engineCoolantTemperature:{}, accelerationRate:{}, gear:{}, engineOutputTorque:{}, load:{}, engineLoad:{}, vehicleAcceleration:{}, vehicleSpeedReduction:{}, count:{}, faultItem:{}.",
                            vin, type, length, speed1, accelerator, brakeSignal, engineSpeed, engineSpeed, engineTurbochargedPressure, engineIntakePressure, engineExhaustTemperature,
                            engineCoolantTemperature, accelerationRate, gear, engineOutputTorque, load, engineLoad, vehicleAcceleration, vehicleSpeedReduction, count, faultListData.toString());

                } else if (type == FAULT_DISAPPEAR) {
                    // 24	故障码列表
                    int count = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 1), 1);
                    byte[] faultCodes = ArraysUtils.subarrays(content, 3);

                    //解析故障码
                    parseFaultItem(faultListData, faultCodes, count);
                    lcFaultInfo.addFaultListData(faultListData);

                    content = ArraysUtils.subarrays(content, 0,2 + 1 + count * 4);

                    logger.info("0F39 fault disappear vin:{}, faultItem:{}.", vin, faultListData.getFaultItemList());
                }
            }

            KafkaCommand kafkaCommand = new KafkaCommand();
            kafkaCommand.setMessage(lcFaultInfo.build().toByteArray());
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(TopicConstants.FAULT_INFO_REPORT);
            kafkaCommand.setKey(packet.getUniqueMark());
            kafkaCommand.setSerialNumber(time);

            long startTime = System.currentTimeMillis();
            kafkaMessageChannel.send(kafkaCommand);
            //记录kafka监控信息
            TAMonitorCache.addKafkaTime(System.currentTimeMillis() - startTime);
            TAMonitorCache.addKafkaCount();

            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS));
            return packetResult;
        } catch (Exception e) {
            logger.error("0F39 parse and save to kafka error:",e);
            e.printStackTrace();
        }

        packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.FAILURE));
        return packetResult;
    }

    /**
     * 解析故障项
     * @param faultListData  故障列表list
     * @param faultCode     故障码byte数组
     * @param faultCount    故障个数
     * */
    private void parseFaultItem(LCFaultInfo.FaultListData.Builder faultListData,byte [] faultCode,int faultCount){
        for (int i = 0; i < faultCount; i++) {
            byte [] faultItems = ArraysUtils.subarrays(faultCode, 4 * i, 4);
            LCFaultInfo.FaultItem.Builder faultItem = LCFaultInfo.FaultItem.newBuilder();
            //源地址，故障码属于哪个系统
            int faultAddr = Convert.byte2Int(ArraysUtils.subarrays(faultItems,0,1),1);
            faultItem.setFaultAddr(faultAddr);

            //SPN第二字节
            int spn1 = Convert.byte2Int(ArraysUtils.subarrays(faultItems,1,1),1);

            //SPN第三字节
            int spn2 = Convert.byte2Int(ArraysUtils.subarrays(faultItems,2,1),1);

            //第四字节
            int fourthByte = Convert.byte2Int(ArraysUtils.subarrays(faultItems,3,1),1);

            //SPN MSB   bit8-bit6
            int spn_msb = (fourthByte >> 5) & 7;
            faultItem.setSPN(spn1 + (spn2 << 8) + (spn_msb << 16));

            //FMI   bit5-bit1
            faultItem.setFMI(fourthByte & 31);

            faultListData.addFaultItem(faultItem);
        }
    }

}
