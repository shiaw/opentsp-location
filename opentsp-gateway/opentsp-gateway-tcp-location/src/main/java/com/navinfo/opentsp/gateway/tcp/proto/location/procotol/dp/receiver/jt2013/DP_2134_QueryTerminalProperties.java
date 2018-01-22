package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.jt2013;


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
 * 查询指定终端参数
 * @author jin_s
 *
 */
@LocationCommand(id = "2134")
public class DP_2134_QueryTerminalProperties extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.QueryTerminalProperty_VALUE);// ************
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.QueryTerminalProperties);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		//TerminalPropertyAnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), LCAllCommands.AllCommands.Terminal.QueryTerminalProperty_VALUE, packet.getSerialNumber());
		Packet outpacket = new Packet();
		outpacket.setSerialNumber(packet.getSerialNumber());
		outpacket.setCommand(Constant.JTProtocol.QueryTerminalProperties);
		outpacket.setUniqueMark(packet.getUniqueMark());
		outpacket.setTo(Long.parseLong(packet.getUniqueMark()));

		//return super.writeToTerminal(outpacket);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(outpacket);
		return packetResult;
	}

}
