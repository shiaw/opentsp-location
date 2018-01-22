package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCCallListener;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "8400")
public class JT_8400_CallListener extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet){
		// TODO Auto-generated method stub
		try {
			LCCallListener.CallListener callListener =
					LCCallListener.CallListener.parseFrom(packet.getContent());
            String phoneNumber = callListener.getPhoneNumber();
            int status = callListener.getStatus().getNumber();
            
            byte[] phone = phoneNumber.getBytes("GBK");
            int capacity = 1 + phone.length;
            
            Packet outPacket = new Packet(capacity);
            outPacket.setCommand(0x8400);
            outPacket.setSerialNumber(packet.getSerialNumber());
            outPacket.setUniqueMark(packet.getUniqueMark());
            
            outPacket.appendContent(Convert.longTobytes(status, 1));
            outPacket.appendContent(phone);

			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
