package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCDownPassThrough;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2131")
public class DP_2131_DownPassThrough extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.DownPassThrough_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalDownPassThrough);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		try {
			LCDownPassThrough.DownPassThrough downPassThrough =
					LCDownPassThrough.DownPassThrough.parseFrom(packet.getContent());
			String content = downPassThrough.getPassContents();
			int types = downPassThrough.getTypes().getNumber();
			
			byte[] contentForByte = content.getBytes("GBK");
			int packetCapacity = 1+contentForByte.length;
			
			Packet outpacket = new Packet(packetCapacity);
			outpacket.setCommand(Constant.JTProtocol.DownPassThrough);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.appendContent(Convert.longTobytes(types, 1));
			outpacket.appendContent(contentForByte);
		//	return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
            return  packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
