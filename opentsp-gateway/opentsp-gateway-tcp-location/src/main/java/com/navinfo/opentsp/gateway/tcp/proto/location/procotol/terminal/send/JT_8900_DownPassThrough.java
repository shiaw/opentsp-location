package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCDownPassThrough;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "8900")
public class JT_8900_DownPassThrough extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
			LCDownPassThrough.DownPassThrough downPassThrough =
					LCDownPassThrough.DownPassThrough.parseFrom(packet.getContent());
			String content = downPassThrough.getPassContents();
			int types = downPassThrough.getTypes().getNumber();
			
			byte[] contentForByte = content.getBytes("GBK");
			int packetCapacity = 1+contentForByte.length;
			
			Packet outpacket = new Packet(packetCapacity);
			outpacket.setCommand(0x8900);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			
			outpacket.appendContent(Convert.longTobytes(types, 1));
			outpacket.appendContent(contentForByte);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return  packetResult;
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
