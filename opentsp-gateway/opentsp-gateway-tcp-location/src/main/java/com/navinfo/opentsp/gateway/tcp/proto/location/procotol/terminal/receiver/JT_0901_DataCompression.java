package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.LCResultCode;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCDataCompression;

/**
 *数据压缩上报3132
 * @author admin
 *
 */
public class JT_0901_DataCompression extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		
		byte[] content = packet.getContent();
		int contentLength = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 4),
                4);
		LCDataCompression.DataCompression.Builder builder = LCDataCompression.DataCompression.newBuilder();
		
		byte[] messageBody = ArraysUtils.subarrays(content, 4,  contentLength);
		builder.setMessageBody(ByteString.copyFrom(messageBody));
		
		
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.DataCompression_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
	//	super.writeToDataProcessing(_out_packet);
    //  super.writeKafKaToDP(_out_packet, TopicConstants.JT_0901_DataCompression);
		PacketResult packetResult=new PacketResult();
		packetResult.setKafkaPacket(_out_packet);
		packetResult.setTerminalPacket(this.commonResponses(packet.getUniqueMark(), packet.getSerialNumber(), 0x0800, LCResultCode.JTTerminal.SUCCESS));
		return  packetResult;

	}
}
