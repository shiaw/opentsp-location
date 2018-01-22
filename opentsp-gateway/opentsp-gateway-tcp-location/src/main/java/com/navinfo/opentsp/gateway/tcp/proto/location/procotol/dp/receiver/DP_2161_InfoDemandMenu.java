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
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCInfoDemandMenu;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCInfoDemandMenu.InfoDemandMenu.DemandInfo;


import java.io.UnsupportedEncodingException;
import java.util.List;
@LocationCommand(id = "2161")
public class DP_2161_InfoDemandMenu extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.InfoDemandMenu_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.InfoDemandMenu);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCInfoDemandMenu.InfoDemandMenu demandMenu = LCInfoDemandMenu.InfoDemandMenu.parseFrom(packet.getContent());
			List<DemandInfo> demandInfos = demandMenu.getInfosList();
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(demandMenu.getTypes().getNumber(), 1));
			bytesArray.append(Convert.intTobytes(demandMenu.getInfosCount(), 1));
			for (DemandInfo demandInfo : demandInfos) {
				byte[] content = demandInfo.getInfoName().getBytes("gbk");
				bytesArray.append(Convert.intTobytes(demandInfo.getInfoType(), 1));
				bytesArray.append(Convert.intTobytes(content.length, 2));
				bytesArray.append(content);
			}
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.InfoDemandMenu);
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
