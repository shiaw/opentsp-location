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
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCAudioRate;
import com.navinfo.opentsp.platform.location.protocol.terminal.common.LCSaveStatus;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCRecordStartCommand;

import static com.navinfo.opentsp.platform.location.protocol.terminal.common.LCAudioRate.*;

@LocationCommand(id = "2168")
public class DP_2168_RecordStartCommand extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.RecordStartCommand_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.RecordStartCommand);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCRecordStartCommand.RecordStartCommand recordStartCommand = LCRecordStartCommand.RecordStartCommand.parseFrom(packet.getContent());

//			rates	音频采样率
			AudioRate rates=recordStartCommand.getRates();
			BytesArray bytesArray = new BytesArray();
//			isRecord	是否开始录音；true开始录音；false停止录音
			bytesArray.append(Convert.intTobytes(recordStartCommand.getIsRecord() == true ? 1 : 0, 1));

//			isAlways	是否一直录音；true一直录音；如果为true，录音时长字段无效；//			recordTime	录音时长
			//bytesArray.append(Convert.intTobytes(recordStartCommand.getIsAlways()==true?0:recordStartCommand.getRecordTime(), 2));
			bytesArray.append(Convert.intTobytes(10, 2));
//			status	存储标识，见附录，枚举
			bytesArray.append(Convert.intTobytes(recordStartCommand.getStatus()== LCSaveStatus.SaveStatus.terminalSave?1:0 , 1));

			switch (rates.getNumber()) {
			case AudioRate.rate_8k_VALUE:
				bytesArray.append(Convert.intTobytes(0 , 1));
				break;
            case AudioRate.rate_11k_VALUE:
            	bytesArray.append(Convert.intTobytes(1 , 1));
				break;
            case AudioRate.rate_23k_VALUE:
            	bytesArray.append(Convert.intTobytes(2 , 1));
				break;
            case AudioRate.rate_32k_VALUE:
            	bytesArray.append(Convert.intTobytes(3 , 1));
				break;
			default:
				bytesArray.append(Convert.intTobytes(100 , 1));
				break;
			}
			Packet outpacket = new Packet();
			outpacket.setCommand(Constant.JTProtocol.RecordStartCommand);
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
		//	return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}

}
