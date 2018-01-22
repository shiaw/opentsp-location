package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.alibaba.fastjson.JSON;
import com.fasterxml.uuid.Generators;
import com.fasterxml.uuid.impl.TimeBasedGenerator;
import com.navinfo.opentsp.common.messaging.MessageChannel;
import com.navinfo.opentsp.common.messaging.ResultCode;
import com.navinfo.opentsp.gateway.api.ConnectionEventHub;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.*;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalMileageOilTypeEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.TerminalStateSync;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.redis.IRedisService;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.StringUtils;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.auth.TerminalAuthCommand;
import com.navinfo.opentsp.platform.configuration.OpentspQueues;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCLocationData.LocationData;
import com.navinfo.opentsp.platform.location.protocol.common.LCStatusType;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalAuth.TerminalAuth;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalOnlineSwitch;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author: qingqi
 * */
@LocationCommand(id = "0102")
public class JT_0102_TerminalAuth extends TerminalCommand {


    private final ConnectionEventHub connectionEventHub;
    private final MessageChannel messageChannel;
    private final String queueName;
    private final TimeBasedGenerator uuidGenerator = Generators.timeBasedGenerator();
    private final TerminalMileageOilTypeCache terminalMileageOilTypeCache;
    @Autowired
    LastMileageOilTypeCache lastMileageOilTypeCache;
//    @Autowired
//    TerminalSatusSyncCache syncCache;

    @Autowired
    private IRedisService redisService;

    @Autowired
    public JT_0102_TerminalAuth(TerminalMileageOilTypeCache terminalMileageOilTypeCache, ConnectionEventHub connectionEventHub, MessageChannel messageChannel, @Qualifier(OpentspQueues.PERSONAL) Queue queue) {
        this.terminalMileageOilTypeCache = terminalMileageOilTypeCache;
        this.connectionEventHub = connectionEventHub;
        this.messageChannel = messageChannel;
        this.queueName = queue.getName();
    }

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        String authCode = StringUtils.bytesToGbkString(packet.getContent());
        log.info("收到终端[" + packet.getUniqueMark() + "]鉴权,鉴权码：" + authCode);
        lastMileageOilTypeCache.delLastMileageOilCache(packet.getUniqueMark());
        PacketResult packetResult = new PacketResult();
        Map pushArguments = packet.getPushArguments();
        //判断是否是回写终端,pushArguments为空则是发送给Push模块；
        if (pushArguments == null) {
            final String id = connection.getId();
            connection.setDevice(packet.getUniqueMark());
            String sendId = uuidGenerator.generate().toString();
            TerminalAuthCommand command = new TerminalAuthCommand();
            command.setAuthCode(authCode);
            command.setDeviceId(packet.getUniqueMark());
            command.setCmd(packet.getCommandForHex());
            command.setReturnAddress(queueName);
            command.setSendId(sendId);
            command.setAuth(true);
            PacketArguments packetArguments = new PacketArguments();
            packetArguments.setSerialNumber(packet.getSerialNumber());
            PacketCache.getInstance().add(sendId, packetArguments);
            messageChannel.send(command);
            return null;
        }

