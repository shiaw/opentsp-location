package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.platform.location.kit.lang.DateUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCMediaType.MediaType;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCMediaDataQuery.MediaDataQuery;

@LocationCommand(id = "2165")
public class DP_2165_MediaDataQuery extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.MediaDataQuery_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.MediaDataQuery);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			MediaDataQuery mediaDataQuery = MediaDataQuery.parseFrom(packet.getContent());
			MediaType types = mediaDataQuery.getTypes();
			
			
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(types.getNumber(), 1));
			bytesArray.append(Convert.intTobytes(mediaDataQuery.getIsAllChannels()==true?0:mediaDataQuery.getChannels(), 1));
			bytesArray.append(Convert.intTobytes(mediaDataQuery.getEvents().getNumber(), 1));
			
			bytesArray.append(Convert.hexStringToBytes(DateUtils.format(mediaDataQuery.getBeginDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
			bytesArray.append(Convert.hexStringToBytes(DateUtils.format(mediaDataQuery.getEndDate(), DateUtils.DateFormat.YYMMDDHHMMSS)));
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.MediaDataQuery);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
