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
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCPhotoResult;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCTakePhotographyRes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@LocationCommand(id = "0001")
public class JT_0001_TerminalCommonRes extends TerminalCommand {
	static Map<Integer, LCResponseResult.ResponseResult> ResponseResultMapping = new ConcurrentHashMap<Integer, LCResponseResult.ResponseResult>();
	static{
		ResponseResultMapping.put(0, LCResponseResult.ResponseResult.success);
		ResponseResultMapping.put(1, LCResponseResult.ResponseResult.failure);
		ResponseResultMapping.put(2, LCResponseResult.ResponseResult.failure);
		ResponseResultMapping.put(3, LCResponseResult.ResponseResult.notSupport);
	}

	@Override
	public PacketResult processor(NettyClientConnection connection,Packet packet) {
		PacketResult packetResult=new PacketResult();
		byte[] content = packet.getContent();
		int responsesSerialNumber = Convert.byte2Int(ArraysUtils.subarrays(content, 0, 2), 2);
//		int responsesId = Convert.byte2Int(ArraysUtils.subarrays(content, 2, 2), 2);
		int result = Convert.byte2Int(ArraysUtils.subarrays(content, 4, 1), 1);
		AnswerEntry answerEntry = AnswerCommandCache.getInstance().getAnswerEntry(packet.getUniqueMark(), responsesSerialNumber, true);
		if(answerEntry != null){
			if(answerEntry.getInternalCommand() == LCAllCommands.AllCommands.Terminal.ParameterQuery_VALUE)
				//return packet;
			//将2011拍照的通用应答转换为2013的拍照应答
            {
                if (answerEntry.getInternalCommand() == LCAllCommands.AllCommands.Terminal.TakePhotography_VALUE) {
                    LCTakePhotographyRes.TakePhotographyRes.Builder builder = LCTakePhotographyRes.TakePhotographyRes.newBuilder();
                    if (result == 0) {
                        result = 0;
                    } else if (result == 1 || result == 2) {
                        result = 1;
                    } else if (result == 3) {
                        result = 2;
                    }
                    builder.setResult(LCResponseResult.ResponseResult.valueOf(result));
                    builder.setResults(LCPhotoResult.PhotoResult.valueOf(result));
                    builder.setSerialNumber(responsesSerialNumber);

                    Packet _out_packet = new Packet();
                    _out_packet.setCommand(LCAllCommands.AllCommands.Terminal.TakePhotographyRes_VALUE);
                    _out_packet.setProtocol(LCConstant.LCMessageType.TERMINAL);
                    _out_packet.setUniqueMark(packet.getUniqueMark());
                    _out_packet.setSerialNumber(packet.getSerialNumber());
                    _out_packet.setContent(builder.build().toByteArray());
                    //super.writeToDataProcessing(_out_packet);
                    packetResult.setTerminalPacket(_out_packet);
                    return packetResult;
                }
            }

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
			//super.writeToDataProcessing(_out_packet);
			super.sendCommandStatus(packet.getUniqueMark(),responsesSerialNumber,_out_packet);
            //super.writeKafKaToDP(_out_packet, TopicConstants.POSRAW);
			packetResult.setKafkaPacket(_out_packet);
			//packetResult.setMqCommand();
			return packetResult;

		}
		return null;
	}

}
