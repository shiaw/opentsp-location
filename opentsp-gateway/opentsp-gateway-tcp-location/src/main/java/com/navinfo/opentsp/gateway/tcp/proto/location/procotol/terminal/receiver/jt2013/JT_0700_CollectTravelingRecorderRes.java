package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver.jt2013;

import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.LCResultCode;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
@LocationCommand(id = "0700" )
public class JT_0700_CollectTravelingRecorderRes extends TerminalCommand {
	
	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		PacketResult result=new PacketResult();
		byte[] content = packet.getContent();
		int cmd = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 1), 1);
		//TRFactory.processor(cmd, 2012, packet);
		result.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(),
				Constant.JTProtocol.TerminalTachographDataReported,
				LCResultCode.JTTerminal.SUCCESS));
		return result;
	}
}
