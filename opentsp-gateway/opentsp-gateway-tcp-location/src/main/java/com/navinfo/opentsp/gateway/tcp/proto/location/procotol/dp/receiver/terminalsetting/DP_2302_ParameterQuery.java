package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;

/**
 *删除圆形区域
 * @author jin_s
 *
 */
public class DP_2302_ParameterQuery extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.ReportDriverReq_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.DriverInfoUpPassThrough);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		Packet outpacket = new Packet();
		outpacket.setSerialNumber(packet.getSerialNumber());
		outpacket.setCommand(Constant.JTProtocol.DriverInfoUpPassThrough);
		outpacket.setUniqueMark(packet.getUniqueMark());
		outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
		
	    //super.writeToTerminal(outpacket);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(outpacket);
		return packetResult;
	}

}
