package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.send;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

public class DP_3002_LocationReport extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//System.out.println("3002 test ===============================================");
		//this.write(packet);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
		return packetResult;
	}

}
