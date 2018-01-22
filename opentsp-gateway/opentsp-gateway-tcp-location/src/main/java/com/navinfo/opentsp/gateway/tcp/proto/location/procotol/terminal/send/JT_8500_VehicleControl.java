package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCTerminalControl;
@LocationCommand(id = "8500")
public class JT_8500_VehicleControl extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		// TODO Auto-generated method stub
		try {
			LCTerminalControl.TerminalControl terminalControl =
					LCTerminalControl.TerminalControl.parseFrom(packet.getContent());
		    int controlStatus = terminalControl.getControlStatus();
		    
		    Packet outPacket = new Packet(1);
		    
		    outPacket.setCommand(0x8500);
		    outPacket.setSerialNumber(packet.getSerialNumber());
		    outPacket.setUniqueMark(packet.getUniqueMark());
		    outPacket.appendContent(Convert.longTobytes(controlStatus, 1));

			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			log.error(e.getMessage());
			e.printStackTrace();
		}
		
		return null;
	}

}
