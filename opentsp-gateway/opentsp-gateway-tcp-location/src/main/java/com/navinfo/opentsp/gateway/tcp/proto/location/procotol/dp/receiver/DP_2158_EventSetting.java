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
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCEventSetting;


import java.io.UnsupportedEncodingException;
import java.util.List;
@LocationCommand(id = "2158")
public class DP_2158_EventSetting extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(AllCommands.Terminal.EventSetting_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.EventSetting);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCEventSetting.EventSetting eventSetting = LCEventSetting.EventSetting.parseFrom(packet.getContent());
			List<LCEventSetting.EventSetting.EventObject> eventObjects = eventSetting.getEventsList();
			
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(eventSetting.getControlStatus().getNumber(), 1));
			bytesArray.append(Convert.intTobytes(eventSetting.getEventsCount(), 1));
			for(int i = 0 ; i < eventObjects.size() ; i ++){
				System.err.println(eventObjects.get(i).getEventContent());
				byte[] eventConent;
				try {
					eventConent = eventObjects.get(i).getEventContent().getBytes("GBK");
					bytesArray.append(Convert.intTobytes(eventObjects.get(i).getEventId(), 1));
					bytesArray.append(Convert.intTobytes(eventConent.length, 1));
					bytesArray.append(eventConent);
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
			}
			
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.EventSetting);
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
