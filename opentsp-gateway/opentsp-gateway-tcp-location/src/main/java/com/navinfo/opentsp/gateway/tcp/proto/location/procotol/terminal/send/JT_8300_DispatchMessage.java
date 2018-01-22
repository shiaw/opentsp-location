package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDispatchMessage;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;

@LocationCommand(id = "8300")
public class JT_8300_DispatchMessage extends TerminalCommand {

    @Override
    public PacketResult processor(NettyClientConnection connection, Packet packet) {
        // TODO Auto-generated method stub
        try {
//            LCDispatchMessage.DispatchMessage dispatchMessage = LCDispatchMessage.
//                    DispatchMessage.parseFrom(packet.getContent());

            String messageContent = packet.getPushArgumentByKey("message") == null ? "" : packet.getPushArgumentByKey("message").toString();
            int signs = 1;//dispatchMessage.getSigns().getNumber();

            byte[] message = messageContent.getBytes("GBK");
            int capacity = 1 + message.length;

            Packet outPacket = new Packet(capacity);
            outPacket.setCommand(33536);
            outPacket.setSerialNumber(packet.getSerialNumber());
            outPacket.setUniqueMark(packet.getUniqueMark());

            outPacket.appendContent(Convert.longTobytes(signs, 1));
            outPacket.appendContent(message);
            PacketResult packetResult=new PacketResult();
            packetResult.setTerminalPacket(outPacket);
            return packetResult;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
