package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
@LocationCommand(id = "2400")
public class DP_2400_TerminalShutdown extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.TerminalShutdown_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalControl);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		BytesArray bytesArray = new BytesArray();
		bytesArray.append(Convert.intTobytes(3, 1));//终端关机
		
		Packet outpacket = new Packet();
		outpacket.setSerialNumber(packet.getSerialNumber());
		outpacket.setCommand(Constant.JTProtocol.TerminalControl);
		outpacket.setUniqueMark(packet.getUniqueMark());
		outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
		outpacket.setContent(bytesArray.get());
	//	return super.writeToTerminal(outpacket);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(outpacket);
		return packetResult;
	}

}