        try {
            if (packet.getResultCode() == ResultCode.OK.code()) {
                authCode = (String) pushArguments.get("authCode");
                //2017-6-19 wanliang add 获取里程油耗类型代码
                Object mileageType = pushArguments.get("mileageType");
                Object oilType = pushArguments.get("oilType");
                log.info("{}获取里程油耗类型mileageType:{},oilType:{}", packet.getUniqueMark(), mileageType, oilType);
                if (mileageType != null && oilType != null) {
                    TerminalMileageOilTypeEntry terminalMileageOilTypeEntry = terminalMileageOilTypeCache.get(packet.getUniqueMark());
                    if (terminalMileageOilTypeEntry == null) {
                        terminalMileageOilTypeEntry = new TerminalMileageOilTypeEntry();
                    }
                    terminalMileageOilTypeEntry.setMileageType((Integer) mileageType);
                    terminalMileageOilTypeEntry.setOilType((Integer) oilType);
                    terminalMileageOilTypeCache.add(packet.getUniqueMark(), terminalMileageOilTypeEntry);
                }
                connection.setAuthCode(authCode);
                Packet outPacket = new Packet();
                TerminalAuth.Builder builder = TerminalAuth.newBuilder();
                builder.setAuthCoding(authCode);
                outPacket.setCommand(AllCommands.Terminal.TerminalAuth_VALUE);
                outPacket.setProtocol(LCConstant.LCMessageType.TERMINAL);
                outPacket.setUniqueMark(packet.getUniqueMark());
                outPacket.setSerialNumber(packet.getSerialNumber());
                outPacket.setContent(builder.build().toByteArray());
                connection.setDevice(packet.getUniqueMark());
                log.error("终端[" + packet.getUniqueMark() + "] 鉴权码：" + authCode + " 缓存鉴权码：" + authCode + "  鉴权成功!");

                Packet pk = new Packet(true);
                pk.setCommand(AllCommands.Terminal.TerminalOnlineSwitch_VALUE);
                pk.setProtocol(LCConstant.LCMessageType.TERMINAL);
                pk.setFrom(0L);
                pk.setUniqueMark(packet.getUniqueMark());
                LCTerminalOnlineSwitch.TerminalOnlineSwitch.Builder builder1 = LCTerminalOnlineSwitch.TerminalOnlineSwitch.newBuilder();
                builder1.setSwitchDate(System.currentTimeMillis() / 1000);
                builder1.setStatus(true);
                pk.setContent(builder1.build().toByteArray());
                packetResult.setKafkaPacket(pk);
                packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0102, LCResultCode.JTTerminal.SUCCESS));
                buildPacket(packetResult,packet.getUniqueMark());
                return packetResult;
            } else {
                log.error("终端[" + packet.getUniqueMark() + "] 鉴权码：" + authCode + " 缓存鉴权码：" + authCode + "  鉴权失败!");
                packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0102, LCResultCode.JTTerminal.FAILURE));
                return packetResult;
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("终端[" + packet.getUniqueMark() + "] 终端缓存terminalInfo =  null 鉴权码：" + authCode + " 鉴权失败!");
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0102, LCResultCode.JTTerminal.FAILURE));
            return packetResult;
        }
    }

    /**
     * 有效末次位置点
     */
    private static final String LASTEST_LOCATION_DATA_ALL = "LASTEST_LOCATION_DATA_ALL";

    private void buildPacket(PacketResult packetResult, String uniqueMark){
        Packet packet = new Packet(true,18);
        //0x9102
        packet.setCommand(37122);
        packet.setUniqueMark(uniqueMark);
        packet.appendContent(Convert.hexStringToBytes(gpsDate()));

        LocationData locationData = redisService.getHashValue(LASTEST_LOCATION_DATA_ALL, uniqueMark, LocationData.class);

        if(locationData == null){
            //GPS里程 (1/10Km)   原单位m
            packet.appendContent(Convert.longTobytes(0, 4));
            //积分里程 (m) 原单位km*100
            packet.appendContent(Convert.longTobytes(0, 4));
            //积分油耗 (ml) 原单位L*100
            packet.appendContent(Convert.longTobytes(0, 4));
        }else {
            //GPS里程 (1/10Km)   原单位m
            packet.appendContent(Convert.longTobytes(locationData.getMileage() / 100, 4));
            log.info("uniqueMark({}) 9102 mileage data-->{}", uniqueMark, locationData.getMileage() / 100);

            //积分里程 (m) 原单位km*100
            LCLocationData.VehicleStatusAddition vehicleStatusAddition = locationData.getStatusAddition();
            boolean flag1 = false;
            boolean flag2 = false;
            for(int i=0;i<vehicleStatusAddition.getStatusList().size();i++){
                if(vehicleStatusAddition.getStatus(i).getTypes() == LCStatusType.StatusType.differentialMileage){
                    //积分里程 (m) 原单位km*100
                    packet.appendContent(Convert.longTobytes(vehicleStatusAddition.getStatus(i).getStatusValue() / 100, 4));
                    log.info("uniqueMark({}) 9102 differentialMileage data-->{}", uniqueMark, vehicleStatusAddition.getStatus(i).getStatusValue() / 100);
                    flag1 = true;
                    continue;
                }

                if(vehicleStatusAddition.getStatus(i).getTypes() == LCStatusType.StatusType.integralFuelConsumption){
                    //积分油耗 (ml) 原单位L*100
                    packet.appendContent(Convert.longTobytes(vehicleStatusAddition.getStatus(i).getStatusValue() * 2 * 1000 / 100, 4));
                    log.info("uniqueMark({}) 9102 integralFuelConsumption data-->{}", uniqueMark, vehicleStatusAddition.getStatus(i).getStatusValue() * 2 * 1000 / 100);
                    flag2 = true;
                    continue;
                }

                if(flag1 && flag2){
                    break;
                }
            }
        }

        packetResult.setAnswerPacket(packet);
    }
    private String gpsDate(){
        long time = System.currentTimeMillis();
        Date d = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyMMddHHmmss");
        return sdf.format(d);
    }
}
