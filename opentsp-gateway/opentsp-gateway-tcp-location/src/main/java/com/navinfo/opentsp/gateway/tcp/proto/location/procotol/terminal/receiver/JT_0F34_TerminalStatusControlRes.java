package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.receiver;import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;


import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.TopicConstants;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.terminal.TerminalCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.platform.location.kit.LCConstant;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.common.LCResponseResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.LCDownCommonRes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@LocationCommand(id = "0F34")
public class JT_0F34_TerminalStatusControlRes extends TerminalCommand {
	static Map<Integer, LCResponseResult.ResponseResult> ResponseResultMapping = new ConcurrentHashMap<Integer, LCResponseResult.ResponseResult>();
	static{
		ResponseResultMapping.put(0, LCResponseResult.ResponseResult.success);
		ResponseResultMapping.put(1, LCResponseResult.ResponseResult.failure);
		ResponseResultMapping.put(2, LCResponseResult.ResponseResult.failure);
		ResponseResultMapping.put(3, LCResponseResult.ResponseResult.notSupport);
	}

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		byte[] content = packet.getContent();
		int responsesSerialNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);//应答流水号
//		int responsesId = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 2), 2);//发出时 已缓存在AnswerCommandCache中
		int result = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);//结果 4-5字节
		AnswerEntry answerEntry = AnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), responsesSerialNumber, true);
		
		if(answerEntry != null){
			LCDownCommonRes.DownCommonRes.Builder builder = LCDownCommonRes.DownCommonRes.newBuilder();
			builder.setResponseId(answerEntry.getInternalCommand());
			builder.setSerialNumber(responsesSerialNumber);
			builder.setResult(ResponseResultMapping.get(result));
			
			Packet _out_packet = new Packet();
			_out_packet.setCommand(LCAllCommands.AllCommands.Terminal.DownCommonRes_VALUE);
			_out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
			_out_packet.setUniqueMark(packet.getUniqueMark());
			_out_packet.setSerialNumber(packet.getSerialNumber());
			_out_packet.setContent(builder.build().toByteArray());
			super.sendCommandStatus(packet.getUniqueMark(),responsesSerialNumber,_out_packet);
		    super.writeKafKaToDP(_out_packet,TopicConstants.POSRAW);
		}

		return null;
	}
}
