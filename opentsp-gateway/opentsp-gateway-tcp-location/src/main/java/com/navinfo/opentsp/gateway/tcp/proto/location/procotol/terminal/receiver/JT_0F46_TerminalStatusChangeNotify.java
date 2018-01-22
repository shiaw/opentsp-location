package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTerminalStatusChangeNotify;

import java.text.ParseException;
import java.text.SimpleDateFormat;

@LocationCommand(id = "0F46")
public class JT_0F46_TerminalStatusChangeNotify extends TerminalCommand {

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        try {
            byte[] content = packet.getContent();
            long currentTime = System.currentTimeMillis() / 1000;
            long gpsdate = currentTime;
            String timestr = "20" + Convert.bytesToHexString(ArraysUtils.subarrays(content, 0, 6));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            try {
                gpsdate = sdf.parse(timestr).getTime() / 1000;
            } catch (ParseException e) {
                gpsdate = System.currentTimeMillis() / 1000;
            }
            int pattern = Convert.byte2Int(ArraysUtils.subarrays(content, 6, 1), 1);
            int sign = Convert.byte2Int(ArraysUtils.subarrays(content, 7, 1), 1);
            int gpsIdLength = Convert.byte2Int(ArraysUtils.subarrays(content, 8, 1), 1);
            long gpsID = Convert.byte2Long(ArraysUtils.subarrays(content, 9, 3), 3);
            int fixedKeyLength = Convert.byte2Int(ArraysUtils.subarrays(content, 12, 1), 1);
            long fixedKey = Convert.byte2Long(ArraysUtils.subarrays(content, 13, 3), 3);
            int ecuType = Convert.byte2Int(ArraysUtils.subarrays(content, 16, 1), 1);
            int ecuVersion = Convert.byte2Int(ArraysUtils.subarrays(content, 17, 2), 1);
            int ecuRunStatus = Convert.byte2Int(ArraysUtils.subarrays(content, 19, 1), 1);
            int ecuActivationStatus = Convert.byte2Int(ArraysUtils.subarrays(content, 20, 1), 1);
            int ecuLockStatus = Convert.byte2Int(ArraysUtils.subarrays(content, 21, 1), 1);
            int key = Convert.byte2Int(ArraysUtils.subarrays(content, 22, 1), 1);
            int gpsIDBool = Convert.byte2Int(ArraysUtils.subarrays(content, 23, 1), 1);
            long ecuTotalhandshake = Convert.byte2Long(ArraysUtils.subarrays(content, 24, 4), 4);
            long ecuSuccesshandshake = Convert.byte2Long(ArraysUtils.subarrays(content, 28, 4), 4);
            int ecuLockErrorCode = Convert.byte2Int(ArraysUtils.subarrays(content, 32, 1), 1);
            log.error("终端ID：" + packet.getUniqueMark() + "时间：" + gpsdate + "   模式：" + pattern + "  标记:" + sign + "  gpsID长度：" + gpsIdLength + "  GPSID:" + gpsID +
                    "  固定密匙长度：" + fixedKeyLength + "  固定密匙：" + fixedKey + "  ECU类型：" + ecuType + "  ECU版本：" + ecuVersion +
                    "  ECU运行状态：" + ecuRunStatus + "  ECU激活状态：" + ecuActivationStatus + "  ECU锁车状态：" + ecuLockStatus + "  KEY:" + key +
                    "  GpsIDBool：" + gpsIDBool + "  ECU握手总次数：" + ecuTotalhandshake + "  ECU握手成功次数：" + ecuSuccesshandshake +
                    "  ECU锁车异常代码：" + ecuLockErrorCode);

            LCTerminalStatusChangeNotify.TerminalStatusChangeNotify.Builder builder = LCTerminalStatusChangeNotify.TerminalStatusChangeNotify.newBuilder();
            builder.setCurrentDate(gpsdate);
            builder.setPattern(pattern);
            builder.setSign(sign);
            builder.setGpsID(gpsID);
            builder.setFixedKey(fixedKey);
            builder.setEcuType(ecuType);
            builder.setEcuVersion(ecuVersion);
            builder.setEcuRunStatus(ecuRunStatus);
            builder.setEcuActivationStatus(ecuActivationStatus);
            builder.setEcuLockStatus(ecuLockStatus);
            builder.setKey(key);
            builder.setGpsIDBool(gpsIDBool);
            builder.setEcuTotalhandshake(ecuTotalhandshake);
            builder.setEcuSuccesshandshake(ecuSuccesshandshake);
            builder.setEcuLockErrorCode(ecuLockErrorCode);

            Packet _out_packet = new Packet();
            _out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TerminalStatusChangeNotify_VALUE);
            _out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
            _out_packet.setUniqueMark(packet.getUniqueMark());
            _out_packet.setSerialNumber(packet.getSerialNumber());
            _out_packet.setContent(builder.build().toByteArray());
            //super.writeToDataProcessing(_out_packet);
           // super.writeKafKaToDP(_out_packet, TopicConstants.JT_0F46_TerminalStatusChangeNotify);
           // return ;
            PacketResult packetResult = new PacketResult();
            packetResult.setKafkaPacket(_out_packet);
            packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS));
            return packetResult;
        } catch (Exception e) {
            log.error("解析0F46数据异常", e);
        }
        return null;
    }

}
