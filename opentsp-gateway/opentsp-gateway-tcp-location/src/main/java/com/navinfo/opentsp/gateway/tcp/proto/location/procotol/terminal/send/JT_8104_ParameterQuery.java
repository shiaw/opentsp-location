package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.send;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

@LocationCommand(id = "8104")
public class JT_8104_ParameterQuery extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		// TODO Auto-generated method stub
		packet.setCommand(0x8104);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
		return packetResult;
	}

}
