package com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.receiver.terminalsetting;

import com.google.protobuf.InvalidProtocolBufferException;
import com.navinfo.opentsp.gateway.tcp.proto.location.cache.AnswerCommandCache;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.AnswerEntry;
import com.navinfo.opentsp.gateway.tcp.proto.location.pojo.PacketResult;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.LocationCommand;
import com.navinfo.opentsp.platform.location.kit.Packet;
import com.navinfo.opentsp.gateway.tcp.proto.location.procotol.dp.DPCommand;
import com.navinfo.opentsp.platform.location.kit.lang.ArraysUtils;
import com.navinfo.opentsp.platform.location.kit.lang.BytesArray;
import com.navinfo.opentsp.gateway.tcp.proto.location.util.Constant;
import com.navinfo.opentsp.platform.location.kit.Convert;
import com.navinfo.opentsp.gateway.tcp.server.NettyClientConnection;
import com.navinfo.opentsp.platform.location.protocol.common.LCAllCommands;
import com.navinfo.opentsp.platform.location.protocol.terminal.setting.parameter.LCMessageTimeoutProcess;
@LocationCommand(id = "2251")
public class DP_2251_MessageTimeoutProcess extends DPCommand {

	@Override
	public PacketResult processor(NettyClientConnection connection, Packet packet) {
		try {
			//缓存指令信息
			AnswerEntry answerEntry = new AnswerEntry();
			answerEntry.setInternalCommand(LCAllCommands.AllCommands.Terminal.MessageTimeoutProcess_VALUE);
			answerEntry.setSerialNumber(packet.getSerialNumber());
			answerEntry.setTerminalCommand(Constant.JTProtocol.TerminalSetting);
			answerEntry.setTimeout(ta_commandTimeoutThreshold);
			AnswerCommandCache.getInstance().addEntry(packet.getUniqueMark(), packet.getSerialNumber(), answerEntry);

			LCMessageTimeoutProcess.MessageTimeoutProcess messageTimeoutProcess = LCMessageTimeoutProcess.MessageTimeoutProcess.parseFrom(packet.getContent());
			int pnumber = 0;
			BytesArray bytesArray = new BytesArray();
			//终端心跳间隔
			if(messageTimeoutProcess.hasHeartbeatInterval()){
				bytesArray.append(Convert.longTobytes(1, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getHeartbeatInterval(), 4));
				pnumber++;
			}
			//TCP消息应答超时时间
			if(messageTimeoutProcess.hasTcpResponseTimeout()){
				bytesArray.append(Convert.longTobytes(2, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getTcpResponseTimeout(), 4));
				pnumber++;
			}
			//TCP消息重传次数
			if(messageTimeoutProcess.hasTcpRetransTimes()){
				bytesArray.append(Convert.longTobytes(3, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getTcpRetransTimes(), 4));
				pnumber++;
			}
			//UDP消息应答超时时间，单位秒
			if(messageTimeoutProcess.hasUdpResponseTimeout()){
				bytesArray.append(Convert.longTobytes(4, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getUdpResponseTimeout(), 4));
				pnumber++;
			}
			//UDP消息重传次数
			if(messageTimeoutProcess.hasUdpRetransTimes()){
				bytesArray.append(Convert.longTobytes(5, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getUdpRetransTimes(), 4));
				pnumber++;
			}
			//SMS消息应答超时时间，单位秒
			if(messageTimeoutProcess.hasSmsResponseTimeout()){
				bytesArray.append(Convert.longTobytes(6, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getSmsResponseTimeout(), 4));
				pnumber++;
			}
			//SMS消息重传次数
			if(messageTimeoutProcess.hasSmsRetransTimes()){
				bytesArray.append(Convert.longTobytes(7, 4));
				bytesArray.append(Convert.longTobytes(4, 1));
				bytesArray.append(Convert.longTobytes(messageTimeoutProcess.getSmsRetransTimes(), 4));
				pnumber++;
			}
			byte[] bytes = ArraysUtils.arraycopy(Convert.intTobytes(pnumber, 1), bytesArray.get());
			
			packet.setCommand(Constant.JTProtocol.TerminalSetting);
			packet.setContent(bytes);
			//return super.writeToTerminal(packet);
			PacketResult packetResult=new PacketResult();
			packetResult.setTerminalPacket(packet);
            return packetResult;
		} catch (InvalidProtocolBufferException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
