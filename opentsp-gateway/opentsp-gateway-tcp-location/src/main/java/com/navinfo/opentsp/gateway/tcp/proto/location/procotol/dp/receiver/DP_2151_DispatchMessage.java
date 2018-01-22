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
import com.navinfo.opentsp.platform.location.protocol.common.LCMessageSign;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCDispatchMessage;

import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2151")
public class DP_2151_DispatchMessage extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.DispatchMessage_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalDispatchMessage);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
//		packet.setCommand(Constant.JTProtocol.TerminalDispatchMessage);
//		this.forwardTermianl(packet);
		
		try {
			LCDispatchMessage.DispatchMessage dispatchMessage = LCDispatchMessage.
					DispatchMessage.parseFrom(packet.getContent());
			String messageContent = dispatchMessage.getMessageContent();
			LCMessageSign.MessageSign messageSign = dispatchMessage.getSigns();
			String[] arrays = new String[]{"0","0","0","0","0","0","0","0"};
			if(messageSign.getIsUrgent()){//紧急
				arrays[0] = "1";
			}
			if(messageSign.getIsDisplay()){//终端显示器显示
				arrays[2] = "1";
			}
			if(messageSign.getIsBroadcast()){//终端TTS播报 
				arrays[3] = "1";
			}
			if(messageSign.getIsAdvertiseScreen()){//广告屏显示
				arrays[4] = "1";
			}
			if(!messageSign.getInfoType()){//true:中心导航信息；false：CAN故障码信息；
				arrays[5] = "1";
			}
			String s = "";
			for (String string : arrays) {
				s = string+s;
			}
			int signs = Integer.parseInt(s, 2);
			byte[] message = messageContent.getBytes("GBK");
			int capacity = 1 + message.length;
			Packet outPacket = new Packet(capacity);
			outPacket.setCommand(Constant.JTProtocol.TerminalDispatchMessage);
			outPacket.setSerialNumber(packet.getSerialNumber());
			outPacket.setUniqueMark(packet.getUniqueMark());
			outPacket.appendContent(Convert.longTobytes(signs, 1));
			outPacket.appendContent(message);
			//return super.writeToTerminal(outPacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outPacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
