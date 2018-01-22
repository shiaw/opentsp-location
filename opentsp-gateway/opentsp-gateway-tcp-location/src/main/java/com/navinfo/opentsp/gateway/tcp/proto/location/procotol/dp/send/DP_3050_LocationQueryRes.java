package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.send;


import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

public class DP_3050_LocationQueryRes extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		return null;
	}

}
