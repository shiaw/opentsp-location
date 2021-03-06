
package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAnswerQuestion;

@LocationCommand(id = "0302")
public class JT_0302_AnswerQuestion extends TerminalCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		log.debug("收到终端[ "+packet.getUniqueMark()+" ]提问应答.");
		byte[] bytes = packet.getContent();
		int serialNumber = Convert.byte2Int(ArraysUtils.subarrays(bytes, 0, 2), 2);
		int id = Convert.byte2Int(ArraysUtils.subarrays(bytes, 2, 1), 1);
		LCAnswerQuestion.AnswerQuestion.Builder builder = LCAnswerQuestion.AnswerQuestion.newBuilder();
		builder.setAnswerId(id);
		//builder.setResult(ResponseResult.success);
		builder.setSerialNumber(serialNumber);
		
		Packet _out_packet = new Packet();
		_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.AnswerQuestion_VALUE);
		_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
		_out_packet.setUniqueMark(packet.getUniqueMark());
		_out_packet.setSerialNumber(packet.getSerialNumber());
		_out_packet.setContent(builder.build().toByteArray());;
		//return super.writeToDataProcessing(_out_packet);
        super.writeKafKaToDP(_out_packet, TopicConstants.JT_0302_AnswerQuestion);
        return null;
	}

}
