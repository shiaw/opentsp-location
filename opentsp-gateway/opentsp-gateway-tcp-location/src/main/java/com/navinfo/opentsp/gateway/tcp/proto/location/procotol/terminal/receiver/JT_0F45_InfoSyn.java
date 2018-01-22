package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

@LocationCommand(id = "0F45")
public class JT_0F45_InfoSyn extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
//		Command command = CommandCache.getInstance().getCommand("8F45");
//		if(command != null){
//			command.processor(packet);
//		}
		return new PacketResult();
	}

}
