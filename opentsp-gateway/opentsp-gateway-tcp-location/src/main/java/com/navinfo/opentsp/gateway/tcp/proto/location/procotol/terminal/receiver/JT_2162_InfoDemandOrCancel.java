package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCInfoDemandOrCancel;

@LocationCommand(id = "2162")
public class JT_2162_InfoDemandOrCancel extends TerminalCommand {

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        byte[] bytes = packet.getContent();
        int infoType = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 1), 1);
        int infoStatus = Convert.byte2Int(ArraysUtils.subarrays(bytes, 1, 1), 1);

        LCInfoDemandOrCancel.InfoDemandOrCancel.Builder builder = LCInfoDemandOrCancel.InfoDemandOrCancel.newBuilder();
        builder.setInfoType(infoType);
        builder.setIsDemand(infoStatus == 0 ? false : true);

        Packet pk = new Packet();
        pk.setCommand(LCAllCommands.AllCommands.Terminal.InfoDemandOrCancel_VALUE);
        pk.setProtocol(LCConstant.LCMessageType.TERMINAL);
        pk.setUniqueMark(packet.getUniqueMark());
        pk.setSerialNumber(packet.getSerialNumber());
        pk.setContent(builder.build().toByteArray());
       // super.writeKafKaToDP(pk, TopicConstants.JT_2162_InfoDemandOrCancel);
        PacketResult packetResult=new PacketResult();
        packetResult.setKafkaPacket(pk);
        return packetResult; //super.writeToDataProcessing(pk);
    }

}
