package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
@LocationCommand(id = "2155")
public class DP_2155_MultimediaUploadRes extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setChannelId(packet.getChannels());
		answerEntry.setInternalCommand(0x2155);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.MultimediaUploadRes);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		packet.setCommand(Constant.JTProtocol.MultimediaUploadRes);
		PacketResult packetResult=new PacketResult();
		packetResult.setTerminalPacket(packet);
		return packetResult;
		//return super.writeToTerminal(packet);
	}

}
