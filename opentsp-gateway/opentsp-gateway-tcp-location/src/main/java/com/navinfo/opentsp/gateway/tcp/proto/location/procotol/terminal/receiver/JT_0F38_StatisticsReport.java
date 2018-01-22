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
import com.navinfo.opentsp.platform.location.protocol.common.LCTerminalStatisticData;
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
 */
@LocationCommand(id = "0F38")
public class JT_0F38_StatisticsReport extends TerminalCommand {

    public static Logger logger = LoggerFactory.getLogger(JT_0F38_StatisticsReport.class);

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    /**
     *分包缓存
     */
    private static Map<String, Packet[]> statisticCache = new HashMap();


    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        PacketResult packetResult = new PacketResult();
        LCTerminalStatisticData.StatisticData.Builder lcTerminalStatisticData = LCTerminalStatisticData.StatisticData.newBuilder();
        try {
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
                Packet[] packets = statisticCache.get(packet.getUniqueMark());
                if (packets == null) {
                    packets = new Packet[total];
                }
                packets[serial - 1] = packet;
                statisticCache.put(packet.getUniqueMark(), packets);

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
            statisticCache.remove(packet.getUniqueMark());

            String vin = packet.getUniqueMark();

//            0	时间	BCD[6]	YY-MM-DD-hh-mm-ss（GMT+8 时间，本标准中之后涉及的时间均采用此时区）
            String timeStr = "20" + Convert.bytesToHexString(ArraysUtils.subarrays(content, 0, 6));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            long time = 0L;
            try {
                time = sdf.parse(timeStr).getTime() / 1000;
            } catch (ParseException e) {
                time = System.currentTimeMillis() / 1000;
            }

            lcTerminalStatisticData.setTime(time);

//            6	纬度	DWORD	以度为单位的纬度值乘以 10 的 6 次方，精确到百万分之一度
            int latitude = Convert.byte2Int(ArraysUtils.subarrays(content, 6, 4), 4);
            lcTerminalStatisticData.setLatitude(latitude);

//            10	经度	DWORD	以度为单位的经度值乘以 10 的 6 次方，精确到百万分之一度
            int longitude = Convert.byte2Int(ArraysUtils.subarrays(content, 10, 4), 4);
            lcTerminalStatisticData.setLongitude(longitude);

//            14	高程	WORD	海拔高度，单位为米（m）
            int height = Convert.byte2SignedInt(ArraysUtils.subarrays(content, 14, 2), 2);
            lcTerminalStatisticData.setHeight(height);

//            16	方向	WORD	0-359，正北为 0，顺时针
            int direction = Convert.byte2Int(ArraysUtils.subarrays(content, 16, 2), 2);
            lcTerminalStatisticData.setDirection(direction);

//            18	速度	WORD	1/10km/h
            int speed = Convert.byte2Int(ArraysUtils.subarrays(content, 18, 2), 2) / 10;
            logger.info("vin:{}, time:{}, latitude:{}, longitude:{}, height:{}, direction:{}, speed:{}.", vin, time, latitude, longitude, height, direction, speed);
            lcTerminalStatisticData.setSpeed(speed);

            byte[] stat = ArraysUtils.subarrays(content, 20);
//            0	车辆行程开始时间	DWORD	单位：s 从1970-01-01 00:00:00开始所经过的秒数
            int routeStartTime = Convert.byte2Int(ArraysUtils.subarrays(stat, 0, 4), 4);
            lcTerminalStatisticData.setRouteStartTime(routeStartTime);

//            4	车辆行程结束时间	DWORD	单位：s 从1970年1月1日开始所经过的秒数
            int routeEndTime = Convert.byte2Int(ArraysUtils.subarrays(stat, 4, 4), 4);
            lcTerminalStatisticData.setRouteEndTime(routeEndTime);

//            8	驾驶循环总里程	DWORD	单位：m
            int driveCycleMileage = Convert.byte2Int(ArraysUtils.subarrays(stat, 8, 4), 4) * 100;
            lcTerminalStatisticData.setDriveCycleMileage(driveCycleMileage);

//            12	空挡滑行里程	DWORD	单位：m 空挡滑行报警开始计算
            int idlingMileage = Convert.byte2Int(ArraysUtils.subarrays(stat, 12, 4), 4) * 100;
            lcTerminalStatisticData.setIdlingMileage(idlingMileage);

//            16	在挡滑行里程	DWORD	单位：m
            int paringRangeMileage = Convert.byte2Int(ArraysUtils.subarrays(stat, 16, 4), 4) * 100;
            lcTerminalStatisticData.setParingRangeMileage(paringRangeMileage);

//            20	停车怠速时间	DWORD	单位：s
            int parkingIdleTime = Convert.byte2Int(ArraysUtils.subarrays(stat, 20, 4), 4) * 100;
            lcTerminalStatisticData.setParkingIdleTime(parkingIdleTime);

//            24	制动次数	WORD
            int brakeNumber = Convert.byte2Int(ArraysUtils.subarrays(stat, 24, 2), 2) * 100;
            lcTerminalStatisticData.setBrakeNumber(brakeNumber);

//            26	制动累计里程	DWORD	单位：m
            int cumulativeMileage = Convert.byte2Int(ArraysUtils.subarrays(stat, 26, 4), 4) * 100;
            lcTerminalStatisticData.setCumulativeMileage(cumulativeMileage);

//            30	制动时长	DWORD	单位：s
            int brakingTime = Convert.byte2Int(ArraysUtils.subarrays(stat, 30, 4), 4) * 100;
            lcTerminalStatisticData.setBrakingTime(brakingTime);

//            34	急加速次数	WORD	行程内的累加值
            int accelerationFrequency = Convert.byte2Int(ArraysUtils.subarrays(stat, 34, 2), 2) * 100;
            lcTerminalStatisticData.setAccelerationFrequency(accelerationFrequency);

//            36	急减速次数	WORD	行程内的累加值
            int decelerationFrequency = Convert.byte2Int(ArraysUtils.subarrays(stat, 36, 2), 2) * 100;
            lcTerminalStatisticData.setDecelerationFrequency(decelerationFrequency);

//            38	整车估算载荷	DWORD	单位：Kg
            int vehicleEstimatedLoad = Convert.byte2Int(ArraysUtils.subarrays(stat, 38, 4), 4) * 100;
            lcTerminalStatisticData.setVehicleEstimatedLoad(vehicleEstimatedLoad);

//            42	起始总油耗	DWORD	单位：L
            int initialTotalFuelConsumption = Convert.byte2Int(ArraysUtils.subarrays(stat, 42, 4), 4) * 100;
            lcTerminalStatisticData.setInitialTotalFuelConsumption(initialTotalFuelConsumption);

//            46	终止总油耗	DWORD	单位：L
            int terminationTotalFuelConsumption = Convert.byte2Int(ArraysUtils.subarrays(stat, 46, 4), 4) * 100;
            lcTerminalStatisticData.setTerminationTotalFuelConsumption(terminationTotalFuelConsumption);

//            50	巡航里程	DWORD	单位：m
            int cruiseRange = Convert.byte2Int(ArraysUtils.subarrays(stat, 50, 4), 4) * 100;
            lcTerminalStatisticData.setCruiseRange(cruiseRange);

//            54	区间平均车速行驶时间	DWORD	单位：s 暂时填0
            int averageSpeedInterval = Convert.byte2Int(ArraysUtils.subarrays(stat, 54, 4), 4) * 100;
            lcTerminalStatisticData.setAverageSpeedInterval(averageSpeedInterval);

//            58	超速行驶次数	WORD
            int overSpeedTimes = Convert.byte2Int(ArraysUtils.subarrays(stat, 58, 2), 2) * 100;
            lcTerminalStatisticData.setOverSpeedTimes(overSpeedTimes);
            logger.info("vin:{}, routeStartTime:{}, routeEndTime:{}, driveCycleMileage:{}, idlingMileage:{}, paringRangeMileage:{}, parkingIdleTime:{}, brakeNumber:{}, cumulativeMileage:{},brakingTime:{}, accelerationFrequency:{}, " +
                            "decelerationFrequency:{}, vehicleEstimatedLoad:{}, initialTotalFuelConsumption:{}, terminationTotalFuelConsumption:{}, cruiseRange:{}, averageSpeedInterval:{}, overSpeedTimes:{}.",
                    vin, routeStartTime, routeEndTime, driveCycleMileage, idlingMileage, paringRangeMileage, parkingIdleTime, brakeNumber, cumulativeMileage, brakingTime, accelerationFrequency,
                    decelerationFrequency, vehicleEstimatedLoad, initialTotalFuelConsumption, terminationTotalFuelConsumption, cruiseRange, averageSpeedInterval, overSpeedTimes);

            //发送消息块到kafka
            KafkaCommand kafkaCommand = new KafkaCommand();
            kafkaCommand.setMessage(lcTerminalStatisticData.build().toByteArray());
            kafkaCommand.setCommandId(packet.getCommandForHex());
            kafkaCommand.setTopic(TopicConstants.STATISTIC_REPORT);
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
            logger.error("0F38 statistic report data parse error:",e);
        }

        return null;
    }
}
