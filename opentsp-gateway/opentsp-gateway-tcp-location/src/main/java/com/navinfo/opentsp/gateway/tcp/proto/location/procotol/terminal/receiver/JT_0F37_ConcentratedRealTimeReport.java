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
import com.navinfo.opentsp.platform.location.protocol.common.LCConcentratedRealTimeData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: ChenJie
 * @Description:密集采集 实时数据上报
 * @Date 2017/10/16
 * @Modified by:
 */
@LocationCommand(id = "0F37")
public class JT_0F37_ConcentratedRealTimeReport extends TerminalCommand {
    //密集上报最小包长
    @Value("${opentsp.concentrated.report.min.length:30}")
    private int concentratedMinLength;

    @Value("${opentsp.kafka.producer.concentrated.topic:concentratedreport}")
    private String topic;

    @Value("${opentsp.concentrated.handle.switch:true}")
    private boolean handleSwitch;

    @Autowired
    private KafkaMessageChannel kafkaMessageChannel;

    private static final int DATA_UNIT_LENGTH = 13;

    private static Map<String, Packet[]> concentratedCache = new HashMap<String, Packet[]>();


    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        // 根据开关判断是否处理密集采集上报数据
        if (!handleSwitch) {
            return null;
        }

        PacketResult packetResult = new PacketResult();
        try{
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
                Packet[] packets = concentratedCache.get(packet.getUniqueMark());
                if (packets == null) {
                    packets = new Packet[total];
                    concentratedCache.put(packet.getUniqueMark(), packets);
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
            concentratedCache.remove(packet.getUniqueMark());

            if (content == null || content.length == 0) {
                log.error("原始数据：[" + Convert.bytesToHexString(content) + "].");
                return packetResult;
            }


            //格式 消息块1消息块2消息块3消息块4...
            int packetLength = content.length;
            //不符合最小包长
            if(packetLength <concentratedMinLength){
                packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                        LCResultCode.JTTerminal.FAILURE));
                return packetResult;
            }
            else{
                byte [] tmp = ArraysUtils.subarrays(content,0);
                //实时消息块长度
                int blockLength = 0;
                //剩余数组长度符合最小包长 则进行循环截取消息块
                while(tmp.length >= concentratedMinLength){
                    LCConcentratedRealTimeData.SpecialRealTimeData.Builder concentratedRealTimeData = LCConcentratedRealTimeData.SpecialRealTimeData.newBuilder();
                    LCConcentratedRealTimeData.GpsLocationData.Builder gpsLocationData = LCConcentratedRealTimeData.GpsLocationData.newBuilder();
                    String timeStr = "20"+Convert.bytesToHexString(ArraysUtils.subarrays(tmp,0,6));
                    int latitude = Convert.byte2Int(ArraysUtils.subarrays(tmp, 6, 4), 4);
                    int longitude = Convert.byte2Int(ArraysUtils.subarrays(tmp, 10, 4), 4);
                    int height = Convert.byte2SignedInt(ArraysUtils.subarrays(tmp,14,2),2);
                    long gpsTime = 0L;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                    try {
                        gpsTime = sdf.parse(timeStr).getTime() / 1000;
                    } catch (ParseException e) {
                        gpsTime = System.currentTimeMillis() / 1000;
                    }
                    //设置gps位置数据
                    gpsLocationData.setGpsTime(gpsTime);
                    gpsLocationData.setLatitude(latitude);
                    gpsLocationData.setLongitude(longitude);
                    gpsLocationData.setHeight(height);
                    concentratedRealTimeData.setGpsLocationData(gpsLocationData);

                    //实时数据单元个数
                    int dataCount = Convert.byte2Int(ArraysUtils.subarrays(tmp,16,1),1);
                    if(dataCount <= 0){
                        tmp = ArraysUtils.subarrays(tmp,17);
                        continue;
                    }
                    // 17为 gps位置数据 + 实时数据单元 长度
                    blockLength = 17 + DATA_UNIT_LENGTH * dataCount;
                    // 判断包长度是否够截取1个消息块
                    if(tmp.length >= blockLength) {
                        byte[] dataUnit = ArraysUtils.subarrays(tmp, 17, DATA_UNIT_LENGTH * dataCount);
                        getRealTimeDataUnit(dataUnit,dataCount,concentratedRealTimeData);
                        concentratedRealTimeData.setDataUnitCount(dataCount);

                        //发送消息块到kafka
                        KafkaCommand kafkaCommand = new KafkaCommand();
                        kafkaCommand.setMessage(concentratedRealTimeData.build().toByteArray());
                        kafkaCommand.setCommandId(packet.getCommandForHex());
                        kafkaCommand.setTopic(TopicConstants.CONCENTRATED_REPORT);
                        kafkaCommand.setKey(packet.getUniqueMark());
                        //流水号作为gps时间
                        kafkaCommand.setSerialNumber(gpsTime);
                        long startTime = System.currentTimeMillis();
                        kafkaMessageChannel.send(kafkaCommand);
                        //记录kafka监控信息
                        TAMonitorCache.addKafkaTime(System.currentTimeMillis() - startTime);
                        TAMonitorCache.addKafkaCount();
                        //把已解析的包截掉
                        tmp = ArraysUtils.subarrays(tmp,blockLength);
                    }
                    else{
                        break;
                    }
                }

                packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                        LCResultCode.JTTerminal.SUCCESS));
                return packetResult;
            }

        }
        catch(Exception e){
            log.error("解析密集采集数据异常，原始报文是:["+Convert.bytesToHexString(packet.getOriginalPacket()) + "].", e);
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(),
                    LCResultCode.JTTerminal.FAILURE));
            return packetResult;
        }
    }

    /**
     * 拼密集采集实时数据单元内容
     *  @param dataUnit 数据单元
     *  @param dataCount 数据单元个数
     *  @param concentratedRealTimeData 密集采集数据
     * */
    private void getRealTimeDataUnit(byte[] dataUnit,int dataCount, LCConcentratedRealTimeData.SpecialRealTimeData.Builder concentratedRealTimeData) {
        if(dataUnit.length >= DATA_UNIT_LENGTH){
            int index = 0;
            for(int i=0; i<dataCount; i++){
                LCConcentratedRealTimeData.RealTimeDataUnit.Builder realTimeDataUnit = LCConcentratedRealTimeData.RealTimeDataUnit.newBuilder();
                index = DATA_UNIT_LENGTH * i;
                //发动机输出扭矩
                int engineOutputTorque = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index,1),1);
                realTimeDataUnit.setEngineOutputTorque((engineOutputTorque-125) * 100);
                //综合车速
                int speed = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+1,2),2);
                realTimeDataUnit.setSpeed(speed * 100 / 256);
                //油门
                int accelerator = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+3,1),1);
                realTimeDataUnit.setAccelerator(accelerator * 100 * 4 / 10);
                //制动信号
                int brake = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+4,1),1);
                realTimeDataUnit.setBrake((brake & 3) * 100);
                //发动机转速
                int rotation = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+5,2),2);
                realTimeDataUnit.setRotation(rotation * 100 * 125 / 1000);
                //挡位
                int gear = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+7,1),1);
                realTimeDataUnit.setGear((gear - 125) * 100);
                //离合器开关
                int clutchSwitch = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+8,1),1);
                realTimeDataUnit.setClutchSwitch((clutchSwitch & 3) * 100);
                //发动机瞬时油耗
                int realTimeOilConsumption = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+9,2),2);
                realTimeDataUnit.setRealTimeOilConsumption(realTimeOilConsumption * 100 / 512);
                //发动机燃油消耗率
                int fuelConsumptionRate = Convert.byte2Int(ArraysUtils.subarrays(dataUnit,index+11,2),2);
                realTimeDataUnit.setFuelConsumptionRate(fuelConsumptionRate * 5);
                concentratedRealTimeData.addRealTimeDataUnit(realTimeDataUnit);
            }

        }
    }
}
