package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;

@LocationCommand(id = "0002")
public class JT_0002_TerminalHeartbeat extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		//收到终端心跳数据,暂不作处理,直接返回通用应答
		PacketResult result=new PacketResult();
		result.setTerminalPacket( this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(),
				Constant.JTProtocol.TerminalHeartbeat,
				LCResultCode.JTTerminal.SUCCESS));
		return result;
	}

}
