
package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;

import com.google.protobuf.ByteString;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.query.LCTerminalRSAKey;

@LocationCommand(id = "0A00",version = "")
public class JT_0A00_TerminalRSAKey extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		PacketResult result=new PacketResult();
		log.debug("收到终端[ "+packet.getUniqueMark()+" ]终端RSA公钥.");
		byte[] bytes = packet.getContent();
		long eKey = Convert.byte2Long(ArraysUtils.subarrays(bytes, 0, 4), 4);
		byte [] nkey= ArraysUtils.subarrays(bytes, 4, 128);
		LCTerminalRSAKey.TerminalRSAKey.Builder builder = LCTerminalRSAKey.TerminalRSAKey.newBuilder();
		builder.setEKey(eKey);
		builder.setNKey(ByteString.copyFrom(nkey));
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TerminalRSAKey_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
		//return super.writeToDataProcessing(_out_packet);todo DP
        //super.writeKafKaToDP(_out_packet, TopicConstants.JT_0A00_TerminalRSAKey);
		result.setTerminalPacket(_out_packet);
		result.setKafkaPacket(_out_packet);
        return result;
	}

}
