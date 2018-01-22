package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver;

import com.google.protobuf.InvalidProtocolBufferException;
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
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCInfoService;


import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2163")
public class DP_2163_InfoService extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.InfoService_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.InfoService);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCInfoService.InfoService infoService = LCInfoService.InfoService.parseFrom(packet.getContent());
			String infoName = infoService.getInfoContent();
			byte[] content = infoName.getBytes("gbk");
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(infoService.getInfoType(), 1));
			bytesArray.append(Convert.intTobytes(content.length, 2));
			bytesArray.append(content);
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.InfoService);
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
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
