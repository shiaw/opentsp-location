package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.*;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPassType;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCUpPassThrough;

@LocationCommand(id = "0900")
public class JT_0900_UpPassThrough extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), packet.getCommand(), LCResultCode.JTTerminal.SUCCESS);
		
		byte[] content = packet.getContent();
		int type = (int) Convert.byte2Long(ArraysUtils.subarrays(content, 0, 1), 1);
		String passContent = StringUtils.bytesToGbkString(ArraysUtils.subarrays(content, 1, content.length - 1));
		
		LCUpPassThrough.UpPassThrough.Builder builder = LCUpPassThrough.UpPassThrough.newBuilder();
		//
		if(LCPassType.PassType.valueOf(type)==null){
			return null;
		}
		builder.setTypes(LCPassType.PassType.valueOf(type));
		builder.setPassContents(passContent);
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.UpPassThrough_VALUE);
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setContent(builder.build().toByteArray());
		//super.writeToDataProcessing(_out_packet);
		//super.writeKafKaToDP(_out_packet,"JT_0900_UpPassThrough");
		log.info("收到上行透传数据：类型："+type+",内容："+passContent);
		PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
		return  packetResult;
	}

}
