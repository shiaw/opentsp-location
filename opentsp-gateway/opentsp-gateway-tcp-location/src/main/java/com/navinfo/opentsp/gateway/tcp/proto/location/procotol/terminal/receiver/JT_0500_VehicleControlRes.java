package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCShortLocationData;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 修伟 on 2017/9/20 0020.
 */
@LocationCommand(id = "0500")
public class JT_0500_VehicleControlRes extends TerminalCommand{
    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        byte[] location = packet.getContent();
        long alarm = Convert.byte2Long(ArraysUtils.subarrays(location, 0, 4), 4);
        long status = Convert.byte2Long(ArraysUtils.subarrays(location, 4, 4), 4);
        if ((status & 0x001000) == 0x001000){
            log.error("终端id{},车门控制状态：车门加锁",packet.getUniqueMark());
        }else{
            log.error("终端id{},车门控制状态：车门解锁",packet.getUniqueMark());
        }
        int latitude = Convert.byte2Int(ArraysUtils.subarrays(location, 8, 4), 4);
        int longitude = Convert.byte2Int(ArraysUtils.subarrays(location, 12, 4), 4);
        int height = Convert.byte2SignedInt(ArraysUtils.subarrays(location, 16, 2), 2);
        int speed = Convert.byte2Int(ArraysUtils.subarrays(location, 18, 2), 2);
        int direction = Convert.byte2Int(ArraysUtils.subarrays(location, 20, 2), 2);
        long currentTime = System.currentTimeMillis()/1000;
        long gpsTime = currentTime;
        String timestr = "20"+Convert.bytesToHexString(ArraysUtils.subarrays(location, 22, 6));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        try {
            gpsTime = sdf.parse(timestr).getTime()/1000;
        } catch (ParseException e) {
            gpsTime = System.currentTimeMillis()/1000;
        }
        LCShortLocationData.ShortLocationData.Builder builder = LCShortLocationData.ShortLocationData.newBuilder();
        builder.setStatus(status);
        builder.setOriginalLat(latitude);
        builder.setOriginalLng(longitude);
        builder.setLatitude(latitude);
        builder.setLongitude(longitude);
        builder.setHeight(height);
        builder.setSpeed(speed);
        builder.setDirection(direction);
        builder.setGpsDate(gpsTime);
        long serverTime = System.currentTimeMillis()/1000;
        builder.setReceiveDate(serverTime);
        builder.setIsPatch((serverTime - gpsTime) > 300 ? true : false);

        Packet _out_packet = new Packet();
        _out_packet.setCommand(LCAllCommands.AllCommands.Terminal.VehicleControlRes_VALUE);
        _out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
        _out_packet.setUniqueMark(packet.getUniqueMark());
        _out_packet.setSerialNumber(packet.getSerialNumber());
        _out_packet.setContent(builder.build().toByteArray());

        super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
        return null;
    }
}
