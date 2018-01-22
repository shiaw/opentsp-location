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
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands.AllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCCallListener;


import java.io.UnsupportedEncodingException;
@LocationCommand(id = "2152")
public class DP_2152_CallListener extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		//缓存指令信息
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(AllCommands.Terminal.CallListener_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.CallListener);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);
		
		try {
			LCCallListener.CallListener callListener =
					LCCallListener.CallListener.parseFrom(packet.getContent());
            String phoneNumber = callListener.getPhoneNumber();
            int status = callListener.getStatus().getNumber();
            byte[] phone = phoneNumber.getBytes("GBK");
            int capacity = 1 + phone.length;
            Packet outPacket = new Packet(capacity);
            outPacket.setCommand(Constant.JTProtocol.CallListener);
            outPacket.setSerialNumber(packet.getSerialNumber());
            outPacket.setUniqueMark(packet.getUniqueMark());
            outPacket.appendContent(Convert.longTobytes(status, 1));
            outPacket.appendContent(phone);
           // return super.writeToTerminal(outPacket);
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
