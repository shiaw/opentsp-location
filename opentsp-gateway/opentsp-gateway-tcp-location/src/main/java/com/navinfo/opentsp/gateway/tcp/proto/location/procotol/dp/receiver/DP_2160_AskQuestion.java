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
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAskQuestion;
import com.navinfo.opentsp.platform.location.protocol.terminal.monitor.LCAskQuestion.AskQuestion.QuestionStatus;


import java.io.UnsupportedEncodingException;
import java.util.List;
@LocationCommand(id = "2160")
public class DP_2160_AskQuestion extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		AnswerEntry answerEntry = new AnswerEntry();
		answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.AskQuestion_VALUE);
		answerEntry.setSerialNumber(packet.getSerialNumber());
		answerEntry.setTerminalCommand(Constant.JTProtocol.AskQuestion);
		answerEntry.setTimeout(ta_commandTimeoutThreshold);
		AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

		try {
			LCAskQuestion.AskQuestion askQuestion = LCAskQuestion.AskQuestion.parseFrom(packet.getContent());
			List<QuestionStatus> status = askQuestion.getStatusList();
			List<LCAskQuestion.AskQuestion.CandidateAnswer> answers = askQuestion.getAnswersList();
			int statusNumber = 0;
			for (QuestionStatus questionStatus : status) {
				if(questionStatus.getNumber() == QuestionStatus.emergency_VALUE){
					statusNumber|=1;
				}
				if(questionStatus.getNumber() == QuestionStatus.terminalTTS_VALUE){
					statusNumber|=8;
				}
				if(questionStatus.getNumber() == QuestionStatus.displayOnScreen_VALUE){
					statusNumber|=16;
				}
			}
			byte[] content = askQuestion.getContent().getBytes("gbk");
			BytesArray bytesArray = new BytesArray();
			bytesArray.append(Convert.intTobytes(statusNumber, 1));
			bytesArray.append(Convert.intTobytes(content.length, 1));
			bytesArray.append(content);
			for (LCAskQuestion.AskQuestion.CandidateAnswer candidateAnswer : answers) {
				byte[] answersContent = candidateAnswer.getAnswerContent().getBytes("gbk");
				bytesArray.append(Convert.intTobytes(candidateAnswer.getAnswerId(), 1));
				bytesArray.append(Convert.intTobytes(answersContent.length , 2));
				bytesArray.append(answersContent);
			}
			Packet outpacket = new Packet();
			outpacket.setSerialNumber(packet.getSerialNumber());
			outpacket.setCommand(Constant.JTProtocol.AskQuestion);
			outpacket.setUniqueMark(packet.getUniqueMark());
			outpacket.setTo(Long.parseLong(packet.getUniqueMark()));
			outpacket.setContent(bytesArray.get());
			//return super.writeToTerminal(outpacket);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(outpacket);
			return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

}
